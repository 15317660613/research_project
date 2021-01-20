package com.adc.da.customerresourcemanage.schedule;

import com.adc.da.budget.dao.ContractPartnerEODao;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.DictionaryEO;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectOwnerSchedule {
    //ALL_PROJECT_OWNER
    @Autowired
    private DicTypeEODao dicTypeEODao;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ContractPartnerEODao contractPartnerEODao;

//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void doTask() throws Exception{
        //约定 字典的extinfo 是总企业数  extinfo2 是当年新增合作企业数
        DicTypeEO cache = dicTypeEODao.selectByPrimaryKey("DASHBOARD_PROJECT_OWNER");

        Integer currentYear = new DateTime().getYear();
        List<Project> projectList = projectRepository.findByProjectType(0);
        Set<String> formerYearEnterpriseNameSet = new HashSet<>(); // all
        Set<String> thisYearEnterpriseNameSet = new HashSet<>();
        List<String> skipList = Arrays.asList("1111111111","20190304","20181105","20160504",
                "20160809","20180305","20180801","20181105","20190203","20190304",
                "201800195","220200001","320200001","320200002","320200003","320200005","1111212",
                "20160504","9999999","180887","20160809","20160703","180663","1111212",
                "220200001","AS20200003","AS20200001","asd22312","6106NF0043N19","RC20160213",
                "RC20160411","CGHT201908004","CGHT201908005","2019000000000000","TJZCHT20190169",
                "TJHT20190828","TJKDK20190925004","CS20200165");
        List<String> partnerNameList = contractPartnerEODao.queryAllPartnerName();
        Set<String> allSet = new HashSet<>();
        for (String partner : partnerNameList) {
            String tmpPartnerName = partner.trim();
            allSet.add(tmpPartnerName);
            formerYearEnterpriseNameSet.add(tmpPartnerName);
        }
        for (Project project : projectList){
            if (StringUtils.length(project.getContractNo()) <= 7){
                continue;
            }
            String contractNO = project.getContractNo().trim();
            if ( StringUtils.isEmpty(project.getProjectOwner())
                    || StringUtils.isEmpty(project.getBudgetDomainId())
                    ||skipList.contains(contractNO)
                    ||(project.getDelFlag()!=null && project.getDelFlag())
            ) {
                continue;
            }
            String owner = project.getProjectOwner().trim();
            if ( null!= currentYear && project.getProjectYear() == currentYear.intValue()
                    &&!thisYearEnterpriseNameSet.contains(owner)&&!StringUtils.equals("南方区",owner)){
                thisYearEnterpriseNameSet.add(owner);
            }
            if (  project.getProjectYear() < currentYear.intValue()
                    &&!formerYearEnterpriseNameSet.contains(owner)&&!StringUtils.equals("南方区",owner)){
                formerYearEnterpriseNameSet.add(owner);
            }
            if (!allSet.contains(owner)){
                allSet.add(owner);
            }
        }
        int thisYearEnterPrise = 0 ;
        for (String enterprise : thisYearEnterpriseNameSet){
            if (!formerYearEnterpriseNameSet.contains(enterprise)){
                thisYearEnterPrise ++;
            }
        }
        if(null == cache){
            cache = new DicTypeEO();
            cache.setId("DASHBOARD_PROJECT_OWNER");
            cache.setDicTypeCode("PROJECT_OWNER");
            cache.setCreateTime(new Date());
            cache.setExtInfo(String.valueOf(allSet.size()));
            cache.setExtInfo2(String.valueOf(thisYearEnterPrise));
            dicTypeEODao.insertSelective(cache);
        }else {
            cache.setExtInfo(String.valueOf(allSet.size()));
            cache.setExtInfo2(String.valueOf(thisYearEnterPrise));
            dicTypeEODao.updateByPrimaryKeySelective(cache);
        }
    }

}
