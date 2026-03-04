package BUS;

import DAL.TaiKhoanDAL;
import DTO.TaiKhoan;
import DTO.TaiKhoanDTO;
import Exception.BusException;
import Exception.DaoException;
import java.util.List;

public class TaiKhoanBUS {
    private TaiKhoanDAL taikhoanDAL = new TaiKhoanDAL();

    public TaiKhoan login(String username, String pass) throws BusException {
        if (username == null || username.trim().isEmpty())
            throw new BusException("Tên đăng nhập không được để trống");

        if (pass == null || pass.trim().isEmpty())
            throw new BusException("Mật khẩu không được để trống");

        try {
            List<TaiKhoan> listAccount = taikhoanDAL.getAllAccount();
            for (TaiKhoan acc : listAccount) {
                if (acc.getMaNhanVien().equals(username)
                        && acc.getMatKhau().equals(pass)) {
                    return acc;
                }
            }
        } catch (DaoException e) {
            throw new BusException("Lỗi hệ thống khi đăng nhập: " + e.getMessage());
        }

        throw new BusException("Sai tên đăng nhập hoặc mật khẩu");
    }

    public List<TaiKhoan> getAllTaiKhoan() throws BusException {
        try {
            return taikhoanDAL.getAllAccount();
        } catch (DaoException e) {
            throw new BusException("Lỗi lấy dữ liệu tài khoản: " + e.getMessage());
        }
    }

    public Boolean editTaiKhoan(TaiKhoan tk) throws BusException {
        try {
            return taikhoanDAL.editTaiKhoan(tk);
        } catch (DaoException e) {
            throw new BusException("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }

    public Boolean changeStatus(TaiKhoan tk) throws BusException {
        try {
            tk.setTrangThai(!tk.getTrangThai());
            return taikhoanDAL.unblockAccount(tk);
        } catch (DaoException e) {
            throw new BusException(e.getMessage());
        }
    }

    public List<TaiKhoan> searchTaiKhoan(String maNhanVien, String maNhomQuyen, Boolean trangThai) throws BusException {
        try {
            return taikhoanDAL.searchTaiKhoan(maNhanVien, maNhomQuyen, trangThai);
        } catch (DaoException e) {
            throw new BusException("Không tìm thấy các tài khoản liên quan: " + e.getMessage());
        }

    }
}
