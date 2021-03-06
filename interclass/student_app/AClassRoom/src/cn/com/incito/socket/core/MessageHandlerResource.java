package cn.com.incito.socket.core;

import java.util.HashMap;
import java.util.Map;

import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.socket.handler.ClassReadyHandler;
import cn.com.incito.socket.handler.DeleteGroupHandler;
import cn.com.incito.socket.handler.DistributePaperHandler;
import cn.com.incito.socket.handler.GroupListHandler;
import cn.com.incito.socket.handler.GroupSubmitHandler;
import cn.com.incito.socket.handler.HeartbeatHandler;
import cn.com.incito.socket.handler.JoinGroupHandler;
import cn.com.incito.socket.handler.LockScreenHandler;
import cn.com.incito.socket.handler.LoginHandler;
import cn.com.incito.socket.handler.SavePaperHandler;
import cn.com.incito.socket.handler.SavePaperResultHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 消息处理器列表
 * 该类用来维护消息和消息处理器的关系
 *
 * @author 刘世平
 */
public final class MessageHandlerResource {

    private static MessageHandlerResource resources;
    private Map<Byte, Class<? extends MessageHandler>> handlerResources;

    public static MessageHandlerResource getHandlerResources() {
        if (resources == null) {
            resources = new MessageHandlerResource();
        }
        return resources;
    }

    private MessageHandlerResource() {
        handlerResources = new HashMap<Byte, Class<? extends MessageHandler>>();
        //设备登陆消息回复，用于启动心跳
        handlerResources.put(Message.MESSAGE_STUDENT_LOGIN, LoginHandler.class);
        //心跳消息
        handlerResources.put(Message.MESSAGE_HEART_BEAT, HeartbeatHandler.class);
        //收到作业
        handlerResources.put(Message.MESSAGE_DISTRIBUTE_PAPER, DistributePaperHandler.class);
        //保存作业图片
        handlerResources.put(Message.MESSAGE_SAVE_PAPER, SavePaperHandler.class);
        //保存作业成功与否的信息
        handlerResources.put(Message.MESSAGE_SAVE_PAPER_RESULT, SavePaperResultHandler.class);
        //解锁屏信息
        handlerResources.put(Message.MESSAGE_LOCK_SCREEN, LockScreenHandler.class);
        //选择小组
        handlerResources.put(Message.MESSAGE_GROUP_CREATE, GroupListHandler.class);
        //删除小组后进入选择小组界面
        handlerResources.put(Message.MESSAGE_GROUP_DELETE, DeleteGroupHandler.class);
        //加入小组
        handlerResources.put(Message.MESSAGE_GROUP_JOIN, JoinGroupHandler.class);
        //创建小组
        handlerResources.put(Message.MESSAGE_GROUP_SUBMIT, GroupSubmitHandler.class);
        //停止其他小组成员添加
        handlerResources.put(Message.MESSAGE_GROUP_CONFIRM, ClassReadyHandler.class);
    }

    public MessageHandler getMessageHandler(Byte key) {
        // 查询是否有该消息ID的消息处理器
        if (handlerResources.containsKey(key)) {
            try {
                // 通过反射取得对应的处理器
                return handlerResources.get(key).newInstance();
            } catch (Exception e) {
            	ApiClient.uploadErrorLog(e.getMessage());
                WLog.e(MessageHandlerResource.class, "获取MessageHandler出错:" + e.getMessage());
                return null;
            }
        }
        return null;
    }

}
