package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Login3 extends MouseAdapter{

	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JLabel lblClassRoom;
	private JButton btnMin, btnClose, btnLogin;
	private JTextField txtClassRoom;
	private JLabel lblBackground;
	private Application app = Application.getInstance();
	private Logger logger = Logger.getLogger(Login2.class.getName());

	public JFrame getFrame() {
		return frame;
	}

	// 构造函数、调用方法
	public Login3() {
		showLoginUI();
		setDragable();
	}

	// 显示登陆界面
	private void showLoginUI() {
		ImageIcon icon = new ImageIcon("images/main/icon.png");
		frame.setIconImage(icon.getImage());
		frame.setSize(460, 290);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);// 设置窗体中间位置
		frame.setLayout(null);// 绝对布局
		frame.setUndecorated(true);// 去除窗体
		frame.setAlwaysOnTop(true); // 设置界面悬浮
		frame.setBackground(new Color(0, 0, 0, 0));// 窗体透明

		JPanel top = new JPanel();
		top.setSize(460, 30);
		top.setLayout(null);
		top.setOpaque(false);

		// 最小化按钮
		btnMin = new JButton();// 创建按钮对象
		btnMin.setBorderPainted(false);// 设置边框不可见
		btnMin.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);// 设置图片
		top.add(btnMin);// 添加按钮
		btnMin.setBounds(385, 9, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		top.add(btnClose);// 添加按钮
		btnClose.setBounds(412, 9, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);
		frame.add(top);
		// 班级
		lblClassRoom = new JLabel();
		lblClassRoom.setForeground(UIHelper.getDefaultFontColor());
		lblClassRoom.setText("请设置本教室名称:");
		lblClassRoom.setBounds(60, 65, 265, 35);
		frame.add(lblClassRoom);
		txtClassRoom = new JTextField();
		txtClassRoom.setBounds(180, 65, 130, 35);
		frame.add(txtClassRoom);

		// 登录按钮
		btnLogin = new JButton();// 创建按钮对象
		btnLogin.setBorderPainted(false);// 设置边框不可见
		btnLogin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
		btnLogin.setIcon(btnImage);// 设置图片
		frame.add(btnLogin);// 添加按钮
		btnLogin.setBounds(125, 220, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnLogin.addMouseListener(this);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sentRoomMsg();
			}
		});
//		initData();
		setBgimg();// 设置背景
	}


	// 拖动窗体的方法
	private void setDragable() {
		frame.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());// 获取窗体位置
				isDragged = true;
				frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDragged) {
					loc = new Point(frame.getLocation().x + e.getX() - tmp.x,
							frame.getLocation().y + e.getY() - tmp.y);
					frame.setLocation(loc);
				}
			}
		});
	}

	// 设置背景
	public void setBgimg() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/login/login_bg.png"));
		lblBackground.setBounds(0, 0, 460, 290);
		frame.add(lblBackground);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// 按钮按下效果
		if (e.getSource() == btnLogin) {
			btnLogin.setIcon(new ImageIcon("images/login/btn_login_pressed.png"));
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/6.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// 按钮释放效果
		if (e.getSource() == btnLogin) {
			ImageIcon btnImage = new ImageIcon(
					"images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
			frame.setExtendedState(JFrame.ICONIFIED);
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// 鼠标进入效果
		if (e.getSource() == btnLogin) {
			btnLogin.setIcon(new ImageIcon("images/login/btn_login_pressed.png"));
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// 鼠标退出效果
		if (e.getSource() == btnLogin) {
			ImageIcon btnImage = new ImageIcon(
					"images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/4.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}
	
	private void sentRoomMsg(){
		String roomName = txtClassRoom.getText();
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper param = new ParamsWrapper();
		param.put("roomName", roomName);
		param.put("mac", app.getMac());
		param.put("schoolId", app.getTeacher().getSchoolId());
		http.post(URLs.URL_TEACHER_ROOM, param, new StringResponseHandler() {
			
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStreamError(IOException exp) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onConnectError(IOException exp) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onResponse(String content, URL url) {
				// TODO Auto-generated method stub
				if (content != null && !content.equals("")) {
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 2) {
						JOptionPane.showMessageDialog(frame, "保存教室出现错误!");
						return;
					}
					frame.setVisible(false);
				Login2 log2 = new Login2(app.getCourses());
				log2.getFrame().setVisible(true);
				}
				logger.info(content);	
			}
		});
	}

	public static void main(String[] args) {
		new Login3();
	}
}