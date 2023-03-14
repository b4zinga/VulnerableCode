package com.example.vulnerablecode.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.vulnerablecode.entity.User;

@Mapper
public interface UserMapper {
    User findUserByName(String name);
    User findUserByName2(String name);
}
