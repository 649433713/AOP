package com.nju.aop.controller;

import com.nju.aop.utils.ResultVOUtil;
import com.nju.aop.utils.excel.AopImportUtil;
import com.nju.aop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yinywf
 * Created on 2017/10/16
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AopImportUtil aopImportUtil;

    @GetMapping("/insert")
    public ResultVO insert(@RequestParam("name") String name) {
        String methodName = "insert" + name;
        try {
            Method method = aopImportUtil.getClass().getMethod(methodName);
            method.invoke(aopImportUtil);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return ResultVOUtil.success();
    }
}
