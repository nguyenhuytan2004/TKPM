# IT TOOL WEB

## 📝 Mô tả

Dự án Spring Boot tích hợp với Tailwind CSS, sử dụng Handlebars làm template engine. Dữ liệu được lưu trữ trong MySQL và được khởi tạo qua file `it_tool_db.sql`.

---

## 🛠️ Yêu cầu hệ thống

- Java 17
- Maven 3.6+
- Node.js & npm (Node 18+ khuyến khích)
- MySQL server (8.x)
- IDE bất kỳ (IntelliJ, VS Code, ...)

---

## ⚙️ Cài đặt và cấu hình

### 1. Khởi tạo database MySQL bằng MySQL Workbench 

#### 🧩 Bước 1: Mở MySQL Workbench và chạy script tạo database

1. Mở **MySQL Workbench** và kết nối với server MySQL (thường là `localhost`, cổng `3306`, user `root`).
2. Vào menu **File → Open SQL Script**.
3. Chọn file `it_tool_db.sql`.
4. Nhấn nút ⚡ **Execute** (hoặc Ctrl + Shift + Enter) để chạy script.
5. Kiểm tra lại xem database `it_tool_db` đã được tạo cùng các bảng cần thiết chưa.

> Nếu không thấy database sau khi chạy script, bạn có thể nhấn F5 để refresh lại danh sách schema ở sidebar.

---

#### ⚙️ Bước 2: Cấu hình kết nối database 

1. Mở project bằng IDE của bạn (VS Code, IntelliJ, ...).
2. Mở file `src/main/resources/application.properties`.
3. Chỉnh sửa thông tin kết nối MySQL như sau:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/it_tool_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

> 🔐 Thay `your_password` bằng mật khẩu thật sự của tài khoản `root` trong MySQL.

---

### 2. Cài đặt Tailwind CSS

1. Cài đặt các package cần thiết:

```bash
npm install
```

2. Tailwind sẽ lấy input từ:

```
src/main/resources/static/css/input.css
```

Và build ra:

```
src/main/resources/static/css/output.css
```

3. Để theo dõi và build Tailwind khi có thay đổi:

```bash
npm run watch
```

> Bạn nên mở một terminal riêng để lệnh `watch` chạy liên tục khi đang dev.

---

### 3. Build và chạy project Spring Boot

1. Đảm bảo đã cài **Java Extension Pack** nếu dùng VS Code.
2. Mở terminal tích hợp (Ctrl + `).
3. Chạy ứng dụng bằng Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Hoặc nếu wrapper không hoạt động:

```bash
mvn spring-boot:run
```

4. Mở trình duyệt truy cập: [http://localhost:8080](http://localhost:8080)

---

## 🧹 Plugin nội bộ

Dự án sử dụng file JAR nội bộ:

```xml
<dependency>
    <groupId>com.example.plugin</groupId>
    <artifactId>IPlugin</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/IPlugin-1.0.0.jar</systemPath>
</dependency>
```

> 📂 Đảm bảo file `libs/IPlugin-1.0.0.jar` tồn tại trong thư mục `libs/` ở root project.

---

✅ Sau khi hoàn tất các bước trên, bạn đã có thể phát triển và chạy ứng dụng Spring Boot với giao diện Tailwind, Handlebars template và cơ sở dữ liệu MySQL đã được khởi tạo.
