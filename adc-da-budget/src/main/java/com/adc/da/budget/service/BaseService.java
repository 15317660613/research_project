package com.adc.da.budget.service;

import com.adc.da.budget.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by chenhaidong 2018/11/29
 */
public class BaseService<T,ID extends Serializable> {

    @Autowired
    ElasticsearchRepository<T,ID> elasticsearchRepository;


    /**
     * 分页查询
     */
    public PageDTO queryByPage(int page, int size){
        //分页条件判断
        if(page < 1) {
            page = 1;
        }
        if(size < 1) {
            size = 10;
        }
        List<T> queryList = null;
        long count = elasticsearchRepository.count();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<T> queryPage = elasticsearchRepository.findAll(new PageRequest(page - 1, size, sort));
        queryList =
                (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<T>() : queryList;
        return new PageDTO(count,queryList,page,size);
    }




}
