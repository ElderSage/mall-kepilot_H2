# 在线商城系统 (Mall System)

基于 Vue3 + Spring Boot 的现代化在线商城系统，采用前后端分离架构。

## 🚀 项目特性

### 技术栈
- **前端**: Vue 3 + Element Plus + Vite
- **后端**: Spring Boot 3.2.8 + Java 17
- **数据库**: H2 (开发环境) / MySQL (生产环境)
- **认证**: JWT Token
- **API文档**: Swagger/OpenAPI 3
- **构建工具**: Maven + Vite

### 核心功能

#### 用户端功能
- ✅ 用户注册登录 (手机号/邮箱)
- ✅ 商品浏览与搜索
- ✅ 购物车管理
- ✅ 订单管理
- ✅ 会员等级系统
- ✅ 优惠券系统
- ✅ 个人中心

#### 管理端功能
- ✅ 商品管理 (CRUD)
- ✅ 分类管理
- ✅ 订单处理
- ✅ 用户管理
- ✅ 营销活动管理
- ✅ 数据统计分析

## 🏗️ 项目结构

```
mall-kepilot_H2/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/mall/
│   │       ├── controller/  # 控制器层
│   │       ├── service/     # 服务层
│   │       ├── repository/  # 数据访问层
│   │       ├── entity/      # 实体类
│   │       ├── config/      # 配置类
│   │       └── security/    # 安全配置
│   └── pom.xml
├── frontend-admin/          # Vue3 管理端
│   ├── src/
│   │   ├── components/
│   │   ├── views/
│   │   ├── router/
│   │   └── store/
│   └── package.json
├── frontend-user/           # Vue3 用户端
│   └── ...
├── admin-demo.html         # 管理端演示页面
└── README.md
```

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- Maven 3.6+

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 前端启动

#### 管理端
```bash
cd frontend-admin
npm install
npm run dev
```

#### 用户端
```bash
cd frontend-user
npm install
npm run dev
```

### 演示页面

访问 `admin-demo.html` 可以查看完整的管理端演示界面。

## 📊 API 文档

启动后端服务后，访问 `http://localhost:8080/swagger-ui.html` 查看完整的API文档。

## 🔐 默认账号

### 管理员账号
- 用户名: `admin`
- 密码: `admin123`

### 测试用户
- 用户名: `testuser`
- 密码: `123456`

## 🗄️ 数据库

### 开发环境 (H2) - 默认配置
- 控制台: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:mall`
- 用户名: `sa`
- 密码: (空)

### 生产环境 (MySQL) - 推荐配置

#### 快速启动MySQL
```bash
# 使用Docker Compose一键启动MySQL
docker-compose up -d mysql

# 使用MySQL配置启动后端
mvn spring-boot:run -Dspring.profiles.active=prod
```

#### 手动配置MySQL
1. 安装MySQL 8.0
2. 执行初始化脚本: `backend/src/main/resources/sql/mysql-init.sql`
3. 修改配置: `backend/src/main/resources/application-prod.yml`

详细配置请参考: [MySQL配置指南](MYSQL_SETUP.md)

## 🎯 核心功能演示

### 1. 用户认证系统
- JWT Token 认证
- 角色权限控制
- 会话管理

### 2. 商品管理
- 商品CRUD操作
- 分类管理
- 库存管理
- 图片上传

### 3. 订单系统
- 订单创建与支付
- 订单状态跟踪
- 物流信息

### 4. 会员系统
- 会员等级 (普通/白银/黄金/钻石)
- 积分累计
- 成长值系统

## 🔧 配置说明

### 后端配置
主要配置文件位于 `backend/src/main/resources/`:
- `application.yml` - 主配置
- `application-dev.yml` - 开发环境
- `application-prod.yml` - 生产环境

### 前端配置
- `vite.config.ts` - Vite 构建配置
- `.env.development` - 开发环境变量
- `.env.production` - 生产环境变量

## 🚀 部署指南

### Docker 部署
```bash
# 构建镜像
docker build -t mall-backend ./backend
docker build -t mall-frontend ./frontend-admin

# 运行容器
docker run -p 8080:8080 mall-backend
docker run -p 3000:3000 mall-frontend
```

### 传统部署
1. 后端打包: `mvn clean package`
2. 前端构建: `npm run build`
3. 部署到服务器

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 更新日志

### v1.0.0 (2025-08-18)
- ✅ 完成基础架构搭建
- ✅ 实现用户认证系统
- ✅ 完成商品管理功能
- ✅ 实现订单管理系统
- ✅ 添加管理端界面
- ✅ 集成 Swagger API 文档

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- 项目地址: https://github.com/ElderSage/mall-kepilot_H2
- 问题反馈: https://github.com/ElderSage/mall-kepilot_H2/issues

---

⭐ 如果这个项目对你有帮助，请给个 Star！