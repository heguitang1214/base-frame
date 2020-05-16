//package com.tang.file.service.email;
//
//import java.util.Properties;
//
//import javax.mail.Address;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//public class JavaMailTest1 {
//
//    public static int ReceivedCount = 1;//接收邮件地址的数组大小
//
//    public static void main(String[] args) throws MessagingException {
//        //1.创建信息配置对象，并配置相应信息
//        Properties props = new Properties();
//        //属性mail.debug是确定是否是开发模式，true为是，false为否
//        //属性mail.transport.protocol设置要使用的邮件协议
//        //属性mail.smtp.auth设置发送时是否校验用户名和密码
//        //还有其他属性，请查看相关文档
//        props.setProperty("mail.debug", "true");
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.smtp.auth", "true");
//
//        //2.通过配置信息创建session对像
//        Session session = Session.getInstance(props);
//        //session设置dbug的时候会将整个邮件交互过程信息打印早控制台
//        session.setDebug(true);
//
//        //3.通过session对象获取Message邮件信息对象
//        Message msg = new MimeMessage(session);
//        //设置邮件信息对象的各种字段信息
//        msg.setText("你好朋友！");//邮件正文
//        msg.setFrom(new InternetAddress("1125642188@qq.com"));//邮件接收地址
//
//        //3.获取传输对象，进行邮件发送
//        Transport transport = session.getTransport();
//        //连接信息：邮件服务器地址，端口号，用户名，密码
////        transport.connect("smtp.sina.com", 25, "emailtest3087", "123qwe");
//        transport.connect("smtp.163.com", 25, "he_guitang@163.com", "");// TODO: 2019/5/21
//        //创建邮件接收地址数组，并放置一个邮箱接收地址。
//        Address[] addresies = new Address[ReceivedCount];//数组大小为上面的成员变量指定
//        addresies[0] = new InternetAddress("heguitang@126.com");
//        transport.sendMessage(msg, addresies);
//
//        //4.关闭传输对象
//        transport.close();//关闭传输对象
//    }
//}
