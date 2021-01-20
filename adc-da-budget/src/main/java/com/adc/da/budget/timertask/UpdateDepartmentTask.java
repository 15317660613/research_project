package com.adc.da.budget.timertask;

import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.util.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:
 * 该定时器用于更新Daily的deptId信息
 *
 * @author 李坤澔
 *     date 2019-06-01
 */
@Component
@Slf4j
public class UpdateDepartmentTask {
    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private OrgListDao orgListDao;

    /**
     * 数据中心员工不超过580，因此初始容量设置为580
     */
    private Map<String, String> userIdOrgIdMap = new HashMap<>(580);

    /**
     * 使用时将@Scheduled 注释取消即可，
     * 存入es方法为 dailyRepository.save(dailyList) 默认也是注释的请放开注释
     */
//    @Scheduled(cron = "0/5 * * ? * *")
    public void startTask() {

        List<OrgWithLevelEO> userList = orgListDao.getMinUserOrgIdInfo();
        //若userList为空后续操作无意义
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        //对map进行初始化
        for (OrgWithLevelEO eo : userList) {
            userIdOrgIdMap.put(eo.getUserId(), eo.getId());
        }

        /*
         *  1天的长度
         */
        Long oneDay = 86400000L;

        /* 2019-01-01  1546272000000L */
        Long startLong = 1546272000000L;

        /*2020-01-01*/
        Long finalLong = 1577808000000L;
        for (int i = 1; ; i++) {
            Long endLong = startLong + oneDay;

            Iterable<Daily> dailyIterable = queryBuild(startLong, endLong);
            if (dailyIterable.iterator().hasNext()) {
                saveAll(dailyIterable);
            }
            startLong = endLong;
            log.warn("day {}", i);
            if (startLong > finalLong) {
                break;
            }
        }

    }

    /**
     * 进行es查询
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private Iterable<Daily> queryBuild(Long start, Long end) {
        BoolQueryBuilder queryBuilder =
            QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));

        /*
         * 拼接部门条件，不存在dailyCreateTime字段的记录
         */
        queryBuilder.must(
            QueryBuilders.rangeQuery("dailyCreateTime")
                         .from(start)
                         .to(end)
                         //包含下界
                         .includeLower(true)
                         //不包含上界
                         .includeUpper(false));

        return dailyRepository.search(queryBuilder);

    }

    /**
     * 更新全部记录
     *
     * @param dailyIterable
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private void saveAll(Iterable<Daily> dailyIterable) {
        List<Daily> dailyList = new ArrayList<>();

        for (Daily dailyNew : dailyIterable) {
            String userId = dailyNew.getCreateUserId();

            if (!dailyNew.getDeptId().equals(userIdOrgIdMap.get(userId))) {
                dailyNew.setDeptId(userIdOrgIdMap.get(userId));
                dailyList.add(dailyNew);
            }
        }
        if (CollectionUtils.isNotEmpty(dailyList)) {
            dailyRepository.save(dailyList);

            log.warn("记录更新完毕 size{}", dailyList.size());
        }
    }
}


