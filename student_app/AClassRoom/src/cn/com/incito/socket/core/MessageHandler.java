package cn.com.incito.socket.core;

import android.os.Bundle;

import java.nio.ByteBuffer;

import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 消息对应的处理器接口
 * 存放消息对应的处理逻辑提供共有的抽象接口方法
 *
 * @author 刘世平
 */
public abstract class MessageHandler {

    protected Message messageInfo;

    /**
     * 消息对应的处理器 存放消息对应的处理逻辑，在消息分发时使用
     *
     * @param msg 被处理消息
     */
    public final void handleMessage(Message msg) {
        this.messageInfo = msg;
        try {
            ByteBuffer buffer = msg.getBodyBuffer();
            buffer.flip();

            // 获取JSON数据
            byte[] intSize = new byte[4];// int
            buffer.get(intSize);
            int jsonLength = Integer.parseInt(BufferUtils.decodeIntLittleEndian(
                    intSize, 0, intSize.length) + "");
            byte[] jsonByte = new byte[jsonLength];
            buffer.get(jsonByte);
            String json = BufferUtils.readUTFString(jsonByte);
            JSONObject data = JSON.parseObject(json);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            handleMessage(bundle);
        } catch (Exception e) {
            return;
        }


    }

    protected abstract void handleMessage(Bundle data);


}
