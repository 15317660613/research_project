package com.adc.da.research.funds.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.business.entity.DeptBudgetEO;
import com.adc.da.business.page.DeptBudgetEOPage;
import com.adc.da.business.service.DeptBudgetEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.dao.ProjectExpendEODao;
import com.adc.da.research.funds.entity.ProjectExpendEO;
import com.adc.da.research.funds.eum.AmountType;
import com.adc.da.research.funds.eum.DelState;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.page.ProjectExpendEOPage;
import com.adc.da.research.funds.vo.expend.ErrorExpendVO;
import com.adc.da.research.funds.vo.expend.ProjectExpendVO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;



@Service("projectExpendEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectExpendEOService extends BaseService<ProjectExpendEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectExpendEOService.class);

    @Autowired
    private ProjectExpendEODao dao;

    @Autowired
    private DeptBudgetEOService deptBudgetEOService;
    @Autowired
    private ProjectDataEOService projectDataEOService;
    @Autowired
    private BeanMapper beanMapper;

    public ProjectExpendEODao getDao() {
        return dao;
    }

    public List excelImport(InputStream is) throws Exception {
        List<String> errorList = new ArrayList<>();
        final String[] HEADERS = new String[]{"本部", "部门名称", "科目名称", "项目档案名称", "凭证号", "摘要", "金额"};
        ImportParams params = new ImportParams();
        //表头为两行内容
        params.setHeadRows(2);
        final List<Map<String, Object>> incomeList = ExcelImportUtil.importExcel(is, Map.class, params);
        //判断表头信息
        if (CollectionUtils.isEmpty(incomeList)) {
            throw new AdcDaBaseException("导入文件异常，请核查");
        }

        final Set<String> headers = incomeList.get(0).keySet();
        //整理导入表头信息
        final List<String> headersReslut = new ArrayList<>();
        for (String header : headers) {
            if (!header.contains("_月") && !header.contains("_日")) {
                final String[] split = header.split("_", -1);
                headersReslut.add(split[0]);
            }
        }
        //核对表头信息
        for (String headerItem : HEADERS) {
            if (!headersReslut.contains(headerItem)) {
                throw new AdcDaBaseException("导入文件表头不正确，缺少" + headerItem + "信息，请核查");

            }
        }

        //整理信息准备入库
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        incomeList.stream().forEach(item -> {
            StringBuffer dateStr = new StringBuffer();
            ProjectExpendEO projectExpendEO = new ProjectExpendEO();
            dateStr.setLength(0);
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                //整理入账日期
                if ((entry.getKey().contains("月"))) {
                    final String[] split = entry.getKey().split("_", -1);
                    dateStr.append(split[0]);
                    dateStr.append("-");
                    //月份补零
                    String monthStr = String.valueOf(entry.getValue());
                    if (monthStr.length() == 1) {
                        monthStr = "0" + monthStr;
                    }
                    dateStr.append(monthStr);
                    dateStr.append("-");
                }

                if (entry.getKey().contains("_日")) {
                    String dayStr = String.valueOf(entry.getValue());
                    if (dayStr.length() == 1) {
                        dayStr = "0" + dayStr;
                    }
                    dateStr.append(dayStr);
                    try {
                        if (StringUtils.isNotBlank(dayStr) && !dateStr.toString().contains("null")){
                            projectExpendEO.setExpendDate(format.parse(dateStr.toString()));
                        }
                    } catch (ParseException e) {
                        logger.warn("此日期不完整");
                    }
                }

                //部门名称整理
                if (entry.getKey().contains("部门名称")) {
                    DeptBudgetEOPage page = new DeptBudgetEOPage();
                    page.setDeptName(entry.getValue() == null ? "" : entry.getValue().toString().trim());
                    List<DeptBudgetEO> deptBudgetEOS = new ArrayList<>();

                    try {
                        deptBudgetEOS = deptBudgetEOService.queryByList(page);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        errorList.add(item.toString());
                        break;
                    }

                    if (CollectionUtils.isNotEmpty(deptBudgetEOS)) {
                        projectExpendEO.setDeptId(deptBudgetEOS.get(0).getDeptId());
                    } else {
                        errorList.add(item.toString());
                        projectExpendEO = new ProjectExpendEO();
                        break;
                    }
                }

                //科目名称
                try {
                    if (entry.getKey().contains("科目名称")) {
                        projectExpendEO.setSubjectName(entry.getValue() == null ? "" : entry.getValue().toString().trim());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    errorList.add(item.toString());
                    break;
                }

                //凭证号
                try {
                    if (entry.getKey().contains("凭证号")) {
                        projectExpendEO.setVoucherNumber(entry.getValue() == null ? "" : entry.getValue().toString().trim());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    errorList.add(item.toString());
                    break;
                }

                //摘要

                try {
                    if (entry.getKey().contains("摘要")) {
                        projectExpendEO.setSummary(entry.getValue() == null ? "" : entry.getValue().toString().trim());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    errorList.add(item.toString());
                    break;
                }

                //金额
                try {
                    if (entry.getKey().contains("金额")) {
                        projectExpendEO.setExpendAmount(Double.parseDouble(String.valueOf(entry.getValue())));
                    }
                } catch (NumberFormatException e) {
                    logger.error(e.getMessage(), e);
                    errorList.add(item.toString());
                    break;
                }


                //项目档案名称
                if (entry.getKey().contains("项目档案名称")) {
                    //如果项目档案名称含国拨 就是国拨
                    String projectName = "";
                    List<ProjectDataEO> projectDataEOS=new ArrayList<>();
                    if (entry.getValue().toString().contains("国拨")) {
                        if (entry.getValue().toString().contains("(国拨")) {
                            final String[] split = entry.getValue().toString().split("\\(国拨", -1);
                            projectName = split[0].trim();
                            //设置国拨经费
                            projectExpendEO.setExpendType(AmountType.STATE_FUND.getLabel());
                        }
                        if (entry.getValue().toString().contains("（国拨")) {
                            final String[] split = entry.getValue().toString().split("（国拨", -1);
                            projectName = split[0].trim();
                            //设置国拨经费
                            projectExpendEO.setExpendType(AmountType.STATE_FUND.getLabel());
                        }
                    } else if (entry.getValue().toString().contains("自筹")) {
                        //如果项目档案名称含自筹 就是自筹

                        if (entry.getValue().toString().contains("(自筹")) {
                            final String[] split = entry.getValue().toString().split("\\(自筹", -1);
                            projectName = split[0].trim();
                            //设置自筹经费
                            projectExpendEO.setExpendType(AmountType.SELF_FUND.getLabel());
                        }
                        if (entry.getValue().toString().contains("（自筹")) {
                            final String[] split = entry.getValue().toString().split("（自筹", -1);
                            projectName = split[0].trim();
                            //设置自筹经费
                            projectExpendEO.setExpendType(AmountType.SELF_FUND.getLabel());
                        }
                    }else{
                        //如果没有提示 项目信息-->看国拨和自筹两个哪个有数值就算哪个 如果两个都有则异常
                        ProjectDataEOPage page = new ProjectDataEOPage();
                        page.setProjectName(entry.getValue().toString().trim());
                        try {
                            projectDataEOS= projectDataEOService.queryByList(page);
                        } catch (Exception e) {
                            logger.error(e.getMessage(),e);
                            errorList.add(item.toString());
                            break;
                        }
                        //找不到项目信息则异常
                        if (CollectionUtils.isEmpty(projectDataEOS)){
                            errorList.add(item.toString());
                            break;
                        }
                        //如果国拨有值自筹无值的情况
                        if (Objects.nonNull(projectDataEOS.get(0).getStateFunding()) &&
                                Objects.isNull(projectDataEOS.get(0).getSelfFunded())){
                            //设置国拨经费
                            projectExpendEO.setExpendType(AmountType.STATE_FUND.getLabel());
                        }
                        //如果国拨无值自筹有值的情况
                        if (Objects.isNull(projectDataEOS.get(0).getStateFunding()) &&
                                Objects.nonNull(projectDataEOS.get(0).getSelfFunded())){
                            //设置自筹经费
                            projectExpendEO.setExpendType(AmountType.SELF_FUND.getLabel());
                        }
                        //如果两者都有值的情况 异常
                        if (Objects.nonNull(projectDataEOS.get(0).getStateFunding()) &&
                                Objects.nonNull(projectDataEOS.get(0).getSelfFunded())){
                            errorList.add(item.toString());
                            break;
                        }
                    }
                //整理项目档案名称

                    //判断如果projectDataEOS有值则省去一次查询
                    if (CollectionUtils.isEmpty(projectDataEOS)){
                        ProjectDataEOPage page = new ProjectDataEOPage();
                        page.setProjectName(projectName);
                        try {
                            projectDataEOS= projectDataEOService.queryByList(page);
                        } catch (Exception e) {
                            logger.error(e.getMessage(),e);
                            errorList.add(item.toString());
                            break;
                        }
                    }

                    if (projectDataEOS.size()>0){
                        //项目id
                        projectExpendEO.setProjectId(projectDataEOS.get(0).getId());
                        //项目code
                        projectExpendEO.setProjectCode(projectDataEOS.get(0).getProjectCode());
                    }else{
                        errorList.add(item.toString());
                        projectExpendEO = new ProjectExpendEO();
                        break;
                    }
                }
            }

            if (StringUtils.isNotBlank(projectExpendEO.getDeptId()) && StringUtils.isNotBlank(projectExpendEO.getProjectId())) {
                try {
                    projectExpendEO.setId(UUID.randomUUID());
                    UserEO user = UserUtils.getUser();
                    if (ObjectUtil.isNull(user)){
                        throw new AdcDaBaseException("登陆可能过期，请登录！");
                    }
                    projectExpendEO.setCreateUserId(user.getUsid());
                    projectExpendEO.setCreateUserName(user.getUsname());
                    //如果4个维度一样则对数据库不进行操作
                    ProjectExpendEOPage projectExpendEOPage=new ProjectExpendEOPage();
                    projectExpendEOPage.setExpendDate(format.format(projectExpendEO.getExpendDate()));
                    projectExpendEOPage.setProjectId(projectExpendEO.getProjectId());
                    projectExpendEOPage.setVoucherNumber(projectExpendEO.getVoucherNumber());
                    projectExpendEOPage.setExpendAmount(String.valueOf(projectExpendEO.getExpendAmount()));
                    final int i = this.queryByCount(projectExpendEOPage);
                    if (i==0){
                        projectExpendEO.setDelFlag(DelState.NOT_DELETED.getCode());
                        this.insert(projectExpendEO);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                } finally {
                    projectExpendEO = new ProjectExpendEO();
                }
            }
        });

        //整理返回错误列表信息
//        List<String>resultErrorList=new ArrayList<>();
        List<ErrorExpendVO>errorExpendVOList=new ArrayList<>();
        errorList.forEach(error -> {
            ErrorExpendVO errorExpendVO=new ErrorExpendVO();
            //凭证号
            String voucher = error.substring(error.indexOf("凭证号=")+4, error.indexOf(", 摘要"));
            //项目档案名称
            String fileName = error.substring(error.indexOf("项目档案名称=")+7, error.indexOf(", 凭证号"));
            //科目名称
            String subjectName = error.substring(error.indexOf("科目名称=")+5, error.indexOf(", 项目档案名称"));
            //日期
            String data = error.substring(1, error.indexOf("_月")) + "-" + error.substring(error.indexOf("月=") + 2, error.indexOf(",")) + "-" +
                    error.substring(error.indexOf("_日=") + 3, error.indexOf(", 本部="));

            String amount = error.substring(error.indexOf("金额_借方=")+6,error.length()-1);

            errorExpendVO.setDate(data);
            errorExpendVO.setAmount(amount);
            errorExpendVO.setFileName(fileName);
            errorExpendVO.setSubjectName(subjectName);
            errorExpendVO.setVoucher(voucher);
            errorExpendVOList.add(errorExpendVO);
//            resultErrorList.add(data + " " + fileName + subjectName + voucher);
        });

        return errorExpendVOList;
    }

    public Workbook export(ExportParams exportParams, ProjectExpendEOPage page) throws Exception {
        List<ProjectExpendEO> projectDataEOS=new ArrayList<>();
        if (StringUtils.isNotBlank(page.getIds()))
        {
            final String[] split = page.getIds().split(",", -1);
            for (String idStr : split) {
                ProjectExpendEOPage idPage=new ProjectExpendEOPage();
                idPage.setId(idStr);
                projectDataEOS.addAll(this.queryByList(idPage));
            }
        }else{
            projectDataEOS.addAll(this.queryByList(page)) ;
        }
        List<ProjectExpendVO> resutList = new ArrayList<>();
        if (projectDataEOS.size()>0){
            resutList = beanMapper.mapList(projectDataEOS, ProjectExpendVO.class);
        }
        return ExcelExportUtil.exportExcel(exportParams, ProjectExpendVO.class,resutList);

    }

    public ProjectExpendVO details(String id) throws Exception {
        ProjectExpendEOPage page =new ProjectExpendEOPage();
        page.setId(id);
        final ProjectExpendEO projectExpendEO = this.selectByPrimaryKey(id);
        if (Objects.nonNull(projectExpendEO)){
            final ProjectExpendVO projectExpendVO = beanMapper.map(projectExpendEO, ProjectExpendVO.class);
            return projectExpendVO;
        }
        return null;
    }

    public List<ProjectExpendVO> detailsList(String projectId) throws Exception {
        ProjectExpendEOPage page =new ProjectExpendEOPage();
        page.setProjectId(projectId);

        final List<ProjectExpendEO> projectExpendEOS = this.queryByList(page);
        if (CollectionUtils.isNotEmpty(projectExpendEOS)){
            final List<ProjectExpendVO> projectExpendVOS = beanMapper.mapList(projectExpendEOS, ProjectExpendVO.class);
            return projectExpendVOS;
        }
        return null;
    }

    public List<ProjectExpendEO> queryByPagePermission(ProjectExpendEOPage page) throws Exception {
        //得到当前用户权限列表
        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){

            UserEO user = UserUtils.getUser();
            if (ObjectUtil.isNull(user)){
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }

            page.setCreateUserId(user.getUsid());
        }
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByPage(page);
    }

    /***
     * @Description: 根据IDS逻辑删除
     * @Param: [param]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/11/24
     */
    public void logicDeleteByPrimaryKeys(List<String>param){
        dao.logicDeleteByPrimaryKeys(param);
    }


}
