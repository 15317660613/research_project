package com.adc.da.file.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.file.dao.MyFileEODao;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.page.MyFilePage;
import com.adc.da.progress.page.ProjectMilepostEOPage;
import com.adc.da.progress.vo.ProjectMilepostResultVO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>TS_FILE FileEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2017-12-24 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 *
 * @see MyFileEODao
 * @see mybatis.mapper.file
 */
@Service("myFileEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class)
public class MyFileEOService extends BaseService<MyFileEO, String> {
    
    /**
     * @see MyFileEODao
     */
    @Autowired
    private MyFileEODao dao;
    @Autowired
    private ProjectMilepostEODao projectMilepostEODao ;
    @Autowired
    private ProjectMilepostResultEODao projectMilepostResultEODao ;
    
    public MyFileEODao getDao() {
        return dao;
    }

    public List<MyFileEO> selectByForeignId( String foreignId){
        return dao.selectByForeignId(foreignId);
    }

    public void deleteByFileId(String fileId){
        dao.deleteByFileId(fileId);
    }


    public void moveFile(List<String> fileIdList ,  String foreignId ){
        dao.moveFile(fileIdList ,foreignId);
    }


    public List<MyFileEO> queryFileByPage(MyFilePage page)throws Exception{
        List<String> projectMilepostEOIds = new ArrayList<>();
        List<String> projectMilepostResultEOIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(page.getStageId())){
            List<ProjectMilepostEO> projectMilepostEOList = projectMilepostEODao.selectByStageId(page.getStageId()) ;
            if (CollectionUtils.isNotEmpty(projectMilepostEOList)){

                for (ProjectMilepostEO milepostEO:projectMilepostEOList){
                    projectMilepostEOIds.add(milepostEO.getId());
                    List<ProjectMilepostResultEO> resultEOList = projectMilepostResultEODao.selectByMilepostId(milepostEO.getId());
                    if (CollectionUtils.isEmpty(resultEOList)){
                        continue;
                    }
                    for (ProjectMilepostResultEO projectMilepostResultEO : resultEOList){
                        projectMilepostResultEOIds.add(projectMilepostResultEO.getId());
                    }
                }

            }
        }
        if (StringUtils.isNotEmpty(page.getMilepostId())){
            List<ProjectMilepostResultEO> resultEOList = projectMilepostResultEODao.selectByMilepostId(page.getMilepostId());
            if (CollectionUtils.isNotEmpty(resultEOList)) {
                for (ProjectMilepostResultEO projectMilepostResultEO : resultEOList) {
                    projectMilepostResultEOIds.add(projectMilepostResultEO.getId());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(projectMilepostEOIds)){
            page.setMilepostIdList(projectMilepostEOIds);

        }
        if (CollectionUtils.isNotEmpty(projectMilepostResultEOIds)){
            page.setMilepostResultIdList(projectMilepostResultEOIds);
        }



        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);

        return dao.queryFileByPage(page);
    }


    
}
