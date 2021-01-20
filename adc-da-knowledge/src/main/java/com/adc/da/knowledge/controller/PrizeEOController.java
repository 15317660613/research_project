package com.adc.da.knowledge.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.knowledge.entity.PrizeEO;
import com.adc.da.knowledge.page.MyPrizeEOPage;
import com.adc.da.knowledge.page.PrizeEOPage;
import com.adc.da.knowledge.service.PrizeEOService;
import com.adc.da.knowledge.vo.PrizeVO;
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
@RequestMapping("/${restPath}/knowledge/prize")
@Api(tags = "知识产权管理|获奖|PrizeEO|")
public class PrizeEOController extends BaseController<PrizeEO>{

    private static final Logger logger = LoggerFactory.getLogger(PrizeEOController.class);

    @Autowired
    private PrizeEOService prizeEOService;
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

//    @ApiOperation(value = "|PrizeEO|分页查询")
//    @GetMapping("/page")
//    //@RequiresPermissions("knowledge:prize:page")
//    public ResponseMessage<PageInfo<PrizeEO>> page(PrizeEOPage page) throws Exception {
//        List<PrizeEO> rows = prizeEOService.queryByPage(page);
//        return Result.success(getPageInfo(page.getPager(), rows));
//    }

    @ApiOperation(value = "|PrizeEO|分页查询")
    @PostMapping("/page")
    @RequiresPermissions("knowledge:prize:page")
    public ResponseMessage<PageInfo<PrizeEO>> page(@RequestBody MyPrizeEOPage page) throws Exception {
        if(page.getStartPrizeTime() != null){
            page.setStartPrizeTime(DateUtils.getOnlyYMD(page.getStartPrizeTime()));
            page.setEndPrizeTime(DateUtils.getOnlyYMD(page.getEndPrizeTime()));
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


        List<PrizeEO> rows = prizeEOService.queryByMyPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|PrizeEO|查询")
    @PostMapping("")
    @RequiresPermissions("knowledge:prize:list")
    public ResponseMessage<List<PrizeEO>> list(@RequestBody PrizeEOPage page) throws Exception {
        return Result.success(prizeEOService.queryByList(page));
	}

    @ApiOperation(value = "|PrizeEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("knowledge:prize:get")
    public ResponseMessage<PrizeVO> find(@PathVariable String id) throws Exception {
        PrizeEO prizeEO =  prizeEOService.selectByPrimaryKey(id) ;
        PrizeVO prizeVO =   beanMapper.map(prizeEO,PrizeVO.class);
        List<FileEO>  list = fileEOService.selectByForeignId(id);
        if(CollectionUtils.isNotEmpty(list)) {
            prizeVO.setFileId(list.get(0).getFileId());
            prizeVO.setFileName(list.get(0).getFileName());
        }
        return Result.success(prizeVO);
    }

    @ApiOperation(value = "|PrizeEO|新增")
    @PostMapping(value = "/create")
    @RequiresPermissions("knowledge:prize:save")
    public ResponseMessage<PrizeEO> create(@RequestBody PrizeVO prizeVO) throws Exception {
        PrizeEO prizeEO =  beanMapper.map(prizeVO,PrizeEO.class);
	    prizeEO.setId(UUID.randomUUID10());
        prizeEO.setUpdateTime(new Date());
        BasePage page = null;
        Integer serialNumber = prizeEOService.queryByMyCount(page)+1;
        String snum = String.valueOf(serialNumber);
        if(snum.length()<4) {
            for(int i=0;i<=(4-snum.length());i++) {
                snum = "0" + snum;
            }
        }
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(prizeEO.getPrizeTime());
        int year = calendar.get(Calendar.YEAR);
        String autoNumber = "HJ"+String.valueOf(year)+snum;
        prizeEO.setAutoNumber(autoNumber);
        prizeEOService.insertSelective(prizeEO);
        FileEO fileEO = fileEOService.selectByPrimaryKey(prizeVO.getFileId());
        fileEO.setForeignId(prizeEO.getId());
        fileEOService.updateByPrimaryKeySelective(fileEO);
        return Result.success(prizeEO);
    }

//    /**
//     * 文件上传
//     * 权限字段：sys:file:upload
//     *
//     * @param prizeVO 指定文件上传路径，空则在根路径
//     * @return MyFileEO
//     * @throws Exception
//     */
//    @ApiOperation(value = "|PrizeEO|新增")
//    @PostMapping("/create")
////    @RequiresPermissions("sys:file:create")
//    public ResponseMessage<PrizeEO> create(PrizeVO prizeVO, @RequestParam("file") MultipartFile file) throws Exception {
//        PrizeEO prizeEO =  beanMapper.map(prizeVO,PrizeEO.class);
//        prizeEO.setId(UUID.randomUUID10());
//        if(prizeVO.getBelongedUserIdArr() != null) {
//            for (int i = 0; i < prizeVO.getBelongedUserIdArr().length; i++) {
//                if (i == 0) {
//                    prizeEO.setBelongedUserId(prizeVO.getBelongedUserIdArr()[i]);
//                    prizeEO.setBelongedUserName(prizeVO.getBelongedUserNameArr()[i]);
//                } else {
//                    prizeEO.setBelongedUserId(prizeEO.getBelongedUserId() + prizeVO.getBelongedUserIdArr()[i]);
//                    prizeEO.setBelongedUserName(prizeEO.getBelongedUserName() + prizeVO.getBelongedUserNameArr()[i]);
//                }
//                if (i != prizeVO.getBelongedUserIdArr().length - 1) {
//                    prizeEO.setBelongedUserId(prizeEO.getBelongedUserId() + ",");
//                    prizeEO.setBelongedUserName(prizeEO.getBelongedUserName() + ",");
//                }
//            }
//        }
//        if(prizeVO.getDeptIdArr() != null) {
//            for (int i = 0; i < prizeVO.getDeptIdArr().length; i++) {
//                if (i == 0) {
//                    prizeEO.setDeptId(prizeVO.getDeptIdArr()[i]);
//                    prizeEO.setDeptName(prizeVO.getDeptNameArr()[i]);
//                } else {
//                    prizeEO.setDeptId(prizeEO.getDeptId() + prizeVO.getDeptIdArr()[i]);
//                    prizeEO.setDeptName(prizeEO.getDeptName() + prizeVO.getDeptNameArr()[i]);
//                }
//                if (i != prizeVO.getDeptIdArr().length - 1) {
//                    prizeEO.setDeptId(prizeEO.getDeptId() + ",");
//                    prizeEO.setDeptName(prizeEO.getDeptName() + ",");
//                }
//            }
//        }
//        prizeEOService.insertSelective(prizeEO);
//        FileStoreUtil myutil=new FileStoreUtil();
//        myutil.uploadFile(file,prizeEO.getId(),fileEOService,iFileStore,userEOService,whiteUrls);
//        return Result.success(prizeEO);
//    }

    @ApiOperation(value = "|PrizeEO|修改")
    @PutMapping(value = "/update")
    @RequiresPermissions("knowledge:prize:update")
    public ResponseMessage<PrizeEO> update(@RequestBody PrizeVO prizeVO) throws Exception {
        PrizeEO prizeEO = beanMapper.map(prizeVO, PrizeEO.class);
        FileEO fileEO = fileEOService.selectByPrimaryKey(prizeVO.getFileId());
        List<FileEO> fileEOList = fileEOService.selectByForeignId(prizeEO.getId());
        if(CollectionUtils.isNotEmpty(fileEOList)&&!StringUtils.equals(prizeVO.getFileId(),fileEOList.get(0).getFileId())){
            fileEOService.deleteByForeignKey(prizeEO.getId());
        }
        fileEO.setForeignId(prizeEO.getId());
        fileEOService.updateByPrimaryKey(fileEO);
        prizeEO.setUpdateTime(new Date());
        prizeEOService.updateByPrimaryKey(prizeEO);
        return Result.success(prizeEO);
    }

    @ApiOperation(value = "|PrizeEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("knowledge:prize:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        prizeEOService.deleteByPrimaryKey(id);
        logger.info("delete from K_PRIZE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|FileEO|删除附件")
    @DeleteMapping("/deleteFile/{foreignId}")
    @RequiresPermissions("knowledge:prize:delete")
    public ResponseMessage deleteFile(@PathVariable String foreignId) throws Exception {
        fileEOService.deleteByForeignKey(foreignId);
        logger.info("delete from TS_FILE where foreignId = {}", foreignId);
        return Result.success();
    }


    @ApiOperation(value = "|获奖记录导出")
    @GetMapping("/export")
    @RequiresPermissions("fin:subtask:export")
    public ResponseMessage excelImport(HttpServletResponse response, @RequestParam String fileName, @RequestParam String[] prizeIds) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        if (CollectionUtils.isEmpty(prizeIds)){
            throw new AdcDaBaseException("没有选择任何记录！");
        }
        List<String> prizeIdList = Arrays.asList(prizeIds);
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = prizeEOService.excelExport(params,prizeIdList);
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
