package com.adc.da.budget.service;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.entity.RevenueExpense;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.RevenueExpenseRepository;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.RevenueVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 14:33
 * @Description:
 */
@Service
public class RevenueExpenseService extends BaseService<RevenueExpense, String> {

    private Logger logger = LoggerFactory.getLogger(RevenueExpenseService.class);

    @Autowired
    private RevenueExpenseRepository revenueExpenseRepository;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserEPDao userEPDao;


    /**
     * @Description: 新增
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:37
     */
    public RevenueExpense insert(RevenueVO revenueVO) {
        RevenueExpense revenueExpense = beanMapper.map(revenueVO, RevenueExpense.class);
        List<UserEPEntity> userEPEntities = userEPDao.checkUserExistById(revenueVO.getParticipateMembers());
        if (null != userEPEntities && userEPEntities.size() > 0) {
            String[] array = new String[userEPEntities.size()];
            for (int i = 0; i < userEPEntities.size(); i++) {
                array[i] = userEPEntities.get(i).getUsname();
            }
            String join = StringUtils.join(array, ",");
            revenueExpense.setParticipateMember(join);
        }
        Project project = projectRepository.findOne
                (revenueVO.getParentProjectId() == null ? null : revenueVO.getParentProjectId());
        if (project != null) { revenueExpense.setProjectName(project.getName()); }
        revenueExpense.setMapList(CommonUtil.userMapKv(userEPEntities));
        revenueExpense.setStartTime(new Date());
        revenueExpense.setId(UUID.randomUUID().toString());
        return revenueExpenseRepository.save(revenueExpense);
    }

    /**
     * @Description: 根据id删除 可以是ids
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */
    public String deleteBatch(String ids) {
        if (ids == null || ids.trim().isEmpty()) { return "删除失败！"; }
        String msg = "删除成功！";
        try {
            String[] idArray = ids.split(",");
            if (null != idArray && idArray.length != 0) {
                for (String id : idArray) {
                    revenueExpenseRepository.delete(id);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            msg = "删除失败！";
        }
        return msg;
    }


    public void deleteAll() {
        revenueExpenseRepository.deleteAll();
    }

    /**
     * @Description: 修改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */

    public RevenueExpense update(RevenueVO revenueVO) {
        try {
            String[] participateMembers = revenueVO.getParticipateMembers();
            List<UserEPEntity> userEPEntities = userEPDao.checkUserExistById(participateMembers);
            String[] names = new String[userEPEntities.size()];
            for (int i = 0; i < userEPEntities.size(); i++) {
                names[i] = userEPEntities.get(i).getUsname();
            }
            RevenueExpense revenue = beanMapper.map(revenueVO, RevenueExpense.class);
            revenue.setMapList(CommonUtil.userMapKv(userEPEntities));
            revenue.setParticipateMember(StringUtils.join(names, ","));
            revenue.setUpdateTime(new Date());
            // 查询
            RevenueExpense re = revenueExpenseRepository.findOne(revenueVO.getId());
            // 复制属性
            BeanUtils.copyPropertiesIgnoreNullValue(revenue, re);
            // 保存
            return revenueExpenseRepository.save(re);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    /**
     * @Description: 查找
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:45
     */
    public RevenueExpense querySingle(String id) {
        return revenueExpenseRepository.findOne(id);
    }


    /**
     * 导入
     * @param is
     * @param params
     * @throws Exception
     */
    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<RevenueExpense> datas = ExcelImportUtil.importExcel(is, RevenueExpense.class, params);
        //导入
        for (RevenueExpense revenueExpense : datas) {
            //1、检验项目是否存在
            if (StringUtils.isEmpty(revenueExpense.getParentProjectId())) {
                logger.error("所属项目ID不存在，导入失败, 跳过数据，项目ID " + revenueExpense.getParentProjectId());
                continue;
            }
            //2、展开人员
            String[] participateMembers = revenueExpense.getParticipateMember().split("，");
            String[] memberIds = new String[participateMembers.length];

            List<UserEPEntity> userEPEntities = userEPDao.checkUserExist(participateMembers);

            //2.1、查找人员

            if (userEPEntities == null || userEPEntities.size() < 1) {
                logger.error("所属人员ID不存在，导入失败, 跳过数据，人员ID " + revenueExpense.getParticipateMember());
                continue;
            }
            for (int i = 0; i < userEPEntities.size(); i++) {
                memberIds[i] = userEPEntities.get(i).getUsid();
            }


            revenueExpense.setParticipateMembers(memberIds);
            //3、导入数据
            if (StringUtils.isEmpty(revenueExpense.getId())) {
                revenueExpense.setId(UUID.randomUUID().toString());
            }
            revenueExpenseRepository.save(revenueExpense);
        }
    }

    /**
     * 导出
     * @param params
     * @return
     */
    public Workbook excelExport(ExportParams params) {
        List<RevenueExpense> oneList = Lists.newArrayList(revenueExpenseRepository.findAll());
        return ExcelExportUtil.exportExcel(params, RevenueExpense.class, oneList);
    }


    public List<RevenueExpense> queryAll() {
        //查询本部门下的所有收入合同，调用project的queryAll（判断了当前部门）
        List<Project> projects = projectService.queryAll();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (null != projects && projects.size()>0){
            for (Project project : projects) {
                queryBuilder.should(QueryBuilders.termQuery("parentProjectId", project.getId()));
            }
        }
        return Lists.newArrayList(revenueExpenseRepository.search(queryBuilder));
    }

    /**
     * 分页查询
     */
    @Override
    public PageDTO queryByPage(int page, int size){
        List<Project> projects = projectService.queryAll();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (null != projects && projects.size() > 0) {
            for (Project project : projects) {
                queryBuilder.should(QueryBuilders.termQuery("parentProjectId", project.getId()));
            }
        }
        List<RevenueExpense> list = null;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<RevenueExpense> queryPage = revenueExpenseRepository.search
                (queryBuilder, new PageRequest(page - 1, size, sort));
        list = (queryPage == null || (list = queryPage.getContent()) == null) ? new ArrayList<RevenueExpense>() : list;
        long count = queryPage == null ? 0 : queryPage.getTotalElements();
        return new PageDTO(count, list, page, size);
    }



}
