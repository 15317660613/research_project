package com.adc.da.budget.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.FileEPDao;
import com.adc.da.budget.entity.FileEO;
import com.adc.da.budget.page.FileEOPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author qichunxu
 * @date 2019-04-23
 */
@Service
@Transactional(
        value = "transactionManager",
        readOnly = false,
        propagation = Propagation.REQUIRED,
        rollbackFor = {Throwable.class}
)
public class FileEPService extends BaseService<FileEO, String> {
    @Autowired
    private FileEPDao dao;

    public FileEPDao getDao() {
        return this.dao;
    }

    public List<FileEO> queryByPage(FileEOPage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return dao.queryByPage(page);
    }
}
