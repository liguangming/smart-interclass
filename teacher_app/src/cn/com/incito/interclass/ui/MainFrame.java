package cn.com.incito.interclass.ui;

/**
 * 互动课堂主界面
 * @author 刘世平
 */
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.Listener.MySystemTrayEvent;
import cn.com.incito.interclass.Listener.MySystemTrayManager;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class MainFrame extends MouseAdapter {
	private static MainFrame instance;
	private static final String CARD_PREPARE = "PREPARE";
	private static final String CARD_QUIZ = "QUIZ";
	
	private Application app = Application.getInstance();
	
	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JButton btnMin, btnClose;
	private JLabel lblBackground;
	private JButton btnBegin;
	private JPanel contentPane;
	
	private CardLayout cardLayout;
	private JPanel centerPanel;
	private PreparePanel preparePanel;
	private QuizLookupPanel quizLookupPanel;
//	private JScrollPane deskScrollPane;
	private MySystemTrayEvent mySystemTrayEvent;

	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	public void setVisible(boolean show) {
		frame.setVisible(show);
	}

	public void refresh() {
		preparePanel.refresh();
	}

	public JFrame getFrame() {
		return frame;
	}

	private MainFrame() {
		// 启动通讯线程
		CoreSocket.getInstance().start();
		showLoginUI();
		setDragable();
	}

	// 显示登陆界面
	private void showLoginUI() {
		mySystemTrayEvent = new MySystemTrayManager();
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		frame.setSize(1004, 748);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);// 设置窗体中间位置
		frame.setLayout(null);// 绝对布局
		frame.setUndecorated(true);// 去除窗体
		frame.setAlwaysOnTop(true); // 设置界面悬浮

		// //////////////////////top部分////////////////////////
		JPanel top = new JPanel();
		top.setSize(1004, 73);
		top.setLayout(null);
		top.setOpaque(false);

		// 最小化按钮
		btnMin = new JButton();// 创建按钮对象
		btnMin.setBorderPainted(false);// 设置边框不可见
		btnMin.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);// 设置图片
		top.add(btnMin);// 添加按钮
		btnMin.setBounds(938, -1, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		top.add(btnClose);// 添加按钮
		btnClose.setBounds(965, -1, imgMax.getIconWidth(),
				imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		// logo
		JLabel lblLogo = new JLabel();
		ImageIcon imgLogo = new ImageIcon("images/main/logo.png");
		lblLogo.setIcon(imgLogo);
		top.add(lblLogo);
		lblLogo.setBounds(13, 23, imgLogo.getIconWidth(),
				imgLogo.getIconHeight());

		// menu
		JLabel btnHome = new JLabel();
		ImageIcon imgHome = new ImageIcon("images/main/ico_home_foucs.png");
		btnHome.setIcon(imgHome);
		top.add(btnHome);
		btnHome.setBounds(150, 40, imgLogo.getIconWidth(),
				imgLogo.getIconHeight());

		contentPane.add(top);

		// //////////////////////left部分////////////////////////
		JPanel left = new JPanel();
		left.setSize(127, 930);
		left.setLayout(null);
		left.setOpaque(false);

		// 准备菜单
		Icon icon = new ImageIcon("images/main/ico_menu_1.png");
		JButton lblStatus = new JButton(" 准备", icon);
		lblStatus.setOpaque(false);
		left.add(lblStatus);
		lblStatus.setBounds(0, 0, 127, 56);
		lblStatus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, CARD_PREPARE);
            }
        });
		
		// 作业菜单
		Icon iconQuiz = new ImageIcon("images/main/ico_menu_1.png");
		JButton lblQuiz = new JButton(" 作业", iconQuiz);
		lblQuiz.setOpaque(false);
		left.add(lblQuiz);
		lblQuiz.setBounds(0, 55, 127, 56);
		lblQuiz.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, CARD_QUIZ);
            }
        });
		
		// 用户信息
		JPanel pnlUser = new JPanel() {
			private static final long serialVersionUID = 1778895558158714379L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconUser = new ImageIcon("images/main/bg_user.png")
						.getImage();
				g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(),
						this);
			}

		};
		pnlUser.setLayout(null);
		String user = String.format("您好,%s!", app.getTeacher().getName());
		JLabel lblTeacher = new JLabel(user, JLabel.CENTER);
		pnlUser.add(lblTeacher);
		lblTeacher.setBounds(0, 70, 127, 20);
		left.add(pnlUser);
		pnlUser.setBounds(0, 110, 127, 95);

		JLabel lblCopyRight = new JLabel("Copy Right © 2014", JLabel.CENTER);
		lblCopyRight.setBounds(0, 605, 127, 20);
		left.add(lblCopyRight);
		JLabel lblCompany1 = new JLabel("四川灵动", JLabel.CENTER);
		lblCompany1.setBounds(0, 630, 127, 20);
		left.add(lblCompany1);
		JLabel lblCompany2 = new JLabel("信息技术有限公司", JLabel.CENTER);
		lblCompany2.setBounds(0, 645, 127, 20);
		left.add(lblCompany2);

		left.setBounds(0, 73, 127, 930);
		contentPane.add(left);

		// ///////////////////center部分////////////////////////
		cardLayout = new CardLayout();
		centerPanel = new JPanel(cardLayout);
		centerPanel.setBounds(127, 75, 878, 618);
		contentPane.add(centerPanel);
		
		//准备上课card
		preparePanel = new PreparePanel();
		preparePanel.setBackground(Color.WHITE);
//		deskScrollPane = new JScrollPane(deskPanel);
//		deskScrollPane.getVerticalScrollBar().setUnitIncrement(50);
//		deskScrollPane.setBorder(null);
//		deskScrollPane.setBounds(127, 75, 878, 618);
		// TODO 根据分组的多少动态调整
//		deskPanel.setPreferredSize(new Dimension(deskScrollPane.getWidth() - 50, deskScrollPane.getHeight() * 3));
//		deskPanel.revalidate();
		centerPanel.add(preparePanel, CARD_PREPARE);
		
		//作业card
		quizLookupPanel = new QuizLookupPanel();
        quizLookupPanel.setBackground(Color.WHITE);
		centerPanel.add(quizLookupPanel, CARD_QUIZ);
        
		// //////////////////////bottom部分////////////////////////
		JPanel bottom = new JPanel();
		bottom.setSize(878, 48);
		bottom.setLayout(null);
		bottom.setOpaque(false);
		bottom.setBounds(127, 694, 878, 54);

		JPanel pnlClass = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png")
						.getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(),
						this);
			}
		};
		pnlClass.setLayout(null);
		bottom.add(pnlClass);
		pnlClass.setBounds(50, 10, 120, 35);

		JPanel pnlCourse = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png")
						.getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(),
						this);
			}
		};
		pnlCourse.setLayout(null);
		bottom.add(pnlCourse);
		pnlCourse.setBounds(190, 10, 120, 35);

		btnBegin = new JButton();// 创建按钮对象
		btnBegin.setBorderPainted(false);// 设置边框不可见
		btnBegin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/main/btn_begin.png");
		btnBegin.setIcon(btnImage);// 设置图片
		bottom.add(btnBegin);// 添加按钮
		btnBegin.setBounds(340, -4, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnBegin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doBegin();
			}
		});

		JLabel lblExpected = new JLabel("应到 %d 人", JLabel.CENTER);
		lblExpected.setBounds(520, 15, 140, 35);
		bottom.add(lblExpected);
		JLabel lblActual = new JLabel("实到 %d 人", JLabel.CENTER);
		lblActual.setBounds(670, 15, 140, 35);
		bottom.add(lblActual);

		contentPane.add(bottom);

		initData();
		setBgimg();// 设置背景
	}

	private void doBegin() {
		List<Table> tableList = app.getTableList();
		if (tableList == null || tableList.size() == 0) {
			JOptionPane.showMessageDialog(frame, "设备还未绑定课桌，请先绑定课桌!");
			return;
		}

		Map<Integer, Group> tableGroup = app.getTableGroup();
		if (tableGroup == null || tableGroup.size() == 0) {
			JOptionPane.showMessageDialog(frame, "还未进行小组分组，请先进行分组!");
			return;
		}

		boolean hasTeamInfo = true;
		for (Group group : tableGroup.values()) {
			if (group.getName() == null || group.getName().equals("")) {
				hasTeamInfo = false;
			}
		}
		if (!hasTeamInfo) {
			int result = JOptionPane.showConfirmDialog(frame,
					"还有小组未编辑小组信息，是否编辑小组信息？", "提示", JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION == result) {
				// 编辑小组信息
				List<Group> groupList = app.getGroupList();
				for (Group group : groupList) {
					JSONObject json = new JSONObject();
					json.put("id", group.getId());
					MessagePacking messagePacking = new MessagePacking(
							Message.MESSAGE_GROUP_EDIT);
					messagePacking.putBodyData(DataType.INT,
							BufferUtils.writeUTFString(json.toString()));
					final List<SocketChannel> channels = app
							.getClientChannelByGroup(group.getId());
					sendMessageToGroup(messagePacking, channels);
				}
			} else if (JOptionPane.NO_OPTION == result) {
				frame.setVisible(false);
				// 开始上课
			}
		}
	}

	private void sendMessageToGroup(final MessagePacking messagePacking,
			final List<SocketChannel> channels) {
		if (channels == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				byte[] data = messagePacking.pack().array();
				ByteBuffer buffer = ByteBuffer.allocate(data.length);
				Iterator<SocketChannel> it = channels.iterator();
				while (it.hasNext()) {
					SocketChannel channel = it.next();
					if (!channel.isConnected()) {
						it.remove();
						continue;
					}
					buffer.clear();
					buffer.put(data);
					buffer.flip();
					try {
						channel.write(buffer);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void initData() {

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
		lblBackground.setIcon(new ImageIcon("images/main/bg.png"));
		lblBackground.setBounds(0, 0, 1004, 748);
		frame.add(lblBackground);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/6.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
			frame.setExtendedState(JFrame.ICONIFIED);
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
			//点击关闭按钮不关闭，而是隐藏
			frame.setVisible(false);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/4.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	public MySystemTrayEvent getMySystemTrayEvent() {
		return mySystemTrayEvent;
	}
	
	    
	public boolean isVisible(){
		return frame.isVisible();
	}
	    
	public int getState(){
		return frame.getState();
	}
	    
	public void setState(int state){
		frame.setState(state);
	}
}
