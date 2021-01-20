package com.adc.da.file.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_FILE FileEOEntity<br>
 * <b>日期：</b> 2017-12-24 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>userId -> user_id</li>
 * <li>url -> url</li>
 * <li>savePath -> save_path</li>
 * <li>remark -> remark</li>
 * <li>lastUpdateTime -> last_update_time</li>
 * <li>fileType -> file_type</li>
 * <li>fileName -> file_name</li>
 * <li>createTime -> create_time</li>
 * <li>contentType -> content_type</li>
 * <li>fileId -> file_id</li>
 *
 * @author comments created by Lee Kwanho
 * @see com.adc.da.file.controller.FileUploadRestController
 * @see com.adc.da.file.controller.FileDownloadRestController
 * date 2018-08-28
 */
public class MyFileEO extends BaseEntity {

    /**
     * 用户id ，默认为空，保留字段
     */
    private String userId;

    /**
     * url信息，默认为空，保留字段
     */
    private String url;

    /**
     * 保存路径，相对路径
     */
    private String savePath;

    /**
     * 保留字段
     */
    private String remark;

    /**
     * 保留字段
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 创建时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * Content类型
     */
    private String contentType;

    /**
     * id
     */
    private String fileId;

    private String foreignId;

    private  String fileSize ;

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    /**  **/
    public String getUserId() {
        return this.userId;
    }

    /**  **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**  **/
    public String getUrl() {
        return this.url;
    }

    /**  **/
    public void setUrl(String url) {
        this.url = url;
    }

    /**  **/
    public String getSavePath() {
        return this.savePath;
    }

    /**  **/
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**  **/
    public String getRemark() {
        return this.remark;
    }

    /**  **/
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**  **/
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /**  **/
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**  **/
    public String getFileType() {
        return this.fileType;
    }

    /**  **/
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**  **/
    public String getFileName() {
        return this.fileName;
    }

    /**  **/
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public String getContentType() {
        return this.contentType;
    }

    /**  **/
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**  **/
    public String getFileId() {
        return this.fileId;
    }

    /**  **/
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

}
