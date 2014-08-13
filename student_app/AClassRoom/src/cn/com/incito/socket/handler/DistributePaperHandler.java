package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;


import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;

public class DistributePaperHandler extends MessageHandler {
	private byte[] imageByte;

	@Override
	public void handleMessage(Message msg) {
		this.message = msg;
		ByteBuffer buffer = msg.getBodyBuffer();
		buffer.flip();
		// 获取id数据
		byte[] intSize = new byte[4];// int
		buffer.get(intSize);
		long idLength = BufferUtils.decodeIntLittleEndian(intSize, 0,
				intSize.length);
		byte[] idByte = new byte[(int) idLength];
		buffer.get(idByte);
		String uuid = BufferUtils.readUTFString(idByte);
		MyApplication.getInstance().setQuizID(uuid);
	    
		// 获取图片信息
		byte[] imageSize = new byte[4];// int
		buffer.get(imageSize);
		int pictureLength = (int)BufferUtils.decodeIntLittleEndian(imageSize, 0,
				imageSize.length);
		imageByte = new byte[pictureLength];
		buffer.get(imageByte);
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		 UIHelper.getInstance().showDrawBoxActivity(imageByte);
	}

}
