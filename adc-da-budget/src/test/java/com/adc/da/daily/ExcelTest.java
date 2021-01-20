package com.adc.da.daily;

import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.params.ExcelExportEntity;
import com.adc.da.sys.entity.UserEO;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelTest {
    @Test
    public void dynaCol() {
        try {
            List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
            ExcelExportEntity colEntity = new ExcelExportEntity("商品名称", "title");
            colList.add(colEntity);

            colEntity = new ExcelExportEntity("供应商", "supplier");
            colList.add(colEntity);

            ExcelExportEntity deliColGroup = new ExcelExportEntity("得力", "deli");
            List<ExcelExportEntity> deliColList = new ArrayList<ExcelExportEntity>();
            deliColList.add(new ExcelExportEntity("市场价", "orgPrice"));
            deliColList.add(new ExcelExportEntity("专区价", "salePrice"));
            deliColGroup.setList(deliColList);
            colList.add(deliColGroup);

            ExcelExportEntity jdColGroup = new ExcelExportEntity("京东", "jd");
            List<ExcelExportEntity> jdColList = new ArrayList<ExcelExportEntity>();
            jdColList.add(new ExcelExportEntity("市场价", "orgPrice"));
            jdColList.add(new ExcelExportEntity("专区价", "salePrice"));
            jdColGroup.setList(jdColList);
            colList.add(jdColGroup);


            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> valMap = new HashMap<String, Object>();
                valMap.put("title", "名称." + i);
                valMap.put("supplier", "供应商." + i);

                List<Map<String, Object>> deliDetailList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < 3; j++) {
                    Map<String, Object> deliValMap = new HashMap<String, Object>();
                    deliValMap.put("orgPrice", "得力.市场价." + j);
                    deliValMap.put("salePrice", "得力.专区价." + j);
                    deliDetailList.add(deliValMap);
                }
                valMap.put("deli", deliDetailList);

                List<Map<String, Object>> jdDetailList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < 2; j++) {
                    Map<String, Object> jdValMap = new HashMap<String, Object>();
                    jdValMap.put("orgPrice", "京东.市场价." + j);
                    jdValMap.put("salePrice", "京东.专区价." + j);
                    jdDetailList.add(jdValMap);
                }
                valMap.put("jd", jdDetailList);

                list.add(valMap);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("价格分析表", "数据"), colList,
                    list);
            FileOutputStream fos = new FileOutputStream("D:/价格分析表.tt.xls");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dynaDaily() {
        try {
            List<UserEO> userEOList = new ArrayList<>();
            for (int i = 0 ; i < 15 ; i ++ ){
                UserEO userEO = new UserEO();
                userEO.setUsid("key"+"id"+i);
                userEO.setUsname("列名"+i);
                userEOList.add(userEO);
            }
            List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
            for (UserEO userEO : userEOList) {
                ExcelExportEntity colEntity = new ExcelExportEntity(userEO.getUsname(), userEO.getUsid());
                colList.add(colEntity);
            }


            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> valMap = new HashMap<String, Object>();
                valMap.put("key"+"id"+i, "数值"+i);
                list.add(valMap);
            }
            ExportParams exportParams = new ExportParams("价格分析表1", "数据1");
            exportParams.setStyle(MyStyle.class);
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, colList,list);
            FileOutputStream fos = new FileOutputStream("D:/价格分析表1.tt.xls");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
