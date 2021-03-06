package cn.com.incito.server.utils;

import java.awt.Color;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.ui.PrepareBottomPanel;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;

public class UIHelper {
	static Logger logger =  Logger.getLogger(UIHelper.class.getName());
	public static Color getDefaultFontColor() {
		return new Color(Integer.parseInt("454545", 16));
	}
	/**
	 * 
	 * @param isLock true 为锁屏，false为锁屏
	 */
	public static void sendLockScreenMessage(boolean isLock){
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_LOCK_SCREEN);
		if(isLock){
			Application.getInstance().setLockScreen(true);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("true"));
		}else{
			Application.getInstance().setLockScreen(false);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("false"));
		}
		CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
		logger.info("锁屏信息发出");
	}
	/**
	 * 下课命令
	 */
	public static void sendClassOverMessage(){
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_LOCK_SCREEN);
		Application.getInstance().setLockScreen(false);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("over"));
		CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
		logger.info("下课信息发出");
	}
}
