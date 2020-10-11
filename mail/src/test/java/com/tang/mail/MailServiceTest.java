package com.tang.mail;

import com.tang.mail.service.IMailService;
import com.tang.mail.service.impl.MailServiceImpl;
import com.tang.mail.template.ITemplate;
import com.tang.mail.template.impl.ResourcePathTemplateService;
import com.tang.mail.util.ZipUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.misc.BASE64Encoder;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author tang
 * @date 2020/9/30
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MailServiceTest {

    @Autowired
    private IMailService mailService;

    @Autowired
    private MailServiceImpl mailServiceImpl;

    @Autowired
    private ResourcePathTemplateService resourcePathTemplateService;

    public static void main(String[] args) {

    }

    /**
     * 简单邮件测试
     */
    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("XXX@163.com", "这是邮件主题", "这是邮件内容-简单邮件");
        System.out.println("=======简单邮件发送成功========");
    }

    /**
     * 静态资源邮件测试
     */
    @Test
    public void sendInlineResourceMail() {
        String rscId = "neo006";
        String content = "<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        String imgPath = "D:\\素材\\952dc5ef044835440f6f7649a665d570.jpg";

        mailService.sendInlineResourceMail("XXX@163.com", "主题：这是有图片的邮件", content, imgPath, rscId);
    }

    /**
     * 带附件邮件测试
     */
    @Test
    public void sendAttachmentsMail() {
        Map<String, byte[]> files = new HashMap<>(16);
        files.put("XXX.jpg", getBytesByFile("D:\\素材\\952dc5ef044835440f6f7649a665d570.jpg"));
        files.put("application.properties", getBytesByFile("D:\\素材\\application.properties"));
        files.put("中文文本.txt", getBytesByFile("D:\\素材\\中文文本.txt"));

        mailService.sendAttachmentsMail("XXX@163.com", "这是邮件主题3", "这是邮件内容", files);

        System.out.println("=========发送邮件结果============");
    }

    /**
     * 邮件模板测试
     */
    @Test
    public void sendThymeleafMail() throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "这是一个新建的thymeleaf模板");
        variables.put("link", "https://www.cnblogs.com/swzx-1213/");
        variables.put("image1", "https://s1.ax1x.com/2020/04/14/JShDYt.th.jpg");

//        ITemplate template = new ResourcePathTemplateService();
        String content = resourcePathTemplateService.getMailContentTemplate("email", variables);

        mailService.sendThymeleafMail("邮件主题", "XXX@163.com", null, content);

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
