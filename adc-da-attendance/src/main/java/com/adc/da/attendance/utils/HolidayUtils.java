package com.adc.da.attendance.utils;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HolidayUtils {
    /**
     * 确定是不是节假日,是节假日返回false ，工作日进行数据抓取返回true
     * 返回 2 ：代表是法定节假日休息。1：正常休息日   0：工作日
     *
     * @param
     * @return
     */
    public static String isHoliday(Date date) {
        try {
            SimpleDateFormat dft2 = new SimpleDateFormat("yyyyMMdd");
            String newDate = dft2.format(date);
            //正常工作日对应结果为 0, 法定节假日对应结果为 1, 节假日调休补班对应的结果为 2
            String url = "http://api.goseek.cn/Tools/holiday";
            Map<String, String> map = new HashMap<>();
            map.put("date", newDate);
            String result = HttpClientUtils.getHttpGet(url, map);
            JSONObject o = JSONObject.fromObject(result);
            System.out.println(o.toString());
            return o.get("data").toString();
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * 获取工作日日期列表
     * @return
     */
//    public List<Date> getWorkDateLst(Date startDate, Date endDate){
//
//
//    }

}
