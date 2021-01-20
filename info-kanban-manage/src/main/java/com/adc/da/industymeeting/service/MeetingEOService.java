package com.adc.da.industymeeting.service;

import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.industymeeting.dto.MeetingDto;
import com.adc.da.industymeeting.page.MeetingEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.industymeeting.dao.MeetingEODao;
import com.adc.da.industymeeting.entity.MeetingEO;

import java.io.InputStream;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>INDUSTY_MEETING MeetingEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("meetingEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MeetingEOService extends BaseService<MeetingEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(MeetingEOService.class);
    //private static final String userId = "GHVRTMA9H2";

    @Autowired
    private MeetingEODao dao;
    @Autowired
    BeanMapper beanMapper;
    @Autowired
    UserEODao userEODao;

    public MeetingEODao getDao() {
        return dao;
    }

    /**
     * Description 新增会议安排
     * @param meetingEO
     * @return
     * @throws Exception
     */
    public MeetingEO save(MeetingEO meetingEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        meetingEO.setId(UUID.randomUUID10());
        meetingEO.setCreateUserId(user.getUsid());
        meetingEO.setCreateUserName(user.getUsname());
        meetingEO.setCreateTime(new Date());
        meetingEO.setDelFlag("0");
        dao.insertSelective(meetingEO);
        return meetingEO;
    }

    /**
     * @Description 修改会议安排
     * @param meetingEO
     * @return
     * @throws Exception
     */
    public MeetingEO update(MeetingEO meetingEO) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        meetingEO.setCreateUserId(user.getUsid());
        meetingEO.setCreateUserName(user.getUsname());
        meetingEO.setCreateTime(new Date());
        dao.updateByPrimaryKeySelective(meetingEO);
        return  meetingEO;
    }

    /**
     * @Description 根据id逻辑删除行业会议信息
     * @param id
     * @throws Exception
     */
    public void logicDeleteByPrimaryKey(String id) throws Exception{
        int num = dao.logicDeleteByPrimaryKey(id);
        if (num>0){}else{
            throw new AdcDaBaseException("删除失败，请检查！");
        }
    }

    /**
     * @Description 导入会议安排
     * @param importParams
     * @param inputStream
     * @return
     * @throws Exception
     */
    public List<ExcelVerifyHanlderErrorResult> excelImportVerify(
            ImportParams importParams, InputStream inputStream) throws Exception{
        ExcelImportResult<MeetingDto> result =
                ExcelImportUtil.importExcelVerify(inputStream, MeetingDto.class, importParams);
        List<ExcelVerifyHanlderErrorResult> errors = result.getErrors();
        List<MeetingDto> datas = result.getList();
        importMeetingDatas(datas);
        return  errors;
    }

    /**
     * @Description 批量插入数据
     * @param datas
     * @throws Exception
     */
    public void importMeetingDatas(List<MeetingDto> datas) throws Exception{
        UserEO user = UserUtils.getUser();
        //UserEO user = userEODao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        for (MeetingDto meetingDto : datas){
            MeetingEO meetingEO;
            meetingEO = beanMapper.map(meetingDto,MeetingEO.class);
            if (StringUtils.isEmpty(meetingEO.getImDate())){
                throw new AdcDaBaseException("会议【"+meetingEO.getImEnterprise()+"】日期填写有误，请检查！");
            }
            if (StringUtils.isEmpty(meetingEO.getImPlace())){
                throw new AdcDaBaseException("地点不能为空，请检查！");
            }
            if (StringUtils.isEmpty(meetingEO.getImEnterprise())){
                throw new AdcDaBaseException("企业不能为空，请检查！");
            }
            if (StringUtils.isEmpty(meetingEO.getImName())){
                throw new AdcDaBaseException("会议不能为空，请检查！");
            }
            if (StringUtils.isEmpty(meetingEO.getDepartName())){
                throw new AdcDaBaseException("负责部门不能为空，请检查！");
            }
            meetingEO.setId(UUID.randomUUID10());
            meetingEO.setCreateTime(new Date());
            meetingEO.setDelFlag("0");
            meetingEO.setCreateUserId(user.getUsid());
            meetingEO.setCreateUserName(user.getUsname());
            dao.insertSelective(meetingEO);
        }
    }

    /**
     * @Description 根据条件查询信息
     * @param eoPage
     * @return
     * @throws Exception
     */
    public List<MeetingEO> queryByPage(MeetingEOPage eoPage) throws Exception{
//        String userId = UserUtils.getUserId();
//        if (StringUtils.isEmpty(userId)) {
//            throw new AdcDaBaseException("登陆可能过期，请登录！");
//        }

        //企业模糊查询
        if (StringUtils.isNotEmpty(eoPage.getImEnterprise())){
            eoPage.setImEnterpriseOperator("LIKE");
            eoPage.setImEnterprise("%"+eoPage.getImEnterprise()+"%");
        }
        //负责部分
        if (StringUtils.isNotEmpty(eoPage.getDepartName())){
            eoPage.setDepartNameOperator("LIKE");
            eoPage.setDepartName("%"+eoPage.getDepartName()+"%");
        }
        Integer totalCount = dao.queryByCount(eoPage);
        eoPage.getPager().setRowCount(totalCount);

        if (StringUtils.isEmpty(eoPage.getSql_filter())){
            eoPage.setSql_filter(" order by IM_DATE,id");
        }



        return dao.queryByPage(eoPage);
    }

}
