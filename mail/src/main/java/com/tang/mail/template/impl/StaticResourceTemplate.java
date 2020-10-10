package com.tang.mail.template.impl;

import com.tang.mail.template.ITemplate;

import java.util.Map;

/**
 * 静态资源模板
 *
 * @author guitang.he@getech.cn
 * @date 2020/10/9
 */
public class StaticResourceTemplate implements ITemplate {

    @Override
    public String getMailContentTemplate(Object o) {
        return null;
    }

    @Override
    public String getMailContentTemplate(String tempName, Map<String, Object> ognlParam) {
        return null;
    }


}
