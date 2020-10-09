package com.tang.mail.service;

import java.util.Map;

/**
 * 发送邮件接口
 *
 * @author guitang.he@getech.cn
 * @date 2020/10/9
 */
public interface IMailService {

    /**
     * 发送简单邮件
     *
     * @param to      邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 是否发送成功
     */
    boolean sendSimpleMail(String to, String subject, String content);


    /**
     * 发送带附件的邮件
     *
     * @param to      邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param files   附件字节流，key为文件名称，byte[]为对应的文件流
     * @return 是否发送成功
     */
    boolean sendAttachmentsMail(String to, String subject, String content, Map<String, byte[]> files);


}
