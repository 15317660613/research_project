package com.adc.da.progress.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>PR_PROJECT_MILEPOST ProjectMilepostEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MyProjectMilepostEOPage extends BasePage {

    private String id ;
    private List<String> fit_idList ;

    private List<String> not_idList ;

    private String milepostManagerId;

    private String projectName;
    private String ne_projectName;
    private String like_projectName;
    private String ne_like_projectName;

    private String projectId;

    private String milepostName;
    private String ne_milepostName;
    private String like_milepostName;
    private String ne_like_milepostName;

    private String milepostTarget;
    private String ne_milepostTarget;
    private String like_milepostTarget;
    private String ne_like_milepostTarget;

    private String milepostManagerName;
    private String ne_milepostManagerName;
    private String like_milepostManagerName;
    private String ne_like_milepostManagerName;

    private Date milepostBeginTime;
    private Date gte_milepostBeginTime;
    private Date gt_milepostBeginTime;
    private Date lte_milepostBeginTime;
    private Date lt_milepostBeginTime;
    private Date ne_milepostBeginTime;




    private Date milepostEndTime;
    private Date gte_milepostEndTime;
    private Date lte_milepostEndTime;
    private Date gt_milepostEndTime;
    private Date lt_milepostEndTime;
    private Date ne_milepostEndTime;

    private String milepostVersion;

    private String finishTime;

    private Integer finishStatus;
    private Integer ne_finishStatus;

    private String notUsed ;

    private List<String> projectIdList ;

}
