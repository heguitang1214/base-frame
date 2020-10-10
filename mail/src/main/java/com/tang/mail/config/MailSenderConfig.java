package com.tang.mail.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Component
public class MailSenderConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private List<JavaMailSenderImpl> senderList;

    /**
     * 初始化 sender
     */
    @PostConstruct
    public void buildMailSender() {
        List<MailConfig.MailProperties> mailConfigs = mailConfig.getConfigs();
        logger.info("初始化mailSender");
        mailConfigs.forEach(mailProperties -> {
            // 邮件发送者
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setDefaultEncoding(mailProperties.getDefaultEncoding());
            javaMailSender.setHost(mailProperties.getHost());
            javaMailSender.setPort(mailProperties.getPort());
            javaMailSender.setProtocol(mailProperties.getProtocol());
            javaMailSender.setUsername(mailProperties.getUsername());
            javaMailSender.setPassword(mailProperties.getPassword());

            // 添加数据
            senderList.add(javaMailSender);
        });
    }

    /**
     * 获取MailSender
     *
     * @return CustomMailSender
     */
    public JavaMailSenderImpl getSender() {
        if (senderList.isEmpty()) {
            buildMailSender();
        }
        // 随机返回一个JavaMailSender
        return senderList.get(new Random().nextInt(senderList.size()));
    }

    /**
     * 清理 sender
     */
    public void clear() {
        senderList.clear();
    }

}
