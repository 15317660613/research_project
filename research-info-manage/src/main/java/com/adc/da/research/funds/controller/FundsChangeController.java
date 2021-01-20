package com.adc.da.research.funds.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.funds.service.ChangeService;
import com.adc.da.research.funds.vo.change.BudgetFundVO;
import com.adc.da.research.funds.vo.change.DetailChangeVO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.research.project.vo.ChangeProjectVO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 经费调整Controller层
 * @Auther: yanyujie
 * @Date: 2020/12/03
 * @Description:
 */

@RestController
@RequestMapping("/${restPath}/research/funds/change")
@Api(tags = "科研系统|科研变更|FundsChangeController")
public class FundsChangeController extends BaseController<ProjectDataEO> {

    private static final Logger logger = LoggerFactory.getLogger(FundsChangeController.class);

    @Autowired
    private ProjectDataEOService projectDataEOService;

    @Autowired
    private ChangeService changeService;

    @ApiOperation(value = "经费变更查询（分页）")
    @GetMapping("/changePage")
    public ResponseMessage< Map<String, List<ChangeProjectVO>>> queryByChangePage() throws Exception{
      //  final List<DetailChangeVO> detailChangeVOS = changeService.comparedBudgetDetail("29SYMREQTE", 4);
        Map<String, List<ChangeProjectVO>> stringListMap = changeService.allChange("29SYMREQTE");

        return Result.success(stringListMap);
    }

    @ApiOperation(value = "经费科目调账信息查询")
    @GetMapping("/queryByFundChange")
    public ResponseMessage queryByFundChange() throws Exception{
        //  final List<DetailChangeVO> detailChangeVOS = changeService.comparedBudgetDetail("29SYMREQTE", 4);
       // changeService.allChange("29SYMREQTE");

//        return Result.success(changeService.comparedDetailAll("29SYMREQTE"));
        return Result.success(changeService.comparedDetailItem("29SYMREQTE",1,"2018"));

    }

    @ApiOperation(value = "查看--详情")
    @GetMapping("/fundChangeDetails")
    public ResponseMessage<List<DetailChangeVO>> fundChangeDetails (String projectId
            ,Integer type
            ,String param) throws Exception{
        return Result.success(changeService.comparedDetailItem(projectId,type,param));

    }

    @ApiOperation(value = "查看")
    @GetMapping("/fundChangeView")
    public ResponseMessage<Map<String,List<DetailChangeVO>>> fundChangeView(String id) throws Exception{
        //  final List<DetailChangeVO> detailChangeVOS = changeService.comparedBudgetDetail("29SYMREQTE", 4);
        // changeService.allChange("29SYMREQTE");

        return Result.success(changeService.comparedDetailAll(id));
//        return Result.success(changeService.comparedDetailItem("29SYMREQTE",1,"2018"));

    }

    @ApiOperation(value = "查看Fund")
    @GetMapping("/fundChangeFund")
    public ResponseMessage<List<BudgetFundVO>> fundChangeFund(String id) throws Exception{
        final List<BudgetFundVO> fundChangeVOS = changeService.fundChangeFund(id);
        return Result.success(fundChangeVOS);

    }

    @ApiOperation(value = "查看Fund详情")
    @GetMapping("/fundChangeFundDetails")
    public ResponseMessage<List<BudgetFundVO>> fundChangeFundDetails(String id,String year) throws Exception{
        final List<BudgetFundVO> fundChangeVOS = changeService.fundChangeFundDetails(id,year);
        return Result.success(fundChangeVOS);

    }

    @ApiOperation(value = "|FundsChangeEo|经费调整列表查询")
    @GetMapping("/fundChangePage")
    public ResponseMessage<PageInfo<ProjectDataEO>> queryByChangePage(ProjectDataEOPage page) throws Exception{
        page.setPropertyChanges("经费预算");
        page.setChangeStatus("审核通过");
        List<ProjectDataEO> list=projectDataEOService.queryByChangePage(page);

        return Result.success(getPageInfo(page.getPager(), list));
    }



}
