package com.adc.da.budget.service;

import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.BusinessRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.vo.BusinessVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 14:33
 * @Description:
 */
@Service
public class BuisnessService {

    private Logger logger = LoggerFactory.getLogger(BuisnessService.class);

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private ProjectRepository projectRepository;
    /**
     *
     * @Description: 新增
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:37
     */
    public Business insert(BusinessVO businessVo){
        Business business = beanMapper.map(businessVo, Business.class);
        business.setId(null);
        business.setCreateTime(new Date());
        business.setModifyTime(new Date());
        return businessRepository.save(business);
    }
   /**
    *
    * @Description:  根据id删除 可以是ids
    * @auther: ZHAIKAIXUAN
    * @params:
    * @return:
    * @date: 2018/11/19 14:44
    */
   @Transactional
    public boolean deleteBatch(String ids) {
        List<String> idList = businessTreeIds(ids.split(","));
        //检测业务类型是否被使用
        BoolQueryBuilder projectBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("businessId", idList));
       Iterator<Project> projectIterator = projectRepository.search(projectBuilder).iterator();
       if(projectIterator.hasNext()){
            throw new AdcDaBaseException("业务类型已被项目使用");
        }
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        for (String itemId : idList){
            businessRepository.delete(itemId);
        }
        return true;
    }

    /**
     *
     * @Description:  修改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */

    @Transactional
    public Business update(BusinessVO businessvo){
        try{
            // 删除
            Business business = businessRepository.findOne(businessvo.getId());
            if(business == null){
                return null;
            }
            if(StringUtils.isNotEmpty(businessvo.getParentId())){
                Business parentBusiness = businessRepository.findOne(businessvo.getParentId());
                if(parentBusiness == null && !"1".equals(businessvo.getParentId())){
                    return null;
                }
                business.setParentId(businessvo.getParentId());
            }else if("".equals(businessvo.getParentId())){
                business.setParentId("");
            }

            business.setName(businessvo.getName());
            business.setModifyTime(new Date());
            // 保存
            return businessRepository.save(business);
        }catch (Exception e){
            logger.info(e.getMessage());
            return null;
        }
    }
    /**
     *
     * @Description: 查找
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:45
     */
    public Business querySingle(String id){
        Iterable<Business> businessIterable = businessRepository.findAll();
        return businessToTree(businessIterable, id);
    }

    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<Business> datas = ExcelImportUtil.importExcel(is, Business.class, params );
        //导入
        for(Business business : datas){
            business.setModifyTime(new Date());
            business.setCreateTime(new Date());
            business.setId(UUID.randomUUID().toString());
            businessRepository.save(business);
        }
    }



    public Workbook excelExport(ExportParams params) {
        List<Business> oneList = Lists.newArrayList(businessRepository.findAll());
        return ExcelExportUtil.exportExcel(params,Business.class,oneList);
    }

    public List<Business> queryAll() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Iterable<Business> businessIterable = businessRepository.findAll(sort);
        return Lists.newArrayList(businessIterable);
    }

    public Business businessToTree(Iterable<Business> businessIterable, String id){
        Iterator<Business> businessIterator = businessIterable.iterator();
        Business business = null;
        while (businessIterator.hasNext()){
            business = businessIterator.next();
            if(id.equals(business.getId())){
                recursiveBusiness(businessIterable, business);
                break;
            }else{
                business = null;
            }
        }
        return business;
    }

    public List<Business> businessToFullTree(Iterable<Business> businessIterable){
        Iterator<Business> businessIterator = businessIterable.iterator();
        List<Business> businessList = new ArrayList<>();
        while (businessIterator.hasNext()){
            Business business = businessIterator.next();
            if("".equals(business.getParentId())){
                recursiveBusiness(businessIterable, business);
                businessList.add(business);
            }
        }
        return businessList;
    }

    private List<String> businessTreeIds(String[] ids){
        List<String> idList = new ArrayList<>();
        Iterable<Business> businessIterable = businessRepository.findAll();
        for(String id : ids){
            Iterator<Business> businessIterator = businessIterable.iterator();
            while (businessIterator.hasNext()){
                Business business = businessIterator.next();
                if(id.equals(business.getId())){
                    List<String> childIds = recursiveBusinessIds(businessIterable, business);
                    idList.addAll(childIds);
                    idList.add(business.getId());
                    break;
                }
            }
        }

        return idList;
    }

    private List<String> recursiveBusinessIds(Iterable<Business> businessIterable, Business business){
        Iterator<Business> businessIterator = businessIterable.iterator();
        List<String> ids = new ArrayList<>();
        while (businessIterator.hasNext()){
            Business itemBusiness = businessIterator.next();
            if(business.getId().equals(itemBusiness.getParentId())){
                List<String> childIds = recursiveBusinessIds(businessIterable, itemBusiness);
                ids.addAll(childIds);
                ids.add(itemBusiness.getId());
            }
        }
        return ids;
    }

    private void recursiveBusiness(Iterable<Business> businessIterable, Business business){
        Iterator<Business> businessIterator = businessIterable.iterator();
        List<Business> businessList = new ArrayList<>();
        while (businessIterator.hasNext()){
            Business itemBusiness = businessIterator.next();
            if(business.getId().equals(itemBusiness.getParentId())){
                recursiveBusiness(businessIterable, itemBusiness);
                businessList.add(itemBusiness);
            }
        }
        business.setBusinessList(businessList);
    }
}
