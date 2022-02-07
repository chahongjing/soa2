package com.zjy.mvc.po;

import com.zjy.mvc.enums.DownTaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DownloadTask {
    private String id;
    private String createdBy;
    private String createdName;
    private Date createdDate;
    private Date updatedDate;
    private String fileUrl;
    private String fileName;
    private int progress;
    private DownTaskStatus status;
    private String message;
    private String stackTrace;
}
