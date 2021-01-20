package com.adc.da.datatable.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.adc.da.util.utils.UUID;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.datatable.service.DataSetService;
import com.adc.da.datatable.service.DataTableService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.datatable.entity.DataTableEO;
import com.adc.da.datatable.page.DataSetEOPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/dataset")
@Api(tags = "报表-数据集管理")
public class DataSetRestController extends BaseController<Map<String,Object>> {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DataSetRestController.class);

	@Autowired
	private DataTableService dataTableService;

	@Autowired
	private DataSetService dataSetService;

	@Autowired
	BeanMapper beanMapper;

//	@SuppressWarnings("unchecked")
//	@ApiOperation(value = "|数据集列表|分页查询")
//	@GetMapping("/{mainTableName}")
////    @RequiresPermissions("sys:user:get")
//	public ResponseMessage<PageInfo<Map<String,Object>>> page(	Integer pageNo,  	    Integer pageSize,
//						@NotNull @PathVariable("mainTableName") String mainTableName, 	String mainPrimaryKey,
//											  					String tableName, 		String subPrimaryKey,
//											  					String queryParam) throws Exception {
//		//返回结果集
//		/**执行获取TS_FORM_FIELD表中FIELD_NAME字段**/
//		StringBuilder fieldName = new StringBuilder();
//		if(StringUtils.isNotEmpty(tableName)){
//			List<DataTableEO> list = dataTableService.getFieldListByTableName(tableName);
//			int listRowCount = list.size();
//			for(int i=0;i<listRowCount;i++){
//				DataTableEO dataTableEO = list.get(i);
//				fieldName.append(dataTableEO.getFieldName());
//				if(i<listRowCount-1){
//					fieldName.append(",");
//				}
//			}
//			if(StringUtils.isNotEmpty(fieldName)){
//				fieldName.insert(0, ",");
//			}
//		}
//
//		/**根据获取的字段从附表T_S_USER中查询对应的数据**/
//		DataSetEOPage page = new DataSetEOPage();
//		if (pageNo != null) {
//			page.setPage(pageNo);
//		}
//		if (pageSize != null) {
//			page.setPageSize(pageSize);
//		}
//		if (!StringUtils.isEmpty(fieldName)) {
//			page.setFieldName(fieldName.toString());
//		}
//		if (!StringUtils.isEmpty(tableName)) {
//			page.setTableName(tableName);
//		}
//		if (!StringUtils.isEmpty(mainTableName)) {
//			page.setMainTableName(mainTableName);
//		}
//		if (!StringUtils.isEmpty(queryParam)) {
//			page.setQueryParam(queryParam.replaceAll("&#39;", "'"));
//		}
//		if(!StringUtils.isEmpty(mainPrimaryKey)){
//			page.setMainPrimaryKey(mainPrimaryKey);
//		}
//		if(!StringUtils.isEmpty(subPrimaryKey)){
//			page.setSubPrimaryKey(subPrimaryKey);
//		}
//		page.setPager(new Pager());
//		List<Map<String,Object>> rows = dataSetService.queryByPage(page);
//		return Result.success(getPageInfo(page.getPager(), rows));
//	}

	@ApiOperation(value = "数据集详情")
	@GetMapping("/{mainTableName}/{idParam}")
//    @RequiresPermissions("sys:user:get")
	public ResponseMessage<Map<String, Object>> getDataSetById(@NotNull @PathVariable("mainTableName") String mainTableName,
															   String mainPrimaryKey, String tableName,
															   String subPrimaryKey,
															   @NotNull @PathVariable("idParam") String idParam) throws Exception {
		//返回结果集
		Map<String, Object> resultMap = new HashMap<>();

		/**根据获取的字段及USERID从主表TS_USER和附表T_S_USER中查询对应的数据**/
		try {
			if (StringUtils.isEmpty(mainPrimaryKey)) {
				mainPrimaryKey = "usid";
			}

			if (StringUtils.isEmpty(subPrimaryKey)) {
				subPrimaryKey = "id";
			}

			Map<String, Object> params = new HashMap<>();
			params.put("idParam", idParam);
			params.put("mainPrimaryKey", mainPrimaryKey);
			params.put("subPrimaryKey", subPrimaryKey);
			params.put("tableName", tableName);
			params.put("mainTableName", mainTableName);

			List<Map<String, Object>> dataSetList = dataSetService.getDataSetById(tableName, params);

			resultMap.put("dataSetList", dataSetList);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.toString());
		}
		return Result.success("", "", resultMap);
	}


	@ApiOperation(value = "新增|数据集New|")
	@PostMapping("/{mainTableName}")
//    @RequiresPermissions("sys:user:get")
	public ResponseMessage<Map<String, Object>> saveDataSetNew(@NotNull @PathVariable("mainTableName") String mainTableName,
															   String mainPrimaryKey, String tableName,
															   String subPrimaryKey, String mainField,
															   String mainFieldValue, String subFieldV) throws Exception {


		if (StringUtils.isEmpty(mainPrimaryKey)) {
			mainPrimaryKey = "usid";
		}

		if (StringUtils.isEmpty(subPrimaryKey)) {
			subPrimaryKey = "id";
		}

		String id = UUID.randomUUID10();
		Map<String, Object> params = new HashMap<>();

		params.put("tableName", tableName);
		params.put("mainTableName", mainTableName);
		params.put("subPrimaryKey", subPrimaryKey);
		params.put("mainPrimaryKey", mainPrimaryKey);

		params.put("mainField", mainField);
		params.put("mainFieldValue", mainFieldValue.replaceAll("&#39;", "'"));

		params.put("id", "'" + id + "'");  //uuid
		params.put("idParam", mainPrimaryKey + "='" + id + "'");

		List<Map<String, Object>> dataSetList = dataSetService.saveDataSetNew(params, mainField, mainFieldValue, subFieldV, tableName);


		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataSetList", dataSetList);
		return Result.success("", "", resultMap);

	}


	@ApiOperation(value = "修改|数据集New|")
	@PutMapping("/{mainTableName}/{id}")
//    @RequiresPermissions("sys:user:get")
	public ResponseMessage<Map<String, Object>> updateDataSetNew(@NotNull @PathVariable("mainTableName") String mainTableName,
																 String mainPrimaryKey,
																 String tableName, String subPrimaryKey,
																 @NotNull @PathVariable("id") String id,
																 String mainFieldV, String subFieldV) throws Exception {
		if (StringUtils.isEmpty(mainPrimaryKey)) {
			mainPrimaryKey = "usid";
		}

		if (StringUtils.isEmpty(subPrimaryKey)) {
			subPrimaryKey = "id";
		}
		Map<String, Object> params = new HashMap<>();

		params.put("tableName", tableName);
		params.put("mainTableName", mainTableName);
		params.put("subPrimaryKey", subPrimaryKey);
		params.put("mainPrimaryKey", mainPrimaryKey);

		params.put("id", "'" + id + "'");
		params.put("idParam", mainPrimaryKey + "='" + id + "'");


		List<Map<String, Object>> dataSetListNew = dataSetService.updateDataSetNew(mainFieldV, subFieldV, params, tableName);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataSetList", dataSetListNew);
		return Result.success("", "", resultMap);

	}


	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "|数据集|物理删除")
	@DeleteMapping("/{mainTableName}/{id}")
	// @RequiresPermissions("sys:user:delete")
	public ResponseMessage delete(@NotNull @PathVariable("mainTableName") String mainTableName, String mainPrimaryKey,
								  String tableName, String subPrimaryKey,
								  @NotNull @PathVariable("id") String id) throws Exception {
		if (StringUtils.isEmpty(mainPrimaryKey)) {
			mainPrimaryKey = "usid";
		}

		if (StringUtils.isEmpty(subPrimaryKey)) {
			subPrimaryKey = "id";
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		params.put("mainTableName", mainTableName);
		params.put("id", id);
		params.put("mainPrimaryKey", mainPrimaryKey);
		params.put("subPrimaryKey", subPrimaryKey);
		dataSetService.delete(params);
		return Result.success();
	}


	@SuppressWarnings("unchecked")
	@ApiOperation(value = "|数据集列表|分页查询")
	@GetMapping("/page")
//    @RequiresPermissions("sys:user:get")
	public ResponseMessage<PageInfo<Map<String, Object>>> page(Integer pageNo, Integer pageSize,
															   String tableName,
															   String Param,
															   String queryParam, String associates) throws Exception {
		//返回结果集

		DataSetEOPage page = new DataSetEOPage();
		if (pageNo != null) {
			page.setPage(pageNo);
		}
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (!StringUtils.isEmpty(queryParam)) {
			page.setQueryParam(queryParam.replaceAll("&#39;", "'"));
		}
		if (!StringUtils.isEmpty(Param)) {
			page.setParam(Param);
		}
		//字符串转map格式
		Gson gson = new Gson();
		Map<String, String> Associates = new HashMap<String, String>();
		Associates = gson.fromJson(associates, Associates.getClass());
		System.out.println(Associates);
		//字符串转数组格式
		String[] TableName = tableName.split(",");
		page.setMainTableName(TableName[0]);

		int Count = 0;  //数组中元素个数
		for (int c = 0; c < TableName.length; c++) {
			if (null != TableName[c]) Count++;
		}

		//表名排序，防止笛卡尔积

		String[] rank = tableName.split(",");
		rank[0] = TableName[0];
		for (int i = 0; i < Count - 1; i++) {
			TableName[i] = TableName[i + 1];
		}
		TableName[Count - 1] = null;
		int x = 1;
		for (int r = 0; r < rank.length - 1; r++) {
			for (int j = 0; j < TableName.length - 1; j++) {
				if (Associates.get("" + rank[r] + "" + "&" + "" + TableName[j] + "") != null || Associates.get("" + TableName[j] + "" + "&" + "" + rank[r] + "") != null) {
					rank[x++] = TableName[j];
					for (int a = j; a < Count - 1; a++) {
						TableName[a] = TableName[a + 1];
					}
					TableName[Count - 1] = null;
				}
			}
		}
//		for (int k = 0; k < rank.length; k++) {
//			System.out.print(rank[k] + " ");
//			System.out.println("");
//		}

		//字符串拼接
		int i, a;
		String test = " ";
		for (i = 1; i < Count; i++) {
			test = test + "left join" + " " + rank[i] + " " + "on" + " " + "1=1" + " ";
			for (a = 0; a < i; a++) {
				if (Associates.get(rank[a] +  "&" + rank[i] ) != null) {
					test = test + " " + "and" + " " + Associates.get(rank[a] + "&" + rank[i] ) + " ";
				}
				if (Associates.get(rank[i] + "&" +rank[a] ) != null) {
					test = test + " " + "and" + " " + Associates.get(rank[i] + "&" +  rank[a] ) + " ";
				}
			}
		}
		page.setTest(test);

		page.setPager(new Pager());
		try {
			List<Map<String, Object>> rows = dataSetService.queryByPage(page);
			return Result.success(getPageInfo(page.getPager(), rows));
		} catch (Exception var8) {
			logger.error("查询用户数据异常：", var8);
			return Result.error("-1", "查询参数异常");
		}
	}

	@ApiOperation(value = "数据表及全部对应列查询")
	@GetMapping("")
	public ResponseMessage allTableAndColumn() {
		List<String> A = dataSetService.tableToAllColumn();
		List<String> B = dataSetService.allTable();
		B.addAll(A);
		return Result.success(B);
	}


	@ApiOperation(value = "数据表查询")
	@GetMapping("/allTable")
	public ResponseMessage allTable() {
		return Result.success(dataSetService.allTable2());
	}


	@ApiOperation(value = "数据表及对应列查询")
	@GetMapping("/tableToColumn/{tableName}")
	public ResponseMessage tableToColumn(@NotNull @PathVariable("tableName") String tableName){
		return Result.success(dataSetService.tableToColumn("'"+tableName+"'"));
	}

	@ApiOperation(value = "注释查询")
	@GetMapping("/colTOComment/{tableName}/{colName}")
	public ResponseMessage colTOComment(@NotNull @PathVariable("tableName") String tableName,@NotNull @PathVariable("colName") String colName){
		return Result.success(dataSetService.colTOComment("'"+tableName+"'","'"+colName+"'"));
	}

    @ApiOperation(value = "sql查询")
    @GetMapping("/forCode/{test}")
    public ResponseMessage forCode(@NotNull @PathVariable("test") String test){
        return Result.success(dataSetService.forCode(test));
    }


}
