package com.adc.da.budget;

import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ImportTestData {

    public static void main(String[] args) {
        try {
//            importProjectTestData();
//            importTaskTestData();
            importDailyTestData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importProjectTestData() throws Exception {
        InputStream is = new FileInputStream(new File("C:\\Users\\Lenovo\\Documents\\1_importData\\导入数据_项目数据.xlsx"));
        ImportParams params = new ImportParams();
        List<Project> projectList = ExcelImportUtil.importExcel(is, Project.class, params);
        for (Project p : projectList) {
            String members = p.getProjectMemberNames();
            p.setProjectMemberIds(members.split("，"));
        }
    }

    public static void importTaskTestData() throws Exception {
        InputStream is = new FileInputStream(new File("C:\\Users\\Lenovo\\Documents\\1_importData\\导入数据_任务数据.xlsx"));
        ImportParams params = new ImportParams();
        List<Task> tasks = ExcelImportUtil.importExcel(is, Task.class, params);
        System.out.println(111);
    }

    public static void importDailyTestData() throws Exception {
        InputStream is = new FileInputStream(new File("C:\\Users\\Lenovo\\Documents\\1_importData\\导入数据_日报数据.xlsx"));
        ImportParams params = new ImportParams();
        List<Daily> dailies = ExcelImportUtil.importExcel(is, Daily.class, params);
        for (Daily daily : dailies) {
            daily.setTaskIdArray(daily.getTaskIds().split(("，")));
            daily.setTaskStatusArray(daily.getTaskIds().split(("，")));
        }
        System.out.println(111);
    }

    @Test
    public void test() {

    }
}
