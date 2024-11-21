package com.zhgw;

import com.zhgw.entity.User;
import com.zhgw.repository.UserRepository;
import com.zhgw.util.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class MomApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomApiApplication.class, args);
    }

    /**
     * 初始化管理员账号
     */
    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 检查admin用户是否存在
            if (!userRepository.findByUsername("admin").isPresent()) {
                // 创建admin用户
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin")); // 使用BCrypt加密密码
                adminUser.setRealName("系统管理员");
                adminUser.setEmail("admin@example.com");
                adminUser.setPhone("13800138000");
                adminUser.setCreateTime(LocalDateTime.now());
                adminUser.setUpdateTime(LocalDateTime.now());
                
                // 保存用户
                userRepository.save(adminUser);
                
                System.out.println("管理员账号初始化成功");
            }
        };
    }
}
