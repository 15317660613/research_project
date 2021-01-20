package com.adc.da.datatable.vo;

public class DataTableVO {
	/**id*/
    private String id;
    /**字段名称*/
    private String fieldName;
    /**功能注释*/
	private String content;
	/**字段默认值*/
	private String fieldDefault;
	/*** 字段校验是否必填*/
	private String fieldMustInput;
	/**控件校验*/
	private String fieldValidType;
	/**是否主键*/
	private String isKey;	
	/**是否允许空值*/
	private String isNull;
	/**表单是否显示*/
	private String isShow;
	/**表ID*/
	private String tableName;
	/**字段类型*/
	private String fieldType;
	/**字段长度*/
	private Integer fieldLength;
	/**控件显示类型*/
	private String showType;
	/**创建时间*/
	private String createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFieldDefault() {
		return fieldDefault;
	}
	public void setFieldDefault(String fieldDefault) {
		this.fieldDefault = fieldDefault;
	}
	public String getFieldMustInput() {
		return fieldMustInput;
	}
	public void setFieldMustInput(String fieldMustInput) {
		this.fieldMustInput = fieldMustInput;
	}
	public String getFieldValidType() {
		return fieldValidType;
	}
	public void setFieldValidType(String fieldValidType) {
		this.fieldValidType = fieldValidType;
	}
	public String getIsKey() {
		return isKey;
	}
	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public Integer getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
