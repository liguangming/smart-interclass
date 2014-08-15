package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.utils.UIHelper;

public class QuizGroupPanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = 882552987989905663L;
	private static final String BTN_PLUS_NORMAL = "images/quiz/ico_add.png";
	private static final String BTN_PLUS_HOVER = "images/quiz/ico_add_hover.png";
	
	private static final String BTN_MINUS_NORMAL = "images/quiz/ico_js.png";
	private static final String BTN_MINUS_HOVER = "images/quiz/ico_js_hover.png";
	
	private static final String BTN_MEDAL_NORMAL = "images/quiz/ico_medal.png";
	private static final String BTN_MEDAL_HOVER = "images/quiz/ico_medalz_hover.png";
	
	private Group group;
	private JLabel lblDesk;
	private JLabel lblLogo;
	private JLabel lblGroupName;
	private JLabel lblScore;
	private JButton btnPlus,btnMinus,btnMedal;
	private List<JPanel> quizList = new ArrayList<JPanel>();
	private List<JLabel> nameList = new ArrayList<JLabel>();
	
	@Override
	protected void paintComponent(Graphics g) {
		Image image = new ImageIcon("images/quiz/bg_list.png").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),this);
	}
	
	public QuizGroupPanel(){
		setLayout(null);
//		setVisible(false);
		
		JPanel quiz1 = createImagePanel(14,15);
		quizList.add(quiz1);
		add(quiz1);
//		quiz1.setVisible(false);
		
		JPanel quiz2 = createImagePanel(208,15);
		quizList.add(quiz2);
		add(quiz2);
//		quiz2.setVisible(false);
		
		JPanel quiz3 = createImagePanel(14,165);
		quizList.add(quiz3);
		add(quiz3);
//		quiz3.setVisible(false);
		
		JPanel quiz4 = createImagePanel(208,165);
		quizList.add(quiz4);
		add(quiz4);
//		quiz4.setVisible(false);
		
		//小组课桌号
		lblDesk = createDeskLabel();
		add(lblDesk);
		//小组logo
		lblLogo = createLogoLabel();
		add(lblLogo);
		//小组名字
		lblGroupName = createGroupName();
		add(lblGroupName);
		//小组积分
		lblScore = createScoreLabel();
		add(lblScore);
		//小组加分按钮
		btnPlus = createPlusButton();
		btnPlus.addMouseListener(this);
		add(btnPlus);
		//小组减分按钮
		btnMinus = createMinusButton();
		btnMinus.addMouseListener(this);
		add(btnMinus);
		//小组勋章按钮
		btnMedal = createMedalButton();
		btnMedal.addMouseListener(this);
		add(btnMedal);
	}
	
	private JLabel createDeskLabel(){
		JLabel lblDesk = new JLabel("12号桌", JLabel.CENTER);
		lblDesk.setOpaque(true);
		lblDesk.setBounds(14,331, 60, 24);
		lblDesk.setBackground(new Color(Integer.parseInt("39a2de", 16)));
		lblDesk.setForeground(new Color(Integer.parseInt("ffffff", 16)));
		return lblDesk;
	}
	
	private JLabel createLogoLabel(){
		JLabel lblLogo = new JLabel();
		final ImageIcon icon = new ImageIcon("images/logo/24/pen.png");
		lblLogo.setIcon(icon);
		lblLogo.setBounds(76, 331, 24, 24);
		return lblLogo;
	}
	
	private JLabel createGroupName(){
		JLabel lblName = new JLabel("哇哈哈小组哈哈哈哇哈哈");
		lblName.setBounds(105,331, 100, 24);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		return lblName;
	}
	
	private JLabel createScoreLabel(){
		JLabel lblDesk = new JLabel("15分", JLabel.CENTER);
		lblDesk.setOpaque(true);
		lblDesk.setBounds(210,331, 40, 24);
		lblDesk.setBackground(Color.RED);
		lblDesk.setForeground(Color.WHITE);
		return lblDesk;
	}
	
	private JButton createPlusButton(){
		JButton lblPlus = new JButton();
		Icon iconPlus = new ImageIcon(BTN_PLUS_NORMAL);
		lblPlus = new JButton();
		lblPlus.setIcon(iconPlus);
		lblPlus.setFocusPainted(false);
		lblPlus.setBorderPainted(false);// 设置边框不可见
		lblPlus.setContentAreaFilled(false);// 设置透明
		lblPlus.setBounds(260,329, iconPlus.getIconWidth(), iconPlus.getIconHeight());
		return lblPlus;
	}
	
	private JButton createMinusButton(){
		JButton lblMinus = new JButton();
		Icon iconMinus = new ImageIcon(BTN_MINUS_NORMAL);
		lblMinus = new JButton();
		lblMinus.setIcon(iconMinus);
		lblMinus.setFocusPainted(false);
		lblMinus.setBorderPainted(false);// 设置边框不可见
		lblMinus.setContentAreaFilled(false);// 设置透明
		lblMinus.setBounds(295,329, iconMinus.getIconWidth(), iconMinus.getIconHeight());
		return lblMinus;
	}
	
	private JButton createMedalButton(){
		JButton lblMedal = new JButton();
		Icon iconMedal = new ImageIcon(BTN_MEDAL_NORMAL);
		lblMedal = new JButton();
		lblMedal.setIcon(iconMedal);
		lblMedal.setFocusPainted(false);
		lblMedal.setBorderPainted(false);// 设置边框不可见
		lblMedal.setContentAreaFilled(false);// 设置透明
		lblMedal.setBounds(330,329, iconMedal.getIconWidth(), iconMedal.getIconHeight());
		return lblMedal;
	}
	
	private JPanel createImagePanel(int x, int y) {
		final ImageIcon icon = new ImageIcon("images/quiz/bg_img.png");
		JPanel imagePanel = new JPanel(){
			private static final long serialVersionUID = -5620925583420692590L;
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(),this);
			}
		};
		imagePanel.setLayout(null);
		imagePanel.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
		JLabel lblName = new JLabel("测试测试从测试", JLabel.CENTER);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		nameList.add(lblName);
		lblName.setBounds(0, icon.getIconHeight()- 25 , icon.getIconWidth(), 25);
		imagePanel.add(lblName);
		return imagePanel;
	}
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public JLabel getLblLogo() {
		return lblLogo;
	}

	public JLabel getLblGroupName() {
		return lblGroupName;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == btnPlus) {
			btnPlus.setIcon(new ImageIcon(BTN_PLUS_HOVER));
		}
		if (e.getSource() == btnMinus) {
			btnMinus.setIcon(new ImageIcon(BTN_MINUS_HOVER));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_HOVER));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
//		if (e.getSource() == btnPlus) {
//			btnPlus.setIcon(new ImageIcon(BTN_PLUS_NORMAL));
//		}
//		if (e.getSource() == btnMinus) {
//			btnMinus.setIcon(new ImageIcon(BTN_MINUS_NORMAL));
//		}
//		if (e.getSource() == btnMedal) {
//			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_NORMAL));
//		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnPlus) {
			btnPlus.setIcon(new ImageIcon(BTN_PLUS_HOVER));
		}
		if (e.getSource() == btnMinus) {
			btnMinus.setIcon(new ImageIcon(BTN_MINUS_HOVER));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_HOVER));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnPlus) {
			btnPlus.setIcon(new ImageIcon(BTN_PLUS_NORMAL));
		}
		if (e.getSource() == btnMinus) {
			btnMinus.setIcon(new ImageIcon(BTN_MINUS_NORMAL));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_NORMAL));
		}
	}
}
