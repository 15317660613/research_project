package com.adc.da.listener.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.adc.da.listener.utils.FormKeyWord.US_ID;
import static com.adc.da.listener.utils.FormKeyWord.US_NAME;
import static com.adc.da.listener.utils.FormKeyWord.VALUE;

/**
 * describe:
 * 里程碑表单
 *
 * @author 李坤澔
 *     date 2019-08-01
 */
@Data
public class MilestoneEO {

    /**
     * 名称
     */
    HashMap<String, String> name;

    /**
     * 用户信息
     */
    HashMap<String, String> user;

    /**
     * 目标
     */
    HashMap<String, String> target;

    /**
     * 起止时间（需要拆分）
     */
    HashMap<String, String> date;

    /**
     * 成果物
     */
    ArrayList<HashMap<String, String>> outcomes;

    /**
     * 获取里程碑名称
     *
     * @return
     */
    public String getNameValue() {
        return name.get(VALUE);
    }

    /**
     * 获取里程碑目标
     *
     * @return
     */
    public String getTargetValue() {
        return target.get(VALUE);
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public String getUserIdValue() {
        return user.get(US_ID);
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public String getUserNameValue() {
        return user.get(US_NAME);
    }

    /**
     * 交付物信息
     *
     * @return
     */
    public List<String> getOutcomesValue() {

        List<String> res = new ArrayList<>(outcomes.size());

        for (HashMap<String, String> eo : outcomes) {
            res.add(eo.get(VALUE));
        }

        return res;

    }

}
