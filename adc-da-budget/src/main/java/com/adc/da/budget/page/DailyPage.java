package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyPage extends BasePage {

    private String budgetName ; //budgetName 名称---------
    private Integer pageType ; // 0 是业务 1 是 项目 2 是任务
    private String budgetId ; //budgetName ID---------
    private String projectId ; //项目ID----------
    private String taskId ; // 任务ID
    private String projectName ; //项目名称----------
    private String taskName ; // 任务名称
    private Integer approveState ; //审批状态

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date queryStartDate ;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date queryEndDate ;



    private String workDescription; //工作描述---------------

    private Date dailyCreateTime; //哪天的日报----------------

    //日报创建人

    private String createUserName; // 创建人----------------------------------

    private String approveUserId ; // 审批人ID

    private String dailyType; // 类型

    private String timeSlot; //上午、下午、晚上

//    private String createUserId; //上午、下午、晚上

    private Date createTime; // 创建日报的时间，但是不一定是那天的日报

}
