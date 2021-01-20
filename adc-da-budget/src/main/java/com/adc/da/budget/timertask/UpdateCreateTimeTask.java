package com.adc.da.budget.timertask;

import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.repository.DailyRepository;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 * 该定时器用于执行Daily中丢失dailyCreateTime的数据进行恢复,使用
 *
 * @author 李坤澔
 *     date 2019-06-01
 */
@Component
@Slf4j
public class UpdateCreateTimeTask {
    @Autowired
    private DailyRepository dailyRepository;

    /**
     * 使用时将@Scheduled 注释取消即可
     */
//    @Scheduled(cron = "0/5 * * ? * *")
    public void startTask() {

        Iterable<Daily> dailyIterable = queryBuild();
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
    private Iterable<Daily> queryBuild() {
        BoolQueryBuilder queryBuilder =
            QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));

        /*
         * 拼接部门条件，不存在dailyCreateTime字段的记录
         */
        ExistsQueryBuilder qb = QueryBuilders.existsQuery("dailyCreateTime");
        queryBuilder.mustNot(qb);
        return dailyRepository.search(queryBuilder);

    }

    /**
     * 进行es查询
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private Iterable<Daily> queryBuilds() {
        BoolQueryBuilder queryBuilder =
                QueryBuilders
                        .boolQuery()
                        .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));

        /*
         * 拼接部门条件，不存在dailyCreateTime字段的记录
         */
        ExistsQueryBuilder qb = QueryBuilders.existsQuery("approveState");
        queryBuilder.mustNot(qb);
        return dailyRepository.search(queryBuilder);

    }



    /**
     * 更新第一条记录
     *
     * @param dailyIterable
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private void saveOne(Iterable<Daily> dailyIterable) {
        Daily daily = dailyIterable.iterator().next();
        if (daily.getCreateTime() != null) {
            daily.setDailyCreateTime(daily.getCreateTime());
        }
        dailyRepository.save(daily);
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
            if (dailyNew.getCreateTime() != null) {
                dailyNew.setDailyCreateTime(dailyNew.getCreateTime());
                dailyList.add(dailyNew);
            }
        }

        dailyRepository.save(dailyList);
        log.info("记录更新完毕");
    }
}


