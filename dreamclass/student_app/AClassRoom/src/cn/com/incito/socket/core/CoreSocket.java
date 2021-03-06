package cn.com.incito.socket.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import android.util.Log;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 客户端Socket
 *
 * @author 刘世平
 */
public final class CoreSocket implements Runnable {
	private static CoreSocket instance = null;
	private boolean isRunning = false;
	private boolean isConnected = false;
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

	public boolean isConnected() {
		return isConnected;
	}

	private void handle(SelectionKey selectionKey) {
		if (selectionKey.isConnectable()) {// 连接建立事件，已成功连接至服务器
			channel = (SocketChannel) selectionKey.channel();
			try {
				if (channel.isConnectionPending()) {
					channel.finishConnect();
					isConnected = true;
					// 发送设备登陆消息
					sendDeviceLoginMessage();
				}
				channel.register(selector, SelectionKey.OP_READ);// 注册读事件
			} catch (IOException e) {
				ApiClient.uploadErrorLog(e.getMessage());
				isConnected = false;
				MyApplication.Logger.debug("CoreSocket", e);
			}
		} else if (selectionKey.isReadable()) {// 若为可读的事件，则进行消息解析
			MessageParser messageParser = new MessageParser();
			messageParser.parseMessage(selectionKey);
		}
	}

	// 发送设备登陆消息至服务器
	private void sendDeviceLoginMessage() {
		MyApplication.Logger.info("CoreSocket 发送设备登陆");
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		byte[] handSharkData = messagePacking.pack().array();
		ByteBuffer buffer = ByteBuffer.allocate(handSharkData.length);
		buffer.put(handSharkData);
		buffer.flip();
		try {
			channel.write(buffer);
		} catch (IOException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			isConnected = false;
			disconnect();
			MyApplication.Logger.debug("CoreSocket 设备登陆消息发送失败", e);
			MyApplication.Logger.debug(Utils.getTime()+"异常信息：" , e);
			ConnectionManager.getInstance().close(Boolean.FALSE);
		}
	}

	/**
	 * 客户端往服务端发送消息
	 *
	 * @param packing
	 */
	public void sendMessage(final MessagePacking packing) {
		new Thread() {
			public void run() {
				byte[] message = packing.pack().array();
				ByteBuffer buffer = ByteBuffer.allocate(message.length);
				buffer.put(message);
				buffer.flip();
				try {
					channel.write(buffer);
				} catch (IOException e) {
					ApiClient.uploadErrorLog(e.getMessage());
					MyApplication.Logger.debug(Utils.getTime()+"客户端往服务端发送消息异常信息：" , e);
				}
			}
		}.start();

	}

	public void stopConnection() {
		new Thread() {
			public void run() {
				if (channel != null) {
					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_LOGOUT);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("imei", MyApplication.deviceId);
					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
					byte[] logoutData = messagePacking.pack().array();
					ByteBuffer buffer = ByteBuffer.allocate(logoutData.length);
					buffer.put(logoutData);
					buffer.flip();
					try {
						channel.write(buffer);
						ConnectionManager.getInstance().close(true);
					} catch (IOException e) {
						ApiClient.uploadErrorLog(e.getMessage());
						MyApplication.Logger.debug(Utils.getTime()+"异常信息：" , e);
					}
				}
			}
		}.start();
	}

	public void restartConnection() {
		disconnect();
		new Thread(this).start();
	}

	public void disconnect() {
		isRunning = false;
	}

	@Override
	public void run() {
		try {
			MyApplication.Logger.debug("CoreSocket CoreSocket开始检查mac地址 ");
			if (MyApplication.deviceId == null
					|| MyApplication.deviceId.equals("")) {
				//没有mac地址,不允许建立连接
				MyApplication.Logger.debug(Utils.getTime()+"CoreSocket：连接建立失败，没有mac地址");
				Log.i("CoreSocket", "连接建立失败，没有mac地址");
				return;
			}
			MyApplication.Logger.debug(Utils.getTime()+"CoreSocket：CoreSocket开始建立连接");
			Log.i("CoreSocket", "有mac地址，CoreSocket开始建立连接 ");
			isRunning = true;
			// 客户端向服务器端发起建立连接请求
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.socket().setSoTimeout(10000);
			selector = Selector.open().wakeup();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			socketChannel.connect(new InetSocketAddress(Constants.IP, Constants.PORT));
			MyApplication.Logger.debug(Utils.getTime()+"CoreSocket：CoreSocket开始建立连接");
			while (isRunning) {// 轮询监听客户端上注册事件的发生
				selector.select(300);
				Set<SelectionKey> keySet = selector.selectedKeys();
				for (final SelectionKey key : keySet) {
					handle(key);
				}
				keySet.clear();
			}
			MyApplication.Logger.debug(Utils.getTime()+"CoreSocket退出!");
			Log.i("CoreSocket", "CoreSocket退出!");
		} catch (IOException e) {
			MyApplication.Logger.debug(Utils.getTime()+"异常信息：" + e);
		}
	}

}
