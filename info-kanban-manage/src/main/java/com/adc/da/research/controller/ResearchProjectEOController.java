package com.adc.da.research.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import cn.afterturn.easypoi.exception.excel.ExcelImportException;
import com.adc.da.login.util.UserUtils;
import com.adc.da.pad.page.PadOperationManageEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.ResearchProjectEO;
import com.adc.da.research.page.ResearchProjectEOPage;
import com.adc.da.research.service.ResearchProjectEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/research/researchProject")
@Api(tags = "科研项目一览表|ResearchProjectEO|")
public class ResearchProjectEOController extends BaseController<ResearchProjectEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectEOController.class);

    @Autowired
    private ResearchProjectEOService researchProjectEOService;

	@ApiOperation(value = "|ResearchProjectEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:researchProject:page")
    public ResponseMessage<PageInfo<ResearchProjectEO>> page(ResearchProjectEOPage page) throws Exception {
        List<ResearchProjectEO> rows = researchProjectEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResearchProjectEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:researchProject:list")
    public ResponseMessage<List<ResearchProjectEO>> list(ResearchProjectEOPage page) throws Exception {
        return Result.success(researchProjectEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResearchProjectEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:researchProject:get")
    public ResponseMessage<ResearchProjectEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchProjectEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchProjectEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:researchProject:save")
    public ResponseMessage<ResearchProjectEO> create(@RequestBody ResearchProjectEO researchProjectEO) throws Exception {
        researchProjectEOService.insertSelective(researchProjectEO);
        return Result.success(researchProjectEO);
    }

    @ApiOperation(value = "|ResearchProjectEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:researchProject:update")
    public ResponseMessage<ResearchProjectEO> update(@RequestBody ResearchProjectEO researchProjectEO) throws Exception {

        researchProjectEOService.updateByPrimaryKeySelective(researchProjectEO);
        return Result.success(researchProjectEO);
    }

    @ApiOperation(value = "|ResearchProjectEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research:researchProject:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        researchProjectEOService.deleteByPrimaryKey(id);
        logger.info("delete from DB_RESEARCH_PROJECT where id = {}", id);
        return Result.success();
    }


    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|ResearchProjectEO|导入")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            researchProjectEOService.excelImport(is);
            return Result.success();
        }catch (ExcelImportException ex){
            logger.error(ex.getMessage());
            return Result.error("请检查日期等数据格式，按照模板填写！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "|ResearchProjectEO|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response, ResearchProjectEOPage page) {
        String fileName = "科研项目一览表导出数据.xlsx";
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            workbook = researchProjectEOService.excelExport(page);
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

    @ApiOperation(value = "批量逻辑删除")
    @DeleteMapping("/del/{ids}")
//    @RequiresPermissions("pad:padOperationManage:delete")
    public ResponseMessage deleteLogicInBatch(@NotNull @PathVariable("ids") String[] ids){
        List<String> list = Arrays.asList(ids);
        try {
            researchProjectEOService.deleteLogicInBatch(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
        return Result.success();
    }
    @ApiOperation(value = "根据危险等级获取列表 参数(高，中，低)")
    @GetMapping("/queryListByDangerLevel")
    public ResponseMessage queryListByDangerLevel(String level){
        return Result.success(researchProjectEOService.queryListByDangerLevel(level));
    }


    @ApiOperation(value = "清空全部")
    @DeleteMapping("/deleteAll")
    public ResponseMessage deleteLogicAll(){
        researchProjectEOService.deleteLogicAll();
        return Result.success();
    }
    @ApiOperation(value = "获取所有科研项目id列表")
    @GetMapping("/getResearchProjectIdList")
    public ResponseMessage  getResearchProjectIdList()throws  Exception{
        List<ResearchProjectEO> researchProjectEOList = researchProjectEOService.queryByList(new ResearchProjectEOPage());
        List<Map<String,String>> mapList = new ArrayList<>();
        Set<String> researchProjectIdSet = new HashSet<>();
        for (ResearchProjectEO researchProjectEO : researchProjectEOList){
            if (researchProjectIdSet.add(researchProjectEO.getResearchProjectId())){
                HashMap<String,String> map = new HashMap<>();
                map.put("researchProjectId",researchProjectEO.getResearchProjectId());
                map.put("projectNo",researchProjectEO.getProjectNo());
                mapList.add(map);
            }
        }

        return Result.success(mapList);
    }

}
