package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.base.service.BaseService;
import com.adc.da.finance.dao.BusinessGonfigEODao;
import com.adc.da.finance.dao.ReceivablesManagementEODao;
import com.adc.da.finance.dao.RevenueManagementEODao;
import com.adc.da.finance.dto.BusinessGonfigCBDto;
import com.adc.da.finance.dto.BusinessGonfigDto;
import com.adc.da.finance.dto.BusinessGonfigKYDto;
import com.adc.da.finance.dto.ResearchIssueDTO;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.handler.BusinessGonfigDtoCBHandler;
import com.adc.da.finance.handler.BusinessGonfigDtoHandler;
import com.adc.da.finance.handler.BusinessGonfigKYDtoHandler;
import com.adc.da.finance.page.BusinessGonfigEOPage;
import com.adc.da.finance.page.MyBusinessGonfigEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.DicEODao;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DictionaryEO;
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

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.adc.da.finance.constant.DepartLevelType.*;
import static com.adc.da.finance.constant.DepartLevelType.THIRD_DEPART;


/**
 *
 * <br>
 * <b>功能：</b>F__BUSINESS_GONFIG BusinessGonfigEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("businessGonfigEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BusinessGonfigEOService extends BaseService<BusinessGonfigEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BusinessGonfigEOService.class);

    @Autowired
    private BusinessGonfigEODao dao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private ReceivablesManagementEODao receivablesManagementEODao;
    @Autowired
    private RevenueManagementEODao revenueManagementEODao;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private OrgEODao orgEODao;
    @Autowired
    private DicTypeEODao dicTypeEODao;
    @Autowired
    private DicEODao dicEODao;
    @Autowired
    private ProfitManagementEOService profitManagementEOService;
    @Autowired
    private CashflowManagementEOService cashflowManagementEOService;

    public BusinessGonfigEODao getDao() {
        return dao;
    }


    /**
     * 新增经营业务配置
     * @param eo
     * @return
     * @throws Exception
     */
    public BusinessGonfigEO create(BusinessGonfigEO eo) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (!this.verifyBgNumberIsOnly(eo.getBgNumber())){
            if (eo.getBgType().equals("0")){
                throw new AdcDaBaseException("业务编号已经存在，请检查！");
            }else if (eo.getBgType().equals("1")){
                throw new AdcDaBaseException("课题编号已经存在，请检查！");
            }else{
                throw new AdcDaBaseException("成本编号已经存在，请检查！");
            }

        }
        if(!this.verifyBgNameIsOnly(eo.getBgName())){
            if (eo.getBgType().equals("0")){
                throw new AdcDaBaseException("业务名称已经存在，请检查！");
            }else if (eo.getBgType().equals("1")){
                throw new AdcDaBaseException("课题名称已经存在，请检查！");
            }else{
                throw new AdcDaBaseException("成本名称已经存在，请检查！");
            }
        }
        String businessGonfigId = UUID.randomUUID10();
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
        eo.setId(businessGonfigId);
        int num = dao.insertSelective(eo);
        if (num>0 && eo.getBgStatus().equals("5E8YLRRFEL") && eo.getBgType().equals("0")){
            //同时新增 进行中的数据利润管理数据
            Calendar cal = Calendar.getInstance();
            String year = cal.get(Calendar.YEAR)+"";
            String month = cal.get(Calendar.MONTH )+1+"";
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(businessGonfigId,year,month);
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(businessGonfigId,year,month);
        }
        return eo;
    }

    /**
     * 修改经营业务配置
     * @param eo
     * @return
     * @throws Exception
     */
    public BusinessGonfigEO update(BusinessGonfigEO eo) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (!eo.getBgNumber().equals(eo.getBgNumberOld())){
            if (!this.verifyBgNumberIsOnly(eo.getBgNumber())){
                if (eo.getBgType().equals("0")){
                    throw new AdcDaBaseException("业务编号已经存在，请检查！");
                }else if (eo.getBgType().equals("1")){
                    throw new AdcDaBaseException("课题编号已经存在，请检查！");
                }else{
                    throw new AdcDaBaseException("成本编号已经存在，请检查！");
                }

            }
        }
        if (!eo.getBgName().equals(eo.getBgNameOld())){
            if (!this.verifyBgNameIsOnly(eo.getBgName())){
                if (eo.getBgType().equals("0")){
                    throw new AdcDaBaseException("业务名称已经存在，请检查！");
                }else if (eo.getBgType().equals("1")){
                    throw new AdcDaBaseException("课题名称已经存在，请检查！");
                }else{
                    throw new AdcDaBaseException("成本名称已经存在，请检查！");
                }
            }
        }
        Date date = new Date();
        String userId = user.getUsid();
        String userName = user.getUsname();
        eo.setUpdateTime(date);
        eo.setUpdateUserId(userId);
        eo.setUpdateUserName(userName);
        eo.setDelFlag("0");
        int num = dao.updateByPrimaryKeySelective(eo);
        if (num>0 && eo.getBgStatus().equals("5E8YLRRFEL") && eo.getBgType().equals("0")){
            //同时新增 进行中的数据利润管理数据
            Calendar cal = Calendar.getInstance();
            String year = cal.get(Calendar.YEAR)+"";
            String month = cal.get(Calendar.MONTH )+1+"";
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(eo.getId(),year,month);
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(eo.getId(),year,month);
        }
        return eo;
    }


    /**
     * 分页查询
     * @param page
     * @return
     * @throws Exception
     */
    public List<BusinessGonfigEO> queryByPage(BusinessGonfigEOPage page) throws Exception {
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(page.getDelFlag())){
            page.setDelFlag("0");
        }
        //根据业务名称模糊查询
        if (StringUtils.isNotEmpty(page.getBgName())){
            page.setBgName("%"+page.getBgName()+"%");
            page.setBgNameOperator("LIKE");
        }
        int totalCount = dao.queryByCount(page);
        page.getPager().setRowCount(totalCount);
        //拼接多个排序条件
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ORDER BY dic.dic_type_code,NLSSORT(bg.BG_NAME,'NLS_SORT = SCHINESE_PINYIN_M')");
        page.setSql_filter(stringBuilder.toString());
        return dao.queryByPage(page);
    }


    /**
     * 不分页查询
     * @param page
     * @return
     * @throws Exception
     */
    public List<BusinessGonfigEO> queryByList(BusinessGonfigEOPage page) throws Exception {
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (StringUtils.isEmpty(page.getDelFlag())){
            page.setDelFlag("0");
        }
        //根据业务名称模糊查询
        if (StringUtils.isNotEmpty(page.getBgName())){
            page.setBgName("%"+page.getBgName()+"%");
            page.setBgNameOperator("LIKE");
        }
        //拼接多个排序条件
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ORDER BY dic.dic_type_code,NLSSORT(bg.BG_NAME,'NLS_SORT = SCHINESE_PINYIN_M')");
        page.setSql_filter(stringBuilder.toString());
        return dao.queryByList(page);
    }

    /**
     * 逻辑删除
     * @param ids
     */
    public void logicDelete(List<String> ids) throws Exception{
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        dao.logicDelete(ids);
        //同时逻辑删除应收账款数据
        receivablesManagementEODao.logicDeleteByBusinessGonfigId(ids);
        //同时逻辑删除收入数据
        revenueManagementEODao.logicDeleteByBusinessGonfigId(ids);
    }

    /**
     *
     * 导入
     * @param inputStream
     * @param importParams
     * @return
     * @throws Exception
     */
    public String importBusinessGonfig(InputStream inputStream, ImportParams importParams,String bgType) throws  Exception{

        if (("0").equals(bgType)){
            importParams.setVerifyHandler(new BusinessGonfigDtoHandler());
            ExcelImportResult<BusinessGonfigDto> result =
                    ExcelImportUtil.importExcelMore(inputStream, BusinessGonfigDto.class, importParams);
            List<BusinessGonfigDto> errors = result.getFailList();
            StringBuilder stringBuilder = new StringBuilder();
            if (errors.size()>0){
                for (BusinessGonfigDto dto : errors){
                    if (!dto.getErrorMsg().contains("无效数据")){
                        stringBuilder.append("第"+(dto.getRowNum()+1)+"行"+dto.getErrorMsg().replaceAll(",",""));
                    }
                }
            }
            if (StringUtils.isEmpty(stringBuilder)){
                List<BusinessGonfigDto> datas = result.getList();
                if (datas.size()>0){
                    this.importDatas(datas,bgType);
                }else {
                    throw new AdcDaBaseException("请正确填写导入数据！");
                }
            }
            return  stringBuilder.toString();
        }else if (("1").equals(bgType)){
            importParams.setVerifyHandler(new BusinessGonfigKYDtoHandler());
            ExcelImportResult<BusinessGonfigKYDto> result =
                    ExcelImportUtil.importExcelMore(inputStream, BusinessGonfigKYDto.class, importParams);
            List<BusinessGonfigKYDto> errors = result.getFailList();
            StringBuilder stringBuilder = new StringBuilder();
            if (errors.size()>0){
                for (BusinessGonfigKYDto dto : errors){
                    if (!dto.getErrorMsg().contains("无效数据")){
                        stringBuilder.append("第"+(dto.getRowNum()+1)+"行"+dto.getErrorMsg().replaceAll(",",""));
                    }
                }
            }
            if (StringUtils.isEmpty(stringBuilder)){
                List<BusinessGonfigKYDto> datas = result.getList();
                if (datas.size()>0){
                    this.importKYDatas(datas,bgType);
                }else {
                    throw new AdcDaBaseException("请正确填写导入数据！");
                }

            }
            return  stringBuilder.toString();
        }else if (("2").equals(bgType)){
            importParams.setVerifyHandler(new BusinessGonfigDtoCBHandler());
            ExcelImportResult<BusinessGonfigCBDto> result =
                    ExcelImportUtil.importExcelMore(inputStream, BusinessGonfigCBDto.class, importParams);
            List<BusinessGonfigCBDto> errors = result.getFailList();
            StringBuilder stringBuilder = new StringBuilder();
            if (errors.size()>0){
                for (BusinessGonfigCBDto dto : errors){
                    if (!dto.getErrorMsg().contains("无效数据")){
                        stringBuilder.append("第"+(dto.getRowNum()+1)+"行"+dto.getErrorMsg().replaceAll(",",""));
                    }
                }
            }
            if (StringUtils.isEmpty(stringBuilder)){
                List<BusinessGonfigCBDto> datas = result.getList();
                if (datas.size()>0){
                    this.importCBDatas(datas,bgType);
                }else {
                    throw new AdcDaBaseException("请正确填写导入数据！");
                }
            }
            return  stringBuilder.toString();
        }else {
            throw new AdcDaBaseException("请核实上传业务类型！");
        }
    }


    private void importDatas(List<BusinessGonfigDto> dtoList,String bgType) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (dtoList.size()==0){
            throw new AdcDaBaseException("请填写导入数据！");
        }
        String userId = user.getUsid();
        String userName = user.getUsname();
        Date time = new Date();
        for (BusinessGonfigDto dto : dtoList){
            BusinessGonfigEO eo;
            eo = beanMapper.map(dto,BusinessGonfigEO.class);
            eo.setBgType(bgType);
            //业务名称唯一性验证
            if (!this.verifyBgNameIsOnly(eo.getBgName())){
                if (eo.getBgType().equals("0")){
                    throw new AdcDaBaseException("【"+eo.getBgName()+"】业务名称已经存在，请检查！");
                }else{
                    throw new AdcDaBaseException("【"+eo.getBgName()+"】课题名称已经存在，请检查！");
                }
            }
            //编号唯一性验证
            if (!this.verifyBgNumberIsOnly(eo.getBgNumber())){
                if (eo.getBgType().equals("0")){
                    throw new AdcDaBaseException("【"+eo.getBgNumber()+"】业务编号已经存在，请检查！");
                }else{
                    throw new AdcDaBaseException("【"+eo.getBgNumber()+"】课题编号已经存在，请检查！");
                }
            }
            //获取部门id
            String departName = eo.getDepartName();
            //根据部门名称查询部门
            List<OrgEO> list = orgEODao.getOrgEOByOrgName(departName);
            String departId = "";
            if (list.size()==0){
                throw new AdcDaBaseException("部门不存在，请检查！");
            }else if (list.size()==1){
                //仅可是部门级别
                String departType = list.get(0).getOrgType();
                if (StringUtils.isEmpty(departType)){
                    throw new AdcDaBaseException("部门类型不明确，请去组织机构设置！");
                }else if (departType.equals(FIRST_DEPART)
                        || departType.equals(SECOND_DEPART)
                        || departType.equals(THIRD_DEPART)){
                    departId = list.get(0).getId();
                }else {
                    throw new AdcDaBaseException("仅可是部门级别，请检查！");
                }
            }else {
                //查询出多个
                List<String> orgIds = new ArrayList<>();
                for (OrgEO orgEO : list){
                    String departType = orgEO.getOrgType();
                    if (departType.equals(FIRST_DEPART)
                            || departType.equals(SECOND_DEPART)
                            || departType.equals(THIRD_DEPART)){
                        orgIds.add(orgEO.getId());
                    }
                }
                orgIds.remove(null);
                orgIds.remove("");
                if(orgIds.size()==0){
                    throw new AdcDaBaseException("仅可是部门级别，请检查！");
                }else if (orgIds.size()==1){
                    departId = orgIds.get(0);
                }else {
                    throw new AdcDaBaseException("系统中存在多个【"+departName+"】，请检查！");
                }
            }
            //根据状态查询状态id
            String bgStatusName = eo.getBgStatusName();
            DictionaryEO dictionaryEO = dicEODao.getDictionaryEOByDicCode("bgStatus");
            String dicTypeId= dicTypeEODao.getDicTypeIdByDicIdAndDicTypeName(dictionaryEO.getId(),bgStatusName);
            if (StringUtils.isEmpty(dicTypeId)){
                throw new AdcDaBaseException("课题状态填写有误，请检查！");
            }
            eo.setDepartId(departId);
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag("0");
            eo.setCreateUserId(userId);
            eo.setCreateUserName(userName);
            eo.setCreateTime(time);
            eo.setUpdateUserId(userId);
            eo.setUpdateUserName(userName);
            eo.setUpdateTime(time);
            eo.setBgStatus(dicTypeId);
            dao.insertSelective(eo);
        }

    }

    private void importKYDatas(List<BusinessGonfigKYDto> dtoList,String bgType) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (dtoList.size()==0){
            throw new AdcDaBaseException("请填写导入数据！");
        }
        String userId = user.getUsid();
        String userName = user.getUsname();
        Date time = new Date();
        for (BusinessGonfigKYDto dto : dtoList){
            BusinessGonfigEO eo;
            eo = beanMapper.map(dto,BusinessGonfigEO.class);
            eo.setBgType(bgType);
            //业务名称唯一性验证
            if (!this.verifyBgNameIsOnly(eo.getBgName())){
                throw new AdcDaBaseException("【"+eo.getBgName()+"】业务名称已经存在，请检查！");
            }
            //编号唯一性验证
            if (!this.verifyBgNumberIsOnly(eo.getBgNumber())){
                throw new AdcDaBaseException("【"+eo.getBgNumber()+"】业务编号已经存在，请检查！");
            }
            //获取部门id
            String departName = eo.getDepartName();
            //根据部门名称查询部门===0420这里有瑕疵，需要等底层方法
            List<OrgEO> list = orgEODao.getOrgEOByOrgName(departName);
            String departId = "";
            if (list.size()==0){
                throw new AdcDaBaseException("部门不存在，请检查！");
            }else if (list.size()==1){
                //仅可是部门级别
                String departType = list.get(0).getOrgType();
                if (StringUtils.isEmpty(departType)){
                    throw new AdcDaBaseException("部门类型不明确，请去组织机构设置！");
                }else if (departType.equals(FIRST_DEPART)
                        || departType.equals(SECOND_DEPART)
                        || departType.equals(THIRD_DEPART)){
                    departId = list.get(0).getId();
                }else {
                    throw new AdcDaBaseException("仅可是部门级别，请检查！");
                }
            }else {
                //查询出多个
                List<String> orgIds = new ArrayList<>();
                for (OrgEO orgEO : list){
                    String departType = orgEO.getOrgType();
                    if (departType.equals(FIRST_DEPART)
                            || departType.equals(SECOND_DEPART)
                            || departType.equals(THIRD_DEPART)){
                        orgIds.add(orgEO.getId());
                    }
                }
                orgIds.remove(null);
                orgIds.remove("");
                if(orgIds.size()==0){
                    throw new AdcDaBaseException("仅可是部门级别，请检查！");
                }else if (orgIds.size()==1){
                    departId = orgIds.get(0);
                }else {
                    throw new AdcDaBaseException("系统中存在多个【"+departName+"】，请检查！");
                }
            }
            //根据状态查询状态id
            String bgStatusName = eo.getBgStatusName();
            DictionaryEO dictionaryEO = dicEODao.getDictionaryEOByDicCode("bgStatus");
            String dicTypeId= dicTypeEODao.getDicTypeIdByDicIdAndDicTypeName(dictionaryEO.getId(),bgStatusName);
            if (StringUtils.isEmpty(dicTypeId)){
                throw new AdcDaBaseException("课题状态填写有误，请检查！");
            }
            eo.setDepartId(departId);
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag("0");
            eo.setCreateUserId(userId);
            eo.setCreateUserName(userName);
            eo.setCreateTime(time);
            eo.setUpdateUserId(userId);
            eo.setUpdateUserName(userName);
            eo.setUpdateTime(time);
            eo.setBgStatus(dicTypeId);
            dao.insertSelective(eo);
        }

    }

    private void importCBDatas(List<BusinessGonfigCBDto> dtoList,String bgType) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (dtoList.size()==0){
            throw new AdcDaBaseException("请填写导入数据！");
        }
        String userId = user.getUsid();
        String userName = user.getUsname();
        Date time = new Date();
        for (BusinessGonfigCBDto dto : dtoList){
            BusinessGonfigEO eo;
            eo = beanMapper.map(dto,BusinessGonfigEO.class);
            eo.setBgType(bgType);
            //业务名称唯一性验证
            if (!this.verifyBgNameIsOnly(eo.getBgName())){
                throw new AdcDaBaseException("【"+eo.getBgName()+"】业务名称已经存在，请检查！");
            }
            //编号唯一性验证
            if (!this.verifyBgNumberIsOnly(eo.getBgNumber())){
                throw new AdcDaBaseException("【"+eo.getBgNumber()+"】业务编号已经存在，请检查！");
            }
            //获取部门id
            String departName = eo.getDepartName();
            //根据部门名称查询部门===0420这里有瑕疵，需要等底层方法
            List<OrgEO> list = orgEODao.getOrgEOByOrgName(departName);
            String departId = "";
            if (list.size()==0){
                throw new AdcDaBaseException("部门不存在，请检查！");
            }else if (list.size()==1){
                //仅可是部门级别
                String departType = list.get(0).getOrgType();
                if (StringUtils.isEmpty(departType)){
                    throw new AdcDaBaseException("部门类型不明确，请去组织机构设置！");
                }else if (departType.equals(FIRST_DEPART)
                        || departType.equals(SECOND_DEPART)
                        || departType.equals(THIRD_DEPART)){
                    departId = list.get(0).getId();
                }else {
                    throw new AdcDaBaseException("仅可是部门级别，请检查！");
                }
            }else {
                //查询出多个
                List<String> orgIds = new ArrayList<>();
                for (OrgEO orgEO : list){
                    String departType = orgEO.getOrgType();
                    if (departType.equals(FIRST_DEPART)
                            || departType.equals(SECOND_DEPART)
                            || departType.equals(THIRD_DEPART)){
                        orgIds.add(orgEO.getId());
                    }
                }
                orgIds.remove(null);
                orgIds.remove("");
                if(orgIds.size()==0){
                    throw new AdcDaBaseException("仅可是部门级别，请检查！");
                }else if (orgIds.size()==1){
                    departId = orgIds.get(0);
                }else {
                    throw new AdcDaBaseException("系统中存在多个【"+departName+"】，请检查！");
                }
            }
            //根据状态查询状态id
            String bgStatusName = eo.getBgStatusName();
            DictionaryEO dictionaryEO = dicEODao.getDictionaryEOByDicCode("bgStatus");
            String dicTypeId= dicTypeEODao.getDicTypeIdByDicIdAndDicTypeName(dictionaryEO.getId(),bgStatusName);
            if (StringUtils.isEmpty(dicTypeId)){
                throw new AdcDaBaseException("课题状态填写有误，请检查！");
            }
            eo.setDepartId(departId);
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag("0");
            eo.setCreateUserId(userId);
            eo.setCreateUserName(userName);
            eo.setCreateTime(time);
            eo.setUpdateUserId(userId);
            eo.setUpdateUserName(userName);
            eo.setUpdateTime(time);
            eo.setBgStatus(dicTypeId);
            dao.insertSelective(eo);
        }

    }

    /**
     * 验证业务编号的唯一性
     * @param bgNumber
     * @return
     * @throws Exception
     */
    private Boolean verifyBgNumberIsOnly(String bgNumber) throws Exception{
        if (StringUtils.isEmpty(bgNumber)){
            throw  new AdcDaBaseException("业务编号不能为空，请检查！");
        }
        BusinessGonfigEO businessGonfigEO = dao.getBusinessGonfigEOByBgNumber(bgNumber);
        if (StringUtils.isEmpty(businessGonfigEO)){
            return true;
        }
        return false;
    }

    /**
     * 验证业务名称的唯一性
     * @param bgName
     * @return
     * @throws Exception
     */
    private Boolean verifyBgNameIsOnly(String bgName) throws Exception{
        if (StringUtils.isEmpty(bgName)){
            throw  new AdcDaBaseException("业务名称不能为空，请检查！");
        }
        BusinessGonfigEO businessGonfigEO = dao.getBusinessGonfigEOByBgName(bgName);
        if (StringUtils.isEmpty(businessGonfigEO)){
            return true;
        }
        return false;
    }

    /**
     * 导出经营业务配置数据
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportBusinessGonfig(ExportParams exportParams,BusinessGonfigEOPage eoPage) throws Exception{
        if (StringUtils.isNotEmpty(eoPage.getBgName())){
            String bgName = URLDecoder.decode(eoPage.getBgName(),"utf-8");
            eoPage.setBgName(bgName);
        }
        List<BusinessGonfigEO> list = this.queryByList(eoPage);
        if (list.size()>0 && eoPage.getBgType().equals("0")){
            List<BusinessGonfigDto> resultList = beanMapper.mapList(list,BusinessGonfigDto.class);
            exportParams.setSheetName("经营业务信息");
            return ExcelExportUtil.exportExcel(exportParams,BusinessGonfigDto.class,resultList);
        }else if (list.size()>0 && eoPage.getBgType().equals("1")){
            List<BusinessGonfigKYDto> resultList = beanMapper.mapList(list,BusinessGonfigKYDto.class);
            exportParams.setSheetName("科研课题信息");
            return ExcelExportUtil.exportExcel(exportParams,BusinessGonfigKYDto.class,resultList);
        }else if (list.size()>0 && eoPage.getBgType().equals("2")){
            List<BusinessGonfigCBDto> resultList = beanMapper.mapList(list, BusinessGonfigCBDto.class);
            exportParams.setSheetName("成本信息");
            return ExcelExportUtil.exportExcel(exportParams,BusinessGonfigCBDto.class,resultList);
        }else {
            throw new AdcDaBaseException("导出错误，请联系开发人员！");
        }

    }

    public List<BusinessGonfigEO> getBusinessListByLoginUser(String userId) {
        return dao.getBusinessListByLoginUser(userId);
    }

    public List<BusinessGonfigEO> getBusinessPage(MyBusinessGonfigEOPage page) {
        Integer count = dao.getBusinessPageCount(page);
        page.getPager().setRowCount(count);
        return dao.getBusinessPage(page);
    }




}
