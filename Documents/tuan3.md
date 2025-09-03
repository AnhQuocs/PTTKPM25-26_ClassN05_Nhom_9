# Tuần 3 - Thiết kế Lớp và Tạo cơ sở code

## 1. Thiết kế Biểu đồ Lớp (Class Diagram)

### Các lớp chính
- **NguoiHienMau**
  - Thuộc tính: maNguoiHien, hoTen, ngaySinh, gioiTinh, nhomMau, lichSuHienMau
  - Phương thức: dangKyHienMau(), huyDangKy(), xemLichHen(), xemLichSu()

- **NhanVienYTe**
  - Thuộc tính: maNhanVien, hoTen, chucVu, maCode
  - Phương thức: quanLyLichHienMau(), xacNhanDangKy(), ghiNhanKetQua()

- **QuanTriVien**
  - Thuộc tính: maQTV, hoTen, taiKhoan
  - Phương thức: quanLyNhanSu(), quanLyNguoiHienMau(), quanLySuKien(), quanLyDiaDiem()

- **SuKienHienMau**
  - Thuộc tính: maSuKien, tenSuKien, ngayToChuc, diaDiem, soLuongToiDa
  - Phương thức: themNguoiHienMau(), kiemTraChoTrong()

- **DangKy**
  - Thuộc tính: maDangKy, ngayDangKy, trangThai
  - Quan hệ: Liên kết giữa NguoiHienMau và SuKienHienMau

- **KetQuaHienMau**
  - Thuộc tính: maKetQua, soLuongMau, tinhTrangSauHien
  - Quan hệ: Gắn với NguoiHienMau và SuKienHienMau

- **HeThong**
  - Thuộc tính: thongBao, duLieuSaoLuu
  - Phương thức: kiemTraDieuKien(), guiThongBao(), luuTruBackup()

### Mỗi quan hệ giữa các lớp

- **QuanTriVien – NhanVienYTe**:  
  Một Quản trị viên có thể quản lý nhiều Nhân viên y tế (1–n).

- **QuanTriVien – NguoiHienMau**:  
  Một Quản trị viên có thể quản lý nhiều Người hiến máu (1–n).

- **QuanTriVien – SuKienHienMau**:  
  Một Quản trị viên có thể tạo/quản lý nhiều Sự kiện hiến máu (1–n).

- **QuanTriVien – DiaDiem**:  
  Một Quản trị viên có thể quản lý nhiều Địa điểm hiến máu (1–n).

- **SuKienHienMau – DiaDiem**:  
  Một sự kiện hiến máu diễn ra tại một địa điểm (n–1).  
  Một địa điểm có thể tổ chức nhiều sự kiện (1–n).

- **NguoiHienMau – DangKy – SuKienHienMau**:  
  Người hiến máu đăng ký tham gia sự kiện thông qua bảng trung gian **DangKy** (n–m).  
  - Một người có thể đăng ký nhiều sự kiện.  
  - Một sự kiện có nhiều người đăng ký.

- **NhanVienYTe – KetQuaHienMau – NguoiHienMau/SuKienHienMau**:  
  Nhân viên y tế ghi nhận kết quả hiến máu (1–n).  
  - Mỗi kết quả thuộc về một người hiến máu và một sự kiện.  
  - Một sự kiện có thể có nhiều kết quả hiến máu.

- **HeThong – SuKienHienMau**:  
  Hệ thống lưu trữ danh sách sự kiện và kiểm tra điều kiện đăng ký (1–n).

- **HeThong – NguoiHienMau**:  
  Hệ thống kiểm tra điều kiện hiến máu và gửi thông báo cho từng người (1–n).

### Biểu đồ Lớp (Class Diagram):

![Class Diagram](https://github.com/user-attachments/assets/eb5b1bf8-6087-4264-819f-466d2d55780e)


### File biểu đồ: 

[ClassDiagram.drawio](https://github.com/user-attachments/files/22123579/ClassDiagram.drawio)

---

## 2. Cơ sở Code (Mobile/Kotlin)
