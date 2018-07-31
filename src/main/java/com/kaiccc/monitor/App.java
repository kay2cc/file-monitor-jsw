package com.kaiccc.monitor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * 协助录音文件上传程序
 *
 * @author kaiccc
 */
public class App implements WrapperListener {

    private static final Log log = LogFactory.get();
    public static final Setting DPDS_SETTING = new Setting("dpds.setting");
    public static Queue queue;

    private App() {
        log.info("**** 协助录音文件上传程序 启动中 ... ****");

        UploadCmsUtil.CMS_ACCKEY = DPDS_SETTING.getStr("cms.accKey");
        UploadCmsUtil.CMS_ENDPOINT = DPDS_SETTING.getStr("cms.endpoint");
        UploadCmsUtil.CMS_SECRETKEY = DPDS_SETTING.getStr("cms.secretKey");
        UploadCmsUtil.CMS_BUCKET = DPDS_SETTING.getStr("cms.bucket");
        UploadFileUtil.PARAM_UPLOADER = DPDS_SETTING.getStr("param.uploader");
        App.queue = new Queue(DPDS_SETTING.getInt("queue"));

        WatchMonitorPhoneFile watchMonitorPhoneFile = new WatchMonitorPhoneFile();
        watchMonitorPhoneFile.createWatchMonitorService(FileUtil.file(DPDS_SETTING.getStr("phone.file.path")));

        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                UploadFileUtil.checkFileTask();
            }
        });

        CronUtil.setCronSetting(new Setting("cron.setting"));
        CronUtil.start();

        log.info("**** 协助录音文件上传程序 启动成功 ****");
    }

    public static void main(String[] args) {
        WrapperManager.start(new App(), args);
    }

    @Override
    public Integer start(String[] strings) {
        try {
            System.out.println("JWS 启动完成 ! ");
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public int stop(int i) {
        System.out.println("stop( " + i + ") ");
        return i;
    }

    @Override
    public void controlEvent(int i) {
        System.out.println("controlEvent( " + i + ") ");
    }
}
