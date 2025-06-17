# 个性化食谱助手 (PersonalizeDiet)

一个集成Dify智能体API的个性化食谱推荐web应用，提供自然语言交互和定制化菜谱服务。

## 项目架构

```
PersonalizeDiet/
├── frontend/          # Vue 3 前端应用
│   ├── src/
│   │   ├── components/    # 组件
│   │   ├── views/        # 页面
│   │   ├── store/        # Pinia状态管理
│   │   ├── api/          # API接口
│   │   └── utils/        # 工具函数
│   ├── public/
│   └── package.json
├── backend/           # Spring Boot 后端服务
│   ├── src/main/java/
│   │   └── com/personalize/diet/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── entity/
│   │       └── config/
│   ├── src/main/resources/
│   └── pom.xml
└── database/          # 数据库脚本
    └── init.sql
```

## 核心功能

### 用户交互界面
- 悬浮式聊天入口（可拖动吸壁球）
- 智能对话窗口（支持流式消息渲染）

### 个性化食谱服务
- 基于用户输入生成菜谱推荐
- 菜谱详情展示（图片、步骤、营养信息）

### 系统集成
- 对接Dify智能体API实现自然语言处理
- 支持流式响应与错误处理

## 技术栈

### 前端
- Vue 3 (Composition API)
- Vue Router
- Pinia 状态管理
- EventSource 流式API

### 后端
- Spring Boot
- MySQL 数据库
- JPA/Hibernate

## 快速开始

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

### 后端启动
```bash
cd backend
mvn spring-boot:run
```

### 数据库初始化
```bash
mysql -u root -p < database/init.sql
```

## API文档

### Dify集成
- 流式聊天接口
- 菜谱推荐接口
- 用户偏好管理

## 部署说明

详细部署文档请参考各模块的README文件。