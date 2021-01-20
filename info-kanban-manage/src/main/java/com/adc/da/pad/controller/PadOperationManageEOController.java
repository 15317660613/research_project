package com.adc.da.pad.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import com.adc.da.login.util.UserUtils;
import com.adc.da.pad.vo.DashBoardBarVO;
import com.adc.da.pad.vo.PadDashBoardHeaderVO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.pad.entity.PadOperationManageEO;
import com.adc.da.pad.page.PadOperationManageEOPage;
import com.adc.da.pad.service.PadOperationManageEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/pad/padOperationManage")
@Api(description = "|PadOperationManageEO|")
public class PadOperationManageEOController extends BaseController<PadOperationManageEO>{

    private static final Logger logger = LoggerFactory.getLogger(PadOperationManageEOController.class);

    @Autowired
    private PadOperationManageEOService padOperationManageEOService;

	@ApiOperation(value = "|PadOperationManageEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("pad:padOperationManage:page")
    public ResponseMessage<PageInfo<PadOperationManageEO>> page(PadOperationManageEOPage page) throws Exception {
        List<PadOperationManageEO> rows = padOperationManageEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|PadOperationManageEO|查询")
    @GetMapping("")
//    @RequiresPermissions("pad:padOperationManage:list")
    public ResponseMessage<List<PadOperationManageEO>> list(PadOperationManageEOPage page) throws Exception {
        return Result.success(padOperationManageEOService.queryByList(page));
	}

    @ApiOperation(value = "|PadOperationManageEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("pad:padOperationManage:get")
    public ResponseMessage<PadOperationManageEO> find(@PathVariable String id) throws Exception {
        return Result.success(padOperationManageEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|PadOperationManageEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("pad:padOperationManage:save")
    public ResponseMessage<PadOperationManageEO> create(@RequestBody PadOperationManageEO padOperationManageEO) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        padOperationManageEO.setId(UUID.randomUUID10());
        padOperationManageEO.setUpdateUserId(userEO.getUsid());
        padOperationManageEO.setUpdateUserName(userEO.getUsname());
        padOperationManageEO.setUpdateTime(new Date());
	    padOperationManageEOService.insertSelective(padOperationManageEO);
        return Result.success(padOperationManageEO);
    }

    @ApiOperation(value = "|PadOperationManageEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("pad:padOperationManage:update")
    public ResponseMessage<PadOperationManageEO> update(@RequestBody PadOperationManageEO padOperationManageEO) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        padOperationManageEO.setUpdateUserId(userEO.getUsid());
        padOperationManageEO.setUpdateUserName(userEO.getUsname());
        padOperationManageEO.setUpdateTime(new Date());
	    padOperationManageEOService.updateByPrimaryKeySelective(padOperationManageEO);
        return Result.success(padOperationManageEO);
    }

    @ApiOperation(value = "|PadOperationManageEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("pad:padOperationManage:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        padOperationManageEOService.deleteByPrimaryKey(id);
        logger.info("delete from PAD_OPERATION_MANAGE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|PadOperationManageEO|批量逻辑删除")
    @DeleteMapping("/del/{ids}")
//    @RequiresPermissions("pad:padOperationManage:delete")
    public ResponseMessage deleteLogicInBatch(@NotNull @PathVariable("ids") String[] ids){
        List<String> list = Arrays.asList(ids);
        padOperationManageEOService.deleteLogicInBatch(list);
        return Result.success();
    }
    @ApiOperation(value = "|PadOperationManageEO|看板柱状图（根据月份） 1-合同 2-开票 3-进账")
    @GetMapping("/dashBoardBarVO/{type}")
    public ResponseMessage DashBoardBarVO(@PathVariable("type") int type){
	    return Result.success(padOperationManageEOService.DashBoardBarVO(type));

    }
    @ApiOperation(value = "|PadOperationManageEO|看板柱状图（根据组织机构） 1-合同 2-开票 3-进账")
    @GetMapping("/orgDashBoardBarVO/{type}")
    public ResponseMessage orgDashBoardBarVO(@PathVariable("type") int type){
        return Result.success(padOperationManageEOService.orgDashBoardBarVO(type));

    }


    /**
     * 头部
     */
    @GetMapping("/getHeaderVO")
    @ApiOperation(value = "|PadOperationManageEO|头部")
    public ResponseMessage getHeaderVO() {
        PadDashBoardHeaderVO padDashBoardHeaderVO = padOperationManageEOService.getHeaderVO();
        return Result.success(padDashBoardHeaderVO);
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|PadOperationManageEO|导入")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            padOperationManageEOService.excelImport(is);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "|PadOperationManageEO|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response,PadOperationManageEOPage padOperationManageEOPage) {
        String fileName = "经营数据表.xlsx";
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            workbook = padOperationManageEOService.excelExport(padOperationManageEOPage);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }


}
