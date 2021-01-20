package com.adc.da.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.dto.NoticeUserVO;
import com.adc.da.dto.PageDTO;
import com.adc.da.entity.NoticeUserEO;
import com.adc.da.util.utils.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeUserEODao extends BaseDao<NoticeUserEO> {

    int deleteByPrimaryKey(String id);

    /**
     * @Description 根据公告id批量删除相关用户信息
     * @param noticeIds
     */
    void deleteInBatch(@Param("noticeIds") List<String> noticeIds);

    int insertSelective(NoticeUserEO noticeUser);

    int updateByPrimaryKeySelective(NoticeUserEO noticeUser);

    int insertList(@Param("list") List<NoticeUserEO> list);

    NoticeUserEO selectById(String id);

    Integer findPageWithCount(PageDTO pageDTO);


    List<NoticeUserEO> findNoticeUserEOByNoticeId(@Param("noticeIds") List<String> noticeIds);

    List<NoticeUserEO> findNoticeUserEOByNoticeIdAndUserId(
            @Param("noticeIds") List<String> noticeIds,
            @Param("receiveUserId") String receiveUserId);
}
