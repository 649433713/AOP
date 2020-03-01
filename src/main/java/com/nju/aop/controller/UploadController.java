package com.nju.aop.controller;

import com.nju.aop.utils.ResultVOUtil;
import com.nju.aop.utils.excel.AopImportUtil;
import com.nju.aop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author yinywf
 * Created on 2020-03-01
 */
@RestController
@Slf4j
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private AopImportUtil aopImportUtil;

    @RequestMapping(value = "/aop",method = RequestMethod.POST)
    @ResponseBody
    public ResultVO uploadAop(@RequestParam(value = "file") MultipartFile excel){

        if (excel.isEmpty()) {
            return ResultVOUtil.error(-1, "data error");
        }else {
            try {
                String path = saveFile(excel);
                if (StringUtils.isEmpty(path)) {
                    return ResultVOUtil.error(-1, "文件存储失败");
                }
                new Thread(() -> {
                    try {
                        aopImportUtil.insertAopExcel(new FileInputStream(path), path.substring(path.indexOf(".xls")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return ResultVOUtil.error(-1, "文件格式不正确");
            }
        }
        return ResultVOUtil.success();
    }


    @RequestMapping(value = "/tox",method = RequestMethod.POST)
    @ResponseBody
    public ResultVO uploadTox(@RequestParam(value = "file") MultipartFile excel){

        if (excel.isEmpty()) {
            return ResultVOUtil.error(-1, "data error");
        }else {
            try {
                String path = saveFile(excel);
                if (StringUtils.isEmpty(path)) {
                    return ResultVOUtil.error(-1, "文件存储失败");
                }
                new Thread(() -> {
                    try {
                        aopImportUtil.insertToxExcel(new FileInputStream(path), path.substring(path.indexOf(".xls")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return ResultVOUtil.error(-1, "文件格式不正确");
            }
        }
        return ResultVOUtil.success();
    }


    private String saveFile(MultipartFile excel) {

        //保存时的文件名
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime()) + excel.getOriginalFilename();

        String filePath = File.separator + "tmp" + File.separator + "resource" + File.separator + "static" + File.separator + "aop" + File.separator + dateName;
        File newFile = new File(filePath);
        System.out.println("filePath=:" + filePath);
        //MultipartFile的方法直接写文件
        try {
            //上传文件
            if (!newFile.exists() && !newFile.createNewFile()) {
                throw new IOException();
            }
            excel.transferTo(newFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePath;
    }
}
