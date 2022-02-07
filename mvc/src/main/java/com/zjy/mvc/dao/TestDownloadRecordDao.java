package com.zjy.mvc.dao;

import com.zjy.mvc.po.TestDownloadRecord;

import java.util.List;

public interface TestDownloadRecordDao {
    int insert(TestDownloadRecord record);
    List<TestDownloadRecord> queryList();
}
