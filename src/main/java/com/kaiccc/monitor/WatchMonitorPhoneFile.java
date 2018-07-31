package com.kaiccc.monitor;

import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 监听 电话录音盒 上传的文件
 * @author kaiccc
 */
public class WatchMonitorPhoneFile {

    private static final Log log = LogFactory.get();
    /**
     * 创建文件监听 服务
     */
    public void createWatchMonitorService(File watchPath){

        log.info("WatchMonitor FilePath: {} ", watchPath.getAbsolutePath());

        WatchMonitor watchMonitor = WatchMonitor.create(watchPath, WatchMonitor.EVENTS_ALL);
        watchMonitor.setWatcher(new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                log.info("创建：{}-> {}", currentPath, event.context().toString());
                ThreadUtil.execute(new UploadThread(event.context().toString()));
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("修改：{}-> {}", currentPath, obj);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("删除：{}-> {}", currentPath, obj);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("Overflow：{}-> {}", currentPath, obj);
            }
        });

        watchMonitor.setMaxDepth(3);
        watchMonitor.start();

        log.info("WatchMonitor Success");
    }

}
