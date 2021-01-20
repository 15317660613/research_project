package com.adc.da.research.personnel.dao;

import com.adc.da.research.personnel.vo.ExpertGroupUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专家组-专家人员关联dao
 */
public interface ExpertGroupUserDao {

    void deleteByUserId(@Param("userId") String userId);

    void insertInfoBatch(@Param("expertGroupUserVOS")List<ExpertGroupUserVO> expertGroupUserVOS);
}
