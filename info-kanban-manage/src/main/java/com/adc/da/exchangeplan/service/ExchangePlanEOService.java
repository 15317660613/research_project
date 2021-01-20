package com.adc.da.exchangeplan.service;

import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.exchangeplan.dto.ExchangePlanDto;
import com.adc.da.exchangeplan.page.ExchangePlanEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.exchangeplan.dao.ExchangePlanEODao;
import com.adc.da.exchangeplan.entity.ExchangePlanEO;

import java.io.InputStream;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>DB_EXCHANGE_PLAN ExchangePlanEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("exchangePlanEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ExchangePlanEOService extends BaseService<ExchangePlanEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ExchangePlanEOService.class);
    //private static final String userId = "GHVRTMA9H2";

    @Autowired
    private ExchangePlanEODao dao;
    @Autowired
    BeanMapper beanMapper;
    @Autowired
    UserEODao userEODao;

    public ExchangePlanEODao getDao() {
        return dao;
    }

    /**
     *
     * @Destription 添加近期交流会议信息
     * @param exchangePlanEO
     * @return
     * @throws Exception
     */
    public ExchangePlanEO create(ExchangePlanEO exchangePlanEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        exchangePlanEO.setId(UUID.randomUUID10());
        exchangePlanEO.setCreateTime(new Date());
        exchangePlanEO.setCreateUserId(user.getUsid());
        exchangePlanEO.setCreateUserName(user.getUsname());
        exchangePlanEO.setDelFlag("0");
        int num = dao.insertSelective(exchangePlanEO);
        if (num>0){
            return exchangePlanEO;
        }else{
            throw new AdcDaBaseException("添加失败，请检查！");
        }
    }
    /**
     *
     * @Destription 修改近期交流会议信息
     * @param exchangePlanEO
     * @return
     * @throws Exception
     */
    public ExchangePlanEO update(ExchangePlanEO exchangePlanEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(StringUtils.isEmpty(exchangePlanEO.getExt2())){
            exchangePlanEO.setExt2("");
        }
        exchangePlanEO.setCreateTime(new Date());
        exchangePlanEO.setCreateUserId(user.getUsid());
        exchangePlanEO.setCreateUserName(user.getUsname());
        exchangePlanEO.setDelFlag("0");
        int num = dao.updateByPrimaryKeySelective(exchangePlanEO);
        if (num>0){
            return exchangePlanEO;
        }else{
            throw new AdcDaBaseException("修改失败，请检查！");
        }
    }

    /**
     * @Destription 根据id进行逻辑删除
     * @param id
     * @throws Exception
     */
    public void logicDeleteByPrimaryKey(String id) throws Exception{
        int num = dao.logicDeleteByPrimaryKey(id);
        if (num>0){}else{
            throw new AdcDaBaseException("删除失败，请检查！");
        }
    }

    public void logicDeleteByPrimaryKeys(List<String> ids){
        dao.logicDeleteByPrimaryKeys(ids);
    }

    /**
     * @Destription 根据模板导入近期交流计划信息
     * @param importParams
     * @param inputStream
     * @return
     * @throws Exception
     */
    public List<ExcelVerifyHanlderErrorResult> excelImportVerify(
            ImportParams importParams, InputStream inputStream) throws Exception{
        ExcelImportResult<ExchangePlanDto> result =
                ExcelImportUtil.importExcelVerify(inputStream, ExchangePlanDto.class, importParams);
        List<ExcelVerifyHanlderErrorResult> errors = result.getErrors();
        List<ExchangePlanDto> datas = result.getList();
        importDatas(datas);
        return errors;
    }

    public void importDatas(List<ExchangePlanDto> datas) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        for (ExchangePlanDto exchangePlanDto : datas){
            ExchangePlanEO exchangePlanEO;
            exchangePlanEO = beanMapper.map(exchangePlanDto,ExchangePlanEO.class);
            if (StringUtils.isEmpty(exchangePlanEO.getEpDate())){
                throw new AdcDaBaseException("企业【"+exchangePlanEO.getEpEnterprise()+"】日期填写有误，请检查！");
            }
            if (StringUtils.isEmpty(exchangePlanEO.getEpEnterprise())){
                throw new AdcDaBaseException("企业不能为空，请检查！");
            }
            if (StringUtils.isEmpty(exchangePlanEO.getEpExchangeDomain())){
                throw new AdcDaBaseException("交流领域不能为空，请检查！");
            }
            if (StringUtils.isEmpty(exchangePlanEO.getEpLeaderName())){
                throw new AdcDaBaseException("建议领导不能为空，请检查！");
            }
            if (StringUtils.isEmpty(exchangePlanEO.getEpForm())){
                throw new AdcDaBaseException("交流形式不能为空，请检查！");
            }
            exchangePlanEO.setId(UUID.randomUUID10());
            exchangePlanEO.setCreateTime(new Date());
            exchangePlanEO.setCreateUserId(user.getUsid());
            exchangePlanEO.setCreateUserName(user.getUsname());
            exchangePlanEO.setDelFlag("0");
            int num = dao.insertSelective(exchangePlanEO);
            if (num>0){
                continue;
            }else{
                throw new AdcDaBaseException("导入失败，请检查！");
            }
        }
    }

    public List<ExchangePlanEO>  getCurrentExchangePlanList(){
        Date date =DateUtils.getOnlyYMD(new Date());
        return dao.getCurrentExchangePlanList(date);
    }


    public List<ExchangePlanEO> queryByPage(ExchangePlanEOPage exchangePlanEOPage) throws Exception{
        if (StringUtils.isNotEmpty(exchangePlanEOPage.getEpEnterprise())){
            exchangePlanEOPage.setEpEnterpriseOperator("LIKE");
            exchangePlanEOPage.setEpEnterprise("%"+exchangePlanEOPage.getEpEnterprise()+"%");
        }
        if (StringUtils.isNotEmpty(exchangePlanEOPage.getEpExchangeDomain())){
            exchangePlanEOPage.setEpExchangeDomainOperator("LIKE");
            exchangePlanEOPage.setEpExchangeDomain("%"+exchangePlanEOPage.getEpExchangeDomain()+"%");
        }
        Integer totalCount = dao.queryByCount(exchangePlanEOPage);
        exchangePlanEOPage.getPager().setRowCount(totalCount);


        return dao.queryByPage(exchangePlanEOPage);
    }

}
