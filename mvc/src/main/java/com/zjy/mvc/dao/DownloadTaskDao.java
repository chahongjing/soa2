package com.zjy.mvc.dao;

import com.zjy.mvc.po.DownloadTask;

import java.util.List;

public interface DownloadTaskDao {
    int insert(DownloadTask task);
    DownloadTask get(String id);
    int update(DownloadTask task);
    List<DownloadTask> queryList();
}
