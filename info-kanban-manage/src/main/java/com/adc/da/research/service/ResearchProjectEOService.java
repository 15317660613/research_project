package com.adc.da.research.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.base.page.BasePage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.pad.entity.PadOperationManageEO;
import com.adc.da.pad.handler.PadOperationManageEOHandler;
import com.adc.da.pad.page.PadOperationManageEOPage;
import com.adc.da.research.dao.ResProArriveFeeEODao;
import com.adc.da.research.dao.ResProFeeDetailEODao;
import com.adc.da.research.entity.ResProArriveFeeEO;
import com.adc.da.research.entity.ResProFeeDetailEO;
import com.adc.da.research.handler.ResearchProjectEOHandler;
import com.adc.da.research.page.ResProArriveFeeEOPage;
import com.adc.da.research.page.ResProFeeDetailEOPage;
import com.adc.da.research.page.ResearchProjectEOPage;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
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

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.ResearchProjectEODao;
import com.adc.da.research.entity.ResearchProjectEO;

import java.io.InputStream;
import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>DB_RESEARCH_PROJECT ResearchProjectEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchProjectEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchProjectEOService extends BaseService<ResearchProjectEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectEOService.class);

    @Autowired
    private ResearchProjectEODao dao;
    @Autowired
    private OrgEODao orgEODao;
    @Autowired
    private DicTypeEODao dicTypeEODao;


    @Autowired
    private ResProArriveFeeEODao resProArriveFeeEODao;

    @Autowired
    private ResProFeeDetailEODao resProFeeDetailEODao;


    public ResearchProjectEODao getDao() {
        return dao;
    }

    public Map<String,String> getLevelDicNameIdMap(){
        List<DicTypeEO> dicTypeEOList  = dicTypeEODao.queryByList("RES_DB_PRO_LEVEL");
        return getDicNameIdMap(dicTypeEOList);
    }

    public Map<String,String> getStatusDicNameIdMap(){
        List<DicTypeEO> dicTypeEOList  = dicTypeEODao.queryByList("RES_DB_PRO_STATUS");
        return getDicNameIdMap(dicTypeEOList);
    }

    public Map<String,String> getDangerNameIdMap(){
        List<DicTypeEO> dicTypeEOList  = dicTypeEODao.queryByList("RES_DB_DANGER_LEVEL");
        return getDicNameIdMap(dicTypeEOList);
    }

    public Map<String,String> getDicNameIdMap(String dicCode){
        List<DicTypeEO> dicTypeEOList  = dicTypeEODao.queryByList(dicCode);
        return getDicNameIdMap(dicTypeEOList);
    }


    public void excelImport(InputStream is) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        String [] arr = {"firstDept","secondDept","thirdDept"};
        List<OrgEO> orgEOList = orgEODao.selectByOrgTypes(arr,null);
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);

        Map<String, String> levelDicNameIdMap  =  getLevelDicNameIdMap();
        Map<String, String> statusDicNameIdMap  =  getStatusDicNameIdMap();
        Map<String, String> dangerDicNameIdMap  =  getDangerNameIdMap();

        ImportParams params = new ImportParams();
        params.setNeedVerfiy(true);
        params.setVerifyHandler(new ResearchProjectEOHandler());
        ExcelImportResult<ResearchProjectEO> result = ExcelImportUtil.importExcelMore(is, ResearchProjectEO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            List<Integer> rowIndexList = new ArrayList<>();
            List<ResearchProjectEO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (ResearchProjectEO error : errors) {
                rowIndexList.add(error.getRowNum() + 1);
            }
            sb.append("第 ");
            sb.append(StringUtils.join(rowIndexList, '、'));
            sb.append(" 行数据不合法，请检查！");
            logger.error("excel校验不通过！" + sb.toString());
            throw new AdcDaBaseException(sb.toString());
        }
        List<ResearchProjectEO> padOperationManageEOList = result.getList();
        if (CollectionUtils.isEmpty(padOperationManageEOList)){
            throw new AdcDaBaseException("导入数据集为空，请检查！");
        }
       List<ResearchProjectEO> researchProjectEOList = dao.queryByList(new ResearchProjectEOPage());
        Set<String> existResearchIdSet = new HashSet<>();
        for(ResearchProjectEO researchProjectEO : researchProjectEOList){
            existResearchIdSet.add(researchProjectEO.getResearchProjectId());
        }

        // 用于检测本次插入之间的科研id是否重复
        Set<String> researchProjectIdSet = new HashSet<>();
        //导入
        for (ResearchProjectEO researchProjectEO : padOperationManageEOList) {
            if (existResearchIdSet.contains(researchProjectEO.getResearchProjectId())){
                throw new AdcDaBaseException("科研项目id " + researchProjectEO.getResearchProjectId() + "填写与已有记录冲突,请检查！");
            }
            if (!researchProjectIdSet.add(researchProjectEO.getResearchProjectId())){
                throw new AdcDaBaseException("科研项目id " + researchProjectEO.getResearchProjectId() + "在excel表内有存在多个相同值,请检查！");
            }

            researchProjectEO.setId(UUID.randomUUID10());
            if (StringUtils.isNotEmpty(researchProjectEO.getChargeOrgName())) {
                String orgId = orgNameIdMap.get(researchProjectEO.getChargeOrgName());
                if (null == orgId) {
                    throw new AdcDaBaseException("组织机构 " + researchProjectEO.getChargeOrgName() + " 填写不合同法,请检查！");
                }
                researchProjectEO.setChargeOrgId(orgId);
            }
            String statusName = editName(researchProjectEO.getProjectStatusDicName());
            String levelName = editName(researchProjectEO.getProjectLevelDicName());
            researchProjectEO.setProjectStatusDicName(statusName);
            researchProjectEO.setProjectLevelDicName(levelName);
            String dangerName = researchProjectEO.getExtInfo1();

            String statusDicId = statusDicNameIdMap.get(statusName);
            if (null == statusDicId){
                throw new AdcDaBaseException("项目状态 " + statusName + " 不存在,请检查！");
            }
            String levelDicId = levelDicNameIdMap.get(levelName);
            if (null == levelDicId){
                throw new AdcDaBaseException("项目级别 " + levelName + " 不存在,请检查！");
            }

            String dangerDicId = dangerDicNameIdMap.get(dangerName);
            if (null == dangerDicId){
                throw new AdcDaBaseException("风险等级 " + dangerName + " 不存在,请检查！");
            }
            researchProjectEO.setProjectStatusDicId(statusDicId);
            researchProjectEO.setProjectLevelDicId(levelDicId);
            researchProjectEO.setExtInfo2(dangerDicId);

            researchProjectEO.setCreateTime(new Date());
            researchProjectEO.setUpdateTime(new Date());
            researchProjectEO.setCreateUserId(userEO.getUsid());
            researchProjectEO.setCreateUserName(userEO.getUsname());
        }
        dao.insertList(padOperationManageEOList);
    }

    public int updateByPrimaryKeySelective(ResearchProjectEO t) throws Exception {
        List<ResearchProjectEO> researchProjectEOList = dao.queryByList(new ResearchProjectEOPage());

        for(ResearchProjectEO researchProjectEO : researchProjectEOList){
            if(StringUtils.equals(researchProjectEO.getResearchProjectId(),t.getResearchProjectId())
                    && !StringUtils.equals(researchProjectEO.getId(),t.getId())){
                throw new AdcDaBaseException("科研项目id " + t.getResearchProjectId() + "填写与已有记录冲突,请检查！");
            }
        }

        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        String statusName = editName(t.getProjectStatusDicName());
        if(StringUtils.isNotEmpty(statusName)) {
            Map<String, String> statusDicNameIdMap = getStatusDicNameIdMap();
            String statusDicId = statusDicNameIdMap.get(statusName);
            if (null == statusDicId) {
                throw new AdcDaBaseException("项目状态 " + statusName + " 不存在,请检查！");
            }
            t.setProjectStatusDicName(statusName);
            t.setProjectStatusDicId(statusDicId);
        }

        String levelName = editName(t.getProjectLevelDicName());
        if(StringUtils.isNotEmpty(levelName)) {
            Map<String, String> levelDicNameIdMap = getLevelDicNameIdMap();
            String levelDicId = levelDicNameIdMap.get(levelName);
            if (null == levelDicId) {
                throw new AdcDaBaseException("项目级别 " + levelName + " 不存在,请检查！");
            }
            t.setProjectLevelDicName(levelName);
            t.setProjectLevelDicId(levelDicId);
        }
        String dangerName = t.getExtInfo1();
        if(StringUtils.isNotEmpty(dangerName)) {
            Map<String, String> dangerDicNameIdMap  =  getDangerNameIdMap();
            String dangerDicId = dangerDicNameIdMap.get(dangerName);
            if (null == dangerDicId) {
                throw new AdcDaBaseException("风险等级 " + dangerName + " 不存在,请检查！");
            }
            t.setExtInfo2(dangerDicId);
        }

        t.setUpdateTime(new Date());
        t.setUpdateUserId(userEO.getUsid());
        t.setUpdateUserName(userEO.getUsname());
        return this.getDao().updateByPrimaryKeySelective(t);
    }

    public int insertSelective(ResearchProjectEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        List<ResearchProjectEO> researchProjectEOList = dao.queryByList(new ResearchProjectEOPage());
        Set<String> existResearchIdSet = new HashSet<>();
        for(ResearchProjectEO researchProjectEO : researchProjectEOList){
            existResearchIdSet.add(researchProjectEO.getResearchProjectId());
        }
        if (existResearchIdSet.contains(t.getResearchProjectId())){
            throw new AdcDaBaseException("科研项目id " + t.getResearchProjectId() + "填写与已有记录冲突,请检查！");
        }
        t.setId(UUID.randomUUID10());
        t.setCreateTime(new Date());
        t.setUpdateTime(new Date());
        t.setCreateUserId(userEO.getUsid());
        t.setCreateUserName(userEO.getUsname());
        String statusName = editName(t.getProjectStatusDicName());
        String levelName = editName(t.getProjectLevelDicName());

        Map<String, String> levelDicNameIdMap  =  getLevelDicNameIdMap();
        Map<String, String> statusDicNameIdMap  =  getStatusDicNameIdMap();

        String statusDicId = statusDicNameIdMap.get(statusName);
        if (null == statusDicId){
            throw new AdcDaBaseException("项目状态 " + statusName + " 不存在,请检查！");
        }
        String levelDicId = levelDicNameIdMap.get(levelName);
        if (null == levelDicId){
            throw new AdcDaBaseException("项目级别 " + levelName + " 不存在,请检查！");
        }

        String dangerName = t.getExtInfo1();
        if(StringUtils.isNotEmpty(dangerName)) {
            Map<String, String> dangerDicNameIdMap  =  getDangerNameIdMap();
            String dangerDicId = dangerDicNameIdMap.get(dangerName);
            if (null == dangerDicId) {
                throw new AdcDaBaseException("风险等级 " + dangerName + " 不存在,请检查！");
            }
            t.setExtInfo2(dangerDicId);
        }


        t.setProjectStatusDicName(statusName);
        t.setProjectLevelDicName(levelName);
        t.setProjectStatusDicId(statusDicId);
        t.setProjectLevelDicId(levelDicId);

        return this.getDao().insertSelective(t);
    }

    private String editName(String name){
        if (StringUtils.isEmpty(name)){
            name = "";
        }
        name = name.replace("(","（");
        name = name.replace(")","）");
        return name;
    }

    public int updateByPrimaryKey(ResearchProjectEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        t.setUpdateTime(new Date());
        t.setUpdateUserId(userEO.getUsid());
        t.setUpdateUserName(userEO.getUsname());
        return this.getDao().updateByPrimaryKey(t);
    }

    /**
     * 导出
     *
     * @param page
     * @return
     */
    public Workbook excelExport(ResearchProjectEOPage page) {
        List<ResearchProjectEO> researchProjectEOList = dao.queryByList(page);
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        return ExcelExportUtil.exportExcel(params, ResearchProjectEO.class, researchProjectEOList);
    }

    public Map<String, String> getOrgNameIdMap(List<OrgEO> orgEOList) {
        Map<String, String> nameIdMap = new HashMap<>();
        for (OrgEO orgEO : orgEOList) {
            nameIdMap.put(orgEO.getName(), orgEO.getId());
        }
        return nameIdMap;
    }



    public Map<String, String> getDicNameIdMap(List<DicTypeEO> dicTypeEOList) {
        Map<String, String> nameIdMap = new HashMap<>();
        for (DicTypeEO dicTypeEO : dicTypeEOList) {
            nameIdMap.put(dicTypeEO.getDicTypeName(), dicTypeEO.getId());
        }
        return nameIdMap;
    }
    public void deleteLogicInBatch(List<String> list) throws Exception{
        List<ResearchProjectEO> researchProjectEOList=dao.queryByIdList(list);
        List<String> researchProjectIdList=new ArrayList<>();
        for(ResearchProjectEO researchProjectEO:researchProjectEOList){
            researchProjectIdList.add(researchProjectEO.getResearchProjectId());
        }
        List<String> prohectNameList=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(researchProjectIdList)){
            List<ResProArriveFeeEO> resProArriveFeeEOList=resProArriveFeeEODao.selectByResearchProjectIdList(researchProjectIdList);
            if(CollectionUtils.isNotEmpty(resProArriveFeeEOList)){
                for(ResProArriveFeeEO resProArriveFeeEO:resProArriveFeeEOList){
                    for(ResearchProjectEO researchProjectEO:researchProjectEOList){
                        if(StringUtils.equals(researchProjectEO.getResearchProjectId(),resProArriveFeeEO.getResearchProjectId())
                                &&!prohectNameList.contains(researchProjectEO.getProjectName())){
                            prohectNameList.add(researchProjectEO.getProjectName());
                        }
                    }
                }

                throw new AdcDaBaseException("项目名称："+prohectNameList.toString()+"下有关联项目到账信息，删除失败");
            }
            List<ResProFeeDetailEO> resProFeeDetailEOList=resProFeeDetailEODao.selectByResearchProjectIdList(researchProjectIdList);
            if(CollectionUtils.isNotEmpty(resProFeeDetailEOList)){
                for(ResProFeeDetailEO resProFeeDetailEO:resProFeeDetailEOList){
                    for(ResearchProjectEO researchProjectEO:researchProjectEOList){
                        if(StringUtils.equals(researchProjectEO.getResearchProjectId(),resProFeeDetailEO.getResearchProjectId())
                                &&!prohectNameList.contains(researchProjectEO.getProjectName())){
                            prohectNameList.add(researchProjectEO.getProjectName());
                        }

                    }
                    throw new AdcDaBaseException("项目名称："+prohectNameList.toString()+"下有关联项目资金信息，删除失败");
                }
            }
        }

        if(CollectionUtils.isEmpty(list)){
            return;
        }
        dao.deleteLogicInBatch(list);
    }

    public void deleteLogicAll(){
        int resProArrFeeCount = resProArriveFeeEODao.queryByCount(new ResProArriveFeeEOPage());
        int resProFeeDetailCount = resProFeeDetailEODao.queryByCount(new ResProFeeDetailEOPage());
        if (resProArrFeeCount > 0){
            throw new AdcDaBaseException("删除全部失败，科研项目到账表存在记录，请检查！");
        }
        if (resProFeeDetailCount > 0){
            throw new AdcDaBaseException("删除全部失败，科研资金一览表存在记录，请检查！");
        }
        dao.deleteLogicAll();
    }

    public List<String> getResearchProjectIdList(){
        return dao.getResearchProjectIdList();
    }


    public List<ResearchProjectEO> queryListByDangerLevel(String level){
        return dao.queryListByDangerLevel(level);
    }

}
