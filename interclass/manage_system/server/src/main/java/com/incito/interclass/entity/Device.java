package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Date;

public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5030861381666684624L;
	private int id;
	private String imei;
	private int tableId;
	private Date ctime;

	private String className;
	private String schoolName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
