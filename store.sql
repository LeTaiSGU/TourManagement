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

--tk--

--Nhomquyen--

create procedure getNQByMa
@MaNQ varchar(5)
as
begin
	select * from NHOMQUYEN
	where maNhomQuyen = @MaNQ
end


--------------

------ttram-----


--------------

------my-----


--------------

------cbao-----


--------------