package com.kaiccc.monitor;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.File;

/**
 * 文件上传线程
 * @author kaiccc
 */
public class UploadThread extends Thread{

    private static final Log log = LogFactory.get();
    /**
     * 上传文件名称
      */

    private String uploadFileName;

    @Override
    public void run() {

        String uploadPath = App.DPDS_SETTING.getStr("phone.file.path");
        String uploadFilePath = uploadPath + File.separator + uploadFileName;

        log.info("uploadPath:{} ,uploadFileName:{}", uploadPath , uploadFileName);

        File file = new File(uploadFilePath);
        /*
         * 可能出现大文件 长时间copy 情况。如果重命名成功，说明文件已copy完成。
         */
        while (true){
            try {
                Thread.sleep(3000);
                boolean back = file.renameTo(new File(uploadFilePath));
                log.info("{}", back);
                if(back){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            UploadCmsUtil.uploadCms(uploadFilePath, UploadFileUtil.getParamMap(uploadFileName));
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public UploadThread(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

}
