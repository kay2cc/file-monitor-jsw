package com.kaiccc.monitor;


import cn.hutool.core.util.StrUtil;

/**
 * 业务单(银保)  参数
 * @author xt
 */
public enum ETransactionBankParam {
    /**
     * 投保单号
     */
    INSURANCENO("insuranceno", "投保单号"),
    /**
     * 上传人
     */
    UPLOADER("uploader", "上传人"),
    /**
     * 录制日期
     */
    RECORDDATE("recorddate", "录制日期"),
    /**
     * 客户信息
     */
    PHONENUM("phonenum", "电话号码"),
    /**
     *
     */
    OTHER("other", "其他参数");

    private String key;
    private String description;

    ETransactionBankParam(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ETransactionBankParam getTransactionParam(String key) {
        ETransactionBankParam eTransactionBankParam = ETransactionBankParam.OTHER;

        for (ETransactionBankParam ct : ETransactionBankParam.values()) {
            if (StrUtil.equalsIgnoreCase(ct.getKey(), key)){
                eTransactionBankParam = ct;
                break;
            }
        }

        return eTransactionBankParam;
    }
}
