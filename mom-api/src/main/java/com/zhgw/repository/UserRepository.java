package com.zhgw.repository;

import com.zhgw.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 用户数据访问层接口
 * 继承JpaRepository以获取基本的CRUD操作方法
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 返回Optional包装的用户对象
     */
    Optional<User> findByUsername(String username);
} 