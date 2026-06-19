package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资料共享对象 ms_resource
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
public class MsResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资料ID */
    private Long resourceId;

    /** 资料标题 */
    @Excel(name = "资料标题")
    private String title;

    /** 资料描述 */
    @Excel(name = "资料描述")
    private String description;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 原始文件名 */
    @Excel(name = "原始文件名")
    private String fileName;

    /** 文件存储路径 */
    @Excel(name = "文件存储路径")
    private String filePath;

    /** 文件类型（pdf/docx/pptx等） */
    @Excel(name = "文件类型", readConverterExp = "p=df/docx/pptx等")
    private String fileType;

    /** 文件大小（字节） */
    @Excel(name = "文件大小", readConverterExp = "字=节")
    private Long fileSize;

    /** 下载次数 */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /** 上传者用户ID */
    @Excel(name = "上传者用户ID")
    private Long uploaderId;

    /** 上传者姓名 */
    @Excel(name = "上传者姓名")
    private String uploaderName;

    /** 状态（0正常 1禁用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=禁用")
    private String status;

    public void setResourceId(Long resourceId) 
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId() 
    {
        return resourceId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setDownloadCount(Long downloadCount) 
    {
        this.downloadCount = downloadCount;
    }

    public Long getDownloadCount() 
    {
        return downloadCount;
    }

    public void setUploaderId(Long uploaderId) 
    {
        this.uploaderId = uploaderId;
    }

    public Long getUploaderId() 
    {
        return uploaderId;
    }

    public void setUploaderName(String uploaderName) 
    {
        this.uploaderName = uploaderName;
    }

    public String getUploaderName() 
    {
        return uploaderName;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resourceId", getResourceId())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("categoryId", getCategoryId())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("downloadCount", getDownloadCount())
            .append("uploaderId", getUploaderId())
            .append("uploaderName", getUploaderName())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
