-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mall_db;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(500),
    gender ENUM('MALE', 'FEMALE', 'UNKNOWN') DEFAULT 'UNKNOWN',
    birthday DATE,
    member_level VARCHAR(20) DEFAULT '普通会员',
    growth_value INT DEFAULT 0,
    points INT DEFAULT 0,
    status ENUM('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE',
    last_login_time DATETIME,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    level INT,
    sort_order INT DEFAULT 0,
    icon VARCHAR(200),
    image VARCHAR(500),
    keywords VARCHAR(500),
    description TEXT,
    is_show BOOLEAN DEFAULT TRUE,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    INDEX idx_parent_id (parent_id),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(200) NOT NULL,
    subtitle VARCHAR(300),
    main_image VARCHAR(500),
    sub_images TEXT,
    detail LONGTEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    stock INT DEFAULT 0,
    sales INT DEFAULT 0,
    status ENUM('ON_SALE', 'OFF_SALE', 'SOLD_OUT') DEFAULT 'ON_SALE',
    category_id BIGINT,
    weight DECIMAL(10,2),
    unit VARCHAR(20),
    keywords VARCHAR(500),
    description TEXT,
    sort_order INT DEFAULT 0,
    is_hot BOOLEAN DEFAULT FALSE,
    is_new BOOLEAN DEFAULT FALSE,
    is_recommend BOOLEAN DEFAULT FALSE,
    video_url VARCHAR(500),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_hot (is_hot),
    INDEX idx_is_new (is_new),
    INDEX idx_is_recommend (is_recommend),
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入初始数据
INSERT INTO users (username, password, nickname, email, phone, member_level, growth_value, points, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIFi', '系统管理员', 'admin@mall.com', '13800138000', '钻石会员', 10000, 1000, 'ADMIN'),
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIFi', '测试用户', 'test@mall.com', '13800138001', '黄金会员', 5000, 500, 'USER');

INSERT INTO categories (name, parent_id, level, sort_order, icon, description, is_show, status) VALUES
('数码电子', NULL, 1, 1, 'icon-electronics', '手机、电脑、数码配件等', TRUE, 'ACTIVE'),
('服装鞋帽', NULL, 1, 2, 'icon-clothing', '男装、女装、鞋子、配饰等', TRUE, 'ACTIVE'),
('家居生活', NULL, 1, 3, 'icon-home', '家具、家电、日用品等', TRUE, 'ACTIVE');

INSERT INTO products (name, subtitle, main_image, sub_images, price, original_price, stock, category_id, keywords, description, is_hot, is_new, is_recommend) VALUES
('iPhone 15 Pro', '苹果最新旗舰手机', 'https://via.placeholder.com/400x400?text=iPhone+15+Pro', 'https://via.placeholder.com/400x400?text=iPhone+15+Pro+1,https://via.placeholder.com/400x400?text=iPhone+15+Pro+2', 8999.00, 10798.80, 100, 1, 'iPhone 15 Pro,数码电子', '苹果最新旗舰手机，A17 Pro芯片，钛金属设计', TRUE, TRUE, FALSE),
('MacBook Air M3', '轻薄便携笔记本电脑', 'https://via.placeholder.com/400x400?text=MacBook+Air+M3', 'https://via.placeholder.com/400x400?text=MacBook+Air+M3+1,https://via.placeholder.com/400x400?text=MacBook+Air+M3+2', 9499.00, 11398.80, 50, 1, 'MacBook Air M3,数码电子', '轻薄便携笔记本电脑，M3芯片强劲性能', FALSE, TRUE, TRUE),
('AirPods Pro 3', '主动降噪无线耳机', 'https://via.placeholder.com/400x400?text=AirPods+Pro+3', 'https://via.placeholder.com/400x400?text=AirPods+Pro+3+1,https://via.placeholder.com/400x400?text=AirPods+Pro+3+2', 1899.00, 2278.80, 200, 1, 'AirPods Pro 3,数码电子', '主动降噪无线耳机，空间音频体验', TRUE, FALSE, TRUE);