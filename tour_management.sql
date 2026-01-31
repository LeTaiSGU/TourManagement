IF DB_ID('tour_management') IS NULL
    CREATE DATABASE tour_management;
GO
USE tour_management;
GO

/* ===== LOAIKHACHHANG ===== */
CREATE TABLE LOAIKHACHHANG (
    maLoaiKH  VARCHAR(5) PRIMARY KEY,
    tenLoaiKH NVARCHAR(100) NOT NULL,
    moTa      NVARCHAR(255)
);
GO

/* ===== KHUYENMAI ===== */
CREATE TABLE KHUYENMAI (
    maKhuyenMai  VARCHAR(5) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(100) NOT NULL,
    moTa         NVARCHAR(255),
    phuongThucKM NVARCHAR(100),
    trangThaiKM  BIT DEFAULT 1
);
GO

/* ===== KHACHHANG ===== */
CREATE TABLE KHACHHANG (
    maKhachHang  VARCHAR(5) PRIMARY KEY,
    maLoaiKH     VARCHAR(5),
    tenKhachHang NVARCHAR(100) NOT NULL,
    gioiTinh     NVARCHAR(10),
    namSinh      INT,
    diaChi       NVARCHAR(255),
    soDienThoai  VARCHAR(10),
	email		 NVARCHAR(100),
    trangThai    BIT DEFAULT 1,
    FOREIGN KEY (maLoaiKH) REFERENCES LOAIKHACHHANG(maLoaiKH)
);
GO

/* ===== CHUCVU ===== */
CREATE TABLE CHUCVU (
    maChucVu  VARCHAR(5) PRIMARY KEY,
    tenChucVu NVARCHAR(100) NOT NULL,
    moTa      NVARCHAR(255)
);
GO

/* ===== NHANVIEN ===== */
CREATE TABLE NHANVIEN (
    maNhanVien  VARCHAR(5) PRIMARY KEY,
    tenNhanVien NVARCHAR(100) NOT NULL,
    gioiTinh    NVARCHAR(10),
    namSinh     INT,
    diaChi      NVARCHAR(255),
    soDienThoai VARCHAR(10),
    maChucVu    VARCHAR(5),
    trangThai   BIT DEFAULT 1,
    FOREIGN KEY (maChucVu) REFERENCES CHUCVU(maChucVu)
);
GO

/* ===== HUONGDANVIEN ===== */
CREATE TABLE HUONGDANVIEN (
    maHDV       VARCHAR(5) PRIMARY KEY,
    tenHDV      NVARCHAR(100) NOT NULL,
    gioiTinh    NVARCHAR(10),
    namSinh     INT,
    chuyenMon   NVARCHAR(100),
    soDienThoai VARCHAR(10),
    trangThai   BIT DEFAULT 1
);
GO

/* ===== LOAITOUR ===== */
CREATE TABLE LOAITOUR (
    maLoaiTour VARCHAR(5) PRIMARY KEY,
    tenLoai    NVARCHAR(100) NOT NULL,
    moTa       NVARCHAR(255)
);
GO

/* ===== TOUR ===== */
CREATE TABLE TOUR (
    maTour      VARCHAR(4) PRIMARY KEY,
    maLoaiTour  VARCHAR(5),
    maHDV       VARCHAR(5),
    tenTour     NVARCHAR(255) NOT NULL,
    anhTour     NVARCHAR(255),
    noiKhoiHanh NVARCHAR(255),
    tgKhoiHanh  DATE,
    tgKetThuc   DATE,
    giaTour     FLOAT,
    soLuongVe   INT,
    soLuongMin  INT,
    trangThai   BIT DEFAULT 1,
    khoiHanh    BIT DEFAULT 0,
    FOREIGN KEY (maLoaiTour) REFERENCES LOAITOUR(maLoaiTour),
    FOREIGN KEY (maHDV) REFERENCES HUONGDANVIEN(maHDV)
);
GO

/* ===== DIADIEM ===== */
CREATE TABLE DIADIEM (
    maDiaDiem  VARCHAR(5) PRIMARY KEY,
    tenDiaDiem NVARCHAR(100) NOT NULL,
    anhDiaDiem NVARCHAR(255),
    quocGia    NVARCHAR(100),
    moTa       NVARCHAR(255),
    trangThai  BIT DEFAULT 1
);
GO

/* ===== PHUONGTIEN ===== */
CREATE TABLE PHUONGTIEN (
    maPT      VARCHAR(5) PRIMARY KEY,
    tenPT     NVARCHAR(100) NOT NULL,
    moTa      NVARCHAR(255),
    trangThai BIT DEFAULT 1
);
GO

/* ===== LICHTRINH ===== */
CREATE TABLE LICHTRINH (
    maLichTrinh VARCHAR(5) PRIMARY KEY,
    maTour      VARCHAR(4),
    maDiaDiem   VARCHAR(5),
    maPT        VARCHAR(5),
    ngayThu     INT,
    noiDung     NVARCHAR(MAX),
    trangThai   BIT DEFAULT 1,
    FOREIGN KEY (maTour) REFERENCES TOUR(maTour),
    FOREIGN KEY (maDiaDiem) REFERENCES DIADIEM(maDiaDiem),
    FOREIGN KEY (maPT) REFERENCES PHUONGTIEN(maPT)
);
GO

/* ===== HOADON ===== */
CREATE TABLE HOADON (
    maHoaDon    VARCHAR(5) PRIMARY KEY,
    maNhanVien  VARCHAR(5),
    maKhachHang VARCHAR(5),
    ngayLapHD   DATE,
    soLuongVe   INT,
    tongTien    FLOAT,
    maKhuyenMai VARCHAR(5),
    thue        FLOAT,
    HTTT        NVARCHAR(50),
    trangThaiTT BIT,
    trangThaiHD BIT,
    FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien),
    FOREIGN KEY (maKhachHang) REFERENCES KHACHHANG(maKhachHang),
    FOREIGN KEY (maKhuyenMai) REFERENCES KHUYENMAI(maKhuyenMai)
);
GO

/* ===== CTHD ===== */
CREATE TABLE CTHD (
    maHoaDon VARCHAR(5),
    maTour   VARCHAR(4),
    PRIMARY KEY (maHoaDon, maTour),
    FOREIGN KEY (maHoaDon) REFERENCES HOADON(maHoaDon),
    FOREIGN KEY (maTour) REFERENCES TOUR(maTour)
);
GO

/* ===== CHUCNANG ===== */
CREATE TABLE CHUCNANG (
    maChucNang  VARCHAR(5) PRIMARY KEY,
    tenChucNang NVARCHAR(100) NOT NULL,
    moTa        NVARCHAR(255)
);
GO

/* ===== NHOMQUYEN ===== */
CREATE TABLE NHOMQUYEN (
    maNhomQuyen  VARCHAR(5) PRIMARY KEY,
    tenNhomQuyen NVARCHAR(100) NOT NULL,
    moTa         NVARCHAR(255)
);
GO

/* ===== CTCNNQ ===== */
CREATE TABLE CTCNNQ (
    maNhomQuyen VARCHAR(5),
    maCN        VARCHAR(5),
    PRIMARY KEY (maNhomQuyen, maCN),
    FOREIGN KEY (maNhomQuyen) REFERENCES NHOMQUYEN(maNhomQuyen),
    FOREIGN KEY (maCN) REFERENCES CHUCNANG(maChucNang)
);
GO

/* ===== TAIKHOAN (1–1 NHANVIEN) ===== */
CREATE TABLE TAIKHOAN (
    maNhanVien  VARCHAR(5) PRIMARY KEY, -- username
    matKhau     NVARCHAR(255) NOT NULL,
    maNhomQuyen VARCHAR(5),
    trangThai   BIT DEFAULT 1,
    FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien),
    FOREIGN KEY (maNhomQuyen) REFERENCES NHOMQUYEN(maNhomQuyen)
);
GO

-- ================== DỮ LIỆU MẪU ==================

use tour_management
go
-- Loại khách hàng
INSERT INTO LOAIKHACHHANG (maLoaiKH, tenLoaiKH, moTa) VALUES
('LKH01', N'Khách thường', N'Khách lẻ đặt tour không thường xuyên'),
('LKH02', N'Khách thân thiết', N'Khách quay lại nhiều lần, được ưu đãi'),
('LKH03', N'Khách doanh nghiệp', N'Công ty đặt tour theo đoàn');

use tour_management
go
-- Khuyến mãi
INSERT INTO KHUYENMAI(maKhuyenMai, tenKhuyenMai, moTa, phuongThucKM, trangThaiKM) VALUES
('KM001', N'Giảm 10% dịp lễ', N'Áp dụng cho tour nội địa trong dịp lễ', N'Giảm phần trăm', 1),
('KM002', N'Giảm 500k cho khách thân thiết', N'Áp dụng cho khách loại LKH02', N'Giảm số tiền', 1),
('KM003', N'Khuyến mãi cho công ty đặt tour', N'Giảm 5% cho từng vé', N'Tặng kèm', 1);


-- Chức vụ
INSERT INTO chuc_vu (maChucVu, tenChucVu, moTa) VALUES
('CV01', 'Quản trị viên', 'Quản lý toàn bộ hệ thống'),
('CV02', 'Nhân viên bán tour', 'Tư vấn & bán tour cho khách'),
('CV03', 'Kế toán', 'Quản lý hóa đơn & thanh toán');

use tour_management
go
-- Nhân viên
INSERT INTO NHANVIEN(maNhanVien, tenNhanVien, gioiTinh, namSinh, diaChi, soDienThoai, maChucVu, trangThai) VALUES
('NV001', N'Lê Tấn Tài', N'Nam', 1995, N'Quận 1, TP. Hồ Chí Minh', '0901000001', 'CV001', 1),
('NV002', N'Nguyễn Thị Tuyết Trâm', N'Nữ', 1997, N'Cầu Giấy, Hà Nội', '0902000002', 'CV002', 1),
('NV003', N'Phan Chí Bảo', N'Nam', 1992, N'Thanh Khê, Đà Nẵng', '0903000003', 'CV003', 1),
('NV004', N'Lê Trương Thảo My', N'Nữ', 1992, N'Vũng Tàu, TP. Hồ Chí Minh', '0903000003', 'CV003', 1);

use tour_management
go
-- Hướng dẫn viên
INSERT INTO HUONGDANVIEN(maHDV, tenHDV, gioiTinh, namSinh, chuyenMon, soDienThoai, trangThai) VALUES
('HDV01', N'Lê Quốc Huy', N'Nam', 1990, N'Tour biển & nghỉ dưỡng', '0911000001', 1),
('HDV02', N'Vũ Thu Trang', N'Nữ', 1993, N'Tour núi & trekking', '0912000002', 1),
('HDV03', N'Đặng Văn Bình', N'Nam', 1995, N'Tour văn hóa lịch sử', '0913000003', 1),
('HDV04', N'Doãn Chí Bình', N'Nam', 1988, N'Tour nước ngoài (Tây Âu)', '0913000003', 1),
('HDV06', N'Đoàn Thanh Thảo', N'Nữ', 2002, N'Tour Bắc Bộ', '0913000003', 1),
('HDV07', N'Dương Chí Bảo', N'Nam', 1999, N'Tour nước ngoài (Úc, Pháp, Ý,..)', '0913000003', 1);

use tour_management
go
-- Loại tour
INSERT INTO LOAITOUR (maLoaiTour, tenLoai, moTa) VALUES
('LT001', N'Tour nội thành', N'Tham quan các điểm nổi bật trong thành phố'),
('LT002', N'Tour biển', N'Nghỉ dưỡng tại các thành phố biển'),
('LT003', N'Tour núi', N'Khám phá vùng núi & khí hậu mát mẻ'),
('LT004', N'Tour quốc tế', N'Trải nghiệm các đất nước tươi đẹp, hiện đại');

use tour_management
go
-- Địa điểm (các thành phố Việt Nam)
INSERT INTO DIADIEM(maDiaDiem, tenDiaDiem, anhDiaDiem, quocGia, moTa, trangThai) VALUES
('DD001', N'Hà Nội', NULL, N'Việt Nam', N'Thủ đô, trung tâm văn hóa - chính trị', 1),
('DD002', N'TP. Hồ Chí Minh', NULL, N'Việt Nam', N'Trung tâm kinh tế lớn nhất cả nước', 1),
('DD003', N'Đà Nẵng', NULL, N'Việt Nam', N'Thành phố biển và du lịch nổi tiếng', 1),
('DD004', N'Nha Trang', NULL, N'Việt Nam', N'Thành phố biển với nhiều resort đẹp', 1),
('DD005', N'Đà Lạt', NULL, N'Việt Nam', N'Thành phố ngàn hoa khí hậu mát mẻ', 1),
('DD006', N'Huế', NULL, N'Việt Nam', N'Cố đô với nhiều di tích lịch sử', 1),
('DD007', N'Phú Quốc', NULL, N'Việt Nam', N'Đảo ngọc với bãi biển đẹp', 1),
('DD008', N'Cần Thơ', NULL, N'Việt Nam', N'Trung tâm miền Tây sông nước', 1),
('DD009', N'Hạ Long', NULL, N'Việt Nam', N'Vịnh Hạ Long – di sản thiên nhiên thế giới', 1),
('DD010', N'Sa Pa', NULL, N'Việt Nam', N'Thị trấn vùng cao, khí hậu lạnh', 1);

use tour_management
go
-- Phương tiện (máy bay, tàu, bus)
INSERT INTO PHUONGTIEN (maPT, tenPT, moTa, trangThai) VALUES
('PT001', N'Máy bay', N'Di chuyển nhanh giữa các thành phố', 1),
('PT002', N'Tàu hỏa', N'Ngắm cảnh dọc đường, chi phí hợp lý', 1),
('PT003', N'Xe bus', N'Di chuyển linh hoạt, phù hợp tour ngắn', 1);

use tour_management
go
-- Tour du lịch
INSERT INTO TOUR (maTour, maLoaiTour, maHDV, tenTour, anhTour, noiKhoiHanh, tgKhoiHanh, tgKetThuc, giaTour, soLuongVe, soLuongMin, trangThai, khoiHanh) VALUES
('T001', 'LT002', 'HDV01', N'Du lịch Nha Trang 4N3Đ từ TP.HCM', NULL, N'TP. Hồ Chí Minh', '2026-06-01', '2026-06-04', 6500000, 30, 10, 1, 0),
('T002', 'LT003', 'HDV02', N'Khám phá Sa Pa 3N2Đ từ Hà Nội', NULL, N'Hà Nội', '2026-04-10', '2026-04-12', 5500000, 25, 8, 1, 0),
('T003', 'LT004', 'HDV03', N'Miền Tây sông nước 2N1Đ Cần Thơ', NULL, N'TP. Hồ Chí Minh', '2026-05-05', '2026-05-06', 2500000, 40, 15, 1, 0),
('T004', 'LT001', 'HDV03', N'City tour Hà Nội 1 ngày', NULL, N'Hà Nội', '2026-03-15', '2026-03-15', 900000, 50, 10, 1, 0),
('T005', 'LT003', 'HDV02', N'Đà Lạt mộng mơ 3N2Đ', NULL, N'TP. Hồ Chí Minh', '2026-07-20', '2026-07-22', 4800000, 35, 12, 1, 0);

use tour_management
go
-- Lịch trình cho từng tour
INSERT INTO LICHTRINH (maLichTrinh, maTour, maDiaDiem, maPT, ngayThu, noiDung, trangThai) VALUES
('LT001', 'T001', 'DD002', 'PT001', 1, N'Bay từ TP.HCM ra Nha Trang, nhận phòng khách sạn, tắm biển', 1),
('LT002', 'T001', 'DD004', 'PT003', 2, N'Tham quan các đảo vịnh Nha Trang, lặn biển', 1),
('LT003', 'T001', 'DD004', 'PT003', 3, N'Tự do tham quan, chợ đêm Nha Trang', 1),

('LT004', 'T002', 'DD001', 'PT002', 1, N'Đi tàu từ Hà Nội lên Lào Cai, di chuyển lên Sa Pa', 1),
('LT005', 'T002', 'DD010', 'PT003', 2, N'Tham quan bản Cát Cát, núi Hàm Rồng', 1),
('LT006', 'T002', 'DD010', 'PT003', 3, N'Chợ Sa Pa, trở về Hà Nội', 1),

('LT007', 'T003', 'DD002', 'PT003', 1, N'Khởi hành từ TP.HCM đi Cần Thơ, tham quan bến Ninh Kiều', 1),
('LT008', 'T003', 'DD008', 'PT003', 2, N'Chợ nổi Cái Răng, vườn trái cây, trở về TP.HCM', 1),

('LT009', 'T004', 'DD001', 'PT003', 1, N'Tham quan Lăng Bác, Văn Miếu, Hồ Gươm', 1),

('LT010', 'T005', 'DD002', 'PT002', 1, N'Từ TP.HCM đi Đà Lạt, ghé đèo Prenn', 1),
('LT011', 'T005', 'DD005', 'PT003', 2, N'Tham quan Thung lũng Tình Yêu, Đồi mộng mơ', 1),
('LT012', 'T005', 'DD005', 'PT003', 3, N'Chợ đêm Đà Lạt, trở về TP.HCM', 1);

-- Loại khách & khách hàng
INSERT INTO KHACHHANG (maKhachHang, maLoaiKH, tenKhachHang, gioiTinh, namSinh, diaChi, soDienThoai,email,trangThai) VALUES
('KH001', 'LKH01', N'Nguyễn Văn A', N'Nam', 1995, N'Hà Nội' ,'0981000001','letantai20072003@gmail.com', 1),
('KH002', 'LKH02', N'Trần Thị B', N'Nữ', 1990, N'TP. Hồ Chí Minh', '0982000002','abc@gmail.com', 1),
('KH003', 'LKH02', N'Phạm Văn C', N'Nam', 1988, N'Đà Nẵng', '0983000003', 'abc@gmail.com',1),
('KH004', 'LKH03', N'Công ty Du lịch ABC', N'Khác', 2000, 'Quận 1, TP.HCM', '0281234567', 'abc@gmail.com',1),
('KH005', 'LKH01', N'Lê Thị D', N'Nữ', 1998, N'Cần Thơ', '0984000004','abc@gmail.com', 1);

-- Nhóm quyền & chức năng
--INSERT INTO nhom_quyen (maNhomQuyen, tenNhomQuyen, moTa) VALUES
--('NQ01', 'ADMIN', 'Quyền quản trị toàn hệ thống'),
--('NQ02', 'NHANVIEN', 'Nhân viên bán tour');


--INSERT INTO chuc_nang (maChucNang, tenChucNang, moTa) VALUES
--('CN01', 'QUAN_LY_TOUR', 'Quản lý tour du lịch'),
--('CN02', 'QUAN_LY_KHACH_HANG', 'Quản lý khách hàng'),
--('CN03', 'QUAN_LY_HOA_DON', 'Quản lý hóa đơn & thanh toán'),
--('CN04', 'QUAN_LY_TAI_KHOAN', 'Quản lý tài khoản & phân quyền');

--INSERT INTO ctcn_nq (maNhomQuyen, maCN) VALUES
--('NQ001', 'CN001'),
--('NQ001', 'CN002'),
--('NQ001', 'CN003'),
--('NQ001', 'CN004'),
--('NQ002', 'CN001'),
--('NQ002', 'CN002'),
--('NQ002', 'CN003');

-- Tài khoản đăng nhập
--INSERT INTO TAIKHOAN (maNhanVien, maNhomQuyen, matKhau, trangThai) VALUES
--('admin', 'NQ01', '123456', 1),
--('nv01', 'NQ02', '123456', 1);

-- Hóa đơn & chi tiết hóa đơn
--INSERT INTO hoa_don (maHoaDon, maNhanVien, maKhachHang, ngayLapHD, soLuongVe, tongTien, maKhuyenMai, thue, HTTT, trangThaiTT, trangThaiHD) VALUES
--('HD001', 'NV02', 'KH001', '2026-03-01', 2, 13000000, 'KM01', 0.10, 'Chuyển khoản', 1, 1),
--('HD002', 'NV02', 'KH002', '2026-04-05', 3, 16500000, 'KM02', 0.10, 'Tiền mặt', 1, 1),
--('HD003', 'NV03', 'KH004', '2026-05-10', 10, 25000000, 'KM03', 0.10, 'Chuyển khoản', 1, 1);

--INSERT INTO cthd (maHoaDon, maTour) VALUES
--('HD001', 'T001'),
--('HD002', 'T002'),
--('HD002', 'T004'),
--('HD003', 'T003'),
--('HD003', 'T005');