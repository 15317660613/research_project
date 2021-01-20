package com.adc.da.research.funds.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.page.BasePage;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.dao.ProjectOverEODao;
import com.adc.da.research.funds.entity.ProjectExpendEO;
import com.adc.da.research.funds.entity.ProjectOverEO;
import com.adc.da.research.funds.eum.DelState;
import com.adc.da.research.funds.eum.OverState;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.page.ProjectExpendEOPage;
import com.adc.da.research.funds.page.ProjectOverEOPage;
import com.adc.da.research.funds.vo.over.ItemDataVO;
import com.adc.da.research.funds.vo.over.ProjectOverVO;
import com.adc.da.research.project.entity.BudgetDetailEO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.BudgetDetailEOPage;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.service.BudgetDetailEOService;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.UUID;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;


@Service("projectOverEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectOverEOService extends BaseService<ProjectOverEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectOverEOService.class);

    @Autowired
    private ProjectOverEODao dao;
    @Autowired
    private ProjectDataEOService projectDataEOService;
    @Autowired
    private BudgetDetailEOService budgetDetailEOService;
    @Autowired
    private ProjectExpendEOService projectExpendEOService;
    @Autowired
    private ProjectOverEOService projectOverEOService;
    @Autowired
    private BeanMapper beanMapper;

    public ProjectOverEODao getDao() {
        return dao;
    }

    /***
     * @Description: 根据给定的年份和参数，将符合的项目信息写入数据库中
     * @Param: [year, param]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/11/17
     */
    public void calculate(String year, String startDate, String endDate) throws Exception {

        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (com.adc.da.util.utils.CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){
            throw new AdcDaBaseException("此用户无权限");
        }

        //先清空状态为未结转的当年的数据
        ProjectOverEOPage projectOverEOPage = new ProjectOverEOPage();
        projectOverEOPage.setOverState(OverState.NOT_OVER.getCode().toString());
        final Integer integer = this.deleteByParam(projectOverEOPage);
        projectOverEOPage.setOverDateBegin(startDate);
        projectOverEOPage.setOverDateEnd(endDate);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

        //获取所有项目
        final List<ProjectDataEO> projectDataEOS = projectDataEOService.queryByList(new ProjectDataEOPage());
        //获取所有项目总和信息
        final List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryAmount(new BudgetDetailEOPage());
        //将获取到所有项目总和信息按项目id进行分组
        final Map<String, List<BudgetDetailEO>> totalProject =
                budgetDetailEOS.stream().filter(item->item.getBudgetYear().equals(year))
                        .collect(Collectors.groupingBy(BudgetDetailEO::getProjectId));
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        for (ProjectDataEO item : projectDataEOS) {
            flag:if (totalProject.containsKey(item.getId())) {
                ProjectOverEOPage projectOverPage = new ProjectOverEOPage();
                projectOverPage.setProjectId(item.getId());
                final List<ProjectOverEO> projectOverEOS = this.projectOverEOService.queryByList(projectOverPage);
                for (ProjectOverEO projectOverEO : projectOverEOS) {
                    if (formatYear.format(projectOverEO.getOverDateBegin()).equals(year)){
                        break flag;
                    }
                }
                final List<BudgetDetailEO> totalSub = totalProject.get(item.getId());
                for (BudgetDetailEO budgetDetailEO : totalSub) {
                    //合计的年份与参数年份相等则进行计算
                    if (budgetDetailEO.getBudgetYear().equals(year)) {
                        ProjectExpendEOPage page = new ProjectExpendEOPage();
                        page.setProjectId(item.getId());
                        page.setExpendDateBegin(startDate);
                        page.setExpendDateEnd(endDate);
                        List<ProjectExpendEO> projectExpendEOS = new ArrayList<>();
                        try {
                            projectExpendEOS = projectExpendEOService.queryByList(page);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        //计算支出总数
                        double sum = projectExpendEOS.stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                        //有余额的情况
                        if (budgetDetailEO.getBudgetAmount() - sum > 0) {
                            //对ProjectOverEO进行填充
                            ProjectOverEO projectOverEO = new ProjectOverEO();
                            projectOverEO.setId(UUID.randomUUID10());
                            projectOverEO.setProjectId(item.getId());
                            projectOverEO.setCreateTime(new Date());
                            projectOverEO.setProjectCode(item.getProjectCode());
                            projectOverEO.setDeptId(item.getDeptId());
                            try {
                                projectOverEO.setOverDateBegin(format.parse(startDate));
                            } catch (ParseException e) {
                                logger.error(e.getMessage(), e);
                            }
                            try {
                                projectOverEO.setOverDateEnd(format.parse(endDate));
                            } catch (ParseException e) {
                                logger.error(e.getMessage(), e);
                            }
                            projectOverEO.setOverState(OverState.NOT_OVER.getCode());
                            projectOverEO.setCreateUserId(user.getUsid());
                            projectOverEO.setCreateUserName(user.getUsname());
                            projectOverEO.setDelFlag(DelState.NOT_DELETED.getCode());
                            try {
                                this.insert(projectOverEO);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }
    }


    /***
     * @Description: 根据参数删除相关数据 （物理删除，小心使用！）
     * @Param: [page]
     * @return: java.lang.Integer
     * @Author: yanyujie
     * @Date: 2020/11/17
     */
    Integer deleteByParam(BasePage page) {
        return dao.deleteByParam(page);
    }

    /***
     * @Description: 结转功能（显示）
     * @Param: [id]
     * @return: com.adc.da.research.funds.vo.over.ProjectOverVO
     * @Author: yanyujie
     * @Date: 2020/11/17
     */
    public ProjectOverVO carryover(String id) throws Exception {

        //根据ID获取结转信息
        final ProjectOverEO projectOverEO = this.selectByPrimaryKey(id);

        if (Objects.nonNull(projectOverEO)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

            //对此条结转信息进行计算
            //获取所有项目总和信息
            final List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryAmount(new BudgetDetailEOPage());
            //将获取到所有项目总和信息按项目id进行分组
            final Map<String, List<BudgetDetailEO>> totalProject =
                    budgetDetailEOS.stream().collect(Collectors.groupingBy(BudgetDetailEO::getProjectId));
            if (totalProject.containsKey(projectOverEO.getProjectId())) {
                final List<BudgetDetailEO> totalSub = totalProject.get(projectOverEO.getProjectId());
                for (BudgetDetailEO budgetDetailEO : totalSub) {
                    if (budgetDetailEO.getBudgetYear().equals(
                            format.format(projectOverEO.getOverDateBegin()).substring(0, 4))) {
                        //获取支出信息
                        ProjectExpendEOPage page = new ProjectExpendEOPage();
                        page.setProjectId(projectOverEO.getProjectId());
                        page.setExpendDateBegin(format.format(projectOverEO.getOverDateBegin()));
                        page.setExpendDateEnd(format.format(projectOverEO.getOverDateEnd()));
                        final List<ProjectExpendEO> projectExpendEOS = projectExpendEOService.queryByList(page);
                        //计算支出总数
                        final double sum = projectExpendEOS.stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                        ProjectOverVO projectOverVO = beanMapper.map(projectOverEO, ProjectOverVO.class);

                        //总金额
                        projectOverVO.setSumAmount(budgetDetailEO.getBudgetAmount());
                        projectOverVO.setBalance(budgetDetailEO.getBudgetAmount() - sum);

                        //年份
                        projectOverVO.setYear(formatYear.format(projectOverEO.getOverDateBegin()));
                        return projectOverVO;
                    }
                }
            }
        }
        return new ProjectOverVO();
    }

    /***
     * @Description: 结转操作
     * @Param: [id, percentage]
     * @return: com.adc.da.research.funds.vo.over.ProjectOverVO
     * @Author: yanyujie
     * @Date: 2020/11/18
     */
    public Integer carryoverAct(String id, String percentage) throws Exception {
        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (com.adc.da.util.utils.CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){
            throw new AdcDaBaseException("此用户无权限");
        }
        if (percentage.contains("-")){
            throw new AdcDaBaseException("不可输入负数");
        }
        //增加"%"后面做格式转换使用
        percentage=percentage+"%";
        int mark=0;
        final ProjectOverVO carryover = this.carryover(id);
        if (Objects.nonNull(carryover) && carryover.getOverState()==OverState.NOT_OVER.getCode()){
            NumberFormat format= NumberFormat.getPercentInstance();
            final double percentageDouble = format.parse(percentage).doubleValue();
            //使用BigDecimal计算
            BigDecimal balance = new BigDecimal(carryover.getBalance());
            BigDecimal factor =new BigDecimal(percentageDouble);
            final BigDecimal multiply = balance.multiply(factor);
            //保留四位小数，四舍五入
            final BigDecimal bigDecimal = multiply.setScale(4, BigDecimal.ROUND_HALF_UP);

            //修改 carryover并保存
            carryover.setBalance(carryover.getSumAmount()-multiply.doubleValue());
            carryover.setOverAmount(bigDecimal.doubleValue());
            carryover.setOverState(OverState.OVER.getCode());
            carryover.setOverPercent(percentageDouble);

            carryover.setExt2(String.valueOf(carryover.getBalance()));
            //记录结转人名称

            UserEO user = UserUtils.getUser();
            if (ObjectUtil.isNull(user)){
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            carryover.setExt1(user.getUsname().trim());
            //记录结转时间
            carryover.setModifyTime(new Date());

            mark = this.updateByPrimaryKeySelective(beanMapper.map(carryover, ProjectOverEO.class));
        }
        return mark;
    }


    /***
    * @Description: 查看
    * @Param: [id]
    * @return: com.adc.da.research.funds.vo.over.ProjectOverVO
    * @Author: yanyujie
    * @Date: 2020/11/25
    */
    public ProjectOverVO details(String id) throws Exception {
        final ProjectOverVO carryover = this.carryover(id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        DecimalFormat format = new DecimalFormat("#.####");
        if (Objects.nonNull(carryover)) {
            final ProjectOverVO projectOverVO = beanMapper.map(carryover, ProjectOverVO.class);

            ProjectOverEOPage projectOverEOPage=new ProjectOverEOPage();
            projectOverEOPage.setProjectId(carryover.getProjectId());
            final List<ProjectOverEO> projectOverEOS = this.queryByList(projectOverEOPage);
            List<ItemDataVO> itemDataVOS=new ArrayList<>();
            projectOverEOS.forEach(item->{
                ItemDataVO itemDataVO=new ItemDataVO();
                itemDataVO.setOverDate(item.getModifyTime());
                itemDataVO.setYear(dateFormat.format(item.getOverDateBegin()));
                itemDataVO.setOverDateBegin(item.getOverDateBegin());
                itemDataVO.setOverDateEnd(item.getOverDateEnd());

                itemDataVO.setTechnicalDirector(item.getTechnicalDirector());
                if (item.getOverPercent().equals(1.0)){
                    itemDataVO.setPercentage("100%");
                }else {
                    itemDataVO.setPercentage(item.getOverPercent()*100+"%");
                }
                itemDataVO.setAmount(item.getOverAmount());
                itemDataVO.setOperator(item.getExt1());
                try {
                    itemDataVO.setRemainAmount(
                            Double.parseDouble(format.
                                    format(item.getOverAmount()/item.getOverPercent()-itemDataVO.getAmount())));
                } catch (Exception e) {
                    itemDataVO.setRemainAmount(0d);
                }
                itemDataVOS.add(itemDataVO);
            });
            projectOverVO.setProjectOverVOList(itemDataVOS.stream().filter(item->!item.getPercentage()
                    .contains("null")).collect(Collectors.toList()));
            return projectOverVO;
        }
        return null;
    }

    /**
     * 导出
     * @param exportParams
     * @param page
     * @return
     * @throws Exception
     */
    public Workbook export(ExportParams exportParams, ProjectOverEOPage page) throws Exception {

        List<ProjectOverEO> projectOverEOS=new ArrayList<>();
        if (StringUtils.isNotBlank(page.getIds()))
        {
            final String[] split = page.getIds().split(",", -1);
            for (String idStr : split) {
                ProjectOverEOPage idPage=new ProjectOverEOPage();
                idPage.setId(idStr);
                projectOverEOS.addAll(this.queryByList(idPage));
            }
        }else{
            projectOverEOS.addAll(this.queryByList(page)) ;
        }

        if (CollectionUtils.isNotEmpty(projectOverEOS)){
            projectOverEOS.forEach(item->{
                {
                    final ArrayList technicalList = JSON.parseObject(item.getTechnicalDirector(), ArrayList.class);
                    if (CollectionUtils.isNotEmpty(technicalList)) {
                        Map<String, String> technicalerInfo = (Map<String, String>) technicalList.get(0);
                    if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"))) {
                            item.setTechnicalDirector(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                        }
                    }
                }
            });
        }
        List<ProjectOverVO> resutList = new ArrayList<>();
        if (projectOverEOS.size() > 0) {
            resutList = beanMapper.mapList(projectOverEOS, ProjectOverVO.class);
        }
        return ExcelExportUtil.exportExcel(exportParams, ProjectOverVO.class, resutList);
    }
}
