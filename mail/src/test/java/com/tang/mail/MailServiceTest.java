package com.tang.mail;

import com.tang.mail.service.IMailService;
import com.tang.mail.test.MailService;
import com.tang.mail.service.impl.MailServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author guitang.he@getech.cn
 * @date 2020/9/30
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MailServiceTest {

    @Autowired
    private IMailService mailService;


    @Test
    public void sendAttachmentsMail() {
        Map<String, byte[]> files = new HashMap<>(16);
        files.put("XXX.jpg", getBytesByFile("D:\\素材\\952dc5ef044835440f6f7649a665d570.jpg"));
        files.put("application.properties", getBytesByFile("D:\\素材\\application.properties"));

        boolean b = mailService.sendAttachmentsMail("guitang.he@tcl.com", "这是邮件主题", "这是邮件内容", files);
        System.out.println("=========发送邮件结果============" + b);
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
