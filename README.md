# MOM制造运营管理系统

## 项目概述
MOM（Manufacturing Operations Management）制造运营管理系统是一个标准的企业级管理系统，采用前后端分离架构。

### 技术栈
#### 后端技术栈
- Spring Boot 2.7.5
- Spring Data JPA
- MySQL 8.0
- JWT (JSON Web Token)
- BCrypt密码加密

#### 前端技术栈
- Vue 3
- TypeScript
- Axios
- Vue Router 4

## 当前功能说明

### 1. 用户认证模块
#### 1.1 登录功能
- 支持用户名密码登录
- 使用BCrypt加密存储密码
- JWT token认证
- 登录状态持久化
- Token过期处理

#### 1.2 安全特性
- 密码加密存储
- Token基于JWT实现
- 请求拦截和认证
- 登录状态校验

### 2. 系统功能
#### 2.1 默认管理员
- 系统启动时自动创建admin用户
- 默认密码加密存储
- 基本用户信息初始化

#### 2.2 接口安全
- 统一的请求拦截
- Token验证机制
- 跨域请求处理
- 统一的响应格式

## 项目结构

### 后端结构 
mom-api/
├── src/main/java/com/zhgw/
│ ├── config/ # 配置类
│ │ ├── WebMvcConfig.java # Web配置
│ │ └── JwtAuthenticationInterceptor.java # JWT认证拦截器
│ ├── controller/ # 控制器
│ │ └── AuthController.java
│ ├── service/ # 服务层
│ │ └── impl/
│ ├── repository/ # 数据访问层
│ ├── entity/ # 实体类
│ ├── dto/ # 数据传输对象
│ ├── common/ # 公共组件
│ └── util/ # 工具类

### 前端结构
mom-web/
├── src/
│ ├── api/ # API请求
│ ├── components/ # 组件
│ ├── router/ # 路由配置
│ ├── types/ # TypeScript类型
│ ├── utils/ # 工具函数
│ └── views/ # 页面

## 后续开发计划

### 1. 安全性增强
- [ ] 添加登录失败次数限制
- [ ] 实现密码强度校验
- [ ] 添加验证码功能
- [ ] 完善JWT Token刷新机制

### 2. 用户管理模块
- [ ] 用户注册功能
- [ ] 密码修改功能
- [ ] 用户信息管理
- [ ] 用户角色管理

### 3. 权限管理模块
- [ ] 角色管理
- [ ] 权限配置
- [ ] 菜单权限
- [ ] 数据权限

### 4. 系统功能
- [ ] 系统日志记录
- [ ] 操作审计
- [ ] 数据字典
- [ ] 系统参数配置

### 5. 用户体验优化
- [ ] 登录失败提示
- [ ] "记住我"功能
- [ ] 界面主题定制
- [ ] 响应式布局适配

## 开发环境搭建

### 后端环境要求
- JDK 18
- Maven 3.8+
- MySQL 8.0+
- IDE: IDEA/Eclipse

### 前端环境要求
- Node.js 14+
- npm 6+
- IDE: VSCode/WebStorm

### 数据库配置
1. 创建数据库：
sql
CREATE DATABASE sim_mom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

2. 修改application.yml中的数据库配置：
yaml
spring:
datasource:
url: jdbc:mysql://localhost:3306/sim_mom
username: your_username
password: your_password

## 运行说明

### 后端运行
bash
cd mom-api
mvn spring-boot:run

### 前端运行
bash
cd mom-web
npm install
npm run serve


## 测试账号
- 用户名：admin
- 密码：admin

## 注意事项
1. 确保MySQL服务已启动
2. 检查数据库连接配置
3. 确保后端服务启动后再启动前端服务
4. 注意跨域配置与前端项目地址对应