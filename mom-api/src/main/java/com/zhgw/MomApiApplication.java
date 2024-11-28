package com.zhgw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@MapperScan("com.zhgw.mapper")
public class MomApiApplication {

    public static void main(String[] args) {
        // 设置默认编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        SpringApplication.run(MomApiApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.setUriEncoding(StandardCharsets.UTF_8);
        return tomcat;
    }

}
