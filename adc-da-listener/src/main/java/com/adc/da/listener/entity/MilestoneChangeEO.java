package com.adc.da.listener.entity;

import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.listener.utils.FormKeyWord.ID;
import static com.adc.da.listener.utils.FormKeyWord.US_ID;
import static com.adc.da.listener.utils.FormKeyWord.US_NAME;
import static com.adc.da.listener.utils.FormKeyWord.VALUE;
import static com.adc.da.listener.utils.FormType.END_TIME;
import static com.adc.da.listener.utils.FormType.BEGIN_TIME;

/**
 * describe:
 * 用于 项目变更流程 里程碑变更解析
 *
 * @author 李坤澔
 *     date 2019-08-02
 */
@Data
public class MilestoneChangeEO {

    /**
     * 里程碑id
     */
    private String id;

    /**
     * 里程碑名称-旧
     */
    private HashMap<String, String> oldName;

    /**
     * 里程碑名称-新
     */
    private HashMap<String, String> newName;

    /**
     * 里程碑负责人-旧
     */
    private HashMap<String, String> oldUser;

    /**
     * 里程碑负责人-新
     */
    private HashMap<String, String> newUser;

    /**
     * 里程碑目标-旧
     */
    private HashMap<String, String> oldTarget;

    /**
     * 里程碑目标-新
     */
    private HashMap<String, String> newTarget;

    /**
     * 里程碑 起止时间-旧
     */
    private MilestoneDateEO oldDate;

    /**
     * 里程碑 起止时间-新
     */
    private MilestoneDateEO newDate;

    /**
     * 里程碑 交付物-旧
     */
    private ArrayList<HashMap<String, String>> oldOutcomes;

    /**
     * 里程碑 交付物-新
     */
    private ArrayList<HashMap<String, String>> newOutcomes;

    /**
     * 获取里程碑名称
     *
     * @return name
     */
    public String getNameValue() {

        if (StringUtils.isNotEmpty(newName.get(VALUE))) {
            return newName.get(VALUE);
        } else {
            return oldName.get(VALUE);
        }

    }

    /**
     * 获取用户id
     *
     * @return id
     */
    public String getUserIdValue() {
        if (StringUtils.isNotEmpty(newUser.get(US_ID))) {
            return newUser.get(US_ID);
        } else {
            return oldUser.get(US_ID);
        }
    }

    /**
     * 获取用户姓名
     *
     * @return name
     */
    public String getUserNameValue() {

        if (StringUtils.isNotEmpty(newUser.get(US_NAME))) {
            return newUser.get(US_NAME);
        } else {
            return oldUser.get(US_NAME);
        }

    }

    /**
     * 以map形式返回用户信息
     *
     * @return <Id,Name>
     */
    public Map<String, String> getUserValue() {
        HashMap<String, String> res = new HashMap<>(1);
        if (StringUtils.isNotEmpty(newUser.get(US_ID))) {
            res.put(newUser.get(US_ID), newUser.get(US_NAME));
        } else {
            res.put(oldUser.get(US_ID), oldUser.get(US_NAME));
        }
        return res;
    }

    /**
     * 获取目标
     *
     * @return name
     */
    public String getTargetValue() {

        if (StringUtils.isNotEmpty(newTarget.get(VALUE))) {
            return newTarget.get(VALUE);
        } else {
            return oldTarget.get(VALUE);
        }

    }

    /**
     * 里程碑 时间
     */
    public Date[] getDateValue() {
        String[] time = new String[2];
        if (StringUtils.isNotEmpty(newDate.getStartValue())) {
            time[BEGIN_TIME] = newDate.getStartValue();
        } else {
            time[BEGIN_TIME] = oldDate.getStartValue();
        }

        if (StringUtils.isNotEmpty(newDate.getEndValue())) {
            time[END_TIME] = newDate.getEndValue();
        } else {
            time[END_TIME] = oldDate.getEndValue();
        }

        Date[] dateArray = new Date[2];

        dateArray[BEGIN_TIME] = DateUtils.stringToDate(time[BEGIN_TIME], DateUtils.YYYY_MM_DD_EN);
        dateArray[END_TIME] = DateUtils.stringToDate(time[END_TIME], DateUtils.YYYY_MM_DD_EN);

        return dateArray;

    }

    /**
     * 交付物信息
     *
     * @return list<String>
     */
    public List<String> getOutcomesValue() {
        List<String> res;
        List<HashMap<String, String>> list;
        if (CollectionUtils.isNotEmpty(newOutcomes)) {
            res = new ArrayList<>(newOutcomes.size());
            list = newOutcomes;

        } else {
            res = new ArrayList<>(oldOutcomes.size());
            list = oldOutcomes;
        }
        for (HashMap<String, String> eo : list) {
            res.add(eo.get(VALUE));
        }
        return res;

    }

    /**
     * 旧交付物的id
     *
     * @return list<String>
     */
    public List<String> getOldOutcomesId() {
        List<String> res;
        List<HashMap<String, String>> list;
        if (CollectionUtils.isNotEmpty(oldOutcomes)) {
            res = new ArrayList<>(oldOutcomes.size());
            list = oldOutcomes;
        } else {
            return new ArrayList<>();
        }
        for (HashMap<String, String> eo : list) {
            res.add(eo.get(ID));
        }
        return res;

    }
}
