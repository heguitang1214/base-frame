package com.tang.file.service.httpclient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDownLoadByte {

    public static void main(String[] args) throws IOException {
        String downloadUrl = "";// TODO: 2019/5/21 下载图片的地址
        byte[] bytes = getDownLoadByte(downloadUrl);
        //将文件流写入到文件中
        writeBytesToFile(bytes, "D://333.png");
        System.out.println("完成！");
    }

    public static byte[] getDownLoadByte(String downloadUrl) throws IOException {

        URL httpurl = new URL(downloadUrl);
        HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
        // 使用 URL 连接进行输出
        httpConn.setDoOutput(true);
        // 使用 URL 连接进行输入
        httpConn.setDoInput(true);
        // 忽略缓存
        httpConn.setUseCaches(false);
        // 设置URL请求方法
        httpConn.setRequestMethod("GET");
        //可设置请求头
        httpConn.setRequestProperty("Content-Type", "application/octet-stream");
        // 维持长连接
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Charset", "UTF-8");
        // 文件大小
        int fileLength = httpConn.getContentLength();
        System.out.println("文件大小：" + fileLength);
        //获取下载的文件流
        byte[] file = input2byte(httpConn.getInputStream());
        System.out.println("文件流：" + file);
        return file;
    }

    private static byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    private static File writeBytesToFile(byte[] b, String outputFile) {
        File file = null;
        FileOutputStream os = null;

        try {
            file = new File(outputFile);
            os = new FileOutputStream(file);
            os.write(b);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }
        }
        return file;
    }




}