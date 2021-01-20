package com.adc.da.statistics.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.statistics.entity.DataBoardTreeEO;
import com.adc.da.statistics.service.DataBoardService;
import com.adc.da.statistics.service.DataBoardSyncService;
import com.adc.da.statistics.service.DataBoardTreeService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${restPath}/dataBoard")
@Api(tags = "|统计-数据看板|")
public class DataBoardController extends BaseController<BusinessWorktimeEO> {

    @Autowired
    private DataBoardService dataBoardService;

    @Autowired
    private DataBoardSyncService dataBoardSyncService;

    @Autowired
    private DataBoardTreeService treeService;

    /**
     * 手动更新合同金额信息到Oracle，每天5时会执行定时任务刷新
     *
     * @param saveFlag
     * @return
     * @see DataBoardTimer#autoSync
     */
    @ApiOperation(value = "同步-合同金额到oracle")
    @GetMapping("/sync_Contract_Amount")
    public ResponseMessage sync(boolean saveFlag) {
        dataBoardSyncService.syncDataFromES(saveFlag);
        return Result.success();
    }

    /**
     * 详情
     *
     * @param year
     * @param deptId
     * @param budgetId
     * @return
     */
    @ApiOperation(value = "详情部分 ")
    @GetMapping("/detail")
    public ResponseMessage<Object> detail(Integer year, String deptId, String budgetId, String mainBoard) {
        if (StringUtils.isNotEmpty(budgetId)) {
            return Result.success(dataBoardService.budgetBoard(budgetId, year));

        } else if (StringUtils.isNotEmpty(deptId)) {
            return Result.success(dataBoardService.orgBoard(deptId, year));
        } else if (StringUtils.isNotEmpty(mainBoard)) {
            return Result.success(dataBoardService.mainBoard(year, mainBoard.split(",")));
        } else {
            return Result.error("-1", "", null);
        }
    }

    /**
     * @param year
     * @param pageSize
     * @param type     0查开票， 1查合同 ， 2全部查询
     * @return
     */
    @ApiOperation(value = "业务树 ")
    @GetMapping("/budgetTree/{year}/{type}")
    public ResponseMessage<Map<String, List<DataBoardTreeEO>>> budgetTree(@PathVariable("year") Integer year,
        Integer pageSize,
        @PathVariable("type") Integer type) {
        return Result.success(treeService.getBudgetTree(year, pageSize, type));
    }

    @ApiOperation(value = "组织树 ")
    @GetMapping("/deptTree")
    public ResponseMessage<List<OrgWithLevelEO>> deptTree() {
        return Result.success(treeService.getDeptTree());
    }
}
