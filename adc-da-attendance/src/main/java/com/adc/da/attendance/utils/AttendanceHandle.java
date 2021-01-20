package com.adc.da.attendance.utils;


import com.adc.da.attendance.entity.AttendanceInfo;
import com.adc.da.attendance.entity.ExportDateExcleDTO;
import com.adc.da.attendance.entity.WorkDateEO;
import com.adc.da.attendance.service.WorkDateService;
import com.adc.da.attendance.utils.excel.ExcelUitls;
import com.adc.da.util.utils.StringUtils;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class AttendanceHandle {

    @Autowired
    private WorkDateService workDateService;
    //考勤表起始时间
    private  Date startDate;
    //考勤表结束时间
    private  Date endDate;
    //excle表中的考勤信息
    private List<AttendanceInfo> attendanceInfoList;

    private static Date Eight_Time;
    private static Date Twen_Time;
    private static Date Five_Time;
    private static Date Eleven_Time;

    public AttendanceHandle(File file) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        String eightTime = "08:00";
        String twenTime = "00:00";
        String fiveTime = "17:00";
        String eleven = "23:59";
        this.Eight_Time =  sf.parse(eightTime);
        this.Twen_Time =  sf.parse(twenTime);
        this.Eleven_Time =  sf.parse(eleven);
        this.Five_Time =  sf.parse(fiveTime);
        Workbook workBook = ExcelUitls.getWorkBook(file);
        Sheet sheet = workBook.getSheetAt(2);
        int lastRowNum = sheet.getLastRowNum();

        //获取到起始时间及终止时间
        String dateStr = this.getCellContent(sheet, 2, 2);
        //获取到起始时间及终止时间
        String[] date = dateStr.split("~");
        //起始时间
        this.startDate = DateUtils.parseDate(date[0].trim(),"yyyy-MM-dd");
        //终止时间
        this.endDate = DateUtils.parseDate(date[1].trim(),"yyyy-MM-dd");
       //临时存储
        List<AttendanceInfo> tmpLst = new ArrayList<>();
        for (int i=4;i<lastRowNum;i+=2){
            AttendanceInfo userAttendanceInfo = this.getUserAttendanceInfo(sheet, i, i+1);
            tmpLst.add(userAttendanceInfo);
        }
        this.attendanceInfoList =tmpLst;
        this.judgeExceptionData();
    }


    //获取指定单元格的内容
    public  String getCellContent(Sheet sheet,int row,int col){
        if(sheet == null){
            return null;
        }
        Cell cell = sheet.getRow(row).getCell(col);
        String stringCellValue = cell.getStringCellValue();
        return stringCellValue;
    }

    /**
     * 返回一个用户的考勤信息
     * @param sheet 当前sheet
     * @param userLine 用户信息所在行数
     *  @param userLine 考勤信息所在行数
     * @return
     */
    public AttendanceInfo getUserAttendanceInfo(Sheet sheet, int userLine, int attendInfoLine){

        if(sheet == null){
            return null;
        }
        //用户信息行
        Row userRow = sheet.getRow(userLine);
        //考勤信息行
        Row attRow = sheet.getRow(attendInfoLine);
        //获取有效行数
        int lastCellNum = attRow.getLastCellNum();
        AttendanceInfo attendanceInfo = new AttendanceInfo();
        //填充属性
        attendanceInfo.setWorkId(userRow.getCell(2).getStringCellValue());
        attendanceInfo.setHumanName(userRow.getCell(10).getStringCellValue());
        attendanceInfo.setDept(userRow.getCell(20).getStringCellValue());
        //存放对应日期的打开记录
        HashMap<Date, String> attendanceTimeInfo = new HashMap<>();
        Date startdate = this.getStartDate();
        for (int i=0;i<lastCellNum;i++){

            //根据起始时间来给每条信息来赋予打卡日期，避免使用表头拼接字符串
            if(i!=0){
                startdate = DateUtils.addDays(startdate,1);
            }
            if(StringUtils.isNotEmpty(attRow.getCell(i).getStringCellValue())){
                StringBuilder  times = new StringBuilder ();
                String cardTime = attRow.getCell(i).getStringCellValue();
                while(StringUtils.isNotEmpty(cardTime)){
                    String substring = cardTime.substring(0, 5);
                    times.append(substring+",");
                        cardTime = cardTime.substring(5);
                }
                times.deleteCharAt(times.length()-1);
                attendanceTimeInfo.put(startdate,""+times);
            }else{
                attendanceTimeInfo.put(startdate,null);
            }
        }
        attendanceInfo.setAttendanceTimeInfo(attendanceTimeInfo);
        return attendanceInfo;
    }

    public void judgeExceptionData() throws ParseException {

        for (AttendanceInfo attendanceInfo : this.attendanceInfoList) {
            Map<Date, String> ifExcep = new HashMap<>();
            Map<Date, String> attendanceTimeInfo = attendanceInfo.getAttendanceTimeInfo();
            for (Map.Entry<Date, String> entry : attendanceTimeInfo.entrySet()) {
                //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                String[] split = entry.getValue().split(",");
             //如果打卡为空，当天直接判断为缺卡
                if(StringUtils.isEmpty(entry.getValue())|| split.length<2){
                    ifExcep.put(entry.getKey(),"1");
                    attendanceInfo.setIsExcption(ifExcep);
                    continue;
                }
//                String[] split = entry.getValue().split(",");
//                //如果长度小于2，当天直接判断为缺卡
//                if(split.length<2){
//                    ifExcep.put(entry.getKey(),"1");
//                    attendanceInfo.setIsExcption(ifExcep);
//                    continue;
//                }
                //判断早上是否正常
                Boolean morningNormal = false;
                //判断下午是否正常
                Boolean dinnerNormal = false;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date modate = simpleDateFormat.parse(split[0]);
                Date dindate = simpleDateFormat.parse(split[split.length-1]);
                //判断 是否存在时间点早于早上八点
                if(DateUtils.earlier(modate,Eight_Time,DateUtils.ACCURACY_MINUTE) && DateUtils.later(modate,Twen_Time,DateUtils.ACCURACY_MINUTE)) {
                        morningNormal = true;}
                //判断 是否存在时间点晚于下午五点
                if(DateUtils.later(dindate,Five_Time,DateUtils.ACCURACY_MINUTE) && DateUtils.earlier(dindate,Eleven_Time,DateUtils.ACCURACY_MINUTE)) {
                       dinnerNormal = true;}
                //如果早上和下午都正常
                if(morningNormal && dinnerNormal){
                    ifExcep.put(entry.getKey(),"0");
                }else{
                    ifExcep.put(entry.getKey(),"1");
                }
                attendanceInfo.setIsExcption(ifExcep);

            }

        }

    }



}
