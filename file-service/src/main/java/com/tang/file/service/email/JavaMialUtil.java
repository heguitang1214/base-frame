package com.tang.file.service.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JavaMialUtil {
	private String username = null;
	private String password = null;
	private Authenticator auth = null;
	private MimeMessage mimeMessage = null;
	private Properties pros = null;
	private Multipart multipart = null;
	private BodyPart bodypart = null;

	/**
	 * 初始化账号密码并验证 创建MimeMessage对象 发送邮件必须的步骤:1
	 * 
	 * @param username 用户名
	 * @param password 密码
	 */
	public JavaMialUtil(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 初始化MimeMessage对象 发送邮件必须的步骤:3
	 */
	public void initMessage() {
		this.auth = new Email_Autherticator();
		Session session = Session.getDefaultInstance(pros, auth);
		session.setDebug(true); // 设置获取 debug 信息
		mimeMessage = new MimeMessage(session);
	}

	/**
	 * 设置email系统参数 接收一个map集合key为string类型，值为String 发送邮件必须的步骤:2
	 * 
	 */
	public void setPros(Map<String, String> map) {
		pros = new Properties();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			pros.setProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 验证账号密码 发送邮件必须的步骤
	 */
	public class Email_Autherticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	/**
	 * 设置发送邮件的基本参数(去除繁琐的邮件设置)
	 * 
	 * @param sub
	 *            设置邮件主题
	 * @param text
	 *            设置邮件文本内容
	 * @param rec
	 *            设置邮件接收人
	 */
	public void setDefaultMessagePros(String sub, String text, String rec)
			throws MessagingException, UnsupportedEncodingException {
		mimeMessage.setSubject(sub);
		mimeMessage.setText(text);
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));
		mimeMessage.setSentDate(new Date());
		mimeMessage.setFrom(new InternetAddress(username, username));
	}

	/**
	 * 设置主题
	 * 
	 */
	public void setSubject(String subject) throws MessagingException {
		mimeMessage.setSubject(subject);
	}

	/**
	 * 设置日期
	 * 
	 */
	public void setDate(Date date) throws MessagingException {
		mimeMessage.setSentDate(new Date());
	}

	/**
	 * 设置邮件文本内容
	 * 
	 */
	public void setText(String text) throws MessagingException {
		mimeMessage.setText(text);
	}

	/**
	 * 设置邮件头部
	 * 
	 */
	public void setHeader(String arg0, String arg1) throws MessagingException {
		mimeMessage.setHeader(arg0, arg1);
	}

	/**
	 * 设置邮件接收人地址 <单人发送>
	 * 
	 * @param recipient
	 * @throws MessagingException
	 */
	public void setRecipient(String recipient) throws MessagingException {
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
	}

	/**
	 * 设置邮件接收人地址 <多人发送>
	 * 
	 * @param recs  邮件接收人地址
	 * @throws MessagingException 消息异常
	 * @throws AddressException 地址异常
	 */
	public String setRecipients(List<String> recs) throws AddressException, MessagingException {
		if (recs.isEmpty()) {
			return "接收人地址为空!";
		}
		for (String str : recs) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
		}
		return "加入接收人地址成功!";
	}

	/**
	 * 设置邮件接收人地址 <多人发送>
	 * 
	 * @param sb <parms,parms2,parms.....>
	 * @throws MessagingException 消息异常
	 * @throws AddressException 地址异常
	 */
	@SuppressWarnings("static-access")
	public String setRecipients(StringBuffer sb) throws AddressException, MessagingException {
		if (sb == null || "".equals(sb)) {
			return "字符串数据为空!";
		}
		Address[] address = new InternetAddress().parse(sb.toString());
		mimeMessage.addRecipients(Message.RecipientType.TO, address);
		return "收件人加入成功";
	}

	/**
	 * 设置邮件发送人的名字
	 * 
	 * @param from
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void setFrom(String from) throws UnsupportedEncodingException, MessagingException {
		mimeMessage.setFrom(new InternetAddress(username, from));
	}

	/**
	 * 发送邮件<单人发送> return 是否发送成功
	 * 
	 */
	public String sendMessage() throws MessagingException {
		Transport.send(mimeMessage);
		return "success";
	}

	/**
	 * 设置附件
	 * 
	 * @param file
	 *            发送文件的路径
	 */
	public void setMultipart(String file) throws MessagingException, IOException {
		if (multipart == null) {
			multipart = new MimeMultipart();
		}
		multipart.addBodyPart(writeFiles(file));
		mimeMessage.setContent(multipart);
	}

	/**
	 * 设置附件<添加多附件>
	 * 
	 * @param fileList <接收List集合>
	 */
	public void setMultiparts(List<String> fileList) throws MessagingException, IOException {
		if (multipart == null) {
			multipart = new MimeMultipart();
		}
//		for (String s : fileList) {
//			multipart.addBodyPart(writeFiles(s));
//		}
		//有多个附件就循环writeFiles方法
		multipart.addBodyPart(writeFiles(""));
		mimeMessage.setContent(multipart);
	}

	/**
	 * 发送文本内容，设置编码方式 <方法与发送附件配套使用> <发送普通的文本内容请使用setText()方法>
	 * 
	 */
	public void setContent(String s, String type) throws MessagingException {
		if (multipart == null) {
			multipart = new MimeMultipart();
		}
		bodypart = new MimeBodyPart();
		bodypart.setContent(s, type);
		multipart.addBodyPart(bodypart);
		mimeMessage.setContent(multipart);
		mimeMessage.saveChanges();
	}

	/**
	 * 读取附件
	 * 
	 */
	public BodyPart writeFiles(String filePath) throws IOException, MessagingException {
//		File file = new File(filePath);
//		if (!file.exists()) {
//			throw new IOException("文件不存在!请确定文件路径是否正确");
//		}
		bodypart = new MimeBodyPart();
//		DataSource dataSource = new FileDataSource(file);
		
		//输入流设置到附件中
//		InputStream is = InputStreamUtil.getInputStream();
		InputStream is = null;
		DataSource dataSource = new ByteArrayDataSource(is, "application/octet-stream");
		bodypart.setDataHandler(new DataHandler(dataSource));
		// 文件名要加入编码，不然出现乱码
//		bodypart.setFileName(MimeUtility.encodeText(file.getName()));
		
		//设置附件编码    路径随便填
		File f = new File("D:\\java\\test1\\bbccc111.xls");
		bodypart.setFileName(MimeUtility.encodeText(f.getName()));
		return bodypart;
	}

}
