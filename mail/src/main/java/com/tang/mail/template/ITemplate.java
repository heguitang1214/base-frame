package com.tang.mail.template;

import java.util.Map;

/**
 * 获取邮件发送内容的模板
 *
 * @author tang
 * @date 2020/10/9
 */
public interface ITemplate {

    /**
     * 获取邮件发送内容的模板
     *
     * @param o 请求参数
     * @return 邮件发送内容
     */
    String getMailContentTemplate(Object o);

    /**
     * 获取资源文件夹下制定模板名称的模板
     *
     * @param tempName 模板名称
     * @param ognlParam        模板参数
     * @return 邮件发送内容
     */
    String getMailContentTemplate(String tempName, Map<String, Object> ognlParam);

}
