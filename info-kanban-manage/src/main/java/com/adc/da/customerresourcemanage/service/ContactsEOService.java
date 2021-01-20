package com.adc.da.customerresourcemanage.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.budget.vo.DashBoardVO;
import com.adc.da.crm.page.ContractBaseEOPage;
import com.adc.da.customerresourcemanage.dao.EnterpriseEODao;
import com.adc.da.customerresourcemanage.dto.ContactsDto;
import com.adc.da.customerresourcemanage.entity.EnterpriseEO;
import com.adc.da.customerresourcemanage.page.ContactsEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.customerresourcemanage.dao.ContactsEODao;
import com.adc.da.customerresourcemanage.entity.ContactsEO;

import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static cn.afterturn.easypoi.excel.ExcelImportUtil.importExcelMore;


/**
 *
 * <br>
 * <b>功能：</b>DB_CONTACTS ContactsEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("contactsEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ContactsEOService extends BaseService<ContactsEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ContactsEOService.class);
    //private static final String userId = "GHVRTMA9H2";

    @Autowired
    private ContactsEODao dao;
    @Autowired
    BeanMapper beanMapper;
    @Autowired
    UserEODao userEODao;
    @Autowired
    EnterpriseEODao enterpriseEODao;
    @Autowired
    DicTypeEOService dicTypeEOService;
    @Autowired
    private ProjectService projectService;


    public ContactsEODao getDao() {
        return dao;
    }

    /**
     *
     * 分页根据企业名称和联系人姓名查询
     * @param contactsEOPage
     * @return
     * @throws Exception
     */
    public List<ContactsEO> queryByPage(ContactsEOPage contactsEOPage) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //企业名称模糊查询
        if (StringUtils.isNotEmpty(contactsEOPage.getEnterpriseName())){
            contactsEOPage.setEnterpriseNameOperator("LIKE");
            contactsEOPage.setEnterpriseName("%"+contactsEOPage.getEnterpriseName()+"%");
        }
        //联系人姓名模糊查询
        if (StringUtils.isNotEmpty(contactsEOPage.getContactsUsname())){
            contactsEOPage.setContactsUsnameOperator("LIKE");
            contactsEOPage.setContactsUsname("%"+contactsEOPage.getContactsUsname()+"%");
        }
        int num = dao.queryByCountVO(contactsEOPage);
        contactsEOPage.getPager().setRowCount(num);
//        contactsEOPage.setOrderBy("c.update_time");
//        contactsEOPage.setOrder("desc");
        if (StringUtils.isEmpty(contactsEOPage.getSql_filter())) {
            contactsEOPage.setSql_filter(" order by c.update_time desc, c.id");
        }
        return dao.queryByPageVO(contactsEOPage);
    }

    /**
     * 新增联系人信息
     * @param contactsEO
     * @return
     * @throws Exception
     */
    public ContactsEO create(ContactsEO contactsEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if(StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        contactsEO.setId(UUID.randomUUID10());
        //contactsEO.setCreateTime(new Date());
        //contactsEO.setCreateUserId(user.getUsid());
        //contactsEO.setCreateUserName(user.getUsname());
        contactsEO.setUpdateTime(new java.util.Date());
        contactsEO.setUpdateUserId(user.getUsid());
        contactsEO.setUpdateUserName(user.getUsname());
        contactsEO.setDelFlag("0");
        dao.insertSelective(contactsEO);
        return contactsEO;
    }

    /**
     * 修改联系人信息
     * @param contactsEO
     * @return
     * @throws Exception
     */
    public ContactsEO update(ContactsEO contactsEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if(StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        /*contactsEO.setCreateTime(new Date());
        contactsEO.setCreateUserId(user.getUsid());
        contactsEO.setCreateUserName(user.getUsname());*/
        contactsEO.setUpdateTime(new java.util.Date());
        contactsEO.setUpdateUserId(user.getUsid());
        contactsEO.setUpdateUserName(user.getUsname());
        contactsEO.setDelFlag("0");
        dao.updateByPrimaryKey(contactsEO);
        return contactsEO;
    }

    /**
     * 逻辑删除联系人信息
     * @param id
     * @return
     * @throws Exception
     */
    public void logicDeleteByPrimaryKey(String id) throws Exception{
         int num = dao.logicDeleteByPrimaryKey(id);
         if (num>0){}else{
             throw new AdcDaBaseException("删除失败，请检查！");
         }
    }

    /**
     * @Description 批量逻辑删除企业以及关联企业的用户信息
     * @param ids
     * @throws Exception
     */
    public void batchLogicDeleteByPrimaryKeys(List<String> ids) throws Exception{
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        int num = dao.batchLogicDeleteByPrimaryKeys(ids);
        if (num>0){}else{
            throw new AdcDaBaseException("删除失败，请检查！");
        }

    }

    public List<ContactsDto> excelImportContacts(
            InputStream inputStream, ImportParams importParams) throws Exception{
        ExcelImportResult<ContactsDto> result = ExcelImportUtil.importExcelMore(inputStream, ContactsDto.class, importParams);
        List<ContactsDto> datas = result.getList();
        List<ContactsDto> errors = result.getFailList();
        importDatas(datas);
        return  errors;
    }

    public void importDatas(List<ContactsDto> datas) throws Exception{
        UserEO user = UserUtils.getUser();
        /*UserEO user = userEODao.selectByPrimaryKey(userId);*/
        if(StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (CollectionUtils.isEmpty(datas)){
            throw new AdcDaBaseException("文件内容为空，请检查！");
        }
        for (ContactsDto dto : datas){
            ContactsEO eo;
            eo = beanMapper.map(dto,ContactsEO.class);
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag("0");
            eo.setUpdateTime(new java.util.Date());
            eo.setUpdateUserId(user.getUsid());
            eo.setUpdateUserName(user.getUsname());
            //验证企业名称是否存在
            String enterpriseName = eo.getEnterpriseName();
            if (StringUtils.isEmpty(enterpriseName)){
                throw new AdcDaBaseException("企业名称不能为空，请检查！");
            }else {
                String enterpriseId = this.verifyEnterpriseByName(enterpriseName);
                eo.setEnterpriseId(enterpriseId);
            }
            if (StringUtils.isEmpty(eo.getContactsUsname())){
                throw new AdcDaBaseException("姓名不能为空，请检查！");
            }
            //验证影响力力
            String effectName = eo.getEffectName();
            if(StringUtils.isEmpty(effectName)){
                throw new AdcDaBaseException("影响力不能为空，请检查！");
            }else{
                String effectDicTypeCode = "";
                switch (effectName) {
                    case "高层" :
                        effectDicTypeCode = "effect_high_00";
                        break;
                    case "中层" :
                        effectDicTypeCode = "effect_middle_01";
                        break;
                    case "骨干" :
                        effectDicTypeCode = "effect_backbone_03";
                        break;
                    default: break;
                }
                if (StringUtils.isEmpty(effectDicTypeCode)){
                    throw new AdcDaBaseException("影响力填写有误，请检查！");
                }else {
                    DicTypeEO effectData = dicTypeEOService.getDicTypeEOByCode(effectDicTypeCode);
                    if (StringUtils.isEmpty(effectData)){
                        throw new AdcDaBaseException("影响力查询有误，请联系开发人员！");
                    }else{
                        eo.setEffectId(effectData.getId());
                    }
                }
            }
            //验证亲密度
            String intimacyName = eo.getIntimacyName();
            if(StringUtils.isNotEmpty(intimacyName)){

                String intimacyDicTypeCode = "";
                switch (intimacyName) {
                    case "密" :
                        intimacyDicTypeCode = "intimacy_dense_00";
                        break;
                    case "中" :
                        intimacyDicTypeCode = "intimacy_middle_01";
                        break;
                    case "疏" :
                        intimacyDicTypeCode = "intimacy_sparse_02";
                        break;
                    default: break;
                }
                if (StringUtils.isEmpty(intimacyDicTypeCode)){
                    throw new AdcDaBaseException("亲密度填写有误，请检查！");
                }else {
                    DicTypeEO intimacyData = dicTypeEOService.getDicTypeEOByCode(intimacyDicTypeCode);
                    if (StringUtils.isEmpty(intimacyData)){
                        throw new AdcDaBaseException("亲密度查询有误，请联系开发人员！");
                    }else{
                        eo.setIntimacyId(intimacyData.getId());
                    }

                }
            }
            dao.insertSelective(eo);
        }
    }

    /**
     * 根据企业名称验证企业时候存在
     * @param enterpriseName
     * @return
     * @throws Exception
     */
    private String verifyEnterpriseByName(String enterpriseName) throws Exception{
        EnterpriseEO enterpriseEo = enterpriseEODao.findEnterpriseByName(enterpriseName);
        if (StringUtils.isEmpty(enterpriseEo)){
            throw new AdcDaBaseException("【"+enterpriseName+"】不存在，请去企业页面添加或者请检查！");
        }else{
            return enterpriseEo.getId();
        }
    }

    /**
     * 导出联系人
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportContacts(ExportParams exportParams) throws Exception{
        ContactsEOPage contactsEOPage = new ContactsEOPage();
        contactsEOPage.setOrder("desc");
        contactsEOPage.setOrderBy("c.update_time");
        List<ContactsEO> datas = dao.queryByList(contactsEOPage);
        List<ContactsDto> resultList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd");
        if (datas.size()>0){
            for (ContactsEO eo : datas){
                ContactsDto dto;
                dto = beanMapper.map(eo,ContactsDto.class);
                if (StringUtils.isNotEmpty(dto.getCreateTime())){
                    Date tempCreateTime = new Date(dto.getCreateTime().getTime());
                    dto.setCreateTime(tempCreateTime);
                }
                if(StringUtils.isNotEmpty(dto.getContactsBirthday())){
                    Date temp_birthday = new Date(dto.getContactsBirthday().getTime());
                    dto.setContactsBirthday(temp_birthday);
                }
                resultList.add(dto);
            }
        }
        return ExcelExportUtil.exportExcel(exportParams,ContactsDto.class,resultList);
    }

    /**
     * 联系人总数
     * @return
     * @throws Exception
     */
    public int contactsStatistics(String year,String effectName) throws Exception{
        String createTimeStart = null;
        String createTimeEnd = null;
        if (StringUtils.isNotEmpty(year)){
            createTimeStart = year + "-01-01 00:00:00";
            createTimeEnd = year + "-12-31 23:59:59";
        }
        String effectId = null;
        if (StringUtils.isNotEmpty(effectName)){
            String effectDicTypeCode = "";
            switch (effectName) {
                case "高层" :
                    effectDicTypeCode = "effect_high_00";
                    break;
                case "中层" :
                    effectDicTypeCode = "effect_middle_01";
                    break;
                case "骨干" :
                    effectDicTypeCode = "effect_backbone_03";
                    break;
                default: break;
            }
            if (StringUtils.isEmpty(effectDicTypeCode)){
                throw new AdcDaBaseException("影响力填写有误，请检查！");
            }else {
                DicTypeEO effectData = dicTypeEOService.getDicTypeEOByCode(effectDicTypeCode);
                if (StringUtils.isEmpty(effectData)){
                    throw new AdcDaBaseException("影响力查询有误，请联系开发人员！");
                }else{
                   effectId = effectData.getId();
                }
            }
        }
        return dao.contactsStatistics(createTimeStart,createTimeEnd,effectId);
    }

    /**
     * 联系人总数
     * @return
     * @throws Exception
     */
    public DashBoardVO contactsStatistics(final int  myear) throws Exception{
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        if (StringUtils.isNotEmpty(myear)){
            year = myear;
        }
        int higherNum = 0;
        int newCreateNum = 0;

        String createTimeStart = null;
        String createTimeEnd = null;
        if (StringUtils.isNotEmpty(year)){
            createTimeStart = year + "-01-01 00:00:00";
            createTimeEnd = year + "-12-31 23:59:59";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(createTimeStart);
        Date endDate = simpleDateFormat.parse(createTimeEnd);
        List<ContactsEO> contactsEOList = dao.queryByList(new ContactsEOPage());
        for (ContactsEO contactsEO : contactsEOList){
            if (StringUtils.equals(contactsEO.getEffectName(),"高层")){
                higherNum ++ ;
            }
            if (null != contactsEO.getCreateTime() && contactsEO.getCreateTime().after(startDate)&&contactsEO.getCreateTime().before(endDate)){
                newCreateNum ++ ;
            }
        }
        DashBoardVO dashBoardVO  = new DashBoardVO();
        dashBoardVO.setYear(year);
        projectService.newProjectOwnerStatistics(dashBoardVO);;
        dashBoardVO.setContactNum(contactsEOList.size());
        dashBoardVO.setCurrentYearContactIncreaseNum(newCreateNum);
        dashBoardVO.setPolicymakerNum(higherNum);

        return dashBoardVO;
    }

}
