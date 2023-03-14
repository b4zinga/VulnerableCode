package com.example.vulnerablecode.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redirect")
public class OpenRedirectController {

    /**
     * 存在重定向漏洞，攻击者诱骗用户点击如下链接后，用户浏览器就会被重定向到 www.evil.com 钓鱼网站
     * https://www.example.com/redirect/1?url=https://www.evil.com
     * @param url
     * @param response
     * @throws IOException
     */
    @GetMapping("1")
    public void redirect(String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(url);
    }

    /**
     * 修复重定向漏洞，通过host白名单进行限制
     * @param url
     * @param response
     * @throws IOException
     */
    @GetMapping("safe")
    public void safeRedirect(String url, HttpServletResponse response) throws IOException {
        if (checkParameter(url)) {
            response.sendRedirect(url);
        }
    }

    public static boolean checkParameter(String url) {
        String[] whiteHostList = {".example.com", ".example2.com"}; // host白名单, 以.开头
        String host = "";
        try {
            url = url.replaceAll("[\\\\#]","/");
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            // log.info(e);
            e.printStackTrace();
        }
        for (String white : whiteHostList) {
            if (host.endsWith(white)) {
                return true;
            }
        }
        return false;
    }
}
