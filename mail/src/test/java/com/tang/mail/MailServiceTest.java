package com.tang.mail;

import com.tang.mail.service.MailService;
import com.tang.mail.service.impl.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;

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
    private MailService mailService;

    @Autowired
    private EmailService emailService;


    @Test
    public void test() {
        try {
            String s = emailService.sendMineMail();
            System.out.println(s + "=====================");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
