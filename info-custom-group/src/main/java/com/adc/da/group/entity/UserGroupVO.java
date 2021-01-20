package com.adc.da.group.entity;

import com.adc.da.sys.entity.UserEO;
import lombok.Data;

import java.util.List;

@Data
public class UserGroupVO {

    private List<UserCustomEO> userCustomEOList;

    private List<UserEO> userEOList;

    private String groupName;

}
