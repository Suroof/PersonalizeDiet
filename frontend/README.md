# 个性化食谱助手前端

## 环境配置

### 1. 安装依赖

```bash
npm install
```

### 2. 配置环境变量

复制 `.env.example` 文件为 `.env`：

```bash
cp .env.example .env
```

然后编辑 `.env` 文件，配置以下变量：

```env
# Dify API 配置
VITE_DIFY_API_URL=https://api.dify.ai/v1
VITE_DIFY_API_KEY=your_actual_dify_api_key

# 后端 API 配置
VITE_API_BASE_URL=http://localhost:8080/api
```

### 3. 获取 Dify API Key

1. 访问 [Dify 官网](https://dify.ai/)
2. 注册并登录账户
3. 创建一个新的应用
4. 在应用设置中找到 API Key
5. 复制 API Key 到 `.env` 文件中的 `VITE_DIFY_API_KEY`

### 4. API Key 格式

Dify API Key 通常格式为：`app-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx`

## 运行项目

### 开发模式

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## API 调用说明

### Dify API 认证

所有对 Dify API 的请求都需要在请求头中包含认证信息：

```javascript
headers: {
  'Authorization': `Bearer ${DIFY_API_KEY}`,
  'Content-Type': 'application/json'
}
```

### 聊天消息格式

发送给 Dify API 的消息格式：

```javascript
{
  inputs: {},
  query: "用户的问题",
  response_mode: "blocking",
  conversation_id: "",
  user: "user-timestamp"
}
```

## 常见问题

### 1. 401 Unauthorized 错误

- 检查 API Key 是否正确配置
- 确认 API Key 格式是否正确
- 验证 Dify 应用是否正常运行

### 2. CORS 错误

- 在 Dify 应用设置中配置允许的域名
- 确保前端域名在 Dify 的白名单中

### 3. 网络请求失败

- 检查网络连接
- 确认 Dify API 服务是否可用
- 检查防火墙设置

## 技术栈

- Vue 3
- Vite
- Element Plus
- Axios
- Pinia
- Vue Router