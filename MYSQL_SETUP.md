# 🗄️ MySQL 数据库配置指南

## 为什么选择MySQL？

虽然项目默认使用H2数据库便于快速开发和演示，但在生产环境中，MySQL是更好的选择：

### MySQL vs H2 对比

| 特性 | H2 | MySQL |
|------|----|----|
| **部署复杂度** | ✅ 简单（内存数据库） | ⚠️ 需要安装配置 |
| **性能** | ⚠️ 适合开发测试 | ✅ 生产级性能 |
| **数据持久化** | ⚠️ 重启丢失数据 | ✅ 持久化存储 |
| **并发支持** | ⚠️ 有限 | ✅ 高并发支持 |
| **扩展性** | ❌ 不支持集群 | ✅ 支持主从、集群 |
| **生产环境** | ❌ 不推荐 | ✅ 企业级数据库 |

## 🚀 快速切换到MySQL

### 方法一：使用Docker Compose（推荐）

```bash
# 1. 启动MySQL和Redis服务
docker-compose up -d mysql redis

# 2. 等待MySQL启动完成（约30秒）
docker-compose logs -f mysql

# 3. 使用生产环境配置启动后端
cd backend
mvn spring-boot:run -Dspring.profiles.active=prod
```

### 方法二：本地安装MySQL

#### 1. 安装MySQL 8.0

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server-8.0
sudo mysql_secure_installation
```

**CentOS/RHEL:**
```bash
sudo yum install mysql-server
sudo systemctl start mysqld
sudo mysql_secure_installation
```

**macOS:**
```bash
brew install mysql
brew services start mysql
```

**Windows:**
下载并安装 MySQL Installer from https://dev.mysql.com/downloads/installer/

#### 2. 创建数据库和用户

```sql
-- 登录MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE mall_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选）
CREATE USER 'mall_user'@'localhost' IDENTIFIED BY 'mall_password';
GRANT ALL PRIVILEGES ON mall_db.* TO 'mall_user'@'localhost';
FLUSH PRIVILEGES;

-- 导入初始数据
USE mall_db;
SOURCE /path/to/backend/src/main/resources/sql/mysql-init.sql;
```

#### 3. 修改配置文件

编辑 `backend/src/main/resources/application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root  # 或 mall_user
    password: 123456  # 或 mall_password
```

#### 4. 启动应用

```bash
cd backend
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 🔧 配置详解

### 数据库连接配置

```yaml
spring:
  datasource:
    # 数据库连接URL
    url: jdbc:mysql://localhost:3306/mall_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    
    # 数据库用户名和密码
    username: root
    password: 123456
    
    # MySQL驱动
    driver-class-name: com.mysql.cj.jdbc.Driver
    
    # 连接池配置（HikariCP）
    hikari:
      minimum-idle: 5          # 最小空闲连接数
      maximum-pool-size: 20    # 最大连接池大小
      auto-commit: true        # 自动提交
      idle-timeout: 30000      # 空闲超时时间（毫秒）
      max-lifetime: 1800000    # 连接最大生存时间（毫秒）
      connection-timeout: 30000 # 连接超时时间（毫秒）

  # JPA/Hibernate 配置
  jpa:
    database: mysql
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update  # 生产环境建议使用 validate
    show-sql: false     # 生产环境关闭SQL日志
```

### 环境切换

#### 开发环境（H2）
```bash
mvn spring-boot:run
# 或
mvn spring-boot:run -Dspring.profiles.active=dev
```

#### 生产环境（MySQL）
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 📊 数据库表结构

项目包含以下主要表：

- **users** - 用户表
- **categories** - 商品分类表
- **products** - 商品表
- **product_skus** - 商品SKU表
- **product_attributes** - 商品属性表
- **cart_items** - 购物车表
- **orders** - 订单表
- **order_items** - 订单项表
- **coupons** - 优惠券表

所有表都包含：
- 主键ID（自增）
- 创建时间和更新时间
- 软删除标记
- 适当的索引优化

## 🔍 数据库管理工具

### 1. MySQL Workbench（官方GUI工具）
- 下载：https://dev.mysql.com/downloads/workbench/
- 功能：数据库设计、查询、管理

### 2. phpMyAdmin（Web界面）
```bash
# Docker方式运行
docker run --name phpmyadmin -d --link mall-mysql:db -p 8081:80 phpmyadmin/phpmyadmin
```

### 3. DBeaver（通用数据库工具）
- 下载：https://dbeaver.io/download/
- 支持多种数据库

### 4. 命令行工具
```bash
# 连接数据库
mysql -h localhost -u root -p mall_db

# 查看表结构
DESCRIBE users;

# 查看数据
SELECT * FROM products LIMIT 10;
```

## 🚀 性能优化建议

### 1. 索引优化
```sql
-- 为常用查询字段添加索引
CREATE INDEX idx_products_category_status ON products(category_id, status);
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_orders_create_time ON orders(create_time);
```

### 2. 连接池配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20    # 根据并发量调整
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

### 3. JPA优化
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20       # 批量操作大小
        order_inserts: true    # 优化插入顺序
        order_updates: true    # 优化更新顺序
```

## 🔒 安全配置

### 1. 数据库用户权限
```sql
-- 创建专用用户，避免使用root
CREATE USER 'mall_app'@'%' IDENTIFIED BY 'strong_password_here';
GRANT SELECT, INSERT, UPDATE, DELETE ON mall_db.* TO 'mall_app'@'%';
```

### 2. SSL连接（生产环境）
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_db?useSSL=true&requireSSL=true
```

### 3. 连接加密
```yaml
spring:
  datasource:
    password: ${DB_PASSWORD:default_password}  # 使用环境变量
```

## 📈 监控和维护

### 1. 慢查询日志
```sql
-- 启用慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
```

### 2. 性能监控
```sql
-- 查看连接状态
SHOW PROCESSLIST;

-- 查看表状态
SHOW TABLE STATUS;

-- 查看索引使用情况
SHOW INDEX FROM products;
```

### 3. 备份策略
```bash
# 数据备份
mysqldump -u root -p mall_db > mall_db_backup_$(date +%Y%m%d).sql

# 数据恢复
mysql -u root -p mall_db < mall_db_backup_20250818.sql
```

---

## 🎯 总结

- **开发阶段**：使用H2快速开发和测试
- **生产环境**：切换到MySQL获得更好的性能和可靠性
- **部署方式**：推荐使用Docker Compose一键部署
- **配置管理**：通过Spring Profile管理不同环境配置

通过以上配置，您的在线商城系统将具备生产级别的数据库支持！