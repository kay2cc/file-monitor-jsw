package com.kaiccc.monitor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 检查文件 辅助上传
 *
 * @author kaiccc
 */
public class UploadFileUtil {

    private static final Log log = LogFactory.get();
    public static String PARAM_UPLOADER = "";

    /**
     * 检查目录中是否有文件，避免遗漏上传
     *
     * @return
     */
    public static void checkFileTask() {
        log.info("文件目录检查功能 start ");
        // 队列中有任务 不执行
        if (App.queue.size() > 0) {
            log.error("任务队列中还有 {} 个任务", App.queue.size());
            return;
        }
        List<File> files = FileUtil.loopFiles(new File(App.DPDS_SETTING.getStr("phone.file.path")));
        log.info("发现目录中有 {} 个文件!", files.size());
        for (File file : files) {
            ThreadUtil.execute(new UploadThread(file.getName()));
        }
        log.info("文件目录检查功能 end ");
    }

    /**
     * 通过文件名 解析业务参数
     *
     * @param fileName B20180720-172200-15901731692-01-000020
     * @return
     */
    public static Map<String, String> getParamMap(String fileName) throws Exception {
        Map<String, String> param = MapUtil.newHashMap();
        String[] fileParam = fileName.split("-");

        long recorddate = DateUtil.parse(StrUtil.subSuf(fileParam[0],1) + fileParam[1]).getTime();

        String phoneNumber = fileParam[2];
        if(StrUtil.isEmpty(phoneNumber)){
            phoneNumber = Long.toString(recorddate);
        }

        param.put(ETransactionBankParam.INSURANCENO.getKey(), phoneNumber);
        param.put(ETransactionBankParam.UPLOADER.getKey(), PARAM_UPLOADER);
        param.put(ETransactionBankParam.PHONENUM.getKey(), phoneNumber);

        param.put(ETransactionBankParam.RECORDDATE.getKey(), Long.toString(recorddate));
        return param;
    }


}
