package com.example.vulnerablecode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("xss")
public class XSSController {

    /**
     * 存在XSS漏洞，攻击者诱骗用户点击如下链接，即可导致用户被攻击
     * https://www.example.com/xss/1?name=1"><script>alert(document.cookie)</script>
     * @param name
     * @return
     */
    @GetMapping("1")
    public String echo(String name) {
        return "Hello " + name;
    }

    /**
     * 修复XSS漏洞，使用htmlEscape进行实体编码再输出
     * @param name
     * @return
     */
    @GetMapping("safe")
    public String safeEcho(String name) {
        name = HtmlUtils.htmlEscape(name); // 使用htmlEscape进行实体编码
        return "Hello" + name;
    }
}
