package com.example.vulnerablecode.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vulnerablecode.mapper.UserMapper;

@RestController
@RequestMapping("sql")
public class SqliController {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    /**
     * 存在SQL注入漏洞，字符串直接拼接导致
     * 当攻击者传入 ?name=a'+union+select+*+from+users%3b时，即可查询所有数据
     * @param name
     * @return
     */
    @GetMapping("1")
    public String getUser(String name) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM users WHERE username='" + name + "'";
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String info = String.format("ID: %s, UserName: %s, Password: %s\n",
                                            rs.getInt("id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                sb.append(info);
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 修复SQL注入漏洞，使用参数化查询
     * @param name
     * @return
     */
    @GetMapping("safe")
    public String safeGetUser(String name) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM users WHERE username=?";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, name);
            ResultSet rs = pStatement.executeQuery();
            while (rs.next()) {
                String info = String.format("ID: %s, UserName: %s, Password: %s\n",
                                            rs.getInt("id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                sb.append(info);
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    @Autowired
    private UserMapper userMapper;
    /**
     * 存在MyBatis SQL注入漏洞，MyBatis使用${}传参导致
     * 当攻击者传入 ?name=a'+union+select+*+from+users%3b时，即可查询所有数据
     * @param name
     * @return
     */
    @GetMapping("2")
    public String getUser2(String name){
        return userMapper.findUserByName(name).toString();
    }

    /**
     * 修复MyBatis SQL注入漏洞，使用#{}代替${}进行传参
     */
    @GetMapping("safe2")
    public String safeGetUser2(String name){
        return userMapper.findUserByName2(name).toString();
    }

}
