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
 * @author guitang.he@getech.cn
 * @date 2020/10/9
 */
@Component
public class ResourcePathTemplateService implements ITemplate {

    // TODO: 2020/10/9 这里为什么一定要使用注入？？？
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
        if (CollectionUtils.isEmpty(ognlParam)){
            throw new RuntimeException("template data not null");
        }

        Context context = new Context();

        context.setVariables(ognlParam);

        return templateEngine.process(tempName, context);
    }
}
