package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.date.DateTime;
import com.adc.da.base.service.BaseService;
import com.adc.da.finance.dao.BusinessGonfigEODao;
import com.adc.da.finance.dao.SalaryManagementEODao;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.entity.SalaryManagementEO;
import com.adc.da.finance.handler.SalaryManagementHandler;
import com.adc.da.finance.page.BusinessGonfigEOPage;
import com.adc.da.finance.page.SalaryManagementEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>F_SALARY_MANAGEMENT SalaryManagementEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("salaryManagementEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SalaryManagementEOService extends BaseService<SalaryManagementEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(SalaryManagementEOService.class);

    @Autowired
    private SalaryManagementEODao dao;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private BusinessGonfigEODao businessGonfigEODao;

    @Autowired
    private ProfitManagementEOService profitManagementEOService;

    @Autowired
    private CashflowManagementEOService cashflowManagementEOService;


    public SalaryManagementEODao getDao() {
        return dao;
    }

    public int insertList(List<SalaryManagementEO> list) {
        return dao.insertList(list);
    }

    public void logicDeleteByPrimaryKey(List<String> idList) throws Exception {
        List<SalaryManagementEO> salaryManagementEOList = dao.selectByPrimaryKeys(idList);
        dao.logicDeleteByPrimaryKeys(idList);
        for (SalaryManagementEO salaryManagementEO : salaryManagementEOList) {
            BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(salaryManagementEO.getBusinessId());
            if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                        salaryManagementEO.getBusinessId(),
                        String.valueOf(salaryManagementEO.getYear()),
                        String.valueOf(salaryManagementEO.getMonth()));
                cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                        salaryManagementEO.getBusinessId(),
                        String.valueOf(salaryManagementEO.getYear()),
                        String.valueOf(salaryManagementEO.getMonth()));
            }
        }
    }

    public SalaryManagementEO myCreate(SalaryManagementEO salaryManagementEO) throws Exception {
        dao.insertSelective(salaryManagementEO);
        BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(salaryManagementEO.getBusinessId());
        if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                    salaryManagementEO.getBusinessId(),
                    String.valueOf(salaryManagementEO.getYear()),
                    String.valueOf(salaryManagementEO.getMonth()));
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                    salaryManagementEO.getBusinessId(),
                    String.valueOf(salaryManagementEO.getYear()),
                    String.valueOf(salaryManagementEO.getMonth()));
        }
        return salaryManagementEO;
    }

    /**
     * 导入
     */

    public void excelImport(InputStream is) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        List<BusinessGonfigEO> businessGonfigEOList = businessGonfigEODao.queryByList(new BusinessGonfigEOPage());

        Map<String, String> businessNameIdMap = getBusinessNameIdMap(businessGonfigEOList);
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerfiy(true);
        importParams.setVerifyHandler(new SalaryManagementHandler());
        ExcelImportResult<SalaryManagementEO> result = ExcelImportUtil.importExcelMore(is, SalaryManagementEO.class, importParams);

        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            List<Integer> rowindexs = new ArrayList<>();
            logger.error("excel校验不通过！");
            List<SalaryManagementEO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (SalaryManagementEO error : errors) {
                rowindexs.add(error.getRowNum() + 1);
                //sb.append("第 " + error.getRowNum() + "、");
            }
            sb.append("第 ");
            sb.append(StringUtils.join(rowindexs, '、'));
            sb.append(" 行数据不合法，请检查！");
            throw new AdcDaBaseException(sb.toString());
        }

        List<SalaryManagementEO> salaryManagementEOList = result.getList();

        for (SalaryManagementEO salaryManagementEO : salaryManagementEOList) {
            salaryManagementEO.setId(UUID.randomUUID10());
            String orgId = orgNameIdMap.get(salaryManagementEO.getOrgName());
            if (null == orgId) {
                throw new AdcDaBaseException("组织机构 " + salaryManagementEO.getOrgName() + " 不存在,请检查！");
            }
            String businessId = businessNameIdMap.get(salaryManagementEO.getBusinessName());
            if (null == businessId) {
                throw new AdcDaBaseException("业务名称 " + salaryManagementEO.getBusinessName() + " 不存在,请在 经营/科研业务配置 功能检查或配置！");
            }
            salaryManagementEO.setBusinessId(businessId);
            salaryManagementEO.setOrgId(orgId);
            salaryManagementEO.setUpdateUserId(userEO.getUsid());
            salaryManagementEO.setUpdateUserName(userEO.getUsname());
            salaryManagementEO.setUpdateTime(new DateTime());
            if (StringUtils.isNotEmpty(salaryManagementEO.getOrgName())) {
                salaryManagementEO.setOrgInitial(salaryManagementEO.getFirstSpell());
            }
        }
        dao.insertList(salaryManagementEOList);
        for (SalaryManagementEO salaryManagementEO : salaryManagementEOList) {
            BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(salaryManagementEO.getBusinessId());
            if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                        salaryManagementEO.getBusinessId(),
                        String.valueOf(salaryManagementEO.getYear()),
                        String.valueOf(salaryManagementEO.getMonth()));
                cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                        salaryManagementEO.getBusinessId(),
                        String.valueOf(salaryManagementEO.getYear()),
                        String.valueOf(salaryManagementEO.getMonth()));
            }
        }
    }

    public SalaryManagementEO myUpdateByPrimaryKeySelective(SalaryManagementEO eo) throws Exception {
        if (StringUtils.isNotEmpty(eo.getOrgName())) {
            eo.setOrgInitial(eo.getFirstSpell());
        }
        SalaryManagementEO oldSalaryManagementEO = dao.selectByPrimaryKey(eo.getId());
        this.updateByPrimaryKeySelective(eo);
        SalaryManagementEO newSalaryManagementEO = dao.selectByPrimaryKey(eo.getId());
        if (null == newSalaryManagementEO) {
            throw new AdcDaBaseException("更新不存在！");
        }

        //如果年月或者业务有更新
        if (compareChange(oldSalaryManagementEO, newSalaryManagementEO)) {
            BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(oldSalaryManagementEO.getBusinessId());
            if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                        oldSalaryManagementEO.getBusinessId(),
                        String.valueOf(oldSalaryManagementEO.getYear()),
                        String.valueOf(oldSalaryManagementEO.getMonth()));
                cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                        oldSalaryManagementEO.getBusinessId(),
                        String.valueOf(oldSalaryManagementEO.getYear()),
                        String.valueOf(oldSalaryManagementEO.getMonth()));
            }
        }
        BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(newSalaryManagementEO.getBusinessId());
        if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                    newSalaryManagementEO.getBusinessId(),
                    String.valueOf(newSalaryManagementEO.getYear()),
                    String.valueOf(newSalaryManagementEO.getMonth()));
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                    newSalaryManagementEO.getBusinessId(),
                    String.valueOf(newSalaryManagementEO.getYear()),
                    String.valueOf(newSalaryManagementEO.getMonth()));
        }

        return newSalaryManagementEO;
    }

    public boolean compareChange(SalaryManagementEO oldSalaryManagementEO, SalaryManagementEO newSalaryManagementEO) {

        if (!StringUtils.equals(oldSalaryManagementEO.getBusinessId(), newSalaryManagementEO.getBusinessId())
                || oldSalaryManagementEO.getYear() != newSalaryManagementEO.getYear()
                || oldSalaryManagementEO.getMonth() != newSalaryManagementEO.getMonth()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 导出
     *
     * @param salaryManagementEOPage
     * @return
     */
    public Workbook excelExport(SalaryManagementEOPage salaryManagementEOPage) throws Exception {
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        List<SalaryManagementEO> salaryManagementEOList = queryByList(salaryManagementEOPage);
        return ExcelExportUtil.exportExcel(params, SalaryManagementEO.class, salaryManagementEOList);
    }

    public Map<String, String> getOrgNameIdMap(List<OrgEO> orgEOList) {
        Map<String, String> nameIdMap = new HashMap<>();
        for (OrgEO orgEO : orgEOList) {
            nameIdMap.put(orgEO.getName(), orgEO.getId());
        }
        return nameIdMap;
    }

    public Map<String, String> getBusinessNameIdMap(List<BusinessGonfigEO> businessGonfigEOList) {
        Map<String, String> businessNameIdMap = new HashMap<>();
        for (BusinessGonfigEO businessGonfigEO : businessGonfigEOList) {
            businessNameIdMap.put(businessGonfigEO.getBgName(), businessGonfigEO.getId());
        }
        return businessNameIdMap;
    }

}
