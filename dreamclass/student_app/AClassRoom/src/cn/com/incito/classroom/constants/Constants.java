package cn.com.incito.classroom.constants;

import java.util.ArrayList;

import android.graphics.Bitmap;
import cn.com.incito.classroom.widget.canvas.Action;
import cn.com.incito.classroom.widget.canvas.ISketchPadTool;

/**
 * 常量文件 Created by popoy on 2014/7/28.
 */
public class Constants {
	public static final String FILE_PATH="ftp_home";
	public static final String FILE_NAME="quiz.jpg";
	public static final boolean OPEN_LOCK_SCREEN = true;// 是否打开锁屏功能
	public static final int PORT = 9001;// pc端口号（socket）
	public static String IP = "192.168.10.100";//pc端地址
	public static String HTTP = "http://";
	/**
	 * 上传日志文件
	 */
	public static String URL_UPLOAD_LOG = "/app/api/log/save";
	/**
	 * 更新apk
	 */
	public static String URL_UPDATE_APK ="/app/api/version/check";
	/**
	 * apk下载地址
	 */
	public static String URL_DOWNLOAD_APK ="/app/api/version/download?id=";
	
	
	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public static final int PAD_PER_DESK = 4;

	/** WisdomCityRestClient 访问API是否输出日志 */
	public static final boolean REST_CLIENT_LOG_SENABLE_LOGGING = true;
	public static final String WISDOMCITY_IAMGE_CACHE_SDCARD_PATH = "demo/image/cache";

	public static final String ACTION_SHOW_EDIT_GROUP = "cn.com.classroom.SHOW_EDIT_GROUP";
	public static final String ACTION_SHOW_DRAWBOX = "cn.com.classroom.SHOW_DRAWBOX";
	public static final String ACTION_SHOW_CONFIRM_GROUP = "cn.com.classroom.SHOW_CONFIRM_GROUP";
	public static final String ACTION_SHOW_EVALUATE = "cn.com.classroom.SHOW_EVALUATE";
	
	// 绘画板常量
	public static ArrayList<String> LIST = null;
	public static ArrayList<Action> actionList = new ArrayList<Action>();
	public static ArrayList<ISketchPadTool> m_undoStack = new ArrayList<ISketchPadTool>();
	public static Bitmap bitmap;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	public final static int LARGE_PEN_WIDTH = 15;
	public final static int MIDDLE_PEN_WIDTH = 10;
	public final static int SMALL_PEN_WIDTH = 5;
	// 注册学生最大数量
	public static final int STUDENT_MAX_NUM = 15;

	//
	public static final String PREFERENCE_IP = "prefence_ip";
	
	

}
