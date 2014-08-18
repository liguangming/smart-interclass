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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.utils.UIHelper;

public class MainFrame extends MouseAdapter {
	private static MainFrame instance;
	private static final String CARD_PREPARE = "PREPARE";
	private static final String CARD_QUIZ = "QUIZ";
	private static final String CARD_PREPARE_BOTTOM = "PREPARE_BOTTOM";
	private static final String CARD_QUIZ_BOTTOM = "QUIZ_BOTTOM";
	
	private Application app = Application.getInstance();
	
	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JButton btnMin, btnClose;
	private JLabel lblBackground;
	private JPanel contentPane;
	
	// menu
	private JButton btnStatus;
	private JButton btnQuiz;
	
	private CardLayout centerCardLayout;
	private JPanel centerCardPanel;
	private PreparePanel preparePanel;
	private QuizPanel quizPanel;
	
	private CardLayout bottomCardLayout;
	private JPanel bottomCardPanel;
	private PrepareBottomPanel prepareBottomPanel;
	private QuizBottomPanel quizBottomPanel;
	
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	public void setVisible(boolean show) {
		frame.setVisible(show);
	}

	public void doSendQuiz(){
		quizBottomPanel.doSendQuiz();
	}
	public void doAcceptQuiz(){
		quizBottomPanel.doAcceptQuiz();
	}
	public void refreshPrepare() {
		preparePanel.refresh();
		quizPanel.refresh();
	}
	
	public void refreshQuiz(){
		quizPanel.refresh();
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
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		frame.setSize(1024, 728);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);// 设置窗体中间位置
		frame.setLayout(null);// 绝对布局
		frame.setUndecorated(true);// 去除窗体
		frame.setAlwaysOnTop(true); // 设置界面悬浮
		frame.setBackground(new Color(0,0,0,0));//窗体透明
		frame.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				refreshPrepare();
				refreshQuiz();
			}
		});
		// //////////////////////top部分////////////////////////
		JPanel top = new JPanel();
		top.setSize(1024, 73);
		top.setLayout(null);
		top.setOpaque(false);

		// 最小化按钮
		btnMin = new JButton();// 创建按钮对象
		btnMin.setBorderPainted(false);// 设置边框不可见
		btnMin.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);// 设置图片
		top.add(btnMin);// 添加按钮
		btnMin.setBounds(948, 9, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		top.add(btnClose);// 添加按钮
		btnClose.setBounds(975, 9, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		contentPane.add(top);

		// //////////////////////left部分////////////////////////
		JPanel left = new JPanel();
		left.setSize(127, 930);
		left.setLayout(null);
		left.setOpaque(false);

		// 准备菜单
		Icon icon = new ImageIcon("images/main/bg_ready_hover.png");
		btnStatus = new JButton();
		btnStatus.setFocusPainted(false);
		btnStatus.setIcon(icon);
		btnStatus.setBorderPainted(false);// 设置边框不可见
		btnStatus.setContentAreaFilled(false);// 设置透明
		left.add(btnStatus);
		btnStatus.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		btnStatus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                centerCardLayout.show(centerCardPanel, CARD_PREPARE);
                bottomCardLayout.show(bottomCardPanel, CARD_PREPARE_BOTTOM);
                
                btnStatus.setIcon(new ImageIcon("images/main/bg_ready_hover.png"));
                btnQuiz.setIcon(new ImageIcon("images/main/bg_works.png"));
            }
        });
		
		// 作业菜单
		Icon iconQuiz = new ImageIcon("images/main/bg_works.png");
		btnQuiz = new JButton();
		btnQuiz.setIcon(iconQuiz);
		btnQuiz.setFocusPainted(false);
		btnQuiz.setBorderPainted(false);// 设置边框不可见
		btnQuiz.setContentAreaFilled(false);// 设置透明
		left.add(btnQuiz);
		btnQuiz.setBounds(0, 55, iconQuiz.getIconWidth(), iconQuiz.getIconHeight());
		btnQuiz.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                centerCardLayout.show(centerCardPanel, CARD_QUIZ);
                bottomCardLayout.show(bottomCardPanel, CARD_QUIZ_BOTTOM);
                
                btnQuiz.setIcon(new ImageIcon("images/main/bg_works_hover.png"));
                btnStatus.setIcon(new ImageIcon("images/main/bg_ready.png"));
            }
        });
		
		// 用户信息
		JPanel pnlUser = new JPanel() {
			private static final long serialVersionUID = 1778895558158714379L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconUser = new ImageIcon("images/main/bg_user.png").getImage();
				g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(), this);
			}

		};
		pnlUser.setLayout(null);
		String user = String.format("您好,%s!", app.getTeacher().getName());
		JLabel lblTeacher = new JLabel(user, JLabel.CENTER);
		lblTeacher.setForeground(UIHelper.getDefaultFontColor());
		pnlUser.add(lblTeacher);
		lblTeacher.setBounds(0, 70, 127, 20);
		left.add(pnlUser);
		pnlUser.setBounds(0, 110, 127, 95);

		JLabel lblCopyRight = new JLabel("Copy Right © 2014", JLabel.CENTER);
		lblCopyRight.setForeground(UIHelper.getDefaultFontColor());
		lblCopyRight.setBounds(0, 585, 127, 20);
		left.add(lblCopyRight);
		JLabel lblCompany1 = new JLabel("四川灵动", JLabel.CENTER);
		lblCompany1.setForeground(UIHelper.getDefaultFontColor());
		lblCompany1.setBounds(0, 610, 127, 20);
		left.add(lblCompany1);
		JLabel lblCompany2 = new JLabel("信息技术有限公司", JLabel.CENTER);
		lblCompany2.setForeground(UIHelper.getDefaultFontColor());
		lblCompany2.setBounds(0, 625, 127, 20);
		left.add(lblCompany2);

		left.setBounds(10, 73, 127, 930);
		contentPane.add(left);

		// ///////////////////center部分////////////////////////
		centerCardLayout = new CardLayout();
		centerCardPanel = new JPanel(centerCardLayout);
		centerCardPanel.setBounds(138, 35, 876, 620);
		contentPane.add(centerCardPanel);
		
		//准备上课card
		preparePanel = new PreparePanel();
		preparePanel.setBackground(Color.WHITE);
		JScrollPane prepareScrollPane = new JScrollPane(preparePanel);
		prepareScrollPane.getVerticalScrollBar().setUnitIncrement(50);
		prepareScrollPane.setBorder(null);
		prepareScrollPane.setBounds(0, 0, 876, 630);
		 //TODO 根据分组的多少动态调整
		preparePanel.setPreferredSize(new Dimension(prepareScrollPane.getWidth() - 50, prepareScrollPane.getHeight() * 3));
		preparePanel.revalidate();
		centerCardPanel.add(prepareScrollPane, CARD_PREPARE);
		
		//作业card
		quizPanel = new QuizPanel();
        quizPanel.setBackground(Color.WHITE);
        JScrollPane quizScrollPane = new JScrollPane(quizPanel);
        quizScrollPane.getVerticalScrollBar().setUnitIncrement(100);
        quizScrollPane.setBorder(null);
        quizScrollPane.setBounds(0, 0, 876, 630);
        quizPanel.setPreferredSize(new Dimension(quizScrollPane.getWidth() - 50, (quizScrollPane.getHeight() - 50) * 4));
		centerCardPanel.add(quizScrollPane, CARD_QUIZ);
		
        
		// //////////////////////bottom部分////////////////////////
		bottomCardLayout = new CardLayout();
		bottomCardPanel = new JPanel(bottomCardLayout);
		bottomCardPanel.setOpaque(false);
		bottomCardPanel.setBounds(137, 664, 878, 54);
		contentPane.add(bottomCardPanel);
		
		//prepare
		prepareBottomPanel = new PrepareBottomPanel();
		bottomCardPanel.add(prepareBottomPanel, CARD_PREPARE_BOTTOM);
		//quiz
		quizBottomPanel = new QuizBottomPanel();
		bottomCardPanel.add(quizBottomPanel, CARD_QUIZ_BOTTOM);
				
		initData();
		setBackground();// 设置背景
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
	public void setBackground() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/main/bg.png"));
		lblBackground.setBounds(0, 0, 1024, 728);
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
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnMin) {
			frame.setExtendedState(JFrame.ICONIFIED);
		}
		if (e.getSource() == btnClose) {
			//点击关闭按钮不关闭，而是隐藏
			frame.setVisible(false);
		}
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
