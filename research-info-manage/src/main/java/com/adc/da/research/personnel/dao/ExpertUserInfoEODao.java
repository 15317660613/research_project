package com.adc.da.research.personnel.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.personnel.entity.ExpertUserInfoEO;
import com.adc.da.research.personnel.page.ExpertUserInfoEOPage;
import com.adc.da.research.personnel.vo.ExpertUserInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_EXPERT_USER_INFO ExpertUserInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ExpertUserInfoEODao extends BaseDao<ExpertUserInfoEO> {

    /**
     * 批量删除研究人员信息
     * @param ids
     */
    void batchDelete(@Param("ids") List<String> ids);

    /**
     * 批量删除专家组与人员关系表(TR_GROUP_USER)信息
     * @param ids
     */
    void batchDeleteGroupUser(@Param("ids") List<String> ids);

    //查询绑定的信息
    List<ExpertUserInfoEO> getBindUserInfoList(@Param("expertGroupId")String expertGroupId);
    //查询未绑定的信息
    List<ExpertUserInfoEO> getUnBindUserInfoList(@Param("expertGroupId")String expertGroupId);
    //删除原绑定信息
    void deleteBindUser(@Param("expertGroupId")String expertGroupId);
    //保存现绑定信息
    void saveBindUser(@Param("id")String id,@Param("expertGroupId")String expertGroupId,
                      @Param("expertUserId")String expertUserId);

    ExpertUserInfoVO selectInfoById(String id);

    /**
     * 添加专家组id和专家人员id到关联表
     * @param id
     * @param expertGroupId
     * @param userId
     */
    void insertInfo(@Param("id") String id,
                    @Param("expertGroupId") String expertGroupId,
                    @Param("userId") String userId);

    /**
     * 查询带专家组的专家人员
     * @param page
     * @return
     */
    List<ExpertUserInfoEO> queryByExpertGroupPage(ExpertUserInfoEOPage page);


    int queryByExpertGroupCount(ExpertUserInfoEOPage page);

}
