package com.adc.da.datatable.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.datatable.entity.DataTableEO;
import com.adc.da.datatable.page.DataTableEOPage;
import com.adc.da.datatable.service.DataTableService;
import com.adc.da.datatable.vo.DataTableVO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/datatable")
@Api(tags = "报表-附表管理")
public class DataTableRestController extends BaseController<DataTableEO>{
	private static final Logger logger = LoggerFactory.getLogger(DataTableRestController.class);

	@Autowired
	private DataTableService dataTableService;

	@Autowired
	BeanMapper beanMapper;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "|DataTableEO|新增")
	@PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
	// @RequiresPermissions("sys:user:save")
	public ResponseMessage<DataTableVO> create(@RequestBody DataTableVO dataTableVO) throws Exception {
		if (StringUtils.isBlank(dataTableVO.getFieldName())) {
			return Result.error("r0014", "字段名不能为空");
		} else if (dataTableService.getDataTableEOByFieldName(dataTableVO.getFieldName(),dataTableVO.getTableName()) != null) {
			return Result.error("r0015", "字段名已存在");
		}
		DataTableEO dataTableEO = dataTableService.save(beanMapper.map(dataTableVO, DataTableEO.class));

		return Result.success(beanMapper.map(dataTableEO, DataTableVO.class));
	}

	@ApiOperation(value = "|DataTableEO|详情")
	@GetMapping("/{id}")
	// @RequiresPermissions("sys:user:get")
	public ResponseMessage<DataTableVO> getById(@NotNull @PathVariable("id") String id) throws Exception {
		DataTableVO dataTableVO = beanMapper.map(dataTableService.selectByPrimaryKey(id), DataTableVO.class);
		return Result.success(dataTableVO);
	}


	@ApiOperation(value = "|DataTableEO|修改")
	@PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
	// @RequiresPermissions("sys:user:update")
	public ResponseMessage<DataTableVO> update(@RequestBody DataTableVO dataTableVO) throws Exception {
		DataTableEO dataTableEO = beanMapper.map(dataTableVO, DataTableEO.class);
		dataTableService.updateFieldToDB(dataTableEO);
		dataTableService.updateByPrimaryKeySelective(dataTableEO);
		return Result.success(dataTableVO);
	}

	@ApiOperation(value = "|DataTableEO|分页查询")
	@GetMapping("")
	// @RequiresPermissions("sys:user:list")
	public ResponseMessage<PageInfo<DataTableVO>> page(Integer pageNo, Integer pageSize, String fieldName,String tableName) throws Exception {
		DataTableEOPage page = new DataTableEOPage();
		if (pageNo != null) {
			page.setPage(pageNo);
		}
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (StringUtils.isNotEmpty(fieldName)) {
			page.setFieldName(fieldName);
			page.setFieldNameOperator("LIKE");
		}
		if (StringUtils.isNotEmpty(tableName)) {
			page.setTableName(tableName);
			page.setTableNameOperator("LIKE");
		}
		page.setPager(new Pager());
		List<DataTableEO> rows = dataTableService.queryByPage(page);
		return Result.success(beanMapper.mapPage(getPageInfo(page.getPager(), rows), DataTableVO.class));
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "|DataTableEO|删除")
	@DeleteMapping("/{id}")
	// @RequiresPermissions("sys:user:delete")
	public ResponseMessage delete(@NotNull @PathVariable("id") String id) throws Exception {
		dataTableService.delete(id);
		return Result.success();
	}
}
