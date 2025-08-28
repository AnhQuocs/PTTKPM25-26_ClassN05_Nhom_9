# Tuần 2 - Mô hình hóa Use Case và Kịch bản

## 1. Biểu đồ Use Case

![Sơ đồ UseCase](https://github.com/user-attachments/assets/9c64d959-737c-44b6-bc33-f7c149183c04)

---

## 2. Kịch bản Use Case chi tiết

### 2.1. Use Case: Đăng ký hiến máu
**Actor chính:** Người hiến máu  
**Mục tiêu:** Người hiến máu đăng ký tham gia một đợt hiến máu thành công.  
**Tiền điều kiện:** Người hiến máu đã có tài khoản hoặc thực hiện đăng nhập.  

#### Luồng chính – thành công
1. Người hiến máu mở ứng dụng và chọn chức năng **Đăng ký hiến máu**.  
2. Ứng dụng yêu cầu nhập thông tin (họ tên, ngày sinh, số CMND/CCCD, nhóm máu, lịch sử hiến máu, chọn địa điểm, chọn thời gian,...).  
3. Hệ thống **kiểm tra điều kiện hiến máu** (tuổi, khoảng cách lần hiến trước, tình trạng sức khỏe,...).  
4. Nếu hợp lệ, hệ thống lưu thông tin đăng ký.  
5. Hệ thống hiển thị thông báo: “Đăng ký hiến máu thành công”.  
6. Hệ thống gửi thông báo nhắc nhở trước lịch hẹn.  

#### Trường hợp đặc biệt
- **A1: Người hiến máu không đủ điều kiện**  
  1. Tại bước 3, hệ thống phát hiện điều kiện không đạt (ví dụ: dưới 18 tuổi, chưa đủ 3 tháng từ lần hiến trước,...).  
  2. Hệ thống hiển thị thông báo: “Bạn chưa đủ điều kiện hiến máu.”  
  3. Quay lại màn hình chính.

- **A2: Địa điểm/Thời gian hiến máu đã đầy chỗ**  
  1. Tại bước 2, sau khi chọn địa điểm, hệ thống báo: “Lịch hẹn tại địa điểm này đã đầy.”  
  2. Người hiến máu được yêu cầu chọn địa điểm hoặc thời gian khác.  

---

### 2.2. Use Case: Xác nhận đăng ký hiến máu
**Actor chính:** Nhân viên y tế  
**Mục tiêu:** Xác nhận đăng ký của người hiến máu hợp lệ.  
**Tiền điều kiện:** Người hiến máu đã gửi yêu cầu đăng ký.  

#### Luồng chính – thành công
1. Nhân viên y tế đăng nhập vào hệ thống.  
2. Nhân viên chọn **Danh sách đăng ký hiến máu**.  
3. Nhân viên y tế mở hồ sơ một người hiến máu.  
4. Hệ thống hiển thị thông tin + lịch sử hiến máu.  
5. Nhân viên y tế xác nhận nếu đủ điều kiện.  
6. Hệ thống cập nhật trạng thái: “Đã xác nhận”.  
7. Người hiến máu nhận được thông báo xác nhận.  

#### Trường hợp đặc biệt
- **B1: Người hiến máu không đủ điều kiện**  
  1. Tại bước 4, nhân viên y tế thấy người hiến máu không đạt điều kiện (ví dụ: huyết áp không phù hợp).  
  2. Nhân viên chọn “Từ chối đăng ký”.  
  3. Hệ thống lưu trạng thái và gửi thông báo từ chối đến người hiến máu.  

---

### 2.3. Use Case: Ghi nhận kết quả hiến máu
**Actor chính:** Nhân viên y tế  
**Mục tiêu:** Lưu kết quả hiến máu sau khi người hiến hoàn tất.  

#### Luồng chính – thành công
1. Sau khi hiến máu, nhân viên y tế mở ứng dụng, chọn **Ghi nhận kết quả hiến máu**.  
2. Nhập thông tin: số lượng máu lấy được, nhóm máu, tình trạng sau hiến.  
3. Hệ thống lưu kết quả vào hồ sơ người hiến máu.  
4. Hệ thống cập nhật **lịch sử hiến máu** cho người hiến.  
5. Hệ thống tự động backup dữ liệu.  

#### Trường hợp đặc biệt
- **C1: Lỗi khi lưu kết quả**  
  1. Tại bước 3, hệ thống gặp lỗi kết nối.  
  2. Hệ thống thông báo: “Không thể lưu kết quả, vui lòng thử lại.”  
  3. Nhân viên y tế có thể lưu lại sau.  
