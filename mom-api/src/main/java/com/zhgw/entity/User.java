package com.zhgw.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表 sys_user
 * 
 * @author zhgw
 * @since 2024-03-xx
 */
@Data
@Entity
@Table(name = "sys_user")
public class User {
    /** 用户ID，主键，自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户名，唯一约束 */
    @Column(unique = true, nullable = false)
    private String username;
    
    /** 密码，使用BCrypt加密存储 */
    @Column(nullable = false)
    private String password;
    
    /** 用户真实姓名 */
    private String realName;
    
    /** 用户邮箱 */
    private String email;
    
    /** 用户手机号 */
    private String phone;
    
    /** 记录创建时间 */
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    /** 记录更新时间 */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 