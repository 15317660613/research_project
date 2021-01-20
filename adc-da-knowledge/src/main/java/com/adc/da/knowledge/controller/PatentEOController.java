package com.adc.da.knowledge.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.knowledge.entity.PatentEO;
import com.adc.da.knowledge.page.MyPatentEOPage;
import com.adc.da.knowledge.page.PatentEOPage;
import com.adc.da.knowledge.service.PatentEOService;
import com.adc.da.knowledge.vo.PatentVO;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/knowledge/patent")
@Api(tags = "知识产权管理|专利|PatentEO|")
public class PatentEOController extends BaseController<PatentEO>{

    private static final Logger logger = LoggerFactory.getLogger(PatentEOController.class);

    @Autowired
    private PatentEOService patentEOService;
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
//    /**
//     * 上传文件类型允许白名单
//     */
//    private static List<String> whiteUrls = new ArrayList<>();
//    /**
//     * 初始化白名单，改用try-with-resources 写法
//     *
//     * @author Lee Kwanho 李坤澔
//     * date 2018-08-28
//     **/
//    @PostConstruct
//    public void init() {
//        /* 读取白名单文件，路径/adc-da-main/src/main/resources/white
//        改为try-with-resources 写法，不用不用手动关闭is和reader */
//        try (InputStream is = FileUploadRestController.class
//                .getClassLoader()
//                .getResourceAsStream("white/uploadWhite.txt");
//             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
//            /*
//             * 添加白名单内容
//             */
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (!"".equals(line)) {
//                    whiteUrls.add(line);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("读取文件上传白名单异常", e);
//        }
//
//    }


//    @ApiOperation(value = "|PatentEO|分页查询")
//    @GetMapping("/page")
//    //@RequiresPermissions("knowledge:patent:page")
//    public ResponseMessage<PageInfo<PatentEO>> page(PatentEOPage page) throws Exception {
//        List<PatentEO> rows = patentEOService.queryByPage(page);
//        return Result.success(getPageInfo(page.getPager(), rows));
//    }

    @ApiOperation(value = "|PatentEO|分页查询")
    @PostMapping("/page")
    @RequiresPermissions("knowledge:patent:page")
    public ResponseMessage<PageInfo<PatentEO>> page(@RequestBody MyPatentEOPage page) throws Exception {
        if(page.getStartApplyDate() != null){
            page.setStartApplyDate(DateUtils.getOnlyYMD(page.getStartApplyDate()));
            page.setEndApplyDate(DateUtils.getOnlyYMD(page.getEndApplyDate()));
        }
        if(page.getStartAuthorizeDate() != null){
            page.setStartAuthorizeDate(DateUtils.getOnlyYMD(page.getStartAuthorizeDate()));
            page.setEndAuthorizeDate(DateUtils.getOnlyYMD(page.getEndAuthorizeDate()));
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


        List<PatentEO> rows = patentEOService.queryByMyPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|PatentEO|查询")
    @PostMapping("")
    @RequiresPermissions("knowledge:patent:list")
    public ResponseMessage<List<PatentEO>> list(@RequestBody PatentEOPage page) throws Exception {
        return Result.success(patentEOService.queryByList(page));
	}

    @ApiOperation(value = "|PatentEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("knowledge:patent:get")
    public ResponseMessage<PatentVO> find(@PathVariable String id) throws Exception {
        PatentEO patentEO =  patentEOService.selectByPrimaryKey(id) ;
        PatentVO patentVO =   beanMapper.map(patentEO,PatentVO.class);
        List<FileEO>  list = fileEOService.selectByForeignId(id);
        if(CollectionUtils.isNotEmpty(list)) {
            patentVO.setFileId(list.get(0).getFileId());
            patentVO.setFileName(list.get(0).getFileName());
        }
        return Result.success(patentVO);
    }

    @ApiOperation(value = "|PatentEO|新增")
    @PostMapping(value = "/create")
    @RequiresPermissions("knowledge:patent:save")
    public ResponseMessage<PatentEO> create(@RequestBody PatentVO patentVO) throws Exception {
        PatentEO patentEO = beanMapper.map(patentVO, PatentEO.class);
        patentEO.setId(UUID.randomUUID10());
        patentEO.setUploadTime(new Date());
        BasePage page = null;
        Integer serialNumber = patentEOService.queryByMyCount(page)+1;
        String snum = String.valueOf(serialNumber);
        if(snum.length()<4) {
            for(int i=0;i<=(4-snum.length());i++) {
                snum = "0" + snum;
            }
        }
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(patentEO.getAuthorizeDate());
        int year = calendar.get(Calendar.YEAR);
        String autoNumber = "ZL"+String.valueOf(year)+snum;
        patentEO.setAutoNumber(autoNumber);
        patentEOService.insertSelective(patentEO);
        FileEO fileEO = fileEOService.selectByPrimaryKey(patentVO.getFileId());
        fileEO.setForeignId(patentEO.getId());
        fileEOService.updateByPrimaryKeySelective(fileEO);
        return Result.success(patentEO);
    }

//    /**
//     * 文件上传
//     * 权限字段：sys:file:upload
//     *
//     * @param patentVO 指定文件上传路径，空则在根路径
//     * @return MyFileEO
//     * @throws Exception
//     */
//    @ApiOperation(value = "|PatentEO|新增")
//    @PostMapping("/create")
////    @RequiresPermissions("sys:file:create")
//    public ResponseMessage<PatentEO> create(PatentVO patentVO, @RequestParam("file") MultipartFile file) throws Exception {
//        PatentEO patentEO =  beanMapper.map(patentVO,PatentEO.class);
//        patentEO.setId(UUID.randomUUID10());
//        if(patentVO.getDesignerUserIdsArr() != null) {
//            for (int i = 0; i < patentVO.getDesignerUserIdsArr().length; i++) {
//                if (i == 0) {
//                    patentEO.setDesignerUserIds(patentVO.getDesignerUserIdsArr()[i]);
//                    patentEO.setDesignerUserNames(patentVO.getDesignerUserNamesArr()[i]);
//                } else {
//                    patentEO.setDesignerUserIds(patentEO.getDesignerUserIds() + patentVO.getDesignerUserIdsArr()[i]);
//                    patentEO.setDesignerUserNames(patentEO.getDesignerUserNames() + patentVO.getDesignerUserNamesArr()[i]);
//                }
//                if (i != patentVO.getDesignerUserIdsArr().length - 1) {
//                    patentEO.setDesignerUserIds(patentEO.getDesignerUserIds() + ",");
//                    patentEO.setDesignerUserNames(patentEO.getDesignerUserNames() + ",");
//                }
//            }
//        }
//        patentEOService.insertSelective(patentEO);
//        FileStoreUtil myutil=new FileStoreUtil();
//        myutil.uploadFile(file,patentEO.getId(),fileEOService,iFileStore,userEOService,whiteUrls);
//
//        return Result.success(patentEO);
//    }


    @ApiOperation(value = "|PatentEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("knowledge:patent:update")
    public ResponseMessage<PatentEO> update(@RequestBody PatentVO patentVO) throws Exception {
        PatentEO patentEO = beanMapper.map(patentVO, PatentEO.class);
        FileEO fileEO = fileEOService.selectByPrimaryKey(patentVO.getFileId());
        List<FileEO> fileEOList = fileEOService.selectByForeignId(patentEO.getId());
        if(CollectionUtils.isNotEmpty(fileEOList)&&!StringUtils.equals(patentVO.getFileId(),fileEOList.get(0).getFileId())){
             fileEOService.deleteByForeignKey(patentEO.getId());
        }
        fileEO.setForeignId(patentEO.getId());
        fileEOService.updateByPrimaryKey(fileEO);
        patentEO.setUploadTime(new Date());
        patentEOService.updateByPrimaryKey(patentEO);
        return Result.success(patentEO);
    }

    @ApiOperation(value = "|PatentEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("knowledge:patent:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        patentEOService.deleteByPrimaryKey(id);
        logger.info("delete from K_PATENT where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|FileEO|删除附件")
    @DeleteMapping("/deleteFile/{foreignId}")
    @RequiresPermissions("knowledge:patent:delete")
    public ResponseMessage deleteFile(@PathVariable String foreignId) throws Exception {
        fileEOService.deleteByForeignKey(foreignId);
        logger.info("delete from TS_FILE where foreignId = {}", foreignId);
        return Result.success();
    }

    @ApiOperation(value = "|专利记录导出")
    @GetMapping("/export")
    @RequiresPermissions("fin:subtask:export")
    public ResponseMessage excelImport(HttpServletResponse response, @RequestParam String fileName, @RequestParam String[] patentIds) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        if (CollectionUtils.isEmpty(patentIds)){
            throw new AdcDaBaseException("没有选择任何记录！");
        }
        List<String> patentIdList = Arrays.asList(patentIds);
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = patentEOService.excelExport(params,patentIdList);
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
