package com.adc.da.smallprogram.schedule;

import com.adc.da.base.page.BasePage;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.smallprogram.dao.ScheduleConfigEODao;
import com.adc.da.smallprogram.entity.ScheduleConfigEO;
import com.adc.da.smallprogram.entity.ScheduleResearchEO;
import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;
import com.adc.da.smallprogram.page.MyScheduleResearchEOPage;
import com.adc.da.smallprogram.page.ScheduleResearchEOPage;
import com.adc.da.smallprogram.page.ScheduleResearchUserEOPage;
import com.adc.da.smallprogram.service.ScheduleResearchEOService;
import com.adc.da.smallprogram.service.ScheduleResearchUserEOService;
import com.adc.da.smallprogram.vo.ResearchVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ScheduledService
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class ResearchScheduleService {
    @Autowired
    private ScheduleResearchEOService scheduleResearchEOService ;
//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void doTask() throws Exception {
        scheduleResearchEOService.doTask();
    }
}
