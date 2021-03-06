package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.LogoUtils;

public class PrepareGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private Application app = Application.getInstance();
	private List<GroupPanel> gpList = new ArrayList<GroupPanel>();

	public PrepareGroupPanel() {
		setLayout(null);
		setOpaque(true);
		initView();
		refresh();
	}

	private void initView() {
		int x = 10, y = 10;
		for (int i = 0; i < 12; i++) {
			GroupPanel groupPanel = new GroupPanel();
			groupPanel.setBounds(x, y, 825, 100);
			add(groupPanel);
			groupPanel.setVisible(false);
			gpList.add(groupPanel);
			y += 110;
		}
	}

	public void refresh() {
		hideGroup();
		Set<Group> groups = app.getGroupList();
		Iterator<Group> it = groups.iterator();
		int index = 0;
		while (it.hasNext()) {
			Group group = it.next();
			GroupPanel groupPanel = gpList.get(index++);
			groupPanel.setVisible(true);
			groupPanel.setGroup(group);
			String logo = LogoUtils.getInstance().getLogo24(group.getLogo());
			ImageIcon icon = new ImageIcon(logo);
			groupPanel.getLblLogo().setIcon(icon);
			groupPanel.getLblGroupName().setText(group.getName());
			groupPanel.getLblGroupName().setVisible(true);
			List<JLabel> lblStudents = groupPanel.getStudentList();
			List<Student> students = group.getStudents();
			for (int i = 0; i < students.size(); i++) {
				Student student = students.get(i);
				JLabel lblName = lblStudents.get(i);
				lblName.setText(student.getName());
				lblName.setVisible(true);
				if (!student.isLogin()) {
					lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
				} else {
					lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
				}
			}
		}
	}
	
	private void hideGroup() {
		for (GroupPanel groupPanel : gpList) {
			groupPanel.setVisible(false);
			ImageIcon icon = new ImageIcon("");
			groupPanel.getLblLogo().setIcon(icon);
			groupPanel.getLblGroupName().setText("");
			groupPanel.getLblGroupName().setVisible(false);
			List<JLabel> lblStudents = groupPanel.getStudentList();
			for (JLabel lblName : lblStudents) {
				lblName.setText("");
				lblName.setVisible(false);
			}
		}
	}
}
