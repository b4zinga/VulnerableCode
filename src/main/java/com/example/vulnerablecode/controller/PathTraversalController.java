package com.example.vulnerablecode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vulnerablecode.utils.FileUtil;

@RestController
@RequestMapping("path")
public class PathTraversalController {

    /**
     * 存在目录穿越漏洞，攻击者传入 ?file=../../../../../../etc/passwd 即可读取/etc/passwd文件
     * @param file
     * @return
     */
    @GetMapping("1")
    public String read(String file) {
        return FileUtil.readFile(file);
    }

    /**
     * 修复目录穿越漏洞，过滤.. / \ 等目录穿越字符
     * @param file
     * @return
     */
    @GetMapping("safe")
    public String safeRead(String file) {
        if (checkParameter(file)) {
            return FileUtil.readFile(file);
        } else {
            return "参数错误";
        }
    }

    public static boolean checkParameter(String filename){
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            return false;
        } else {
            return true;
        }
    }
}
