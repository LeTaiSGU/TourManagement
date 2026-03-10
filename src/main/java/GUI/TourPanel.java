
package GUI;

import BUS.CTHDBUS;
import BUS.DiaDiemBUS;
import BUS.HDVBUS;
import BUS.LichTrinhBUS;
import BUS.LoaiTourBUS;
import BUS.PhuongTienBUS;
import BUS.TourBUS;
import DTO.CTCN_NQ;
import DTO.DiaDiem;
import DTO.HuongDanVien;
import DTO.LichTrinh;
import DTO.LoaiTour;
import DTO.PhuongTien;
import DTO.Tour;
import Exception.BusException;
import Helper.LichTrinhExcelReader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.miginfocom.swing.MigLayout;

public class TourPanel extends javax.swing.JPanel {

    private TourBUS tourBUS = new TourBUS();
    private LoaiTourBUS loaiTourBUS = new LoaiTourBUS();
    private LichTrinhBUS lichTrinhBUS = new LichTrinhBUS();
    private HDVBUS hdvBUS = new HDVBUS();
    private DiaDiemBUS diaDiemBUS = new DiaDiemBUS();
    private PhuongTienBUS phuongTienBUS = new PhuongTienBUS();
    private CTHDBUS cthdbus = new CTHDBUS();
    private List<LoaiTour> listLoaiTour = new ArrayList();
    private List<HuongDanVien> listHDV = new ArrayList();
    // Fields cho panel nhập lịch trình
    private GUI.Menu.CustomComboBox cbMaDiaDiem;
    private GUI.Menu.CustomComboBox cbMaPhuongTien;
    private javax.swing.JTextArea txtNoiDung;

    // Fields cho panel thông tin tour
    private GUI.LoginForm.CustomTextField txtMaTour;
    private GUI.Menu.CustomComboBox cbMaLoaiTour;
    private GUI.LoginForm.CustomTextField txtTenTour;
    private GUI.LoginForm.CustomTextField txtNoiKhoiHanh;
    private javax.swing.JSpinner spTgKhoiHanh;
    private javax.swing.JSpinner spTgKetThuc;
    private GUI.LoginForm.CustomTextField txtSoLuongMin;
    private GUI.LoginForm.CustomTextField txtSoLuongVe;
    private GUI.Menu.CustomComboBox cbMaHDV;
    private GUI.LoginForm.CustomTextField txtGiaTour;
    private GUI.Menu.ActionButton btnThemTour;
    private GUI.Menu.ActionButton btnSuaTour;
    private GUI.Menu.ActionButton btnXoaTour;
    private GUI.Menu.ActionButton btnThemLichTrinh;
    private GUI.Menu.ActionButton btnThemExcel;

    public TourPanel(CTCN_NQ ctnq) {
        initComponents();
        initGUI();
        String chiTiet = (ctnq != null && ctnq.getChiTiet() != null) ? ctnq.getChiTiet() : "";
        boolean coQuyenThem = chiTiet.contains("Thêm");
        boolean coQuyenSua = chiTiet.contains("Sửa");
        boolean coQuyenXoa = chiTiet.contains("Xóa");

        btnThemTour.setVisible(coQuyenThem);
        btnSuaTour.setVisible(coQuyenSua);
        btnXoaTour.setVisible(coQuyenXoa);

        btnThemTour.setEnabled(coQuyenThem);
        btnSuaTour.setEnabled(coQuyenSua);
        btnXoaTour.setEnabled(coQuyenXoa);

        btnThemLichTrinh.setVisible(coQuyenThem);
        btnThemExcel.setVisible(coQuyenThem);
    }

    public void initGUI() {

        lbTenAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lbTenAnh.setVerticalAlignment(SwingConstants.CENTER);
        Font popupFont = new Font("Segoe UI", Font.PLAIN, 18);
        popLT.setFont(popupFont);
        itemSua.setFont(popupFont);
        itemXoa.setFont(popupFont);
        customTableHeader(tbTour);
        customTableHeader(tbLichtrinh);
        checkTourDuSoLuongKhoiHanhKhiMoPanel();
        setUpPanelActTour();
        setUpPanelBTNLichtrinh();
        setUpPanelNhapLT();
        setUpPanelTTTour();
        setMaTour();
        try {
            listLoaiTour = loaiTourBUS.getAllLoaiTour();
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        loadComboboxMaLoaiTour();
        try {
            listHDV = hdvBUS.getAllHuongDanVien();
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        loadComboboxMaHDV();
        loadComboboxMaDiaDiem();
        loadComboboxMaPhuongTien();
    }

    private void checkTourDuSoLuongKhoiHanhKhiMoPanel() {
        try {
            tourBUS.checkKhoiHanhHomNay();
            List<Tour> danhSachTourChuaKhoiHanh = tourBUS.getTourByMaTourChuaKhoiHanh();
            int soTourBiHuy = 0;
            for (Tour tour : danhSachTourChuaKhoiHanh) {
                int daCapNhat = tourBUS.checkTourDuSoLuongKhoiHanh(tour.getMaTour());
                if (daCapNhat == 1) {
                    try {
                        if (tourBUS.huyTourByAdmin(tour.getMaTour(), "Hủy do không đủ số lượng khởi hành")) {
                            cthdbus.setUpCTHD(tour.getMaTour(), "Hủy do không đủ số lượng khởi hành", true);
                            cthdbus.thietLapHoanTien(tour.getMaTour(), "Hủy do không đủ số lượng khởi hành");
                            soTourBiHuy++;
                        } else {
                            return;
                        }
                    } catch (BusException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            loadTableTour();
            if (soTourBiHuy > 0) {
                JOptionPane.showMessageDialog(this,
                        "Đã cập nhật trạng thái " + soTourBiHuy + " tour do không đủ số lượng khởi hành.");
            }
        } catch (BusException ex) {
            ex.printStackTrace();
        }
    }

    public void setMaTour() {
        try {
            String ma = tourBUS.getLastMa();
            txtMaTour.setText(ma);
        } catch (BusException exx) {
            JOptionPane.showMessageDialog(null, exx.getMessage());
        }
    }

    public void loadComboboxMaLoaiTour() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("...");
        for (LoaiTour lt : listLoaiTour) {
            model.addElement(lt.getTenLoai());
        }
        cbMaLoaiTour.setModel(model);

    }

    public void loadComboboxMaHDV() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("...");
        for (HuongDanVien hdv : listHDV) {
            model.addElement(hdv.getTenHDV());
        }
        cbMaHDV.setModel(model);
    }

    public void loadTableTour() {
        try {
            List<Tour> listTour = tourBUS.getAllTour();
            DefaultTableModel model1 = new DefaultTableModel(
                    new Object[] { "Mã Tour", "Mã loại Tour", "Tên Tour", "Khởi hành", "Trạng thái" }, 0);
            for (Tour tr : listTour) {

                model1.addRow(new Object[] {
                        tr.getMaTour(),
                        tr.getMaLoaiTour(),
                        tr.getTenTour(),
                        tr.getKhoiHanh() ? "Đã khởi hành" : "Chưa khởi hành",
                        tr.getTrangThai() ? "Hoạt động" : "Đã hủy"
                });
            }
            tbTour.setModel(model1);
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + ex);
        }
    }

    public void loadComboboxMaDiaDiem() {
        try {
            List<DiaDiem> listDiaDiem = diaDiemBUS.getAllDiaDiemTai();
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            model.addElement("...");
            for (DiaDiem dd : listDiaDiem) {
                model.addElement(dd.getTenDiaDiem());
            }
            cbMaDiaDiem.setModel(model);
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void loadComboboxMaPhuongTien() {
        try {
            List<PhuongTien> listPhuongTien = phuongTienBUS.getAllPhuongTien();
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            model.addElement("...");
            for (PhuongTien pt : listPhuongTien) {
                model.addElement(pt.getTenPT());
            }
            cbMaPhuongTien.setModel(model);
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void setUpPanelNhapLT() {
        panelTTLichtrinh.setBackground(Color.white);
        panelTTLichtrinh.setLayout(new MigLayout(
                "insets 16, gap 10 12",
                "[180px, fill][grow, fill]",
                "[]12[]12[]"));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 18);

        // --- Cột 1: Mã địa điểm ---
        javax.swing.JLabel lblDiaDiem = new javax.swing.JLabel("Mã địa điểm");
        lblDiaDiem.setFont(labelFont);

        cbMaDiaDiem = new GUI.Menu.CustomComboBox();
        cbMaDiaDiem.setFont(inputFont);

        // --- Cột 1: Mã phương tiện ---
        javax.swing.JLabel lblPhuongTien = new javax.swing.JLabel("Mã phương tiện");
        lblPhuongTien.setFont(labelFont);

        cbMaPhuongTien = new GUI.Menu.CustomComboBox();
        cbMaPhuongTien.setFont(inputFont);

        // --- Cột 2: Nội dung (span 2 hàng) ---
        javax.swing.JLabel lblNoiDung = new javax.swing.JLabel("Nội dung");
        lblNoiDung.setFont(labelFont);

        txtNoiDung = new javax.swing.JTextArea();
        txtNoiDung.setFont(inputFont);
        txtNoiDung.setLineWrap(true);
        txtNoiDung.setWrapStyleWord(true);
        txtNoiDung.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        javax.swing.JScrollPane scrollNoiDung = new javax.swing.JScrollPane(txtNoiDung);
        scrollNoiDung.setBorder(null);

        // Add vào panel
        panelTTLichtrinh.add(lblDiaDiem, "");
        panelTTLichtrinh.add(lblNoiDung, "wrap");

        panelTTLichtrinh.add(cbMaDiaDiem, "");
        panelTTLichtrinh.add(scrollNoiDung, "spany 3, grow, wrap");

        panelTTLichtrinh.add(lblPhuongTien, "wrap");
        panelTTLichtrinh.add(cbMaPhuongTien, "");
    }

    public void setUpPanelTTTour() {
        panelTTTour.setBackground(Color.white);
        panelTTTour.setLayout(new MigLayout(
                "insets 20 30 20 30, gap 10 14",
                "[130px, right][grow, fill, sg col]40[130px, right][grow, fill, sg col]",
                "[grow][grow][grow][grow][grow]"));

        Font lblFont = new Font("Segoe UI", Font.BOLD, 15);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);
        javax.swing.border.Border spinnerBorder = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 4));

        txtMaTour = new GUI.LoginForm.CustomTextField();
        txtMaTour.setFont(inputFont);
        txtMaTour.setPlaceholder("Tự động tạo");
        txtMaTour.setEditable(false);
        txtMaTour.setBackgroundColor(new Color(245, 245, 245));

        cbMaLoaiTour = new GUI.Menu.CustomComboBox();
        cbMaLoaiTour.setFont(inputFont);

        txtTenTour = new GUI.LoginForm.CustomTextField();
        txtTenTour.setFont(inputFont);
        txtTenTour.setPlaceholder("Nhập tên tour...");

        txtNoiKhoiHanh = new GUI.LoginForm.CustomTextField();
        txtNoiKhoiHanh.setFont(inputFont);
        txtNoiKhoiHanh.setPlaceholder("Nhập nơi khởi hành...");

        spTgKhoiHanh = new javax.swing.JSpinner(new javax.swing.SpinnerDateModel());
        spTgKhoiHanh.setEditor(new javax.swing.JSpinner.DateEditor(spTgKhoiHanh, "dd/MM/yyyy"));
        spTgKhoiHanh.setFont(inputFont);
        spTgKhoiHanh.setBorder(spinnerBorder);

        spTgKetThuc = new javax.swing.JSpinner(new javax.swing.SpinnerDateModel());
        spTgKetThuc.setEditor(new javax.swing.JSpinner.DateEditor(spTgKetThuc, "dd/MM/yyyy"));
        spTgKetThuc.setFont(inputFont);
        spTgKetThuc.setBorder(spinnerBorder);
        spTgKetThuc.setEnabled(false);

        txtSoLuongMin = new GUI.LoginForm.CustomTextField();
        txtSoLuongMin.setFont(inputFont);
        txtSoLuongMin.setPlaceholder("Số lượng tối thiểu...");

        txtSoLuongVe = new GUI.LoginForm.CustomTextField();
        txtSoLuongVe.setFont(inputFont);
        txtSoLuongVe.setPlaceholder("Tổng số vé...");

        txtGiaTour = new GUI.LoginForm.CustomTextField();
        txtGiaTour.setFont(inputFont);
        txtGiaTour.setPlaceholder("Giá tour...");

        cbMaHDV = new GUI.Menu.CustomComboBox();
        cbMaHDV.setFont(inputFont);

        // Hàng 1: Mã tour | Tên tour
        panelTTTour.add(makeLabel("Mã tour", lblFont));
        panelTTTour.add(txtMaTour);
        panelTTTour.add(makeLabel("Tên tour", lblFont));
        panelTTTour.add(txtTenTour, "wrap");

        // Hàng 2: Mã loại tour | Nơi khởi hành
        panelTTTour.add(makeLabel("Mã loại tour", lblFont));
        panelTTTour.add(cbMaLoaiTour);
        panelTTTour.add(makeLabel("Nơi khởi hành", lblFont));
        panelTTTour.add(txtNoiKhoiHanh, "wrap");

        // Hàng 3: HDV | Số lượng vé
        panelTTTour.add(makeLabel("Hướng dẫn viên", lblFont));
        panelTTTour.add(cbMaHDV);
        panelTTTour.add(makeLabel("Số lượng vé", lblFont));
        panelTTTour.add(txtSoLuongVe, "wrap");

        // Hàng 4: TG khởi hành | TG kết thúc
        panelTTTour.add(makeLabel("TG khởi hành", lblFont));
        panelTTTour.add(spTgKhoiHanh);
        panelTTTour.add(makeLabel("TG kết thúc", lblFont));
        panelTTTour.add(spTgKetThuc, "wrap");

        // Hàng 5: SL vé tối thiểu | Giá tour
        panelTTTour.add(makeLabel("SL vé tối thiểu", lblFont));
        panelTTTour.add(txtSoLuongMin);
        panelTTTour.add(makeLabel("Giá tour", lblFont));
        panelTTTour.add(txtGiaTour, "wrap");
    }

    private JLabel makeLabel(String text, Font font) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(font);
        return lbl;
    }

    public void setUpPanelBTNLichtrinh() {
        jPanel6.setBackground(Color.white);
        jPanel6.setLayout(new MigLayout(
                "insets 25, gap 20",
                "[grow, fill][grow, fill]"));
        java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 25);
        java.awt.Dimension btnSize = new java.awt.Dimension(200, 55);

        btnThemLichTrinh = new GUI.Menu.ActionButton();
        btnThemLichTrinh.setText("Thêm");
        btnThemLichTrinh.setFont(btnFont);
        btnThemLichTrinh.setPreferredSize(btnSize);
        btnThemLichTrinh.setColorTop(new Color(39, 174, 96));
        btnThemLichTrinh.setColorBottom(new Color(27, 136, 73));
        btnThemLichTrinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/plus.png")));
        btnThemLichTrinh.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnThemLTMouseClicked(e);
            }
        });

        btnThemExcel = new GUI.Menu.ActionButton();
        btnThemExcel.setText("Thêm excel");
        btnThemExcel.setFont(btnFont);
        btnThemExcel.setPreferredSize(btnSize);
        btnThemExcel.setColorTop(new Color(39, 174, 96));
        btnThemExcel.setColorBottom(new Color(27, 136, 73));
        btnThemExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/excel.png")));
        btnThemExcel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnThemExcelMouseClicked(e);
            }
        });

        jPanel6.add(btnThemLichTrinh, "grow");
        jPanel6.add(btnThemExcel, "grow");
    }

    private void btnThemExcelMouseClicked(java.awt.event.MouseEvent e) {
        if (btnThemExcel != null && !btnThemExcel.isEnabled()) {
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("D:\\"));
        fileChooser.setDialogTitle("Chọn file Excel lịch trình");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel (*.xlsx, *.xls)", "xlsx", "xls"));

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();
        String maTour = txtMaTour.getText().trim();
        int success = 0;
        int failed = 0;
        StringBuilder failedDetail = new StringBuilder();

        try {
            List<LichTrinhExcelReader.ExcelLichTrinhRow> rows = LichTrinhExcelReader.read(file);
            if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(this, "File Excel không có dữ liệu hợp lệ.");
                return;
            }

            for (LichTrinhExcelReader.ExcelLichTrinhRow row : rows) {
                String maDiaDiem = row.getTenDiaDiem() != null ? row.getTenDiaDiem().trim() : null;
                String maPhuongTien = row.getTenPhuongTien() != null ? row.getTenPhuongTien().trim() : null;
                String noiDung = row.getNoiDung() != null ? row.getNoiDung().trim() : null;

                if (maDiaDiem == null || maDiaDiem.isEmpty()
                        || maPhuongTien == null || maPhuongTien.isEmpty()
                        || noiDung == null || noiDung.isEmpty()
                        || "...".equals(maDiaDiem)
                        || "...".equals(maPhuongTien)) {
                    failed++;
                    failedDetail.append("- Dòng ").append(failed + success).append(": thiếu dữ liệu bắt buộc.\n");
                    continue;
                }

                // Import excel dùng trực tiếp mã địa điểm/mã phương tiện từ file.
                try {
                    if (diaDiemBUS.getTenDiaDiemByMa(maDiaDiem) == null
                            || phuongTienBUS.getTenPhuongTienByMa(maPhuongTien) == null) {
                        failed++;
                        failedDetail.append("- Dòng ").append(failed + success)
                                .append(": mã địa điểm/mã phương tiện không tồn tại.\n");
                        continue;
                    }
                } catch (BusException ex) {
                    failed++;
                    failedDetail.append("- Dòng ").append(failed + success)
                            .append(": lỗi kiểm tra mã địa điểm/mã phương tiện.\n");
                    continue;
                }

                String maLichTrinh = generateNextMaLichTrinh();
                addLichTrinhToTable(maLichTrinh, maTour, maDiaDiem, maPhuongTien, noiDung);
                success++;
            }

            String message = "Import Excel hoàn tất.\nThành công: " + success + "\nBỏ qua: " + failed;
            if (failed > 0) {
                message += "\n\nChi tiết bỏ qua:\n" + failedDetail;
            }
            JOptionPane.showMessageDialog(this, message);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể đọc file Excel: " + ex.getMessage());
        }
    }

    private void btnThemLTMouseClicked(java.awt.event.MouseEvent e) {
        if (btnThemLichTrinh != null && !btnThemLichTrinh.isEnabled()) {
            return;
        }
        try {
            String maTour = txtMaTour.getText().trim();
            String noiDung = txtNoiDung.getText().trim();

            String tenDiaDiem = cbMaDiaDiem.getSelectedItem() != null ? cbMaDiaDiem.getSelectedItem().toString() : null;
            String tenPhuongTien = cbMaPhuongTien.getSelectedItem() != null
                    ? cbMaPhuongTien.getSelectedItem().toString()
                    : null;

            if ("...".equals(tenDiaDiem)) {
                tenDiaDiem = null;
            }
            if ("...".equals(tenPhuongTien)) {
                tenPhuongTien = null;
            }
            if (noiDung.isEmpty()) {
                noiDung = null;
            }

            String maDiaDiem = tenDiaDiem != null ? diaDiemBUS.getMaDiaDiemByTen(tenDiaDiem) : null;
            String maPhuongTien = tenPhuongTien != null ? phuongTienBUS.getMaPhuongTienByTen(tenPhuongTien) : null;

            if (maDiaDiem == null || maPhuongTien == null || noiDung == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn địa điểm, phương tiện và nhập nội dung.");
                return;
            }
            String maLichTrinh = generateNextMaLichTrinh();
            addLichTrinhToTable(maLichTrinh, maTour, maDiaDiem, maPhuongTien, noiDung);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private String generateNextMaLichTrinh() throws BusException {
        DefaultTableModel model = (DefaultTableModel) tbLichtrinh.getModel();
        if (model.getRowCount() == 0) {
            return lichTrinhBUS.getLastMaLichTrinh();
        }

        Object lastValue = model.getValueAt(model.getRowCount() - 1, 0);
        if (lastValue == null) {
            return lichTrinhBUS.getLastMaLichTrinh();
        }

        String lastMa = lastValue.toString().trim();
        if (lichTrinhBUS.checkMaLT(lastMa)) {
            return lichTrinhBUS.getLastMaLichTrinh();
        }
        if (!lastMa.startsWith("LT") || lastMa.length() < 3) {
            return lichTrinhBUS.getLastMaLichTrinh();
        }

        try {
            int soThuTu = Integer.parseInt(lastMa.substring(2)) + 1;
            return String.format("LT%03d", soThuTu);
        } catch (NumberFormatException ex) {
            return lichTrinhBUS.getLastMaLichTrinh();
        }
    }

    private void addLichTrinhToTable(String maLichTrinh, String maTour, String maDiaDiem, String maPhuongTien,
            String noiDung) {
        DefaultTableModel model = (DefaultTableModel) tbLichtrinh.getModel();
        int ngayThu = model.getRowCount() + 1;

        model.addRow(new Object[] {
                maLichTrinh,
                maTour,
                maDiaDiem,
                maPhuongTien,
                ngayThu,
                noiDung
        });
        tbLichtrinh.setModel(model);
        updateNgayKetThucByLichTrinhCount();
    }

    private void updateNgayKetThucByLichTrinhCount() {
        Object startValue = spTgKhoiHanh.getValue();
        if (!(startValue instanceof java.util.Date)) {
            return;
        }

        int soNgayLichTrinh = tbLichtrinh.getRowCount();
        LocalDate ngayKhoiHanh = ((java.util.Date) startValue)
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        LocalDate ngayKetThuc = ngayKhoiHanh.plusDays(soNgayLichTrinh);
        spTgKetThuc.setValue(Date.valueOf(ngayKetThuc));
    }

    public void setUpPanelActTour() {
        panelActionTour.setBackground(Color.white);
        panelActionTour.setLayout(new MigLayout(
                "insets 25, gap 20",
                "[grow, fill][grow, fill][grow, fill]",
                "[grow, fill][grow, fill]"));

        java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 25);
        java.awt.Dimension btnSize = new java.awt.Dimension(200, 55);

        btnThemTour = new GUI.Menu.ActionButton();
        btnThemTour.setText("Thêm");
        btnThemTour.setFont(btnFont);
        btnThemTour.setPreferredSize(btnSize);
        btnThemTour.setColorTop(new Color(39, 174, 96));
        btnThemTour.setColorBottom(new Color(27, 136, 73));
        btnThemTour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/plus.png")));
        btnThemTour.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnThemTourMouseClicked(e);
            }
        });

        btnSuaTour = new GUI.Menu.ActionButton();
        btnSuaTour.setText("Sửa");
        btnSuaTour.setFont(btnFont);
        btnSuaTour.setPreferredSize(btnSize);
        btnSuaTour.setColorTop(new Color(243, 156, 18));
        btnSuaTour.setColorBottom(new Color(211, 128, 9));
        btnSuaTour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/save.png")));
        btnSuaTour.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnSuaTourMouseClicked(e);
            }
        });

        btnXoaTour = new GUI.Menu.ActionButton();
        btnXoaTour.setText("Hủy Tour");
        btnXoaTour.setFont(btnFont);
        btnXoaTour.setPreferredSize(btnSize);
        btnXoaTour.setColorTop(new Color(231, 76, 60));
        btnXoaTour.setColorBottom(new Color(192, 47, 34));
        btnXoaTour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/close.png")));
        btnXoaTour.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnXoaTourMouseClicked(e);
            }
        });

        GUI.Menu.ActionButton btnLamMoi = new GUI.Menu.ActionButton();
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setFont(btnFont);
        btnLamMoi.setPreferredSize(btnSize);
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload2.png")));
        btnLamMoi.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnLamMoiTourMouseClicked(e);
            }
        });

        GUI.Menu.ActionButton btnTimKiem = new GUI.Menu.ActionButton();
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.setFont(btnFont);
        btnTimKiem.setPreferredSize(btnSize);
        btnTimKiem.setColorTop(new Color(149, 165, 166));
        btnTimKiem.setColorBottom(new Color(127, 140, 141));
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/search2.png")));
        btnTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnTimKiemTourMouseClicked(e);
            }
        });

        panelActionTour.add(btnThemTour, "grow");
        panelActionTour.add(btnSuaTour, "grow");
        panelActionTour.add(btnTimKiem, "grow, wrap");
        panelActionTour.add(btnXoaTour, "grow");
        panelActionTour.add(btnLamMoi, "grow");
    }

    private void btnThemTourMouseClicked(java.awt.event.MouseEvent e) {
        if (btnThemTour != null && !btnThemTour.isEnabled()) {
            return;
        }
        try {
            String tenLoai = cbMaLoaiTour.getSelectedItem() != null ? cbMaLoaiTour.getSelectedItem().toString() : "";
            String maLoaiTour = listLoaiTour.stream()
                    .filter(lt -> lt.getTenLoai().equals(tenLoai))
                    .map(LoaiTour::getMaLoaiTour)
                    .findFirst()
                    .orElse(null);

            String tenHDV = cbMaHDV.getSelectedItem() != null ? cbMaHDV.getSelectedItem().toString() : "";
            String maHDV = listHDV.stream()
                    .filter(hdv -> hdv.getTenHDV().equals(tenHDV))
                    .map(HuongDanVien::getMaHDV)
                    .findFirst()
                    .orElse(null);

            LocalDate tgKhoiHanh = ((java.util.Date) spTgKhoiHanh.getValue())
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate tgKetThuc = ((java.util.Date) spTgKetThuc.getValue())
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String tenAnh = lbTenAnh.getText() != null ? lbTenAnh.getText().trim() : "";
            if (tenAnh.equals("...")) {
                JOptionPane.showMessageDialog(this, "Ảnh tour không được để trống.");
                return;
            }

            int row = tbLichtrinh.getRowCount();
            if (row == 0) {
                JOptionPane.showMessageDialog(this, "Chưa tạo lịch trình cho Tour.");
                return;
            }
            Tour tour = Tour.builder()
                    .maTour(txtMaTour.getText().trim().isEmpty() ? tourBUS.getLastMa() : txtMaTour.getText().trim())
                    .maLoaiTour(maLoaiTour)
                    .maHDV(maHDV)
                    .tenTour(txtTenTour.getText().trim())
                    .anhTour("...".equals(tenAnh) ? null : tenAnh)
                    .noiKhoiHanh(txtNoiKhoiHanh.getText().trim())
                    .tgKhoiHanh(tgKhoiHanh)
                    .tgKetThuc(tgKetThuc)
                    .giaTour(Double.parseDouble(txtGiaTour.getText().trim()))
                    .soLuongVe(Integer.parseInt(txtSoLuongVe.getText().trim()))
                    .soLuongMin(Integer.parseInt(txtSoLuongMin.getText().trim()))
                    .khoiHanh(false)
                    .trangThai(true)
                    .build();

            if (tourBUS.insertTour(tour)) {
                DefaultTableModel modelLichTrinh = (DefaultTableModel) tbLichtrinh.getModel();
                for (int i = 0; i < modelLichTrinh.getRowCount(); i++) {
                    String maLichTrinh = modelLichTrinh.getValueAt(i, 0).toString();
                    String maTourLT = modelLichTrinh.getValueAt(i, 1).toString();
                    String maDiaDiem = modelLichTrinh.getValueAt(i, 2).toString();
                    String maPhuongTien = modelLichTrinh.getValueAt(i, 3).toString();
                    int ngayThu = Integer.parseInt(modelLichTrinh.getValueAt(i, 4).toString());
                    String noiDungLT = modelLichTrinh.getValueAt(i, 5).toString();
                    insertLichTrinh(maLichTrinh, maTourLT, maDiaDiem, maPhuongTien, ngayThu, noiDungLT, true);
                }

                JOptionPane.showMessageDialog(this, "Thêm tour và lịch trình thành công.");
                loadTableTour();
                btnLamMoiTourMouseClicked(null);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm tour.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng/Giá tour không đúng định dạng.");
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public void insertLichTrinh(String maLichTrinh, String maTour, String maDiaDiem, String maPhuongTien, int ngayThu,
            String noiDung, boolean trangThai) throws BusException {
        LichTrinh lichTrinh = LichTrinh.builder()
                .maLichTrinh(maLichTrinh)
                .maTour(maTour)
                .maDiaDiem(maDiaDiem)
                .maPT(maPhuongTien)
                .ngayThu(ngayThu)
                .noiDung(noiDung)
                .trangThai(trangThai)
                .build();
        if (!lichTrinhBUS.insertLichTrinh(lichTrinh)) {
            throw new BusException("Không thể thêm lịch trình: " + maLichTrinh);
        }
    }

    private void editLichTrinh(LichTrinh lichTrinh) throws BusException {
        try {
            if (lichTrinhBUS.checkMaLT(lichTrinh.getMaLichTrinh())) {
                lichTrinh.setTrangThai(true);
                lichTrinhBUS.editLichTrinh(lichTrinh);
            } else {
                lichTrinh.setTrangThai(true);
                lichTrinhBUS.insertLichTrinh(lichTrinh);
            }
        } catch (BusException ex) {
            throw new BusException("Không thể sửa lịch trình: " + lichTrinh.getMaLichTrinh());
        }
    }

    private void btnSuaTourMouseClicked(java.awt.event.MouseEvent e) {
        if (btnSuaTour != null && !btnSuaTour.isEnabled()) {
            return;
        }
        try {
            String tenLoai = cbMaLoaiTour.getSelectedItem() != null ? cbMaLoaiTour.getSelectedItem().toString() : "";
            String maLoaiTour = listLoaiTour.stream()
                    .filter(lt -> lt.getTenLoai().equals(tenLoai))
                    .map(LoaiTour::getMaLoaiTour)
                    .findFirst()
                    .orElse(null);

            String tenHDV = cbMaHDV.getSelectedItem() != null ? cbMaHDV.getSelectedItem().toString() : "";
            String maHDV = listHDV.stream()
                    .filter(hdv -> hdv.getTenHDV().equals(tenHDV))
                    .map(HuongDanVien::getMaHDV)
                    .findFirst()
                    .orElse(null);
            LocalDate tgKhoiHanh = ((java.util.Date) spTgKhoiHanh.getValue())
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int soNgayLichTrinh = tbLichtrinh.getRowCount();
            LocalDate tgKetThuc = tgKhoiHanh.plusDays(soNgayLichTrinh);

            Tour tour = Tour.builder()
                    .maTour(txtMaTour.getText().trim())
                    .maLoaiTour(maLoaiTour)
                    .maHDV(maHDV)
                    .tenTour(txtTenTour.getText().trim())
                    .anhTour(lbTenAnh.getText() != null ? lbTenAnh.getText().trim() : "")
                    .noiKhoiHanh(txtNoiKhoiHanh.getText().trim())
                    .tgKhoiHanh(tgKhoiHanh)
                    .tgKetThuc(tgKetThuc)
                    .giaTour(Double.parseDouble(txtGiaTour.getText().trim()))
                    .soLuongVe(Integer.parseInt(txtSoLuongVe.getText().trim()))
                    .soLuongMin(Integer.parseInt(txtSoLuongMin.getText().trim()))
                    .build();
            if (tourBUS.editTour(tour)) {
                lichTrinhBUS.dropLichTrinh(tour.getMaTour());
                DefaultTableModel modelLichTrinh = (DefaultTableModel) tbLichtrinh.getModel();
                for (int i = 0; i < modelLichTrinh.getRowCount(); i++) {
                    LichTrinh lichTrinh = LichTrinh.builder()
                            .maLichTrinh(modelLichTrinh.getValueAt(i, 0).toString())
                            .maTour(tour.getMaTour())
                            .maDiaDiem(modelLichTrinh.getValueAt(i, 2).toString())
                            .maPT(modelLichTrinh.getValueAt(i, 3).toString())
                            .ngayThu(Integer.parseInt(modelLichTrinh.getValueAt(i, 4).toString()))
                            .noiDung(modelLichTrinh.getValueAt(i, 5).toString())
                            .build();
                    editLichTrinh(lichTrinh);
                }
                loadTableTour();
                loadTableLichTrinh(tour.getMaTour());
                JOptionPane.showMessageDialog(this, "Sửa tour thành công.");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể sửa tour.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng/Giá tour không đúng định dạng.");
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void btnXoaTourMouseClicked(java.awt.event.MouseEvent e) {
        if (btnXoaTour != null && !btnXoaTour.isEnabled()) {
            return;
        }
        int row = tbTour.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tour cần hủy.");
            return;
        }

        String maTour = tbTour.getValueAt(row, 0) != null ? tbTour.getValueAt(row, 0).toString().trim() : "";
        String trangThai = tbTour.getValueAt(row, 4) != null ? tbTour.getValueAt(row, 4).toString().trim() : "";
        if ("Đã hủy".equalsIgnoreCase(trangThai) || "Đã hủy".equalsIgnoreCase(trangThai)) {
            JOptionPane.showMessageDialog(this, "Tour này đã ở trạng thái hủy.");
            return;
        }

        try {
            Tour tour = tourBUS.getTourByMaTour(maTour);
            if (tour != null && tour.getTgKhoiHanh() != null) {
                LocalDate today = LocalDate.now();
                LocalDate tgKhoiHanh = tour.getTgKhoiHanh();
                LocalDate oneWeekLater = today.plusDays(7);
                boolean sapKhoiHanh = (!tgKhoiHanh.isBefore(today)) && (!tgKhoiHanh.isAfter(oneWeekLater));
                if (sapKhoiHanh) {
                    Font oldMessageFont = javax.swing.UIManager.getFont("OptionPane.messageFont");
                    Font oldButtonFont = javax.swing.UIManager.getFont("OptionPane.buttonFont");
                    UIManager.put("OptionPane.messageFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 20));
                    UIManager.put("OptionPane.buttonFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 20));
                    int confirmSoon = JOptionPane.showConfirmDialog(
                            this,
                            "Tour sắp khởi hành. Bạn có chắc muốn tiếp tục hủy?",
                            "Xác nhận hủy tour sắp khởi hành",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    UIManager.put("OptionPane.messageFont", oldMessageFont);
                    UIManager.put("OptionPane.buttonFont", oldButtonFont);
                    if (confirmSoon != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }

        GUI.Dialog.HuyTourDialog huyTourPanel = new GUI.Dialog.HuyTourDialog();
        huyTourPanel.setOnConfirm(lyDoHuy -> {
            try {
                if (tourBUS.huyTourByAdmin(maTour, lyDoHuy)) {
                    cthdbus.setUpCTHD(maTour, lyDoHuy, true);

                    cthdbus.thietLapHoanTien(maTour, lyDoHuy);
                    JOptionPane.showMessageDialog(this, "Hủy tour thành công.");
                    loadTableTour();
                    btnLamMoiTourMouseClicked(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể hủy tour.");
                }
            } catch (BusException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        JDialog dialog = new JDialog(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                "Hủy tour",
                true);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(huyTourPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void btnLamMoiTourMouseClicked(java.awt.event.MouseEvent e) {
        txtTenTour.setText("");
        txtNoiKhoiHanh.setText("");
        txtSoLuongVe.setText("");
        txtSoLuongMin.setText("");
        txtGiaTour.setText("");

        if (cbMaLoaiTour.getItemCount() > 0) {
            cbMaLoaiTour.setSelectedIndex(0);
        }
        if (cbMaHDV.getItemCount() > 0) {
            cbMaHDV.setSelectedIndex(0);
        }
        if (cbMaDiaDiem != null && cbMaDiaDiem.getItemCount() > 0) {
            cbMaDiaDiem.setSelectedIndex(0);
        }
        if (cbMaPhuongTien != null && cbMaPhuongTien.getItemCount() > 0) {
            cbMaPhuongTien.setSelectedIndex(0);
        }

        java.util.Date now = new java.util.Date();
        spTgKhoiHanh.setValue(now);
        spTgKetThuc.setValue(now);
        if (txtNoiDung != null) {
            txtNoiDung.setText("");
        }

        lbAnhTour.setIcon(null);
        lbAnhTour.setText("...");
        lbTenAnh.setText("...");
        if (btnSuaTour != null) {
            btnSuaTour.setEnabled(true);
        }
        if (btnXoaTour != null) {
            btnXoaTour.setEnabled(true);
        }
        if (btnThemTour != null) {
            btnThemTour.setEnabled(true);
        }
        if (btnThemLichTrinh != null) {
            btnThemLichTrinh.setEnabled(true);
        }
        if (btnThemExcel != null) {
            btnThemExcel.setEnabled(true);
        }
        itemSua.setEnabled(true);
        itemXoa.setEnabled(true);

        DefaultTableModel modelLt = (DefaultTableModel) tbLichtrinh.getModel();
        modelLt.setRowCount(0);
        checkTourDuSoLuongKhoiHanhKhiMoPanel();
        setMaTour();
    }

    private void btnTimKiemTourMouseClicked(java.awt.event.MouseEvent e) {
        GUI.Dialog.TourSearchDialog searchPanel = new GUI.Dialog.TourSearchDialog();
        searchPanel.setOnSearch(this::timKiemTourNangCao);

        javax.swing.JDialog dialog = new javax.swing.JDialog(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                "Tìm kiếm tour",
                true);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(searchPanel);
        dialog.pack();
        dialog.setSize(1200, 430);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void timKiemTourNangCao(GUI.Dialog.TourSearchDialog.SearchCriteria criteria) {

        try {
            ArrayList<Tour> listTour = tourBUS.searchTour(criteria);
            DefaultTableModel modelSearch = new DefaultTableModel(
                    new Object[] { "Mã Tour", "Mã loại Tour", "Tên Tour", "Khởi hành", "Trạng thái" }, 0);
            for (Tour tour : listTour) {
                modelSearch.addRow(new Object[] {
                        tour.getMaTour(),
                        tour.getMaLoaiTour(),
                        tour.getTenTour(),
                        tour.getKhoiHanh() ? "Đã khởi hành" : "Chưa khởi hành",
                        tour.getTrangThai() ? "Hoạt động" : "Đã huỷ"
                });
            }
            tbTour.setModel(modelSearch);
            customTableHeader(tbTour);
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void customTableHeader(javax.swing.JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBackground(new Color(41, 128, 185));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 60)));
                setOpaque(true);
                return this;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popLT = new javax.swing.JPopupMenu();
        itemSua = new javax.swing.JMenuItem();
        itemXoa = new javax.swing.JMenuItem();
        tabbedPaneCustom1 = new GUI.Menu.TabbedPaneCustom();
        jPanel2 = new javax.swing.JPanel();
        panelTT = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        panelTenAnh = new javax.swing.JPanel();
        lbTenAnh = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lbAnhTour = new javax.swing.JLabel();
        panelTTTour = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panelActionTour = new javax.swing.JPanel();
        jScrollPane1 = new GUI.ScrollPane.ScrollPaneWin11();
        tbTour = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        panelActionLT = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        panelTTLichtrinh = new javax.swing.JPanel();
        jScrollPane2 = new GUI.ScrollPane.ScrollPaneWin11();
        tbLichtrinh = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();

        popLT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        itemSua.setText("Sửa");
        itemSua.addActionListener(this::itemSuaActionPerformed);
        popLT.add(itemSua);

        itemXoa.setText("Xóa");
        itemXoa.addActionListener(this::itemXoaActionPerformed);
        popLT.add(itemXoa);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        tabbedPaneCustom1.setBackground(new java.awt.Color(255, 255, 255));
        tabbedPaneCustom1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tabbedPaneCustom1.setSelectedColor(new java.awt.Color(51, 153, 255));

        jPanel2.setLayout(new java.awt.BorderLayout());

        panelTT.setBackground(new java.awt.Color(255, 255, 255));
        panelTT.setPreferredSize(new java.awt.Dimension(1315, 330));
        panelTT.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 204));
        jPanel7.setPreferredSize(new java.awt.Dimension(450, 330));
        jPanel7.setLayout(new java.awt.BorderLayout());

        panelTenAnh.setBackground(new java.awt.Color(255, 255, 255));
        panelTenAnh.setPreferredSize(new java.awt.Dimension(500, 40));
        panelTenAnh.setLayout(new java.awt.GridLayout(1, 0));

        lbTenAnh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTenAnh.setText("...");
        panelTenAnh.add(lbTenAnh);

        jPanel7.add(panelTenAnh, java.awt.BorderLayout.PAGE_END);

        jPanel12.setBackground(new java.awt.Color(204, 204, 204));
        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        lbAnhTour.setBackground(new java.awt.Color(204, 204, 204));
        lbAnhTour.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbAnhTourMouseClicked(evt);
            }
        });
        jPanel12.add(lbAnhTour);

        jPanel7.add(jPanel12, java.awt.BorderLayout.CENTER);

        panelTT.add(jPanel7, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout panelTTTourLayout = new javax.swing.GroupLayout(panelTTTour);
        panelTTTour.setLayout(panelTTTourLayout);
        panelTTTourLayout.setHorizontalGroup(
                panelTTTourLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 865, Short.MAX_VALUE));
        panelTTTourLayout.setVerticalGroup(
                panelTTTourLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 330, Short.MAX_VALUE));

        panelTT.add(panelTTTour, java.awt.BorderLayout.CENTER);

        jPanel2.add(panelTT, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));
        jPanel4.setLayout(new java.awt.BorderLayout());

        panelActionTour.setPreferredSize(new java.awt.Dimension(657, 200));

        javax.swing.GroupLayout panelActionTourLayout = new javax.swing.GroupLayout(panelActionTour);
        panelActionTour.setLayout(panelActionTourLayout);
        panelActionTourLayout.setHorizontalGroup(
                panelActionTourLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 657, Short.MAX_VALUE));
        panelActionTourLayout.setVerticalGroup(
                panelActionTourLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 200, Short.MAX_VALUE));

        jPanel4.add(panelActionTour, java.awt.BorderLayout.PAGE_END);

        tbTour.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tbTour.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        tbTour.setRowHeight(24);
        tbTour.setShowGrid(false);
        tbTour.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTourMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbTour);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        panelActionLT.setBackground(new java.awt.Color(255, 255, 255));
        panelActionLT.setPreferredSize(new java.awt.Dimension(657, 300));
        panelActionLT.setLayout(new java.awt.BorderLayout());

        jPanel6.setPreferredSize(new java.awt.Dimension(657, 80));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 657, Short.MAX_VALUE));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 80, Short.MAX_VALUE));

        panelActionLT.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout panelTTLichtrinhLayout = new javax.swing.GroupLayout(panelTTLichtrinh);
        panelTTLichtrinh.setLayout(panelTTLichtrinhLayout);
        panelTTLichtrinhLayout.setHorizontalGroup(
                panelTTLichtrinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 657, Short.MAX_VALUE));
        panelTTLichtrinhLayout.setVerticalGroup(
                panelTTLichtrinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 220, Short.MAX_VALUE));

        panelActionLT.add(panelTTLichtrinh, java.awt.BorderLayout.CENTER);

        jPanel5.add(panelActionLT, java.awt.BorderLayout.PAGE_END);

        tbLichtrinh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tbLichtrinh.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã LT", "Mã Tour", "Mã Địa Điểm", "Mã PT", "Ngày thứ", "Nội dung"
                }));
        tbLichtrinh.setComponentPopupMenu(popLT);
        tbLichtrinh.setRowHeight(24);
        tbLichtrinh.setShowGrid(false);
        tbLichtrinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLichtrinhMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbLichtrinh);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(5, 474));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 5, Short.MAX_VALUE));
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 274, Short.MAX_VALUE));

        jPanel5.add(jPanel8, java.awt.BorderLayout.LINE_START);

        jPanel3.add(jPanel5);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        tabbedPaneCustom1.addTab("Tour", jPanel2);

        add(tabbedPaneCustom1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void itemXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itemXoaActionPerformed
        int row = tbLichtrinh.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng lịch trình cần xóa.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbLichtrinh.getModel();
        model.removeRow(row);

        for (int i = row; i < model.getRowCount(); i++) {
            Object maLtValue = model.getValueAt(i, 0);
            if (maLtValue != null) {
                String maLt = maLtValue.toString().trim();
                if (maLt.startsWith("LT") && maLt.length() > 2) {
                    try {
                        int soThuTu = Integer.parseInt(maLt.substring(2)) - 1;
                        if (soThuTu < 1)
                            soThuTu = 1;
                        model.setValueAt(String.format("LT%03d", soThuTu), i, 0);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            Object ngayThuValue = model.getValueAt(i, 4);
            if (ngayThuValue != null) {
                try {
                    int ngayThu = Integer.parseInt(ngayThuValue.toString()) - 1;
                    if (ngayThu < 1)
                        ngayThu = 1;
                    model.setValueAt(ngayThu, i, 4);
                } catch (NumberFormatException ignored) {
                    model.setValueAt(i + 1, i, 4);
                }
            } else {
                model.setValueAt(i + 1, i, 4);
            }
        }

        tbLichtrinh.setModel(model);
    }// GEN-LAST:event_itemXoaActionPerformed

    private void tbLichtrinhMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbLichtrinhMouseClicked
        int row = tbLichtrinh.getSelectedRow();
        if (row >= 0) {
            String maDiaDiem = (String) tbLichtrinh.getValueAt(row, 2);
            String maPhuongTien = (String) tbLichtrinh.getValueAt(row, 3);
            String noiDung = (String) tbLichtrinh.getValueAt(row, 5);

            try {
                cbMaDiaDiem.setSelectedItem(diaDiemBUS.getTenDiaDiemByMa(maDiaDiem));
                cbMaPhuongTien.setSelectedItem(phuongTienBUS.getTenPhuongTienByMa(maPhuongTien));
                txtNoiDung.setText(noiDung);
            } catch (BusException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }// GEN-LAST:event_tbLichtrinhMouseClicked

    private void itemSuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itemSuaActionPerformed
        try {
            int row = tbLichtrinh.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng lịch trình cần sửa.");
                return;
            }

            String tenDiaDiem = cbMaDiaDiem.getSelectedItem() != null ? cbMaDiaDiem.getSelectedItem().toString() : null;
            String tenPhuongTien = cbMaPhuongTien.getSelectedItem() != null
                    ? cbMaPhuongTien.getSelectedItem().toString()
                    : null;
            String noiDungMoi = txtNoiDung.getText().trim();

            if ("...".equals(tenDiaDiem)) {
                tenDiaDiem = null;
            }
            if ("...".equals(tenPhuongTien)) {
                tenPhuongTien = null;
            }
            if (noiDungMoi.isEmpty()) {
                noiDungMoi = null;
            }
            if (tenDiaDiem == null || tenPhuongTien == null || noiDungMoi == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn địa điểm, phương tiện và nhập nội dung mới.");
                return;
            }

            String maDiaDiemMoi = diaDiemBUS.getMaDiaDiemByTen(tenDiaDiem);
            String maPhuongTienMoi = phuongTienBUS.getMaPhuongTienByTen(tenPhuongTien);

            DefaultTableModel model = (DefaultTableModel) tbLichtrinh.getModel();
            model.setValueAt(maDiaDiemMoi, row, 2);
            model.setValueAt(maPhuongTienMoi, row, 3);
            model.setValueAt(noiDungMoi, row, 5);
            tbLichtrinh.setModel(model);
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }// GEN-LAST:event_itemSuaActionPerformed

    private void tbTourMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbTourMouseClicked
        int row = tbTour.getSelectedRow();
        if (row >= 0) {
            String maTour = (String) tbTour.getValueAt(row, 0);
            try {
                Tour tour = tourBUS.getTourByMaTour(maTour);
                if (tour != null) {
                    txtMaTour.setText(tour.getMaTour());
                    txtTenTour.setText(tour.getTenTour());
                    txtNoiKhoiHanh.setText(tour.getNoiKhoiHanh());

                    listLoaiTour.stream()
                            .filter(lt -> lt.getMaLoaiTour().equals(tour.getMaLoaiTour()))
                            .findFirst()
                            .ifPresent(lt -> cbMaLoaiTour.setSelectedItem(lt.getTenLoai()));

                    listHDV.stream()
                            .filter(hdv -> hdv.getMaHDV().equals(tour.getMaHDV()))
                            .findFirst()
                            .ifPresent(hdv -> cbMaHDV.setSelectedItem(hdv.getTenHDV()));

                    if (tour.getTgKhoiHanh() != null)
                        spTgKhoiHanh.setValue(Date.valueOf(tour.getTgKhoiHanh()));
                    if (tour.getTgKetThuc() != null)
                        spTgKetThuc.setValue(Date.valueOf(tour.getTgKetThuc()));
                    txtSoLuongVe.setText(String.valueOf(tour.getSoLuongVe()));
                    txtSoLuongMin.setText(String.valueOf(tour.getSoLuongMin()));
                    txtGiaTour.setText(formatGiaTour(tour.getGiaTour()));
                    String tenAnhDb = tour.getAnhTour() != null ? tour.getAnhTour().trim() : "";
                    String tenTour = tour.getTenTour() != null ? tour.getTenTour().trim() : "";
                    String keyAnh = !tenAnhDb.isEmpty() ? tenAnhDb : tenTour;

                    String[] imageCandidates = new String[] {
                            "/image/tour/" + keyAnh,
                            "/image/tour/" + keyAnh + ".jpg",
                            "/image/tour/" + keyAnh + ".jpeg",
                            "/image/tour/" + keyAnh + ".png",
                            "/image/tour/" + keyAnh + ".gif",
                            "/image/tour/" + keyAnh + ".bmp",
                            "/image/tour/" + keyAnh.toLowerCase(),
                            "/image/tour/" + keyAnh.toLowerCase() + ".jpg",
                            "/image/tour/" + keyAnh.toLowerCase() + ".jpeg",
                            "/image/tour/" + keyAnh.toLowerCase() + ".png",
                            "/image/tour/" + keyAnh.toLowerCase() + ".gif",
                            "/image/tour/" + keyAnh.toLowerCase() + ".bmp"
                    };

                    java.net.URL imageUrl = null;
                    String tenAnhThucTe = null;
                    for (String imagePath : imageCandidates) {
                        imageUrl = getClass().getResource(imagePath);
                        if (imageUrl != null) {
                            tenAnhThucTe = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                            break;
                        }
                    }
                    if (imageUrl != null) {
                        int w = lbAnhTour.getWidth() > 0 ? lbAnhTour.getWidth() : 200;
                        int h = lbAnhTour.getHeight() > 0 ? lbAnhTour.getHeight() : 200;
                        Image scaled = new javax.swing.ImageIcon(imageUrl).getImage()
                                .getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
                        lbAnhTour.setIcon(new javax.swing.ImageIcon(scaled));
                        lbAnhTour.setText("");
                        lbTenAnh.setText(tenAnhThucTe != null ? tenAnhThucTe : "...");
                    } else {
                        lbAnhTour.setIcon(null);
                        lbAnhTour.setText("...");
                        lbTenAnh.setText(!tenAnhDb.isEmpty() ? tenAnhDb : "...");
                    }
                    boolean tourDangHoatDong = Boolean.TRUE.equals(tour.getTrangThai());
                    boolean tourDaKhoiHanh = Boolean.TRUE.equals(tour.getKhoiHanh());
                    boolean choPhepSuaXoa = tourDangHoatDong && !tourDaKhoiHanh;
                    if (btnThemTour != null) {
                        btnThemTour.setEnabled(choPhepSuaXoa);
                    }
                    if (btnSuaTour != null) {
                        btnSuaTour.setEnabled(choPhepSuaXoa);
                    }
                    if (btnXoaTour != null) {
                        btnXoaTour.setEnabled(choPhepSuaXoa);
                    }
                    if (btnThemLichTrinh != null) {
                        btnThemLichTrinh.setEnabled(choPhepSuaXoa);
                    }
                    if (btnThemExcel != null) {
                        btnThemExcel.setEnabled(choPhepSuaXoa);
                    }
                    itemSua.setEnabled(choPhepSuaXoa);
                    itemXoa.setEnabled(choPhepSuaXoa);
                    loadTableLichTrinh(maTour);
                    customTableHeader(tbLichtrinh);
                }
            } catch (BusException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }

    }// GEN-LAST:event_tbTourMouseClicked

    public void loadTableLichTrinh(String maTour) throws BusException {
        List<LichTrinh> listLichTrinh = lichTrinhBUS.getLichTrinhByMaTour(maTour);
        DefaultTableModel modelLichtrinh = new DefaultTableModel(
                new Object[] { "Mã LT", "Mã Tour", "Mã Địa Điểm", "Mã Phương Tiện", "Ngày thứ", "Nội dung" },
                0);
        for (LichTrinh lichTrinh : listLichTrinh) {
            modelLichtrinh.addRow(new Object[] { lichTrinh.getMaLichTrinh(), lichTrinh.getMaTour(),
                    lichTrinh.getMaDiaDiem(), lichTrinh.getMaPT(), lichTrinh.getNgayThu(), lichTrinh.getNoiDung() });
        }
        tbLichtrinh.setModel(modelLichtrinh);
    }

    private String formatGiaTour(double giaTour) {
        return BigDecimal.valueOf(giaTour).stripTrailingZeros().toPlainString();
    }

    private void lbAnhTourMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lbAnhTourMouseClicked
        JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("D:\\"));
        fileChooser.setDialogTitle("Chọn ảnh tour");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Ảnh (*.jpg, *.jpeg, *.png, *.gif, *.bmp)", "jpg", "jpeg", "png", "gif", "bmp"));

        if (fileChooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            java.awt.image.BufferedImage img;
            try {
                img = javax.imageio.ImageIO.read(file);
                int w = lbAnhTour.getWidth() > 0 ? lbAnhTour.getWidth() : 200;
                int h = lbAnhTour.getHeight() > 0 ? lbAnhTour.getHeight() : 200;
                Image scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
                lbAnhTour.setIcon(new javax.swing.ImageIcon(scaled));
                lbTenAnh.setText(file.getName());
            } catch (java.io.IOException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không thể đọc ảnh: " + ex.getMessage());
            }
        }
    }// GEN-LAST:event_lbAnhTourMouseClicked

    private void actionButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh tour");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Ảnh (*.jpg, *.jpeg, *.png, *.gif, *.bmp)", "jpg", "jpeg", "png", "gif", "bmp"));

        if (fileChooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            java.awt.image.BufferedImage img;
            try {
                img = javax.imageio.ImageIO.read(file);
                int w = lbAnhTour.getWidth() > 0 ? lbAnhTour.getWidth() : 200;
                int h = lbAnhTour.getHeight() > 0 ? lbAnhTour.getHeight() : 200;
                java.awt.Image scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
                lbAnhTour.setIcon(new javax.swing.ImageIcon(scaled));
                lbTenAnh.setText(file.getName());
            } catch (java.io.IOException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không thể đọc ảnh: " + ex.getMessage());
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem itemSua;
    private javax.swing.JMenuItem itemXoa;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbAnhTour;
    private javax.swing.JLabel lbTenAnh;
    private javax.swing.JPanel panelActionLT;
    private javax.swing.JPanel panelActionTour;
    private javax.swing.JPanel panelTT;
    private javax.swing.JPanel panelTTLichtrinh;
    private javax.swing.JPanel panelTTTour;
    private javax.swing.JPanel panelTenAnh;
    private javax.swing.JPopupMenu popLT;
    private GUI.Menu.TabbedPaneCustom tabbedPaneCustom1;
    private javax.swing.JTable tbLichtrinh;
    private javax.swing.JTable tbTour;
    // End of variables declaration//GEN-END:variables
}
