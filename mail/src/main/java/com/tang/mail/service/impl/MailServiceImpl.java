package com.tang.mail.service.impl;

import com.tang.mail.service.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * 邮件发送服务
 *
 * @author guitang.he@getech.cn
 * @date 2020/9/30
 */
@Component
public class MailServiceImpl implements IMailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    private final static String SEPARATOR_COMMA = ",";


    private MimeMessageHelper getMimeMessageHelper(MimeMessage mimeMessage, String subject, String to, String cc, String bcc, boolean multipart) {
        try {
            // 是否需要设置
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart);
            helper.setSubject(subject);
            if (to == null) {
                throw new RuntimeException("The recipient of the mail can't be empty");
            }
            String[] toArray = to.split(SEPARATOR_COMMA);
            helper.setTo(toArray);
            if (!StringUtils.isEmpty(cc)) {
                String[] ccArray = cc.split(SEPARATOR_COMMA);
                helper.setCc(ccArray);
            }
            if (!StringUtils.isEmpty(bcc)) {
                String[] bccArray = bcc.split(SEPARATOR_COMMA);
                helper.setBcc(bccArray);
            }
            helper.setFrom(from);
            return helper;
        } catch (MessagingException messagingException) {
            System.out.println("messagingException 异常");
            throw new RuntimeException(messagingException);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件设置
        message.setSubject(subject);
        message.setText(content);
        message.setTo(to);
        message.setFrom(from);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("发送简单邮件失败，异常信息为：", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = getMimeMessageHelper(message, subject, to, null, null, true);

            helper.setText(content, true);
            // 资源的处理，读取文件的方式
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);
            // 资源的处理，使用byte数组的方式
            // todo type 从对应的工具类中获取
//            byte[] data = getBytesByFile(rscPath);
//            if (data == null) {
//                throw new RuntimeException("不能为空！");
//            }
//            helper.addInline(rscId, new ByteArrayResource(data), "image/jpeg");

            javaMailSender.send(message);
            logger.info("嵌入静态资源的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送嵌入静态资源的邮件时发生异常！", e);
        }
    }

    @Override
//    @Async("mailpool")
    public void sendAttachmentsMail(String to, String subject, String content, Map<String, byte[]> files) {
        try {
            //1、创建一个复杂的邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = getMimeMessageHelper(mimeMessage, subject, to, null, null, true);

            helper.setText(content);
            // 添加附件
            for (String fileName : files.keySet()) {
                byte[] value = files.get(fileName);
                helper.addAttachment(fileName, new ByteArrayResource(value));
            }
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("发送复杂邮件失败，异常信息为：", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendZipAttachmentsMail(String to, String subject, String content, Map<String, byte[]> files) {
        // TODO: 2020/10/10 需要实现
    }


    @Override
    public void sendThymeleafMail(String subject, String to, String cc, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = getMimeMessageHelper(mimeMessage, subject, to, cc, null, false);
        try {
            helper.setText(content, true);
        } catch (MessagingException messagingException) {
            logger.error("发送带有模板的邮件失败，设置文件内容失败，异常信息为：", messagingException);
            throw new RuntimeException(messagingException);
        } catch (Exception e) {

        }
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendThymeleafMailBydb(String subject, String to, String cc, String content) {
        // TODO: 2020/10/10 需要实现
    }

    //==============================================================================

}
