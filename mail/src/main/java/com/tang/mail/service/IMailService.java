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
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送带静态资源的邮件
     *
     * @param to      邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param rscPath 资源路径
     * @param rscId   资源id
     */
    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);

    /**
     * 发送带附件的邮件
     *
     * @param to      邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param files   附件字节流，key为文件名称，byte[]为对应的文件流
     */
    void sendAttachmentsMail(String to, String subject, String content, Map<String, byte[]> files);

    /**
     * 发送带附件的邮件，并压缩对应的附件
     *
     * @param to      邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param files   附件字节流，key为文件名称，byte[]为对应的文件流
     */
    void sendZipAttachmentsMail(String to, String subject, String content, Map<String, byte[]> files);

    /**
     * 邮件模板：
     * 发送带有html模板的邮件，模板中的图片显示为图片链接，而非文件
     * 模板来自项目资源文件夹的读取
     *
     * @param subject 邮件主题
     * @param to      邮件接收人
     * @param cc      邮件抄送人
     * @param content 邮件内容
     */
    void sendThymeleafMail(String subject, String to, String cc, String content);

    /**
     * 邮件模板：
     * 发送带有html模板的邮件，模板中的图片显示为图片链接，而非文件
     * 模板来自其他存储的设备，如：mysql、redis
     *
     * @param subject 邮件主题
     * @param to      邮件接收人
     * @param cc      邮件抄送人
     * @param content 邮件内容
     */
    void sendThymeleafMailBydb(String subject, String to, String cc, String content);


    // 支持回调的方式


}
