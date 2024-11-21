package com.zhgw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhgw.mapper")
public class MomApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomApiApplication.class, args);
    }

}
