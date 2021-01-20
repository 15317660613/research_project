package com.adc.da.carsales.service;

import com.adc.da.budget.service.ProjectService;
import com.adc.da.carsales.dto.CarSalesDto;
import com.adc.da.carsales.page.CarSalesEOPage;
import com.adc.da.customerresourcemanage.dao.EnterpriseEODao;
import com.adc.da.customerresourcemanage.entity.EnterpriseEO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.DoubleUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.carsales.dao.CarSalesEODao;
import com.adc.da.carsales.entity.CarSalesEO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>DB_CAR_SALES CarSalesEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("carSalesEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CarSalesEOService extends BaseService<CarSalesEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CarSalesEOService.class);

    @Autowired
    private CarSalesEODao dao;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private EnterpriseEODao enterpriseEODao;
    @Autowired
    UserEODao userEODao;
    @Autowired
    private ProjectService projectService;
    public CarSalesEODao getDao() {
        return dao;
    }

    /**
     * 分页查询月度销量
     * @param page
     * @return
     * @throws Exception
     */
    public List<CarSalesEO> queryByPage(CarSalesEOPage page) throws Exception{
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        int total = dao.queryByCount(page);
        page.getPager().setRowCount(total);
        page.setOrderBy("cs.MONTH_SALES");
        page.setOrder("DESC");
        return dao.queryByPage(page);
    }

    /**
     * 不分页查询月度销量
     * @param page
     * @return
     * @throws Exception
     */
    public List<CarSalesEO> queryByList(CarSalesEOPage page) throws Exception{
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        int total = dao.queryByCount(page);
        page.getPager().setRowCount(total);
        page.setOrderBy("cs.MONTH_SALES");
        page.setOrder("DESC");
        return dao.queryByList(page);
    }

    /**
     * 新增车企月度销量
     * @param carSalesEO
     * @return
     * @throws Exception
     */
    public CarSalesEO create(CarSalesEO carSalesEO) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        carSalesEO.setId(UUID.randomUUID10());
        carSalesEO.setCreateUserId(user.getUsid());
        carSalesEO.setCreateUserName(user.getUsname());
        carSalesEO.setUpdateTime(new Date());
        carSalesEO.setDelFlag("0");
        if(StringUtils.isNotEmpty(carSalesEO.getGrowthRate())){
            carSalesEO.setGrowthRate(carSalesEO.getGrowthRate()+"%");
        }
        dao.insertSelective(carSalesEO);
        return  carSalesEO;
    }

    /**
     * 修改车企月度销量
     * @param carSalesEO
     * @return
     * @throws Exception
     */
    public CarSalesEO update(CarSalesEO carSalesEO) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if(StringUtils.isNotEmpty(carSalesEO.getGrowthRate()) && !carSalesEO.getGrowthRate().endsWith("%")){
            carSalesEO.setGrowthRate(carSalesEO.getGrowthRate()+"%");
        }
        carSalesEO.setCreateUserId(user.getUsid());
        carSalesEO.setCreateUserName(user.getUsname());
        carSalesEO.setUpdateTime(new Date());
        carSalesEO.setDelFlag("0");
        dao.updateByPrimaryKeySelective(carSalesEO);
        return  carSalesEO;
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    public void logicDeleteByPrimaryKey(String id) throws Exception{
        String userId = UserUtils.getUserId();
        if(StringUtils.isEmpty(userId)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        int num = dao.logicDeleteByPrimaryKey(id);
        if (num>0){}else{
            throw new AdcDaBaseException("删除失败，请检查！");
        }

    }
    /**
     * 批量删除
     * @param ids
     * @throws Exception
     */
    public void batchLogicDeleteByIds(List<String> ids) throws Exception{
        String userId = UserUtils.getUserId();
        if(StringUtils.isEmpty(userId)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        int num = dao.batchLogicDeleteByIds(ids);
        if (num>0){}else{
            throw new AdcDaBaseException("删除失败，请检查！");
        }
    }

    /**
     * 逻辑删除全部
     * @throws Exception
     */
    public void deleteAll() throws Exception{
        dao.batchDeleteAll();
    }
    /**
     * 导入企业月度销量
     * @param inputStream
     * @param importParams
     * @return
     * @throws Exception
     */
    public List<ExcelVerifyHanlderErrorResult> excelImportCarSales(
            InputStream inputStream, ImportParams importParams) throws Exception{
        ExcelImportResult<CarSalesDto> resutls = ExcelImportUtil.importExcelVerify(inputStream, CarSalesDto.class, importParams);
        List<ExcelVerifyHanlderErrorResult> errors = resutls.getErrors();
        this.importDatas(resutls.getList());
        return errors;
    }

    private void importDatas(List<CarSalesDto> carSalesDtos) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if (carSalesDtos.size()==0 || carSalesDtos==null){
            throw new AdcDaBaseException("导入文件中没有数据，请检查！");
        }

        for (CarSalesDto dto : carSalesDtos){
            CarSalesEO eo ;
            eo = beanMapper.map(dto,CarSalesEO.class);
            String enterpriseName = eo.getEnterpriseName();
            if(StringUtils.isEmpty(enterpriseName)){
                throw new AdcDaBaseException("企业名称不能为空，请检查！");
            }else{
                EnterpriseEO enterpriseEO = enterpriseEODao.findEnterpriseByName(enterpriseName);
                if (StringUtils.isEmpty(enterpriseEO)){
                    throw new AdcDaBaseException("企业名称不存在，请前往客户资源管理中添加！");
                }else {
                    eo.setEnterpriseId(enterpriseEO.getId());
                }
            }
            if (eo.getMonthSales() == null){
                throw new AdcDaBaseException("月度销量不能为空，请检查！");
            }
            String growthRate = eo.getGrowthRate();
            if (StringUtils.isEmpty(eo.getGrowthRate())){
                throw new AdcDaBaseException("增长率不能为空，请检查！");
            }else{
                Double temp_rate = Double.parseDouble(growthRate)*100;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                growthRate = decimalFormat.format(temp_rate);
                if (!growthRate.endsWith("%")){
                    growthRate += "%";
                }
                eo.setGrowthRate(growthRate);
            }
            eo.setId(UUID.randomUUID10());
            eo.setCreateUserId(user.getUsid());
            eo.setCreateUserName(user.getUsname());
            eo.setUpdateTime(new Date());
            eo.setDelFlag("0");
            dao.insertSelective(eo);
        }
    }

    /**
     * 导出车企数量
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook excelExportCarSales(ExportParams exportParams) throws Exception{
        CarSalesEOPage carSalesEOPage = new CarSalesEOPage();
        carSalesEOPage.setOrder("DESC");
        carSalesEOPage.setOrderBy("cs.MONTH_SALES");
        List<CarSalesEO> lists = dao.queryByList(carSalesEOPage);
        List<CarSalesDto> carSalesDtoList = new ArrayList<>();
        if (lists.size()>0){
            for (CarSalesEO carSalesEO : lists){
                CarSalesDto dto;
                dto = beanMapper.map(carSalesEO,CarSalesDto.class);
                carSalesDtoList.add(dto);
            }
        }
        return ExcelExportUtil.exportExcel(exportParams,CarSalesDto.class,carSalesDtoList);
    }

    /**
     * 车销售量排行
     * @return
     * @throws Exception
     */
    public List<CarSalesEO> carSalesRanking() throws Exception{
        List<CarSalesEO> carSalesEOS = dao.carSalesRanking();
        List<CarSalesEO> result = new ArrayList<>();
        //Map<String,Float> projectOwnerContractAmountMap  = projectService.getProjectOwnerContractAmountMap();
        Map<String,Float> projectOwnerContractAmountMap  = projectService.getTotalContractAmountMap();
        if (carSalesEOS.size()>0){
            //① 查询所有经营类项目  得出当年总合同金额
            //Float totalCount = projectService.getTotalContractAmount();
            Float totalCount = projectService.getTotalContractAmount(null);
            for (CarSalesEO carSalesEO : carSalesEOS){
                //销量以万为单位保留两位小数
                BigDecimal monthSales = carSalesEO.getMonthSales();
                BigDecimal monthSalesTemp = DoubleUtils.getDivideTenThousandScale2(monthSales);
                carSalesEO.setMonthSalesKanban(monthSalesTemp.toString());
                carSalesEO.setMonthSales(monthSalesTemp);

                String enterpriseName = carSalesEO.getEnterpriseName();
                //Float enterpriseCount = projectService.getTotalContractAmount(enterpriseName);
                Float enterpriseCount = projectOwnerContractAmountMap.get(enterpriseName);
                if (null == enterpriseCount){
                    enterpriseCount=0f;
                }
                //float proportion = enterpriseCount / totalCount * 100;
                BigDecimal proportion = BigDecimal.valueOf(enterpriseCount).divide(BigDecimal.valueOf(10000)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalCount),2, RoundingMode.HALF_UP);


//                carSalesEO.setProportion(DoubleUtils.formatToNumber(new BigDecimal(proportion)));
                carSalesEO.setProportion(DoubleUtils.formatToNumber(proportion));
                result.add(carSalesEO);
            }
        }
        return result;
    }




}
