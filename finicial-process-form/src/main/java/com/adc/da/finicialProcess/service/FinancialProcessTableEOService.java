package com.adc.da.finicialProcess.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.finicialProcess.combine.ExcelCombineUtil;
import com.adc.da.finicialProcess.dao.FinancialProcessTableEODao;
import com.adc.da.finicialProcess.entity.FinancialProcessTableEO;
import com.adc.da.finicialProcess.page.FinancialProcessTableEOPage;
import com.adc.da.finicialProcess.split.ExcelSplitUtil;
import com.adc.da.finicialProcess.split.ResultFile;
import com.adc.da.finicialProcess.split.SplitByBigDeptTask;
import com.adc.da.finicialProcess.split.SplitBySmallDeptTask;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * <br>
 * <b>功能：</b>FINANCIAL_PROCESS_TABLE FinancialProcessTableEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("financialProcessTableEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FinancialProcessTableEOService extends BaseService<FinancialProcessTableEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FinancialProcessTableEOService.class);

    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private FinancialProcessTableEODao dao;

    @Autowired
    private UserEODao userEODao;

    @Value("${file.path}")
    private String basePath;

    @Autowired
    private DicTypeEOService dicTypeEOService;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private FileEOService fileEOService;

    public FinancialProcessTableEODao getDao() {
        return dao;
    }

    public FinancialProcessTableEO save(FinancialProcessTableEO financialProcessTableEO) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        try {
            if (!isSameFileHead(financialProcessTableEO)) {
                throw new AdcDaBaseException("上传文件跟模板不一致，请修改！");
            }
        }catch (Exception e){
            throw new AdcDaBaseException("上传文件跟模板不一致，请修改！");
        }
        financialProcessTableEO.setId(UUID.randomUUID10());
        financialProcessTableEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        financialProcessTableEO.setCreateUserId(userEO.getUsid());
        financialProcessTableEO.setCreateUserName(userEO.getUsname());
        financialProcessTableEO.setReceiveUserId(userEO.getUsid());
        financialProcessTableEO.setReceiveUserName(userEO.getUsname());
        financialProcessTableEO.setCreateTime(new Date());
        financialProcessTableEO.setReceiveTime(new Date());
        financialProcessTableEO.setExtInfo6(UUID.randomUUID10());
        dao.insertSelective(financialProcessTableEO);
        return financialProcessTableEO;
    }

    public FinancialProcessTableEO send(FinancialProcessTableEO financialProcessTableEO) throws Exception {
        String prefix = "";
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (StringUtils.isNotEmpty(financialProcessTableEO.getFileId())) {
            financialProcessTableEO.setState(1);
            financialProcessTableEO = save(financialProcessTableEO);
        } else {
            //已发送置为1
            financialProcessTableEO = dao.selectByPrimaryKey(financialProcessTableEO.getId());
            financialProcessTableEO.setState(1);
            dao.updateByPrimaryKeySelective(financialProcessTableEO);
        }

        FileEO fileEO = fileEOService.selectByPrimaryKey(financialProcessTableEO.getFileId());
        String fileType = fileEO.getFileType();
        if (!StringUtils.equals(fileType,"xls")&&!StringUtils.equals(fileType,"xlsx")){
            throw new AdcDaBaseException("所传文件不是excel格式，请检查！");
        }
        String contentType = fileEO.getContentType();

        String filePath = basePath + File.separator + fileEO.getSavePath();
        File file = new File(filePath);

        List<ResultFile> resultSmallDeptFileList = new ArrayList<>();
        List<ResultFile> resultCallerFileList = new ArrayList<>();
        if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "0")) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            prefix = "2020年部门收入及合同汇总";
            SplitByBigDeptTask splitByBigDeptTask = new SplitByBigDeptTask(file, basePath);
            FutureTask<List<ResultFile>> bigDeptFuture = new FutureTask<>(splitByBigDeptTask);
            executorService.submit(bigDeptFuture);

            SplitBySmallDeptTask splitBySmallDeptTask = new SplitBySmallDeptTask(file, basePath);
            FutureTask<List<ResultFile>> smallDeptFuture = new FutureTask<>(splitBySmallDeptTask);
            executorService.submit(smallDeptFuture);

            resultSmallDeptFileList.addAll(bigDeptFuture.get());
            logger.info("2020年部门收入及合同汇总按大部门拆分完毕");
            resultSmallDeptFileList.addAll(smallDeptFuture.get());
            logger.info("2020年部门收入及合同汇总按小部门拆分完毕");
        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "1")) {
            prefix = "部门成本明细";

            String fileName = "1_tpl.xlsx";
            //部门成本明细表
            List<ResultFile> resultBigDeptFileList = ExcelSplitUtil
                .doSplitByBigDept4DepartCostDetails(file, fileName, basePath,this);
            resultSmallDeptFileList = ExcelSplitUtil.doSplitByDept4DepartCostDetails(file, fileName, basePath,this);
            resultSmallDeptFileList.addAll(resultBigDeptFileList);
        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "2")) {
            prefix = "课题执行情况";
            String fileName = "2_tpl.xlsx";
            //课题执行情况表
            List<ResultFile> resultBigDeptFileList = ExcelSplitUtil
                .doSplitByBigDept4TopicImplementation(file, fileName, basePath,this);
            resultSmallDeptFileList = ExcelSplitUtil.doSplitByDept4TopicImplementation(file, fileName, basePath,this);
            resultSmallDeptFileList.addAll(resultBigDeptFileList);
            resultCallerFileList = ExcelSplitUtil.doSplitByCaller4TopicImplementation(file, fileName,basePath,this);
        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "3")) {
            prefix = "往来账款催办";

            String fileName = "3_tpl.xlsx";
            //往来账款催办表
            List<ResultFile> resultBigDeptFileList = ExcelSplitUtil
                .doSplitByBigDept4ReciprocalAccountRequest(file, fileName, basePath,this);
            resultSmallDeptFileList = ExcelSplitUtil.doSplitByDept4ReciprocalAccountRequest(file, fileName, basePath,this);
            resultSmallDeptFileList.addAll(resultBigDeptFileList);

        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "4")) {
            prefix = "应收账款";
            String fileName = "4_tpl.xlsx";
            //应收账款
            List<ResultFile> resultBigDeptFileList = ExcelSplitUtil
                .doSplitByBigDept4AccountsReceivable(file, fileName, basePath,this);
            resultSmallDeptFileList = ExcelSplitUtil.doSplitByDept4AccountsReceivable(file, fileName, basePath,this);
            resultSmallDeptFileList.addAll(resultBigDeptFileList);

        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "5")) {
            prefix = "支出合同明细";
//            File DOCFile = getResFile("5_tpl.xlsx");
            String fileName = "5_tpl.xlsx";
            //支出合同明细表
            resultSmallDeptFileList = ExcelSplitUtil.doSplitByExpenditureContractDetails(file, fileName, basePath,this);
        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "6")) {
        prefix = "支出项目明细";
//            File DOCFile = getResFile("5_tpl.xlsx");
        String fileName = "6_tpl.xlsx";
        //支出合同明细表
        resultSmallDeptFileList = ExcelSplitUtil.doSplitByDept4ProjectCost(file, fileName, basePath,this,orgEODao);
    }

        List<FinancialProcessTableEO> resultFinacialEOList = new ArrayList<>();
        for (ResultFile resultFile : resultSmallDeptFileList) {
            FileEO fileEO1 = new FileEO();
            fileEO1.setFileId(UUID.randomUUID10());
            fileEO1.setFileType(fileType);
            fileEO1.setContentType(contentType);
            fileEO1.setFileName(resultFile.getFileName());
            fileEO1.setSavePath(resultFile.getFilePath());
            fileEO1.setCreateTime(new Date());
            List<OrgEO> orgEOList = orgEODao.getOrgEOByOrgName(resultFile.getKey());
            //针对 团委、党支部、中汽智联这些不在组织机构表里的组织做特殊处理
            if (CollectionUtils.isEmpty(orgEOList)) {
                OrgEO orgEO = new OrgEO();
                orgEO.setName(resultFile.getKey());
                orgEO.setId("MH8JQV5TSN");
                orgEO.setLayer("2");
                orgEOList.add(orgEO);
            }
            String orgId = orgEOList.get(0).getId();

            // 先从字典找一圈
            List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FinancialRecipient");
            if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
                for (DicTypeEO dicTypeEO : dicTypeEOList) {
                    if (StringUtils.equals(dicTypeEO.getDicTypeName(), resultFile.getKey())) {
                        String usid = dicTypeEO.getDicTypeCode();
                        FinancialProcessTableEO eo = new FinancialProcessTableEO();
                        eo.setId(UUID.randomUUID10());
                        eo.setFinancialTableType(financialProcessTableEO.getFinancialTableType());
                        if (StringUtils.equals(eo.getFinancialTableType(), "5")) {
                            eo.setState(2);
                        } else {
                            eo.setState(4);
                        }
                        eo.setReceiveUserId(usid);
                        UserEO userEO1 = userEODao.getUserWithRoles(usid);
                        if (null != userEO1) {
                            eo.setParentId(financialProcessTableEO.getId());
                            eo.setExtInfo6(UUID.randomUUID10());
                            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
                            eo.setDeptName(resultFile.getKey());
                            eo.setDeptId(orgId);
                            eo.setCreateUserName(financialProcessTableEO.getCreateUserName());
                            eo.setCreateUserId(financialProcessTableEO.getCreateUserId());
                            eo.setReceiveUserName(userEO1.getUsname());
                            eo.setReceiveTime(new Date());
                            eo.setCreateTime(financialProcessTableEO.getCreateTime());
                            eo.setFileId(fileEO1.getFileId());
                            eo.setFinancialTableName(eo.getDeptName() + "_" + prefix);
                            resultFinacialEOList.add(eo);
                        }
                        break;
                    }
                }
            }
//            if (resultFile.getKey().contains("部")) { //按这个 中汽智联公司可能分发不到
            if (StringUtils.equals(orgEOList.get(0).getLayer(),"3")) { //部级
                //找总监
                List<UserEO> userEOList = userEODao.selectListByBelongOrgIdAndRoleCode(orgId, "BIZ_FINANCIAL_REPORT_RECEIVER");
                if (CollectionUtils.isNotEmpty(userEOList)) {
                    setResultFinacialEOList(userEOList, financialProcessTableEO,
                            resultFinacialEOList, resultFile, orgId, "", fileEO1, prefix);
                }
            } else {
                //科室级别
                List<UserEO> userEOList = userEODao.getByOrgIdAndRoleCode(orgId, "FINANCIAL_REPORT_RECEIVER");
                if (CollectionUtils.isNotEmpty(userEOList)) {
                    setResultFinacialEOList(userEOList, financialProcessTableEO,
                        resultFinacialEOList, resultFile, orgId, "", fileEO1, prefix);
                }
            }
            fileEOService.insertSelective(fileEO1);
        }
        //对联系人分发
        for (ResultFile resultFile : resultCallerFileList) {
            String key = resultFile.getKey();
            String deptName = key.split("_")[0];
            String userNames = key.split("_")[1];
            String[] usrNameArr = userNames.split(",");

            FileEO fileEO1 = new FileEO();
            fileEO1.setFileId(UUID.randomUUID10());
            fileEO1.setFileType(fileType);
            fileEO1.setContentType(contentType);
            fileEO1.setFileName(resultFile.getFileName());
            fileEO1.setSavePath(resultFile.getFilePath());
            fileEO1.setCreateTime(new Date());

            List<OrgEO> orgEOList = orgEODao.getOrgEOByOrgName(deptName);
            String orgId = "";
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                orgId = orgEOList.get(0).getId();
            }

            for (String userName : usrNameArr) {
                List<UserEO> userEOList = userEODao.getUserEOListByUserNameEquals(userName);

                if (CollectionUtils.isNotEmpty(userEOList)) {
                        setResultFinacialEOList(userEOList, financialProcessTableEO,
                                resultFinacialEOList, resultFile, orgId, deptName, fileEO1, prefix);
                } else {
                    logger.error("联系人 " +resultFile.getKey()+"中的 " + userName + " 不存在，请核查！");
                    //throw new AdcDaBaseException("联系人 "+ eo.getDeptName() +"不存在，请核查！");
                }

            }
            fileEOService.insertSelective(fileEO1);

        }
        dao.insertList(resultFinacialEOList);
        return financialProcessTableEO;
    }

    private void setResultFinacialEOList(List<UserEO> userEOList, FinancialProcessTableEO financialProcessTableEO,
        List<FinancialProcessTableEO> resultFinacialEOList, ResultFile resultFile, String orgId, String orgName,
        FileEO fileEO1, String prefix) {
        for (UserEO user : userEOList) {
            FinancialProcessTableEO eo = new FinancialProcessTableEO();
            eo.setId(UUID.randomUUID10());
            eo.setFinancialTableType(financialProcessTableEO.getFinancialTableType());
            if (StringUtils.equals(eo.getFinancialTableType(), "5")) {
                eo.setState(2);
            } else {
                eo.setState(4);
            }
            eo.setParentId(financialProcessTableEO.getId());
            eo.setExtInfo6(UUID.randomUUID10());
            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
            if (StringUtils.isEmpty(orgName)) {
                eo.setDeptName(resultFile.getKey());
            } else {
                eo.setDeptName(orgName);
            }
            eo.setDeptId(orgId);
            eo.setReceiveUserId(user.getUsid());
            eo.setReceiveUserName(user.getUsname());
            eo.setCreateUserName(financialProcessTableEO.getCreateUserName());
            eo.setCreateUserId(financialProcessTableEO.getCreateUserId());
            eo.setReceiveTime(new Date());
            eo.setCreateTime(financialProcessTableEO.getCreateTime());
            eo.setFileId(fileEO1.getFileId());
            eo.setFinancialTableName(eo.getDeptName() + "_" + prefix);
            resultFinacialEOList.add(eo);
        }
    }

    public void combineExcel(String id) throws Exception {
        FinancialProcessTableEOPage processTableEOPage = new FinancialProcessTableEOPage();
        processTableEOPage.setParentId(id);
        List<FinancialProcessTableEO> financialProcessTableEOList = queryByList(processTableEOPage);
        FinancialProcessTableEO eo = selectByPrimaryKey(id);
        FinancialProcessTableEO combine = combine(financialProcessTableEOList, eo);
        combine.setState(4);
        updateByPrimaryKeySelective(combine);
    }

    public FinancialProcessTableEO combine(List<FinancialProcessTableEO> financialProcessTableEOList,
        FinancialProcessTableEO eo) throws Exception {

        String fileType = "" ;
        String contentType = "";
        ArrayList<File> srcFiles = new ArrayList<>();
        for (FinancialProcessTableEO financialProcessTableEO : financialProcessTableEOList) {
            String fileId = financialProcessTableEO.getFileId();
            FileEO fileEO = fileEOService.selectByPrimaryKey(fileId);
            fileType = fileEO.getFileType();
            contentType = fileEO.getContentType();
            String savePath = fileEO.getSavePath();
            srcFiles.add(new File(savePath));
        }

        File DOCFile = getResFile("5_tpl.xlsx");

        Date now = new Date();
        String data = format.format(now);
        //判断目录是否已经存在
        String basePath01 = basePath + "/" + data;
        File file = new File(basePath01);
        if (!file.exists()) {
            file.mkdirs();
        }
        //重命名文件
        String name = eo.getFinancialTableName();
        String fileName01 = name.substring(0, name.indexOf(".")) + "（汇总）" + ".xlsx";
        String destFilePath = basePath01 + "/" + fileName01;
        ExcelCombineUtil.combineExcelsToOne(srcFiles, DOCFile, destFilePath, 0, 2);
        //文件的相对路径
        String path = data + "/" + fileName01;
        FileEO fileEO = new FileEO();

        eo.setCreateTime(now);

        fileEO.setFileId(UUID.randomUUID10());
        fileEO.setFileType(fileType);
        fileEO.setContentType(contentType);
        fileEO.setCreateTime(now);
        fileEO.setFileName(fileName01);
        fileEO.setSavePath(path);
        fileEO.setUserId(eo.getCreateUserId());
        fileEO.setUserName(eo.getCreateUserName());
        fileEOService.insertSelective(fileEO);

        eo.setFinancialTableName(fileName01);
        eo.setFileId(fileEO.getFileId());
        eo.setState(1);
        return eo;
    }

    public File getResFile(String fileName) throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("financial_tpl/" + fileName);
        File targetFile = new File(fileName);
        FileUtils.copyInputStreamToFile(stream, targetFile);
        return targetFile;
    }

    public void deleteByKey(List<String> ids) {
        dao.deleteLogicInBatch(ids);
        //物理删除关联的文件
        dao.deleteByParentId(ids);
    }

    public boolean isSameFileHead(FinancialProcessTableEO financialProcessTableEO) throws Exception {
        FileEO fileEO = fileEOService.selectByPrimaryKey(financialProcessTableEO.getFileId());
        String filePath = basePath + File.separator + fileEO.getSavePath();

        String fileType = fileEO.getFileType();
        if (!StringUtils.equals(fileType,"xls")&&!StringUtils.equals(fileType,"xlsx")){
            throw new AdcDaBaseException("所传文件不是excel格式，请检查！");
        }
        File file = new File(filePath);
        String prefix = "";
        if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "0")) {
            return true;
        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "1")) {
            prefix = "部门成本明细";
            File DOCFile = getResFile("1_tpl.xlsx");
            Workbook workbook = WorkbookFactory.create(DOCFile);
            List<String> stringList = ExcelSplitUtil.readerHeader(workbook.getSheetAt(0), 0, 2, 10);

            Workbook srcWorkbook = WorkbookFactory.create(file);
            List<String> srcStringList = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(0), 0, 2, 10);

            return ExcelSplitUtil.isSameHeader(stringList, srcStringList);

        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "2")) {
            prefix = "课题执行情况";
            File DOCFile = getResFile("2_tpl.xlsx");
            //课题执行情况表
            Workbook workbook = WorkbookFactory.create(DOCFile);
            List<String> stringList = ExcelSplitUtil.readerHeader(workbook.getSheetAt(0), 0, 2, 25);
            List<String> stringList2 = ExcelSplitUtil.readerHeader(workbook.getSheetAt(1), 0, 3, 9);
            Workbook srcWorkbook = WorkbookFactory.create(file);
            List<String> srcStringList = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(0), 0, 2, 25);
            List<String> srcStringList2 = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(1), 0, 3, 9);

            return ExcelSplitUtil.isSameHeader(stringList, srcStringList)&&ExcelSplitUtil.isSameHeader(stringList2, srcStringList2);

        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "3")) {
            prefix = "往来账款催办";
            File DOCFile = getResFile("3_tpl.xlsx");

            //往来账款催办表
            Workbook workbook = WorkbookFactory.create(DOCFile);
            List<String> stringList = ExcelSplitUtil.readerHeader(workbook.getSheetAt(0), 0, 1, 12);

            Workbook srcWorkbook = WorkbookFactory.create(file);
            List<String> srcStringList = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(0), 0, 1, 12);

            return ExcelSplitUtil.isSameHeader(stringList, srcStringList);

        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "4")) {
            prefix = "应收账款";

            File DOCFile = getResFile("4_tpl.xlsx");
            //应收账款
            Workbook workbook = WorkbookFactory.create(DOCFile);
            List<String> stringList = ExcelSplitUtil.readerHeader(workbook.getSheetAt(0), 0, 2, 16);

            Workbook srcWorkbook = WorkbookFactory.create(file);
            List<String> srcStringList = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(0), 0, 2, 16);

            return ExcelSplitUtil.isSameHeader(stringList, srcStringList);

        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "5")) {
            prefix = "支出合同明细";

            File DOCFile = getResFile("5_tpl.xlsx");

            //支出合同明细表
            Workbook workbook = WorkbookFactory.create(DOCFile);
            List<String> stringList = ExcelSplitUtil.readerHeader(workbook.getSheetAt(0), 0, 2, 20);

            Workbook srcWorkbook = WorkbookFactory.create(file);
            List<String> srcStringList = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(0), 0, 2, 20);

            return ExcelSplitUtil.isSameHeader(stringList, srcStringList);
        } else if (StringUtils.equals(financialProcessTableEO.getFinancialTableType(), "6")) {
        File DOCFile = getResFile("6_tpl.xlsx");
        //项目支出明细表
        Workbook workbook = WorkbookFactory.create(DOCFile);
        List<String> stringList = ExcelSplitUtil.readerHeader(workbook.getSheetAt(0), 0, 1, 13);

        Workbook srcWorkbook = WorkbookFactory.create(file);
        List<String> srcStringList = ExcelSplitUtil.readerHeader(srcWorkbook.getSheetAt(0), 0, 1, 13);

        return ExcelSplitUtil.isSameHeader(stringList, srcStringList);
    }

        return false;
    }

}
