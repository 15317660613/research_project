package com.adc.da.onlyoffice.controller;

import com.adc.da.event.entity.EventEO;
import com.adc.da.event.entity.EventReceiveEO;
import com.adc.da.event.entity.HistoryFileEO;
import com.adc.da.event.page.HistoryFileEOPage;
import com.adc.da.event.service.EventEOService;
import com.adc.da.event.service.EventFileEOService;
import com.adc.da.event.service.EventReceiveEOService;
import com.adc.da.event.service.HistoryFileEOService;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.finicialProcess.entity.FinancialProcessTableEO;
import com.adc.da.finicialProcess.page.FinancialProcessTableEOPage;
import com.adc.da.finicialProcess.service.FinancialProcessTableEOService;
import com.adc.da.onlyoffice.utils.Stream2FileUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
 * 用于保存修改后的文件
 * */
@RestController
@RequestMapping("/savefilectrl")
public class SaveFileController {

    private static final Logger logger = LoggerFactory.getLogger(SaveFileController.class);

    @Value("${file.path}")
    private String filepath;

    @Value("${historyFile.maxNum:10}")
    private int maxNum;

    @Value("${historyFile.delNum:5}")
    private int delNum;


    @Autowired
    private EventReceiveEOService eventReceiveEOService;

    @Autowired
    private FinancialProcessTableEOService financialProcessTableEOService;

    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private EventEOService eventEOService;

    @Autowired
    private EventFileEOService eventFileEOService;

    @Autowired
    private HistoryFileEOService historyFileEOService;


    /**
     * 文档编辑服务使用JavaScript API通知callbackUrl，向文档存储服务通知文档编辑的状态。文档编辑服务使用具有正文中的信息的POST请求。
     * https://api.onlyoffice.com/editors/callback
     * 参数示例：
     * {
     * "actions": [{"type": 0, "userid": "78e1e841"}],
     * "changesurl": "https://documentserver/url-to-changes.zip",
     * "history": {
     * "changes": changes,
     * "serverVersion": serverVersion
     * },
     * "key": "Khirz6zTPdfd7",
     * "status": 2,
     * "url": "https://documentserver/url-to-edited-document.docx",
     * "users": ["6d5a81d0"]
     * }
     *
     * @throws ParseException
     */
    @RequestMapping("/mySaveEditedFile")
    public void mySaveEditedFile(HttpServletRequest request, HttpServletResponse response, String eventReceiveId) {
        PrintWriter writer = null;
        if (StringUtils.isEmpty(eventReceiveId)) {
            eventReceiveId = request.getParameter("eventReceiveId");
        }

        try {
            writer = response.getWriter();
            String body = null;
            Scanner tmp = null;
            try (Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A")) {
                body = scanner.hasNext() ? scanner.next() : "";
                tmp = scanner;
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (null != tmp) {
                    tmp.close();
                }
            }
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(body);

            /*
                0 - no document with the key identifier could be found,
                1 - document is being edited,
                2 - document is ready for saving,
                3 - document saving error has occurred,
                4 - document is closed with no changes,
                6 - document is being edited, but the current document state is saved,
                7 - error has occurred while force saving the document.
             **/
            if ((long) jsonObj.get("status") == 2 || (long) jsonObj.get("status") == 3) {
                /*
                    当我们关闭编辑窗口后，十秒钟左右onlyoffice会将它存储的我们的编辑后的文件，，此时status = 2，通过request发给我们，我们需要做的就是接收到文件然后回写该文件。
                    定义要与文档存储服务保存的编辑文档的链接。当状态值仅等于2或3时，存在链路。
                 * */
                String downloadUri = (String) jsonObj.get("url");
                //解析得出文件名
                String fileName = downloadUri.substring(downloadUri.lastIndexOf('/') + 1);

                URL url = new URL(downloadUri);
                logger.error("-----"+downloadUri+"--------");
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();

                String eventId = "";
                if (StringUtils.isNotEmpty(eventReceiveId)) {
                    EventReceiveEO eventReceiveEO = eventReceiveEOService.selectByPrimaryKey(eventReceiveId);
                    EventEO eventEO = eventEOService.selectByPrimaryKey(eventReceiveEO.getEventId());
                    eventId = eventEO.getId();
                    eventEO.setExtInfo6(UUID.randomUUID10());
                    eventEOService.updateByPrimaryKeySelective(eventEO);
                }
                String fileId = eventFileEOService.selectByEventId(eventId);
                FileEO fileEO = fileEOService.selectByPrimaryKey(fileId);

                String path = this.filepath + File.separator + fileEO.getSavePath();
                File savedFile = new File(path);

                //保存文件的历史版本信息
                //historyFileInsert(path, fileId, eventReceiveId);

                Stream2FileUtil.doSave(stream,savedFile);

                connection.disconnect();
            }
            /*
             * status = 1，我们给onlyoffice的服务返回{"error":"0"}的信息，这样onlyoffice会认为回调接口是没问题的，这样就可以在线编辑文档了，否则的话会弹出窗口说明
             * */
            if (writer != null) {
                writer.write("{\"error\":0}");
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    @RequestMapping("/myFinancialSaveFile")
    public void myFinancialSaveFile(HttpServletRequest request, HttpServletResponse response, String eventReceiveId) {
        PrintWriter writer = null;
        if (StringUtils.isEmpty(eventReceiveId)) {
            eventReceiveId = request.getParameter("eventReceiveId");
        }

        try {
            writer = response.getWriter();
            String body = null;
            Scanner tmp = null;
            try (Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A")) {
                body = scanner.hasNext() ? scanner.next() : "";
                tmp = scanner;
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (null != tmp) {
                    tmp.close();
                }
            }
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(body);

            /*
                0 - no document with the key identifier could be found,
                1 - document is being edited,
                2 - document is ready for saving,
                3 - document saving error has occurred,
                4 - document is closed with no changes,
                6 - document is being edited, but the current document state is saved,
                7 - error has occurred while force saving the document.
             **/
            if ((long) jsonObj.get("status") == 2 || (long) jsonObj.get("status") == 3) {
                /*
                    当我们关闭编辑窗口后，十秒钟左右onlyoffice会将它存储的我们的编辑后的文件，，此时status = 2，通过request发给我们，我们需要做的就是接收到文件然后回写该文件。
                    定义要与文档存储服务保存的编辑文档的链接。当状态值仅等于2或3时，存在链路。
                 * */
                String downloadUri = (String) jsonObj.get("url");
                //解析得出文件名
                String fileName = downloadUri.substring(downloadUri.lastIndexOf('/') + 1);

                URL url = new URL(downloadUri);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();


                FinancialProcessTableEO financialProcessTableEO = financialProcessTableEOService.selectByPrimaryKey(eventReceiveId);


                financialProcessTableEO.setExtInfo6(UUID.randomUUID10());

                financialProcessTableEOService.updateByPrimaryKeySelective(financialProcessTableEO);

                FileEO fileEO = fileEOService.selectByPrimaryKey(financialProcessTableEO.getFileId());

                String fullPath = fileEO.getSavePath();
                if (!fullPath.contains(this.filepath)) {
                    fullPath =  this.filepath + File.separator + fileEO.getSavePath();
                }

                File savedFile = new File(fullPath);

                //保存文件的历史版本信息
                //historyFileInsert(path, fileId, eventReceiveId);
                Stream2FileUtil.doSave(stream,savedFile);

                connection.disconnect();

                //判断是否是支出合同明细表，如果是该张表，则将状态位设置为4
                if (financialProcessTableEO.getFinancialTableType().equals("5") && financialProcessTableEO.getState() == 4){
                    boolean flag = true;
                    //从父表中查看子表的状态为是否全部为4，如果是，就将子表自动合并到父表中;
                    FinancialProcessTableEOPage financialProcessTableEOPage = new FinancialProcessTableEOPage();
                    financialProcessTableEOPage.setParentId(financialProcessTableEO.getParentId());
                    List<FinancialProcessTableEO> financialProcessTableEOList = financialProcessTableEOService.queryByList(financialProcessTableEOPage);
                    for (FinancialProcessTableEO financialProcessTableEO1 : financialProcessTableEOList){
                        if(financialProcessTableEO1.getState() != 4){
                            flag = false;
                            break;
                        }
                    }

                    if (flag){
                        //将父表下的子文件进行合并
                        FinancialProcessTableEO financialProcessTableEOParent = financialProcessTableEOService.selectByPrimaryKey(financialProcessTableEO.getParentId());
                        if (financialProcessTableEOParent.getState() != 4){
                            financialProcessTableEOService.combineExcel(financialProcessTableEOParent.getId());
                        }
                    }
                }
            }


            /*
             * status = 1，我们给onlyoffice的服务返回{"error":"0"}的信息，这样onlyoffice会认为回调接口是没问题的，这样就可以在线编辑文档了，否则的话会弹出窗口说明
             * */
            if (writer != null) {
                writer.write("{\"error\":0}");
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("/myFinancialChangeState")
    public void myFinancialFileStatus(String eventReceiveId) {

        try {
            FinancialProcessTableEO financialProcessTableEO = financialProcessTableEOService.selectByPrimaryKey(eventReceiveId);
            financialProcessTableEO.setState(4);
            financialProcessTableEOService.updateByPrimaryKeySelective(financialProcessTableEO);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }


    @RequestMapping("/myReportChangeState")
    public void myReportChangeState(String eventReceiveId) {

        try {
            EventReceiveEO eventReceiveEO  = eventReceiveEOService.selectByPrimaryKey(eventReceiveId);
            EventEO eventEO =  eventEOService.selectByPrimaryKey(eventReceiveEO.getEventId());
            if (StringUtils.equals(eventEO.getCreateUserId(),eventReceiveEO.getReceiveUserId()) ){
                return;
            }
            eventReceiveEO.setState(3);
            eventReceiveEOService.updateByPrimaryKeySelective(eventReceiveEO);


            List<EventReceiveEO> eventReceiveEOList = eventReceiveEOService.selectByEventId(eventReceiveEO.getEventId());
            boolean flag = true;

            Iterator eventReceiveEOIterator = eventReceiveEOList.iterator();
            while (eventReceiveEOIterator.hasNext()) {
                EventReceiveEO eo = (EventReceiveEO) eventReceiveEOIterator.next();
                if (eo.getState() != 3 && !StringUtils.equals(eventEO.getCreateUserId(),eo.getReceiveUserId()) ) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                String createUserId = eventEO.getCreateUserId();
                eventReceiveEOService.updateByReceiveUserId(2, eventEO.getId(), createUserId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }


    @RequestMapping("/recallOK")
    public void recallOK( HttpServletResponse response,String fileId) {
        logger.error("onlyoffice默认回调异常--- recallOK");
        try {
            response.getWriter().write("{\"error\":0}");
        } catch (IOException e) {
            logger.error("onlyoffice默认回调异常",e.getMessage());
        }
    }


    public void historyFileInsert(String path, String fileId, String eventReceiveId) throws Exception {

        //创建历史文件的前后缀
        String historyPath = path.substring(0, path.lastIndexOf('.'));
        String suffix = path.substring(path.lastIndexOf('.') + 1);
        //历史版本文件的路径
        historyPath = historyPath + "_" + System.currentTimeMillis() + "." + suffix;
        //将原文件写入到历史版本文件中
        byte[] temp = new byte[1024];
        int len;
        try (
                FileInputStream fis = new FileInputStream(new File(path));
                FileOutputStream fos = new FileOutputStream(historyPath)
        ) {
            while ((len = fis.read(temp)) != -1) {
                fos.write(temp, 0, len);
            }
            fos.flush();

        } catch (Exception e) {
            logger.error(e.getMessage());
//            e.printStackTrace();
        }
        HistoryFileEOPage page = new HistoryFileEOPage();
        page.setFileId(fileId);
        page.setOrderBy("edit_time_");
        List<HistoryFileEO> historyFileEOList = historyFileEOService.queryByList(page);
        if (maxNum < delNum) {
            throw new AdcDaBaseException("简报历史版本的保存上限数小于删除数");
        }
        if (historyFileEOList.size() >= maxNum) {
            File delFile = null;
            String id = null;
            for (int i = 0; i < delNum; i++) {
                delFile = new File(historyFileEOList.get(i).getFilePath());
                String s = Pattern.compile("\\\\").matcher(delFile.getAbsolutePath()).replaceAll("\\\\\\\\");
                File file = new File(s);
                id = historyFileEOList.get(i).getId();
                doDel(file, id);
            }
        }

        HistoryFileEO historyFileEO = new HistoryFileEO();
        historyFileEO.setId(UUID.randomUUID10());
        historyFileEO.setFileId(fileId);
        historyFileEO.setFilePath(historyPath);
        EventReceiveEO eventReceiveEO = eventReceiveEOService.selectByPrimaryKey(eventReceiveId);
        historyFileEO.setEditorId(eventReceiveEO.getReceiveUserId());
        historyFileEO.setEditorName(eventReceiveEO.getReceiveUserName());
        historyFileEO.setEditTime(new Date());
        historyFileEOService.insert(historyFileEO);
    }

    private void doDel(File file, String id) throws Exception {
        if (file != null) {
            boolean del = file.delete();
            if (del) {
                historyFileEOService.deleteByPrimaryKey(id);
            }
        }
    }

}
