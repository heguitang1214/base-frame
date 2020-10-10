package com.tang.mail.service;

/**
 * 发送邮件后的回调
 *
 * @author guitang.he@getech.cn
 * @date 2020/10/10
 */
public interface IMailCallback {

    /**
     * 邮件回调方法
     */
    void mailCallback();

}
