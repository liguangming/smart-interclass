package cn.com.incito.server.core;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import cn.com.incito.server.exception.NoHandlerException;

/**
 * 通用消息类
 * 
 * @author 刘世平
 * 
 */
public class Message {

	 /**
     * 消息识别码
     */
    public static final int MESSAGE_FAKE_ID = 0xFAFB;

    /**
     * 心跳消息
     */
    public static final Byte MESSAGE_HEART_BEAT = (byte) 0xFE;
   
    /**
     * 设备（学生）登陆消息
     */
	public static final Byte MESSAGE_STUDENT_LOGIN = 0x01;
	
	/**
     * 创建小组信息
     */
    public static final Byte MESSAGE_GROUP_CREATE = 0x02;
    
	 /**
	 * 删除小组
	 */
	public static final Byte MESSAGE_GROUP_DELETE = 0x03;
	
	/**
	 * 提交小组信息
	 */
	public static final Byte MESSAGE_GROUP_SUBMIT = 0x04;

	/**
	 * 确认小组信息
	 */
	public static final Byte MESSAGE_GROUP_CONFIRM = 0x05;
	
	/**
	 * 加入小组(android-pc)
	 */
	public static final Byte MESSAGE_GROUP_JOIN = 0x07;
    /**
     * 发送作业(android-pc)
     */
    public static final Byte MESSAGE_DISTRIBUTE_PAPER = 0x08;
    /**
     * 保存作业(android-pc)
     */
    public static final Byte MESSAGE_SAVE_PAPER = 0x09;
    
    /**
	 * 保存作业后PC端传回的信息
	 */
	public static final Byte MESSAGE_SAVE_PAPER_RESULT=0x0A;
	
	/**
	 * 设备退出
	 */
	public static final Byte MESSAGE_DEVICE_LOGOUT=0x0B;
	/**
	 * 锁屏
	 */
	public static final Byte MESSAGE_LOCK_SCREEN=0x0C;
	
	private Logger log = Logger.getLogger(Message.class);
	
	private byte msgID;
	private int msgSize;
	private ByteBuffer bodyBuffer;
	private MessageHandler handler;
	private SocketChannel channel;

	public byte getMsgID() {
		return msgID;
	}

	public void setMsgID(byte msgId) {
		this.msgID = msgId;
		handler = MessageHandlerResource.getHandlerResources()
				.getMessageHandler(getMsgID());
	}

	public int getMsgSize() {
		return msgSize;
	}

	public void setMsgSize(int msgSize) {
		this.msgSize = msgSize;
	}

	public ByteBuffer getBodyBuffer() {
		return bodyBuffer;
	}

	public void setBodyBuffer(ByteBuffer bodyBuffer) {
		this.bodyBuffer = bodyBuffer;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	public void handleMessage() throws NoHandlerException{
		if (handler == null) {
			log.error("执行命令出错：没有找到想应的Handler!");
			throw new NoHandlerException();
		}
		handler.handleMessage(this);
	}
}
