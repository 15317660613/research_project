package com.adc.da.research.personnel.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.personnel.dao.ExpertGroupInfoEODao;
import com.adc.da.research.personnel.dao.ExpertGroupUserDao;
import com.adc.da.research.personnel.dto.BindUserInfoDto;
import com.adc.da.research.personnel.dto.ExpertGroupInfoEODto;
import com.adc.da.research.personnel.entity.ExpertGroupInfoEO;
import com.adc.da.research.personnel.entity.ExpertUserInfoEO;
import com.adc.da.research.personnel.page.ExpertGroupInfoEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_EXPERT_GROUP_INFO ExpertGroupInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("expertGroupInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ExpertGroupInfoEOService extends BaseService<ExpertGroupInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ExpertGroupInfoEOService.class);

    @Autowired
    private ExpertGroupInfoEODao dao;
    @Autowired
    private ExpertGroupUserDao expertGroupUserDao;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ExpertUserInfoEOService expertUserInfoEOService;

    public ExpertGroupInfoEODao getDao() {
        return dao;
    }


    /**
     * 新增 专家组数据
     * @param eo
     * @return
     * @throws Exception
     */
    public ExpertGroupInfoEO create (ExpertGroupInfoEO eo) throws Exception{


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
        eo.setDelFlag(0);

        this.insertSelective(eo);
        return eo;
    }

    /**
     * 批量删除
     * @param ids
     */
    public void deleteByIds(String[] ids) {
        this.dao.deleteByIds(ids);
    }
    /**
     * 导出专家组数据
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportExpertGroup(ExportParams exportParams, ExpertGroupInfoEOPage eoPage) throws Exception{
        List<ExpertGroupInfoEO> list;
        if (eoPage.getIds() != null && eoPage.getIds().length > 0) {
            String[] ids = eoPage.getIds();
            list = this.dao.queryByIds(ids);
        } else {
            list = this.queryByList(eoPage);
        }
        List<ExpertGroupInfoEODto> resutList = new ArrayList<>();
        if (list.size()>0){
            resutList = beanMapper.mapList(list, ExpertGroupInfoEODto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams,ExpertGroupInfoEODto.class,resutList);
    }


    //查询专家组绑定专家信息
    public BindUserInfoDto getBindUserInfoDto(String id) throws Exception{
        BindUserInfoDto bindUserInfoDto = new BindUserInfoDto();
        ExpertGroupInfoEO expertGroupInfoEO = dao.selectByPrimaryKey(id);
        if (expertGroupInfoEO != null){
            beanMapper.copy(expertGroupInfoEO,bindUserInfoDto);
            List<ExpertUserInfoEO> bindUserInfoList = expertUserInfoEOService.getBindUserInfoList(id);
            bindUserInfoDto.setBindUserInfoList(bindUserInfoList);
            List<ExpertUserInfoEO> unBindUserInfoList = expertUserInfoEOService.getUnBindUserInfoList(id);
            bindUserInfoDto.setUnBindUserInfoList(unBindUserInfoList);
        }
        return bindUserInfoDto;
    }

    //保存专家组绑定用户信息
    public void saveBindUserInfo(BindUserInfoDto bindUserInfoDto) throws Exception{
        String id = bindUserInfoDto.getId();
        List<ExpertUserInfoEO> newBindUserInfoList = bindUserInfoDto.getBindUserInfoList();
        //获取原绑定信息
        List<ExpertUserInfoEO> oldBindUserInfoList = expertUserInfoEOService.getBindUserInfoList(id);
        //删除原绑定信息
        if (CollectionUtils.isNotEmpty(oldBindUserInfoList)){
            expertUserInfoEOService.deleteBindUser(id);
        }
        if (CollectionUtils.isNotEmpty(newBindUserInfoList)){
            for (ExpertUserInfoEO eo : newBindUserInfoList){
                String uuid = UUID.randomUUID10();
                expertUserInfoEOService.saveBindUser(uuid,id,eo.getId());
            }
        }
    }

    /**
     * 获取专家组加用户绑定列表
     * @param page
     * @return
     * @throws Exception
     */
    public List<ExpertUserInfoEO> getBindUserInfoList(ExpertGroupInfoEOPage page) throws Exception{
    List<ExpertUserInfoEO> bindUserList =new ArrayList<ExpertUserInfoEO>();
  //  String  userGroupName="";
        List<ExpertGroupInfoEO> groupList=   this.queryByList(page);
        for (ExpertGroupInfoEO eg:groupList) {
            BindUserInfoDto bindUserInfoDto = this.getBindUserInfoDto(eg.getId());
            for (ExpertUserInfoEO eu:bindUserInfoDto.getBindUserInfoList()) {
               // userGroupName=eg.getExpertGroupName()+"-"+eu.getUserName();
              //  eu.setUserName(userGroupName);
                eu.setExpertGroupName(eg.getExpertGroupName());
                bindUserList.add(eu);
            }
        }

        return bindUserList;

}

    /**
     * 根据专家人员id获取专家组列表
     * @param id
     * @return
     */
    public List<String> getBindExpertGroupList(String id) {
        return dao.getBindExpertGroupList(id);
    }

    /**
     * 根据专家人员id获取专家组列表
     * @param id
     * @return
     */
    public List<ExpertGroupInfoEO> getExpertGroupList(String id) {
        return dao.getExpertGroupList(id);
    }

    /**
     * 根据专家人员id删除专家组关联信息
     * @param userId
     */
    public void deleteByUserId(String userId) {
        expertGroupUserDao.deleteByUserId(userId);
    }
}
