package com.adc.da.statistics.service;

import com.adc.da.base.entity.TreeEntity;
import com.adc.da.base.service.BaseService;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.statistics.dao.DataBoardOrgEODao;
import com.adc.da.statistics.dto.DataBoardOrgDTO;
import com.adc.da.statistics.entity.DataBoardOrgEO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>功能：</b>ST_DATA_BOARD_ORG DataBoardOrgEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-15 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("dataBoardOrgEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class DataBoardOrgEOService extends BaseService<DataBoardOrgEO, String> {

    @Autowired
    private DataBoardOrgEODao dao;

    @Autowired
    private OrgEODao orgEODao;

    public DataBoardOrgEODao getDao() {
        return dao;
    }

    /**
     * 读取Excel 2019年度部门费用明细表
     *
     * @param wb
     * @param year
     */
    public void insertExpenditureDataFromExcel(Workbook wb, int year) {

        Sheet sheet = wb.getSheetAt(0);
        int rowStart = 2;
        int rowEnd = sheet.getLastRowNum();

        List<DataBoardOrgDTO> dtoList = new ArrayList<>();

        for (int i = rowStart; i <= rowEnd; i++) {
            DataBoardOrgDTO eo = new DataBoardOrgDTO();

            Row row = sheet.getRow(i);
            if (null == row) {
                break;
            }
            /* 月 */
            eo.setYear(year);
            eo.setMonth(Double.valueOf(getCellStringValue(row.getCell(0))).intValue());
            eo.setDay(Double.valueOf(getCellStringValue(row.getCell(1))).intValue());
            eo.setDeptName(getCellStringValue(row.getCell(5)));
            eo.setData(Double.valueOf(getCellStringValue(row.getCell(8))));
            dtoList.add(eo);

        }

        /*
         * <name, id>
         */
        String baseId = UUID.randomUUID10() + "_E";
        List<DataBoardOrgEO> saveList = initSaveList(dtoList, baseId, INT_EXPENDITURE);

        saveData(saveList);

    }

    /**
     * 应收  2019年度应收账款余额表
     *
     * @param wb
     */
    public void insertCreditDataFromExcel(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        int rowStart = 2;
        int rowEnd = sheet.getLastRowNum();

        List<DataBoardOrgDTO> dtoList = new ArrayList<>();

        for (int i = rowStart; i <= rowEnd; i++) {

            DataBoardOrgDTO eo = new DataBoardOrgDTO();

            Row row = sheet.getRow(i);
            if (null == row || StringUtils.isEmpty(getCellStringValue(row.getCell(4)))) {
                log.warn("stop row is {}", i + 3);
                break;
            }
            try {
                String data = getCellStringValue(row.getCell(8));
                String time = getCellStringValue(row.getCell(12));
                if (StringUtils.isNotEmpty(data)
                    && StringUtils.isNotEmpty(time)) {
                    /* 时间只取前6位 */
                    String yearStr = time.substring(0, 4);
                    String monthStr = time.substring(4, 6);
                    eo.setYear(Integer.parseInt(yearStr));
                    eo.setMonth(Integer.parseInt(monthStr));
                    /* 部门名称位第 4 列 */
                    eo.setDeptName(getCellStringValue(row.getCell(3)));
                    eo.setData(Double.valueOf(getCellStringValue(row.getCell(8))));
                    dtoList.add(eo);
                } else {
                    log.warn("row num {} not insert", i + 3);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }

        String baseId = UUID.randomUUID10() + "_C";
        List<DataBoardOrgEO> saveList = initSaveList(dtoList, baseId, INT_CREDIT);

        saveData(saveList);
    }

    /**
     * 支出
     */
    private static final int INT_EXPENDITURE = 0;

    /**
     * 进账
     */
    private static final int INT_CREDIT = 1;

    /**
     * 对数据进行初始化
     *
     * @param dtoList
     * @param baseId
     * @param type
     * @return
     */
    private List<DataBoardOrgEO> initSaveList(List<DataBoardOrgDTO> dtoList, String baseId, final int type) {
        List<DataBoardOrgEO> saveList = new ArrayList<>();
        Map<String, String> orgNameIdMap = initOrgNameIdMap();
        int[] i = {0};
        dtoList.forEach(eo -> {
            DataBoardOrgEO saveEO = new DataBoardOrgEO();
            saveEO.setId(baseId + "_" + i[0]++);
            saveEO.setMonth(eo.getMonth());
            saveEO.setYear(eo.getYear());
            if (type == INT_EXPENDITURE) {
                saveEO.setExpenditure(eo.getData());
            } else if (type == INT_CREDIT) {
                saveEO.setCredit(eo.getData());
            }

            String id = orgNameIdMap.get(eo.getDeptName());
            if (null != id) {
                saveEO.setDeptId(id);
            } else {
                saveEO.setExtInfo01(eo.getDeptName());
            }

            saveList.add(saveEO);
        });
        return saveList;
    }

    /**
     * @return <Name,Id>
     * @author Lee Kwanho 李坤澔
     *     date 2019-11-19
     **/
    private Map<String, String> initOrgNameIdMap() {

        if (orgEODao != null) {
            List<OrgEO> orgEOList = orgEODao.queryOrgAll();
            /*
             * <name, id>
             */
            return orgEOList.stream()
                            .collect(Collectors
                                .toMap(TreeEntity::getName, TreeEntity::getId, (a, b) -> b));
        } else {
            /* 非Spring环境下 ，用于测试*/
            return new HashMap<>();
        }

    }

    /**
     * save into Oracle
     *
     * @param saveList
     */
    private void saveData(List<DataBoardOrgEO> saveList) {
        if (dao != null) {
            /*
             * 实际运行时，执行保存
             */
            List<List<DataBoardOrgEO>> splitList = CommonUtil.splitList(saveList, 512);
            splitList.forEach(dao::insertSelectiveAll);
        }
    }

    /**
     * 获取cell值，统一当String返回
     *
     * @param cell
     * @return
     */
    private String getCellStringValue(Cell cell) {
        String result;
        if (cell == null) {
            return null;
        }
        switch (cell.getCellTypeEnum()) {
            case _NONE:
            case BLANK:
                result = "";
                break;
            case STRING:
                result = cell.getStringCellValue();
                break;
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
            case NUMERIC:
                result = String.valueOf(cell.getNumericCellValue());
                break;
            case ERROR:
            default:
                throw new AdcDaBaseException("error");
        }
        return result;
    }

}
