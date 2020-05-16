package com.tang.file.service.httpclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class HttpURLClientUtil1 {
    public static String sendPost(String path, Map<String, Object> params) throws IOException {
        return httpReqInner(path, params, "POST", null);
    }

    public static String sendPost(String path, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return httpReqInner(path, params, "POST", headers);
    }

    public static String sendPost(String path, Object params, Map<String, String> headers) throws IOException {
        return httpReqInner(path, params, "POST", headers);
    }


    public static String sendGet(String path) throws IOException {
        return httpReqInner(path, null, "GET", null);
    }

    public static <T> T sendGet(String path, Map<String, Object> params, TypeReference valueTypeRef) throws IOException {
        return httpReq(path, params, "GET", null, valueTypeRef);
    }

    public static String sendGet(String path, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return httpReqInner(path, params, "GET", headers);
    }


    private static <T> T httpReq(String path, Map<String, Object> params, String method, Map<String, String> headers, TypeReference valueTypeRef) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsondata = httpReqInner(path, params, method, headers);
        Object rootNode = mapper.readTree(jsondata);
        jsondata = mapper.writeValueAsString(rootNode);
        // TODO: 2020/4/1
//        return mapper.readValue(jsondata, valueTypeRef);
        return (T) mapper.readValue(jsondata, valueTypeRef);
    }

    private static String httpReqInner(String path, Map<String, Object> params, String method, Map<String, String> headers) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        String result = "";
        BufferedReader reader = null;
        ObjectMapper mapper = new ObjectMapper();

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        for (String key : headers.keySet()) {
            AddHttpHeader(conn, key, headers.get(key));
        }
        if (!headers.containsKey("Connection")) {
            AddHttpHeader(conn, "Connection", "Keep-Alive");
        }
        if (!headers.containsKey("Connection")) {
            AddHttpHeader(conn, "Charset", "UTF-8");
        }
        if (!headers.containsKey("Content-Type")) {
            AddHttpHeader(conn, "Content-Type", "application/json; charset=UTF-8");
        }
        if (!headers.containsKey("accept")) {
            AddHttpHeader(conn, "accept", "application/json");
        }
        String contenttype = conn.getRequestProperty("Content-Type");
        String reqData = "";
        if (contenttype.startsWith("application/json")) {
            reqData = mapper.writeValueAsString(params);
        } else {
            for (String name : params.keySet()) {
                if (reqData.equals("")) {
                    reqData = name + "=" + params.get(name).toString();
                } else {
                    reqData = "&" + name + "=" + params.get(name).toString();
                }
            }
        }
        if (reqData != null && !Objects.equals(reqData, "") && Objects.equals(method, "POST")) {
            byte[] writebytes = reqData.getBytes();
            AddHttpHeader(conn, "Content-Length", String.valueOf(writebytes.length));
            OutputStream outwritestream = conn.getOutputStream();
            outwritestream.write(reqData.getBytes(Charset.forName("UTF-8")));
            outwritestream.flush();
            outwritestream.close();
        }
        int httpcode = conn.getResponseCode();


        if (httpcode == 200) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String s;
            StringBuffer sb = new StringBuffer();
            while ((s = reader.readLine()) != null) {
                sb.append(s).append("\n");
            }
            result = sb.toString();
        } else {
            return String.valueOf(httpcode);
        }
        Dispose(reader);
        return result;
    }

    private static void AddHttpHeader(HttpURLConnection con, String key, String Val) {
        con.setRequestProperty(key, Val);
    }

    private static void Dispose(Closeable resource) {
        try {
            resource.close();
        } catch (Exception e) {
        }
    }

    private static String httpReqInner(String path, Object params, String method, Map<String, String> headers) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        String result;
        BufferedReader reader;

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        for (String key : headers.keySet()) {
            AddHttpHeader(conn, key, headers.get(key));
        }
        if (!headers.containsKey("Connection")) {
            AddHttpHeader(conn, "Connection", "Keep-Alive");
        }
        if (!headers.containsKey("Connection")) {
            AddHttpHeader(conn, "Charset", "UTF-8");
        }
        if (!headers.containsKey("Content-Type")) {
            AddHttpHeader(conn, "Content-Type", "application/json; charset=UTF-8");
        }
        if (!headers.containsKey("accept")) {
            AddHttpHeader(conn, "accept", "application/json");
        }

        if (params != null && !Objects.equals(params, "") && Objects.equals(method, "POST")) {
            String reqData = params.toString();
            byte[] writebytes = reqData.getBytes();
            AddHttpHeader(conn, "Content-Length", String.valueOf(writebytes.length));
            OutputStream outwritestream = conn.getOutputStream();
            outwritestream.write(reqData.getBytes(Charset.forName("UTF-8")));
            outwritestream.flush();
            outwritestream.close();
        }
        int httpcode = conn.getResponseCode();

        if (httpcode == 200) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s).append("\n");
            }
            result = sb.toString();
        } else {
            return String.valueOf(httpcode);
        }
        Dispose(reader);
        return result;
    }

    public static byte[] getImageByte(String downloadUrl) throws IOException {
        //下载得到的文件流
        URL httpurl = new URL(downloadUrl);
        HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        // 忽略缓存
        httpConn.setUseCaches(false);
        //设置URL请求方法
        httpConn.setRequestMethod("GET");
        //可设置请求头
        httpConn.setRequestProperty("Content-Type", "application/octet-stream");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Charset", "UTF-8");

        InputStream inputStream = httpConn.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            outputStream.write(buff, 0, rc);
        }
        return outputStream.toByteArray();
    }

    /**
     * 上传图片 : 流数据的处理
     */
    public static String imageUpload(String urlStr, Map<String, String> paramMap, Map<String, byte[]> fileMap, String filename) throws IOException {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "----------Ef1KM7cH2Ef1cH2Ij5ae0cH2ae0G13";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("xxxx", "YYYYY");
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

                    out.write(sb.getBytes());

                    //对文件流进行循环处理写入?? todo 测试大图片的上传
                    out.write(inputValue);
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
//        } catch (Exception e) {
//            System.out.println("发送POST请求出错，请求url为：" + urlStr);
//            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

}
