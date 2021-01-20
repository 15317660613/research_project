package com.adc.da.datatable.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.datatable.dao.DataTableDao;
import com.adc.da.datatable.entity.DataTableEO;
import com.adc.da.util.utils.UUID;


@Service("dataTableService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DataTableService extends BaseService<DataTableEO, String>{
	private static final Logger logger = LoggerFactory.getLogger(DataTableService.class);

    @Autowired
	private DataTableDao dao;


	public DataTableDao getDao() {
		return dao;
	}
	
	@Transactional(readOnly = true)
	public DataTableEO getDataTableEOByFieldName(String fieldName, String tableName) {
		return dao.getDataTableEOByFieldName(fieldName,tableName);
	}
	
	@Transactional
	public DataTableEO save(DataTableEO dataTableEO) {
		dataTableEO.setId(UUID.randomUUID10());
		String fieldType = dataTableEO.getFieldType();
		Integer fieldLength = dataTableEO.getFieldLength();
		String fieldTypeLength = fieldType+"("+fieldLength.toString()+")";
		if("NUMBER".equals(fieldType)){
			fieldTypeLength = fieldType+"("+fieldLength.toString()+",0)";
		}
		
		//往TS_FORM_FIELD表中添加字段信息
		dao.insert(dataTableEO);
		//往数据表中添加字段
		addFieldToDB(dataTableEO.getTableName(), dataTableEO.getFieldName(), fieldTypeLength, dataTableEO.getIsNull());
		return dataTableEO;
	}

	public void addFieldToDB(String tableName, String fieldName, String fieldTypeLength, String isNull) {
		Map<String, Object> params = new HashMap<String, Object>();  
        params.put("tableName", tableName);  
        params.put("fieldName", fieldName);  
        params.put("fieldTypeLength", fieldTypeLength);  
        params.put("isNull", isNull);  
		dao.addFieldToDB(params);
	}

	public void updateFieldToDB(DataTableEO dataTableEO) throws Exception {
		DataTableEO oldDataTableEO = this.selectByPrimaryKey(dataTableEO.getId());
		String fieldType = dataTableEO.getFieldType();
		String fieldTypeLength = fieldType+"("+dataTableEO.getFieldLength().toString()+")";
		if("NUMBER".equals(fieldType)){
			fieldTypeLength = fieldType+"("+dataTableEO.getFieldLength().toString()+",0)";
		}
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("tableName", dataTableEO.getTableName());  
        params.put("oldFieldName", oldDataTableEO.getFieldName());  
        params.put("newFieldName", dataTableEO.getFieldName());  
        params.put("fieldTypeLength", fieldTypeLength);  
   
        dao.updateFieldToDB(params);
        if(!oldDataTableEO.getFieldName().equals(dataTableEO.getFieldName())){
        	dao.updateFieldNameToDB(params);
        }
	}

	public void delete(String id) throws Exception {

		DataTableEO dataTableEO = this.selectByPrimaryKey(id);
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("tableName", dataTableEO.getTableName());  
        params.put("fieldName", dataTableEO.getFieldName());  
        dao.deleteFieldFromDt(params);
		dao.deleteFieldById(id);
	}


	
	/*****根据tableName获取对应的字段列表*****/
	public List<DataTableEO> getFieldListByTableName(String tableName) {
		return dao.getFieldListByTableName(tableName);
	}
}
