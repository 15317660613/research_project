package com.adc.da.onlyoffice.controller;

import com.adc.da.event.entity.EventEO;
import com.adc.da.event.entity.EventReceiveEO;
import com.adc.da.event.service.EventEOService;
import com.adc.da.event.service.EventReceiveEOService;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.finicialProcess.entity.FinancialProcessTableEO;
import com.adc.da.finicialProcess.service.FinancialProcessTableEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.onlyoffice.entity.FileModel;
import com.adc.da.onlyoffice.helpers.ConfigManager;
import com.adc.da.onlyoffice.helpers.DocumentManager;
import com.adc.da.onlyoffice.helpers.FileUtility;
import com.adc.da.onlyoffice.helpers.ServiceConverter;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
public class EditorController {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    @Autowired
    private FileEOService fileEOService ;
    @Autowired
    private FinancialProcessTableEOService financialProcessTableEOService;
    @Autowired
    private EventEOService eventEOService ;
    @Autowired
    private EventReceiveEOService eventReceiveEOService ;
    @Value("${serverHost}")
    private String serverHost;

    @Value("${serverHostSSLFalse}")
    private String serverHostSSLFalse;


    @Value("${officialHeader}")
    private String officialHeader;

    private static final String FILE_NAME = "fileName";    //文件名
    private static final String MODE = "mode";             //平台类型
    private static final String FILE_TYPE = "fileType";    //文件类型
    private static final String DOCUMENT_TYPE = "documentType";    //文档类型
    private static final String FILE_URI = "fileUri";      //文件的Uri
    private static final String FILE_KEY = "fileKey";      //文件的Key
    private static final String CALLBACK_URL = "callbackUrl";  //回滚地址
    private static final String SERVER_URL = "serverUrl";      //服务器地址
    private static final String EDITOR_MODE = "editorMode";    //编辑模式
    private static final String EDITOR_USER_ID = "editorUserId";   //编辑者的ID
    private static final String TYPE = "type";
    private static final String DOC_SERVICE_API_URL = "docserviceApiUrl";  //文档服务的API地址
    private static final String DOC_SERVICE_URL_PRELOADER = "docServiceUrlPreloader";
    private static final String CURRENT_YEAR = "currentYear";   //年份
    private static final String CONVERT_EXTS = "convertExts";
    private static final String EDITED_EXTS = "editedExts";
    private static final String DOCUMENT_CREATED = "documentCreated"; //文档创建时间
    private static final String PERMISSIONS_EDIT = "permissionsEdit";  //是否可以编辑,onlyOffice的配置
    private static final String EDITOR = "editor";
    private static final String CHANGE_STATE_URL = "changeStateUrl";

    @RequestMapping("/Editor")
    public ModelAndView editor(HttpServletRequest request, HttpServletResponse response, Model model,String fileId ,String eventReceiveId) throws Exception {

        String localFileId = fileId;
        UserEO userEO = UserUtils.getUser();
        if (null==userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (request.getParameterMap().containsKey("fileId")) {
            localFileId = request.getParameter("fileId");
        }
        EventReceiveEO eventReceiveEO = eventReceiveEOService.selectByPrimaryKey(eventReceiveId);
        String envetId =eventReceiveEO.getEventId();
        FileEO fileEO = fileEOService.selectByPrimaryKey(localFileId);

        if (null == fileEO){
            throw  new AdcDaBaseException("文件不存在");
        }
        String fileName = fileEO.getFileName() ;

        String mode = "";
        if (request.getParameterMap().containsKey(MODE))
        {
            mode = request.getParameter(MODE);
        }
        Boolean desktopMode = !"embedded".equals(mode);
        FileModel file = new FileModel(fileEO.getSavePath());
        file.initDesktop(mode);
        DocumentManager.init(request, response);
        //要编辑的文件名
        model.addAttribute(FILE_NAME, fileName) ;
        //要编辑的文件类型
        model.addAttribute(FILE_TYPE, FileUtility.getFileExtension(fileEO.getSavePath()).replace(".", "")) ;
        //要编辑的文档类型
        model.addAttribute(DOCUMENT_TYPE,FileUtility.getFileType(fileEO.getSavePath()).toString().toLowerCase()) ;
        //要编辑的文档访问url
        model.addAttribute(FILE_URI,this.serverHost+"api/sys/file/"+localFileId+"/download") ;
        //ADC_info头
        model.addAttribute("officialHeader",officialHeader);


        EventEO eventEO = eventEOService.selectByPrimaryKey(envetId);
        String boolEdit = getReportBoolEdit(eventReceiveEO,eventEO) ;
        int eventReceiveEOState = eventReceiveEO.getState();
        /*if(
                eventReceiveEOState==0||eventReceiveEOState==1 ||eventReceiveEOState==2||(eventReceiveEOState==3&& StringUtils.equals(eventEO.getCreateUserId(),eventReceiveEO.getReceiveUserId()))
        ){
            boolEdit = "true" ;
        }*/

        if (eventReceiveEOState == 2) {
        } else if (eventReceiveEOState != 3 && eventReceiveEOState != 4 && !StringUtils.equals(eventEO.getCreateUserId(),eventReceiveEO.getReceiveUserId())) {
            eventReceiveEO.setState(1);
            eventReceiveEOService.updateByPrimaryKeySelective(eventReceiveEO);
//            List<EventReceiveEO> eventReceiveEOList = eventReceiveEOService.selectByEventId(eventReceiveEO.getEventId());
//            boolean flag = true;
//
//            Iterator eventReceiveEOIterator = eventReceiveEOList.iterator();
//            while (eventReceiveEOIterator.hasNext()) {
//                EventReceiveEO eo = (EventReceiveEO) eventReceiveEOIterator.next();
//                if (eo.getState() != 1) {
//                    flag = false;
//                    break;
//                }
//            }
//
//            if (flag) {
//                eventEO =  eventEOService.selectByPrimaryKey(eventEO.getId());
//                String createUserId = eventEO.getCreateUserId();
//                eventReceiveEOService.updateByReceiveUserId(2, eventEO.getId(), createUserId);
//            }
        }

        if (StringUtils.isEmpty(eventEO.getExtInfo6())){
            String key =  ServiceConverter.generateRevisionId(envetId);
            eventEO.setExtInfo6(key);
            eventEOService.updateByPrimaryKeySelective(eventEO);
        }

        model.addAttribute(FILE_KEY, eventEO.getExtInfo6()+"1214") ;
        //model.addAttribute(CALLBACK_URL, DocumentManager.GetCallback(fileName))
        model.addAttribute(CALLBACK_URL, this.serverHost +"savefilectrl/mySaveEditedFile?eventReceiveId="+eventReceiveId) ;
        model.addAttribute(SERVER_URL, serverHost) ;
        model.addAttribute(EDITOR_MODE, DocumentManager.getEditedExts().contains(FileUtility.getFileExtension(fileEO.getSavePath())) && !"view".equals(request.getAttribute(MODE)) ? "edit" : "view") ;
        model.addAttribute(EDITOR_USER_ID,userEO.getUsname()) ;
        model.addAttribute(TYPE, desktopMode ? "desktop" : "embedded");
        model.addAttribute(DOC_SERVICE_API_URL, this.serverHost + ConfigManager.getProperty("files.docservice.url.api"));
        model.addAttribute(DOC_SERVICE_URL_PRELOADER, this.serverHost + ConfigManager.getProperty("files.docservice.url.preloader")) ;
        model.addAttribute(CURRENT_YEAR, "2019") ;
        model.addAttribute(CONVERT_EXTS, String.join(",", DocumentManager.getConvertExts())) ;
        model.addAttribute(EDITED_EXTS, String.join(",", DocumentManager.getEditedExts())) ;
        model.addAttribute(DOCUMENT_CREATED, new SimpleDateFormat("MM/dd/yyyy").format(new Date())) ;
        model.addAttribute(PERMISSIONS_EDIT, boolEdit) ;
        model.addAttribute(CHANGE_STATE_URL,  this.serverHostSSLFalse+"savefilectrl/myReportChangeState?eventReceiveId="+eventReceiveId);


        return new ModelAndView(EDITOR) ;
    }

    private String getReportBoolEdit(EventReceiveEO eventReceiveEO,EventEO eventEO){
        String boolEdit = "" ;
        int  eventReceiveEOState = eventReceiveEO.getState();
        if(eventReceiveEOState==0||eventReceiveEOState==1||eventReceiveEOState==2||
             (eventReceiveEOState==3&&StringUtils.equals(eventEO.getCreateUserId(),eventReceiveEO.getReceiveUserId()))){
            boolEdit = "true" ;
        }
        return boolEdit;
    }

    @RequestMapping("/financialEditor")
    public ModelAndView financialEditor(HttpServletRequest request, HttpServletResponse response, Model model,String eventReceiveId) throws Exception {


        UserEO userEO = UserUtils.getUser();
        if (null==userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        FinancialProcessTableEO financialProcessTableEO = financialProcessTableEOService.selectByPrimaryKey(eventReceiveId);

        FileEO fileEO = fileEOService.selectByPrimaryKey(financialProcessTableEO.getFileId());

        if (null == fileEO){
            throw  new AdcDaBaseException("文件不存在");
        }
        String fileName = fileEO.getFileName() ;

        String mode = "";
        if (request.getParameterMap().containsKey(MODE))
        {
            mode = request.getParameter(MODE);
        }
        Boolean desktopMode = !"embedded".equals(mode);
        FileModel file = new FileModel(fileEO.getSavePath());
        file.initDesktop(mode);
        DocumentManager.init(request, response);
        //要编辑的文件名
        model.addAttribute(FILE_NAME, fileName) ;
        //要编辑的文件类型
        model.addAttribute(FILE_TYPE, FileUtility.getFileExtension(fileEO.getSavePath()).replace(".", "")) ;
        //要编辑的文档类型
        model.addAttribute(DOCUMENT_TYPE,FileUtility.getFileType(fileEO.getSavePath()).toString().toLowerCase()) ;
        //要编辑的文档访问url
        model.addAttribute(FILE_URI,this.serverHost+"api/sys/file/"+financialProcessTableEO.getFileId()+"/download") ;
        //判断是否有编辑权限
        String boolEdit = getFinancialBoolEdit(financialProcessTableEO) ;

        model.addAttribute(FILE_KEY, financialProcessTableEO.getExtInfo6()+"1214") ;
        //model.addAttribute(CALLBACK_URL, DocumentManager.GetCallback(fileName))
        model.addAttribute(CALLBACK_URL, this.serverHost +"savefilectrl/myFinancialSaveFile?eventReceiveId="+eventReceiveId) ;
        model.addAttribute(SERVER_URL, serverHost) ;
        model.addAttribute(EDITOR_MODE, DocumentManager.getEditedExts().contains(FileUtility.getFileExtension(fileEO.getSavePath())) && !"view".equals(request.getAttribute(MODE)) ? "edit" : "view") ;
        model.addAttribute(EDITOR_USER_ID,userEO.getUsname()) ;
        model.addAttribute(TYPE, desktopMode ? "desktop" : "embedded");
        model.addAttribute(DOC_SERVICE_API_URL, this.serverHost + ConfigManager.getProperty("files.docservice.url.api"));
        model.addAttribute(DOC_SERVICE_URL_PRELOADER, this.serverHost + ConfigManager.getProperty("files.docservice.url.preloader")) ;
        model.addAttribute(CURRENT_YEAR, "2019") ;
        model.addAttribute(CONVERT_EXTS, String.join(",", DocumentManager.getConvertExts())) ;
        model.addAttribute(EDITED_EXTS, String.join(",", DocumentManager.getEditedExts())) ;
        model.addAttribute(DOCUMENT_CREATED, new SimpleDateFormat("MM/dd/yyyy").format(new Date())) ;
        model.addAttribute(PERMISSIONS_EDIT, boolEdit) ;

        model.addAttribute(CHANGE_STATE_URL,  this.serverHostSSLFalse+"savefilectrl/myFinancialChangeState?eventReceiveId="+eventReceiveId);
        //ADC_info头
        model.addAttribute("officialHeader",officialHeader);
        return new ModelAndView(EDITOR) ;
    }

    private String getFinancialBoolEdit(FinancialProcessTableEO financialProcessTableEO){
        if (financialProcessTableEO.getState() == 4){
            return "";
        }else {
            return "true";
        }
    }

    @ApiOperation(value = "|standEditor|文件在线预览-传文件id即可")
    @GetMapping("/standEditor")
    public ModelAndView standBookEditor(HttpServletRequest request, HttpServletResponse response,
                                        Model model,@RequestParam("fileId") String fileId) throws Exception {

        UserEO userEO = UserUtils.getUser();
        if (null==userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        FileEO fileEO = fileEOService.selectByPrimaryKey(fileId);

        if (null == fileEO){
            throw  new AdcDaBaseException("模板文件未找到!");
        }
        String fileName = fileEO.getFileName() ;

        String mode = "";
        if (request.getParameterMap().containsKey(MODE))
        {
            mode = request.getParameter(MODE);
        }
        Boolean desktopMode = !"embedded".equals(mode);
        FileModel file = new FileModel(fileEO.getSavePath());
        file.initDesktop(mode);
        DocumentManager.init(request, response);
        //要编辑的文件名
        model.addAttribute(FILE_NAME, fileName) ;
        //要编辑的文件类型
        model.addAttribute(FILE_TYPE, FileUtility.getFileExtension(fileEO.getSavePath()).replace(".", "")) ;
        //要编辑的文档类型
        model.addAttribute(DOCUMENT_TYPE,FileUtility.getFileType(fileEO.getSavePath()).toString().toLowerCase()) ;
        //要编辑的文档访问url
        model.addAttribute(FILE_URI,this.serverHostSSLFalse+"api/sys/file/"+fileEO.getFileId()+"/download") ;
        //ADC_info头
        model.addAttribute("officialHeader",officialHeader);



//        if (StringUtils.isEmpty(contractTemplateEO.getExt5())) {
//            model.addAttribute(FILE_KEY, contractTemplateEO.getId() + "1214");
//        }else {
        model.addAttribute(FILE_KEY, fileId);
//        }
        //model.addAttribute(CALLBACK_URL, DocumentManager.GetCallback(fileName))
        model.addAttribute(CALLBACK_URL, this.serverHostSSLFalse +"savefilectrl/recallOK?fileId="+fileId) ;
        model.addAttribute(SERVER_URL, serverHost) ;
        model.addAttribute(EDITOR_MODE, DocumentManager.getEditedExts().contains(FileUtility.getFileExtension(fileEO.getSavePath())) && !"view".equals(request.getAttribute(MODE)) ? "edit" : "view") ;
        model.addAttribute(EDITOR_USER_ID,userEO.getUsname()) ;
        model.addAttribute(TYPE, desktopMode ? "desktop" : "embedded");
        model.addAttribute(DOC_SERVICE_API_URL, this.serverHost + ConfigManager.getProperty("files.docservice.url.api"));
        model.addAttribute(DOC_SERVICE_URL_PRELOADER, this.serverHost + ConfigManager.getProperty("files.docservice.url.preloader")) ;
        model.addAttribute(CURRENT_YEAR, "2019") ;
        model.addAttribute(CONVERT_EXTS, String.join(",", DocumentManager.getConvertExts())) ;
        model.addAttribute(EDITED_EXTS, String.join(",", DocumentManager.getEditedExts())) ;
        model.addAttribute(DOCUMENT_CREATED, new SimpleDateFormat("MM/dd/yyyy").format(new Date())) ;
        model.addAttribute(PERMISSIONS_EDIT, "") ;

        model.addAttribute(CHANGE_STATE_URL,  this.serverHostSSLFalse+"savefilectrl/recallOK?fileId="+fileId);

        return new ModelAndView(EDITOR) ;
    }




}
