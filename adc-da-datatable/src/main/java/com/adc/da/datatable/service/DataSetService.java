package com.adc.da.datatable.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.datatable.dao.DataSetDao;
import com.adc.da.datatable.dao.DataTableDao;
import com.adc.da.datatable.entity.DataTableEO;
import com.adc.da.util.utils.StringUtils;

@SuppressWarnings("rawtypes")
@Service("dataSetService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DataSetService extends BaseService{

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DataSetService.class);

	@Autowired
	private DataSetDao dao;
	@Autowired
	private DataTableDao dataTableDao;

	public DataSetDao getDao() {
		return dao;
	}





	public List<Map<String, Object>> getDataSetById(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dao.getDataSetById(params);
	}



	private void putFieldNmame(Map<String,Object>params, String tableName) {

		StringBuilder fieldName = new StringBuilder();
		if(StringUtils.isNotEmpty(tableName)){
			List<DataTableEO> list = dataTableDao.getFieldListByTableName(tableName);
			int listRowCount = list.size();
			for(int i=0;i<listRowCount;i++){
				DataTableEO dataTableEO = list.get(i);
				fieldName.append(dataTableEO.getFieldName());
				if(i<listRowCount-1){
					fieldName.append(",");
				}
			}
			if(StringUtils.isNotEmpty(fieldName)){
				fieldName.insert(0, ",");
			}
		}
		params.put("fieldName", fieldName.toString());

	}

	public List<Map<String, Object>> getDataSetById(String tableName, Map<String,Object>params) {
		/**执行获取T_FORM_FIELD表中FIELD_NAME字段**/

		putFieldNmame(params,tableName);

		return dao.getDataSetById(params);
	}



	public List<Map<String, Object>> saveDataSetNew(Map<String, Object> params, String mainField,
												 String mainFieldValue, String subFieldV,String tableName) {

		putFieldNmame(params,tableName);
		dao.insertMainTable(params);//写主表
		if(StringUtils.isNotEmpty(tableName)){
			dao.insertSubTable(params);//写附表ID
			if(!StringUtils.isEmpty(subFieldV)){
				params.put("subFieldV", subFieldV.replaceAll("&#39;", "'"));
				dao.addKeyValueById(params);
			}
		}
		return getDataSetById(params);
	}



	public List<Map<String, Object>> updateDataSetNew( String mainFieldV,
												   String subFieldV, Map<String ,Object>params, String tableName) {

		if(!StringUtils.isEmpty(mainFieldV)){
			params.put("mainFieldV", mainFieldV.replaceAll("&#39;", "'"));
			dao.updateMainTableById(params);
		}
		if(!StringUtils.isEmpty(subFieldV)){
			params.put("subFieldV", subFieldV.replaceAll("&#39;", "'"));
			dao.addKeyValueById(params);
		}
		putFieldNmame(params,tableName);
		return getDataSetById(params);
	}

	public void delete(Map<String, Object> params) {

		if(null!=params.get("tableName")){

			dao.deleteSubTableById(params);
		}
		dao.deleteMainTableById(params);
	}
	public List<String> allTable(){
		return dao.allTable();

	}

	public List<String> tableToAllColumn() {
		return dao.tableToAllColumn();
	}

	public List<String> tableToColumn(String tableName) {
		return dao.tableToColumn(tableName);
	}

	public List<String> allTable2() {
		return dao.allTable2();
	}
	public List<String> colTOComment(String tableName,String colName) {
		return dao.colTOComment(tableName,colName);
	}

    public List<String> forCode(String test) {
		return dao.forCode(test);
    }
}
