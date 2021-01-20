package com.adc.da.budget.query.project;

import com.adc.da.budget.query.BaseESQueryPage;
import com.adc.da.budget.query.QueryVO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能：</b>TS_BUDGET BudgetEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Setter
@Getter
public class ProjectQuery extends BaseESQueryPage {

    private List<QueryVO> projectName = new ArrayList<>();

    private List<QueryVO> projectMemberNames = new ArrayList<>();

    private List<QueryVO> budgetBelong = new ArrayList<>();

    private List<QueryVO> projectLeader = new ArrayList<>();

    private List<QueryVO> projectCreateDate = new ArrayList<>();

    private List<QueryVO> projectEndDate = new ArrayList<>();

    private List<QueryVO> projectStatus = new ArrayList<>();

    private List<QueryVO> inputPersonDay = new ArrayList<>();

    private List<QueryVO> createUser = new ArrayList<>();

    private List<QueryVO> createDate = new ArrayList<>();

    private String finishedStatus;

    private Integer searchFlag; // 0 是查自己  1 是部门下

    private String searchName;

    private  String[] projectTypeArr ={"0"};

}
