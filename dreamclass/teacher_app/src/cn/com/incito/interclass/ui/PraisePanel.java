package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.LogoUtils;

public class PraisePanel extends JPanel {

	private static final long serialVersionUID = 6316121486627261595L;
	private static final String ICON_NO_DESK = "images/main/bg_binding_desk.png";
	private Application app = Application.getInstance();
	private JLabel lblNoDesk;
	private List<Group> groupList = new ArrayList<Group>();

	private List<PraiseGroupPanel> praiseGroupList = new ArrayList<PraiseGroupPanel>();

	public PraisePanel() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}

	private void initView() {
		int x = 15, y = 10;
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 3; column++) {
				PraiseGroupPanel groupPanel = new PraiseGroupPanel();
				groupPanel.setBounds(x, y, 266, 246);
				add(groupPanel);
				praiseGroupList.add(groupPanel);
				x += 280;
			}
			x = 15;
			y += 265;
		}
		lblNoDesk = new JLabel();
		ImageIcon icon = new ImageIcon(ICON_NO_DESK);
		lblNoDesk.setIcon(icon);
		lblNoDesk.setBounds(288, 235, 300, 160);
		lblNoDesk.setVisible(false);
		add(lblNoDesk);
	}

	public void refresh() {
		initData();
		if (groupList.size() == 0) {// 未绑定
			lblNoDesk.setVisible(true);
			return;
		}
		lblNoDesk.setVisible(false);
		int i = 0;
		while (i < groupList.size()) {
			PraiseGroupPanel pnlLeft = praiseGroupList.get(i);
			showPraiseGroupPanel(pnlLeft, groupList.get(i));
			if (++i < groupList.size()) {
				PraiseGroupPanel pnlMiddle = praiseGroupList.get(i);
				showPraiseGroupPanel(pnlMiddle, groupList.get(i));
			}
			if (++i < groupList.size()) {
				PraiseGroupPanel pnlRight = praiseGroupList.get(i);
				showPraiseGroupPanel(pnlRight, groupList.get(i));
			}
		}
		repaint();
		revalidate();
	}

	private void showPraiseGroupPanel(PraiseGroupPanel panel, Group group) {
		panel.setVisible(true);
		panel.setGroup(group);
		String logo = LogoUtils.getInstance().getLogo(group.getLogo());
		ImageIcon icon = new ImageIcon(logo);
		panel.getLblLogo().setIcon(icon);
		panel.getLblGroupName().setText(group.getName());
		panel.getLblGroupName().setToolTipText(group.getName());
		String memberStr = "";
		int score = 0;
		double scoreResult = 0;
		if (group.getStudents() != null) {
			for (Student student : group.getStudents()) {
				memberStr += student.getName() + ",";
				score += student.getScore();
			}
		 if(group.getStudents().size() > 0){
			 scoreResult= score/(float)group.getStudents().size();
		 }else{
			 scoreResult=0;
		 }
			group.setScore((int)Math.round(scoreResult));
		}
		if (memberStr != null && !memberStr.equals("")) {
			memberStr = memberStr.substring(0, memberStr.length() - 1);
		}
		panel.getLblMember().setText(memberStr);
		panel.getLblMember().setToolTipText(memberStr);
		panel.getLblScore().setText(String.valueOf(Math.round(scoreResult)));
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		groupList = new ArrayList<Group>();
		// 课桌绑定分组，生成内存模型
		List<Table> tables = app.getTableList();
		for (Table table : tables) {
			// 获得课桌对应的分组
			Group group = app.getTableGroup().get(table.getId());
			if (group == null) {
				group = new Group();
			}
			group.setTableId(table.getId());
			group.setTableNumber(table.getNumber());
			group.setDevices(table.getDevices());
			groupList.add(group);
		}
		Collections.sort(groupList, new Comparator<Group>() {
			@Override
			public int compare(Group o1, Group o2) {
				return o2.getScore() - o1.getScore();
			}
		});
	}

}
