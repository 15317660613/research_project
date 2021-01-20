package com.adc.da.crm.entity;

import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserEOCompare extends UserEO implements Comparable<UserEOCompare> {


    @Override
    public int compareTo(UserEOCompare o) {
        /**
         * 果指定的数与参数相等返回0。

         如果指定的数小于参数返回 -1。

         如果指定的数大于参数返回 1。
         */
        UserEOCompare o1 = (UserEOCompare) o;
        Integer i=0;
        Integer j =0;

        if(!StringUtils.isEmpty(this.getExtInfo()) && Integer.valueOf((this.getExtInfo()))>i){
            i=Integer.valueOf(this.getExtInfo());
        }
        if(!StringUtils.isEmpty(o1.getExtInfo()) &&Integer.valueOf(o1.getExtInfo())>i){
                j=Integer.valueOf(o1.getExtInfo());
        }


        if(StringUtils.isEmpty(this.getExtInfo())||StringUtils.isEmpty(o1.getExtInfo())){
            return 0;
        }
        return compareM(Integer.valueOf(this.getExtInfo()), Integer.valueOf(o1.getExtInfo()));
    }

    public int compareM(Integer roleSort, Integer roleSort2) {
        if (roleSort > roleSort2) {
            return 1;
        } else if (roleSort == roleSort2) {
            return 0;
        } else {
            return -1;
        }
    }


}
