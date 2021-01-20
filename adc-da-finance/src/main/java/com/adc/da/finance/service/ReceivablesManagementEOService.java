package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.base.page.BasePage;
import com.adc.da.finance.dao.BusinessGonfigEODao;
import com.adc.da.finance.dao.CashflowManagementEODao;
import com.adc.da.finance.dto.ReceivablesManagementDto;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.entity.CashflowManagementEO;
import com.adc.da.finance.page.ReceivablesManagementEOPage;
import com.adc.da.finance.page.RevenueManagementEOPage;
import com.adc.da.finance.util.DoubleUtils;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.finance.dao.ReceivablesManagementEODao;
import com.adc.da.finance.entity.ReceivablesManagementEO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.adc.da.finance.constant.DepartLevelType.*;
import static com.adc.da.finance.constant.DepartLevelType.THIRD_DEPART;


/**
 *
 * <br>
 * <b>功能：</b>F__RECEIVABLES_MANAGEMENT ReceivablesManagementEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("receivablesManagementEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ReceivablesManagementEOService extends BaseService<ReceivablesManagementEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ReceivablesManagementEOService.class);

    @Autowired
    private ReceivablesManagementEODao dao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private OrgEODao orgEODao;
    @Autowired
    private BusinessGonfigEODao businessGonfigEODao;
    @Autowired
    private  CashflowManagementEOService cashflowManagementEOService;
    public ReceivablesManagementEODao getDao() {
        return dao;
    }

    /**
     * 新增 应收账款数据
     * @param eo
     * @return
     * @throws Exception
     */
    public ReceivablesManagementEO create (ReceivablesManagementEO eo) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Date date = new Date();
        String userId = user.getUsid();
        String userName = user.getUsname();
        eo.setCreateTime(date);
        eo.setCreateUserId(userId);
        eo.setCreateUserName(userName);
        eo.setUpdateTime(date);
        eo.setUpdateUserId(userId);
        eo.setUpdateUserName(userName);
        eo.setDelFlag("0");
        eo.setId(UUID.randomUUID10());
        BigDecimal remMoney = eo.getRemMoney();
        if (!remMoney.equals(BigDecimal.ZERO)){
            remMoney = DoubleUtils.getScale2BybigDecimal(remMoney);
            eo.setRemMoney(remMoney);
        }
        int num = dao.insertSelective(eo);
        if (num>0){
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRemYear(),eo.getRemMonth());
        }
        return eo;
    }

    /**
     * 修改 应收账款数据
     * @param eo
     * @return
     * @throws Exception
     */
    public ReceivablesManagementEO update (ReceivablesManagementEO eo) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Date date = new Date();
        String userId = user.getUsid();
        String userName = user.getUsname();
        eo.setUpdateTime(date);
        eo.setUpdateUserId(userId);
        eo.setUpdateUserName(userName);
        eo.setDelFlag("0");
        BigDecimal remMoney = eo.getRemMoney();
        if (!remMoney.equals(BigDecimal.ZERO)){
            remMoney = DoubleUtils.getScale2BybigDecimal(remMoney);
            eo.setRemMoney(remMoney);
        }
        int num = dao.updateByPrimaryKeySelective(eo);
        if (num>0){
            if (eo.getBusinessGonfigId().equals(eo.getBusinessGonfigIdOld())
                    && eo.getRemYear().equals(eo.getRemYearOld())
                    && eo.getRemMonth().equals(eo.getRemMonthOld())){}
            else{
                cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRemYear(),eo.getRemMonth());
            }
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigIdOld(),eo.getRemYearOld(),eo.getRemMonthOld());
        }
        return eo;
    }

    /**
     * 分页查询应收款数据
     * @param page
     * @return
     * @throws Exception
     */
    public List<ReceivablesManagementEO> queryByPage(ReceivablesManagementEOPage page) throws Exception {
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //默认查询正常的数据
        if (StringUtils.isEmpty(page.getDelFlag())){
            page.setDelFlag("0");
        }
        //根据企业名称模糊查询
        if (StringUtils.isNotEmpty(page.getBusinessGonfigName())){
            page.setBusinessGonfigName("%"+page.getBusinessGonfigName()+"%");
            page.setBusinessGonfigNameOperator("LIKE");
        }
        //根据部门名称模糊查询
        if (StringUtils.isNotEmpty(page.getDepartName())){
            page.setDepartName("%"+page.getDepartName()+"%");
            page.setDepartNameOperator("LIKE");
        }
        int totalCount = dao.queryByCount(page);
        page.getPager().setRowCount(totalCount);
        //排序-多个字段
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ORDER BY ");
        stringBuilder.append("ABS(REM.REM_YEAR) DESC,");
        stringBuilder.append("ABS(REM.REM_MONTH) DESC,");
        stringBuilder.append("NLSSORT(TS_ORG.ORG_NAME,'NLS_SORT = SCHINESE_PINYIN_M') ASC,");
        stringBuilder.append("NLSSORT(BG.BG_NAME,'NLS_SORT = SCHINESE_PINYIN_M') ASC");
        page.setSql_filter(stringBuilder.toString());
        return dao.queryByPage(page);
    }

    /**
     * 逻辑删除数据
     * @param ids
     * @throws Exception
     */
    public void logicDelete(List<String> ids) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        ids.remove(null);
        ids.remove("");
        dao.logicDeleteIds(ids);
        //同时更新现金流量管理
        for (String id : ids){
            ReceivablesManagementEOPage eoPage = new ReceivablesManagementEOPage();
            eoPage.setId(id);
            eoPage.setDelFlag("1");
            List<ReceivablesManagementEO> list = dao.queryByList(eoPage);
            if (list.size()==1){
                ReceivablesManagementEO eo = list.get(0);
                cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRemYear(),eo.getRemMonth());
            }else{
                throw new AdcDaBaseException("系统错误，请联系开发人员！");
            }
        }
    }

    /**
     * 导入应收账款数据管理
     * @param inputStream
     * @param importParams
     * @return
     * @throws Exception
     */
    public String importReceivablesManagement(
            InputStream inputStream, ImportParams importParams) throws Exception{
        ExcelImportResult<ReceivablesManagementDto> result =
                ExcelImportUtil.importExcelMore(inputStream, ReceivablesManagementDto.class, importParams);
        List<ReceivablesManagementDto> errors = result.getFailList();
        StringBuilder stringBuilder = new StringBuilder();
        if (errors.size()>0){
            for (ReceivablesManagementDto dto : errors){
                if (!dto.getErrorMsg().contains("无效数据")){
                    stringBuilder.append("第"+(dto.getRowNum()+1)+"行"+dto.getErrorMsg().replaceAll(",",""));
                }
            }
        }
        if (StringUtils.isEmpty(stringBuilder)){
            List<ReceivablesManagementDto> datas = result.getList();
            if (datas.size()>0){
                this.importDatas(datas);
            }else {
                throw new AdcDaBaseException("请正确填写导入数据！");
            }
        }
        return stringBuilder.toString();

    }

    private void importDatas (List<ReceivablesManagementDto> receivablesManagementDtoList) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (receivablesManagementDtoList.size()>0){
            String userId = user.getUsid();
            String userName = user.getUsname();
            Date time = new Date();
            List<String> importIds = new ArrayList<>();
            for (ReceivablesManagementDto dto : receivablesManagementDtoList){
                ReceivablesManagementEO eo;
                eo = beanMapper.map(dto,ReceivablesManagementEO.class);
                String id = UUID.randomUUID10();
                //根据部门名称查询部门id
                String departName = eo.getDepartName();
                List<OrgEO> list = orgEODao.getOrgEOByOrgName(departName);
                String departId = "";
                if (list.size()==0){
                    throw new AdcDaBaseException("部门不存在，请检查！");
                }else if (list.size()==1){
                    //部门类型在部门以上
                    String departType = list.get(0).getOrgType();
                    if (StringUtils.isEmpty(departType)){
                        throw new AdcDaBaseException("部门类型不明确，请去组织机构设置！");
                    }else if (departType.equals(BEN_DEPART) || departType.equals(FIRST_DEPART)
                            || departType.equals(SECOND_DEPART)
                            || departType.equals(THIRD_DEPART)){
                        departId = list.get(0).getId();
                    }else {
                        throw new AdcDaBaseException("仅可是部门及以上级别，请检查！");
                    }
                }else {
                    //查询出多个
                    List<String> orgIds = new ArrayList<>();
                    for (OrgEO orgEO : list){
                        String departType = orgEO.getOrgType();
                        if (departType.equals(BEN_DEPART) || departType.equals(FIRST_DEPART)
                                || departType.equals(SECOND_DEPART)
                                || departType.equals(THIRD_DEPART)){
                            orgIds.add(orgEO.getId());
                        }
                    }
                    orgIds.remove(null);
                    orgIds.remove("");
                    if(orgIds.size()==0){
                        throw new AdcDaBaseException("仅可是部门及以上级别，请检查！");
                    }else if (orgIds.size()==1){
                        departId = orgIds.get(0);
                    }else {
                        throw new AdcDaBaseException("系统中存在多个【"+departName+"】，请检查！");
                    }
                }
                eo.setDepartId(departId);
                //根据所属业务名称查询id并且状态是进行中的
                BusinessGonfigEO businessGonfigEO =
                        businessGonfigEODao.getBusinessGonfigEOByBgNameAndBgType(eo.getBusinessGonfigName(),"0");
                if(StringUtils.isEmpty(businessGonfigEO)){
                    throw new AdcDaBaseException("所属业务名称不存在，请检查！");
                }else{
                    if (!businessGonfigEO.getBgStatus().equals("5E8YLRRFEL")){
                        throw new AdcDaBaseException("所属业务状态不是进行中，请检查！");
                    }
                }
                eo.setBusinessGonfigId(businessGonfigEO.getId());
                eo.setId(id);
                eo.setDelFlag("0");
                eo.setCreateUserId(userId);
                eo.setCreateUserName(userName);
                eo.setCreateTime(time);
                eo.setUpdateUserId(userId);
                eo.setUpdateUserName(userName);
                eo.setUpdateTime(time);
                dao.insertSelective(eo);
                importIds.add(id);
            }
            if (importIds.size()>0){
                List<ReceivablesManagementEO> list = dao.queryByGroup(importIds);
                if (list.size()>0){
                    for (ReceivablesManagementEO eo : list){
                        cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRemYear(),eo.getRemMonth());
                    }
                }
            }
        }
    }

    /**
     * 导出应收账款数据
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportReceivablesManagement(ExportParams exportParams,ReceivablesManagementEOPage eoPage) throws Exception{
        eoPage.setPageSize(15000);
        List<ReceivablesManagementEO> list = this.queryByPage(eoPage);
        List<ReceivablesManagementDto> resultList = new ArrayList<>();
        if(list.size()>0){
            resultList = beanMapper.mapList(list,ReceivablesManagementDto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams,ReceivablesManagementDto.class,resultList);
    }

}
