package com.adc.da.main.filter;

import com.adc.da.util.utils.RequestUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class MyCsrfFilter implements Filter {
    private static final Log logger = LogFactory.getLog(MyCsrfFilter.class);
    private static final List<String> whiteURIList = Arrays.asList("/swagger-ui.html","/verifyCode","/smallprogram","/smallProgram","sys/file","budget/project/getPadProjectContractAmount",
            "exchangeplan/exchangePlan/pad","customerresourcemanage/contacts","pad/padOperationManage","sys/sync","dataSync","user/supplierRegistry",
            "updatePassword","updateUserInfo","userMenu","getUserByRoleCode","/service","savefilectrl","Editor","/financialEditor","/model","/download","/export","Export","research/resProReport",
            "/sys/file/uploadMultipartFilesWithToken","/research/project/projectData/addOrUpdate","/research/project/approvalComment","/research/project/approvalComment/batchInsertApprovalCommentEO"

    );
    private static final CopyOnWriteArraySet<String> sycWhiteURISet = new CopyOnWriteArraySet(whiteURIList);
    private static final int sycWhiteURISetSize = sycWhiteURISet.size();

    private static final List<String> whiteReferList = Arrays.asList("123.127.164.20","192.168.7.123","localhost","catarc.info","192.168.144.216","192.168.5.244","192.168.10.78","127.0.0.1","localhost","10.0.3.46");
    private static final CopyOnWriteArraySet<String> sycWhiteReferSet = new CopyOnWriteArraySet(whiteReferList);
    private static final int sycWhiteReferSetSize = sycWhiteReferSet.size();

    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse res = (HttpServletResponse)response;
            String clientIp = RequestUtils.getClientIp(req);
            String url = req.getRequestURL().toString();
            String requestURI = req.getRequestURI();
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String referUrl = req.getHeader("Referer");
            String log;
            if (this.isWhiteReq(referUrl,requestURI)) {
                log = "正常请求---->>>clientIp= " + clientIp + "||date = " + date + "||referUrl=  " + referUrl + "||url= " + url;
                logger.info(log);
                chain.doFilter(request, response);
            } else {
                //req.getRequestDispatcher("/").forward(req, res);
                log = "跨站请求---->>>clientIp= " + clientIp + "||date= " + date + "||referUrl=  " + referUrl + "||url= " + url;
                logger.warn(log);
                res.sendError(401, "跨站请求，请联系管理员！|| referUrl=  "+ referUrl + "||url= " + url);
            }
        } catch (Exception var11) {
            logger.error("doFilter Exception:", var11);
        }

    }

    private boolean isWhiteReq(String referUrl,String requestURI) {
        if (sycWhiteURISetSize>0){
            for (String whiteUri : sycWhiteURISet){
                if (StringUtils.contains(requestURI.toLowerCase().trim(),whiteUri.toLowerCase().trim())) {
                    return true;
                }
            }
        }

        if (StringUtils.isEmpty(referUrl)){
            return false;
        }
        if (sycWhiteReferSetSize > 0) {
            String refHost =  referUrl.toLowerCase().trim();
            for (String whiteRefer : sycWhiteReferSet) {
                if (StringUtils.contains(refHost, whiteRefer.trim())) {
                    return true;
                }
            }
        }
            return false;
    }


    public void destroy() {

    }

    public static void main(String[] args) {
        for (String whiteRefer : whiteReferList) {
            System.out.println(StringUtils.contains("192.168.144.216", whiteRefer));
        }
    }
}
