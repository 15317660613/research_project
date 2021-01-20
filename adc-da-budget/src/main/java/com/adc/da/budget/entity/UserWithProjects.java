package com.adc.da.budget.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO 用户所属的业务、项目、任务、子任务列表
 *
 * @author qichunxu
 * @date 2019-04-09
 */
@Data
@Document(indexName = "financial_prd", type = "UserWithProjects")
public class UserWithProjects {

    /**
     * userID
     */
    @Id
    private String userId;

    /**
     * 子任务ID列表
     */
    private Set<String> childTaskIds;

    /**
     * 任务ID列表
     */
    private Set<String> taskIds;

    /**
     * 项目ID列表
     */
    private Set<String> projectIds;

    /**
     * 业务ID列表
     */
    private Set<String> businessIds;

    public UserWithProjects() {
        childTaskIds = new HashSet<>();
        taskIds = new HashSet<>();
        projectIds = new HashSet<>();
        businessIds = new HashSet<>();
    }
}
