package com.adc.da.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.dto.NoticeUserVO;
import com.adc.da.entity.NoticeEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>Notice NoticeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-23 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 */
public interface NoticeEODao extends BaseDao<NoticeEO> {

    int insertSelective(NoticeEO noticeEO);

    int updateByPrimaryKeySelective(NoticeEO noticeEO);

    int delete(String id);

    int deleteLogicInBatch(@Param("ids") List<String> ids);

    NoticeEO selectById(String id);

    List<NoticeEO> findNotIgnoreNoticeListByUserId(@Param("receiveUserId") String receiveUserId);


    List<NoticeEO> findIsIgnoreNoticeListByUserId(
            @Param("userId") String userId,
            @Param("operationStatus") String operationStatus);

    List<NoticeEO> findListFilterByIds(@Param("ids") List<String> ids);

    List<NoticeEO> findListByIds(@Param("ids") List<String> ids);

    List<NoticeUserVO> findAll( @Param("startIndex") Integer startIndex,@Param("endIndex") Integer endIndex,
                                @Param("userId") String userId);

    List<NoticeEO> findAllNoticeByCreateUserIdInPage( @Param("startIndex") Integer startIndex,@Param("endIndex") Integer endIndex,
                                @Param("createUserId") String createUserId);

    List<NoticeEO>  findAllNoticeByReceiveUserIdInPage (
            @Param("startIndex") Integer startIndex,@Param("endIndex") Integer endIndex,
            @Param("receiveUserId") String receiveUserId);

    Integer findAllConut(@Param("receiveUserId") String receiveUserId);

    List<NoticeEO>  findAllNoticeByReceiveUserIdInList(@Param("receiveUserId") String receiveUserId);

    List<NoticeEO> findAllNoticeByReceiveUserIdAndNameEqualInList(@Param("receiveUserId") String receiveUserId, @Param("name") String name);

    void deleteNoticeByReceiveUserIdEqualAndNoticeTypeEqual(@Param("receiveUserId") String receiveUserId, @Param("noticeType") String noticeType);
    Integer  findAllCountNoticeByReceiveUserIdInPage(@Param("receiveUserId") String receiveUserId);


    List<NoticeEO> findAllInPage(@Param("startIndex") Integer startIndex,@Param("endIndex") Integer endIndex);

    Integer findAllCountInPage();

    Integer insertList(@Param("list") List<NoticeEO> list);

}
