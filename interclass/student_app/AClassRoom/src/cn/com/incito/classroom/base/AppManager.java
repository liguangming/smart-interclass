package cn.com.incito.classroom.base;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.WifiAdmin;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.CoreSocket;

/**
 * 应用程序Activity管理类
 * Created by popoy on 2014/7/28.
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i =0 ; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
        	WifiAdmin.getWifiAdmin(context).releasWifiLock();
        	MyApplication.getInstance().release();
            finishAllActivity();
            CoreSocket.getInstance().stopConnection();
            Thread.sleep(100);//先让socket发送退出消息再完全退出
            ConnectionManager.getInstance(null).close(true);
            MyApplication.getInstance().stopSocketService();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        	ApiClient.uploadErrorLog(e.getMessage());
        	MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":AppManager:应用退出");
        	e.printStackTrace();
        }
    }
} 
