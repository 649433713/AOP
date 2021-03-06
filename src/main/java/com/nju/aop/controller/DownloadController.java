package com.nju.aop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author yinywf
 * Created on 2020-06-06
 */

@RestController
@Slf4j
@RequestMapping("/api/download")
public class DownloadController {

    @GetMapping("/zip")
    public String downLoad(HttpServletResponse response){
        String filename="归档.zip";
        String filePath = "/usr/project/aop" ;
        downLoad(filename, filePath, response);
        return null;
    }

    @GetMapping("/instruction")
    public String downLoadInstruction(HttpServletResponse response){
        String filename = "AOP用户使用说明.pdf";
        String filePath = "/usr/project/aop" ;
        downLoad(filename, filePath, response);
        return null;
    }

    private void downLoad(String filename, String filePath, HttpServletResponse response) {
        File file = new File(filePath + "/" + filename);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/force-download");
            try {
                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
                bis.close();
                fis.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
        }
    }
}
