package cn.com.incito.socket.handler;

//import cn.com.incito.classroom.vo.GroupVo;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 获取组成员列表hanlder Created by liushiping on 2014/7/28.
 */
public class GroupListHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(System.currentTimeMillis()+"收到分组列表消息：" + data);
		if(0 == data.getIntValue("code")){
			if(AppManager.getAppManager().currentActivity().getComponentName().getClassName().equals("WaitForOtherMembersActivity")){
				UIHelper.getInstance().getWaitForOtherMembersActivity().setText(data.getString("data"));
			} else {
				UIHelper.getInstance().showGroupSelect(data.getString("data"));
			}
		}
		
	}
}
