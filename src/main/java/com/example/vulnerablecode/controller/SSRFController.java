package com.example.vulnerablecode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vulnerablecode.utils.HttpUtil;

@RestController
@RequestMapping("ssrf")
public class SSRFController {

    /**
     * 存在SSRF漏洞，攻击者传入 ?url=http://10.10.10.1/admin 即可访问内网
     * @param url
     * @return
     */
    @GetMapping("1")
    public String download(String url) {
        return HttpUtil.doGet(url);
    }

    /**
     * 修复SSRF漏洞，通过URL白名单进行限制
     * @param url
     * @return
     */
    @GetMapping("safe")
    public String safeDownload(String url) {
        if (checkParameter(url)) {
            return HttpUtil.doGet(url);
        } else {
            return "参数不合法";
        }
    }

    public static boolean checkParameter(String url) {
        String[] urlWhiteList = {"https://img.example.com/", "https://cdn.example.com/"}; // url白名单，以 / 结尾
        for (String whiteUrl : urlWhiteList) {
            if (url.startsWith(whiteUrl)) {
                return true;
            }
        }
        return false;
    }
}
