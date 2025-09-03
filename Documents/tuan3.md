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

### Quan hệ giữa các lớp
- NguoiHienMau **đăng ký** SuKienHienMau thông qua DangKy.
- NhanVienYTe **xác nhận** và **ghi nhận kết quả** hiến máu.
- QuanTriVien **quản lý** NhanVienYTe, NguoiHienMau, SuKienHienMau, DiaDiem.
- HeThong **kiểm tra điều kiện** và **gửi thông báo**.

### Biểu đồ: 


---

## 2. Cơ sở Code (Mobile/Kotlin)