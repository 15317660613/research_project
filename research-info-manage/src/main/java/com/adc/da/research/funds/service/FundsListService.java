package com.adc.da.research.funds.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.adc.da.base.service.BaseService;
import com.adc.da.research.funds.entity.ProjectExpendEO;
import com.adc.da.research.funds.entity.ProjectIncomeEO;
import com.adc.da.research.funds.eum.AmountType;
import com.adc.da.research.funds.eum.FundsListType;
import com.adc.da.research.funds.page.ProjectExpendEOPage;
import com.adc.da.research.funds.page.ProjectIncomeEOPage;
import com.adc.da.research.funds.vo.fundsList.FundsListVO;
import com.adc.da.research.project.dao.ProjectDataEODao;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/10
 * @Description:
 */
@Service
public class FundsListService extends BaseService<ProjectDataEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FundsListService.class);

    @Autowired
    private ProjectDataEOService projectDataEOService;

    @Autowired
    private ProjectExpendEOService projectExpendEOService;

    @Autowired
    private ProjectIncomeEOService projectIncomeEOService;

    @Autowired
    private ProjectDataEODao dao;

    @Autowired
    private BeanMapper beanMapper;

    public ProjectDataEODao getDao() {
        return dao;
    }


    public List<FundsListVO> page(ProjectDataEOPage page, Integer type) throws Exception {
        List<ProjectDataEO> projectDataEOS = new ArrayList<>();
        if (type == FundsListType.PAGE.getValue()) {
            projectDataEOS = projectDataEOService.queryByPageForFunds(page);
        }else{
            projectDataEOS = projectDataEOService.queryByListForFunds(page);
        }
        List<FundsListVO> listVOS = new ArrayList<>();

        //获取本年的年度信息
        LocalDate localDate = LocalDate.now();
        final int year = localDate.getYear();
        String dateStart = year + "-01-01";
        String dateEnd = year + "-12-31";
        //将ProjectDataEO转换成FundsListVO
        projectDataEOS.stream().forEach(item -> {
            FundsListVO fundsListVO = new FundsListVO();
            BeanUtils.copyProperties(item, fundsListVO);
            //计算国拨经费已到账累计到账经费

            ProjectIncomeEOPage projectIncomeEOPage = new ProjectIncomeEOPage();
            projectIncomeEOPage.setProjectId(item.getId());
            //费用类型为国拨
            projectIncomeEOPage.setIncomeType(AmountType.STATE_FUND.getLabel());
            List<ProjectIncomeEO> projectIncomeEOS = new ArrayList<>();
            try {
                projectIncomeEOS = projectIncomeEOService.queryByList(projectIncomeEOPage);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            if (CollectionUtils.isNotEmpty(projectIncomeEOS)) {
                //计算国拨经费已到账累加
                final double sum = projectIncomeEOS.stream().mapToDouble(ProjectIncomeEO::getIncomeAmount).sum();
                fundsListVO.setStateIncomeSum(sum);
            }

            projectIncomeEOPage.setIncomeDateBegin(dateStart);
            projectIncomeEOPage.setIncomeDateEnd(dateEnd);

            List<ProjectIncomeEO> projectIncomeEOSDate = new ArrayList<>();
            try {
                projectIncomeEOSDate = projectIncomeEOService.queryByList(projectIncomeEOPage);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            if (CollectionUtils.isNotEmpty(projectIncomeEOSDate)) {
                //计算本年度国拨经费累加
                final double sum = projectIncomeEOSDate.stream().mapToDouble(ProjectIncomeEO::getIncomeAmount).sum();
                fundsListVO.setStateIncomeDate(sum);
            }
            //计算国拨支出费用
            ProjectExpendEOPage projectExpendEOPage = new ProjectExpendEOPage();
            projectExpendEOPage.setExpendType(AmountType.STATE_FUND.getLabel());
            projectExpendEOPage.setProjectId(item.getId());
            List<ProjectExpendEO> projectExpendEOS = new ArrayList<>();
            try {
                projectExpendEOS = projectExpendEOService.queryByList(projectExpendEOPage);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            if (CollectionUtils.isNotEmpty(projectExpendEOS)) {
                final double sum = projectExpendEOS.stream().
                        filter(stateItem->Objects.nonNull(stateItem.getExpendAmount())).collect(Collectors.toList())
                        .stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                fundsListVO.setStateExpend(sum);
            }

            if (StringUtils.isNotBlank(item.getBudgetsum())){
                fundsListVO.setBudgetsum(item.getBudgetsum());
            }
            //计算自筹费用支出
            projectExpendEOPage.setExpendType(null);
            projectExpendEOPage.setExpendTypeNotState(Integer.parseInt(AmountType.SELF_FUND.getValue()));
            List<ProjectExpendEO> projectExpendEOSNotState = new ArrayList<>();
            try {
                projectExpendEOSNotState = projectExpendEOService.queryByList(projectExpendEOPage);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (CollectionUtils.isNotEmpty(projectExpendEOSNotState)) {
                final double sum = projectExpendEOSNotState.stream()
                        .filter(stateItem->Objects.nonNull(stateItem.getExpendAmount())).collect(Collectors.toList()).stream()
                        .mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                fundsListVO.setSelfExpend(sum);
            }
            listVOS.add(fundsListVO);


        });

        return listVOS;
    }

    /***
     * @Description: 生成workBOOK
     * @Param: [exportParams, page]
     * @return: org.apache.poi.ss.usermodel.Workbook
     * @Author: yanyujie
     * @Date: 2020/11/10
     */
    public Workbook exportProjectData(ExportParams exportParams, ProjectDataEOPage page) throws Exception {
        final List<FundsListVO> resutList =new ArrayList<>();
        if (StringUtils.isNotBlank(page.getIds()))
        {
            final String[] split = page.getIds().split(",", -1);
            for (String idStr : split) {
                ProjectDataEOPage idPage=new ProjectDataEOPage();
                idPage.setId(idStr);
                resutList.addAll(this.page(idPage,FundsListType.EXPORT.getValue()));
            }
        }else{
            final List<FundsListVO> fundsListVOList = this.page(page, FundsListType.EXPORT.getValue());
            resutList.addAll(fundsListVOList);
        }
        if (resutList.size() > 0) {

            //解析技术负责人信息
            try {
                resutList.stream().forEach(item -> {
                    final ArrayList technicalList = JSON.parseObject(item.getTechnicalDirector(), ArrayList.class);
                    if (CollectionUtils.isNotEmpty(technicalList)) {
                        technicalList.forEach(i->{
                            Map<String, String> technicalerInfo = (Map<String, String>) i;
                            if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"))) {
                                if (item.getTechnicalDirector().contains("}")){
                                    item.setTechnicalDirector(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                                }else{
                                    item.setTechnicalDirector(item.getTechnicalDirector()==null
                                            ?"":(item.getTechnicalDirector()+",")
                                            +technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                                }
                            }
                        });

                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //课题负责人
            try {
                resutList.stream().forEach(item -> {
                    final ArrayList subjectlList = JSON.parseObject(item.getSubjectDirector(), ArrayList.class);
                    if (CollectionUtils.isNotEmpty(subjectlList)) {
                        subjectlList.forEach(i->{
                            Map<String, String> subjectInfo = (Map<String, String>) i;
                            if (Objects.nonNull(subjectInfo.get("SUBJECT_DIRECTOR_NAME"))) {
                                if (item.getSubjectDirector().contains("}")){
                                    item.setSubjectDirector(subjectInfo.get("SUBJECT_DIRECTOR_NAME"));
                                }else{
                                    item.setSubjectDirector(item.getSubjectDirector()==null
                                            ?"":(item.getSubjectDirector()+",")
                                            +subjectInfo.get("SUBJECT_DIRECTOR_NAME"));
                                }
                            }
                        });

                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return ExcelExportUtil.exportExcel(exportParams, FundsListVO.class, resutList);
    }

}
