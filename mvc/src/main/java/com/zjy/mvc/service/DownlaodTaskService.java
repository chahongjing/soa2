package com.zjy.mvc.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjy.mvc.common.utils.CSVUtils;
import com.zjy.mvc.common.utils.Utils;
import com.zjy.mvc.dao.DownloadTaskDao;
import com.zjy.mvc.dao.TestDownloadRecordDao;
import com.zjy.mvc.enums.DownTaskStatus;
import com.zjy.mvc.po.DownloadTask;
import com.zjy.mvc.po.TestDownloadRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DownlaodTaskService {
    @Autowired
    private DownloadTaskDao downloadTaskDao;
    @Autowired
    private TestDownloadRecordDao testDownloadRecordDao;

    // 创建下载任务
    public int addTask(DownloadTask task) {
        task.setId(Utils.getUuid());
        task.setCreatedDate(new Date());
        task.setStatus(DownTaskStatus.CREATED);
        int i = downloadTaskDao.insert(task);
        // 开始处理任务，异步线程池
        this.startTask(task.getId());
        return i;
    }

    /**
     * 开始处理任务
     * @param id
     */
    public void startTask(String id) {
        DownloadTask downloadTask = downloadTaskDao.get(id);
        if (downloadTask == null) {
            log.warn("下载任务不存在！{}", id);
            return;
        }
        downloadTask.setStatus(DownTaskStatus.STARTED);
        downloadTask.setUpdatedDate(new Date());
        downloadTaskDao.update(downloadTask);
        // 开始处理任务
        Page<TestDownloadRecord> page = new Page<>(1, 5000);
        page.setOrderBy("userId");
        PageInfo<TestDownloadRecord> pageInfo = this.queryPageList(page);
        if (pageInfo.getTotal() == 0) {
            downloadTask.setStatus(DownTaskStatus.FINISHED);
            downloadTask.setMessage("没有数据");
            downloadTask.setProgress(100);
            downloadTask.setUpdatedDate(new Date());
            downloadTaskDao.update(downloadTask);
            return;
        }

        try {
            // 初始化文件相关
            StringWriter stringWriter = new StringWriter();
            BufferedWriter bw = new BufferedWriter(stringWriter);
            CSVPrinter csvPrinter = CSVUtils.initPrinter(bw);
            // 循环所有页数
            int pages = pageInfo.getPages();
            for (int i = 0; i < pages; i++) {
                if (i != 0) {
                    page = new Page<>(i, 5000);
                    page.setOrderBy("userId");
                    pageInfo = this.queryPageList(page);
                }
                if (pageInfo.getTotal() == 0) {
                    break;
                }
                // 处理数据到文件
                CSVUtils.fillData(csvPrinter, pageInfo.getList());
                // 修改进度
                downloadTask.setStatus(DownTaskStatus.STARTED);
                downloadTask.setProgress((int) (i * 100.0f / pages));
                downloadTask.setUpdatedDate(new Date());
                downloadTaskDao.update(downloadTask);
                TimeUnit.SECONDS.sleep(1);
            }
            String url = "D:/tmp/myexcel.csv";
            // 保存文件
            byte[] writerBytes = CSVUtils.getWriterBytes(stringWriter);
            FileUtils.writeByteArrayToFile(new File(url), writerBytes);
//            byteToFile(writerBytes, url);
            // 更新数据
            downloadTask.setStatus(DownTaskStatus.FINISHED);
            downloadTask.setProgress(100);
            downloadTask.setFileUrl(url);
            downloadTask.setFileName("yourFileName.csv");
            downloadTask.setUpdatedDate(new Date());
            downloadTaskDao.update(downloadTask);
        } catch (Exception ex) {
            downloadTask.setMessage(ex.getMessage());
            String stackTrace = Arrays.toString(ex.getStackTrace());
            downloadTask.setStackTrace(stackTrace.substring(0, Math.min(450, stackTrace.length())));
            downloadTask.setUpdatedDate(new Date());
            downloadTask.setStatus(DownTaskStatus.ERROR);
            downloadTaskDao.update(downloadTask);
        }
    }

    /**
     * 批量获取数据
     * @param page
     * @return
     */
    public PageInfo<TestDownloadRecord> queryPageList(Page<TestDownloadRecord> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize()).setOrderBy(page.getOrderBy());
        return new PageInfo<>(testDownloadRecordDao.queryList());
    }

    /**
     * byte转文件
     * @param in
     * @param filePath
     * @return
     */
    public static File byteToFile(byte[] in, String filePath) {
        File file = new File(filePath);

        try (OutputStream output = new FileOutputStream(file);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {
            bufferedOutput.write(in);
            bufferedOutput.flush();
            output.flush();
        } catch (Exception ex) {
        }
        return file;
    }

    /**
     * 添加数据记录
     * @param record
     * @return
     */
    public int addRecord(TestDownloadRecord record) {
        return testDownloadRecordDao.insert(record);
    }
}
