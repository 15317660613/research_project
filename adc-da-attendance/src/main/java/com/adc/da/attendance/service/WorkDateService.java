package com.adc.da.attendance.service;

import com.adc.da.attendance.dao.MWorkDateEODao;
import com.adc.da.attendance.dao.WorkDateEODao;
import com.adc.da.attendance.entity.*;
import com.adc.da.attendance.utils.DateUtil;
import com.adc.da.attendance.utils.HolidayUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WorkDateService {
    @Autowired
    private WorkDateEODao dao;
    @Autowired
    private MWorkDateEODao mworkdao;


    public Object HolidayForever(ExportExcleRealDTO exportExcleDTO) {
        Date startDate = DateUtils.stringToDate(exportExcleDTO.getStartDate(), "yyyy-MM-dd");
        Date endDate = DateUtils.stringToDate(exportExcleDTO.getEndDate(), "yyyy-MM-dd");
        while(com.adc.da.attendance.utils.DateUtils.earlier(startDate,endDate)){

            //正常工作日对应结果为 0, 法定节假日对应结果为 1, 节假日调休补班对应的结果为 2
            String isHoliday = HolidayUtils.isHoliday(startDate);
            WorkDateEO workDateEO = new WorkDateEO();
            workDateEO.setId(UUID.randomUUID().toString().replace("-", ""));
            workDateEO.setDateTime(startDate);
            workDateEO.setDept(1);
            String  weekOfDate = com.adc.da.attendance.utils.DateUtils.getWeekOfDate(startDate);
            workDateEO.setWeek(weekOfDate );

            if(isHoliday.equals("0")) {
                workDateEO.setIsPublicHoliday(0);
                workDateEO.setIsWorkDate(1);
            }
            if(isHoliday.equals("1")){
                workDateEO.setIsPublicHoliday(1);
                workDateEO.setIsWorkDate(0);
            }
            if(isHoliday.equals("2")){
                workDateEO.setIsPublicHoliday(1);
                workDateEO.setIsWorkDate(0);
            }
            dao.insert(workDateEO);
            startDate = com.adc.da.attendance.utils.DateUtils.addDays(startDate, 1);
        }
        return null;
    }

    /**
     * 获取指定日期期间的工作日
     * @return
     */
    public    List<WorkDateEO> getWorkDate( ExportDateExcleDTO exportDateExcleDTO){
        try {
            return mworkdao.selectByBeginAndLast(exportDateExcleDTO);
        }catch (Exception e){
            throw new AdcDaBaseException("查询失败");

        }

    }

    /**
     * 获取起止日期之间的所有日历
     * @param exportExcleRealDTO
     * @return
     */
    public List<WorkDateResDTO> getListByStartEnd(ExportExcleRealDTO exportExcleRealDTO) {
        Date endDate = DateUtil.parseDate(exportExcleRealDTO.getEndDate(), "yyyy-MM-dd");
        Date startDate =  DateUtil.parseDate(exportExcleRealDTO.getStartDate(), "yyyy-MM-dd");
        ExportDateExcleDTO exportDateExcleDTO = new ExportDateExcleDTO();
        exportDateExcleDTO.setStartDate(startDate);
        exportDateExcleDTO.setEndDate(endDate);
        try {
            List<WorkDateResDTO> workDateResDTOList = new ArrayList<>();
            List<WorkDateEO> list = mworkdao.selectAllLstByBeginAndLast(exportDateExcleDTO);
            for (WorkDateEO workDateEO : list) {
                WorkDateResDTO workDateResDTO = new WorkDateResDTO();
                workDateResDTO.setCalendar(DateUtil.getDateStr(workDateEO.getDateTime(),"yyyy-MM-dd"));
                workDateResDTO.setId(workDateEO.getId());
                workDateResDTO.setIsWorkDate(workDateEO.getIsWorkDate());
                workDateResDTOList.add(workDateResDTO);
            }
            return workDateResDTOList;
        }catch (Exception e){
            throw new AdcDaBaseException("查询失败");
        }

    }
}
