//package com.tang.mail.test;
//
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Properties;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.Address;
//import javax.mail.Authenticator;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
//import org.dom4j.DocumentException;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.SAXReader;
//import org.dom4j.io.XMLWriter;
//
//import com.core.cbx.action.actionContext.SaveDoc;
//import com.core.cbx.action.exception.ActionException;
//import com.core.cbx.common.type.DateTime;
//import com.core.cbx.data.DynamicEntityModel;
//import com.core.cbx.data.constants.ColorAlbert;
//import com.core.cbx.data.entity.DynamicEntity;
//import com.core.cbx.data.exception.DataException;
//
///**
// * @author Albert.chen
// *
// */
//public class SaveDocAction extends com.core.cbx.action.SaveDocAction<SaveDoc>{
//
//    /* (non-Javadoc)
//     * @see com.core.cbx.action.SaveDocAction#process(com.core.cbx.action.actionContext.SaveDoc)
//     */
//
//    @Override
//    protected void process(final SaveDoc actionContext) throws ActionException {
//
//
//        final DynamicEntity doc = actionContext.getDoc();
//
//         //getValue
//        final BigDecimal defaultValue = new BigDecimal(0);
//
//        final BigDecimal rgb_number = doc.getBigDecimal(ColorAlbert.RGB_CODE,defaultValue);
//
//        final BigDecimal cmyk_number = doc.getBigDecimal(ColorAlbert.CMYK_CODE,defaultValue);
//
//        final BigDecimal mul_result =rgb_number.multiply(cmyk_number);
//
//         //setValue
//        doc.put(ColorAlbert.HSV_CODE, mul_result);
//
//        doc.put(ColorAlbert.STATUS, ColorAlbert.WorkflowStatus.IN_PROGRESS);
//
//        //save information
//        super.process(actionContext);
//
//
//    }
//
//    @Override
//    protected void postprocess(SaveDoc actionContext) throws ActionException {
//        // TODO Auto-generated method stub
//
//        final DynamicEntity doc = actionContext.getDoc();
//        try {
//            final String docString=DynamicEntityModel.toLightXml(doc);
//            final String refNo=doc.getReference();
//            final DateTime updatedOn=doc.getDateTime(ColorAlbert.UPDATED_ON);
//            final String updateUser=doc.getString(ColorAlbert.UPDATE_USER);
//            final String fileName=strChangeXML(docString,refNo);
//            compressedFile(fileName, "D:\\"+ refNo +".zip");
//            sengMail(refNo, fileName, updatedOn, updateUser);
//        } catch (final DataException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (final IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (final MessagingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (final Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            System.out.println("压缩文件生成失败...");
//        }
//
//    }
//
//    //将字符串string类型转换成xml文件
//    public static String strChangeXML(final String str, final String refNo) throws IOException {
//           final SAXReader saxReader = new SAXReader();
//           final StringBuffer stringBuffer=new StringBuffer();
//           stringBuffer.append(refNo).append(".xml");
//           final String fileName=stringBuffer.toString();
//           org.dom4j.Document document;
//           try {
//            document =saxReader.read(new ByteArrayInputStream(str.getBytes("UTF-8")));
//            final OutputFormat format = OutputFormat.createPrettyPrint();
//            /** 将document中的内容写入文件中 */
//            final XMLWriter writer = new XMLWriter(new FileWriter(new File(fileName)),format);
//            writer.write(document);
//            writer.close();
//           } catch (final DocumentException e) {
//            e.printStackTrace();
//           }
//
//           return fileName;
//   }
//
//    public static void sengMail( final String refNo,final String fileName, DateTime updatedOn, String updateUser) throws MessagingException{
//        // 创建邮件的发送过程中用到的主机和端口号的属性文件
//        final Properties pro = new Properties();
//        // 设置邮件发送方的主机地址如果是163邮箱，则为smtp.163.com
//        // 如果是其他的邮箱可以参照http://wenku.baidu.com/link?url=Cf-1ggeW3e7Rm9KWfz47UL7vvkRpPxAKBlYoTSGpnK4hxpJDiQ0A4lRoPDncMlcMIvUpEn6PD0aObgm5zJaM7AOGkRdccSx6HDH2fSWkxIq这个文档
//        pro.put("mail.smtp.host", "*******.com");
//        // 设置发送邮件端口号
//        pro.put("mail.smtp.port", "25");
//        pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        // 设置邮件发送需要认证
//        pro.put("mail.smtp.auth", "true");
//        // 创建邮件验证信息，即发送邮件的用户名和密码
//        final Authenticator authenticator = new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                // 重写验证方法，填写用户名，密码
//                return new PasswordAuthentication("*******.com", "ma2s******");
//            }
//        };
//
//        // 根据邮件会话 构建一个邮件的session
//        final Session sendMailSession = Session
//                .getDefaultInstance(pro, authenticator);
//        //打印信息
//        sendMailSession.setDebug(true);
//        // 创建一个邮件消息
//        final Message message = new MimeMessage(sendMailSession);
//        // 创建邮件发送者地址
//        final Address sourceAddress = new InternetAddress("******.com");
//        // 将原地址设置到消息的信息中
//        message.setFrom(sourceAddress);
//        // 创建邮件的接收者地址
//        final Address destAddress = new InternetAddress("***********.com");
//        final Address ccAddress=new InternetAddress("************.com");
//        // 将接收者的地址设置到消息的信息中
//        message.setRecipient(Message.RecipientType.TO, destAddress);
//        // 将接收者的地址设置到消息的信息中
//        message.setRecipient(Message.RecipientType.CC, ccAddress);
//        // 设置邮件的主题
//        final StringBuffer strBuf2=new StringBuffer();
//        strBuf2.append("subject:").append(refNo).append(".xml ").append("was updatedp[Do Not Reply]");
//        message.setSubject(strBuf2.toString());
//
//        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
//        final Multipart multipart = new MimeMultipart();
//
//        //设置邮件的文本内容
//        final BodyPart contentPart = new MimeBodyPart();
//
//        final StringBuffer strBuf=new StringBuffer();
//        strBuf.append("subject:").append(refNo).append(".xml ").append("was updatedp[Do Not Reply]").append("<br/><br/>");
//        strBuf.append("-------------------------------------------------------------").append("<br/>");
//        strBuf.append(" Dear Customer,").append("<br/><br/>");
//        strBuf.append(refNo).append(" is updated at").append(updatedOn).append(" by ").append(updateUser).append(".").append("<br/><br/>");
//        strBuf.append("Regards,").append("<br/>");
//        strBuf.append("CBX System").append("<br/>");
//        // 设置邮件的发送内容
//        contentPart.setContent(strBuf.toString(),"text/html;charset=UTF-8");
//        multipart.addBodyPart(contentPart);
//        //添加附件
//        final BodyPart messageBodyPart= new MimeBodyPart();
////        final String filename="file.txt";
//        final DataSource source = new FileDataSource(fileName);
//        //添加附件的内容
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        messageBodyPart.setFileName(fileName);
//
//        multipart.addBodyPart(messageBodyPart);
//
//        //添加附件2
//        final BodyPart messageBodyPart2= new MimeBodyPart();
//        final String zipFileName=refNo +".zip";
//        final DataSource source2 = new FileDataSource("D:\\"+ zipFileName);
//        //添加附件的内容
//        messageBodyPart2.setDataHandler(new DataHandler(source));
//        messageBodyPart2.setFileName(zipFileName);
//
//        multipart.addBodyPart(messageBodyPart2);
//
//
//
//
//
//
//        //将multipart对象放到message中
//        message.setContent(multipart);
//        //保存邮件
//        message.saveChanges();
//        // 可以设置邮件的发送时间(就是对方看邮件发送的时间)
//        // String sendDate = "2013-12-23 17:55:00";
//        // Date date = new
//        // SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sendDate);
//        // message.setSentDate(date);
//
//        // 发送邮件
//        Transport.send(message);
//    }
//
//
//    /**
//     * @desc 将源文件/文件夹生成指定格式的压缩文件,格式zip
//     * @param resourePath 源文件/文件夹
//     * @param targetPath  目的压缩文件保存路径
//     * @return void
//     * @throws Exception
//     */
//    public void compressedFile(String resourcesPath,String targetPath) throws Exception{
//        final File resourcesFile = new File(resourcesPath);     //源文件
//        final File targetFile = new File(targetPath);           //目的
//        //如果目的路径不存在，则新建
//        if(!targetFile.exists()){
//            targetFile.mkdirs();
//        }
//
//        final String targetName = resourcesFile.getName()+".zip";   //目的压缩文件名
//        final FileOutputStream outputStream = new FileOutputStream(targetPath+"\\"+targetName);
//        final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));
//
//        createCompressedFile(out, resourcesFile, "");
//
//        out.close();
//    }
//
//    /**
//     * @desc 生成压缩文件。
//     *                  如果是文件夹，则使用递归，进行文件遍历、压缩
//     *       如果是文件，直接压缩
//     * @param out  输出流
//     * @param file  目标文件
//     * @return void
//     * @throws Exception
//     */
//    public void createCompressedFile(ZipOutputStream out,File file,String dir) throws Exception{
//        //如果当前的是文件夹，则进行进一步处理
//        if(file.isDirectory()){
//            //得到文件列表信息
//            final File[] files = file.listFiles();
//            //将文件夹添加到下一级打包目录
//            out.putNextEntry(new ZipEntry(dir+"/"));
//
//            dir = dir.length() == 0 ? "" : dir +"/";
//
//            //循环将文件夹中的文件打包
//            for(int i = 0 ; i < files.length ; i++){
//                createCompressedFile(out, files[i], dir + files[i].getName());         //递归处理
//            }
//        }
//        else{   //当前的是文件，打包处理
//            //文件输入流
//            final FileInputStream fis = new FileInputStream(file);
//
//            out.putNextEntry(new ZipEntry(dir));
//            //进行写操作
//            int j =  0;
//            final byte[] buffer = new byte[1024];
//            while((j = fis.read(buffer)) > 0){
//                out.write(buffer,0,j);
//            }
//            //关闭输入流
//            fis.close();
//            System.out.println("压缩文件已经生成...");
//        }
//    }
//
//}