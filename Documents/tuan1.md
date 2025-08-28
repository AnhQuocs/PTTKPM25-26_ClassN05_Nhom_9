Tuần 1 - Phân Tích Yêu Cầu

1. Actors (Vai trò)
- Người hiến máu:
  + Đăng nhập/Đăng ký hiến máu.
  + Hủy đăng ký.
  + Xem lịch hẹn hiến máu.
  + Xem lịch sử hiến máu.

- Nhân viên y tế:
  + Quản lý lịch hiến máu.
  + Xác nhận đăng ký hiến máu.
  + Ghi nhận kết quả hiến máu.

- Quản trị viên:
  + Quản lý người hiến máu.
  + Quản lý sự kiện hiến máu.
  + Quản lý địa điểm hiến máu.

- Hệ thống:
  + Kiểm tra điều kiện hiến máu (tuổi, sức khỏe, khoảng cách ngày hiến máu trước đó…).
  + Gửi thông báo nhắc nhở trước lịch hẹn.
  + Lưu trữ và backup dữ liệu.

2. Tổng quan hệ thống
- Mục tiêu hệ thống:
  + Kết nối người hiến máu, nhân viên y tế và ban tổ chức.
  + Hỗ trợ quy trình đăng ký, quản lý, nhắc nhở và lưu trữ lịch sử.

- Phạm vi hệ thống:
  + Đối tượng phục vụ: người hiến máu, nhân viên y tế, quản trị viên.
  + Nền tảng: Android / Web / Mobile-first.

- Các ràng buộc:
  + Cần Internet.
  + Phải tuân thủ quy định y tế về bảo mật thông tin cá nhân.

- Giả định và phụ thuộc:
  + Người dùng có thiết bị kết nối Internet.
  + Người hiến máu cung cấp thông tin chính xác khi đăng ký.

3. Yêu cầu chức năng (Functional Requirements)
- Hệ thống cho phép người hiến máu tạo tài khoản và đăng ký hiến máu trong các sự kiện còn chỗ.
- Người hiến máu có thể hủy đăng ký trong thời gian cho phép.
- Nhân viên y tế có thể quản lý danh sách đăng ký, xác nhận và cập nhật trạng thái hiến máu.
- Quản trị viên có thể quản lý thông tin sự kiện, địa điểm, và người hiến máu.
- Hệ thống gửi thông báo nhắc nhở trước lịch hiến máu ít nhất 1 ngày.
- Hệ thống cho phép người hiến máu tra cứu lịch sử hiến máu của bản thân.

4. Yêu cầu phi chức năng (Non-functional Requirements)
- Bảo mật: Bảo vệ thông tin cá nhân và y tế, xác thực đăng nhập an toàn.
- Hiệu năng: Thời gian phản hồi tra cứu sự kiện ≤ 2 giây.
- Sẵn sàng: Hệ thống hoạt động ổn định, có thể phục vụ ≥ 500 người dùng cùng lúc.
- Khả năng mở rộng: Có thể mở rộng số lượng sự kiện và địa điểm dễ dàng.
- Dễ dùng: Giao diện rõ ràng, hỗ trợ mobile-first.
- Bảo toàn dữ liệu: Sao lưu định kỳ và khôi phục nhanh khi có sự cố.
- Khả năng truy cập: Hỗ trợ đa ngôn ngữ (Việt – Anh).

5. Biểu đồ UseCase

<img width="1249" height="770" alt="Sơ đồ UseCase" src="https://github.com/user-attachments/assets/9c64d959-737c-44b6-bc33-f7c149183c04" />

