package com.example.vulnerablecode.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandUtil {
    public static String execute(String command) {
        StringBuilder sb = new StringBuilder();
        try{
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
            BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = bf.readLine()) != null){
                sb.append(line + "\n");
            }
        }catch (Exception e){
            return e.toString();
        }
        return sb.toString();
    }
}
