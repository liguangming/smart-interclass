package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.cloud.PaperWork;

public interface PaperWorkMapper {

	/**
	 * 保存随堂练习
	 * 
	 * @param paperWork
	 * @return
	 */
	Integer save(PaperWork paperWork);

	/**
	 * 保存随堂练习
	 * 
	 * @param paperWork
	 * @return
	 */
	List<PaperWork> query(@Param("teacher_id") String teacher_id, @Param("quizid") String quizid);

}
