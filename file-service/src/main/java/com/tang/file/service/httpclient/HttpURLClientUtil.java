package com.tang.file.service.httpclient;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * http客户端：Java的实现
 */
public class HttpURLClientUtil {

    /**
     * 附件上传：上传图片
     *
     * @param uploadUrl   上传URL
     * @param paramMap    请求参数
     * @param fileMap     文件
     * @param contentType 文件类型，没有传入文件类型默认采用application/octet-stream，
     *                    contentType非空采用filename匹配默认的图片类型
     * @return HTTP请求返回结果
     */
    @SuppressWarnings("rawtypes")
    public static String formUpload(String uploadUrl, Map<String, String> paramMap, Map<String, String> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "----------Ef1KM7cH2Ef1cH2Ij5ae0cH2ae0G13";
        try {
            URL url = new URL(uploadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            conn.setDoInput(true);
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // param
            if (paramMap != null) {
                StringBuilder sb = new StringBuilder();
                Iterator iter = paramMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"\r\n\r\n");
                    sb.append(inputValue);
                }
                System.out.println("请求的普通参数：" + sb.toString());
                out.write(sb.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();

                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    //contentType非空采用filename匹配默认的图片类型
                    if (!"".equals(contentType)) {
                        if (filename.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        } else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        } else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                    sb.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"; filename=\"").append(filename).append("\"\r\n");
//                    sb.append("Content-Type:").append(contentType).append("\r\n\r\n");
                    String sb = "\r\n" + "--" + BOUNDARY + "\r\n" +
                            "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n" +
                            "Content-Type:" + contentType + "\r\n\r\n";

                    System.out.println("文件上传：name=" + inputName + ";   filename=" + filename + "   ;contentType=" + contentType);

                    out.write(sb.getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));

                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuilder sbResponse = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sbResponse.append(line).append("\n");
            }
            res = sbResponse.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错，请求url为：" + uploadUrl);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }


    /**
     * 附件上传：上传图片
     *
     * @param uploadUrl 上传路径
     * @param paramMap  请求参数
     * @param fileMap   对应的文件流
     * @param filename  上传的文件名
     * @return HTTP请求返回的结果
     */
    public static String formUpload1(String uploadUrl, Map<String, String> paramMap, Map<String, byte[]> fileMap, String filename) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "----------Ef1KM7cH2Ef1cH2Ij5ae0cH2ae0G13";
        try {
            URL url = new URL(uploadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            conn.setDoInput(true);
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // param
            if (paramMap != null) {
                StringBuilder sb = new StringBuilder();
                Iterator iter = paramMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"\r\n\r\n");
                    sb.append(inputValue);
                }
                System.out.println("请求的普通参数：" + sb.toString());
                out.write(sb.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    byte[] inputValue = (byte[]) entry.getValue();

                    String contentType = "";
                    if (filename != null && !"".equals(filename) && filename.contains(".")) {
                        contentType = filename.split("\\.")[1];
                    }

                    String sb = "\r\n" + "--" + BOUNDARY + "\r\n" +
                            "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n" +
                            "Content-Type:" + contentType + "\r\n\r\n";

                    System.out.println("文件上传：name=" + inputName + ";   filename=" + filename + "   ;contentType=" + contentType);

                    out.write(sb.getBytes());

                    //对文件流进行循环处理写入
                    out.write(inputValue);
//                    DataInputStream in = new DataInputStream(new FileInputStream(file));
//                    int bytes;
//                    byte[] bufferOut = new byte[1024];
//                    while ((bytes = in.read(bufferOut)) != -1) {
//                        out.write(bufferOut, 0, bytes);
//                    }
//                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuilder sbResponse = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sbResponse.append(line).append("\n");
            }
            res = sbResponse.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错，请求url为：" + uploadUrl);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }


}
