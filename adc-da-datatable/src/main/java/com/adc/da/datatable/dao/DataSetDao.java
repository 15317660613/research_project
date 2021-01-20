package com.adc.da.datatable.dao;

import java.util.List;
import java.util.Map;

import com.adc.da.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

@SuppressWarnings("rawtypes")
public interface DataSetDao extends BaseDao{

	public List<Map<String, Object>> getDataSetById(Map<String, Object> params);

	public void insertMainTable(Map<String, Object> params);

	public void addKeyValueById(Map<String, Object> params);

	public void insertSubTable(Map<String, Object> params);

	public void updateMainTableById(Map<String, Object> params);

	public void deleteMainTableById(Map<String, Object> params);

	public void deleteSubTableById(Map<String, Object> params);

	public List<String> allTable();

	public List<String> tableToAllColumn();

	public  List<String> tableToColumn( @Param("tableName") String var1);

    public	List<String> allTable2();

	public  List<String> colTOComment( @Param("tableName") String var1,@Param("colName") String var2);

	public List<String> forCode(@Param("test") String var1);
}
