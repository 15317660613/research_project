package com.adc.da.search.service;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.budget.query.BaseESQueryPage;
import com.adc.da.budget.query.QueryVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Calendar;
import java.util.List;

import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;
import static com.adc.da.budget.constant.ProjectSearchField.EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.GREATER_THAN;
import static com.adc.da.budget.constant.ProjectSearchField.GREATER_THAN_OR_EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.LESS_THAN;
import static com.adc.da.budget.constant.ProjectSearchField.LESS_THAN_OR_EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.LIKE;
import static com.adc.da.budget.constant.ProjectSearchField.NOT_EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.NOT_LIKE;

/**
 * @author Lee Kwanho 李坤澔
 *     date 2019-10-17
 */
public abstract class AbstractBaseSearchQuery<T extends BaseESQueryPage, E extends BaseEntity> {

    /**
     * 对时间进行搜索
     * = 与 ！= 条件一致
     *
     * @param queryVOProjectCreateDate
     * @param fieldName
     * @return
     */
    public QueryBuilder searchTime(QueryVO queryVOProjectCreateDate, String fieldName) {
        BoolQueryBuilder result = QueryBuilders.boolQuery();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        /*
         *  0.999sec
         *  1000 * 3600 * 24
         *  86,400,000‬
         */
        int oneSecond = 86400000;
        calendar.setTimeInMillis(Long.parseLong(queryVOProjectCreateDate.getName()));
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        switch (queryVOProjectCreateDate.getOperator()) {
            case EQUAL:
                result.must(QueryBuilders.rangeQuery(fieldName).from(calendar.getTimeInMillis()-1)
                                         .to(calendar.getTimeInMillis() + oneSecond - 1));

                break;
            case NOT_EQUAL:
                result.mustNot(QueryBuilders.rangeQuery(fieldName).from(calendar.getTimeInMillis())
                                            .to(calendar.getTimeInMillis() + oneSecond - 1));

                break;
            case GREATER_THAN_OR_EQUAL:
                result.must(QueryBuilders.rangeQuery(fieldName).gte(calendar.getTimeInMillis()-1));
                break;
            case GREATER_THAN:
                result.must(QueryBuilders.rangeQuery(fieldName).gt(calendar.getTimeInMillis() + oneSecond));
                break;
            case LESS_THAN_OR_EQUAL:
                result.must(QueryBuilders.rangeQuery(fieldName).lte(calendar.getTimeInMillis() + oneSecond));
                break;
            case LESS_THAN:
                result.must(QueryBuilders.rangeQuery(fieldName).lt(calendar.getTimeInMillis()));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    /**
     * 常量字段搜索
     *
     * @param queryVOProjectName
     * @param fieldName
     * @return
     */
    public QueryBuilder searchStringField(QueryVO queryVOProjectName, String fieldName) {
        BoolQueryBuilder result = QueryBuilders.boolQuery();
        switch (queryVOProjectName.getOperator()) {
            case EQUAL:
                result.must(QueryBuilders.termQuery(fieldName, queryVOProjectName.getName()));
                break;
            case NOT_EQUAL:
                result.mustNot(QueryBuilders.termQuery(fieldName, queryVOProjectName.getName()));
                break;
            case LIKE:
                result.must(QueryBuilders.wildcardQuery(fieldName, "*" + queryVOProjectName.getName() + "*"));
                break;
            case NOT_LIKE:
                result.mustNot(QueryBuilders.wildcardQuery(fieldName, "*" + queryVOProjectName.getName() + "*"));
                break;
            default:
                throw new IllegalArgumentException();
        }

        return result;

    }

    /**
     * 可以互操作序列
     * and 为 1
     * or 为 0
     *
     * @param allObject
     * @return
     */
    public int[] initOperate(List<QueryVO> allObject) {

        int size = allObject.size();
        int[] operate = new int[size];
        for (int i = 0; i < size; i++) {
            /*
             *  AND length is 3
             *  OR  length is 2
             *  so operate is  0 or 1
             */
            operate[i] = allObject.get(i).getLogic().length() - 2;
        }
        return operate;
    }

    /**
     * 拼装查询条件
     *
     * @param operate
     * @param allQuery
     */
    public BoolQueryBuilder initBoolQuery(
        int[] operate, List<QueryBuilder> allQuery) {
        int len = operate.length;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder subQueryResult = QueryBuilders.boolQuery();
        QueryBuilder subQuery;

        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));


        /*
         *  or == 0
         *  and == 1
         */
        final int and = 1;
        int operateFlag = 1;
        for (int i = 0; i < len; i++) {
            subQuery = allQuery.get(i);
            if (and == operateFlag) {
                subQueryResult.must(subQuery);
            } else {
                subQueryResult.should(subQuery);
            }

            operateFlag = operate[i];
            if (and == operateFlag) {
                subQueryResult = QueryBuilders.boolQuery().must(subQueryResult);
            } else {
                subQueryResult = QueryBuilders.boolQuery().should(subQueryResult);
            }
        }

        return boolQueryBuilder.must(subQueryResult);

    }

    /**
     * 对所有入参元素进行初始化，预拼接查询条件
     *
     * @param allObject
     * @return
     */
    protected abstract List<QueryBuilder> initAllQuery(List<QueryVO> allObject);

    protected abstract List<E> doSearch(SearchQuery searchQuery, T page);

    /**
     * 初始化查询
     *
     * @param page
     * @return
     */
    public BoolQueryBuilder initQueryBuilder(T page) {
        List<QueryVO> allObject = page.getAllObject();
        int[] operate = initOperate(allObject);
        List<QueryBuilder> allQuery = initAllQuery(allObject);
        return initBoolQuery(operate, allQuery);
    }
}
