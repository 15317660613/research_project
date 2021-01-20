package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.finance.dao.BusinessGonfigEODao;
import com.adc.da.finance.dao.ProfitManagementEODao;
import com.adc.da.finance.dto.RevenueManagementDto;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.entity.ProfitManagementEO;
import com.adc.da.finance.entity.ResearchIssueEO;
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
import com.adc.da.finance.dao.RevenueManagementEODao;
import com.adc.da.finance.entity.RevenueManagementEO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static com.adc.da.finance.constant.DepartLevelType.*;


/**
 *
 * <br>
 * <b>功能：</b>F__REVENUE_MANAGEMENT RevenueManagementEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("revenueManagementEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RevenueManagementEOService extends BaseService<RevenueManagementEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(RevenueManagementEOService.class);

    @Autowired
    private RevenueManagementEODao dao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private OrgEODao orgEODao;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private BusinessGonfigEODao businessGonfigEODao;
    @Autowired
    private ProfitManagementEOService profitManagementEOService;

    public RevenueManagementEODao getDao() {
        return dao;
    }

    /**
     * 新增 收入数据管理 同时更新利润管理中的收入金额、利润金额和利润率
     * @param eo
     * @return
     * @throws Exception
     */
    public RevenueManagementEO create (RevenueManagementEO eo) throws Exception{
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
        int num = dao.insertSelective(eo);
        if (num>0){
            //同时更新利润管理中的收入金额
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRmYear(),eo.getRmMonth());
        }
        return eo;
    }

    /**
     * 修改 收入数据管理
     * @param eo
     * @return
     * @throws Exception
     */
    public RevenueManagementEO update (RevenueManagementEO eo) throws Exception{
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
        int num = dao.updateByPrimaryKey(eo);
        if (num>0){
            //同时更新利润管理中的收入金额
            if (eo.getBusinessGonfigId().equals(eo.getBusinessGonfigIdOld())
                    && eo.getRmYear().equals(eo.getRmYearOld())
                    && eo.getRmMonth().equals(eo.getRmMonthOld())){}else{
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRmYear(),eo.getRmMonth());
            }
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigIdOld(),eo.getRmYearOld(),eo.getRmMonthOld());
        }
        return eo;
    }

    /**
     * 分页查询应收款数据
     * @param page
     * @return
     * @throws Exception
     */
    public List<RevenueManagementEO> queryByPage(RevenueManagementEOPage page) throws Exception {
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //默认查询正常的
        if (StringUtils.isEmpty(page.getDelFlag())){
            page.setDelFlag("0");
        }
        //部门模糊查询
        if (StringUtils.isNotEmpty(page.getDepartName())){
            page.setDepartName("%"+page.getDepartName()+"%");
            page.setDepartNameOperator("LIKE");
        }
        int totalCount = dao.queryByCount(page);
        page.getPager().setRowCount(totalCount);
        //排序-多个字段
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ORDER BY ");
        stringBuilder.append("ABS(RM.RM_YEAR) DESC,");
        stringBuilder.append("ABS(RM.RM_MONTH) DESC,");
        stringBuilder.append("NLSSORT(TS_ORG.ORG_NAME,'NLS_SORT = SCHINESE_PINYIN_M') ASC");
        //stringBuilder.append("BG.BG_NAME ASC");
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

        //同时更新利润管理
        for (String id: ids){
            RevenueManagementEOPage eoPage = new RevenueManagementEOPage();
            eoPage.setDelFlag("1");
            eoPage.setId(id);
            List<RevenueManagementEO> list = dao.queryByList(eoPage);
            if (list.size()==1){
                RevenueManagementEO eo = list.get(0);
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(eo.getBusinessGonfigId(),eo.getRmYear(),eo.getRmMonth());
            }else{
                throw new AdcDaBaseException("系统错误，请联系开发人员！");
            }
        }

    }

    /**
     * 导入收入数据
     * @param inputStream
     * @param importParams
     * @return
     * @throws Exception
     */
    public String importRevenueManagement (InputStream inputStream, ImportParams importParams) throws Exception{
        ExcelImportResult<RevenueManagementDto> result =
                ExcelImportUtil.importExcelMore(inputStream, RevenueManagementDto.class, importParams);
        List<RevenueManagementDto> errors = result.getFailList();
        StringBuilder stringBuilder = new StringBuilder();
        if (errors.size()>0){
            for (RevenueManagementDto dto : errors){
                if (!dto.getErrorMsg().contains("无效数据")){
                    stringBuilder.append("第"+(dto.getRowNum()+1)+"行"+dto.getErrorMsg().replaceAll(",",""));
                }
            }
        }
        if (StringUtils.isEmpty(stringBuilder)){
            List<RevenueManagementDto> datas = result.getList();
            if (datas.size()>0){
                this.importDatas(datas);
            }else {
                throw new AdcDaBaseException("请正确填写导入数据！");
            }

        }
        return stringBuilder.toString();
    }

    private void importDatas(List<RevenueManagementDto> datas) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (datas.size()>0){
            String userId = user.getUsid();
            String userName = user.getUsname();
            Date time = new Date();
            Set<String> importBusinessIds = new HashSet<>();
            for (RevenueManagementDto dto : datas){
                RevenueManagementEO eo;
                eo = beanMapper.map(dto,RevenueManagementEO.class);
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
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(businessGonfigEO.getId(),eo.getRmYear(),eo.getRmMonth());
            }
        }
    }

    /**
     * 导出收入数据
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportRevenueManagement(ExportParams exportParams,RevenueManagementEOPage eoPage) throws Exception{
        eoPage.setPageSize(15000);
        List<RevenueManagementEO> list = this.queryByPage(eoPage);
        List<RevenueManagementDto> resutList = new ArrayList<>();
        if (list.size()>0){
            resutList = beanMapper.mapList(list, RevenueManagementDto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams,RevenueManagementDto.class,resutList);
    }

}
