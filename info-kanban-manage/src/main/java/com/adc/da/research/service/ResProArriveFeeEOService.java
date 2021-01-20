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
import com.adc.da.research.dao.ResearchProjectEODao;
import com.adc.da.research.entity.ResearchProjectEO;
import com.adc.da.research.handler.ResProArriveFeeEOHandler;
import com.adc.da.research.page.ResProArriveFeeEOPage;
import com.adc.da.research.page.ResearchProjectEOPage;
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
import com.adc.da.research.dao.ResProArriveFeeEODao;
import com.adc.da.research.entity.ResProArriveFeeEO;

import java.io.InputStream;
import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>DB_RES_PRO_ARRIVE_FEE ResProArriveFeeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("resProArriveFeeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResProArriveFeeEOService extends BaseService<ResProArriveFeeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResProArriveFeeEOService.class);

    @Autowired
    private ResProArriveFeeEODao dao;

    @Autowired
    private ResearchProjectEODao researchProjectEODao;

    public ResProArriveFeeEODao getDao() {
        return dao;
    }



    public void excelImport(InputStream is) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        ImportParams params = new ImportParams();
        params.setNeedVerfiy(true);
        params.setVerifyHandler(new ResProArriveFeeEOHandler());
        ExcelImportResult<ResProArriveFeeEO> result = ExcelImportUtil.importExcelMore(is, ResProArriveFeeEO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            List<Integer> rowIndexList = new ArrayList<>();
            List<ResProArriveFeeEO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (ResProArriveFeeEO error : errors) {
                rowIndexList.add(error.getRowNum() + 1);
            }
            sb.append("第 ");
            sb.append(StringUtils.join(rowIndexList, '、'));
            sb.append(" 行数据不合法，请检查！");
            logger.error("excel校验不通过！" + sb.toString());
            throw new AdcDaBaseException(sb.toString());
        }

        Map<String,String> researchProjectIdAndProjectNOMap = getResearchProjectIdAndProjectNOMap();

        List<ResProArriveFeeEO> resProArriveFeeEOList = result.getList();
        if (CollectionUtils.isEmpty(resProArriveFeeEOList)){
            throw new AdcDaBaseException("导入数据集为空，请检查！");
        }
        //导入
        for (ResProArriveFeeEO resProArriveFeeEO : resProArriveFeeEOList) {
            resProArriveFeeEO.setId(UUID.randomUUID10());
            if (researchProjectIdAndProjectNOMap.containsKey(resProArriveFeeEO.getResearchProjectId())){
                String projectNo = researchProjectIdAndProjectNOMap.get(resProArriveFeeEO.getResearchProjectId());
                if(!StringUtils.equals(projectNo,resProArriveFeeEO.getProjectNo())){
                    throw new AdcDaBaseException("科研项目id " + resProArriveFeeEO.getResearchProjectId() + "在项目概况一览中的项目编号为"+projectNo+",请检查！");
                }
            }else {
                throw new AdcDaBaseException("科研项目id " + resProArriveFeeEO.getResearchProjectId() + "在项目概况一览中不存在,请检查！");
            }
            resProArriveFeeEO.setCreateTime(new Date());
            resProArriveFeeEO.setUpdateTime(new Date());
            resProArriveFeeEO.setCreateUserId(userEO.getUsid());
            resProArriveFeeEO.setCreateUserName(userEO.getUsname());
        }
        dao.insertList(resProArriveFeeEOList);
    }

    /**
     * 导出
     *
     * @param page
     * @return
     */
    public Workbook excelExport(ResProArriveFeeEOPage page) {
        List<ResProArriveFeeEO> resProArriveFeeEOList = dao.queryByList(page);
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        return ExcelExportUtil.exportExcel(params, ResProArriveFeeEO.class, resProArriveFeeEOList);
    }

    public int insertSelective(ResProArriveFeeEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        Map<String,String> researchProjectIdAndProjectNOMap = getResearchProjectIdAndProjectNOMap();
        if (researchProjectIdAndProjectNOMap.containsKey(t.getResearchProjectId())){
            String projectNo = researchProjectIdAndProjectNOMap.get(t.getResearchProjectId());
            if(!StringUtils.equals(projectNo,t.getProjectNo())){
                throw new AdcDaBaseException("科研项目id " + t.getResearchProjectId() + "在项目概况一览中的项目编号为"+projectNo+",请检查！");
            }
        }else {
            throw new AdcDaBaseException("科研项目id " + t.getResearchProjectId() + "在项目概况一览中不存在,请检查！");
        }
        t.setId(UUID.randomUUID10());
        t.setCreateTime(new Date());
        t.setUpdateTime(new Date());
        t.setCreateUserId(userEO.getUsid());
        t.setCreateUserName(userEO.getUsname());
        return this.getDao().insertSelective(t);
    }

    public int updateByPrimaryKeySelective(ResProArriveFeeEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        Map<String,String> researchProjectIdAndProjectNOMap = getResearchProjectIdAndProjectNOMap();
        if (researchProjectIdAndProjectNOMap.containsKey(t.getResearchProjectId())){
            String projectNo = researchProjectIdAndProjectNOMap.get(t.getResearchProjectId());
            if(!StringUtils.equals(projectNo,t.getProjectNo())){
                throw new AdcDaBaseException("科研项目id " + t.getResearchProjectId() + "在项目概况一览中的项目编号为"+projectNo+",请检查！");
            }
        }else {
            throw new AdcDaBaseException("科研项目id " + t.getResearchProjectId() + "在项目概况一览中不存在,请检查！");
        }
        t.setUpdateTime(new Date());
        t.setUpdateUserId(userEO.getUsid());
        t.setUpdateUserName(userEO.getUsname());
        return this.getDao().updateByPrimaryKeySelective(t);
    }

    public List<ResProArriveFeeEO> queryByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        List<ResProArriveFeeEO> rows = this.getDao().queryByPage(page);
        Map<String,String> researchIdNameMap =   getResearchIdNameMap(rows);
        for (ResProArriveFeeEO eo : rows){
            String name = researchIdNameMap.get(eo.getResearchProjectId());
            eo.setProjectName(name);
        }
        return rows;
    }


    private Map<String,String> getResearchIdNameMap(List<ResProArriveFeeEO> resProArriveFeeEOList){
        Set<String> researchIdSet = new HashSet<>();
        for (ResProArriveFeeEO eo : resProArriveFeeEOList){
            if (StringUtils.isNotEmpty(eo.getResearchProjectId())) {
                researchIdSet.add(eo.getResearchProjectId());
            }
        }
        if (CollectionUtils.isEmpty(researchIdSet)){
            return new HashMap<>();
        }
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByResearchProjectIdList(new ArrayList<String>(researchIdSet));
        Map<String,String> researchIdNameMap= new HashMap<>();
        for (ResearchProjectEO researchProjectEO : researchProjectEOList){
            researchIdNameMap.put(researchProjectEO.getResearchProjectId(),researchProjectEO.getProjectName());
        }
        return researchIdNameMap;
    }


    public Map<String,String> getResearchProjectIdAndProjectNOMap(){
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(new ResearchProjectEOPage());
        Map<String,String> researchProjectIdAndProjectNOMap = new HashMap<>();
        for(ResearchProjectEO researchProjectEO : researchProjectEOList){
            researchProjectIdAndProjectNOMap.put(researchProjectEO.getResearchProjectId(),researchProjectEO.getProjectNo());
        }
        return researchProjectIdAndProjectNOMap;
    }



    public void deleteLogicInBatch(List<String> list){
        dao.deleteLogicInBatch(list);
    }
    public void deleteLogicAll(){
        dao.deleteLogicAll();
    }
}
