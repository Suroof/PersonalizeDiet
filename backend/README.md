# PersonalizeDiet 后端服务

这是 PersonalizeDiet 项目的 Spring Boot 后端服务，负责封装 Dify API 调用，为前端提供统一的接口。

## 功能特性

- 🤖 **智能聊天**: 集成 Dify 聊天 API，支持对话管理
- 🥗 **营养分析**: 提供文本和文件的营养成分分析
- 📊 **历史记录**: 营养分析历史记录管理
- 🔒 **安全性**: API Key 在后端管理，前端无需暴露
- 🌐 **跨域支持**: 配置 CORS 支持前端调用

## 技术栈

- **Java 17**
- **Spring Boot 2.7+**
- **Spring Web**: RESTful API
- **Spring Data JPA**: 数据持久化
- **MySQL**: 数据库
- **Lombok**: 简化代码
- **RestTemplate**: HTTP 客户端

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6+
- MySQL 8.0+

### 1. 数据库配置

创建 MySQL 数据库：

```sql
CREATE DATABASE personalize_diet CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. API 密钥配置

复制环境变量配置文件：

```bash
cp .env.example .env
```

编辑 `.env` 文件，填入你的 Dify API 密钥：

```properties
# 聊天功能的Dify API Key
DIFY_CHAT_API_KEY=app-your-chat-api-key-here

# 营养分析功能的Dify API Key
DIFY_NUTRITION_API_KEY=app-your-nutrition-api-key-here

# AI聊天功能的Dify API Key
DIFY_AI_CHAT_API_KEY=app-your-ai-chat-api-key-here
```

### 3. 运行项目

```bash
# 安装依赖
mvn clean install

# 运行项目
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

## API 接口

### 聊天接口

#### 发送聊天消息
```http
POST /api/chat/send
Content-Type: application/json

{
  "message": "你好",
  "conversationId": "可选的对话ID",
  "user": "可选的用户ID"
}
```

#### AI 聊天
```http
POST /api/chat/ai-chat
Content-Type: application/json

{
  "message": "请帮我制定一个减肥计划"
}
```

#### 流式聊天
```http
POST /api/chat/stream
Content-Type: application/json

{
  "message": "告诉我一些健康饮食建议"
}
```

### 营养分析接口

#### 文本营养分析
```http
POST /api/nutrition/analyze-text
Content-Type: application/json

{
  "text": "一个苹果，100克米饭，一杯牛奶"
}
```

#### 文件营养分析
```http
POST /api/nutrition/analyze-file
Content-Type: multipart/form-data

file: [选择图片/文本/PDF文件]
```

#### 获取分析历史
```http
GET /api/nutrition/history
```

#### 清空分析历史
```http
DELETE /api/nutrition/history
```

## 项目结构

```
src/main/java/com/itheima/zhinengti/
├── ZhiNengTiApplication.java          # 启动类
├── config/                            # 配置类
│   ├── CorsConfig.java               # 跨域配置
│   ├── RestTemplateConfig.java       # HTTP客户端配置
│   └── GlobalExceptionHandler.java   # 全局异常处理
├── controller/                        # 控制器
│   ├── ChatController.java          # 聊天接口
│   └── NutritionController.java     # 营养分析接口
├── dto/                              # 数据传输对象
│   ├── ApiResponse.java             # 统一响应格式
│   ├── ChatRequest.java             # 聊天请求
│   ├── ChatResponse.java            # 聊天响应
│   ├── NutritionAnalysisRequest.java # 营养分析请求
│   └── NutritionAnalysisResponse.java # 营养分析响应
├── service/                          # 服务层
│   └── DifyService.java             # Dify API 服务
├── entity/                           # 实体类（预留）
├── repository/                       # 数据访问层（预留）
└── util/                            # 工具类（预留）
```

## 配置说明

### application.properties

主要配置项：

```properties
# 服务器端口
server.port=8080

# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/personalize_diet
spring.datasource.username=root
spring.datasource.password=your_password

# Dify API 配置
dify.api.url=https://api.dify.ai/v1
dify.api.key=${DIFY_CHAT_API_KEY:}

# 文件上传限制
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## 开发说明

### 添加新的 Dify 功能

1. 在 `DifyService` 中添加新的方法
2. 创建对应的 DTO 类
3. 在相应的 Controller 中添加接口
4. 更新配置文件（如需要）

### 数据库集成

项目已配置 JPA，可以：

1. 在 `entity` 包中创建实体类
2. 在 `repository` 包中创建数据访问接口
3. 在服务层中注入使用

## 常见问题

### Q: API 调用失败，提示 "API Key 未配置"

A: 请检查 `.env` 文件是否正确配置了 Dify API Key。

### Q: 跨域问题

A: 项目已配置 CORS，支持本地开发。如需修改允许的域名，请编辑 `CorsConfig.java`。

### Q: 文件上传失败

A: 检查文件大小是否超过 10MB，文件类型是否支持（图片、文本、PDF）。

### Q: 数据库连接失败

A: 请确认 MySQL 服务已启动，数据库已创建，用户名密码正确。

## 许可证

MIT License