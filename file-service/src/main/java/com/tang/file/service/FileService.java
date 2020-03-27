package com.tang.file.service;


import com.wangchi.uploaddemo.service.httpclient.HttpURLClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {
    private Logger logger = LoggerFactory.getLogger(FileService.class);


    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newUploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传的文件名是: {}，上传的路径是: {}，新文件名：{}", fileName, path, newUploadFileName);

//        String fileName2 = file2.getOriginalFilename();
//        String fileExtensionName2 = fileName2.substring(fileName2.lastIndexOf(".") + 1);
//        String uploadFileName2 = UUID.randomUUID().toString() + "." + fileExtensionName2;
//        logger.info("开始上传文件，上传的文件名是: {}，上传的路径是: {}，新文件名：{}", fileName2, path, uploadFileName2);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, newUploadFileName);
//        File targetFile2 = new File(path, uploadFileName2);
        try {
            file.transferTo(targetFile);
//            file2.transferTo(targetFile2);
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
//        return targetFile1.getName()+targetFile2.getName();
        return targetFile.getName();
    }


    /**
     * 图片处理
     *
     * @param fileName    上传名
     * @param uploadUrl   上传URL
     * @param downloadUrl 图片下载url
     * @return 返回结果
     */
    public String upload1(String fileName, String uploadUrl, String downloadUrl, Map<String, String> paramMap) {
        Map<String, byte[]> fileMap = new Hashtable<>();
        try {
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
            byte[] file = this.input2byte(httpConn.getInputStream());
            fileMap.put("file", file);
        } catch (Exception e) {
            logger.error("图片下载出现异常，请求URL为：{}，异常信息为：{}", downloadUrl, e);
            throw new RuntimeException("图片下载出现异常！");
        }
        try {
            return HttpURLClientUtil.formUpload1(uploadUrl, paramMap, fileMap, fileName);
        } catch (Exception e) {
            logger.error("图片上传出现异常，上传URL为：{}，异常信息为：{}", uploadUrl, e);
            throw new RuntimeException("图片上传出现异常！");
        }
    }

    private byte[] input2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            outputStream.write(buff, 0, rc);
        }
        return outputStream.toByteArray();
    }


    private static byte[] InputStream2ByteArray(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }

    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }


}
