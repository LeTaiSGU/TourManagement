------tài-----
--tk--
use tour_management
go 
create procedure getNhanVienByMa
@MaNV varchar(5)
as
begin 
	select * from NHANVIEN
	where maNhanVien = @MaNV
end

use tour_management
go
ALTER PROCEDURE editTaiKhoan
    @MaNV VARCHAR(5),
    @MatKhau NVARCHAR(50),
    @MaNhomQuyen NVARCHAR(20)
AS
BEGIN

    UPDATE TAIKHOAN
    SET matKhau = @MatKhau,
        maNhomQuyen = @MaNhomQuyen
    WHERE maNhanVien = @MaNV
END

use tour_management
go
create procedure setStatusAcount
	@MaTk varchar(5),
	@TrangThai bit
as
begin
	update TAIKHOAN
	set trangThai = @TrangThai
	where maNhanVien = @MaTk
end

use tour_management
go
select * from TAIKHOAN
exec setStatusAcount 'NV001', 0;


--Nhomquyen--

create procedure getNQByMa
@MaNQ varchar(5)
as
begin
	select * from NHOMQUYEN
	where maNhomQuyen = @MaNQ
end

--tour--
use tour_management
go
create procedure insertTour
@MaTour varchar(4),
@MaLoaiTour varchar(5),
@MaHDV varchar(5),
@tenTour nvarchar(255),
@Anhtour nvarchar(20),
@NoiKhoiHanh nvarchar(255),
@TgKhoiHanh date,
@TgKetThuc date,
@GiaTour decimal(18,2),
@SoLuongVe int,
@SoLuongMin int,
@KhoiHanh bit,
@TrangThai bit
as
begin 
	insert into TOUR(maTour,maLoaiTour,maHDV,tenTour,anhTour,noiKhoiHanh,tgKhoiHanh,tgKetThuc,giaTour,soLuongVe,soLuongMin,khoiHanh,trangThai)
	values (@MaTour,@MaLoaiTour,@MaHDV,@tenTour,@Anhtour,@NoiKhoiHanh,@TgKhoiHanh,@TgKetThuc,@GiaTour,@SoLuongVe,@SoLuongMin,@KhoiHanh,@TrangThai)
end

use tour_management
go
create procedure updateTour
@MaTour varchar(4),
@MaLoaiTour varchar(5),
@MaHDV varchar(5),
@tenTour nvarchar(255),
@Anhtour nvarchar(20),
@NoiKhoiHanh nvarchar(255),
@TgKhoiHanh date,
@TgKetThuc date,
@GiaTour decimal(18,2),
@SoLuongVe int,
@SoLuongMin int
as
begin
	update TOUR
	set 
		maLoaiTour = @MaLoaiTour,
		maHDV = @MaHDV,
		tenTour = @tenTour,
		anhTour = @Anhtour,
		noiKhoiHanh = @NoiKhoiHanh,
		tgKhoiHanh = @TgKhoiHanh,
		tgKetThuc = @TgKetThuc,
		giaTour = @GiaTour,
		soLuongVe = @SoLuongVe,
		soLuongMin = @SoLuongMin
	where maTour = @MaTour
end

use tour_management
go 
create procedure hideTour
@MaTour varchar(4)
as
begin 
	update TOUR
	set
		trangThai = 0
end

SELECT COUNT(*) FROM CTHD WHERE maTour = 'T005' and trangThai = 'DA_DAT'

use tour_management
go 
create procedure deleteTourByAdmin
@MaTour varchar(4),
@GhiChu nvarchar(255)
as
begin
	update TOUR
	set 
		trangThai = 0,
		ghiChu = @GhiChu
	where maTour = @MaTour
end

use tour_management
go
create procedure getLatestTourId
as
begin
    set nocount on;

    select top 1 maTour
    from TOUR
    order by maTour desc
end

use tour_management
go
create procedure getTourByMa
@maTour varchar(4)
as
begin
	select * from TOUR
	where maTour = @maTour
end

use tour_management
go
create procedure getGiaTour
@MaTour varchar(4)
as 
begin
	select giaTour
	from TOUR
	where maTour = @MaTour
end

exec getGiaTour 'T007';

--lichtrinh--
use tour_management
go
create procedure checkMaLT
@MaLT VARCHAR(5)
as
begin
	select 
		case 
            WHEN EXISTS (
                SELECT 1 
                FROM LICHTRINH 
                WHERE maLichTrinh = @MaLT
            ) 
            THEN 1 
            ELSE 0 
        END AS isExist
end


use tour_management
go
create procedure getLastMaLT
as
begin
    set nocount on;

    select top 1 maLichTrinh
    from LICHTRINH
    order by maLichTrinh desc
end

use tour_management
go
create procedure insertLichTrinh
@MaLichTrinh varchar(5),
@MaTour varchar(4),
@MaDiaDiem varchar(5),
@MaPT varchar(5),
@NgayThu int,
@NoiDung nvarchar(255),
@TrangThai bit
as
begin
	insert into LICHTRINH (maLichTrinh,maTour,maDiaDiem,maPT,ngayThu,noiDung,trangThai)
	values (@MaLichTrinh,@MaTour,@MaDiaDiem,@MaPT,@NgayThu,@NoiDung,@TrangThai)
end

use tour_management
go 
create procedure dropLichtrinhTour
@MaTour varchar(4)
as
begin
	update LICHTRINH
	set trangThai = 0
	where maTour = @MaTour
end

use tour_management
go
create procedure editLichTrinh
@MaLichTrinh varchar(5),
@MaTour varchar(4),
@MaDiaDiem varchar(5),
@MaPT varchar(5),
@NgayThu int,
@NoiDung nvarchar(255),
@TrangThai bit
as
begin 
	update LICHTRINH
	set 
		maTour = @MaTour,
		maDiaDiem = @MaDiaDiem,
		maPT = @MaPT,
		ngayThu = @NgayThu,
		noiDung = @NoiDung,
		trangThai = @TrangThai
	where maLichTrinh = @MaLichTrinh
end


USE tour_management;
GO
CREATE OR ALTER PROCEDURE checkTourDuSoLuongKhoiHanh
    @MaTour VARCHAR(4)
AS
BEGIN
    DECLARE 
        @NgayKhoiHanh DATE,
        @SoLuongMin INT,
        @SoLuongDaDat INT = 0,
        @SoNgayConLai INT;

    -- Lấy thông tin tour (không kiểm tra khoiHanh ở đây)
    SELECT 
        @NgayKhoiHanh = tgKhoiHanh,
        @SoLuongMin = soLuongMin
    FROM TOUR
    WHERE maTour = @MaTour;

    -- Không tìm thấy tour
    IF @NgayKhoiHanh IS NULL OR @SoLuongMin IS NULL
    BEGIN
        SELECT 0 AS daCapNhat, N'Không tìm thấy tour.' AS thongBao;
        RETURN;
    END

    -- Số ngày còn lại tới ngày khởi hành
    SET @SoNgayConLai = DATEDIFF(DAY, CAST(GETDATE() AS DATE), @NgayKhoiHanh);

    -- Chỉ kiểm tra khi còn từ 0 đến 7 ngày
    IF @SoNgayConLai BETWEEN 0 AND 7
    BEGIN
        -- Tổng số vé đã mua hợp lệ
        SELECT 
            @SoLuongDaDat = ISNULL(SUM(ct.soLuongVe), 0)
        FROM CTHD ct
        WHERE ct.maTour = @MaTour
          AND ct.trangThai IN ('DA_DAT');

        -- Điều kiện bạn yêu cầu: phải > soLuongMin mới đạt
        IF @SoLuongDaDat <= @SoLuongMin
        BEGIN
            UPDATE TOUR
            SET trangThai = 0,
				ghiChu = N'Hủy tour do không đủ số lượng'
            WHERE maTour = @MaTour;

            SELECT 
                1 AS daCapNhat,
                N'Đã hủy tour do số vé mua không đủ điều kiện (> soLuongMin).' AS thongBao,
                @SoLuongDaDat AS soLuongDaDat,
                @SoLuongMin AS soLuongMin;
            RETURN;
        END
    END

    SELECT 
        0 AS daCapNhat,
        N'Tour đủ điều kiện hoặc chưa trong khoảng 7 ngày trước khởi hành.' AS thongBao,
        @SoLuongDaDat AS soLuongDaDat,
        @SoLuongMin AS soLuongMin;
END;
GO

CREATE OR ALTER PROCEDURE checkKhoiHanhHomNay
AS
BEGIN
    -- Cập nhật các tour khởi hành hôm nay
    -- Điều kiện: tổng vé đã đặt > soLuongMin
    UPDATE t
    SET t.khoiHanh = 1
    FROM TOUR t
    INNER JOIN (
        SELECT 
            ct.maTour,
            ISNULL(SUM(ct.soLuongVe), 0) AS tongVeDaDat
        FROM CTHD ct
        WHERE ct.trangThai = 'DA_DAT'
        GROUP BY ct.maTour
    ) x ON x.maTour = t.maTour
    WHERE t.tgKhoiHanh = CAST(GETDATE() AS DATE)
      AND t.khoiHanh = 0
      AND t.trangThai = 1
      AND x.tongVeDaDat > t.soLuongMin;
    -- Trả số tour vừa được set khoiHanh = 1
    SELECT @@ROWCOUNT AS soTourDaKhoiHanh;
END;
GO

EXEC checkKhoiHanhHomNay;

--Hoa Don--
use tour_management
go

create procedure getHDByMaTourHuy
@MaTour varchar(4)
as
begin
    select hd.*, kh.tenKhachHang, kh.soDienThoai
    from HOADON hd
    join KHACHHANG kh on hd.maKhachHang = kh.maKhachHang
    where hd.maHoaDon in (
        select maHoaDon
        from CTHD
        where maTour = @MaTour
    )
end

use tour_management
go
create procedure getEmailKhachHang
@MaHoaDon varchar(5)
as
begin
	select email
	from KHACHHANG as kh
	join HOADON as hd on hd.maKhachHang = kh.maKhachHang
	where hd.maHoaDon = @MaHoaDon
end

use tour_management
go
create procedure getTTKhachHang
@MaHoaDon varchar(5)
as
begin
	select tenKhachHang,soDienThoai
	from KHACHHANG as kh
	join HOADON as hd on hd.maKhachHang = kh.maKhachHang
	where hd.maHoaDon = @MaHoaDon
end

use tour_management
go 
create procedure updateHoaDonHuyDoCongTy
@MaHoaDon varchar(5),
@TienHoan decimal(18,2)
as
begin
	update HOADON
    set tongTien = tongTien - @TienHoan
    where maHoaDon = @MaHoaDon
end
exec updateHoaDonHuyDoCongTy 'HD048', 50000000;

--Chi tiet hoa don--
use tour_management
go 
create procedure HuyHoaDonDatTour
@MaTour varchar(4),
@LyDoHuy nvarchar(255),
@HoanTien bit
as
begin
	update CTHD
	set 
		trangThai = N'HUY_DO_CONG_TY',
		ghiChu = @LyDoHuy,
		hoanTien = @HoanTien
	where maTour = @MaTour
end



--------------

------ttram-----


--------------

------my-----


--------------

------cbao-----


--------------