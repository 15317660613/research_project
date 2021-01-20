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
import com.adc.da.research.funds.dao.ProjectIncomeEODao;
import com.adc.da.research.funds.entity.ProjectIncomeEO;
import com.adc.da.research.funds.eum.AmountType;
import com.adc.da.research.funds.eum.DelState;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.page.ProjectIncomeEOPage;
import com.adc.da.research.funds.vo.income.ErrorIncomeVO;
import com.adc.da.research.funds.vo.income.ProjectIncomeVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * ProjectIncomeEOController 服务类
 */
@Service("projectIncomeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectIncomeEOService extends BaseService<ProjectIncomeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectIncomeEOService.class);

    @Autowired
    private ProjectIncomeEODao dao;
    @Autowired
    private ProjectDataEOService projectDataEOService;
    @Autowired
    private DeptBudgetEOService deptBudgetEOService;
    @Autowired
    private BeanMapper beanMapper;


    public ProjectIncomeEODao getDao() {
        return dao;
    }

    /***
     * @Description: 导入功能
     * @Param: [is]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/11/11
     */
    public List excelImport(InputStream is) throws Exception {

        List<String> errorList = new ArrayList<>();
        final String[] HEADERS = new String[]{"科目名称", "部门名称", "项目档案名称", "凭证号", "摘要", "到账金额"};
        ImportParams params = new ImportParams();
        //表头为两行内容
        params.setHeadRows(2);
        final List<Map<String, String>> incomeList = ExcelImportUtil.importExcel(is, Map.class, params);
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
            ProjectIncomeEO projectIncomeEO = new ProjectIncomeEO();
            dateStr.setLength(0);
            for (Map.Entry<String, String> entry : item.entrySet()) {
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
                        projectIncomeEO.setIncomeDate(format.parse(dateStr.toString()));
                    } catch (ParseException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                //项目档案名称整理
                if (entry.getKey().contains("项目档案名称")) {
                    String projectName = "";

                    if (entry.getValue().contains("(国拨")) {
                        final String[] split = entry.getValue().split("\\(国拨", -1);
                        projectName = split[0].trim();
                    }
                    if (entry.getValue().contains("（国拨")) {
                        final String[] split = entry.getValue().split("（国拨", -1);
                        projectName = split[0].trim();
                    }
                    ProjectDataEOPage page = new ProjectDataEOPage();
                    if (StringUtils.isBlank(projectName)){
                        projectName=entry.getValue().trim();
                    }
                    page.setProjectName(projectName);
                    List<ProjectDataEO> projectDataEOS = new ArrayList<>();
                    try {
                        projectDataEOS = projectDataEOService.queryByList(page);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    if (projectDataEOS.size() > 0) {
                        //项目id
                        projectIncomeEO.setProjectId(projectDataEOS.get(0).getId());
                        //项目code
                        projectIncomeEO.setProjectCode(projectDataEOS.get(0).getProjectCode());
                    } else {
                        errorList.add(item.toString());
                        projectIncomeEO = new ProjectIncomeEO();
                        break;
                    }
                }

                //部门名称整理
                if (entry.getKey().contains("部门名称")) {
                    DeptBudgetEOPage page = new DeptBudgetEOPage();
                    page.setDeptName(entry.getValue().trim());
                    List<DeptBudgetEO> deptBudgetEOS = new ArrayList<>();

                    try {
                        deptBudgetEOS = deptBudgetEOService.queryByList(page);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                    if (CollectionUtils.isNotEmpty(deptBudgetEOS)) {
                        projectIncomeEO.setDeptId(deptBudgetEOS.get(0).getDeptId());
                    } else {
                        errorList.add(item.toString());
                        projectIncomeEO = new ProjectIncomeEO();
                        break;
                    }
                }

                //科目名称
                try {
                    if (entry.getKey().contains("科目名称")) {
                        projectIncomeEO.setSubjectName(entry.getValue().trim());
                    }
                } catch (Exception e) {
                    errorList.add(item.toString());
                    break;
                }

                //凭证号
                try {
                    if (entry.getKey().contains("凭证号")) {
                        projectIncomeEO.setVoucherNumber(entry.getValue().trim());
                    }
                } catch (Exception e) {
                    errorList.add(item.toString());
                    break;
                }

                //摘要

                try {
                    if (entry.getKey().contains("摘要")) {
                        projectIncomeEO.setSummary(entry.getValue().trim());
                    }
                } catch (Exception e) {
                    errorList.add(item.toString());
                    break;
                }

                //对到账金额与类型的整理
                try {
                    if (entry.getKey().contains("到账金额")) {
                        projectIncomeEO.setIncomeAmount(Double.parseDouble(String.valueOf(entry.getValue())));
                        //此导入和需求沟通过全部为国拨经费
                        projectIncomeEO.setIncomeType(AmountType.STATE_FUND.getLabel());
                    }
                } catch (NumberFormatException e) {
                    errorList.add(item.toString());
                    break;
                }

            }
            if (StringUtils.isNotBlank(projectIncomeEO.getDeptId()) && StringUtils.isNotBlank(projectIncomeEO.getProjectId())) {
                try {
                    projectIncomeEO.setId(UUID.randomUUID());
                    UserEO user = UserUtils.getUser();
                    if (ObjectUtil.isNull(user)){
                        throw new AdcDaBaseException("登陆可能过期，请登录！");
                    }
                    projectIncomeEO.setCreateUserId(user.getUsid());
                    projectIncomeEO.setCreateUserName(user.getUsname());
                    projectIncomeEO.setDelFlag(DelState.NOT_DELETED.getCode());
                    //如果4个维度一样则对数据库不进行操作
                    ProjectIncomeEOPage projectIncomeEOPage = new ProjectIncomeEOPage();
                    projectIncomeEOPage.setIncomeDate(format.format(projectIncomeEO.getIncomeDate()));
                    projectIncomeEOPage.setProjectId(projectIncomeEO.getProjectId());
                    projectIncomeEOPage.setVoucherNumber(projectIncomeEO.getVoucherNumber());
                    projectIncomeEOPage.setIncomeAmount(String.valueOf(projectIncomeEO.getIncomeAmount()));
                    final int i = this.queryByCount(projectIncomeEOPage);
                    if (i == 0) {
                        this.insert(projectIncomeEO);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

        });

        //整理返回错误列表信息
//        List<String>resultErrorList=new ArrayList<>();
        List<ErrorIncomeVO>errorIncomeVOList=new ArrayList<>();
        errorList.forEach(error -> {
            ErrorIncomeVO errorIncomeVO=new ErrorIncomeVO();
            //凭证号
            String voucher = error.substring(error.indexOf("凭证号="), error.indexOf(", 摘要"));
            //项目档案名称
            String fileName = error.substring(error.indexOf("项目档案名称="), error.indexOf(", 凭证号"));
            //科目名称
            String subjectName = error.substring(error.indexOf("科目名称="), error.indexOf(", 部门名称"));
            //日期
            String data = error.substring(1, error.indexOf("_月")) + "-" + error.substring(error.indexOf("月=") + 2, error.indexOf(",")) + "-" +
                    error.substring(error.indexOf("_日=") + 3, error.indexOf(", 科目名称"));
            String amount=error.substring(error.indexOf("金额_贷方=")+6,error.length()-1);

            errorIncomeVO.setDate(data);
            errorIncomeVO.setAmount(amount);
            errorIncomeVO.setFileName(fileName);
            errorIncomeVO.setSubjectName(subjectName);
            errorIncomeVO.setVoucher(voucher);
            errorIncomeVOList.add(errorIncomeVO);
           // resultErrorList.add(data + " " + fileName + subjectName + voucher);
        });
        return errorIncomeVOList;
    }

    public Workbook export(ExportParams exportParams, ProjectIncomeEOPage page) throws Exception {

        List<ProjectIncomeEO> projectDataEOS=new ArrayList<>();
        if (StringUtils.isNotBlank(page.getIds()))
        {
            final String[] split = page.getIds().split(",", -1);
            for (String idStr : split) {
                ProjectIncomeEOPage idPage=new ProjectIncomeEOPage();
                idPage.setId(idStr);
                projectDataEOS.addAll(this.queryByList(idPage));
            }
        }else{
            projectDataEOS.addAll(this.queryByList(page)) ;
        }
        List<ProjectIncomeVO> resutList = new ArrayList<>();
        if (projectDataEOS.size() > 0) {
            resutList = beanMapper.mapList(projectDataEOS, ProjectIncomeVO.class);
        }
        return ExcelExportUtil.exportExcel(exportParams, ProjectIncomeVO.class, resutList);
    }

    /***
     * @Description: 显示详情（单一）
     * @Param: [id]
     * @return: com.adc.da.research.funds.vo.income.ProjectIncomeVO
     * @Author: yanyujie
     * @Date: 2020/11/12
     */
    public ProjectIncomeVO details(String id) throws Exception {
        ProjectIncomeEOPage page = new ProjectIncomeEOPage();
        page.setId(id);
        final ProjectIncomeEO projectIncomeEO = this.selectByPrimaryKey(id);
        if (Objects.nonNull(projectIncomeEO)) {
            final ProjectIncomeVO projectIncomeVO = beanMapper.map(projectIncomeEO, ProjectIncomeVO.class);
            return projectIncomeVO;
        }
        return null;
    }

    /***
     * @Description: 显示详情（列表，用项目id去查）
     * @Param: [projectId]
     * @return: java.util.List<com.adc.da.research.funds.vo.income.ProjectIncomeVO>
     * @Author: yanyujie
     * @Date: 2020/11/12
     */
    public List<ProjectIncomeVO> detailsList(String projectId) throws Exception {
        ProjectIncomeEOPage page = new ProjectIncomeEOPage();
        page.setProjectId(projectId);

        final List<ProjectIncomeEO> projectIncomeEOS = this.queryByList(page);
        if (CollectionUtils.isNotEmpty(projectIncomeEOS)) {
            final List<ProjectIncomeVO> projectIncomeVOS = beanMapper.mapList(projectIncomeEOS, ProjectIncomeVO.class);
            return projectIncomeVOS;
        }
        return null;
    }

    /***
    * @Description: 带权限的分页查询功能
    * @Param: [page]
    * @return: java.util.List<com.adc.da.research.funds.entity.ProjectIncomeEO>
    * @Author: yanyujie
    * @Date: 2020/11/13
    */
    public List<ProjectIncomeEO> queryByPagePermission(ProjectIncomeEOPage page) throws Exception {
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
