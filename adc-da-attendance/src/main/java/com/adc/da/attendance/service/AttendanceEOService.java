package com.adc.da.attendance.service;

import com.adc.da.attendance.dao.AttendanceEODao;
import com.adc.da.attendance.dao.WorkDateEODao;
import com.adc.da.attendance.entity.AttendanceEO;
import com.adc.da.attendance.entity.WorkDateEO;
import com.adc.da.attendance.utils.AttendanceCodeUtils;
import com.adc.da.base.service.BaseService;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.adc.da.attendance.utils.AttendanceCodeUtils.BE_LATE;
import static com.adc.da.attendance.utils.AttendanceCodeUtils.BE_LATE_AND_LEAVE_EARLY;
import static com.adc.da.attendance.utils.AttendanceCodeUtils.LEAVE_EARLY;
import static com.adc.da.attendance.utils.AttendanceCodeUtils.NORMAL_ATTENDANCE;

/**
 * <br>
 * <b>功能：</b>ST_ATTENDANCE AttendanceEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-13 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("attendanceEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class AttendanceEOService extends BaseService<AttendanceEO, String> {

    @Autowired
    private AttendanceEODao dao;

    @Autowired
    private WorkDateEODao workDateEODao;

    public AttendanceEODao getDao() {
        return dao;
    }

    /**
     * 开始行 == 表头行数
     */
    private static final int INT_ROW_START = 2;

    /**
     * 用户code 非 完整code 缺失 ABCD
     */
    private static final int INT_USER_CODE = 0;

    /**
     * 部门名称
     */
    private static final int INT_DEPT_NAME = 2;

    /**
     * 用户名称
     */
    private static final int INT_USER_NAME = 1;

    /**
     * 获取 excel 单元格 内容
     *
     * @param row
     * @return
     */
    private static String getCellStringValue(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

    /**
     * * 读取Excel 2019年度部门费用明细表
     *
     * @param wb
     * @param beginLong
     * @param endLong
     * @param saveFlag
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-12-13
     **/
    public void importAttendanceDataFromExcel(Workbook wb, long beginLong, long endLong, boolean saveFlag) {

        Sheet sheet = wb.getSheetAt(0);
        int rowEnd = sheet.getLastRowNum();

        Row titleRow = sheet.getRow(INT_ROW_START - 1);
        int rowLength = titleRow.getPhysicalNumberOfCells();

        Map<Date, Integer> map;
        Map<String, String> userCodeIdMap;
        Date[] date = new Date[rowLength];
        if (workDateEODao != null) {
            /* 直接从数据库访问，对节假日进行标注 */
            List<WorkDateEO> holidayList = workDateEODao.getNotWorkDate(new Date(beginLong), new Date(endLong));

            map = holidayList.stream().collect(Collectors
                .toMap(WorkDateEO::getDateTime, WorkDateEO::getIsWorkDate, (a, b) -> b));
            long tempLong = beginLong;
            for (int i = 0; i < rowLength; i++) {
                date[i] = new Date(tempLong);
                /* 增加1天 单位为毫秒 */
                tempLong += 86_400_000;
            }

            List<WorkDateEO> userCodeList = workDateEODao.getUserCode();
            userCodeIdMap = userCodeList.stream().collect(Collectors
                .toMap(WorkDateEO::getWeek, WorkDateEO::getId, (a, b) -> b));
        } else {
            log.warn("Test Mode");
            /*
             * 初始化时间类  表示 年月日
             */
            map = new HashMap<>();
            userCodeIdMap = new HashMap<>();
            long tempLong = beginLong;
            int x = 4;
            for (int i = 0; i < rowLength; i++) {
                if (x == 0 || x == 6) {
                    map.put(new Date(tempLong), 1);
                }

                date[i] = new Date(tempLong);
                /* 增加1天 单位为毫秒 */
                tempLong += 86_400_000;
                x++;
                x %= 7;
            }
        }

        List<AttendanceEO> saveList = new ArrayList<>();
        String id = UUID.randomUUID10();
        for (int i = INT_ROW_START; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            String code = getCellStringValue(row, INT_USER_CODE);
            String deptName = getCellStringValue(row, INT_DEPT_NAME);
            String userName = getCellStringValue(row, INT_USER_NAME);
            String userCode = initUserCode(code);
            for (int j = 3; j < rowLength; j++) {
                AttendanceEO eo = new AttendanceEO();
                String source = getCellStringValue(row, j);
                eo.setDeptName(deptName);
                eo.setUserName(userName);
                eo.setId(id + "_" + userCode + "_" + String.format("%02d", (j - 2)));
                eo.setUserCode(userCode);
                eo.setUserId(userCodeIdMap.get(userCode));
                eo.setSource(source);
                Date nowDate = date[j - 3];
                /*
                 *  假期 = 0
                 *  工作日 = null
                 */
                eo.setBeginTimeInt(map.get(nowDate));
                updateSource(eo);
                eo.setDate(nowDate);
                saveList.add(eo);
            }
        }

        if (saveFlag) {
            dao.deleteByTimeArea(new Date(beginLong), new Date(endLong));
            List<List<AttendanceEO>> saveL = splitList(saveList, 500);
            saveL.forEach(dao::insertSelectiveAll);
        }
    }

    /**
     * 追加userCode 头
     *
     * @param code
     * @return
     */
    private static String initUserCode(String code) {
        /*
         * 用户头
         *  1xxx -> A1xxx
         *  2xxx -> B2xxx
         *  3xxx -> C3xxx
         *  4xxx -> D4xxx
         */
        char[] codeIndex = {'A', 'B', 'C', 'D'};
        char x = code.charAt(0);
        return codeIndex[x - '1'] + code;
    }

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     *
     * @param <T>
     * @param list
     * @param len
     * @return
     */
    private static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList();
        }

        List<List<T>> result = new ArrayList<>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, (Math.min((i + 1) * len, size)));
            result.add(subList);
        }

        return result;
    }

    /**
     * 处理考勤记录
     *
     * @param eo
     * @see AttendanceCodeUtils ;
     */
    private void updateSource(AttendanceEO eo) {

        /*
         *
         */
        if (null != eo.getBeginTimeInt()) {
            eo.setReason("假期");
            return;
        }

        String source = eo.getSource();
        if (StringUtils.isNotEmpty(eo.getSource())) {
            /*
             * 对记录 去除":" 首尾空格, 根据空格切割
             */
            String[] time = source.replaceAll(":", "")
                                  .trim()
                                  .split(" ");

            int flag = BE_LATE_AND_LEAVE_EARLY;
            /*
             *
             * 对于早于8点的记录取第一条，对于晚于1700 取最后一条
             * 进行 或 预算
             */
            for (String s : time) {
                int subTimeInt = Integer.parseInt(s);
                if (null == eo.getBeginTime() && subTimeInt < 801) {
                    eo.setBeginTimeInt(subTimeInt);
                    flag |= LEAVE_EARLY;
                }
                if (subTimeInt >= 1700) {
                    flag |= BE_LATE;
                    eo.setEndTimeInt(subTimeInt);
                }
            }
            /*
             * 存储二进制为String
             */
            eo.setRemark(Integer.toBinaryString(flag));
            String reason;
            switch (flag) {

                case BE_LATE_AND_LEAVE_EARLY:
                    reason = "迟到早退";
                    break;
                case LEAVE_EARLY:
                    reason = "早退";
                    break;
                case BE_LATE:
                    reason = "迟到";
                    break;
                case NORMAL_ATTENDANCE:
                default:
                    reason = "";
            }
            eo.setReason(reason);

        } else {
            eo.setReason("旷工");
        }
    }

    private static final int ROW_DATE_INT = 4;

    private static final int ROW_WEEK_INT = 5;

    private static final int CELL_DATE_BEGIN_INT = 7;

    private static final int CELL_DATE_END_INT = 38;

    /**
     * 导出考勤数据
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     */
    public void exportAttendance(Workbook wb, OutputStream os, long beginLong, long endLong) throws IOException {
        String[] weekStr = {"日", "一", "二", "三", "四", "五", "六"};
        /*
         * 表头
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(beginLong);
        int startPoint = calendar.get(Calendar.DAY_OF_WEEK);
        int index = startPoint - 1;

        Sheet sheet = wb.getSheetAt(0);

        sheet.getRow(0).getCell(0)
             .setCellValue(calendar.get(Calendar.YEAR) + "年"
                 + (calendar.get(Calendar.MONTH) + 1) + "月考勤");

        Row dateRow = sheet.getRow(ROW_DATE_INT);
        Row weekRow = sheet.getRow(ROW_WEEK_INT);

        /*表头相关数据调整  日期 以及 星期*/
        for (int i = CELL_DATE_BEGIN_INT; i < CELL_DATE_END_INT; i++) {
            Cell cellDate = dateRow.getCell(i);
            Cell cellWeek = weekRow.getCell(i);

            cellDate.setCellValue(calendar.get(Calendar.DAY_OF_MONTH));
            cellWeek.setCellValue(weekStr[index % 7]);

            calendar.add(Calendar.DAY_OF_MONTH, 1);

            /* 周六 周日 标红 其他标黑 */
            if (index % 7 == 0 || index % 7 == 6) {
                /* 周列 默认 为 黑，特殊情况标红*/
                CellStyle red = cellDate.getCellStyle();
                cellWeek.setCellStyle(red);
            } else {
                /* 日列 默认为 红，特殊情况标黑 */
                CellStyle black = cellWeek.getCellStyle();
                cellDate.setCellStyle(black);
            }
            index++;
            if (calendar.getTimeInMillis() > endLong) {
                break;
            }

        }
        //todo 插入考勤相关数据（仅异常数据）
        Row dataRow = sheet.getRow(ROW_WEEK_INT + 1);

        int memberCount = 1;
        List<Map<String, List<AttendanceEO>>> allUserAttendance = new ArrayList<>();
        Map<String, List<AttendanceEO>> x = new HashMap<>();

        allUserAttendance.forEach(eo -> {

        });

        for (int j = 0; j < 1; j++) {
            for (int k = CELL_DATE_BEGIN_INT; k < CELL_DATE_END_INT; k++) {

            }
            sheet.createRow(1).getRowStyle();

        }
        wb.write(os);

        os.flush();

//
    }

}
