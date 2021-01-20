package com.adc.da.datatable.page;

import com.adc.da.base.page.BasePage;

public class DataSetEOPage extends BasePage{
	/**id*/
    private String id;
    private String idOperator = "=";
    
    /**字段名称*/
    private String fieldName;
    private String fieldNameOperator = "=";
    
    private String tableName;
	private String tableNameOperator = "=";
	
	private String mainTableName;
	private String mainTableNameOperator = "=";
	
	private String queryParam;

	private String MainPrimaryKey;
	private String SubPrimaryKey;


	private String Param; //列名

	private String  associates; //表关系
	private String test;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdOperator() {
		return idOperator;
	}
	public void setIdOperator(String idOperator) {
		this.idOperator = idOperator;
	}



	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldNameOperator() {
		return fieldNameOperator;
	}
	public void setFieldNameOperator(String fieldNameOperator) {
		this.fieldNameOperator = fieldNameOperator;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableNameOperator() {
		return tableNameOperator;
	}
	public void setTableNameOperator(String tableNameOperator) {
		this.tableNameOperator = tableNameOperator;
	}
	public String getMainTableName() {
		return mainTableName;
	}
	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}
	public String getMainTableNameOperator() {
		return mainTableNameOperator;
	}
	public void setMainTableNameOperator(String mainTableNameOperator) {
		this.mainTableNameOperator = mainTableNameOperator;
	}
	public String getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}




	public String getParam() {
		return Param;
	}

	public void setParam(String Param) {
		this.Param = Param;
	}

	public String getAssociates() {
		return associates;
	}

	public void setAssociates(String associates) {
		this.associates = associates;
	}

	public String getMainPrimaryKey() {
		return MainPrimaryKey;
	}

	public void setMainPrimaryKey(String mainPrimaryKey) {
		MainPrimaryKey = mainPrimaryKey;
	}

	public String getSubPrimaryKey() {
		return SubPrimaryKey;
	}

	public void setSubPrimaryKey(String subPrimaryKey) {
		SubPrimaryKey = subPrimaryKey;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
