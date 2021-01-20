package com.adc.da.customerresourcemanage.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.customerresourcemanage.dao.ContactsEODao;
import com.adc.da.customerresourcemanage.dto.EnterpriseDto;
import com.adc.da.customerresourcemanage.page.EnterpriseEOPage;
import com.adc.da.dashboard.dao.ProvinceAreaEODao;
import com.adc.da.dashboard.entity.ProvinceAreaEO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
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
import com.adc.da.customerresourcemanage.dao.EnterpriseEODao;
import com.adc.da.customerresourcemanage.entity.EnterpriseEO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>DB_ENTERPRISE EnterpriseEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("enterpriseEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EnterpriseEOService extends BaseService<EnterpriseEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseEOService.class);
    //private static final String userId = "GHVRTMA9H2";

    @Autowired
    private EnterpriseEODao dao;
    @Autowired
    BeanMapper beanMapper;
    @Autowired
    UserEODao userEODao;
    @Autowired
    private ContactsEODao contactsEODao;
    @Autowired
    private ProvinceAreaEODao provinceAreaEODao;

    public EnterpriseEODao getDao() {
        return dao;
    }

    /**
     * @Description 分页模糊查询
     * @param enterpriseEOPage
     * @return
     * @throws Exception
     */
    public List<EnterpriseEO> queryByPage(EnterpriseEOPage enterpriseEOPage) throws Exception{
//        UserEO user = UserUtils.getUser();
//        //UserEO user = userEODao.selectByPrimaryKey(userId);
//        if (StringUtils.isEmpty(user)) {
//            throw new AdcDaBaseException("登陆可能过期，请登录！");
//        }
        //企业名称模糊查询
        if (StringUtils.isNotEmpty(enterpriseEOPage.getEnterpriseName())){
            enterpriseEOPage.setEnterpriseNameOperator("LIKE");
            enterpriseEOPage.setEnterpriseName("%"+enterpriseEOPage.getEnterpriseName()+"%");
        }
        int num = dao.queryByCount(enterpriseEOPage);
        enterpriseEOPage.getPager().setRowCount(num);
        if (StringUtils.isEmpty(enterpriseEOPage.getSql_filter())){
            enterpriseEOPage.setSql_filter(" order by UPDATE_TIME desc,id");
        }
        return  dao.queryByPage(enterpriseEOPage);
    }


    /**
     * @Description 模糊查询
     * @param enterpriseEOPage
     * @return
     * @throws Exception
     */
    public List<EnterpriseEO> queryByList(EnterpriseEOPage enterpriseEOPage) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //企业名称模糊查询
        if (StringUtils.isNotEmpty(enterpriseEOPage.getEnterpriseName())){
            enterpriseEOPage.setEnterpriseNameOperator("LIKE");
            enterpriseEOPage.setEnterpriseName("%"+enterpriseEOPage.getEnterpriseName()+"%");
        }
        if (StringUtils.isEmpty(enterpriseEOPage.getSql_filter())){
            enterpriseEOPage.setSql_filter(" order by UPDATE_TIME desc,id");
        }
        return dao.queryByList(enterpriseEOPage);
    }

    /**
     * @Description 新增企业信息
     * @param enterpriseEO
     * @return
     * @throws Exception
     */
    public EnterpriseEO create(EnterpriseEO enterpriseEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        verifyEnterpriseName(enterpriseEO.getEnterpriseName());
        Date date = new Date();
        enterpriseEO.setId(UUID.randomUUID10());
        enterpriseEO.setCreateUserId(user.getUsid());
        enterpriseEO.setCreateUserName(user.getUsname());
        enterpriseEO.setUpdateTime(date);
        enterpriseEO.setDelFlag("0");
        dao.insertSelective(enterpriseEO);
        return  enterpriseEO;
    }

    public void verifyEnterpriseName(String enterpriseName) throws Exception{
        EnterpriseEO eo = dao.findEnterpriseByName(enterpriseName);
        if(StringUtils.isNotEmpty(eo)){
            throw new AdcDaBaseException("企业名称已存在，请检查！");
        }
    }
    /**
     * @Description 修改企业信息
     * @param enterpriseEO
     * @return
     * @throws Exception
     */
    public EnterpriseEO update(EnterpriseEO enterpriseEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }/*
        if (StringUtils.isEmpty(enterpriseEO.getCreateTime())){
            enterpriseEO.setCreateTime(new Date());
        }*/
        EnterpriseEO oldEnterpriseEO = dao.selectByPrimaryKey(enterpriseEO.getId());
        if (!enterpriseEO.getEnterpriseName().equals(oldEnterpriseEO.getEnterpriseName())){
            verifyEnterpriseName(enterpriseEO.getEnterpriseName());
        }
        enterpriseEO.setUpdateTime(new Date());
        enterpriseEO.setDelFlag("0");
        enterpriseEO.setCreateUserId(user.getUsid());
        enterpriseEO.setCreateUserName(user.getUsname());
        dao.updateByPrimaryKey(enterpriseEO);
        return  enterpriseEO;
    }

    /**
     * @Description 逻辑删除企业以及关联企业的用户信息
     * @param id
     * @throws Exception
     */
    public void logicDeleteByPrimaryKey(String id) throws Exception{
        int num = dao.logicDeleteByPrimaryKey(id);
        if (num>0){
            //同时逻辑删除关联的企业用户信息
            contactsEODao.logicDeleteByEnterpriseId(id);
        }else {
            throw new AdcDaBaseException("删除企业信息失败，请检查！");
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
        if (num>0){
            //同时逻辑删除关联的用户
            contactsEODao.batchLogicDeleteByEnterpriseIds(ids);
        }else{
            throw new AdcDaBaseException("删除企业信息失败，请检查！");
        }

    }

    /**
     * @Description 导入
     * @param inputStream
     * @param importParams
     * @return
     * @throws Exception
     */
    public List<ExcelVerifyHanlderErrorResult> importEnterprise(
            InputStream inputStream, ImportParams importParams) throws Exception{
        ExcelImportResult<EnterpriseDto> result =ExcelImportUtil.importExcelVerify(inputStream, EnterpriseDto.class, importParams);

        List<ExcelVerifyHanlderErrorResult> errors = result.getErrors();
        List<EnterpriseDto> datas = result.getList();
        importEnterpriseDatas(datas);
        return errors;
    }

    public void importEnterpriseDatas(List<EnterpriseDto> datas) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if (datas.size()==0 || datas ==null){
            throw new AdcDaBaseException("导入文件中没有数据，请检查！");
        }

        for (EnterpriseDto dto : datas){
            EnterpriseEO eo;//新增eo
            EnterpriseEO eoUpdate = new EnterpriseEO();//更新eo
            eo = beanMapper.map(dto,EnterpriseEO.class);
            if(StringUtils.isEmpty(eo.getEnterpriseType())){
                throw new AdcDaBaseException("企业类型不能为空，请检查！");
            }
            if(StringUtils.isEmpty(eo.getEnterpriseDomain())){
                throw new AdcDaBaseException("企业领域不能为空，请检查！");
            }

            //查询企业省份id
            if (StringUtils.isNotEmpty(eo.getEnterpriseProvince())){
                ProvinceAreaEO provinceAreaEO = provinceAreaEODao.queryOneByProvince(eo.getEnterpriseProvince());
                if (StringUtils.isNotEmpty(provinceAreaEO)){
                    eo.setEnterpriseProvinceId(provinceAreaEO.getId());
                }else{
                    throw new AdcDaBaseException("企业省份填写有误，请检查！");
                }
            }

            if(StringUtils.isEmpty(eo.getEnterpriseName())){
                throw new AdcDaBaseException("企业名称不能为空，请检查！");
            }else{
                //验证企业重名问题
                eoUpdate = dao.findEnterpriseByName(eo.getEnterpriseName());
                if(StringUtils.isNotEmpty(eoUpdate)){
                    //更新数据
                    eoUpdate.setEnterpriseType(eo.getEnterpriseType());
                    eoUpdate.setEnterpriseDomain(eo.getEnterpriseDomain());
                    eoUpdate.setEnterpriseProvinceId(eo.getEnterpriseProvinceId());
                    eoUpdate.setEnterpriseProvince(eo.getEnterpriseProvince());
                    eoUpdate.setEnterpriseAddress(eo.getEnterpriseAddress());
                    eoUpdate.setCreateTime(eo.getCreateTime());
                }
            }
            Date date = new Date();
            if (StringUtils.isNotEmpty(eoUpdate)){
                eoUpdate.setCreateUserId(user.getUsid());
                eoUpdate.setCreateUserName(user.getUsname());
                eoUpdate.setUpdateTime(date);
                eoUpdate.setDelFlag("0");
                dao.updateByPrimaryKey(eoUpdate);
            }else{
                eo.setId(UUID.randomUUID10());
                eo.setCreateUserId(user.getUsid());
                eo.setCreateUserName(user.getUsname());
                //eo.setCreateTime(date);
                eo.setUpdateTime(date);
                eo.setDelFlag("0");
                dao.insertSelective(eo);
            }


        }
    }

    /**
     * @Description 导出
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook excelExportEnterprise(ExportParams exportParams) throws Exception{
        EnterpriseEOPage enterpriseEOPage = new EnterpriseEOPage();
        List<EnterpriseEO> lists = this.queryByList(new EnterpriseEOPage());
        List<EnterpriseDto> resultLists = new ArrayList<>();
        if (lists.size()>0){
            for (EnterpriseEO eo : lists){
                EnterpriseDto dto;
                dto = beanMapper.map(eo,EnterpriseDto.class);
                if (StringUtils.isNotEmpty(dto.getCreateTime())){
                    java.sql.Date tempCreateTime = new java.sql.Date(dto.getCreateTime().getTime());
                    dto.setCreateTime(tempCreateTime);
                }
                resultLists.add(dto);
            }

        }
        return ExcelExportUtil.exportExcel(exportParams,EnterpriseDto.class,resultLists);
    }

    /**
     * 根据企业名称查询企业信息
     * @param enterpeisrName
     * @return
     * @throws Exception
     */
    public EnterpriseEO findEnterpriseByName(String enterpeisrName) throws Exception{
        return dao.findEnterpriseByName(enterpeisrName);
    }
}
