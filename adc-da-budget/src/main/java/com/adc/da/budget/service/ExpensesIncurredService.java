package com.adc.da.budget.service;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.ExpensesIncurred;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.budget.repository.ExpensesIncurredRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.ExpensesIncurredVO;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

/**
 * @Auther: chenhaidong
 * @Date: 2018/11/20
 * @Description:
 */
@Service
public class ExpensesIncurredService extends BaseService<ExpensesIncurred, String> {

    private Logger logger = LoggerFactory.getLogger(ExpensesIncurredService.class);

    @Autowired
    private ExpensesIncurredRepository expensesIncurredRepository;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private UserEPDao userEPDao;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * @Description: 新增
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2018/11/19 14:37
     */
    public ExpensesIncurred insert(ExpensesIncurredVO expensesIncurredVO) {
        ExpensesIncurred expensesIncurred = beanMapper.map(expensesIncurredVO, ExpensesIncurred.class);
        List<UserEPEntity> userEPEntities = userEPDao.checkUserExistById(expensesIncurredVO.getParticipateMembers());
        if (null != userEPEntities && userEPEntities.size() > 0) {
            String[] array = new String[userEPEntities.size()];
            for (int i = 0; i < userEPEntities.size(); i++) {
                array[i] = userEPEntities.get(i).getUsname();
            }
            String join = StringUtils.join(array, ",");
            expensesIncurred.setParticipateMember(join);
        }
        expensesIncurred.setMapList(CommonUtil.userMapKv(userEPEntities));
        Project project = projectRepository.findOne(expensesIncurredVO.getParentProjectId() == null ? null : expensesIncurredVO.getParentProjectId());
        if (project != null) {expensesIncurred.setProjectName(project.getName());}
        expensesIncurred.setStartTime(new Date());
        expensesIncurred.setId(UUID.randomUUID().toString());
        return expensesIncurredRepository.save(expensesIncurred);
    }

    /**
     * @Description: 根据id删除 可以是ids
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2018/11/20
     */
    public String deleteBatch(String ids) {
        if (ids == null || ids.trim().isEmpty()) {return "删除失败！";}
        String msg = "删除成功！";
        try {
            String[] idArray = ids.split(",");
            if (null != idArray && idArray.length != 0) {
                for (String id : idArray) {
                    expensesIncurredRepository.delete(id);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            msg = "删除失败！";
        }
        return msg;
    }


    public void deleteAll() {
        expensesIncurredRepository.deleteAll();
    }

    /**
     * @Description: 修改
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */

    @Transactional
    public ExpensesIncurred update(ExpensesIncurredVO expensesIncurredVO) {
        try {
            String[] participateMembers = expensesIncurredVO.getParticipateMembers();
            ExpensesIncurred expensesIncurred = beanMapper.map(expensesIncurredVO, ExpensesIncurred.class);
            List<UserEPEntity> userEPEntities = userEPDao.checkUserExistById(participateMembers);
            String[] names = new String[userEPEntities.size()];
            if (null != userEPEntities && userEPEntities.size() > 0) {
                for (int i = 0; i < userEPEntities.size(); i++) {
                    names[i] = userEPEntities.get(i).getUsname();
                }
                expensesIncurred.setParticipateMember(StringUtils.join(names, ","));
            }
            expensesIncurred.setMapList(CommonUtil.userMapKv(userEPEntities));
            expensesIncurred.setUpdateTime(new Date());
            // 查询
            ExpensesIncurred ei = expensesIncurredRepository.findOne(expensesIncurredVO.getId());
            // 复制属性
            BeanUtils.copyPropertiesIgnoreNullValue(expensesIncurred, ei);
            ExpensesIncurred bui = expensesIncurredRepository.save(ei);
            return bui;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    /**
     * @Description: 查找
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2018/11/19 14:45
     */
    public ExpensesIncurred querySingle(String id) {
        return expensesIncurredRepository.findOne(id);
    }

    /**
     * 导入
     *
     * @param is
     * @param params
     * @throws Exception
     */
    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<ExpensesIncurred> datas = ExcelImportUtil.importExcel(is, ExpensesIncurred.class, params);
        //导入


        for (ExpensesIncurred expensesIncurred : datas) {
            //1、检验项目是否存在
            if (StringUtils.isEmpty(expensesIncurred.getParentProjectId())) {
                logger.error("所属项目ID不存在，导入失败, 跳过数据，项目ID " + expensesIncurred.getParentProjectId());
                continue;
            }
            //2、展开人员
            String[] participateMembers = expensesIncurred.getParticipateMember().split("，");
            String[] memberIds = new String[participateMembers.length];

            List<UserEPEntity> userEPEntities = userEPDao.checkUserExist(participateMembers);
            if (userEPEntities.size() < participateMembers.length
                    || userEPEntities == null
                    || userEPEntities.size() == 0) {
                throw new Exception("人员不合法");
            } else {
                for (int i = 0; i < userEPEntities.size(); i++) {
                    memberIds[i] = userEPEntities.get(i).getUsid();
                }
            }
            //2.1、查找人员

            expensesIncurred.setParticipateMembers(memberIds);
            //3、导入数据
            if (StringUtils.isEmpty(expensesIncurred.getId())) {
                expensesIncurred.setId(UUID.randomUUID().toString());
            }
            expensesIncurredRepository.save(expensesIncurred);
        }
    }


    /**
     *  导出
     * @param params
     * @return
     */
    public Workbook excelExport(ExportParams params) {
        List<ExpensesIncurred> oneList = Lists.newArrayList(expensesIncurredRepository.findAll());
        return ExcelExportUtil.exportExcel(params, ExpensesIncurred.class, oneList);
    }


    public List<ExpensesIncurred> queryAll() {
        //查询本部门下的所有收入合同，调用project的queryAll（判断了当前部门）
        List<Project> projects = projectService.queryAll();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (null != projects && projects.size() > 0) {
            for (Project project : projects) {
                queryBuilder.should(QueryBuilders.termQuery("parentProjectId", project.getId()));
            }
        }
        ArrayList<ExpensesIncurred> revenueExpense = Lists.newArrayList(expensesIncurredRepository.search(queryBuilder));
        return revenueExpense;
    }

    /**
     * 分页查询
     */
    @Override
    public PageDTO queryByPage(int page, int size) {
        List<Project> projects = projectService.queryAll();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (null != projects && projects.size() > 0) {
            for (Project project : projects) {
                queryBuilder.should(QueryBuilders.termQuery("parentProjectId", project.getId()));
            }
        }
        List<ExpensesIncurred> list = null;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<ExpensesIncurred> queryPage = expensesIncurredRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
        list = (queryPage == null || (list = queryPage.getContent()) == null) ? new ArrayList<ExpensesIncurred>() : list;
        long count = queryPage == null ? 0 : queryPage.getTotalElements();
        return new PageDTO(count, list, page, size);
    }


}
