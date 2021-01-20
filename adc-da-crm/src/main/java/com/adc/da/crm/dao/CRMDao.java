package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.crm.page.AdcFormDataVOPage;
import com.adc.da.form.entity.AdcFormDataEO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author ZhengZhiwei
 * @description
 * @date create at 13:16 2018/11/22
 * @modified by
 */
public interface CRMDao extends BaseDao<AdcFormDataEO> {
    List<AdcFormDataEO> queryPageByFormId(AdcFormDataVOPage page);

    String queryServiceNameByFormId(String formId);

	List<Map> queryExport1();

	List<Map> queryExport2();

	List<Map> queryExport3();

	List<Map> queryExport4();

	List<Map> queryExport5();

	List<Map> queryExport6();

	List<Map> queryExport7();

	List<Map> queryExport8();

	List<Map> queryExport9(@Param(value = "month") String month,@Param(value = "year") String year);

	List<Map> queryExport10(@Param(value = "month") String month,@Param(value = "year") String year);
}
