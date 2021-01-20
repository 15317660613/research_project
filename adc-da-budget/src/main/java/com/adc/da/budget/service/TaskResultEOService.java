package com.adc.da.budget.service;

import com.adc.da.base.page.Pager;
import com.adc.da.budget.page.TaskResultEOPage;
import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.file.dao.FileEODao;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.page.MyFilePage;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.TaskResultEODao;
import com.adc.da.budget.entity.TaskResultEO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

/**
 *
 * <br>
 * <b>功能：</b>PF_TASK_RESULT TaskResultEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("taskResultEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class TaskResultEOService extends BaseService<TaskResultEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(TaskResultEOService.class);

    @Autowired
    private TaskResultEODao dao;
    @Autowired
    private UserEODao userEODao ;
    @Autowired
    private FileEODao fileEODao ;

    public TaskResultEODao getDao() {
        return dao;
    }

     public void insertList(TaskResultEO[] taskResultEOS){

        for (TaskResultEO taskResultEO : taskResultEOS){
            taskResultEO.setId(UUID.randomUUID10());
            taskResultEO.setCreateTime(new Date());
            dao.insertSelective(taskResultEO);
        }
    }

    public void insertList(List<TaskResultEO> taskResultEOS,String taskId){

        for (TaskResultEO taskResultEO : taskResultEOS){
            taskResultEO.setId(UUID.randomUUID10());
            taskResultEO.setTaskId(taskId);
            taskResultEO.setCreateTime(new Date());
            dao.insertSelective(taskResultEO);
        }
    }

    public void updateList(TaskResultEO[] taskResultEOS){

        for (TaskResultEO taskResultEO : taskResultEOS){
            if (StringUtils.isEmpty(taskResultEO.getId())){
                taskResultEO.setId(UUID.randomUUID10());
                taskResultEO.setCreateTime(new Date());
                dao.insertSelective(taskResultEO);
            }else {
                dao.updateByPrimaryKeySelective(taskResultEO);
            }
        }
    }

    public void updateList(List<TaskResultEO> taskResultEOS , String taskId) throws Exception{
        List<TaskResultEO> dataBaseList =  selectByTaskId(taskId);
        for (TaskResultEO dataBaseTaskResultEO : dataBaseList){
            boolean isDelete = true;
            for (TaskResultEO newTaskResultEO : taskResultEOS) {
                if (StringUtils.equals(dataBaseTaskResultEO.getId(),newTaskResultEO.getId())){
                    isDelete = false;
                }
            }
            if (isDelete){
                deleteByPrimaryKey(dataBaseTaskResultEO.getId());
            }
        }

        for (TaskResultEO taskResultEO : taskResultEOS){
            if (StringUtils.isEmpty(taskResultEO.getId())){
                taskResultEO.setTaskId(taskId);
                taskResultEO.setId(UUID.randomUUID10());
                taskResultEO.setCreateTime(new Date());
                dao.insertSelective(taskResultEO);
            }else {
                dao.updateByPrimaryKeySelective(taskResultEO);
            }
        }
    }

    public void deleteByPrimaryKeys(List<String> idList){
        dao.deleteByPrimaryKeys(idList);
    }

    public void deleteAllByTaskId(String taskId){
        dao.deleteAllByTaskId(taskId);

    }


    public List<TaskResultEO> selectByTaskId(String value){

        return dao.selectByTaskId(value);
    }

    public PageInfo<TaskResultVO>queryTaskResultFileByPage(String id ,
            int pageIndex  , int size){
        TaskResultEOPage page = new TaskResultEOPage();
        MyFilePage myFilePage = new MyFilePage();
        myFilePage.setPage(pageIndex);
        myFilePage.setPageSize(size);

        page.setTaskId(id);
        page.setPage(1);
        page.setPageSize(1000);

        List<TaskResultVO> taskResultVOList = new ArrayList<>() ;
        List<String> foreignIdList = new ArrayList<>();

        List<TaskResultEO> taskResultEOList = dao.queryByPage(page);
        for (TaskResultEO eo : taskResultEOList){
            foreignIdList.add( eo.getId() ) ;
        }
        Set<String> taskResultIdSet = new HashSet<>(foreignIdList);
        foreignIdList.add(id);
        myFilePage.setForeignIdList(foreignIdList);
        List<FileEO>  rows = fileEODao.queryFileByPageForeignKey(myFilePage);
        Integer rowCount = fileEODao.queryByCountForeignKey(myFilePage);
        myFilePage.getPager().setRowCount(rowCount);

        for(FileEO fileEO : rows){
            TaskResultVO taskResultVO = new TaskResultVO() ;

            UserEO userEO = userEODao.selectByPrimaryKey(fileEO.getUserId());
            if (null != userEO){
                taskResultVO.setUploadUserName(userEO.getUsname());
            }
            if (taskResultIdSet.contains(fileEO.getForeignId())){
                taskResultVO.setId(fileEO.getForeignId());
                for (TaskResultEO taskResultEO: taskResultEOList){
                    if (StringUtils.equals(taskResultEO.getId(),fileEO.getForeignId())){
                        taskResultVO.setResultType(taskResultEO.getResultType());
                    }
                }
            }
            taskResultVO.setFileId(fileEO.getFileId());
            taskResultVO.setFileSize(fileEO.getFileSize());
            taskResultVO.setUploadTime(fileEO.getCreateTime());
            taskResultVO.setFileName(fileEO.getFileName());
            taskResultVO.setUploadUserId(fileEO.getUserId());
            taskResultVO.setFileType(fileEO.getFileType());

            taskResultVOList.add(taskResultVO);
        }

        return getMyPageInfo(myFilePage.getPager(),taskResultVOList);
    }

    private PageInfo<TaskResultVO> getMyPageInfo(Pager pager, List<TaskResultVO> rows) {
        PageInfo<TaskResultVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }


}
