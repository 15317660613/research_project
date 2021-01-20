package com.adc.da.datatable.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.datatable.entity.DataTableEO;


public interface DataTableDao extends BaseDao<DataTableEO>{
	
	public DataTableEO getDataTableEOByFieldName(@Param("fieldName") String fieldName,@Param("tableName") String tableName);

	//public void addFieldToDB(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("fieldTypeLength") String fieldTypeLength, @Param("isNull") String isNull);

	public void addFieldToDB(Map<String, Object> params);

	public void updateFieldToDB(Map<String, Object> params);
	
	public void updateFieldNameToDB(Map<String, Object> params);

	public void deleteFieldById(String id);

	public void deleteFieldFromDt(Map<String, Object> params);

	public List<DataTableEO> getFieldListByTableName(String tableName);


}
