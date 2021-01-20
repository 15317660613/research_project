package com.adc.da.research.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.page.BasePage;
import com.adc.da.base.page.Pager;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.dao.ResearchProjectEODao;
import com.adc.da.research.entity.ResProArriveFeeEO;
import com.adc.da.research.entity.ResearchProjectEO;
import com.adc.da.research.page.ResProFeeDetailEOPage;
import com.adc.da.research.page.ResearchProjectEOPage;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.page.DicTypeEOPage;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.util.ExcelReaderUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.twelvemonkeys.lang.StringUtil;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.ResProFeeDetailEODao;
import com.adc.da.research.entity.ResProFeeDetailEO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;


/**
 *
 * <br>
 * <b>功能：</b>DB_RES_PRO_FEE_DETAIL ResProFeeDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("resProFeeDetailEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResProFeeDetailEOService extends BaseService<ResProFeeDetailEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResProFeeDetailEOService.class);

    @Autowired
    private ResProFeeDetailEODao dao;

    @Autowired
    private DicTypeEOService dicTypeEOService;

    @Autowired
    ResearchProjectEOService researchProjectEOService ;

    @Autowired
    private ResearchProjectEODao researchProjectEODao;

    public ResProFeeDetailEODao getDao() {
        return dao;
    }

    public void excelImport(InputStream is) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Map<Integer,List<String>> rowNumValueStrListMap = ExcelReaderUtil.readExcel(is,0,2,
                0,22);

        List<ResProFeeDetailEO> resProFeeDetailEOList = checkAndInsert(rowNumValueStrListMap);

        if (CollectionUtils.isEmpty(resProFeeDetailEOList)) {
            throw new AdcDaBaseException("导入数据集为空，请检查！");
        }
        Map<String,String> nameIdMap = researchProjectEOService.getDicNameIdMap("RES_DB_FEE_TYPE");
        Map<String,String> researchProjectIdAndProjectNOMap = getResearchProjectIdAndProjectNOMap();

        //导入
        for (ResProFeeDetailEO resProFeeDetailEO : resProFeeDetailEOList) {
            resProFeeDetailEO.setId(UUID.randomUUID10());

            if (researchProjectIdAndProjectNOMap.containsKey(resProFeeDetailEO.getResearchProjectId())){
                String projectNo = researchProjectIdAndProjectNOMap.get(resProFeeDetailEO.getResearchProjectId());
                if(!StringUtils.equals(projectNo,resProFeeDetailEO.getProjectNo())){
                    throw new AdcDaBaseException("科研项目id " + resProFeeDetailEO.getResearchProjectId() + "在项目概况一览中的项目编号为"+projectNo+",请检查！");
                }
            }else {
                throw new AdcDaBaseException("科研项目id " + resProFeeDetailEO.getResearchProjectId() + "在项目概况一览中不存在,请检查！");
            }

            String fundsType =editName(resProFeeDetailEO.getFundsType());
            String fundsTypeDicId = nameIdMap.get(fundsType);
            resProFeeDetailEO.setFundsType(fundsType);
            resProFeeDetailEO.setFundsTypeId(fundsTypeDicId);
            doCheck(resProFeeDetailEO);
            resProFeeDetailEO.setCreateTime(new Date());
            resProFeeDetailEO.setUpdateTime(new Date());
            resProFeeDetailEO.setCreateUserId(userEO.getUsid());
            resProFeeDetailEO.setCreateUserName(userEO.getUsname());
        }
        dao.insertList(resProFeeDetailEOList);
    }

    public void doCheck(ResProFeeDetailEO resProFeeDetailEO){
        if (StringUtil.isEmpty(resProFeeDetailEO.getFundsType())){
            throw new AdcDaBaseException("费用类型ID不能为空,请检查！");
        }
        if (null == resProFeeDetailEO.getFundsTypeId()){
            throw new AdcDaBaseException("费用类型ID " + resProFeeDetailEO.getFundsType() + " 不存在,请检查！");
        }
        if("国拨费用（预算）自筹费用（预算）".contains(resProFeeDetailEO.getFundsType())){
            if(null != resProFeeDetailEO.getFundsYear() || null != resProFeeDetailEO.getFundsMonth()) {
                throw new AdcDaBaseException("费用类型ID为 " + resProFeeDetailEO.getFundsType() + " 的不可填写年、月,请检查！");
            }
        }

        if("国拨费用（使用）国拨费用（预算）".contains(resProFeeDetailEO.getFundsType())){
            if(null != resProFeeDetailEO.getOtherFee()) {
                throw new AdcDaBaseException("费用类型ID为 " + resProFeeDetailEO.getFundsType() + " 的不可填写其他费用,请检查！");
            }
        }

        if("国拨费用（使用）自筹费用（使用）".contains(resProFeeDetailEO.getFundsType())){
            if(null == resProFeeDetailEO.getFundsYear() || null == resProFeeDetailEO.getFundsMonth()) {
                throw new AdcDaBaseException("费用类型ID为 " + resProFeeDetailEO.getFundsType() + " 的必须填写年、月,请检查！");
            }
        }
//        if("国拨费用（使用）自筹费用（使用）".contains(resProFeeDetailEO.getFundsType())){
//            if(null == resProFeeDetailEO.getOtherFee()) {
//                throw new AdcDaBaseException("费用类型ID为 " + resProFeeDetailEO.getFundsType() + " 的必须填写其他费用,请检查！");
//            }
//        }
    }


    private  List<ResProFeeDetailEO> checkAndInsert(Map<Integer, List<String>> rowNumValueStrListMap) {
        if (CollectionUtils.isEmpty(rowNumValueStrListMap)) {
            throw new AdcDaBaseException("首行数据科研项目ID为空或导入数据集为空，请检查！");
        }

        List<ResProFeeDetailEO> resProFeeDetailEOList = new ArrayList<>();
        List<Integer> errorIndexList = new ArrayList<>();
        // 如果校验不通过，返回错误信息
        for (Map.Entry<Integer, List<String>> entry : rowNumValueStrListMap.entrySet()) {

            boolean dataErrorFlag = false;
            //boolean nullYearFlag = false;
            int rowIndex = entry.getKey();
            List<String> valueStrList = entry.getValue();
            ResProFeeDetailEO resProFeeDetailEO = new ResProFeeDetailEO();
            //科研项目ID
            if (StringUtils.isNotEmpty(valueStrList.get(0))) {
                String id = valueStrList.get(0);
                if (id.indexOf('.')>-1){
                    id = id.substring(0,id.indexOf('.'));
                }
                resProFeeDetailEO.setResearchProjectId(id);
            } else {
                dataErrorFlag = true;
            }
            //科研项目编号
            if (StringUtils.isNotEmpty(valueStrList.get(1))) {
                resProFeeDetailEO.setProjectNo(valueStrList.get(1));
            } else {
                dataErrorFlag = true;
            }
            //费用类型ID
            if (StringUtils.isNotEmpty(valueStrList.get(2))) {
                resProFeeDetailEO.setFundsType(editName(valueStrList.get(2)));
            } else {
                dataErrorFlag = true;
            }
            //年份
            if (StringUtils.isNotEmpty(valueStrList.get(3))) {
                String value = valueStrList.get(3);
                if (value.contains(".")) {
                    resProFeeDetailEO.setFundsYear(Integer.valueOf(value.substring(0, value.indexOf("."))));
                }else {
                    resProFeeDetailEO.setFundsYear(Integer.valueOf(value));
                }
            } else {
               // nullYearFlag= true;
                resProFeeDetailEO.setFundsYear(null);
            }
            //月份
            if (StringUtils.isNotEmpty(valueStrList.get(4))) {
                String value = valueStrList.get(4);
                if (value.contains(".")) {
                    resProFeeDetailEO.setFundsMonth(Integer.valueOf(value.substring(0, value.indexOf("."))));
                }else {
                    resProFeeDetailEO.setFundsMonth(Integer.valueOf(value));
                }
//                if (nullYearFlag){
//                    throw new AdcDaBaseException("第 "+rowIndex+" 年、月必须同为空，或者同不为空！");
//                }
            } else {
//                if (!nullYearFlag){
//                    throw new AdcDaBaseException("第 "+rowIndex+" 年、月必须同为空，或者同不为空！");
//                }
                resProFeeDetailEO.setFundsYear(null);
            }
            //购置设备费
            if (StringUtils.isNotEmpty(valueStrList.get(5))) {
                if (isDecimal(valueStrList.get(5)) && validate(new BigDecimal(valueStrList.get(5)),9,6)) {
                    resProFeeDetailEO.setEquipPurchaseFee(new BigDecimal(valueStrList.get(5)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //试制设备费
            if (StringUtils.isNotEmpty(valueStrList.get(6))) {
                if (isDecimal(valueStrList.get(6)) && validate(new BigDecimal(valueStrList.get(6)),9,6)) {
                    resProFeeDetailEO.setEquipTestCreateFee(new BigDecimal(valueStrList.get(6)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //设备租赁费
            if (StringUtils.isNotEmpty(valueStrList.get(7))) {
                if (isDecimal(valueStrList.get(7)) && validate(new BigDecimal(valueStrList.get(7)),9,6)) {
                    resProFeeDetailEO.setEquipRentFee(new BigDecimal(valueStrList.get(7)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //材料费
            if (StringUtils.isNotEmpty(valueStrList.get(8))) {
                if (isDecimal(valueStrList.get(8)) && validate(new BigDecimal(valueStrList.get(8)),9,6)) {
                    resProFeeDetailEO.setMaterialFee(new BigDecimal(valueStrList.get(8)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //测试化验加工费
            if (StringUtils.isNotEmpty(valueStrList.get(9))) {
                if (isDecimal(valueStrList.get(9)) && validate(new BigDecimal(valueStrList.get(9)),9,6)) {
                    resProFeeDetailEO.setTestProcessFee(new BigDecimal(valueStrList.get(9)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;

                }
            }

            //燃料动力费
            if (StringUtils.isNotEmpty(valueStrList.get(10))) {
                if (isDecimal(valueStrList.get(10)) && validate(new BigDecimal(valueStrList.get(10)),9,6)) {
                    resProFeeDetailEO.setFuelPowerFee(new BigDecimal(valueStrList.get(10)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //差旅费
            if (StringUtils.isNotEmpty(valueStrList.get(11))) {
                if (isDecimal(valueStrList.get(11)) && validate(new BigDecimal(valueStrList.get(11)),9,6)) {
                    resProFeeDetailEO.setTravelFee(new BigDecimal(valueStrList.get(11)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //会议费
            if (StringUtils.isNotEmpty(valueStrList.get(12))) {
                if (isDecimal(valueStrList.get(12)) && validate(new BigDecimal(valueStrList.get(12)),9,6)) {
                    resProFeeDetailEO.setMeetingFee(new BigDecimal(valueStrList.get(12)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //国际合作交流费
            if (StringUtils.isNotEmpty(valueStrList.get(13))) {
                if (isDecimal(valueStrList.get(13)) && validate(new BigDecimal(valueStrList.get(13)),9,6)) {
                    resProFeeDetailEO.setInternationExchangeFee(new BigDecimal(valueStrList.get(13)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //软件购置费
            if (StringUtils.isNotEmpty(valueStrList.get(14))) {
                if (isDecimal(valueStrList.get(14)) && validate(new BigDecimal(valueStrList.get(14)),9,6)) {
                    resProFeeDetailEO.setkSoftFee(new BigDecimal(valueStrList.get(14)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }

            //除软件购置费之外的其他费用
            if (StringUtils.isNotEmpty(valueStrList.get(15))) {
                if (isDecimal(valueStrList.get(15)) && validate(new BigDecimal(valueStrList.get(15)),9,6)) {
                    resProFeeDetailEO.setkOtherFee(new BigDecimal(valueStrList.get(15)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            // 劳务费
            if (StringUtils.isNotEmpty(valueStrList.get(16))) {
                if (isDecimal(valueStrList.get(16)) && validate(new BigDecimal(valueStrList.get(16)),9,6)) {
                    resProFeeDetailEO.setLaborFee(new BigDecimal(valueStrList.get(16)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //专家咨询费
            if (StringUtils.isNotEmpty(valueStrList.get(17))) {
                if (isDecimal(valueStrList.get(17)) && validate(new BigDecimal(valueStrList.get(17)),9,6)) {
                    resProFeeDetailEO.setExpertConsultFee(new BigDecimal(valueStrList.get(17)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            //外协费
            if (StringUtils.isNotEmpty(valueStrList.get(18))) {
                if (isDecimal(valueStrList.get(18)) && validate(new BigDecimal(valueStrList.get(18)),9,6)) {
                    resProFeeDetailEO.setExternalAssistFee(new BigDecimal(valueStrList.get(18)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }

            //人员费
            if (StringUtils.isNotEmpty(valueStrList.get(19))) {
                if (isDecimal(valueStrList.get(19)) && validate(new BigDecimal(valueStrList.get(19)),9,6)) {
                    resProFeeDetailEO.setPersonFee(new BigDecimal(valueStrList.get(19)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }

            //管理费
            if (StringUtils.isNotEmpty(valueStrList.get(20))) {
                if (isDecimal(valueStrList.get(20)) && validate(new BigDecimal(valueStrList.get(20)),9,6)) {
                    resProFeeDetailEO.setManageFee(new BigDecimal(valueStrList.get(20)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }

            if (StringUtils.isNotEmpty(valueStrList.get(21))) {
                if (isDecimal(valueStrList.get(21)) && validate(new BigDecimal(valueStrList.get(21)),9,6)) {
                    resProFeeDetailEO.setOtherFee(new BigDecimal(valueStrList.get(21)));
                }else {
                    //errorIndexList.add(rowIndex);
                    dataErrorFlag = true;
                }
            }
            if (dataErrorFlag){
                errorIndexList.add(rowIndex);
            }

            resProFeeDetailEOList.add(resProFeeDetailEO);
        }
        if (CollectionUtils.isNotEmpty(errorIndexList)) {
            StringBuilder sb = new StringBuilder();
            sb.append("第 ");
            sb.append(StringUtils.join(errorIndexList, '、'));
            sb.append(" 行数据不合法，请检查！");
            logger.error("excel校验不通过！" + sb.toString());
            throw new AdcDaBaseException(sb.toString());
        }

        return resProFeeDetailEOList;
    }

    private String editName(String name){
        if (StringUtils.isEmpty(name)){
            name = "";
        }
        name = name.replace("(","（");
        name = name.replace(")","）");
        return name;
    }
    public static  boolean validate(BigDecimal bigDecimal, int intLength, int floatLength){
        if (bigDecimal.compareTo(BigDecimal.ZERO) == 0){
            return true;
        }
        String data = bigDecimal.toString();
        if (data.indexOf(".")<0){
            return true;
        }
        int intLen = data.substring(0,data.indexOf(".")).length();
        int floatLen = data.substring(data.indexOf(".")+1,data.length()).length();
        if(intLen > intLength){
            return false;
        }
        return intLen <= intLength && floatLen <= floatLength ;
    }

    public static boolean isDecimal(String str) {
        if(str==null || "".equals(str))
            return false;
        java.util.regex.Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        System.out.println(isDecimal("9。9"));
    }

    /**
     * 导出
     *
     * @param page
     * @return
     */
    public Workbook excelExport(ResProFeeDetailEOPage page) {
        List<ResProFeeDetailEO> resProFeeDetailEOList = dao.queryByList(page);
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        return ExcelExportUtil.exportExcel(params, ResProFeeDetailEO.class, resProFeeDetailEOList);
    }

    public int insertSelective(ResProFeeDetailEO t) throws Exception {
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
        Map<String,String> nameIdMap = researchProjectEOService.getDicNameIdMap("RES_DB_FEE_TYPE");
        String fundsType =editName(t.getFundsType());
        String fundsTypeDicId = nameIdMap.get(fundsType);
        t.setFundsType(fundsType);
        t.setFundsTypeId(fundsTypeDicId);
        doCheck(t);
        t.setId(UUID.randomUUID10());
        t.setCreateTime(new Date());
        t.setUpdateTime(new Date());
        t.setCreateUserId(userEO.getUsid());
        t.setCreateUserName(userEO.getUsname());
        return this.getDao().insertSelective(t);
    }

    public int updateByPrimaryKeySelective(ResProFeeDetailEO t) throws Exception {
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

        String fundsType =editName(t.getFundsType());

        Map<String, String> nameIdMap = researchProjectEOService.getDicNameIdMap("RES_DB_FEE_TYPE");
        String fundsTypeDicId = nameIdMap.get(fundsType);
        t.setFundsType(fundsType);
        t.setFundsTypeId(fundsTypeDicId);
        doCheck(t);

        t.setUpdateTime(new Date());
        t.setUpdateUserId(userEO.getUsid());
        t.setUpdateUserName(userEO.getUsname());
        return this.getDao().updateByPrimaryKeySelective(t);
    }

    public void deleteLogicInBatch(List<String> list){
        dao.deleteLogicInBatch(list);
    }

    public void deleteLogicAll(){
        dao.deleteLogicAll();
    }

    public List<ResProFeeDetailEO> queryByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        List<ResProFeeDetailEO> rows = this.getDao().queryByPage(page);
        Map<String,String> researchIdNameMap =   getResearchIdNameMap(rows);
        for (ResProFeeDetailEO eo : rows){
            String name = researchIdNameMap.get(eo.getResearchProjectId());
            eo.setProjectName(name);
        }
        return rows;
    }


    private Map<String,String> getResearchIdNameMap(List<ResProFeeDetailEO> eoList){
        Set<String> researchIdSet = new HashSet<>();
        for (ResProFeeDetailEO eo : eoList){
            if (StringUtils.isNotEmpty(eo.getResearchProjectId())) {
                researchIdSet.add(eo.getResearchProjectId());
            }
        }
        if(CollectionUtils.isEmpty(researchIdSet)){
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


}
