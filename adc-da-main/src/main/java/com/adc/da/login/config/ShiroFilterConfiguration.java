//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adc.da.login.config;

import com.adc.da.login.security.AdcFormAuthenticationFilter;
import com.adc.da.util.constant.GlobalConfig;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroFilterConfiguration {
    private static final String LOGIN = "/login";
    private static final String AUTHC = "authc";
    private static final String ANON = "anon";
    private static final String LOGOUT = "logout";
    private static final String USER = "user";
    @Autowired
    private Environment env;



    public ShiroFilterConfiguration() {
    }

    @PostConstruct
    public void init() {
        GlobalConfig.setAdminPath(this.env.getProperty("adminPath"));
        GlobalConfig.setRestApiPath(this.env.getProperty("restPath"));
    }

    @Bean(
            name = {"shiroFilter"}
    )
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        String adminPath = GlobalConfig.getAdminPath();
        String restPath = GlobalConfig.getRestApiPath();
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(adminPath + LOGIN);
        shiroFilterFactoryBean.setSuccessUrl(adminPath);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
        filterChainDefinitionMap.put(restPath + LOGIN, ANON);
        filterChainDefinitionMap.put(restPath + "/logout", ANON);
        filterChainDefinitionMap.put(restPath + "/sys/file/*/download", ANON);
        filterChainDefinitionMap.put(restPath + "/smallProgram/**", ANON);
        filterChainDefinitionMap.put(restPath + "/smallprogram/**", ANON);
        filterChainDefinitionMap.put("/api/sys/user/getUserByRoleCode/*", ANON);

        filterChainDefinitionMap.put(restPath + "/judgeVerify", ANON);
        filterChainDefinitionMap.put(restPath + "/verifyCode", ANON);
        filterChainDefinitionMap.put(restPath + "/userInfo", ANON);
        filterChainDefinitionMap.put(restPath + "/onlineUser", ANON);
        filterChainDefinitionMap.put(restPath + "/userMenu", ANON);
        filterChainDefinitionMap.put(restPath + "/updateUserInfo", ANON);
        filterChainDefinitionMap.put(restPath + "/updatePassword", ANON);
        filterChainDefinitionMap.put(restPath + "/user/supplierRegistry/*", ANON);
        filterChainDefinitionMap.put(restPath + "/dataSync/**", ANON);
        filterChainDefinitionMap.put(restPath + "/sys/sync/**", ANON);
        filterChainDefinitionMap.put(restPath + "/statics/sStaticOperationAmount/**", ANON);
        filterChainDefinitionMap.put(restPath + "/pad/padOperationManage/**", ANON);
        filterChainDefinitionMap.put(restPath + "/research/resProReport/**", ANON);
        filterChainDefinitionMap.put(restPath + "/customerresourcemanage/contacts/**", ANON);

        filterChainDefinitionMap.put(restPath + "/exchangeplan/exchangePlan/pad", ANON);
        filterChainDefinitionMap.put(restPath + "/budget/project/getPadProjectContractAmount", ANON);
        filterChainDefinitionMap.put(restPath + "/budget/project/getPadProjectContractAmount", ANON);
        filterChainDefinitionMap.put(restPath + "/dashboard/**", ANON);
        filterChainDefinitionMap.put(restPath + "/industymeeting/meeting/page", ANON);
        filterChainDefinitionMap.put(restPath + "/exchangeplan/exchangePlan/page", ANON);
        filterChainDefinitionMap.put(restPath + "/customerresourcemanage/enterprise/page", ANON);
        filterChainDefinitionMap.put(restPath + "/industymeeting/communicationFrequency", ANON);

        filterChainDefinitionMap.put(restPath + "/industymeeting/receivableIncome/getPartBOperationStatistics", ANON);
        filterChainDefinitionMap.put(restPath + "/budget/project/getProjectContractAmount", ANON);
        filterChainDefinitionMap.put(restPath + "/carsales/carSales/carSalesRanking", ANON);

        filterChainDefinitionMap.put(restPath + "/industymeeting/receivableIncome/accountReceivableByDepart", ANON);
        filterChainDefinitionMap.put(restPath + "/industymeeting/receivableIncome/accountReceivableByEnterprise", ANON);


        filterChainDefinitionMap.put(restPath + "/industymeeting/meeting/**", ANON);
        filterChainDefinitionMap.put(restPath + "/customerresourcemanage/enterprise/**", ANON);
        filterChainDefinitionMap.put(restPath + "/research/researchProject/queryListByDangerLevel", ANON);
        // 科研管理相关接口
        filterChainDefinitionMap.put(restPath + "/sys/file/uploadMultipartFilesWithToken", ANON);
        filterChainDefinitionMap.put(restPath + "/research/project/projectData/addOrUpdate", ANON);
        filterChainDefinitionMap.put(restPath + "/research/project/approvalComment", ANON);
        filterChainDefinitionMap.put(restPath + "/research/project/approvalComment/batchInsertApprovalCommentEO", ANON);



        filterChainDefinitionMap.put("/api/**", AUTHC);
        filterChainDefinitionMap.put("/static/**", ANON);
        filterChainDefinitionMap.put("/userfiles/**", ANON);
        filterChainDefinitionMap.put(adminPath + LOGIN, AUTHC);
        filterChainDefinitionMap.put(adminPath + "/logout", LOGOUT);
        filterChainDefinitionMap.put(adminPath + "/**", USER);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new AdcFormAuthenticationFilter());//----> 这是正确的写法

        return shiroFilterFactoryBean;
    }
}
