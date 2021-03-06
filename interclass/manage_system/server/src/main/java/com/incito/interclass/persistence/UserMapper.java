package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;

public interface UserMapper {
	Admin loginForAdmin(Admin admin);

	Teacher loginForTeacher(Teacher teacher);
	
	Student loginForStudent(Student student);

	List<Student> getStudentByGroupId(int groupId);
	
	List<Student> getStudentByCaptainId(int captainId);
	
	List<Teacher> getTeacherListByCondition(@Param("name") String name,
			@Param("schoolName") String schoolName);

	List<Student> getStudentListByCondition(@Param("name") String name,
			@Param("schoolName") String schoolName);
	
	Student getStudent(@Param("name")String name, @Param("number")String number);
	
	Student getStudentByImei(String imei);
	
	Student getStudentBySchoolId(@Param("name") String name,
			@Param("number") String number, @Param("schoolId") int schoolId);
	
	Integer saveUser(User user);
	
	Integer saveTeacher(Teacher teacher);

	Integer saveStudent(Student student);

	void deleteUser(int id);
	
	void deleteTeacher(int teacherId);

	void deleteStudent(int studentId);
	@Transactional
	Integer changePoint(@Param("studentId")String studentId,@Param("score")int score);
	@Transactional
	Student getScore(String studentId);
	@Transactional
	Integer updateMedals(@Param("groupId")int groupId,@Param("medals")String medals);

	List<Student> getStudentByClassId(int classId);

	int getTeacherByUname(String uname);

	int getTeacherByIdCard(String idcard);

	Teacher getTeacherById(int teacherId);

	Student getStudentById(int studentId);

	Integer updateTeacherById(Teacher teacher);

	Integer updateUserById(User user);

	Integer updateStudentById(Student student);


}
