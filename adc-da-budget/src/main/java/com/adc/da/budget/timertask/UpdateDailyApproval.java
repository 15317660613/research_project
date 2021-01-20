package com.adc.da.budget.timertask;

import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.util.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * describe:
 * 强制审批
 *
 * @author 李坤澔
 *     date 2019-06-01
 */
@Component
@Slf4j
public class UpdateDailyApproval {
    @Autowired
    private DailyRepository dailyRepository;

    Integer timeLoop = 0;

    /**
     * 使用时将@Scheduled 注释取消即可
     */
    //  @Scheduled(cron = "0/5 * * ? * *")
    public void startTask() {

        if (timeLoop > 0) {
            return;
        } else {
            timeLoop++;
        }

        String userId = "";
        List<String> dailyList = new ArrayList<>();

        runTask(userId, dailyList);

    }

    /**
     * 指定用户id进行未审批日报的更新操作
     *
     * @param userId
     */
    public void runTask(String userId, List<String> dailyIdList) {

        Iterable<Daily> dailyIterable;

        if (CollectionUtils.isNotEmpty(dailyIdList)) {
            //根据日报id获取
            dailyIterable = queryBuild(dailyIdList);

        } else {
            //根据用户id获取
            dailyIterable = queryBuild(userId);

        }

        if (dailyIterable.iterator().hasNext()) {
            saveAll(dailyIterable);
        }

    }

    /**
     * 进行es查询
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private Iterable<Daily> queryBuild(String userId) {
        BoolQueryBuilder queryBuilder =
            QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));

        queryBuilder.must(QueryBuilders.termQuery("approveState", 2));
        queryBuilder.must(QueryBuilders.termQuery("createUserId", userId));

        return dailyRepository.search(queryBuilder);

    }

    /**
     * 进行id序列查询
     *
     * @param dailyIdList
     * @return
     */
    private Iterable<Daily> queryBuild(List<String> dailyIdList) {
        BoolQueryBuilder queryBuilder =
            QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));

        queryBuilder.must(QueryBuilders.termQuery("approveState", 2));

        /*
         * 拼接部门条件
         */
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        for (String id : dailyIdList) {
            qb.should(QueryBuilders.termQuery("id", id));
        }

        queryBuilder.must(qb);
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
            // 1为审批通过 3为草稿
            dailyNew.setApproveState(1);
            dailyNew.setModifyTime(new Date());
            dailyList.add(dailyNew);
        }

        dailyRepository.save(dailyList);
        log.info("记录更新完毕");
    }
}


