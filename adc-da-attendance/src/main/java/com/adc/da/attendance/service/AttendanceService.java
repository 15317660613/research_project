package com.adc.da.attendance.service;

import com.adc.da.attendance.dao.AttendanceInfoEODao;
import com.adc.da.attendance.dao.MAttendanceInfoEODao;
import com.adc.da.attendance.entity.*;
import com.adc.da.attendance.utils.AttendanceHandle;

import com.adc.da.attendance.utils.DateUtil;
import com.adc.da.attendance.utils.ZipUtils;
import com.adc.da.attendance.utils.excel.ExcelUitls;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.*;


@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    @Autowired
    private AttendanceInfoEODao dao;
    @Autowired
    private MAttendanceInfoEODao mdao;
    @Autowired
    private WorkDateService workDateService;

    public AttendanceInfoEODao getDao() {
        return dao;
    }

//    public Object AttendanceImport(MultipartFile file)  {
//        try{
//            // 获取文件名
//            String fileName = file.getOriginalFilename();
//            // 获取文件后缀
//            String prefix = fileName.substring(fileName.lastIndexOf("."));
//            // 用uuid作为文件名，防止生成的临时文件重复
//            File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
//            // MultipartFile to File
//            file.transferTo(excelFile);
//
//            //业务逻辑
//            File[] unzip = ZipUtils.unzip(excelFile, "/attendceinfo");
//            List<AttendanceInfo> arr = new ArrayList<>();
//
//
//            for (File file1 : unzip) {
//                AttendanceHandle attendanceHandle = new AttendanceHandle(file1);
//                arr.addAll(attendanceHandle.getAttendanceInfoList());
//            }
//            for  (AttendanceInfo attendanceInfo : arr) {
//
//                Map<Date, String> attendanceTimeInfo = attendanceInfo.getAttendanceTimeInfo();
//                for (Map.Entry<Date, String> entry : attendanceTimeInfo.entrySet()) {
//                    AttendanceInfoEO attendanceInfoEO =new AttendanceInfoEO();
//                    attendanceInfoEO.setId(UUID.randomUUID().toString().replace("-", ""));
//                    attendanceInfoEO.setAttendance(entry.getKey());
//                    attendanceInfoEO.setAttendanceTime(entry.getValue());
//                    attendanceInfoEO.setCreatedTime(new Date());
//                    attendanceInfoEO.setModifiedTime(attendanceInfoEO.getCreatedTime());
//                    attendanceInfoEO.setHumanName(attendanceInfo.getHumanName());
//                    attendanceInfoEO.setDept(attendanceInfo.getDept());
//                    attendanceInfoEO.setWorkId(attendanceInfo.getWorkId());
//                    attendanceInfoEO.setIsdel("0");
//                    attendanceInfoEO.setIsexcept(attendanceInfo.getIsExcption().get(entry.getKey()));
//                    if(mdao.existsAttendanceInfo(attendanceInfoEO)>0){
//                        continue;
//                    }
//                    dao.insertSelective(attendanceInfoEO);
//
//                }
//            }
//
//
//
//            //程序结束时，删除临时文件
//            deleteFile(excelFile);
//            for (File unzipfile: unzip) {
//                deleteFile(unzipfile);
//            }
//            return arr;
//
//        }catch (Exception e){
//            throw new AdcDaBaseException("导入失败");
//        }
//
//
//
//    }

    public Workbook AttendanceImport(MultipartFile file)  {
        try{
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            // 用uuid作为文件名，防止生成的临时文件重复
            File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
            // MultipartFile to File
            file.transferTo(excelFile);

            //业务逻辑
            File[] unzip = ZipUtils.unzip(excelFile, "/attendceinfo");
            List<AttendanceInfo> arr = new ArrayList<>();

            ExportExcleDTO exportExcleDTO = new ExportExcleDTO();
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            exportExcleDTO.setParams(params);
            exportExcleDTO.setFileName("考勤表异常统计表");
            //保证只有一次赋值
            Integer flag= 0;
            for (File file1 : unzip) {
                AttendanceHandle attendanceHandle = new AttendanceHandle(file1);
                arr.addAll(attendanceHandle.getAttendanceInfoList());
                if(flag==0){
                    exportExcleDTO.setStartDate(DateUtils.dateToString(attendanceHandle.getStartDate(),"yyyy-MM-dd"));
                    exportExcleDTO.setEndDate(DateUtils.dateToString(attendanceHandle.getEndDate(),"yyyy-MM-dd"));
                }
            }
            ArrayList<AttendanceInfoEO> attendanceInfoEOList = new ArrayList<>();


            for  (AttendanceInfo attendanceInfo : arr) {

                Map<Date, String> attendanceTimeInfo = attendanceInfo.getAttendanceTimeInfo();
                for (Map.Entry<Date, String> entry : attendanceTimeInfo.entrySet()) {
                    AttendanceInfoEO attendanceInfoEO =new AttendanceInfoEO();

                //    attendanceInfoEO.setId(UUID.randomUUID().toString().replace("-", ""));
                    attendanceInfoEO.setAttendance(entry.getKey());
                    attendanceInfoEO.setAttendanceTime(entry.getValue());

                    attendanceInfoEO.setHumanName(attendanceInfo.getHumanName());
                    attendanceInfoEO.setDept(attendanceInfo.getDept());
                    attendanceInfoEO.setWorkId(attendanceInfo.getWorkId());
                    attendanceInfoEO.setIsexcept(attendanceInfo.getIsExcption().get(entry.getKey()));
                    attendanceInfoEOList.add(attendanceInfoEO);
//                    if(mdao.existsAttendanceInfo(attendanceInfoEO)>0){
//                        continue;
//                    }
//                    dao.insertSelective(attendanceInfoEO);

                }
            }
            List<AttendanceInfoEO> uniqueattendanceInfoEOList= new ArrayList<AttendanceInfoEO>( );
            for (AttendanceInfoEO str : attendanceInfoEOList) {
                if (!uniqueattendanceInfoEOList.contains(str)) {
                    uniqueattendanceInfoEOList.add(str);
                }
            }
            attendanceInfoEOList.clear();
            attendanceInfoEOList.addAll(uniqueattendanceInfoEOList);

            Workbook sheets = attendanceExport(exportExcleDTO, attendanceInfoEOList);

            //程序结束时，删除临时文件
            deleteFile(excelFile);
            for (File unzipfile: unzip) {
                deleteFile(unzipfile);
            }
            return sheets;

        }catch (Exception e){
            throw new AdcDaBaseException("导入失败");
        }



    }

    public Workbook attendanceExport(ExportExcleDTO exportExcleDTO,List<AttendanceInfoEO>  attendanceInfoEOSLst) {
        if (StringUtils.isEmpty(exportExcleDTO.getStartDate())) {
            throw new AdcDaBaseException("起始时间不能为空");
        }
        if (StringUtils.isEmpty(exportExcleDTO.getEndDate())) {
            throw new AdcDaBaseException("终止时间不能为空");
        }


        Date startDate = DateUtils.stringToDate(exportExcleDTO.getStartDate(), "yyyy-MM-dd");
        Date endDate = DateUtils.stringToDate(exportExcleDTO.getEndDate(), "yyyy-MM-dd");
        ExportDateExcleDTO exportDateExcleDTO = new ExportDateExcleDTO();
        exportDateExcleDTO.setEndDate(endDate);
        exportDateExcleDTO.setStartDate(startDate);
        List<WorkDateEO> workDate = workDateService.getWorkDate(exportDateExcleDTO);
        List<Date> workDateLst = new ArrayList<>();
        for (WorkDateEO workDateEO : workDate) {
            workDateLst.add(workDateEO.getDateTime());
        }

        exportDateExcleDTO.setWorkDateLst(workDateLst);
//        List<AttendanceInfoEO> attendanceInfoEOSLst;
//        try {
//            attendanceInfoEOSLst = mdao.selectAllList(exportDateExcleDTO);
//        } catch (Exception e) {
//            throw new AdcDaBaseException("查询数据库");
//        }
        Map<Integer, AttendanceInfoExcelEO> resultMap = new TreeMap<>();
        for (AttendanceInfoEO attendanceInfoEO : attendanceInfoEOSLst) {
            if(resultMap.get(Integer.valueOf(attendanceInfoEO.getWorkId()))==null){
                AttendanceInfoExcelEO attendanceInfoExcelEO = new AttendanceInfoExcelEO();
                attendanceInfoExcelEO.setWorkId(attendanceInfoEO.getWorkId());
                attendanceInfoExcelEO.setHumanName(attendanceInfoEO.getHumanName());
                Map<String, TimeExcptionDTO> timeMap = new HashMap<>();
                TimeExcptionDTO timeExcptionDTO = new TimeExcptionDTO();
                if(workDateLst.contains(attendanceInfoEO.getAttendance())){
                    timeExcptionDTO.setIsException(attendanceInfoEO.getIsexcept());
                }else{
                    timeExcptionDTO.setIsException("0");
                }
                timeExcptionDTO.setTime(attendanceInfoEO.getAttendanceTime());
                timeMap.put(DateUtils.dateToString(attendanceInfoEO.getAttendance(),"yyyy-MM-dd"), timeExcptionDTO);
                attendanceInfoExcelEO.setAttendanceTime(timeMap);
                resultMap.put(Integer.valueOf(attendanceInfoEO.getWorkId()),attendanceInfoExcelEO);
            }else{

                TimeExcptionDTO timeExcptionDTO = new TimeExcptionDTO();
                if(workDateLst.contains(attendanceInfoEO.getAttendance())){
                    timeExcptionDTO.setIsException(attendanceInfoEO.getIsexcept());
                }else{
                    timeExcptionDTO.setIsException("0");
                }
                timeExcptionDTO.setTime(attendanceInfoEO.getAttendanceTime());
                resultMap.get(Integer.valueOf(attendanceInfoEO.getWorkId())).getAttendanceTime().put(DateUtils.dateToString(attendanceInfoEO.getAttendance(),"yyyy-MM-dd"),timeExcptionDTO);
            }
        }

        if (resultMap == null || resultMap.size() == 0) { throw new AdcDaBaseException("没有导出数据！"); }


        try {
            HSSFWorkbook sheets = ExcelUitls.exportExcle(resultMap, startDate, endDate,workDateLst);
            return sheets;
        } catch (Exception e) {
            throw new AdcDaBaseException("excle格式异常");
        }

    }


    /**
     * 删除
     *
     * @param files
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                boolean bool = file.delete();
                logger.info("file.delete" + bool);
            }
        }
    }

}
