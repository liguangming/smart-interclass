package cn.com.incito.interclass.main;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Version;
import cn.com.incito.interclass.ui.widget.UpdateDialog;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.exception.AppExceptionHandler;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 应用程序入口
 * 
 * @author 刘世平
 * 
 */
public class Main {
	public static final int VERSION_CODE = 4;
	public static final String VERSION_NAME = "V200R001C001B004";
	private static final long FREE_SIZE = 1024 * 1024 * 100;//100M
	
	public static void main(String args[]) {
		// 注册异常处理器
		registerExceptionHandler();
		//设置观感
		setLookAndFeel();
		//设置字体
		initGlobalFontSetting();
		//检查升级
		checkUpdate();
	}
	
	private static void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void checkUpdate() {
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("type", 1);
		params.put("code", VERSION_CODE);
		http.post(URLs.URL_CHECK_UPDATE, params, new StringResponseHandler() {
			@Override
			protected void onResponse(String content, URL url) {
				if (content != null && !content.equals("")) {
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 1) {//没有升级
						// 初始化应用程序
						Application.getInstance();
						new Thread() {
							public void run() {
								// 初始化自定义字体
								initDefinedFont();
							}
						}.start();
						return;
					}
					File file = new File("update.exe");
					if(!file.exists()){
						JOptionPane.showMessageDialog(null, "检测到程序需要更新，但缺少必要的升级程序!");
						// 初始化应用程序
						Application.getInstance();
						new Thread() {
							public void run() {
								// 初始化自定义字体
								initDefinedFont();
							}
						}.start();
						return;
					}
					long freeSize = file.getFreeSpace();
					if (freeSize < FREE_SIZE) {
						JOptionPane.showMessageDialog(null, "检测到程序需要更新，但磁盘空间不足，无法完成更新，请确保磁盘空余空间100M以上!");
						System.exit(0);
						return;
					}
					// 获得带升级的版本
					String data = jsonObject.getString("data");
					Version version = JSON.parseObject(data, Version.class);
					new UpdateDialog(version).setVisible(true);
				}
			}

			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onConnectError(IOException exp) {
				JOptionPane.showMessageDialog(null, "不能连接到服务器，请检查网络！");
				System.exit(0);
			}

			@Override
			public void onStreamError(IOException exp) {
				JOptionPane.showMessageDialog(null, "数据解析错误！");
				System.exit(0);
			}
		});
	}
	
	private static void initGlobalFontSetting() {
		Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
		FontUIResource fontRes = new FontUIResource(font);
		Enumeration<?> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}
	
	private static void registerExceptionHandler(){
		Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());
	}
	
	private static void initDefinedFont() {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(
					"font/新蒂小丸子小学版.ttf"));
			Font font = Font.createFont(Font.TRUETYPE_FONT, bis);
			Application.getInstance().setDefinedFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bis) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
