package com.adc.da.budget.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.OrgWithParentName;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.vo.BudgetExportVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.page.UserEOPage;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出业务项目
 *
 * @author liuzixi
 * date 2019-03-18
 */
@Service
public class BudgetExportService {

    /**
     * 表头
     */
    private static final String[] TITLES = {"一级部门", "二级部门", "组别名称", "业务名称", "业务经理",
            "项目名称", "项目负责人"};

    /**
     * 业务查询
     */
    @Autowired
    private BudgetEODao budgetEODao;

    /**
     * 部门
     */
    @Autowired
    private OrgListDao orgListDao;

    /**
     * 用户
     */
    @Autowired
    private UserEODao userEODao;

    /**
     * 项目查询
     */
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 部门全称
     */
    @Autowired
    private ImportUsersService importUsersService;

    /**
     * 获取业务项目Excel
     *
     * 导出数据字段为：一级部门，二级部门，组别名称，业务名称，业务经理，项目名称，项目负责人
     *
     * @return
     * @author liuzixi
     * date 2019-03-18
     */
    public Workbook getBudgetData() {
        // 1. 表头
        HSSFWorkbook workbook = getTemplate();
        // 2. 获取所有部门、用户数据
        List<OrgEO> allOrg = orgListDao.listAllOrg();
        List<OrgWithParentName> allOrgWithP = importUsersService.getAll(allOrg);
        UserEOPage page = new UserEOPage();
        page.setDelFlag("0");
        List<UserEO> allUsers = userEODao.queryByList(page);
        // 3. 查找所有业务
        List<BudgetEO> allBudgets = budgetEODao.findAll(null);

        // 4. 获取数据
        List<BudgetExportVO> data = getData(allBudgets, allOrgWithP, allUsers);

        // 5. 写入Excel
        if (CollectionUtils.isNotEmpty(data)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1, len = data.size(); i <= len; i++) {
                Row row = sheet.createRow(i);
                BudgetExportVO budgetExportVO = data.get(i - 1);
                // 数据
                Cell firstDeptCell = row.createCell(0);
                firstDeptCell.setCellValue(budgetExportVO.getFirstDept());
                Cell deptCell = row.createCell(1);
                deptCell.setCellValue(budgetExportVO.getDept());
                Cell groupCell = row.createCell(2);
                groupCell.setCellValue(budgetExportVO.getGroup());
                Cell budgetCell = row.createCell(3);
                budgetCell.setCellValue(budgetExportVO.getBudget());
                Cell pmCell = row.createCell(4);
                pmCell.setCellValue(budgetExportVO.getBudgetManager());
                Cell projectCell = row.createCell(5);
                projectCell.setCellValue(budgetExportVO.getProject());
                Cell pLCell = row.createCell(6);
                pLCell.setCellValue(budgetExportVO.getProjectLeader());
            }
        }

        return workbook;
    }

    /**
     * 获取表头
     * @return
     * @author liuzixi
     * date 2019-03-18
     */
    private HSSFWorkbook getTemplate() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("业务-项目信息");
        Row titleRow = sheet.createRow(0); // 创建表头
        for (int i = 0, len = TITLES.length; i < len; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(TITLES[i]);
        }
        return workbook;
    }

    /**
     * 整理数据
     * @param allBudgets
     * @param allOrgWithP
     * @return
     * @author liuzixi
     * date 2019-03-18
     */
    private List<BudgetExportVO> getData(List<BudgetEO> allBudgets, List<OrgWithParentName> allOrgWithP,
                                         List<UserEO> allUsers) {
        List<BudgetExportVO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(allBudgets)) {
            return list;
        }
        for (BudgetEO budgetEO : allBudgets) {
            // 部门
            OrgWithParentName org = getOrg(budgetEO.getDeptId(), allOrgWithP);
            String firstDept = null;
            String dept = null;
            String group = null;
            if (null != org) {
                String holeName = org.getNameWithParent();
                String[] names = holeName.split("-");
                if (names.length <= 2) {
                    firstDept = names[names.length - 1];
                } else if (names.length == 3) {
                    firstDept = names[1];
                    dept = names[2];
                    group = budgetEO.getTeamName();
                } else {
                    firstDept = names[1];
                    dept = names[2];
                    group = names[names.length - 1];
                }
            }
            // 业务下任务
            QueryBuilder builder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.termQuery("delFlag", true))
                    .should(QueryBuilders.termQuery("budgetId", budgetEO.getId()));
            List<Project> projects = Lists.newArrayList(projectRepository.search(builder));
            if (CollectionUtils.isEmpty(projects)) {
                // 如果没有任务，单独一条数据
                BudgetExportVO budgetExportVO = new BudgetExportVO();
                budgetExportVO.setBudget(budgetEO.getProjectName());
                UserEO pm = getUser(budgetEO.getPm(), allUsers);
                if (null != pm) {
                    budgetExportVO.setBudgetManager(pm.getUsname());
                }
                budgetExportVO.setFirstDept(firstDept);
                budgetExportVO.setDept(dept);
                budgetExportVO.setGroup(group);

                list.add(budgetExportVO);
            } else {
                // 如果有任务，遍历
                for (Project project : projects) {
                    BudgetExportVO budgetExportVO = new BudgetExportVO();
                    budgetExportVO.setBudget(budgetEO.getProjectName());
                    UserEO pm = getUser(budgetEO.getPm(), allUsers);
                    if (null != pm) {
                        budgetExportVO.setBudgetManager(pm.getUsname());
                    }
                    budgetExportVO.setFirstDept(firstDept);
                    budgetExportVO.setDept(dept);
                    budgetExportVO.setGroup(group);

                    budgetExportVO.setProject(project.getName());
                    UserEO projectLeader = getUser(project.getProjectLeaderId(), allUsers);
                    if (null != projectLeader) {
                        budgetExportVO.setProjectLeader(projectLeader.getUsname());
                    }
                    list.add(budgetExportVO);
                }
            }
        }

        return list;
    }

    /**
     * 获取部门
     * @param deptId
     * @param allOrgs
     * @return
     * @author liuzixi
     * date 2019-03-18
     */
    private OrgWithParentName getOrg(String deptId, List<OrgWithParentName> allOrgs) {
        for (OrgWithParentName org : allOrgs) {
            if (StringUtils.equals(deptId, org.getId())) {
                return org;
            }
        }
        return null;
    }

    /**
     * 获取用户
     * @param userId
     * @param allUsers
     * @return
     * @author liuzixi
     * date 2019-03-18
     */
    private UserEO getUser(String userId, List<UserEO> allUsers) {
        for (UserEO user : allUsers) {
            if (StringUtils.equals(userId, user.getUsid())) {
                return user;
            }
        }
        return null;
    }
}
