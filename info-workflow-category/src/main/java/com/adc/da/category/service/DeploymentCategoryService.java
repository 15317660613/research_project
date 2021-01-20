package com.adc.da.category.service;

import com.adc.da.category.dao.MyCategoryEODao;
import com.adc.da.category.dao.MyModelCategoryEODao;
import com.adc.da.category.entity.MyCategoryEO;
import com.adc.da.category.entity.MyModelCategoryEO;
import com.adc.da.category.entity.MyCategoryEO;
import com.adc.da.category.page.MyCategoryEOPage;
import com.adc.da.category.page.MyModelCategoryEOPage;
import com.adc.da.util.http.PageInfo;
import com.adc.da.workflow.service.ActivitiProcessService;
import com.adc.da.workflow.vo.ActivitiProcessDefinitionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:
 * 只读sql
 *
 * @author 李坤澔
 *     date 2019-08-12
 */
@Service("MyDeploymentCategoryService")
@Transactional(value = "transactionManager", readOnly = true,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeploymentCategoryService {
    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private MyModelCategoryEODao dao;

    @Autowired
    private MyCategoryEODao categoryEODao;

    public List<MyCategoryEO> getList(Integer pageSize, Integer pageNo, String name, boolean deployedFlag) {

        PageInfo<ActivitiProcessDefinitionVO> data = activitiProcessService
            .pageList(pageSize, pageNo, name, deployedFlag);

        Map<String, Integer> keyIndexMap = new HashMap<>();
        Map<String, List<ActivitiProcessDefinitionVO>> keyIndexMapResult = new HashMap<>();

        List<MyModelCategoryEO> baseDataList = dao.queryByList(new MyModelCategoryEOPage());
        List<ActivitiProcessDefinitionVO> dataList = data.getList();

        List<MyCategoryEO> resultList = categoryEODao.queryByList(new MyCategoryEOPage());
        int i = 0;
        for (MyModelCategoryEO eo : baseDataList) {
            keyIndexMap.put(eo.getProcDefKey(), i++);
        }

        MyModelCategoryEO categoryEO;
        /*
         * 对需要分组的 流程进行处理，不需要的直接排除
         */
        List<ActivitiProcessDefinitionVO> subResultList;
        String key;
        String categoryId;
        for (ActivitiProcessDefinitionVO vo : dataList) {
            key = vo.getProcessKey();
            if (keyIndexMap.containsKey(key)) {
                /*
                 * modelId: "5862569"
                 * processId: "p1be7d235071f44738b0b246741c95938:2:6060315"
                 * processKey: "p1be7d235071f44738b0b246741c95938"
                 * processName: "项目管理-经营-考核（绩效）-07-2"
                 * processVersion: "2"
                 * publishStatus: "1"
                 * publishTime: "2019-07-23 17:03:18"
                 */
                categoryEO = baseDataList.get(keyIndexMap.get(vo.getProcessKey()));
                categoryId = (categoryEO.getCategoryId());

                if (keyIndexMapResult.containsKey(categoryId)) {
                    subResultList = keyIndexMapResult.get(categoryId);
                    subResultList.add(vo);
                    keyIndexMapResult.put(categoryId, subResultList);

                } else {
                    subResultList = new ArrayList<>();
                    subResultList.add(vo);
                    keyIndexMapResult.put(categoryId, subResultList);

                }

            }
        }

        for (MyCategoryEO eo : resultList) {
            eo.setDeploymentList(keyIndexMapResult.get(eo.getId()));
        }

        return resultList;
    }
}
