package com.adc.da.knowledge.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.knowledge.entity.PaperEO;
import com.adc.da.knowledge.page.MyPaperEOPage;
import com.adc.da.knowledge.page.PaperEOPage;
import com.adc.da.knowledge.service.PaperEOService;
import com.adc.da.knowledge.vo.PaperVO;
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
@RequestMapping("/${restPath}/knowledge/paper")
@Api(tags = "知识产权管理|论文|PaperEO|")
public class PaperEOController extends BaseController<PaperEO>{

    private static final Logger logger = LoggerFactory.getLogger(PaperEOController.class);

    @Autowired
    private PaperEOService paperEOService;

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

    @ApiOperation(value = "|论文记录导出")
    @GetMapping("/export")
    @RequiresPermissions("fin:subtask:export")
    public ResponseMessage excelImport(HttpServletResponse response,@RequestParam String fileName,@RequestParam String[] paperIds) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        if (CollectionUtils.isEmpty(paperIds)){
            throw new AdcDaBaseException("没有选择任何记录！");
        }
        List<String> paperIdList = Arrays.asList(paperIds);
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = paperEOService.excelExport(params,paperIdList);
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

//    @ApiOperation(value = "|PaperEO|分页查询")
//    @GetMapping("/page")
//    //@RequiresPermissions("knowledge:paper:page")
//    public ResponseMessage<PageInfo<PaperEO>> page(PaperEOPage page) throws Exception {
//        List<PaperEO> rows = paperEOService.queryByPage(page);
//        return Result.success(getPageInfo(page.getPager(), rows));
//    }

    @ApiOperation(value = "|PaperEO|分页查询")
    @PostMapping("/page")
    @RequiresPermissions("knowledge:paper:page")
    public ResponseMessage<PageInfo<PaperEO>> page(@RequestBody MyPaperEOPage page) throws Exception {
        if(page.getStartPublishedTime() != null) {
            page.setStartPublishedTime(DateUtils.getOnlyYMD(page.getStartPublishedTime()));
            page.setEndPublishedTime(DateUtils.getOnlyYMD(page.getEndPublishedTime()));
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


        List<PaperEO> rows = paperEOService.queryByMyPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|PaperEO|查询")
    @PostMapping("")
    @RequiresPermissions("knowledge:paper:list")
    public ResponseMessage<List<PaperEO>> list(@RequestBody PaperEOPage page) throws Exception {
        return Result.success(paperEOService.queryByList(page));
	}

    @ApiOperation(value = "|PaperEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("knowledge:paper:get")
    public ResponseMessage<PaperVO> find(@PathVariable String id) throws Exception {
        PaperEO paperEO =  paperEOService.selectByPrimaryKey(id) ;
        PaperVO paperVO =   beanMapper.map(paperEO,PaperVO.class);
        List<FileEO>  list = fileEOService.selectByForeignId(id);
        if(CollectionUtils.isNotEmpty(list)) {
            paperVO.setFileId(list.get(0).getFileId());
            paperVO.setFileName(list.get(0).getFileName());
        }
        return Result.success(paperVO);
    }

//    /**
//     * 文件上传
//     * 权限字段：sys:file:upload
//     *
//     * @param paperVO 指定文件上传路径，空则在根路径
//     * @return MyFileEO
//     * @throws Exception
//     */
//    @ApiOperation(value = "|PaperVO|新增")
//    @PostMapping("/create")
////    @RequiresPermissions("sys:file:create")
//    public ResponseMessage<PaperEO> create(PaperVO paperVO, @RequestParam("file") MultipartFile file) throws Exception {
//        PaperEO paperEO =  beanMapper.map(paperVO,PaperEO.class);
//        paperEO.setId(UUID.randomUUID10());
//        if(paperVO.getAuthorUserIdsArr() != null) {
//            for (int i = 0; i < paperVO.getAuthorUserIdsArr().length; i++) {
//                if (i == 0) {
//                    paperEO.setAuthorUserIds(paperVO.getAuthorUserIdsArr()[i]);
//                    paperEO.setAuthorUserNames(paperVO.getAuthorUserNamesArr()[i]);
//                } else {
//                    paperEO.setAuthorUserIds(paperEO.getAuthorUserIds() + paperVO.getAuthorUserIdsArr()[i]);
//                    paperEO.setAuthorUserNames(paperEO.getAuthorUserNames() + paperVO.getAuthorUserNamesArr()[i]);
//                }
//                if (i != paperVO.getAuthorUserIdsArr().length - 1) {
//                    paperEO.setAuthorUserIds(paperEO.getAuthorUserIds() + ",");
//                    paperEO.setAuthorUserNames(paperEO.getAuthorUserNames() + ",");
//                }
//            }
//        }
//        paperEOService.insertSelective(paperEO);
//        FileStoreUtil myutil=new FileStoreUtil();
//        myutil.uploadFile(file,paperEO.getId(), fileEOService, iFileStore, userEOService,whiteUrls);
//        return Result.success(paperEO);
//    }

    @ApiOperation(value = "|PaperEO|新增")
    @PostMapping(value = "/create")
    @RequiresPermissions("knowledge:paper:save")
    public ResponseMessage<PaperEO> create(@RequestBody PaperVO paperVO) throws Exception {
        PaperEO paperEO =  beanMapper.map(paperVO,PaperEO.class);
        paperEO.setId(UUID.randomUUID10());
        paperEO.setUpdateTime(new Date());
        BasePage page = null;
        Integer serialNumber = paperEOService.queryByMyCount(page)+1;
        String snum = String.valueOf(serialNumber);
        if(snum.length()<4) {
            for(int i=0;i<=(4-snum.length());i++) {
                snum = "0" + snum;
            }
        }
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(paperEO.getPublishedTime());
        int year = calendar.get(Calendar.YEAR);
        String autoNumber = "LW"+String.valueOf(year)+snum;
        paperEO.setAutoNumber(autoNumber);
        paperEOService.insertSelective(paperEO);
        FileEO fileEO = fileEOService.selectByPrimaryKey(paperVO.getFileId());
        fileEO.setForeignId(paperEO.getId());
        fileEOService.updateByPrimaryKeySelective(fileEO);
        return Result.success(paperEO);
    }

    @ApiOperation(value = "|PaperEO|修改")
    @PutMapping(value = "/update")
    @RequiresPermissions("knowledge:paper:update")
    public ResponseMessage<PaperEO> update(@RequestBody PaperVO paperVO) throws Exception {
        PaperEO paperEO = beanMapper.map(paperVO, PaperEO.class);
        FileEO fileEO = fileEOService.selectByPrimaryKey(paperVO.getFileId());
        List<FileEO> fileEOList = fileEOService.selectByForeignId(paperEO.getId());
        if(CollectionUtils.isNotEmpty(fileEOList)&&!StringUtils.equals(paperVO.getFileId(),fileEOList.get(0).getFileId())){
            fileEOService.deleteByForeignKey(paperEO.getId());
        }
        fileEO.setForeignId(paperEO.getId());
        fileEOService.updateByPrimaryKey(fileEO);

        paperEO.setUpdateTime(new Date());
        paperEOService.updateByPrimaryKey(paperEO);
        return Result.success(paperEO);
    }

    @ApiOperation(value = "|PaperEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("knowledge:paper:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        paperEOService.deleteByPrimaryKey(id);
        logger.info("delete from K_PAPER where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|FileEO|删除附件")
    @DeleteMapping("/deleteFile/{foreignId}")
    @RequiresPermissions("knowledge:paper:delete")
    public ResponseMessage deleteFile(@PathVariable String foreignId) throws Exception {
        fileEOService.deleteByForeignKey(foreignId);
        logger.info("delete from TS_FILE where foreignId = {}", foreignId);
        return Result.success();
    }
}
