package com.adc.da.research.personnel.service;

import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.personnel.dao.UserInfoEODao;
import com.adc.da.research.personnel.entity.UserInfoEO;



import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.research.personnel.dto.UserInfoEODto;
import com.adc.da.research.personnel.page.UserInfoEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;




import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_USER_INFO UserInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("userInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserInfoEOService extends BaseService<UserInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoEOService.class);

    @Autowired
    private UserInfoEODao dao;

    @Autowired
    private BeanMapper beanMapper;

    public UserInfoEODao getDao() {
        return dao;
    }

    @Autowired
    private UserEODao userEODao;

    /**
     * 新增 科研人员数据
     * @param eo
     * @return
     * @throws Exception
     */
    public UserInfoEO create (UserInfoEO eo) throws Exception{
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
        eo.setId(UUID.randomUUID10());

        //同时修改用户管理 对应人员的信息
        UserEO newUser =new UserEO();
        newUser.setUsid(eo.getUserId());
        newUser.setGender(eo.getSex());
        newUser.setBirthDate(eo.getExt1());
        newUser.setLastDegree(eo.getLastDegree());
        newUser.setFinalDegree(eo.getQualifications());
        newUser.setJobTitle(eo.getTitleOf());
        newUser.setEmail(eo.getEmail());

//        userEODao.updateByPrimaryKeySelectiveWithDelFlag(newUser);
        userEODao.updateByPrimaryKeySelective(newUser);

       // eo.setId(UUID.randomUUID10());
        dao.insertSelective(eo);

        return eo;
    }


    /**
     * 修改 科研人员数据
     * @param eo
     * @return
     * @throws Exception
     */
    public UserInfoEO updateAndUser (UserInfoEO eo) throws Exception{

        //同时修改用户管理 对应人员的信息
        UserEO newUser =new UserEO();
        newUser.setUsid(eo.getUserId());
        newUser.setGender(eo.getSex());
        newUser.setBirthDate(eo.getExt1());
        newUser.setLastDegree(eo.getLastDegree());
        newUser.setFinalDegree(eo.getQualifications());
        newUser.setJobTitle(eo.getTitleOf());
        newUser.setEmail(eo.getEmail());

        userEODao.updateByPrimaryKeySelectiveWithDelFlag(newUser);
            //修改科研人员表
        this.updateByPrimaryKeySelective(eo);
        return eo;
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


    }

    /**
     * 导出科研人员数据
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportRevenueManagement(ExportParams exportParams, UserInfoEOPage eoPage) throws Exception{
        eoPage.setPageSize(15000);
        eoPage.setUserNameOperator("like");
        eoPage.setIdOperator("like");
        List<UserInfoEO> list = this.queryByPage(eoPage);
        List<UserInfoEODto> resutList = new ArrayList<>();
        if (list.size()>0){
            resutList = beanMapper.mapList(list, UserInfoEODto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams,UserInfoEODto.class,resutList);
    }


    /**
     * 导入科研人员数据
     * @param inputStream
     * @param importParams
     * @return
     * @throws Exception
     */
    public String importRevenueManagement (InputStream inputStream, ImportParams importParams) throws Exception{
        ExcelImportResult<UserInfoEODto> result =
                ExcelImportUtil.importExcelMore(inputStream, UserInfoEODto.class, importParams);
        List<UserInfoEODto> errors = result.getFailList();
        StringBuilder stringBuilder = new StringBuilder();
        if (errors.size()>0){
            for (UserInfoEODto dto : errors){
                if (!dto.getErrorMsg().contains("无效数据")){
                    stringBuilder.append("第"+(dto.getRowNum()+1)+"行"+dto.getErrorMsg().replaceAll(",",""));
                }
            }
        }
        if (StringUtils.isEmpty(stringBuilder)){
            List<UserInfoEODto> datas = result.getList();
            if (datas.size()>0){
                // this.importDatas(datas);
            }else {
                throw new AdcDaBaseException("请正确填写科研人员数据！");
            }

        }
        return stringBuilder.toString();
    }

    /**
     * 导入方法
     * @param datas
     */
    public void importData(List<UserInfoEODto> datas){
        for(UserInfoEODto dto : datas){
            UserInfoEO eo = new UserInfoEO();
            eo = beanMapper.map(dto,UserInfoEO.class);
             BeanUtils.copyProperties(dto, eo);
            eo.setId(DigestUtils.md5Hex(dto.toString()));
            try {
               // dao.batchInsert(eo);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }

}
