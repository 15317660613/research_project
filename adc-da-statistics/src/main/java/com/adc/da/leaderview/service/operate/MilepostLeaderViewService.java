package com.adc.da.leaderview.service.operate;

import com.adc.da.base.page.Pager;
import com.adc.da.budget.query.QueryVO;
import com.adc.da.budget.query.milepost.MilepostQuery;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.page.MyProjectMilepostEOPage;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MilepostLeaderViewService {

    @Autowired
    private ProjectMilepostEODao projectMilepostEODao;

    @Autowired
    private ProjectMilepostResultEODao projectMilepostResultEODao;

    public PageInfo<ProjectMilepostEO> searchByLoginUser(String userId, MilepostQuery page) throws Exception {

        return getProjectByUserId(userId, page);

    }

    private PageInfo<ProjectMilepostEO> getProjectByUserId(String userId, MilepostQuery page) throws Exception {
        //不符合要求的所有
        List<ProjectMilepostEO> expList = new ArrayList<>();
        //符合要求的
        List<ProjectMilepostEO> incList = new ArrayList<>();

        boolean searchFlag = false;

        MyProjectMilepostEOPage milepostEOPage = new MyProjectMilepostEOPage();
        milepostEOPage.setPageSize(page.getPageSize());
        milepostEOPage.setPage(page.getPage());
        milepostEOPage.setMilepostManagerId(userId);
        if (CollectionUtils.isNotEmpty(page.getMilepostName())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getMilepostName()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setMilepostName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_milepostName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "like":
                        myPage.setLike_milepostName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "not like":
                        myPage.setNe_like_milepostName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }
            }

        }
        if (CollectionUtils.isNotEmpty(page.getMilepostLeader())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getMilepostLeader()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setMilepostManagerName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_milepostManagerName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "like":
                        myPage.setLike_milepostManagerName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "not like":
                        myPage.setNe_like_milepostManagerName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }
            }
        }
        if (CollectionUtils.isNotEmpty(page.getMilepostTarget())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getMilepostTarget()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setMilepostTarget(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_milepostTarget(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "like":
                        myPage.setLike_milepostTarget(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "not like":
                        myPage.setNe_like_milepostTarget(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }
            }
        }
        if (CollectionUtils.isNotEmpty(page.getFinishStatus())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getFinishStatus()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setFinishStatus(Integer.valueOf(queryVO.getName()));
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_finishStatus(Integer.valueOf(queryVO.getName()));
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }
            }
        }
        if (CollectionUtils.isNotEmpty(page.getProjectName())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getProjectName()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setProjectName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_projectName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "like":
                        myPage.setLike_projectName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "not like":
                        myPage.setNe_like_projectName(queryVO.getName());
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }
            }
        }
        if (CollectionUtils.isNotEmpty(page.getMilepostBeginTime())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getMilepostBeginTime()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                Date date = getDate(queryVO.getName());
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setMilepostBeginTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case ">=":
                        myPage.setGte_milepostBeginTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case ">":
                        myPage.setGt_milepostBeginTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "<":
                        myPage.setLt_milepostBeginTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "<=":
                        myPage.setLte_milepostBeginTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_milepostBeginTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }

            }
        }
        if (CollectionUtils.isNotEmpty(page.getMilepostEndTime())) {
            searchFlag = true;
            for (QueryVO queryVO : page.getMilepostEndTime()) {
                MyProjectMilepostEOPage myPage = new MyProjectMilepostEOPage();
                Date date = getDate(queryVO.getName());
                switch (queryVO.getOperator()) {
                    case "=":
                        myPage.setMilepostEndTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case ">=":
                        myPage.setGte_milepostEndTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case ">":
                        myPage.setGt_milepostEndTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "<":
                        myPage.setLt_milepostEndTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "<=":
                        myPage.setLte_milepostEndTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    case "!=":
                        myPage.setNe_milepostEndTime(date);
                        incList.addAll(getMilePostByPage(myPage));
                        break;
                    default:
                }
            }
        }

        List<String> fitIdList = getFitMilePostIdList(incList);
        List<String> notIdList = getFitMilePostIdList(expList);

        if (CollectionUtils.isNotEmpty(notIdList)) {
            milepostEOPage.setNot_idList(notIdList);
        }
        if (CollectionUtils.isNotEmpty(fitIdList)) {
            milepostEOPage.setFit_idList(fitIdList);
        } else if (searchFlag) {
            fitIdList.add("");
            milepostEOPage.setFit_idList(fitIdList);
        }
        List<ProjectMilepostEO> projectMilepostEOList = projectMilepostEODao.page4Tips(milepostEOPage);
        Date currentDate = DateUtils.getOnlyYMD(new Date());
        for (ProjectMilepostEO projectMilepostEO : projectMilepostEOList) {
            List<ProjectMilepostResultEO> projectMilepostResultEOList = projectMilepostResultEODao
                .selectByMilepostId(projectMilepostEO.getId());
            StringBuilder stringBuilder = new StringBuilder();
            for (ProjectMilepostResultEO resultEO : projectMilepostResultEOList) {
                stringBuilder.append(resultEO.getFileName()).append("、");
            }
            if (stringBuilder.lastIndexOf("、") > 0) {
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("、"));
            }
            projectMilepostEO.setResultName(stringBuilder.toString());

            projectMilepostEO.setProjectMilepostResultEOList(projectMilepostResultEOList);
            if (projectMilepostEO.getFinishStatus() == 9) {
                continue;
            }

            int compareToBegin = DateUtils.getOnlyYMD(projectMilepostEO.getMilepostBeginTime()).compareTo(currentDate);
            int compareToEnd = DateUtils.getOnlyYMD(projectMilepostEO.getMilepostEndTime()).compareTo(currentDate);
            //起始日期大于当前日期    未开始
            if (compareToBegin == 1) {
                projectMilepostEO.setFinishStatus(0);
            }
            //起日期 ≤ 当前日期 且 止日期 ≥ 当前日期       未完成
            if ((compareToBegin == -1 || compareToBegin == 0) && (compareToEnd == 1 || compareToEnd == 0)) {
                projectMilepostEO.setFinishStatus(3);
            }
            //未完成（已有风险）：起日期 ≤ 当前日期 且 止日期 ＜ 当前日期
            if ((compareToBegin == -1 || compareToBegin == 0) && (compareToEnd == -1)) {
                projectMilepostEO.setFinishStatus(6);
            }
        }

        Integer rowCount = projectMilepostEODao.query4TipsByCount(milepostEOPage);
        page.getPager().setRowCount(rowCount);
        return getPageInfo(page.getPager(), projectMilepostEOList);
    }

    private Date getDate(String timestamp) {
        return new Date(Long.valueOf(timestamp));
    }

    private List<String> getFitMilePostIdList(List<ProjectMilepostEO> idList) {
        List<String> retList = new ArrayList<>();
        for (ProjectMilepostEO projectMilepostEO : idList) {
            retList.add(projectMilepostEO.getId());
        }
        return retList;
    }

    private List<ProjectMilepostEO> getMilePostByPage(MyProjectMilepostEOPage milepostEOPage) throws Exception {
        milepostEOPage.setPageSize(100000);
        return projectMilepostEODao.page4Tips(milepostEOPage);
    }

    public PageInfo<ProjectMilepostEO> getPageInfo(Pager pager, List<ProjectMilepostEO> rows) {
        PageInfo<ProjectMilepostEO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long) pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long) pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }

}
