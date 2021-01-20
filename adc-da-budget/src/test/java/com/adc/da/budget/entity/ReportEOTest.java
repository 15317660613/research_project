package com.adc.da.budget.entity;

import com.adc.da.budget.utils.NewExcelImportUtil;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.excel.poi.excel.entity.params.ExcelExportEntity;
import com.adc.da.excel.poi.excel.entity.params.ExcelImportEntity;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * ReportEO Tester.
 *
 * @author JunyuMei
 * @version 1.0
 * @since <pre>$now</pre>
 */
public class ReportEOTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCol01()
     */
    public void testGetCol01() throws Exception {
        /**
         * 数据存储结构， 二维数组，第二维维String数组
         */
        //构造测试数据
        String[][] data = new String[2][7];
        data[0] = new String[]{"项目", "201701-08", "201801-08", "同期对比", "预算完成率", "2017年度", "2018年预算", "fsdfdsf"};
        data[1] = new String[]{"收入", "300", "200", "100", "50", "30", "10", "1211"};

        // 构造动态导出列
        List<ExcelExportEntity> beanList = new ArrayList<ExcelExportEntity>();
        for(int i=0;i < data[0].length;i++){
            beanList .add(new ExcelExportEntity(String.valueOf(i), String.valueOf(i)));
        }
        // 设置导出参数为07版本Excel
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        params.setCreateHeadRows(false);
        // 构造数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i< data.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            for(int j=0; j< data[0].length;j++){
                map.put(String.valueOf(j), data[i][j]);
            }
            list.add(map);
        }
        // 导出数据
        Workbook workbook = ExcelExportUtil.exportExcel(params, beanList, list);
        File f = new File("C:\\Users\\Lenovo\\Documents\\1.xlsx");
        f.createNewFile();
        workbook.write(new FileOutputStream(f));
    }

    /**
     * Method: setCol01(String col01)
     */
    public void testSetCol01() throws Exception {
        File f = new File("C:\\Users\\Lenovo\\Documents\\1.xlsx");
        List<String[]> beanList = NewExcelImportUtil.importExcel(new FileInputStream(f));
        System.out.println("success");
    }

    /**
     * Method: getCol02()
     */
    @Test
    public void testGetCol02() throws Exception {

    }

    /**
     * Method: setCol02(String col02)
     */
    @Test
    public void testSetCol02() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol03()
     */
    @Test
    public void testGetCol03() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol03(String col03)
     */
    @Test
    public void testSetCol03() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol04()
     */
    @Test
    public void testGetCol04() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol04(String col04)
     */
    @Test
    public void testSetCol04() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol05()
     */
    @Test
    public void testGetCol05() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol05(String col05)
     */
    @Test
    public void testSetCol05() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol06()
     */
    @Test
    public void testGetCol06() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol06(String col06)
     */
    @Test
    public void testSetCol06() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol07()
     */
    @Test
    public void testGetCol07() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol07(String col07)
     */
    @Test
    public void testSetCol07() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol08()
     */
    @Test
    public void testGetCol08() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol08(String col08)
     */
    @Test
    public void testSetCol08() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol09()
     */
    @Test
    public void testGetCol09() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol09(String col09)
     */
    @Test
    public void testSetCol09() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol10()
     */
    @Test
    public void testGetCol10() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol10(String col10)
     */
    @Test
    public void testSetCol10() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol11()
     */
    @Test
    public void testGetCol11() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol11(String col11)
     */
    @Test
    public void testSetCol11() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol12()
     */
    @Test
    public void testGetCol12() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol12(String col12)
     */
    @Test
    public void testSetCol12() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol13()
     */
    @Test
    public void testGetCol13() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol13(String col13)
     */
    @Test
    public void testSetCol13() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol14()
     */
    @Test
    public void testGetCol14() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol14(String col14)
     */
    @Test
    public void testSetCol14() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol15()
     */
    @Test
    public void testGetCol15() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol15(String col15)
     */
    @Test
    public void testSetCol15() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol16()
     */
    @Test
    public void testGetCol16() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol16(String col16)
     */
    @Test
    public void testSetCol16() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol17()
     */
    @Test
    public void testGetCol17() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol17(String col17)
     */
    @Test
    public void testSetCol17() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol18()
     */
    @Test
    public void testGetCol18() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol18(String col18)
     */
    @Test
    public void testSetCol18() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol19()
     */
    @Test
    public void testGetCol19() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol19(String col19)
     */
    @Test
    public void testSetCol19() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol20()
     */
    @Test
    public void testGetCol20() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol20(String col20)
     */
    @Test
    public void testSetCol20() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol21()
     */
    @Test
    public void testGetCol21() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol21(String col21)
     */
    @Test
    public void testSetCol21() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCol22()
     */
    @Test
    public void testGetCol22() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setCol22(String col22)
     */
    @Test
    public void testSetCol22() throws Exception {
//TODO: Test goes here... 
    }


} 
