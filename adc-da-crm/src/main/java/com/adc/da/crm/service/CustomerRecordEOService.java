package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.config.Import;
import com.adc.da.crm.dao.CustomerContactEODao;
import com.adc.da.crm.dao.CustomerVisitRecordEODao;
import com.adc.da.crm.entity.*;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.crm.vo.CustomerRecordVO;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.form.dao.AdcFormDataDao;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.CustomerRecordEODao;

import java.io.InputStream;
import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>CUSTOMER_RECORD CustomerRecordEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("customerRecordEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CustomerRecordEOService extends BaseService<CustomerRecordEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRecordEOService.class);

    @Autowired
    private CustomerRecordEODao dao;
    @Autowired
    private CustomerContactEODao customerContactEODao;
    @Autowired
    private CustomerVisitRecordEODao customerVisitRecordEODao;
    @Autowired
    private AdcFormDataDao formDataDao;
    @Autowired
    BeanMapper beanMapper;

    public CustomerRecordEODao getDao() {
        return dao;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public AdcFormDataEO save(AdcFormDataEO adcFormDataEO) {
        //保存到ct_form
        //1.保存ct_data
        adcFormDataEO.setId(UUID.randomUUID10());
        adcFormDataEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        adcFormDataEO.setCreateTime(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.formDataDao.insert(adcFormDataEO);
        //2.保存关联 ct_form_data
        this.formDataDao.deleteFormAndDataById(adcFormDataEO.getId());
        this.formDataDao.saveFormAndData(adcFormDataEO.getFormId(), adcFormDataEO.getId());
        //保存解析后的数据
        CustomerRecordVO customerRecordVO = (CustomerRecordVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, CustomerRecordVO.class);
        saveCustomerInfo(customerRecordVO,adcFormDataEO.getId(),adcFormDataEO.getCreateName());
        return adcFormDataEO;
    }

    /**
     * 保存客户相关信息
     * @param customerRecordVO
     * @param uuid
     * @param createName 创建者名称
     */
    @Transactional(rollbackFor = {Exception.class})
    public void saveCustomerInfo(CustomerRecordVO customerRecordVO, String uuid, String createName){
        //vo 转eo
        CustomerRecordEO customerRecordEO = this.beanMapper.map(customerRecordVO, CustomerRecordEO.class);
        customerRecordEO.setId(uuid);
        customerRecordEO.setCreatedTime(new Date());
        customerRecordEO.setCreatedUser(createName);
        customerRecordEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        //客户联系信息
        List<CustomerContactEO> customerContactEOList = customerRecordVO.getCustomerContactEOList();
        for (CustomerContactEO customerContactEO:customerContactEOList) {
            customerContactEO.setId(UUID.randomUUID10());
            customerContactEO.setCustomerNumber(customerRecordEO.getCustomerNumber());
            customerContactEO.setCusRecordId(customerRecordEO.getId());
            customerContactEO.setCreatedTime(new Date());
            customerContactEO.setCreatedUser(createName);
            customerContactEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.customerContactEODao.insertSelective(customerContactEO);
        }
        List<CustomerVisitRecordEO> customerVisitRecordEOList = customerRecordVO.getCustomerVisitRecordEOList();
        for (CustomerVisitRecordEO customerVisitRecordEO : customerVisitRecordEOList) {
            customerVisitRecordEO.setId(UUID.randomUUID10());
            customerVisitRecordEO.setCustomerNumber(customerRecordEO.getCustomerNumber());
            customerVisitRecordEO.setCusRecordId(customerRecordEO.getId());
            customerVisitRecordEO.setCreatedTime(new Date());
            customerVisitRecordEO.setCreatedUser(createName);
            customerVisitRecordEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.customerVisitRecordEODao.insertSelective(customerVisitRecordEO);
        }
        this.dao.insertSelective(customerRecordEO);
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);
        //更新解析后的数据
        CustomerRecordVO customerRecordVO = (CustomerRecordVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, CustomerRecordVO.class);
        //vo 转eo
        CustomerRecordEO customerRecordEO = this.beanMapper.map(customerRecordVO, CustomerRecordEO.class);
        customerRecordEO.setId(adcFormDataEO.getId());
        customerRecordEO.setModifiedTime(new Date());
        customerRecordEO.setModifiedUser(userId);
        this.customerContactEODao.deleteByCusRecordId(adcFormDataEO.getId());
        //客户联系信息
        List<CustomerContactEO> customerContactEOList = customerRecordVO.getCustomerContactEOList();
        for (CustomerContactEO customerContactEO:customerContactEOList) {
            customerContactEO.setId(UUID.randomUUID10());
            customerContactEO.setCustomerNumber(customerRecordEO.getCustomerNumber());
            customerContactEO.setCusRecordId(customerRecordEO.getId());
            customerContactEO.setCreatedTime(new Date());
            customerContactEO.setCreatedUser(adcFormDataEO.getCreateName());
            customerContactEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.customerContactEODao.insertSelective(customerContactEO);
        }
        this.customerVisitRecordEODao.deleteByCusRecordId(adcFormDataEO.getId());
        List<CustomerVisitRecordEO> customerVisitRecordEOList = customerRecordVO.getCustomerVisitRecordEOList();
        for (CustomerVisitRecordEO customerVisitRecordEO : customerVisitRecordEOList) {
            customerVisitRecordEO.setId(UUID.randomUUID10());
            customerVisitRecordEO.setCustomerNumber(customerRecordEO.getCustomerNumber());
            customerVisitRecordEO.setCusRecordId(customerRecordEO.getId());
            customerVisitRecordEO.setCreatedTime(new Date());
            customerVisitRecordEO.setCreatedUser(adcFormDataEO.getCreateName());
            customerVisitRecordEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.customerVisitRecordEODao.insertSelective(customerVisitRecordEO);
        }

        return this.dao.updateByPrimaryKeySelective(customerRecordEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        this.customerContactEODao.updateByCusRecordIdAndDelFlag(id ,1);
        this.customerVisitRecordEODao.updateByCusRecordIdAndDelFlag(id, 1);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<CustomerRecordEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }


    /**
     * 导入
     *
     * @param is
     * @param params
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class})
    public String excelImport(InputStream is, ImportParams params) throws Exception {
        List<CustomerRecordVO> datas = ExcelImportUtil.importExcel(is, CustomerRecordVO.class, params);
        if(datas == null || datas.size() == 0) return "无数据！";
        String info = null;
        Set<String> nameSet = new HashSet<>();
        Set<String> deptSet = new HashSet<>();
        Map<String,List<CustomerRecordVO>> nameMap = new HashMap<>();
        Map<String,List<CustomerRecordVO>> deptMap = new HashMap<>();
        for (CustomerRecordVO customerRecordVO : datas) {
            //区域经理处理
            if((info = customerRecordVO.getAreaManagerId()) == null){
                return "区域经理不能为空！";
            }
            nameSet.add(info);
            customerRecordVO.setAreaManagerId(null);
            if(nameMap.get(info) == null) nameMap.put(info,new LinkedList<CustomerRecordVO>());
            nameMap.get(info).add(customerRecordVO);


            //所属部门处理
            if((info = customerRecordVO.getDeptId()) == null){
                return "所属部门不能为空！";
            }
            deptSet.add(info);
            customerRecordVO.setDeptId(null);
            if(deptMap.get(info) == null) deptMap.put(info,new LinkedList<CustomerRecordVO>());
            deptMap.get(info).add(customerRecordVO);
        }

        //根据用户名获取用户信息
        List<IdNameInfo> idNameInfoList = this.dao.getUserByName(nameSet);
        if(idNameInfoList == null || idNameInfoList.size() < nameSet.size()){
            return "有区域经理不存在！";
        }

        //根据部门名获取部门信息
        List<IdNameInfo> deptInfoList = this.dao.getDeptByName(deptSet);
        if(deptInfoList == null || deptInfoList.size() < deptSet.size()){
            return "有所属部门不存在！";
        }

        //填充区域经理id
        for (IdNameInfo idNameInfo : idNameInfoList) {
            for(CustomerRecordVO customerRecordVO : nameMap.get(idNameInfo.getName())){
                customerRecordVO.setAreaManagerId(idNameInfo.getId());
            }
        }

        //填充所属部门id
        for (IdNameInfo idNameInfo : deptInfoList) {
            for(CustomerRecordVO customerRecordVO : deptMap.get(idNameInfo.getName())){
                customerRecordVO.setDeptId(idNameInfo.getId());
            }
        }

        for (CustomerRecordVO customerRecordVO : datas) {
            if(customerRecordVO.getAreaManagerId() == null) return "有区域经理不存在！";
            if(customerRecordVO.getDeptId() == null) return "有所属部门不存在！";
            saveCustomerInfo(customerRecordVO,UUID.randomUUID10(), UserUtils.getUser().getUsname());
        }
        return Import.SUCCESS;
    }
}
