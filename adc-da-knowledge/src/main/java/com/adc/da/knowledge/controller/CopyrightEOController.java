package com.adc.da.knowledge.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.knowledge.entity.CopyrightEO;
import com.adc.da.knowledge.page.CopyrightEOPage;
import com.adc.da.knowledge.page.MyCopyrightEOPage;
import com.adc.da.knowledge.service.CopyrightEOService;
import com.adc.da.knowledge.vo.CopyrightVO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/${restPath}/knowledge/copyright")
@Api(tags = "知识产权管理|软件著作权|CopyrightEO|")
public class CopyrightEOController extends BaseController<CopyrightEO>{

    private static final Logger logger = LoggerFactory.getLogger(CopyrightEOController.class);

    @Autowired
    private CopyrightEOService copyrightEOService;
    @Autowired
    private BeanMapper beanMapper ;

    /**
     * @see FileEOService
     */
    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private OrgEODao orgEODao ;


    /**
     * @see IFileStore
     */
    @Autowired
    private IFileStore iFileStore;

    @Autowired
    private UserEOService userEOService;
    private List<FileEO> fileEO;


//    @ApiOperation(value = "|CopyrightEO|分页查询")
//    @GetMapping("/page")
//   // @RequiresPermissions("knowledge:copyright:page")
//    public ResponseMessage<PageInfo<CopyrightEO>> page(CopyrightEOPage page) throws Exception {
//        List<CopyrightEO> rows = copyrightEOService.queryByPage(page);
//        return Result.success(getPageInfo(page.getPager(), rows));
//    }

    @ApiOperation(value = "|CopyrightEO|分页查询")
    @PostMapping("/page")
    @RequiresPermissions("knowledge:copyright:page")
    public ResponseMessage<PageInfo<CopyrightEO>> page(@RequestBody MyCopyrightEOPage page) throws Exception {

        if(page.getStartApplyDate() != null) {
            page.setStartApplyDate(DateUtils.getOnlyYMD(page.getStartApplyDate()));
            page.setEndApplyDate(DateUtils.getOnlyYMD(page.getEndApplyDate()));
        }
        if(page.getStartRegisterDate() != null){
            page.setStartRegisterDate(DateUtils.getOnlyYMD(page.getStartRegisterDate()));
            page.setEndRegisterDate(DateUtils.getOnlyYMD(page.getEndRegisterDate()));
        }
        //找到所有的部门，并判断否是查找部门的下级部门
        if(page.getDeptId() != null ){

            List<OrgEO> orgEOList = orgEODao.queryOrgAll();
            List<String> deptIdList = new ArrayList<>();
            for (OrgEO orgEO : orgEOList){
                if (orgEO.getParentIds().indexOf(page.getDeptId()) != -1){
                    deptIdList.add(orgEO.getId());
                }
            }
            deptIdList.add(page.getDeptId());
            page.setDeptId(null);
            page.setDeptIdList(deptIdList);
        }



        List<CopyrightEO> rows = copyrightEOService.queryByMyPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CopyrightEO|查询")
    @PostMapping("")
    @RequiresPermissions("knowledge:copyright:list")
    public ResponseMessage<List<CopyrightEO>> list(@RequestBody CopyrightEOPage page) throws Exception {
        return Result.success(copyrightEOService.queryByList(page));
	}


    @ApiOperation(value = "|CopyrightEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("knowledge:copyright:get")
    public ResponseMessage<CopyrightVO> find(@PathVariable String id) throws Exception {
       CopyrightEO copyrightEO =  copyrightEOService.selectByPrimaryKey(id) ;
       CopyrightVO copyrightVO =   beanMapper.map(copyrightEO,CopyrightVO.class);
       List<FileEO>  list = fileEOService.selectByForeignId(id);
       if(CollectionUtils.isNotEmpty(list)) {
           copyrightVO.setFileId(list.get(0).getFileId());
           copyrightVO.setFileName(list.get(0).getFileName());
       }
        return Result.success(copyrightVO);
    }

    @ApiOperation(value = "|CopyrightEO|新增")
    @PostMapping(value = "/create")
    @RequiresPermissions("knowledge:copyright:save")
    public ResponseMessage<CopyrightEO> create(@RequestBody CopyrightVO copyrightVO) throws Exception {
        CopyrightEO copyrightEO =  beanMapper.map(copyrightVO,CopyrightEO.class);
        copyrightEO.setId(UUID.randomUUID10());
        copyrightEO.setUpdateTime(new Date());
        BasePage page = null;
        Integer serialNumber = copyrightEOService.queryByMyCount(page)+1;
        String snum = String.valueOf(serialNumber);
        if(snum.length()<4) {
            for(int i=0;i<=(4-snum.length());i++) {
                snum = "0" + snum;
            }
        }
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(copyrightEO.getRegisterDate());
        int year = calendar.get(Calendar.YEAR);
        String autoNumber = "RZ"+String.valueOf(year)+snum;
        copyrightEO.setAutoNumber(autoNumber);
        copyrightEOService.insertSelective(copyrightEO);
        FileEO fileEO = fileEOService.selectByPrimaryKey(copyrightVO.getFileId());
        fileEO.setForeignId(copyrightEO.getId());
        fileEOService.updateByPrimaryKeySelective(fileEO);
        return Result.success(copyrightEO);
    }

//    /**
//     * 文件上传
//     * 权限字段：sys:file:upload
//     *
//     * @param copyrightVO 指定文件上传路径，空则在根路径
//     * @return MyFileEO
//     * @throws Exception
//     */
//    @ApiOperation(value = "|CopyrightEO|新增文件")
//    @PostMapping("/create1")
////    @RequiresPermissions("sys:file:create")
//    public ResponseMessage<CopyrightEO> create(CopyrightVO copyrightVO, @RequestParam("file")  MultipartFile file) throws Exception {
//        CopyrightEO copyrightEO =  beanMapper.map(copyrightVO,CopyrightEO.class);
//        copyrightEO.setId(UUID.randomUUID10());
//        copyrightEOService.insertSelective(copyrightEO);
//        FileStoreUtil myutil=new FileStoreUtil();
//        myutil.uploadFile(file,copyrightEO.getId(),fileEOService,iFileStore,userEOService,whiteUrls);
//        return Result.success(copyrightEO);
//    }

    @ApiOperation(value = "|CopyrightEO|修改")
    @PutMapping(value = "/update")
    @RequiresPermissions("knowledge:copyright:update")
    public ResponseMessage<CopyrightEO> update(@RequestBody CopyrightVO copyrightVO) throws Exception {
        CopyrightEO copyrightEO = beanMapper.map(copyrightVO, CopyrightEO.class);
        FileEO fileEO = fileEOService.selectByPrimaryKey(copyrightVO.getFileId());
        List<FileEO> fileEOList = fileEOService.selectByForeignId(copyrightEO.getId());
        if(CollectionUtils.isNotEmpty(fileEOList)&&!StringUtils.equals(copyrightVO.getFileId(),fileEOList.get(0).getFileId())){
            fileEOService.deleteByForeignKey(copyrightEO.getId());
        }
        fileEO.setForeignId(copyrightEO.getId());
        fileEOService.updateByPrimaryKey(fileEO);
        copyrightEO.setUpdateTime(new Date());
        copyrightEOService.updateByPrimaryKey(copyrightEO);
        return Result.success(copyrightEO);
    }

    @ApiOperation(value = "|CopyrightEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("knowledge:copyright:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
            copyrightEOService.deleteByPrimaryKey(id);
            logger.info("delete from K_COPYRIGHT where id = {}", id);
            return Result.success();
        }

    @ApiOperation(value = "|FileEO|删除附件")
    @DeleteMapping("/deleteFile/{foreignId}")
    @RequiresPermissions("knowledge:copyright:delete")
    public ResponseMessage deleteFile(@PathVariable String foreignId) throws Exception {
            fileEOService.deleteByForeignKey(foreignId);
            logger.info("delete from TS_FILE where foreignId = {}", foreignId);
            return Result.success();
    }

    @ApiOperation(value = "|软件著作权记录导出")
    @GetMapping("/export")
    @RequiresPermissions("fin:subtask:export")
    public ResponseMessage excelImport(HttpServletResponse response, @RequestParam String fileName, @RequestParam String[] copyrightIds) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        if (CollectionUtils.isEmpty(copyrightIds)){
            throw new AdcDaBaseException("没有选择任何记录！");
        }
        List<String> copyrightIdList = Arrays.asList(copyrightIds);
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = copyrightEOService.excelExport(params,copyrightIdList);
            os = response.getOutputStream();
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

}
