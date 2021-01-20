package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.constant.Role;
import com.adc.da.login.util.UserUtils;
import com.adc.da.project.poi.XWPFTemplate;
import com.adc.da.research.dao.DeclarationEODao;
import com.adc.da.research.entity.DeclarationEO;
import com.adc.da.research.page.DeclarationEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import static com.adc.da.research.utils.RSPendingStatus.RS_DECLARATION_COMMIT;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_DECLARATION DeclarationEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("declarationEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class DeclarationEOService extends BaseService<DeclarationEO, String> {

    @Autowired
    private DeclarationEODao dao;

    public DeclarationEODao getDao() {
        return dao;
    }

    /**
     * 获取当前登录用户id
     */
    private String getUserId() {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return userId;
    }



    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<DeclarationEO> queryByPage(DeclarationEOPage page) throws Exception {
        String userId = getUserId();
        Subject subject = SecurityUtils.getSubject();

        /* 管理员查询所有，科委会只查询已提交的 */
        if (!subject.hasRole(Role.ADMIN) && !subject.hasRole(Role.SUPER_ADMIN)) {
            if (
                subject.hasRole(Role.COMMITTEE_MEMBER)) {
                page.setStatus(RS_DECLARATION_COMMIT);
                page.setCommitteeUserId(userId);
            } else {
                page.setExtInfo6(userId);
            }
        }

        int rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByPage(page);
    }

    @Override
    public int insertSelective(DeclarationEO t) {
        t.setId(UUID.randomUUID10());
        t.setExtInfo6(getUserId());
        t.setCreateTime(new Date());
        return this.getDao().insertSelective(t);
    }

    @Override
    public int updateByPrimaryKeySelective(DeclarationEO t) {
        return this.getDao().updateByPrimaryKeySelective(t);
    }

    /**
     * @param id
     * @return
     */
    public void deleteById(String id) {
        DeclarationEO t = new DeclarationEO();
        t.setId(id);
        t.setDelFlag(1);
        this.getDao().updateByPrimaryKeySelective(t);
    }

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * @param id
     * @param response
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-11-27
     **/
    public ResponseMessage downloadDoc(String id, HttpServletResponse response) {
        DeclarationEO eo = dao.selectByPrimaryKey(id);
        if (eo == null) {
            throw new AdcDaBaseException("id错误");
        }
        /*
         *  将四个空格替换成两个空格
         */
        eo.setSummaryDoc01(replaceSpace(eo.getSummaryDoc01()));
        eo.setSummaryDoc02(replaceSpace(eo.getSummaryDoc02()));
        eo.setSummaryDoc03(replaceSpace(eo.getSummaryDoc03()));
        eo.setSummaryDoc04(replaceSpace(eo.getSummaryDoc04()));
        eo.setSummaryDoc05(replaceSpace(eo.getSummaryDoc05()));

        Resource resource = resourceLoader.getResource("classpath:docxTemplate\\Declaration_form_template.docx");
        //打开doc
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=" + Encodes.urlEncode(eo.getProjectName() + ".docx"));
        response.setContentType("application/force-download");
        try (OutputStream os = response.getOutputStream()) {
            XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream())
                                                .render(eo);
            template.write(os);
            os.flush();
            template.close();
            return Result.success();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.error("下载失败，请联系管理员");
        }

    }

    /**
     * 替换7个半角空格为2个全角空格
     *
     * @param source
     * @return
     */
    private String replaceSpace(String source) {
        if (StringUtils.isNotEmpty(source)) {
            return source.replaceAll(" {7}", "\u3000\u3000");
        } else {
            return "";
        }

    }
}
