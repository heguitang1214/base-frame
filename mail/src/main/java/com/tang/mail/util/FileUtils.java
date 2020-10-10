package com.tang.mail.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class FileUtils {

    public static final String DEFAULT_PATH = "test/";

    public static String generatePath(String fileName,String basePath){
        String suffix = fileName.substring(fileName.indexOf("."));
        String path = basePath + System.nanoTime() + UUID.randomUUID() + suffix;
        return path.replace("-","");
    }

    public static String generatePath(String fileName){
        return generatePath(fileName,DEFAULT_PATH);
    }

    /**
     *@描述  ：用于券码导出并压缩加密
     *@创建人：lee
     *@创建时间：2017年7月10日 下午1:38:59
     *@修改人：
     *@修改时间：
     *@修改描述：
     *@param srcCode
     *@param filePath
     *@param password
     *@param file
     */
    public static void EncryptAndZip(StringBuffer srcCode,String filePath,String password,File file) {
        String name="cardList"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        try {
            // 根据filePath创建相应的目录   
            if(!file.getParentFile().exists()){     
                   file.getParentFile().mkdirs();     
               }

            file.createNewFile();

            // 向文件写入内容(输出流)  
            byte bt[] = new byte[1024];  
            bt = srcCode.toString().getBytes();  
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(bt, 0, bt.length);  
            fos.close(); 

           ZipFile zipFile = new ZipFile(filePath+"/"+name+".zip"); // 创建zip包，指定了zip路径和zip名称  
           ArrayList<File> fileAddZip = new ArrayList<File>(); // 向zip包中添加文件集合  
           fileAddZip.add(file); // 向zip包中添加一个word文件  
           ZipParameters parameters = new ZipParameters(); // 设置zip包的一些参数集合  
           parameters.setEncryptFiles(true); // 是否设置密码（此处设置为：是）  
           parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式(默认值)  
           parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 普通级别（参数很多）  
           parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密级别  

           parameters.setPassword(password); // 压缩包密码为123456  
           zipFile.createZipFile(fileAddZip, parameters); // 创建压缩包完成 
           file.delete();

        } catch (IOException | ZipException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
