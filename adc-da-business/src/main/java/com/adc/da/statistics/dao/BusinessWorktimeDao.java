package com.adc.da.statistics.dao;

import com.adc.da.business.entity.BusinessWorktimeEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BusinessWorktimeDao {

    List<BusinessWorktimeEO> queryByYearAndMonth( @Param("year") Integer year,
                                                        @Param("startMonth") Integer startMonth,
                                                        @Param("finishMonth") Integer finishMonth
                                                        );


    Double queryDeptWorkTimeByYearAndMonth(
            @Param("year") Integer year,
            @Param("startMonth") Integer startMonth,
                                    @Param("finishMonth") Integer finishMonth,
                                    @Param("deptId") String deptId
                                    );


    Double queryBusinessWorkTimeByYearAndMonth(@Param("year") Integer year,
                                                @Param("startMonth") Integer startMonth,
                                                @Param("finishMonth") Integer finishMonth,
                                                @Param("businessId") String businessId
                                        );
}
