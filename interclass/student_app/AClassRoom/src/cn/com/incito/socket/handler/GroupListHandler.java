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
		MyApplication.Logger.debug(System.currentTimeMillis()
				+ ":GroupListHandler:收到分组列表消息：" + data);

		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();

		if (0 == data.getIntValue("code")) {
			// 如果该学生的界面是等待老师分组界面的则将等待上课界面关闭
			if ("ClassReadyActivity".equals(currentActivityName)) {
				UIHelper.getInstance().showGroupSelect(data.getString("data"));
				AppManager.getAppManager().currentActivity().finish();
			} else if ("WaitForOtherMembersActivity".equals(currentActivityName)) {// 如果当前界面是等待其他小组成员界面,刷新界面，否则进去选择选择小组界面
				UIHelper.getInstance().getWaitForOtherMembersActivity().setTextName(data.getString("data"));
			} else if ("SelectGroupActivity".equals(currentActivityName)) {
				UIHelper.getInstance().showGroupSelect(data.getString("data"));
			}
		}
	}
}
