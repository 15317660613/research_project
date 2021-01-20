package com.adc.da.datatable.entity;

import java.io.Serializable;

import com.adc.da.base.entity.BaseEntity;

public class DataTableEO extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 3658632939727891047L;
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
	
	/**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>extInfo -> ext_info</li>
     * <li>usname -> usname</li>
     * <li>password -> password</li>
     * <li>delFlag -> del_flag</li>
     * <li>account -> account</li>
     * <li>usid -> usid</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "id": return "id";
            case "fieldName": return "field_name";
            case "content": return "content";
            case "fieldDefault": return "field_default";
            case "fieldMustInput": return "field_must_input";
            case "fieldValidType": return "field_valid_type";
            case "isKey": return "is_key";
            case "isNull": return "is_null";
            case "isShow": return "is_show";
            case "tableName": return "table_name";
            case "fieldType": return "field_type";
            case "fieldLength": return "field_length";
            case "showType": return "show_type";
            case "createTime": return "create_time";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>ext_info -> extInfo</li>
     * <li>usname -> usname</li>
     * <li>password -> password</li>
     * <li>del_flag -> delFlag</li>
     * <li>account -> account</li>
     * <li>usid -> usid</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
	        case "id": return "id";
	        case "field_name": return "fieldName";
	        case "content": return "content";
	        case "field_default": return "fieldDefault";
	        case "field_must_input": return "fieldMustInput";
	        case "field_valid_type": return "fieldValidType";
	        case "is_key": return "isKey";
	        case "is_null": return "isNull";
	        case "is_show": return "isShow";
	        case "table_name": return "tableName";
	        case "field_type": return "fieldType";
	        case "field_length": return "fieldLength";
	        case "show_type": return "showType";
	        case "create_time": return "createTime";
            default: return null;
        }
    }

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
