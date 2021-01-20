package com.adc.da.datatable.page;

import com.adc.da.base.page.BasePage;

public class DataTableEOPage extends BasePage{
	/**id*/
    private String id;
    private String idOperator = "=";
    /**字段名称*/
    private String fieldName;
    private String fieldNameOperator = "=";
    /**功能注释*/
	private String content;
	private String contentOperator = "=";
	private String tableName;
	private String tableNameOperator = "=";
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentOperator() {
		return contentOperator;
	}
	public void setContentOperator(String contentOperator) {
		this.contentOperator = contentOperator;
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
	
	
}
