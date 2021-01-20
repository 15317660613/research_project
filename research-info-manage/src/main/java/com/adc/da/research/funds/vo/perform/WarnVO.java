package com.adc.da.research.funds.vo.perform;

/**
 * 报警VO类
 *
 * @Auther: yanyujie
 * @Date: 2020/12/02/11:43
 * @Description:
 */
public class WarnVO {
    //项目ID
    private String projectId;
    //项目编号
    private String projectCode;
    //项目名称
    private String projectName;
    //报警内容
    private String content;
    //标题
    private String title;
    //数值
    private String numerical;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumerical() {
        return numerical;
    }

    public void setNumerical(String numerical) {
        this.numerical = numerical;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
