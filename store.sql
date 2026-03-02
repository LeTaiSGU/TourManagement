------tài-----
use tour_management
go 
EXEC getNhanVienByMa NV002;
create procedure getNhanVienByMa
@MaNV varchar(5)
as
begin 
	select * from NHANVIEN
	where maNhanVien = @MaNV
end


--------------

------ttram-----


--------------

------my-----


--------------

------cbao-----


--------------