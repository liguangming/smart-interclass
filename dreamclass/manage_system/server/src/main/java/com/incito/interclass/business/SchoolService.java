
package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.School;
import com.incito.interclass.persistence.SchoolMapper;

@Service
public class SchoolService {

	@Autowired
	private SchoolMapper schoolMapper;
	
	public List<School> getSchoolListByCondition(String name, int schoolType) {
		return schoolMapper.getSchoolListByCondition(name, schoolType);
	}

	public boolean saveSchool(School school) {
		int id = (Integer) schoolMapper.save(school);
		return id != 0;
	}

	public void deleteSchool(int schoolId) {
		schoolMapper.delete(schoolId);
	}

	public int getSchoolIdByName(String schoolName) {
		List<Integer> schools=schoolMapper.getSchoolIdByName(schoolName);
		return schools.get(0);
	}
}