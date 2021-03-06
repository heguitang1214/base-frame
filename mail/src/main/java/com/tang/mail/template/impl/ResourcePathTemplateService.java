package com.tang.mail.template.impl;

import com.tang.mail.template.ITemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * 使用资源模板获取数据
 *
 * @author tang
 * @date 2020/10/9
 */
@Component
public class ResourcePathTemplateService implements ITemplate {

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String getMailContentTemplate(Object o) {
        throw new RuntimeException("该模板不支持该参数获取");
    }

    @Override
    public String getMailContentTemplate(String tempName, Map<String, Object> ognlParam) {
        if (StringUtils.isEmpty(tempName)) {
            throw new RuntimeException("template name not null");
        }
        if (CollectionUtils.isEmpty(ognlParam)) {
            throw new RuntimeException("template data not null");
        }

        Context context = new Context();
        context.setVariables(ognlParam);
        // 这里使用Spring注入的对象，而不是使用new对象
        return templateEngine.process(tempName, context);
    }
}
