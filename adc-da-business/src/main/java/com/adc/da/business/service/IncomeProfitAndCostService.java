package com.adc.da.business.service;

import com.adc.da.business.vo.DeptOperationVO;
import com.adc.da.statistics.dao.BusinessWorktimeDao;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * describe:
 * 1.7 加入工时数据
 *
 * @author 李坤澔
 *     date 2019-06-13
 */
@Service
public class IncomeProfitAndCostService {

    @Autowired
    private BusinessWorktimeDao businessWorktimeDao;

    public List<DeptOperationVO> initWorkTime(List<DeptOperationVO> deptOperationVOList,
        String thisYear, String startMonth, String endMonth) {
        //1.查找所有部门
        for (DeptOperationVO result : deptOperationVOList) {
            Double workTime = businessWorktimeDao.queryDeptWorkTimeByYearAndMonth(
                Integer.valueOf(thisYear),
                Integer.valueOf(startMonth),
                Integer.valueOf(endMonth),
                result.getDeptId()
            );
            //保留2位小数
            if (null != workTime) {
                BigDecimal workTimeTep = new BigDecimal(workTime);
                workTime = workTimeTep.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            result.setActualWorkTime(workTime);
        }
        return deptOperationVOList;
    }

    /**
     * 组装搜索条件
     *
     * @param deptIdList
     * @return
     */
    private BoolQueryBuilder deptIdQueryBuilder(List<String> deptIdList) {

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        for (String superOrgId : deptIdList) {
            qb.should(QueryBuilders.matchQuery("superOrgId", superOrgId));
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .must(QueryBuilders.termQuery("type", "year"))
                                                     .mustNot(QueryBuilders.termQuery("delFlag", true));
        return queryBuilder.must(qb);

    }

}
