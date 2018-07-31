package com.kaiccc.monitor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.google.gson.Gson;
import com.yuancore.cms.client.CmsClient;
import com.yuancore.utils.UUIDUtils;

import java.io.FileInputStream;
import java.util.Map;

/**
 * 上传文件到cms
 * @author kaiccc
 */
public class UploadCmsUtil {

    private static final Log log = LogFactory.get();

    public static String CMS_ENDPOINT = "";
    public static String CMS_ACCKEY = "";
    public static String CMS_SECRETKEY = "";
    public static String CMS_BUCKET = "";

    public static void uploadCms(String uploadFilePath, Map<String, String> params){

        /*
         * 生成任务 加入队列
         */
        App.queue.put(uploadFilePath);
        String uuid = UUIDUtils.generateUUID();
        log.info("queue:{}, 线程名:{}, UUID:{}, uploadFilePath:{}, params:{}",
                        App.queue.size(),Thread.currentThread().getName(),uuid,uploadFilePath, new Gson().toJson(params));

        FileInputStream fis = null;
        boolean cmsRest = false;
        try {
            fis = new FileInputStream(uploadFilePath);
            CmsClient cmsClient = new CmsClient(CMS_ENDPOINT, CMS_ACCKEY, CMS_SECRETKEY);
            cmsRest = cmsClient.simpleUpload(CMS_BUCKET, uuid, fis, params);
        } catch (Exception e) {
            log.error(e, "上传到cms 失败，id：{}，uploadFilePath: {} ,params : {}", uuid, uploadFilePath, new Gson().toJson(params));
            e.printStackTrace();
        } finally {
            IoUtil.close(fis);
            /*
             * 完成任务 移除队列
             */
            App.queue.take();
            log.info("queue:{}", App.queue.size());
        }

        if (cmsRest){
            FileUtil.del(uploadFilePath);
        }
    }
}
