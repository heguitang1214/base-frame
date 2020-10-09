package com.tang.mail.service.impl;

import com.tang.mail.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送服务
 *
 * @author guitang.he@getech.cn
 * @date 2020/9/30
 */
@Component
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;
    //    @Value("spring.mail.username")
    private String from = "1125642188@qq.com";


    @Override
    public boolean sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件设置
        message.setSubject(subject);
        message.setText(content);
        message.setTo(to);
        message.setFrom(from);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            // 日志
            return false;
        }
        return true;
    }

    @Override
    public boolean sendAttachmentsMail(String to, String subject, String content, Map<String, byte[]> files) {
        try {
            //1、创建一个复杂的邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            //邮件主题
            helper.setSubject(subject);
            //邮件内容
            helper.setText(content);
            helper.setTo(to);
            helper.setFrom(from);

            //添加附件
            for (String str : files.keySet()) {
                byte[] value = files.get(str);
                helper.addAttachment(str, new ByteArrayResource(value));
            }
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {

            return false;
        }
        return true;
    }


    public String sendMineMail() throws MessagingException {
        Map<String, Object> params = new HashMap<>();
        Map<String, byte[]> files = new HashMap<>();
        //1、创建一个复杂的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        files.put("XXX.jpg", getBytesByFile("D:\\素材\\952dc5ef044835440f6f7649a665d570.jpg"));
        files.put("application.properties", getBytesByFile("D:\\素材\\application.properties"));

        //邮件主题
        helper.setSubject("SAM邮件");
        //文本中添加图片
        helper.addInline("image1", new FileSystemResource("D:\\素材\\5225cd55c0c365cd09d09d0980106cbb.jpg"));
        //邮件内容
        helper.setText("这是邮件的内容", true);
        helper.setTo("guitang.he@tcl.com");
        helper.setFrom("1125642188@qq.com");
        //附件添加图片
        for (String str : files.keySet()) {
            byte[] value = files.get(str);
            helper.addAttachment(str, new ByteArrayResource(value));
        }
        javaMailSender.send(mimeMessage);
        return "复杂邮件发送！";
    }

    private static byte[] getBytesByFile(String filePath) {
        try {
            File file = new File(filePath);
            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}