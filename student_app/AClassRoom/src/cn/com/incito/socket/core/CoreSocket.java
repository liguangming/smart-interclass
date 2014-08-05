package cn.com.incito.socket.core;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 客户端Socket
 *
 * @author 刘世平
 */
public final class CoreSocket extends Thread {
    private static CoreSocket instance = null;
    private Selector selector;
    private SocketChannel channel;

    public static CoreSocket getInstance() {
        if (instance == null) {
            instance = new CoreSocket();
        }
        return instance;
    }

    private CoreSocket() {

    }

    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isConnectable()) {//连接建立事件，已成功连接至服务器
            channel = (SocketChannel) selectionKey.channel();
            if (channel.isConnectionPending()) {
                channel.finishConnect();
                //发送握手消息
                byte[] handSharkData = getHandShakeMessage();
                ByteBuffer buffer = ByteBuffer.allocate(handSharkData.length);
                buffer.put(handSharkData);
                buffer.flip();
                channel.write(buffer);// 发送握手消息至服务器
            }
            channel.register(selector, SelectionKey.OP_READ);// 注册读事件
        } else if (selectionKey.isReadable()) {// 若为可读的事件，则进行消息解析
            MessageParser messageParser = new MessageParser();
            messageParser.parseMessage(selectionKey);
        }
    }

    private byte[] getHandShakeMessage() {
        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_HAND_SHAKE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", MyApplication.deviceId);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
        return messagePacking.pack().array();
    }

    /**
     * 客户端往服务端发送消息
     *
     * @param packing
     */
    public void sendMessage(final MessagePacking packing, MessageHandler messageHandler) {
        MessageHandlerResource.getHandlerResources().putHandlerResource(packing.msgId, messageHandler);
        new Thread() {
            public void run() {

                byte[] message = packing.pack().array();
                ByteBuffer buffer = ByteBuffer.allocate(message.length);
                buffer.put(message);
                buffer.flip();
                try {
                    channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void stopConnection() {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //客户端向服务器端发起建立连接请求
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(Constants.IP, Constants.PORT));
            while (true) {//轮询监听客户端上注册事件的发生
                selector.select();
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (final SelectionKey key : keySet) {
                    handle(key);
                }
                keySet.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (channel == null) {
            return false;
        }
        return channel.isConnected();
    }

}
