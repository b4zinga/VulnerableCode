package com.example.vulnerablecode.controller;

import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vulnerablecode.utils.CommandUtil;

@RestController
@RequestMapping("cmd")
public class CommandInjectionController {

    /**
     * 存在命令注入漏洞，攻击者传入?bin=nginx;useradd tom 即可创建后门账户
     * @param bin
     * @return
     */
    @GetMapping("1")
    public String restart(String bin) {
        String action = "systemctl restart " + bin;
        return CommandUtil.execute(action);
    }

    /**
     * 修复命令注入漏洞，通过正则校验限制参数拼接
     * @param bin
     * @return
     */
    @GetMapping("safe")
    public String safeRestart(String bin) {
        if (checkParameter(bin)) {
            String action = "systemctl restart " + bin;
            return CommandUtil.execute(action);
        } else {
            return "参数不合法";
        }
    }

    public static boolean checkParameter(String command){
        return Pattern.compile("[0-9A-Za-z_]+").matcher(command).matches();
    }
}
