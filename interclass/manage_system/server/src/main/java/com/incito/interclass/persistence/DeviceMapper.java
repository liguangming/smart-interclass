package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Device;

public interface DeviceMapper {
	List<Device> getDeviceListByCondition(@Param("imei")String imei, @Param("schoolName")String schoolName);
	
	Device getDeviceByIMEI(String imei);
	
	Integer save(Device device);
	
	void delete(int id);

	Integer update(Device device);

}
