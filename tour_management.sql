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
    soDienThoai  VARCHAR(15),
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
    soDienThoai VARCHAR(15),
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
    soDienThoai VARCHAR(15),
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
    maHoaDon    VARCHAR(6) PRIMARY KEY,
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
    maHoaDon VARCHAR(6),
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
