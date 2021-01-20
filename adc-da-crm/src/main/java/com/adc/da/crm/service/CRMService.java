package com.adc.da.crm.service;/**
 * @description
 * @author ZhengZhiwei
 * @date create at 13:13 2018/11/22
 * @modified by
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.CRMDao;
import com.adc.da.crm.page.AdcFormDataVOPage;
import com.adc.da.crm.entity.excel.BlockInfo;
import com.adc.da.crm.entity.excel.CompareInfo;
import com.adc.da.crm.entity.excel.ContractInfo;
import com.adc.da.crm.entity.excel.DeptInfo;
import com.adc.da.crm.entity.excel.GroupInfo;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.params.ExcelExportEntity;
import com.adc.da.form.entity.AdcFormDataEO;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author ZhengZhiwei
 * @date 2018-11-22
 */
@Service("crmService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CRMService extends BaseService<AdcFormDataEO, String> {
    @Autowired
    private CRMDao dao;

    @Override
    public BaseDao<AdcFormDataEO> getDao() {
        return this.dao;
    }

    public List<AdcFormDataEO> queryPageByFormId(AdcFormDataVOPage adcFormDataVOPage) {
        return dao.queryPageByFormId( adcFormDataVOPage );
    }

    /**
     * @author ZhengZhiwei
     * @describtion todo 根据表单id获取相应servic名称
     * @param
     * @return
     * @date create at 14:31 2018/11/29
     * @modified by
     */
    public String queryServiceNameByFormId(String formId){
        return dao.queryServiceNameByFormId( formId );
    }
    /**
     * @author ZHanghong
     * @describtion 导出报表1
     * @param
     * @return
     * @date create at 14:31 2018/12/3
     * @modified by
     */
	public List<ContractInfo> excelExport(String type) {
		List<Map> listmap=new ArrayList<>();
		if("1".equals(type)){
			listmap=dao.queryExport1();
		}else{
			listmap=dao.queryExport2();
		}
		//获取当年年份
		Calendar cale = Calendar.getInstance();  
        int year = cale.get(Calendar.YEAR);
        Map<String , Object> thisY= new HashMap<String , Object>();//当年完成
        Map<String , Object> forec= new HashMap<String , Object>();//目标
		List<ContractInfo> oneList = new ArrayList<>();
		for(Map m : listmap){
			if((year+"年").equals(m.get("Y")!=null?m.get("Y").toString():"0")&&"1".equals(m.get("TYPE").toString())){
				//当年实际用于计算
				thisY=m;
				/*//不显示当年实际完成情况
				ContractInfo c=new ContractInfo();
				c.setYear(m.get("Y").toString());
				c.setJanuary(m.get("M1").toString());
				c.setFebruary(m.get("M2").toString());
				c.setMarch(m.get("M3").toString());
				c.setApril(m.get("M4").toString());
				c.setMay(m.get("M5").toString());
				c.setJune(m.get("M6").toString());
				c.setJuly(m.get("M7").toString());
				c.setAugust(m.get("M8").toString());
				c.setSeptember(m.get("M9").toString());
				c.setOctober(m.get("M10").toString());
				c.setNovember(m.get("M11").toString());
				c.setDecember(m.get("M12").toString());
				c.setTotal(m.get("TT").toString());
				oneList.add(c);*/
			}else{
				if("2".equals(m.get("TYPE").toString())){
					//预测记录用于计算
					//forec=m;
					//没有预测时候默认值给1，防止被除出现异常
					forec.put("Y", m.get("Y").toString());
					forec.put("M1", "0".equals(m.get("M1").toString())?"1":m.get("M1").toString());
					forec.put("M2", "0".equals(m.get("M2").toString())?"1":m.get("M2").toString());
					forec.put("M3", "0".equals(m.get("M3").toString())?"1":m.get("M3").toString());
					forec.put("M4", "0".equals(m.get("M4").toString())?"1":m.get("M4").toString());
					forec.put("M5", "0".equals(m.get("M5").toString())?"1":m.get("M5").toString());
					forec.put("M6", "0".equals(m.get("M6").toString())?"1":m.get("M6").toString());
					forec.put("M7", "0".equals(m.get("M7").toString())?"1":m.get("M7").toString());
					forec.put("M8", "0".equals(m.get("M8").toString())?"1":m.get("M8").toString());
					forec.put("M9", "0".equals(m.get("M9").toString())?"1":m.get("M9").toString());
					forec.put("M10", "0".equals(m.get("M10").toString())?"1":m.get("M10").toString());
					forec.put("M11", "0".equals(m.get("M11").toString())?"1":m.get("M11").toString());
					forec.put("M12", "0".equals(m.get("M12").toString())?"1":m.get("M12").toString());
					forec.put("TT", "0".equals(m.get("TT").toString())?"1":m.get("TT").toString());
				}
				ContractInfo c=new ContractInfo();
				c.setYear(m.get("Y").toString());
				c.setJanuary(m.get("M1").toString());
				c.setFebruary(m.get("M2").toString());
				c.setMarch(m.get("M3").toString());
				c.setApril(m.get("M4").toString());
				c.setMay(m.get("M5").toString());
				c.setJune(m.get("M6").toString());
				c.setJuly(m.get("M7").toString());
				c.setAugust(m.get("M8").toString());
				c.setSeptember(m.get("M9").toString());
				c.setOctober(m.get("M10").toString());
				c.setNovember(m.get("M11").toString());
				c.setDecember(m.get("M12").toString());
				c.setTotal(m.get("TT").toString());
				oneList.add(c);
			}
		}
		ContractInfo finish= new ContractInfo();//完成率
		finish.setYear("预算执行率");
		finish.setJanuary(String.format("%.2f", (Double.valueOf(thisY.get("M1").toString())/Double.valueOf(forec.get("M1").toString()))*100)+"%");
		finish.setFebruary(String.format("%.2f", (Double.valueOf(thisY.get("M2").toString())/Double.valueOf(forec.get("M2").toString()))*100)+"%");
		finish.setMarch(String.format("%.2f", (Double.valueOf(thisY.get("M3").toString())/Double.valueOf(forec.get("M3").toString()))*100)+"%");
		finish.setApril(String.format("%.2f", (Double.valueOf(thisY.get("M4").toString())/Double.valueOf(forec.get("M4").toString()))*100)+"%");
		finish.setMay(String.format("%.2f", (Double.valueOf(thisY.get("M5").toString())/Double.valueOf(forec.get("M5").toString()))*100)+"%");
		finish.setJune(String.format("%.2f", (Double.valueOf(thisY.get("M6").toString())/Double.valueOf(forec.get("M6").toString()))*100)+"%");
		finish.setJuly(String.format("%.2f", (Double.valueOf(thisY.get("M7").toString())/Double.valueOf(forec.get("M7").toString()))*100)+"%");
		finish.setAugust(String.format("%.2f", (Double.valueOf(thisY.get("M8").toString())/Double.valueOf(forec.get("M8").toString()))*100)+"%");
		finish.setSeptember(String.format("%.2f", (Double.valueOf(thisY.get("M9").toString())/Double.valueOf(forec.get("M9").toString()))*100)+"%");
		finish.setOctober(String.format("%.2f", (Double.valueOf(thisY.get("M10").toString())/Double.valueOf(forec.get("M10").toString()))*100)+"%");
		finish.setNovember(String.format("%.2f", (Double.valueOf(thisY.get("M11").toString())/Double.valueOf(forec.get("M11").toString()))*100)+"%");
		finish.setDecember(String.format("%.2f", (Double.valueOf(thisY.get("M12").toString())/Double.valueOf(forec.get("M12").toString()))*100)+"%");
		finish.setTotal(String.format("%.2f", (Double.valueOf(thisY.get("TT").toString())/Double.valueOf(forec.get("TT").toString()))*100)+"%");
		oneList.add(finish);
//		Workbook  workbook = new XSSFWorkbook();
//		workbook.createSheet("first sheet");
//		workbook= ExcelExportUtil.exportExcel(params,ContractInfo.class,oneList);
//        return workbook;
		return oneList;
	}
	 /**
     * @author ZHanghong
     * @describtion 导出报表3,4
     * @param
     * @return
     * @date create at 14:31 2018/12/3
     * @modified by
     */
	public List<GroupInfo> excelExport3(String type) {
		List<Map> listmap=new ArrayList<>();
		if("3".equals(type)){
			listmap=dao.queryExport3();
		}else{
			listmap=dao.queryExport4();
		}
		List<GroupInfo> oneList = new ArrayList<>();
		for(Map m : listmap){
			GroupInfo g=new GroupInfo();
			g.setGroup(m.get("DN")==null?"-":m.get("DN").toString());
			g.setQ1th(m.get("Q1")==null?"0":m.get("Q1").toString());
			g.setQ2nd(m.get("Q2")==null?"0":m.get("Q2").toString());
			g.setQ3rd(m.get("Q3")==null?"0":m.get("Q3").toString());
			g.setQ4th(m.get("Q4")==null?"0":m.get("Q4").toString());
			g.setYear(m.get("Y")==null?"-":m.get("Y").toString());
			g.setTotal(m.get("TT")==null?"":m.get("TT").toString());
			oneList.add(g);
		} 
//		Workbook  workbook = new XSSFWorkbook();
//		workbook.createSheet("first sheet");
//		workbook= ExcelExportUtil.exportExcel(params,GroupInfo.class,oneList);
//        return workbook;
		return oneList;
	}
	 /**
     * @author ZHanghong
     * @describtion 导出报表5,6
     * @param
     * @return
     * @date create at 14:31 2018/12/3
     * @modified by
     */
	public List<BlockInfo> excelExport5(String type) {
		List<Map> listmap=new ArrayList<>();
		if("5".equals(type)){
			listmap=dao.queryExport5();
		}else{
			listmap=dao.queryExport6();
		}
		List<BlockInfo> oneList = new ArrayList<>();
		List<BlockInfo> twoList = new ArrayList<>();
		Set<String> ySet=new HashSet<String>();
		for(Map m : listmap){
			BlockInfo g=new BlockInfo();
			g.setBlock(m.get("DN")==null?"-":m.get("DN").toString());
			g.setQ1th(m.get("Q1")==null?"0":m.get("Q1").toString());
			g.setQ2nd(m.get("Q2")==null?"0":m.get("Q2").toString());
			g.setQ3rd(m.get("Q3")==null?"0":m.get("Q3").toString());
			g.setQ4th(m.get("Q4")==null?"0":m.get("Q4").toString());
			g.setYear(m.get("YEAR1")==null?"-":m.get("YEAR1").toString());
			ySet.add(m.get("YEAR1")==null?"-":m.get("YEAR1").toString());
			g.setTotal(m.get("TT")==null?"0":m.get("TT").toString());
			oneList.add(g);
		} 
		for (String year : ySet) {
			BlockInfo bTotal= new BlockInfo();
			Double q1th = 0.0, q2nd=0.0, q3rd= 0.0, q4th= 0.0, total= 0.0;
		      for(BlockInfo b:oneList){
		    	  if(year.endsWith(b.getYear())){
		    		  q1th += Double.parseDouble(b.getQ1th());
		    		  q2nd += Double.parseDouble(b.getQ2nd());
		    		  q3rd += Double.parseDouble(b.getQ3rd());
		    		  q4th += Double.parseDouble(b.getQ4th());
		    		  total += Double.parseDouble(b.getTotal());
		    	  }
		      }
		    bTotal.setBlock("总计");
		    bTotal.setYear(year);
		    bTotal.setQ1th(q1th.toString());
	    	bTotal.setQ2nd(q2nd.toString());
	    	bTotal.setQ3rd(q3rd.toString());
	    	bTotal.setQ4th(q4th.toString());
	    	bTotal.setTotal(total.toString());
		    twoList.add(bTotal);
		} 
		oneList.addAll(twoList);
//		Workbook  workbook = new XSSFWorkbook();
//		workbook.createSheet("first sheet");
//		workbook= ExcelExportUtil.exportExcel(params,BlockInfo.class,oneList);
        return oneList;
        
	}
	 /**
     * @author ZHanghong
     * @describtion 导出报表7,8
     * @param
     * @return
     * @date create at 14:31 2018/12/3
     * @modified by
     */
	public List<DeptInfo> excelExport7(String type) {
		List<Map> listmap=new ArrayList<>();
		if("7".equals(type)){
			listmap=dao.queryExport7();
		}else{
			listmap=dao.queryExport8();
		}
		List<DeptInfo> oneList = new ArrayList<>();
		List<DeptInfo> twoList = new ArrayList<>();
		Set<String> ySet=new HashSet<String>();
		for(Map m : listmap){
			DeptInfo g=new DeptInfo();
			g.setDept(m.get("DN")==null?"-":m.get("DN").toString());
			g.setQ1th(m.get("Q1")==null?"0":m.get("Q1").toString());
			g.setQ2nd(m.get("Q2")==null?"0":m.get("Q2").toString());
			g.setQ3rd(m.get("Q3")==null?"0":m.get("Q3").toString());
			g.setQ4th(m.get("Q4")==null?"0":m.get("Q4").toString());
			g.setYear(m.get("YEAR1")==null?"-":m.get("YEAR1").toString());
			ySet.add(m.get("YEAR1")==null?"-":m.get("YEAR1").toString());
			g.setTotal(m.get("TT")==null?"0":m.get("TT").toString());
			oneList.add(g);
		} 
		for (String year : ySet) {
			DeptInfo bTotal= new DeptInfo();
			Double q1th = 0.0, q2nd=0.0, q3rd= 0.0, q4th= 0.0, total= 0.0;
		      for(DeptInfo b:oneList){
		    	  if(year.endsWith(b.getYear())){
		    		  q1th += Double.parseDouble(b.getQ1th());
		    		  q2nd += Double.parseDouble(b.getQ2nd());
		    		  q3rd += Double.parseDouble(b.getQ3rd());
		    		  q4th += Double.parseDouble(b.getQ4th());
		    		  total += Double.parseDouble(b.getTotal());
		    	  }
		      }
		    bTotal.setDept("总计");
		    bTotal.setYear(year);
		    bTotal.setQ1th(q1th.toString());
	    	bTotal.setQ2nd(q2nd.toString());
	    	bTotal.setQ3rd(q3rd.toString());
	    	bTotal.setQ4th(q4th.toString());
	    	bTotal.setTotal(total.toString());
		    twoList.add(bTotal);
		} 
		oneList.addAll(twoList);
		
		//workbook.createSheet("first sheet");
		
        return oneList;
        
	}
	public Map<String ,Object>  excelExport9(String month,String year) {
		if(Integer.parseInt(month)<10){
			//如果月份小于10，作为参数需补0
			month="0"+month;
		}
		//所需全部数据
		List<Map> listmap=new ArrayList<>();
		listmap=dao.queryExport9(month,year);
		//表头
        List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
        //本部列
		ExcelExportEntity colEntity = new ExcelExportEntity("本部", "benbu");
		colEntity.setMergeVertical(true);
		colList.add(colEntity);
		//部门列
		colEntity = new ExcelExportEntity("部门", "bumen");
		//colEntity.setMergeVertical(true);
		colList.add(colEntity);
		//合同额列
		ExcelExportEntity ht1Group = new ExcelExportEntity("合同额"+year+"年", "hte1");
		List<ExcelExportEntity> hteList = new ArrayList<>();
		hteList.add(new ExcelExportEntity(month+"月", "m1"));
		hteList.add(new ExcelExportEntity("累计", "tt1"));
		ht1Group.setList(hteList);
		//ht1Group.setStatistics(true);
		colList.add(ht1Group);
		ExcelExportEntity ht2Group = new ExcelExportEntity("合同额"+(Integer.parseInt(year)-1)+"年", "hte2");
		List<ExcelExportEntity> ht2List = new ArrayList<ExcelExportEntity>();
		ht2List.add(new ExcelExportEntity(month+"月", "m2"));
		ht2List.add(new ExcelExportEntity("累计", "tt2"));
		ht2Group.setList(ht2List);
		//ht2Group.setStatistics(true);
		colList.add(ht2Group);
		
		colEntity = new ExcelExportEntity("合同额01-"+month+"月对比", "compC");
		//colEntity.setNeedMerge(true);
		colList.add(colEntity);
		
		ExcelExportEntity kpGroup1 = new ExcelExportEntity("开票额"+year+"年", "kpe1");
		List<ExcelExportEntity> kpList1 = new ArrayList<ExcelExportEntity>();
		kpList1.add(new ExcelExportEntity(month+"月", "m3"));
		kpList1.add(new ExcelExportEntity("累计", "tt3"));
		kpGroup1.setList(kpList1);
		//kpGroup1.setStatistics(true);
		colList.add(kpGroup1);
		ExcelExportEntity kpGroup2 = new ExcelExportEntity("开票额"+(Integer.parseInt(year)-1)+"年", "kpe2");
		List<ExcelExportEntity> kpList2 = new ArrayList<ExcelExportEntity>();
		kpList2.add(new ExcelExportEntity(month+"月", "m4"));
		kpList2.add(new ExcelExportEntity("累计", "tt4"));
		kpGroup2.setList(kpList2);
		//kpGroup2.setStatistics(true);
		colList.add(kpGroup2);
		colEntity = new ExcelExportEntity("开票额01-"+month+"月对比", "compK");
		//colEntity.setNeedMerge(true);
		colList.add(colEntity);
		
		//获取所有本部
		Set<String> benBuSet=new HashSet<String>();
		for(Map m :listmap){
			benBuSet.add(m.get("BB").toString());
		}	
		
		List<Map<String, Object>> listall = new ArrayList<Map<String, Object>>();
		//总计
		Map<String, Object> zjMap = new HashMap<String, Object>();
		//部门
		zjMap.put("bumen", "总计");
		double m1zj=0.0,tt1zj=0.0,m2zj=0.0,tt2zj=0.0,compCzj=0.0,m3zj=0.0,tt3zj=0.0,m4zj=0.0,tt4zj=0.0,compKzj=0.0;
		//按本部进行外层循环，用于统计本部数据
		for(String benB :benBuSet){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			//本部统计
			Map<String, Object> ttMap = new HashMap<String, Object>();
			//本部
			ttMap.put("benbu",benB);
			//部门
			ttMap.put("bumen", "合计");
			double m1=0.0,tt1=0.0,m2=0.0,tt2=0.0,compC=0.0,m3=0.0,tt3=0.0,m4=0.0,tt4=0.0,compK=0.0;
			for (Map m :listmap) {
				//只统计当前外层循环到的本部
				if(benB.equals(m.get("BB").toString())){
					Map<String, Object> valMap = new HashMap<String, Object>();
					//本部
					valMap.put("benbu",benB);
					//部门
					valMap.put("bumen", m.get("BM").toString());
					//合同额2018年
					List<Map<String, Object>> deliDetailList1 = new ArrayList<Map<String, Object>>();
					Map<String, Object> deliValMap = new HashMap<String, Object>();
					deliValMap.put("m1", m.get("MO1")==null?0.0:Double.parseDouble(m.get("MO1").toString()));
					deliValMap.put("tt1",m.get("TTO1")==null?0.0:Double.parseDouble(m.get("TTO1").toString()));
					deliDetailList1.add(deliValMap);
					m1+=Double.parseDouble(deliValMap.get("m1").toString());//本部统计
					tt1+=Double.parseDouble(deliValMap.get("tt1").toString());//本部统计
					valMap.put("hte1", deliDetailList1);
					//合同额2017年
					List<Map<String, Object>> deliDetailList2 = new ArrayList<Map<String, Object>>();
					Map<String, Object> deliValMap2 = new HashMap<String, Object>();
					deliValMap2.put("m2", m.get("MO2")==null?0.0:Double.parseDouble(m.get("MO2").toString()));
					deliValMap2.put("tt2", m.get("TTO2")==null?0.0:Double.parseDouble(m.get("TTO2").toString()));
					m2+=Double.parseDouble(deliValMap2.get("m2").toString());//本部统计
					tt2+=Double.parseDouble(deliValMap2.get("tt2").toString());//本部统计
					deliDetailList2.add(deliValMap2);
					valMap.put("hte2", deliDetailList2);
					//合同额对比
					double bumenTt1=Double.parseDouble(deliValMap.get("tt1").toString());//今年累计
					double bumenTt2=Double.parseDouble(deliValMap2.get("tt2").toString());//去年累计
//					valMap.put("compC", String.format("%.2f", (bumenTt1/(bumenTt2==0?1:bumenTt2))*100)+"%");
					valMap.put("compC", String.format("%.2f", (bumenTt1/(bumenTt2==0?1:bumenTt2))));
					
					//开票额2018年
					List<Map<String, Object>> jdDetailList = new ArrayList<Map<String, Object>>();
					Map<String, Object> jdValMap = new HashMap<String, Object>();
					jdValMap.put("m3",  m.get("MI3")==null?0.0:Double.parseDouble(m.get("MI3").toString()));
					jdValMap.put("tt3",  m.get("TTI3")==null?0.0:Double.parseDouble(m.get("TTI3").toString()));
					m3+=Double.parseDouble(jdValMap.get("m3").toString());//本部统计
					tt3+=Double.parseDouble(jdValMap.get("tt3").toString());//本部统计
					jdDetailList.add(jdValMap);
					valMap.put("kpe1", jdDetailList);
					//开票额2017年
					List<Map<String, Object>> jdDetailList2 = new ArrayList<Map<String, Object>>();
					Map<String, Object> jdValMap2 = new HashMap<String, Object>();
					jdValMap2.put("m4", m.get("MI4")==null?0.0:Double.parseDouble(m.get("MI4").toString()));
					jdValMap2.put("tt4",  m.get("TTI4")==null?0.0:Double.parseDouble(m.get("TTI4").toString()));
					m4+=Double.parseDouble(jdValMap2.get("m4").toString());//本部统计
					tt4+=Double.parseDouble(jdValMap2.get("tt4").toString());//本部统计
					jdDetailList2.add(jdValMap2);
					valMap.put("kpe2", jdDetailList2);
					//开票额对比
					double bumenTt3=Double.parseDouble(jdValMap.get("tt3").toString());//今年累计
					double bumenTt4=Double.parseDouble(jdValMap2.get("tt4").toString());//去年累计
//					valMap.put("compK", String.format("%.2f", (bumenTt3/(bumenTt4==0?1:bumenTt4))*100)+"%");
					valMap.put("compK", String.format("%.2f", (bumenTt3/(bumenTt4==0?1:bumenTt4))));
					list.add(valMap);
				}
			}
			//合计合同额2018年
			List<Map<String, Object>> hejiList1 = new ArrayList<Map<String, Object>>();
			Map<String, Object> hejiMap1 = new HashMap<String, Object>();
			hejiMap1.put("m1", m1);
			hejiMap1.put("tt1", tt1);
			hejiList1.add(hejiMap1);
			ttMap.put("hte1", hejiList1);
			//合计合同额2017年
			List<Map<String, Object>> hejiList2 = new ArrayList<Map<String, Object>>();
			Map<String, Object> hejiMap2 = new HashMap<String, Object>();
			hejiMap2.put("m2", m2);
			hejiMap2.put("tt2", tt2);
			hejiList2.add(hejiMap2);
			ttMap.put("hte2", hejiList2);
			//合计开票额2018年
			List<Map<String, Object>> hejiList3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> hejiMap3 = new HashMap<String, Object>();
			hejiMap3.put("m3", m3);
			hejiMap3.put("tt3", tt3);
			hejiList3.add(hejiMap3);
			ttMap.put("kpe1", hejiList3);
			//合计开票额2017年
			List<Map<String, Object>> hejiList4 = new ArrayList<Map<String, Object>>();
			Map<String, Object> hejiMap4 = new HashMap<String, Object>();
			hejiMap4.put("m4", m4);
			hejiMap4.put("tt4", tt4);
			hejiList4.add(hejiMap4);
			ttMap.put("kpe2", hejiList4);
			//合计合同额对比
			//ttMap.put("compC", String.format("%.2f", (tt1/(tt2==0?1:tt2))*100)+"%");
			ttMap.put("compC", String.format("%.2f", (tt1/(tt2==0?1:tt2))));
			//合计开票额对比
//			ttMap.put("compK", String.format("%.2f", (tt3/(tt4==0?1:tt4))*100)+"%");
			ttMap.put("compK", String.format("%.2f", (tt3/(tt4==0?1:tt4))));
			list.add(ttMap);
			listall.addAll(list);
			//总计计数
			m1zj+=m1;
			m2zj+=m2;
			m3zj+=m3;
			m4zj+=m4;
			tt1zj+=tt1;
			tt2zj+=tt2;
			tt3zj+=tt3;
			tt4zj+=tt4;
		}
		//总计合同额2018年
		List<Map<String, Object>> zjList1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> zjMap1 = new HashMap<String, Object>();
		zjMap1.put("m1", m1zj);
		zjMap1.put("tt1", tt1zj);
		zjList1.add(zjMap1);
		zjMap.put("hte1", zjList1);
		//总计合同额2017年
		List<Map<String, Object>> zjList2 = new ArrayList<Map<String, Object>>();
		Map<String, Object> zjMap2 = new HashMap<String, Object>();
		zjMap2.put("m2", m2zj);
		zjMap2.put("tt2", tt2zj);
		zjList2.add(zjMap2);
		zjMap.put("hte2", zjList2);
		//总计开票额2018年
		List<Map<String, Object>> zjList3 = new ArrayList<Map<String, Object>>();
		Map<String, Object> zjMap3 = new HashMap<String, Object>();
		zjMap3.put("m3", m3zj);
		zjMap3.put("tt3", tt3zj);
		zjList3.add(zjMap3);
		zjMap.put("kpe1", zjList3);
		//总计开票额2017年
		List<Map<String, Object>> zjList4 = new ArrayList<Map<String, Object>>();
		Map<String, Object> zjMap4 = new HashMap<String, Object>();
		zjMap4.put("m4", m4zj);
		zjMap4.put("tt4", tt4zj);
		zjList4.add(zjMap4);
		zjMap.put("kpe2", zjList4);
		//合计合同额对比
		zjMap.put("compC", String.format("%.2f", (tt1zj/(tt2zj==0?1:tt2zj))));
		//合计开票额对比 
		zjMap.put("compK", String.format("%.2f", (tt3zj/(tt4zj==0?1:tt4zj))));
		listall.add(zjMap);
		Map<String ,Object> returnMap=new HashMap<>(); 
		List returnList=new ArrayList<>();
		returnList.addAll(listall);
		returnMap.put("returnList", returnList);
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("合同开票同期对比", "数据"), colList,
				listall);
		returnMap.put("workbook", workbook);
        return returnMap;
    }
	/**
     * @author ZHanghong
     * @describtion 导出报表10
     * @param
     * @return
     * @date create at 14:31 2018/12/3
     * @modified by
     */
	public List<CompareInfo> excelExport10(String month,String year) {
		List<Map> listmap=new ArrayList<>();
		listmap=dao.queryExport10(month,year);
		 
		List<CompareInfo> oneList = new ArrayList<>();

		//获取所有本部
		Set<String> benBuSet=new HashSet<String>();
		for(Map m :listmap){
			benBuSet.add(m.get("BB").toString());
		}	
		CompareInfo Tt=new CompareInfo();
		Tt.setBenbu("");
		Tt.setBumen("总计");
		double t3=0.0,w3=0.0,h3=0.0;
		for(String bb : benBuSet){
			CompareInfo cTt=new CompareInfo();
			cTt.setBenbu(bb);
			cTt.setBumen("小计");
			double t2=0.0,w2=0.0,h2=0.0;
			for (Map m :listmap) {
				if(bb.equals(m.get("BB").toString())){					
					CompareInfo c=new CompareInfo();
					c.setBenbu(bb);
					c.setBumen(m.get("BM").toString());
					c.setContractThisYear("");
					double t1=Double.parseDouble(m.get("THISY")==null?"0":m.get("THISY").toString()),
							w1=Double.parseDouble(m.get("WEIKUAN")==null?"0":m.get("WEIKUAN").toString()),
							h1=Double.parseDouble(m.get("HEJI")==null?"0":m.get("HEJI").toString());
					c.setContractTotal(getTwoDecimal(t1));
					c.setNopay(getTwoDecimal(w1));
					c.setTotal(getTwoDecimal(h1));
					t2+=t1;
					w2+=w1;
					h2+=h1;
					oneList.add(c);
				}
			}
			cTt.setContractTotal(getTwoDecimal(t2));
			cTt.setNopay(getTwoDecimal(w2));
			cTt.setTotal(getTwoDecimal(h2));
			t3+=t2;
			w3+=w2;
			h3+=h2;
			oneList.add(cTt);
		} 
		Tt.setContractTotal(getTwoDecimal(t3));
		Tt.setNopay(getTwoDecimal(w3));
		Tt.setTotal(getTwoDecimal(h3));
		oneList.add(Tt);
		 
		//workbook.createSheet("first sheet");
		
        return oneList;
        
	}
	
	/**
     * @author ZHanghong
     * @describtion 转万元保留两位
     * @param
     * @return
     * @date create at 14:31 2018/12/
     * @modified by
     */
	private double getTwoDecimal(double num) {
		DecimalFormat dFormat = new DecimalFormat("#.00");
        String s = dFormat.format(num/10000);
        Double temp = Double.valueOf(s);
        return temp;
   }

}
