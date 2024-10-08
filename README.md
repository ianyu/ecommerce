
# E-Commerce Platform

## 專案簡介

這是一個基於 Spring Boot、Thymeleaf、PostgreSQL 和 RabbitMQ 的線上交易平台，專案模擬了一個完整的電子商務網站，提供會員註冊、登入、商店管理、商品管理、購物車管理、訂單管理與會員通知等功能。透過 RabbitMQ 實現會員通知的即時訊息傳遞，並使用 Thymeleaf 作為前端模板引擎來進行頁面渲染。

## 技術棧

- **後端框架**：Spring Boot 3.x
- **前端模板**：Thymeleaf
- **資料庫**：PostgreSQL
- **消息隊列**：RabbitMQ
- **依賴管理**：Maven
- **測試框架**：JUnit
- **API 文件生成與測試**：Swagger 或 Springdoc OpenAPI
- **開發工具**：IntelliJ IDEA 或 Eclipse

## 功能模組

1. **會員管理模組**
   - 會員註冊、會員登入、會員資料查詢與修改。
   - 提供會員登入與註冊的 Thymeleaf 頁面。
   - 使用 RabbitMQ 發送註冊成功與訂單變更的通知給會員。

2. **商店管理模組**
   - 商店新增、查詢、更新與刪除。
   - 商店擁有者可管理自己商店的商品資料。

3. **商品管理模組**
   - 商品上架、下架、查詢與修改。
   - 商品按類別篩選與查詢。

4. **購物車管理模組**
   - 會員可將商品加入購物車，調整數量，移除商品或清空購物車。
   - 購物車頁面顯示會員購物車中所有商品的名稱、價格與數量。

5. **訂單管理模組**
   - 根據購物車生成訂單，管理訂單狀態（如待付款、已取消、已完成）。
   - 查詢、修改與取消訂單，並透過 RabbitMQ 發送通知給買賣雙方。

6. **會員通知模組**
   - 當會員發生訂單狀態變更或系統通知時，透過 RabbitMQ 發送通知給會員，會員可查詢所有通知訊息。

## 環境配置與安裝

### 1. 環境需求

- JDK 17
- Maven 3.6+
- Docker 與 Docker Compose

### 2. 安裝與啟動

#### 2.1. 資料庫與 RabbitMQ 設置

請使用以下 `docker-compose.yml` 文件啟動 PostgreSQL 和 RabbitMQ 服務：

```yaml
version: '3'
services:
  postgres:
    image: postgres:latest
    environment:
      POSTGRES_DB: ecommerce
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  rabbitmq:
    image: rabbitmq:management
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    ports:
      - "5672:5672"
      - "15672:15672"  # 管理介面
    volumes:
      - rabbitmqdata:/var/lib/rabbitmq

volumes:
  pgdata:
  rabbitmqdata:
```

啟動服務：

```bash
docker-compose up -d
```

確認 PostgreSQL 服務已啟動在 `5432` 端口，RabbitMQ 管理介面已啟動在 `15672` 端口（預設帳號與密碼為 `guest/guest`）。

#### 2.2. 專案配置

1. 將 `application.yml` 中的資料庫與 RabbitMQ 連接配置與本地端對應的 Docker 服務相匹配：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: admin
    password: admin
  jpa:
    hibernate:
      ddl-auto: update  # 自動更新資料庫表結構
    show-sql: true      # 顯示 SQL 執行語句

rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest
```

2. 執行以下 Maven 指令來編譯與打包專案：

```bash
mvn clean install
```

3. 啟動專案：

```bash
mvn spring-boot:run
```

4. 訪問 `http://localhost:8080` 來進行測試與操作。

### 3. 測試與使用說明

1. **會員註冊與登入**：
   - 訪問 `http://localhost:8080/users/register` 進行會員註冊。
   - 註冊完成後，可使用註冊的電子郵件與密碼登入系統。

2. **購物車與訂單管理**：
   - 登入後訪問 `http://localhost:8080/cart/{userId}` 查看購物車內容，並將商品新增至購物車。
   - 訪問 `http://localhost:8080/orders/checkout/{userId}` 進行結帳並生成訂單。

3. **訂單狀態管理與通知**：
   - 賣方可修改訂單狀態（如已發貨），並透過 RabbitMQ 通知買方。
   - 買方可在個人中心查看所有訂單通知。

## API 文件與測試

使用 Swagger 或 Springdoc OpenAPI 來生成 API 文件。

- **Swagger UI**：
  - 如果使用的是 `springfox`，請訪問 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)。
  
- **Springdoc OpenAPI**：
  - 如果使用的是 `springdoc-openapi`，請訪問 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)。

### API 範例

1. **會員註冊 API**

   - 路徑：`POST /api/users/register`
   - 請求範例：

     ```json
     {
       "name": "John Doe",
       "email": "john.doe@example.com",
       "password": "password123",
       "address": "123 Main St, Springfield"
     }
     ```

   - 回應範例：

     ```json
     {
       "message": "註冊成功",
       "userId": 1
     }
     ```

2. **新增商品至購物車 API**

   - 路徑：`POST /api/cart/add`
   - 請求範例：

     ```json
     {
       "userId": 1,
       "productId": 101,
       "quantity": 2
     }
     ```

   - 回應範例：

     ```json
     {
       "message": "商品已成功加入購物車",
       "cartItemId": 1001
     }
     ```

## 專案結構

```plaintext
ecommerce-platform
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.tpisoftware.org.stlucia.ecommerce
│   │   │       ├── config        # 配置類別（RabbitMQ、Swagger 等）
│   │   │       ├── controller    # 控制器類別
│   │   │       ├── dto           # 資料傳輸物件（DTO）
│   │   │       ├── model         # 資料庫實體類別
│   │   │       ├── repository    # 資料庫操作介面
│   │   │       ├── service       # 業務邏輯層
│   │   │       └── EcommerceApplication.java # 主程式入口
│   ├── resources
│   │   ├── templates             # Thymeleaf 模板頁面
│   │   ├── static                # 靜態資源（CSS、JS、圖片）
│   │   ├── application.yml       # Spring Boot 配置檔案
├── docker-compose.yml             # Docker Compose 配置檔案
├── pom.xml                        # Maven 配置檔案
└── README.md                      # 說明文件
```
