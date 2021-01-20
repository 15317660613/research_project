package com.adc.da.research.controller;

import com.adc.da.budget.entity.Project;
import com.adc.da.research.entity.CheckFormEO;
import com.adc.da.research.service.ProjectSaveService;
import com.adc.da.research.vo.CheckBaseVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.RequestUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-08-26
 */
@RestController
@RequestMapping("/${restPath}/research/save")
@Api(tags = "|科研类项目模块-校验存储相关|")
public class ProjectSaveController {

    @Autowired
    private ProjectSaveService projectSaveService;

    /**
     * 进行科研表单校验
     *
     * @param projectId
     * @param midterm
     * @param showAll   是否过滤流程中的检查内容
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-09-06
     **/
    @GetMapping(value = "/checkpoint")
    @ApiOperation("节点检查纪录表单数据")
    public ResponseMessage<CheckBaseVO> checkPointData(String projectId, boolean midterm, boolean showAll) {
        if (midterm) {
            return Result.success(projectSaveService.checkMidtermData(projectId));

        } else {
            return Result.success(projectSaveService.checkpointData(projectId, showAll));

        }
    }

    /**
     * 进行科研表单校验
     */
    @GetMapping(value = "/check")
    @ApiOperation("校验表单数据")
    public ResponseMessage<Map<String, Object>> researchProjectCheck(String projectId) {
        Map<String, Object> resultMap = new HashMap<>();
        CheckFormEO checkFormEO = projectSaveService.researchProjectCheck(projectId);
        String[] str = checkFormEO.getStr();
        int[] count = checkFormEO.getCount();

        for (int i = 0; i < 10; i++) {
            if (count[i] < 1) {
                resultMap.put(str[i], "未填写");
                return Result.error("r400", str[i] + "未填写", resultMap);
            } else {
                resultMap.put(str[i], count[i]);
            }
        }

        return Result.success(resultMap);
    }

    /**
     * 后续会采用监听进行存储
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @GetMapping
    @ApiOperation("存储项目到ES")
    public ResponseMessage<Project> list(String projectId, String createUserId) throws Exception {

        String usid;
        /*
         * 刘复星 id
         */
        if (StringUtils.isNotEmpty(createUserId)) {
            usid = createUserId;
        } else {
            usid = ("JC9TFGZDEC");

        }
        return Result.success(projectSaveService.save(projectId, usid));
    }

    @ApiOperation(value = "|research|save|新增日常事务项目")
    @PostMapping(value = "/createDailyProject",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<Project> createDailyProject(HttpServletRequest request,
                                                       @RequestBody @Valid Project project){
        return Result.success(projectSaveService.saveDailyProject(project));

    }
}
