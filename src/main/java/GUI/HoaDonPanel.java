package GUI;

import BUS.CTHDBUS;
import BUS.HoaDonBUS;
import BUS.TourBUS;
import BUS.KhuyenMaiBUS;
import DTO.CTCN_NQ;
import DTO.CTHD;
import DTO.HoaDon;
import DTO.Tour;
import DTO.KhuyenMai;
import Exception.BusException;
import GUI.Dialog.KhachHangDialog;
import GUI.ScrollPane.DesignTable;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class HoaDonPanel extends javax.swing.JPanel {
    HoaDonBUS hdbus = new HoaDonBUS();
    CTHDBUS cthdbus = new CTHDBUS();
    TourBUS tbus = new TourBUS();
    KhuyenMaiBUS kmbus = new KhuyenMaiBUS();

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    DecimalFormat df = new DecimalFormat("#,###", symbols);
    DecimalFormat df2 = new DecimalFormat("#.0", symbols);
    private final float thue = Float.parseFloat(df2.format(0.1f));

    DefaultTableModel modeltour, modelhd, modelcthd, modelcthd2;
    TableRowSorter dshdSorter, dstourSorter;

    private String manv;

    Color headerColor = Color.decode("#18306F");

    public HoaDonPanel(String manv, CTCN_NQ quyenCN011) {
        this.manv = manv;
        initComponents();
        initGUI();

        String chiTiet = (quyenCN011 != null && quyenCN011.getChiTiet() != null) ? quyenCN011.getChiTiet() : "";
        applyPanelPermissions(chiTiet);
    }

    private void applyPanelPermissions(String chiTiet) {
        String detailRaw = (chiTiet != null) ? chiTiet : "";
        String detail = normalizeText(chiTiet);
        boolean coQuyenDatVe = detailRaw.contains("Đặt vé") || detail.contains("dat ve");
        boolean coQuyenThanhToanHuy = detailRaw.contains("Thanh toán + hủy")
                || detail.contains("thanh toan + huy")
                || (detail.contains("thanh toan") && detail.contains("huy"));

        tabbedPaneQLDatVe.removeAll();
        if (coQuyenDatVe) {
            tabbedPaneQLDatVe.addTab("Đặt vé", tabDatVe);
        }
        if (coQuyenThanhToanHuy) {
            tabbedPaneQLDatVe.addTab("Quản lý hóa đơn", tabQLHD);
        }
    }

    private String normalizeText(String input) {
        if (input == null) {
            return "";
        }
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replaceAll("\\s+", " ")
                .toLowerCase()
                .trim();
    }

    public void initGUI() {
        setTxtEditable();
        modelhd = (DefaultTableModel) tableDSHD.getModel();
        modeltour = (DefaultTableModel) tableDSTour.getModel();
        modelcthd = (DefaultTableModel) tableCTHD.getModel();
        modelcthd2 = (DefaultTableModel) tableCTHD2.getModel();

        loadComboboxMaKM();
        loadDataTour();

        cmboxKM.setSelectedIndex(-1);
        cmboxTTTT.addItem("Đã thanh toán");
        cmboxTTTT.addItem("Chưa thanh toán");
        cmboxTTTT.setSelectedIndex(-1);

        DesignTable.designTable(tableDSTour, headerColor);
        DesignTable.designTable(tableDSHD, headerColor);
        DesignTable.designTable(tableCTHD, headerColor);
        DesignTable.designTable(tableCTHD2, headerColor);

        autoHuyDatVeQuaHanThanhToan();

        lblThongBaoHoanTien.setVisible(false);
        btnXemDSCanHoanTien.setVisible(false);
        autoHuyDatVeDoCongTyHuyTour();
        menuItemHoanTien.setVisible(false);

        autoXuLyVeDaHoanTat();
        // System.out.println(thue);
    }

    public void setTxtEditable() {
        boolean t = false;
        txtMaHD.setEditable(t);
        txtMaKH.setEditable(t);
        txtMaNV.setText(manv);
        txtMaNV.setEditable(t);
        txtNgayLap.setEditable(t);
        txtNgayLap.setText(String.valueOf(LocalDate.now()));
        txtTongTien.setEditable(t);
        txtThue.setEditable(t);
        txtKhuyenMai.setEditable(t);
        txtTongThanhToan.setEditable(t);
    }

    public void loadDataTour() {
        try {
            modeltour.setRowCount(0);
            ArrayList<Tour> dstour = tbus.getAllTourWithSoChoCon();

            for (Tour t : dstour) {
                String matour = t.getMaTour();
                String tentour = t.getTenTour();
                String noikh = t.getNoiKhoiHanh();
                LocalDate ngaykh = t.getTgKhoiHanh();
                double giatour = t.getGiaTour();
                int sochocon = t.getSoChoCon();

                Object[] row = { matour, tentour, noikh, ngaykh, giatour, sochocon };
                modeltour.addRow(row);
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void loadTableDSHD(ArrayList<HoaDon> dshd) {
        modelhd.setRowCount(0);
        for (HoaDon hd : dshd) {
            String mahd = hd.getMaHoaDon();
            String manv = hd.getMaNhanVien();
            String makh = hd.getMaKhachHang();
            String sdt = hd.getSdt();
            LocalDate ngaylap = hd.getNgayLapHD();
            double tongtien = hd.getTongTien();
            String makm = hd.getMaKhuyenMai();
            String httt = hd.getHTTT();
            boolean tttt = hd.isTrangThaiTT();

            Object[] row = { mahd, manv, makh, sdt, ngaylap, tongtien, makm, httt, tttt };
            modelhd.addRow(row);
        }
    }

    public void loadDataHoaDon() {
        try {
            modelhd.setRowCount(0);
            ArrayList<HoaDon> dshd = hdbus.getAllHoaDon();
            loadTableDSHD(dshd);
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void loadTableCTHD2(String mahd) {
        try {
            modelcthd2.setRowCount(0);
            ArrayList<CTHD> dscthd = cthdbus.getDSCTHDTheoMaHD(mahd);

            for (CTHD cthd : dscthd) {
                String matour = cthd.getMaTour();
                String tentour = cthd.getTenTour();
                int soluongve = cthd.getSoLuongVe();
                double dongia = cthd.getGiaTour();
                double thanhtien = dongia * soluongve;
                String trangthai = cthd.getTrangThai();

                Object[] row = { mahd, matour, tentour, soluongve, dongia, thanhtien, trangthai };
                modelcthd2.addRow(row);
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void loadComboboxMaKM() {
        try {
            ArrayList<KhuyenMai> dskm = kmbus.getGiaTriKhuyenMai();
            for (KhuyenMai km : dskm) {
                String makm = km.getMaKhuyenMai();
                String tenkm = km.getTenKhuyenMai();
                cmboxKM.addItem(makm + " - " + tenkm);
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void clearTimKiemInput() {
        txtMaHDTK.setText("");
        txtMaKHTK.setText("");
        txtSDTTK.setText("");
        cmboxTTTT.setSelectedIndex(-1);
        txtNgayLap2.setText("");
    }

    public void autoHuyDatVeQuaHanThanhToan() {
        try {
            int count = cthdbus.autoHuyDatVeQuaHan();
            if (count > 0) {
                loadDataTour();
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void fillTinhTien(DefaultTableModel model) {
        try {
            double tongtien = 0;
            double tienthue = 0;
            double khuyenmai = 0;
            double tongtt = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                tongtien += Double.parseDouble(String.valueOf(model.getValueAt(i, 4)));
            }

            if (cmboxKM.getSelectedIndex() != -1) {
                String cmboxkm = String.valueOf(cmboxKM.getSelectedItem());
                String[] partscmboxmakm = cmboxkm.split("-");
                String makm = String.valueOf(partscmboxmakm[0].trim());
                double giatrikm = kmbus.getGiaTriKMByMaKM(makm);
                if (giatrikm <= 1)
                    khuyenmai = tongtien * giatrikm;
                else
                    khuyenmai = giatrikm;
            } else
                khuyenmai = 0;

            tienthue = tongtien * thue;
            tongtt = tongtien + tienthue - khuyenmai;

            if (tabbedPaneQLDatVe.getSelectedIndex() == 0) {
                txtTongTien.setText(String.valueOf(df.format(tongtien)));
                txtThue.setText(String.valueOf(df.format(tienthue)));
                txtKhuyenMai.setText(String.valueOf(df.format(khuyenmai)));
                txtTongThanhToan.setText(String.valueOf(df.format(tongtt)));
            }
            if (tabbedPaneQLDatVe.getSelectedIndex() == 1) {
                txtMaHDTT.setText(String.valueOf(df.format(tongtien)));
                txtMaKHTT.setText(String.valueOf(df.format(tienthue)));
                txtSDTTT.setText(String.valueOf(df.format(khuyenmai)));
                txtTongThanhToan2.setText(String.valueOf(df.format(tongtt)));
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void capNhatThongTinHuyVe() {
        try {
            double tienhoan = 0;

            int[] rows = tableCTHD2.getSelectedRows();
            int count = rows.length;

            txtSoTourHuy.setText(String.valueOf(count));

            for (int row : rows) {
                String mahd = String.valueOf(tableCTHD2.getValueAt(row, 0));
                String matour = String.valueOf(tableCTHD2.getValueAt(row, 1));
                double tongtientour = Double.parseDouble(String.valueOf(tableCTHD2.getValueAt(row, 5)));

                HoaDon hd = hdbus.getHoaDonByMaHD(mahd);
                if (!hd.isTrangThaiTT())
                    tienhoan = 0;
                else {
                    CTHD cthd = cthdbus.getCTHDTheoMaHDMaTour(mahd, matour);
                    int phantram = cthdbus.tinhPhanTramHoanTien(cthd);
                    tienhoan += tongtientour * phantram / 100;
                }
            }
            tienhoan = tienhoan + tienhoan * thue;
            txtSoTienHoan.setText(df.format(tienhoan));
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void capNhatThongBaoHoanTien() {
        try {
            int so = cthdbus.demSoLuongCanHoanTien();
            if (so > 0) {
                lblThongBaoHoanTien.setText("Có " + so + " trường hợp đang cần hoàn tiền do công ty hủy tour");
                lblThongBaoHoanTien.setVisible(true);
                btnXemDSCanHoanTien.setVisible(true);
            } else {
                lblThongBaoHoanTien.setVisible(false);
                btnXemDSCanHoanTien.setVisible(true);
            }

            this.revalidate();
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoHuyDatVeDoCongTyHuyTour() {
        try {
            int count = cthdbus.autoHuyDatVeDoCongTyHuyTour();
            if (count > 0) {
                loadDataTour();
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void autoXuLyVeDaHoanTat() {
        try {
            cthdbus.xuLyVeDaHoanTat();
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupHTTT = new javax.swing.ButtonGroup();
        popupMenuHoanTien = new javax.swing.JPopupMenu();
        menuItemHoanTien = new javax.swing.JMenuItem();
        tabbedPaneQLDatVe = new GUI.Menu.TabbedPaneCustom();
        tabDatVe = new javax.swing.JPanel();
        panelTTHD = new javax.swing.JPanel();
        lblLapHoaDon = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        txtMaHD = new GUI.Menu.TextFieldCustom();
        lblMaNV = new javax.swing.JLabel();
        txtMaNV = new GUI.Menu.TextFieldCustom();
        lblMaKH = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtMaKH = new GUI.Menu.TextFieldCustom();
        btnChonMaKH = new GUI.Menu.ActionButton();
        lblNgayLap = new javax.swing.JLabel();
        txtNgayLap = new GUI.Menu.TextFieldCustom();
        PanelTrong3 = new javax.swing.JPanel();
        lblTamTinh = new javax.swing.JLabel();
        panelTamTinh = new javax.swing.JPanel();
        lblTongTien = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        lblThue = new javax.swing.JLabel();
        txtThue = new javax.swing.JTextField();
        lblKhuyenMai = new javax.swing.JLabel();
        txtKhuyenMai = new javax.swing.JTextField();
        cmboxKM = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        lblTongThanhToan = new javax.swing.JLabel();
        txtTongThanhToan = new javax.swing.JTextField();
        PanelTrong2 = new javax.swing.JPanel();
        panelTrong8 = new javax.swing.JPanel();
        btnTaoHoaDon = new GUI.Menu.ActionButton();
        panelTrong18 = new javax.swing.JPanel();
        btnTiepTucTaoHD = new GUI.Menu.ActionButton();
        panelDSHD = new javax.swing.JPanel();
        lblDanhSachTour = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMaTourTK = new GUI.Menu.TextFieldCustom();
        jLabel3 = new javax.swing.JLabel();
        txtTenTourTK = new GUI.Menu.TextFieldCustom();
        btnTimKiemTour = new GUI.Menu.ActionButton();
        btnLamMoiTKTour = new GUI.Menu.ActionButton();
        jScrollPaneDanhSachTour = new GUI.ScrollPane.ScrollPaneWin11();
        tableDSTour = new javax.swing.JTable() {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row < 0 || col < 0)
                    return null;

                Object value = getValueAt(row, col);
                if (value == null)
                    return null;

                String text = value.toString();
                if (text.isEmpty())
                    return null;

                // Tạo tooltip dạng HTML để tùy chỉnh font, wrap, v.v.
                return "<html><div style='font-size:16px; font-family:Arial; padding:4px; max-width:400px;'>"
                        + text.replace("\n", "<br>")
                        + "</div></html>";
            }
        };
        lblSoLuong = new javax.swing.JLabel();
        panelSL = new javax.swing.JPanel();
        btnChonTour = new GUI.Menu.ActionButton();
        txtSoLuong = new GUI.Menu.TextFieldCustom();
        panelTrong1 = new javax.swing.JPanel();
        lblDanhSachTour1 = new javax.swing.JLabel();
        jScrollPaneCTHD = new GUI.ScrollPane.ScrollPaneWin11();
        tableCTHD = new javax.swing.JTable() {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row < 0 || col < 0)
                    return null;

                Object value = getValueAt(row, col);
                if (value == null)
                    return null;

                String text = value.toString();
                if (text.isEmpty())
                    return null;

                // Tạo tooltip dạng HTML để tùy chỉnh font, wrap, v.v.
                return "<html><div style='font-size:16px; font-family:Arial; padding:4px; max-width:400px;'>"
                        + text.replace("\n", "<br>")
                        + "</div></html>";
            }
        };
        panelTrong = new javax.swing.JPanel();
        tabQLHD = new javax.swing.JPanel();
        panelTimKiemHD = new javax.swing.JPanel();
        panelTrong14 = new javax.swing.JPanel();
        lblTimKiemHD = new javax.swing.JLabel();
        panelTrong13 = new javax.swing.JPanel();
        lblMaHD2 = new javax.swing.JLabel();
        txtMaHDTK = new GUI.Menu.TextFieldCustom();
        panelTrong9 = new javax.swing.JPanel();
        lblMaKH2 = new javax.swing.JLabel();
        txtMaKHTK = new GUI.Menu.TextFieldCustom();
        panelTrong10 = new javax.swing.JPanel();
        lblSDT2 = new javax.swing.JLabel();
        txtSDTTK = new GUI.Menu.TextFieldCustom();
        panelTrong11 = new javax.swing.JPanel();
        lblTTTT = new javax.swing.JLabel();
        cmboxTTTT = new javax.swing.JComboBox<>();
        panelTrong12 = new javax.swing.JPanel();
        lblNgayLap2 = new javax.swing.JLabel();
        txtNgayLap2 = new GUI.Menu.TextFieldCustom();
        panelTrong3 = new javax.swing.JPanel();
        panelTrong15 = new javax.swing.JPanel();
        bthTimKiemHD = new GUI.Menu.ActionButton();
        panelTrong6 = new javax.swing.JPanel();
        btnLamMoiTKHD = new GUI.Menu.ActionButton();
        panelRight = new javax.swing.JPanel();
        lblDSHD = new javax.swing.JLabel();
        jScrollPaneDSHD = new GUI.ScrollPane.ScrollPaneWin11();
        tableDSHD = new javax.swing.JTable() {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row < 0 || col < 0)
                    return null;

                Object value = getValueAt(row, col);
                if (value == null)
                    return null;

                String text = value.toString();
                if (text.isEmpty())
                    return null;

                // Tạo tooltip dạng HTML để tùy chỉnh font, wrap, v.v.
                return "<html><div style='font-size:16px; font-family:Arial; padding:4px; max-width:400px;'>"
                        + text.replace("\n", "<br>")
                        + "</div></html>";
            }
        };
        panelTrong7 = new javax.swing.JPanel();
        lblCTHD2 = new javax.swing.JLabel();
        jScrollPaneCTHD2 = new GUI.ScrollPane.ScrollPaneWin11();
        tableCTHD2 = new javax.swing.JTable() {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row < 0 || col < 0)
                    return null;

                Object value = getValueAt(row, col);
                if (value == null)
                    return null;

                String text = value.toString();
                if (text.isEmpty())
                    return null;

                // Tạo tooltip dạng HTML để tùy chỉnh font, wrap, v.v.
                return "<html><div style='font-size:16px; font-family:Arial; padding:4px; max-width:400px;'>"
                        + text.replace("\n", "<br>")
                        + "</div></html>";
            }
        };
        panelTrong17 = new javax.swing.JPanel();
        panelbtnChucNang = new javax.swing.JPanel();
        btnCNXemHD = new GUI.Menu.ActionButton();
        btnCNThanhToan = new GUI.Menu.ActionButton();
        btnCNHuyVe = new GUI.Menu.ActionButton();
        panelTrong16 = new javax.swing.JPanel();
        panelChucNang = new javax.swing.JPanel();
        panelXemDSHD = new javax.swing.JPanel();
        panelTrong19 = new javax.swing.JPanel();
        lblThongBaoHoanTien = new javax.swing.JLabel();
        panelTrong20 = new javax.swing.JPanel();
        btnXemDSCanHoanTien = new GUI.Menu.ActionButton();
        panelThaoTacThanhToan = new javax.swing.JPanel();
        lblThaoTacThanhToan = new javax.swing.JLabel();
        panelXacNhanThanhToan = new javax.swing.JPanel();
        panelXacNhanTTThanhToan = new javax.swing.JPanel();
        lblMaHoaDonTT = new javax.swing.JLabel();
        txtMaHDTT = new javax.swing.JTextField();
        lblMaKHTT = new javax.swing.JLabel();
        txtMaKHTT = new javax.swing.JTextField();
        lblSDTTT = new javax.swing.JLabel();
        txtSDTTT = new javax.swing.JTextField();
        lblTongTT = new javax.swing.JLabel();
        txtTongThanhToan2 = new javax.swing.JTextField();
        panelHTTT = new javax.swing.JPanel();
        lblHTTT = new javax.swing.JLabel();
        radiobtnTienMat = new javax.swing.JRadioButton();
        radiobtnChuyenKhoan = new javax.swing.JRadioButton();
        panelTrong23 = new javax.swing.JPanel();
        btnThanhToan = new GUI.Menu.ActionButton();
        panelThaoTacHuyVe = new javax.swing.JPanel();
        lblThaoTacHuyVe = new javax.swing.JLabel();
        panelThongTinHuyVe = new javax.swing.JPanel();
        panelLyDoHuy = new javax.swing.JPanel();
        lblLyDo = new javax.swing.JLabel();
        txtLyDoHuy = new GUI.Menu.TextFieldCustom();
        panelTienHuyVe = new javax.swing.JPanel();
        lblMaHDHuy = new javax.swing.JLabel();
        txtMaHDHuy = new javax.swing.JTextField();
        lblSoTourHuy = new javax.swing.JLabel();
        txtSoTourHuy = new javax.swing.JTextField();
        lblSoTienHoan = new javax.swing.JLabel();
        txtSoTienHoan = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        btnHuyVe = new GUI.Menu.ActionButton();
        panelTrong5 = new javax.swing.JPanel();

        menuItemHoanTien.setText("Hoàn tiền");
        menuItemHoanTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemHoanTienActionPerformed(evt);
            }
        });
        popupMenuHoanTien.add(menuItemHoanTien);

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1350, 920));
        setPreferredSize(new java.awt.Dimension(1350, 920));
        setLayout(new java.awt.BorderLayout());

        tabbedPaneQLDatVe.setBackground(new java.awt.Color(255, 255, 255));
        tabbedPaneQLDatVe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tabbedPaneQLDatVe.setPreferredSize(new java.awt.Dimension(1350, 920));
        tabbedPaneQLDatVe.setSelectedColor(new java.awt.Color(51, 153, 255));
        tabbedPaneQLDatVe.setUnselectedColor(new java.awt.Color(243, 243, 243));

        tabDatVe.setBackground(new java.awt.Color(255, 255, 255));
        tabDatVe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tabDatVe.setMinimumSize(new java.awt.Dimension(1350, 879));
        tabDatVe.setLayout(new java.awt.BorderLayout());

        panelTTHD.setBackground(new java.awt.Color(255, 255, 255));
        panelTTHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        panelTTHD.setMaximumSize(new java.awt.Dimension(583, 212879));
        panelTTHD.setMinimumSize(new java.awt.Dimension(583, 879));
        panelTTHD.setPreferredSize(new java.awt.Dimension(583, 879));
        panelTTHD.setLayout(new javax.swing.BoxLayout(panelTTHD, javax.swing.BoxLayout.Y_AXIS));

        lblLapHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        lblLapHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLapHoaDon.setText("LẬP HÓA ĐƠN");
        lblLapHoaDon.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblLapHoaDon.setAlignmentX(0.5F);
        lblLapHoaDon.setMaximumSize(new java.awt.Dimension(200, 50));
        lblLapHoaDon.setMinimumSize(new java.awt.Dimension(150, 50));
        lblLapHoaDon.setPreferredSize(new java.awt.Dimension(200, 50));
        panelTTHD.add(lblLapHoaDon);

        lblMaHD.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblMaHD.setText("Mã hóa đơn");
        lblMaHD.setAlignmentX(0.5F);
        lblMaHD.setMaximumSize(new java.awt.Dimension(390, 35));
        lblMaHD.setMinimumSize(new java.awt.Dimension(390, 35));
        lblMaHD.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTTHD.add(lblMaHD);

        txtMaHD.setForeground(new java.awt.Color(51, 51, 51));
        txtMaHD.setColorBottom(new java.awt.Color(243, 243, 243));
        txtMaHD.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtMaHD.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtMaHD.setColorTop(new java.awt.Color(243, 243, 243));
        txtMaHD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMaHD.setMaximumSize(new java.awt.Dimension(390, 35));
        txtMaHD.setPreferredSize(new java.awt.Dimension(390, 35));
        txtMaHD.setSelectionColor(new java.awt.Color(204, 204, 204));
        panelTTHD.add(txtMaHD);

        lblMaNV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblMaNV.setText("Mã nhân viên lập");
        lblMaNV.setAlignmentX(0.5F);
        lblMaNV.setMaximumSize(new java.awt.Dimension(390, 35));
        lblMaNV.setMinimumSize(new java.awt.Dimension(390, 35));
        lblMaNV.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTTHD.add(lblMaNV);

        txtMaNV.setForeground(new java.awt.Color(51, 51, 51));
        txtMaNV.setColorBottom(new java.awt.Color(243, 243, 243));
        txtMaNV.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtMaNV.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtMaNV.setColorTop(new java.awt.Color(243, 243, 243));
        txtMaNV.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMaNV.setMaximumSize(new java.awt.Dimension(390, 35));
        txtMaNV.setPreferredSize(new java.awt.Dimension(390, 35));
        txtMaNV.setSelectionColor(new java.awt.Color(204, 204, 204));
        panelTTHD.add(txtMaNV);

        lblMaKH.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblMaKH.setText("Mã khách hàng");
        lblMaKH.setAlignmentX(0.5F);
        lblMaKH.setMaximumSize(new java.awt.Dimension(390, 35));
        lblMaKH.setMinimumSize(new java.awt.Dimension(390, 35));
        lblMaKH.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTTHD.add(lblMaKH);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(390, 35));
        jPanel4.setMinimumSize(new java.awt.Dimension(390, 35));
        jPanel4.setPreferredSize(new java.awt.Dimension(390, 35));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        txtMaKH.setForeground(new java.awt.Color(51, 51, 51));
        txtMaKH.setColorBottom(new java.awt.Color(243, 243, 243));
        txtMaKH.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtMaKH.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtMaKH.setColorTop(new java.awt.Color(243, 243, 243));
        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMaKH.setMaximumSize(new java.awt.Dimension(355, 35));
        txtMaKH.setPreferredSize(new java.awt.Dimension(355, 35));
        txtMaKH.setSelectionColor(new java.awt.Color(204, 204, 204));
        jPanel4.add(txtMaKH);

        btnChonMaKH.setText("...");
        btnChonMaKH.setColorBottom(new java.awt.Color(153, 153, 153));
        btnChonMaKH.setColorTop(new java.awt.Color(153, 153, 153));
        btnChonMaKH.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnChonMaKH.setMaximumSize(new java.awt.Dimension(35, 35));
        btnChonMaKH.setMinimumSize(new java.awt.Dimension(35, 35));
        btnChonMaKH.setPreferredSize(new java.awt.Dimension(35, 35));
        btnChonMaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonMaKHActionPerformed(evt);
            }
        });
        jPanel4.add(btnChonMaKH);

        panelTTHD.add(jPanel4);

        lblNgayLap.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        lblNgayLap.setText("Ngày lập");
        lblNgayLap.setAlignmentX(0.5F);
        lblNgayLap.setMaximumSize(new java.awt.Dimension(390, 35));
        lblNgayLap.setMinimumSize(new java.awt.Dimension(390, 35));
        lblNgayLap.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTTHD.add(lblNgayLap);

        txtNgayLap.setForeground(new java.awt.Color(51, 51, 51));
        txtNgayLap.setColorBottom(new java.awt.Color(243, 243, 243));
        txtNgayLap.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtNgayLap.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtNgayLap.setColorTop(new java.awt.Color(243, 243, 243));
        txtNgayLap.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtNgayLap.setMaximumSize(new java.awt.Dimension(390, 35));
        txtNgayLap.setPreferredSize(new java.awt.Dimension(390, 35));
        txtNgayLap.setSelectionColor(new java.awt.Color(204, 204, 204));
        panelTTHD.add(txtNgayLap);

        PanelTrong3.setBackground(new java.awt.Color(255, 255, 255));
        PanelTrong3.setMaximumSize(new java.awt.Dimension(600, 30));
        PanelTrong3.setMinimumSize(new java.awt.Dimension(600, 30));

        javax.swing.GroupLayout PanelTrong3Layout = new javax.swing.GroupLayout(PanelTrong3);
        PanelTrong3.setLayout(PanelTrong3Layout);
        PanelTrong3Layout.setHorizontalGroup(
                PanelTrong3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        PanelTrong3Layout.setVerticalGroup(
                PanelTrong3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE));

        panelTTHD.add(PanelTrong3);

        lblTamTinh.setFont(new java.awt.Font("Segoe UI", 1, 23)); // NOI18N
        lblTamTinh.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTamTinh.setText("Tạm tính");
        lblTamTinh.setAlignmentX(0.5F);
        lblTamTinh.setMaximumSize(new java.awt.Dimension(500, 35));
        lblTamTinh.setMinimumSize(new java.awt.Dimension(500, 35));
        lblTamTinh.setPreferredSize(new java.awt.Dimension(500, 35));
        panelTTHD.add(lblTamTinh);

        panelTamTinh.setBackground(new java.awt.Color(228, 241, 255));
        panelTamTinh.setMaximumSize(new java.awt.Dimension(500, 250));
        panelTamTinh.setMinimumSize(new java.awt.Dimension(200, 200));
        panelTamTinh.setPreferredSize(new java.awt.Dimension(500, 250));
        panelTamTinh.setLayout(new java.awt.GridLayout(5, 2, 0, 10));

        lblTongTien.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblTongTien.setText("Tổng tiền: ");
        lblTongTien.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblTongTien.setMinimumSize(new java.awt.Dimension(548, 30));
        lblTongTien.setPreferredSize(new java.awt.Dimension(548, 30));
        panelTamTinh.add(lblTongTien);

        txtTongTien.setBackground(new java.awt.Color(228, 241, 255));
        txtTongTien.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongTien.setText("0");
        txtTongTien.setBorder(null);
        txtTongTien.setMaximumSize(new java.awt.Dimension(390, 35));
        txtTongTien.setMinimumSize(new java.awt.Dimension(390, 35));
        txtTongTien.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTamTinh.add(txtTongTien);

        lblThue.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblThue.setText("Thuế (10%):");
        lblThue.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblThue.setMinimumSize(new java.awt.Dimension(548, 30));
        lblThue.setPreferredSize(new java.awt.Dimension(548, 30));
        panelTamTinh.add(lblThue);

        txtThue.setBackground(new java.awt.Color(228, 241, 255));
        txtThue.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtThue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtThue.setText("0");
        txtThue.setBorder(null);
        txtThue.setMaximumSize(new java.awt.Dimension(390, 35));
        txtThue.setMinimumSize(new java.awt.Dimension(390, 35));
        txtThue.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTamTinh.add(txtThue);

        lblKhuyenMai.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblKhuyenMai.setText("Khuyến mãi:");
        lblKhuyenMai.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblKhuyenMai.setMinimumSize(new java.awt.Dimension(548, 30));
        lblKhuyenMai.setPreferredSize(new java.awt.Dimension(548, 30));
        panelTamTinh.add(lblKhuyenMai);

        txtKhuyenMai.setBackground(new java.awt.Color(228, 241, 255));
        txtKhuyenMai.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtKhuyenMai.setForeground(new java.awt.Color(51, 204, 0));
        txtKhuyenMai.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtKhuyenMai.setText("- 0");
        txtKhuyenMai.setBorder(null);
        txtKhuyenMai.setMaximumSize(new java.awt.Dimension(390, 35));
        txtKhuyenMai.setMinimumSize(new java.awt.Dimension(390, 35));
        txtKhuyenMai.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTamTinh.add(txtKhuyenMai);

        cmboxKM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        cmboxKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmboxKMActionPerformed(evt);
            }
        });
        panelTamTinh.add(cmboxKM);
        panelTamTinh.add(jLabel1);

        lblTongThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTongThanhToan.setText("Tổng thanh toán:");
        lblTongThanhToan.setMaximumSize(new java.awt.Dimension(200000, 30));
        lblTongThanhToan.setMinimumSize(new java.awt.Dimension(548, 30));
        lblTongThanhToan.setPreferredSize(new java.awt.Dimension(600, 30));
        panelTamTinh.add(lblTongThanhToan);

        txtTongThanhToan.setBackground(new java.awt.Color(228, 241, 255));
        txtTongThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        txtTongThanhToan.setForeground(new java.awt.Color(0, 102, 204));
        txtTongThanhToan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongThanhToan.setText("0");
        txtTongThanhToan.setBorder(null);
        txtTongThanhToan.setMaximumSize(new java.awt.Dimension(390, 35));
        txtTongThanhToan.setMinimumSize(new java.awt.Dimension(390, 35));
        txtTongThanhToan.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTamTinh.add(txtTongThanhToan);

        panelTTHD.add(panelTamTinh);

        PanelTrong2.setBackground(new java.awt.Color(255, 255, 255));
        PanelTrong2.setMaximumSize(new java.awt.Dimension(600, 30));
        PanelTrong2.setMinimumSize(new java.awt.Dimension(600, 30));
        PanelTrong2.setPreferredSize(new java.awt.Dimension(600, 30));

        javax.swing.GroupLayout PanelTrong2Layout = new javax.swing.GroupLayout(PanelTrong2);
        PanelTrong2.setLayout(PanelTrong2Layout);
        PanelTrong2Layout.setHorizontalGroup(
                PanelTrong2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        PanelTrong2Layout.setVerticalGroup(
                PanelTrong2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE));

        panelTTHD.add(PanelTrong2);

        panelTrong8.setBackground(new java.awt.Color(255, 255, 255));
        panelTrong8.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong8.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong8Layout = new javax.swing.GroupLayout(panelTrong8);
        panelTrong8.setLayout(panelTrong8Layout);
        panelTrong8Layout.setHorizontalGroup(
                panelTrong8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong8Layout.setVerticalGroup(
                panelTrong8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTTHD.add(panelTrong8);

        btnTaoHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/save.png"))); // NOI18N
        btnTaoHoaDon.setText("  Tạo hóa đơn");
        btnTaoHoaDon.setAlignmentX(0.5F);
        btnTaoHoaDon.setColorBottom(new java.awt.Color(0, 0, 0));
        btnTaoHoaDon.setColorTop(new java.awt.Color(51, 51, 51));
        btnTaoHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnTaoHoaDon.setMaximumSize(new java.awt.Dimension(400, 40));
        btnTaoHoaDon.setPreferredSize(new java.awt.Dimension(400, 40));
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });
        panelTTHD.add(btnTaoHoaDon);

        panelTrong18.setBackground(new java.awt.Color(255, 255, 255));
        panelTrong18.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong18.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong18Layout = new javax.swing.GroupLayout(panelTrong18);
        panelTrong18.setLayout(panelTrong18Layout);
        panelTrong18Layout.setHorizontalGroup(
                panelTrong18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong18Layout.setVerticalGroup(
                panelTrong18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTTHD.add(panelTrong18);

        btnTiepTucTaoHD.setForeground(new java.awt.Color(0, 0, 0));
        btnTiepTucTaoHD.setText("Tiếp tục");
        btnTiepTucTaoHD.setAlignmentX(0.5F);
        btnTiepTucTaoHD.setColorBottom(new java.awt.Color(227, 227, 227));
        btnTiepTucTaoHD.setColorTop(new java.awt.Color(255, 255, 255));
        btnTiepTucTaoHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnTiepTucTaoHD.setMaximumSize(new java.awt.Dimension(400, 40));
        btnTiepTucTaoHD.setPreferredSize(new java.awt.Dimension(400, 40));
        btnTiepTucTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTiepTucTaoHDActionPerformed(evt);
            }
        });
        panelTTHD.add(btnTiepTucTaoHD);

        tabDatVe.add(panelTTHD, java.awt.BorderLayout.WEST);

        panelDSHD.setBackground(new java.awt.Color(255, 255, 255));
        panelDSHD.setMaximumSize(new java.awt.Dimension(2147483647, 212123));
        panelDSHD.setMinimumSize(new java.awt.Dimension(767, 879));
        panelDSHD.setPreferredSize(new java.awt.Dimension(767, 879));
        panelDSHD.setLayout(new javax.swing.BoxLayout(panelDSHD, javax.swing.BoxLayout.Y_AXIS));

        lblDanhSachTour.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblDanhSachTour.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDanhSachTour.setText("DANH SÁCH TOUR");
        lblDanhSachTour.setAlignmentX(0.5F);
        lblDanhSachTour.setMaximumSize(new java.awt.Dimension(200, 35));
        lblDanhSachTour.setMinimumSize(new java.awt.Dimension(200, 35));
        lblDanhSachTour.setPreferredSize(new java.awt.Dimension(200, 35));
        panelDSHD.add(lblDanhSachTour);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 70));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(735, 70));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Mã tour:");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setMaximumSize(new java.awt.Dimension(80, 30));
        jLabel2.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 30));
        jPanel1.add(jLabel2);

        txtMaTourTK.setBorder(null);
        txtMaTourTK.setForeground(new java.awt.Color(51, 51, 51));
        txtMaTourTK.setColorBottom(new java.awt.Color(243, 243, 243));
        txtMaTourTK.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtMaTourTK.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtMaTourTK.setColorTop(new java.awt.Color(243, 243, 243));
        txtMaTourTK.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMaTourTK.setMaximumSize(new java.awt.Dimension(3212, 30));
        txtMaTourTK.setPreferredSize(new java.awt.Dimension(180, 30));
        txtMaTourTK.setSelectionColor(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtMaTourTK);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tên tour:");
        jLabel3.setMaximumSize(new java.awt.Dimension(80, 30));
        jLabel3.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 30));
        jPanel1.add(jLabel3);

        txtTenTourTK.setForeground(new java.awt.Color(51, 51, 51));
        txtTenTourTK.setColorBottom(new java.awt.Color(243, 243, 243));
        txtTenTourTK.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtTenTourTK.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtTenTourTK.setColorTop(new java.awt.Color(243, 243, 243));
        txtTenTourTK.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTenTourTK.setMaximumSize(new java.awt.Dimension(3212, 30));
        txtTenTourTK.setPreferredSize(new java.awt.Dimension(180, 30));
        txtTenTourTK.setSelectionColor(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtTenTourTK);

        btnTimKiemTour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/find.png"))); // NOI18N
        btnTimKiemTour.setAlignmentX(0.5F);
        btnTimKiemTour.setColorBottom(new java.awt.Color(0, 0, 0));
        btnTimKiemTour.setColorTop(new java.awt.Color(51, 51, 51));
        btnTimKiemTour.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnTimKiemTour.setMaximumSize(new java.awt.Dimension(400, 40));
        btnTimKiemTour.setPreferredSize(new java.awt.Dimension(40, 32));
        btnTimKiemTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemTourActionPerformed(evt);
            }
        });
        jPanel1.add(btnTimKiemTour);

        btnLamMoiTKTour.setForeground(new java.awt.Color(0, 0, 0));
        btnLamMoiTKTour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload.png"))); // NOI18N
        btnLamMoiTKTour.setAlignmentX(0.5F);
        btnLamMoiTKTour.setColorBottom(new java.awt.Color(227, 227, 227));
        btnLamMoiTKTour.setColorTop(new java.awt.Color(255, 255, 255));
        btnLamMoiTKTour.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLamMoiTKTour.setMaximumSize(new java.awt.Dimension(400, 40));
        btnLamMoiTKTour.setMinimumSize(new java.awt.Dimension(0, 0));
        btnLamMoiTKTour.setPreferredSize(new java.awt.Dimension(40, 32));
        btnLamMoiTKTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiTKTourActionPerformed(evt);
            }
        });
        jPanel1.add(btnLamMoiTKTour);

        panelDSHD.add(jPanel1);

        jScrollPaneDanhSachTour.setMaximumSize(new java.awt.Dimension(32767, 280));
        jScrollPaneDanhSachTour.setMinimumSize(new java.awt.Dimension(767, 280));
        jScrollPaneDanhSachTour.setPreferredSize(new java.awt.Dimension(767, 280));

        tableDSTour.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        tableDSTour.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null }
                },
                new String[] {
                        "Mã tour", "Tên tour", "Nơi khởi hành", "Thời gian khởi hành", "Giá tour", "Số chỗ còn"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableDSTour.setRowHeight(25);
        tableDSTour.setShowGrid(false);
        jScrollPaneDanhSachTour.setViewportView(tableDSTour);

        panelDSHD.add(jScrollPaneDanhSachTour);

        lblSoLuong.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblSoLuong.setText("Số lượng vé");
        lblSoLuong.setAlignmentX(0.5F);
        lblSoLuong.setMaximumSize(new java.awt.Dimension(2147483647, 35));
        lblSoLuong.setMinimumSize(new java.awt.Dimension(764, 35));
        lblSoLuong.setPreferredSize(new java.awt.Dimension(764, 35));
        panelDSHD.add(lblSoLuong);

        panelSL.setBackground(new java.awt.Color(255, 255, 255));
        panelSL.setMaximumSize(new java.awt.Dimension(2147483647, 35));
        panelSL.setMinimumSize(new java.awt.Dimension(764, 35));
        panelSL.setPreferredSize(new java.awt.Dimension(764, 35));
        panelSL.setLayout(new java.awt.BorderLayout(30, 0));

        btnChonTour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/plus.png"))); // NOI18N
        btnChonTour.setText("  Chọn tour ");
        btnChonTour.setColorBottom(new java.awt.Color(153, 153, 153));
        btnChonTour.setColorTop(new java.awt.Color(153, 153, 153));
        btnChonTour.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        btnChonTour.setMaximumSize(new java.awt.Dimension(250, 35));
        btnChonTour.setMinimumSize(new java.awt.Dimension(0, 0));
        btnChonTour.setPreferredSize(new java.awt.Dimension(250, 35));
        btnChonTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonTourActionPerformed(evt);
            }
        });
        panelSL.add(btnChonTour, java.awt.BorderLayout.EAST);

        txtSoLuong.setForeground(new java.awt.Color(51, 51, 51));
        txtSoLuong.setColorBottom(new java.awt.Color(243, 243, 243));
        txtSoLuong.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtSoLuong.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtSoLuong.setColorTop(new java.awt.Color(243, 243, 243));
        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSoLuong.setMaximumSize(new java.awt.Dimension(232323, 35));
        txtSoLuong.setPreferredSize(new java.awt.Dimension(485, 35));
        txtSoLuong.setSelectionColor(new java.awt.Color(204, 204, 204));
        panelSL.add(txtSoLuong, java.awt.BorderLayout.CENTER);

        panelDSHD.add(panelSL);

        panelTrong1.setBackground(new java.awt.Color(255, 255, 255));
        panelTrong1.setMaximumSize(new java.awt.Dimension(764, 25));
        panelTrong1.setMinimumSize(new java.awt.Dimension(764, 25));
        panelTrong1.setPreferredSize(new java.awt.Dimension(764, 25));

        javax.swing.GroupLayout panelTrong1Layout = new javax.swing.GroupLayout(panelTrong1);
        panelTrong1.setLayout(panelTrong1Layout);
        panelTrong1Layout.setHorizontalGroup(
                panelTrong1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 764, Short.MAX_VALUE));
        panelTrong1Layout.setVerticalGroup(
                panelTrong1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelDSHD.add(panelTrong1);

        lblDanhSachTour1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblDanhSachTour1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDanhSachTour1.setText("CHI TIẾT HÓA ĐƠN");
        lblDanhSachTour1.setAlignmentX(0.5F);
        lblDanhSachTour1.setMaximumSize(new java.awt.Dimension(200, 35));
        lblDanhSachTour1.setMinimumSize(new java.awt.Dimension(200, 35));
        lblDanhSachTour1.setPreferredSize(new java.awt.Dimension(200, 35));
        panelDSHD.add(lblDanhSachTour1);

        jScrollPaneCTHD.setMaximumSize(new java.awt.Dimension(32767, 280));
        jScrollPaneCTHD.setMinimumSize(new java.awt.Dimension(767, 280));
        jScrollPaneCTHD.setPreferredSize(new java.awt.Dimension(767, 280));

        tableCTHD.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        tableCTHD.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã tour", "Tên tour", "Số lượng vé", "Đơn giá", "Thành tiền"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class,
                    java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableCTHD.setRowHeight(25);
        tableCTHD.setShowGrid(false);
        jScrollPaneCTHD.setViewportView(tableCTHD);

        panelDSHD.add(jScrollPaneCTHD);

        tabDatVe.add(panelDSHD, java.awt.BorderLayout.CENTER);

        panelTrong.setBackground(new java.awt.Color(255, 255, 255));
        panelTrong.setMaximumSize(new java.awt.Dimension(50, 32767));
        panelTrong.setMinimumSize(new java.awt.Dimension(30, 879));
        panelTrong.setPreferredSize(new java.awt.Dimension(50, 905));

        javax.swing.GroupLayout panelTrongLayout = new javax.swing.GroupLayout(panelTrong);
        panelTrong.setLayout(panelTrongLayout);
        panelTrongLayout.setHorizontalGroup(
                panelTrongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 50, Short.MAX_VALUE));
        panelTrongLayout.setVerticalGroup(
                panelTrongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 930, Short.MAX_VALUE));

        tabDatVe.add(panelTrong, java.awt.BorderLayout.EAST);

        tabbedPaneQLDatVe.addTab("Đặt vé", tabDatVe);

        tabQLHD.setBackground(new java.awt.Color(255, 255, 255));
        tabQLHD.setLayout(new java.awt.BorderLayout(20, 0));

        panelTimKiemHD.setBackground(new java.awt.Color(243, 243, 243));
        panelTimKiemHD.setMaximumSize(new java.awt.Dimension(400, 32767));
        panelTimKiemHD.setMinimumSize(new java.awt.Dimension(400, 879));
        panelTimKiemHD.setPreferredSize(new java.awt.Dimension(400, 879));
        panelTimKiemHD.setRequestFocusEnabled(false);
        panelTimKiemHD.setLayout(new javax.swing.BoxLayout(panelTimKiemHD, javax.swing.BoxLayout.Y_AXIS));

        panelTrong14.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong14.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong14.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong14Layout = new javax.swing.GroupLayout(panelTrong14);
        panelTrong14.setLayout(panelTrong14Layout);
        panelTrong14Layout.setHorizontalGroup(
                panelTrong14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong14Layout.setVerticalGroup(
                panelTrong14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong14);

        lblTimKiemHD.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTimKiemHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTimKiemHD.setText("Tìm kiếm hóa đơn");
        lblTimKiemHD.setAlignmentX(0.5F);
        lblTimKiemHD.setMaximumSize(new java.awt.Dimension(250, 55));
        lblTimKiemHD.setMinimumSize(new java.awt.Dimension(150, 35));
        lblTimKiemHD.setPreferredSize(new java.awt.Dimension(250, 55));
        panelTimKiemHD.add(lblTimKiemHD);

        panelTrong13.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong13.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong13.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong13Layout = new javax.swing.GroupLayout(panelTrong13);
        panelTrong13.setLayout(panelTrong13Layout);
        panelTrong13Layout.setHorizontalGroup(
                panelTrong13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong13Layout.setVerticalGroup(
                panelTrong13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong13);

        lblMaHD2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblMaHD2.setText("Mã hóa đơn");
        lblMaHD2.setAlignmentX(0.5F);
        lblMaHD2.setMaximumSize(new java.awt.Dimension(350, 35));
        lblMaHD2.setMinimumSize(new java.awt.Dimension(390, 35));
        lblMaHD2.setPreferredSize(new java.awt.Dimension(350, 35));
        panelTimKiemHD.add(lblMaHD2);

        txtMaHDTK.setForeground(new java.awt.Color(51, 51, 51));
        txtMaHDTK.setColorBottom(new java.awt.Color(255, 255, 255));
        txtMaHDTK.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtMaHDTK.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtMaHDTK.setColorTop(new java.awt.Color(255, 255, 255));
        txtMaHDTK.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMaHDTK.setMaximumSize(new java.awt.Dimension(350, 35));
        txtMaHDTK.setPreferredSize(new java.awt.Dimension(350, 35));
        txtMaHDTK.setSelectionColor(new java.awt.Color(238, 238, 238));
        panelTimKiemHD.add(txtMaHDTK);

        panelTrong9.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong9.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong9.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong9Layout = new javax.swing.GroupLayout(panelTrong9);
        panelTrong9.setLayout(panelTrong9Layout);
        panelTrong9Layout.setHorizontalGroup(
                panelTrong9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong9Layout.setVerticalGroup(
                panelTrong9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong9);

        lblMaKH2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblMaKH2.setText("Mã khách hàng");
        lblMaKH2.setAlignmentX(0.5F);
        lblMaKH2.setMaximumSize(new java.awt.Dimension(350, 35));
        lblMaKH2.setMinimumSize(new java.awt.Dimension(390, 35));
        lblMaKH2.setPreferredSize(new java.awt.Dimension(350, 35));
        panelTimKiemHD.add(lblMaKH2);

        txtMaKHTK.setForeground(new java.awt.Color(51, 51, 51));
        txtMaKHTK.setColorBottom(new java.awt.Color(255, 255, 255));
        txtMaKHTK.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtMaKHTK.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtMaKHTK.setColorTop(new java.awt.Color(255, 255, 255));
        txtMaKHTK.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMaKHTK.setMaximumSize(new java.awt.Dimension(350, 35));
        txtMaKHTK.setPreferredSize(new java.awt.Dimension(350, 35));
        txtMaKHTK.setSelectionColor(new java.awt.Color(238, 238, 238));
        panelTimKiemHD.add(txtMaKHTK);

        panelTrong10.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong10.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong10.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong10Layout = new javax.swing.GroupLayout(panelTrong10);
        panelTrong10.setLayout(panelTrong10Layout);
        panelTrong10Layout.setHorizontalGroup(
                panelTrong10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong10Layout.setVerticalGroup(
                panelTrong10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong10);

        lblSDT2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblSDT2.setText("Số điện thoại");
        lblSDT2.setAlignmentX(0.5F);
        lblSDT2.setMaximumSize(new java.awt.Dimension(350, 35));
        lblSDT2.setMinimumSize(new java.awt.Dimension(390, 35));
        lblSDT2.setPreferredSize(new java.awt.Dimension(350, 35));
        panelTimKiemHD.add(lblSDT2);

        txtSDTTK.setForeground(new java.awt.Color(51, 51, 51));
        txtSDTTK.setColorBottom(new java.awt.Color(255, 255, 255));
        txtSDTTK.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtSDTTK.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtSDTTK.setColorTop(new java.awt.Color(255, 255, 255));
        txtSDTTK.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSDTTK.setMaximumSize(new java.awt.Dimension(350, 35));
        txtSDTTK.setPreferredSize(new java.awt.Dimension(350, 35));
        txtSDTTK.setSelectionColor(new java.awt.Color(238, 238, 238));
        panelTimKiemHD.add(txtSDTTK);

        panelTrong11.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong11.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong11.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong11Layout = new javax.swing.GroupLayout(panelTrong11);
        panelTrong11.setLayout(panelTrong11Layout);
        panelTrong11Layout.setHorizontalGroup(
                panelTrong11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong11Layout.setVerticalGroup(
                panelTrong11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong11);

        lblTTTT.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblTTTT.setText("Trạng thái thanh toán");
        lblTTTT.setAlignmentX(0.5F);
        lblTTTT.setMaximumSize(new java.awt.Dimension(350, 35));
        lblTTTT.setMinimumSize(new java.awt.Dimension(390, 35));
        lblTTTT.setPreferredSize(new java.awt.Dimension(350, 35));
        panelTimKiemHD.add(lblTTTT);

        cmboxTTTT.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmboxTTTT.setMaximumSize(new java.awt.Dimension(350, 32));
        cmboxTTTT.setMinimumSize(new java.awt.Dimension(350, 32));
        cmboxTTTT.setPreferredSize(new java.awt.Dimension(350, 32));
        panelTimKiemHD.add(cmboxTTTT);

        panelTrong12.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong12.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong12.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong12Layout = new javax.swing.GroupLayout(panelTrong12);
        panelTrong12.setLayout(panelTrong12Layout);
        panelTrong12Layout.setHorizontalGroup(
                panelTrong12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong12Layout.setVerticalGroup(
                panelTrong12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong12);

        lblNgayLap2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        lblNgayLap2.setText("Ngày lập");
        lblNgayLap2.setAlignmentX(0.5F);
        lblNgayLap2.setMaximumSize(new java.awt.Dimension(350, 35));
        lblNgayLap2.setMinimumSize(new java.awt.Dimension(390, 35));
        lblNgayLap2.setPreferredSize(new java.awt.Dimension(350, 35));
        panelTimKiemHD.add(lblNgayLap2);

        txtNgayLap2.setForeground(new java.awt.Color(51, 51, 51));
        txtNgayLap2.setColorBottom(new java.awt.Color(255, 255, 255));
        txtNgayLap2.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtNgayLap2.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtNgayLap2.setColorTop(new java.awt.Color(255, 255, 255));
        txtNgayLap2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtNgayLap2.setMaximumSize(new java.awt.Dimension(350, 35));
        txtNgayLap2.setPreferredSize(new java.awt.Dimension(350, 35));
        txtNgayLap2.setSelectionColor(new java.awt.Color(238, 238, 238));
        panelTimKiemHD.add(txtNgayLap2);

        panelTrong3.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong3.setMaximumSize(new java.awt.Dimension(350, 35));
        panelTrong3.setMinimumSize(new java.awt.Dimension(350, 35));

        javax.swing.GroupLayout panelTrong3Layout = new javax.swing.GroupLayout(panelTrong3);
        panelTrong3.setLayout(panelTrong3Layout);
        panelTrong3Layout.setHorizontalGroup(
                panelTrong3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong3Layout.setVerticalGroup(
                panelTrong3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 35, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong3);

        panelTrong15.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong15.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong15.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong15Layout = new javax.swing.GroupLayout(panelTrong15);
        panelTrong15.setLayout(panelTrong15Layout);
        panelTrong15Layout.setHorizontalGroup(
                panelTrong15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong15Layout.setVerticalGroup(
                panelTrong15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong15);

        bthTimKiemHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/find.png"))); // NOI18N
        bthTimKiemHD.setText(" Tìm kiếm");
        bthTimKiemHD.setAlignmentX(0.5F);
        bthTimKiemHD.setColorBottom(new java.awt.Color(0, 0, 0));
        bthTimKiemHD.setColorTop(new java.awt.Color(51, 51, 51));
        bthTimKiemHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bthTimKiemHD.setMaximumSize(new java.awt.Dimension(350, 40));
        bthTimKiemHD.setPreferredSize(new java.awt.Dimension(350, 40));
        bthTimKiemHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bthTimKiemHDActionPerformed(evt);
            }
        });
        panelTimKiemHD.add(bthTimKiemHD);

        panelTrong6.setBackground(new java.awt.Color(243, 243, 243));
        panelTrong6.setMaximumSize(new java.awt.Dimension(350, 25));
        panelTrong6.setMinimumSize(new java.awt.Dimension(350, 25));

        javax.swing.GroupLayout panelTrong6Layout = new javax.swing.GroupLayout(panelTrong6);
        panelTrong6.setLayout(panelTrong6Layout);
        panelTrong6Layout.setHorizontalGroup(
                panelTrong6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 350, Short.MAX_VALUE));
        panelTrong6Layout.setVerticalGroup(
                panelTrong6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 25, Short.MAX_VALUE));

        panelTimKiemHD.add(panelTrong6);

        btnLamMoiTKHD.setForeground(new java.awt.Color(0, 0, 0));
        btnLamMoiTKHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload.png"))); // NOI18N
        btnLamMoiTKHD.setText(" Làm mới");
        btnLamMoiTKHD.setAlignmentX(0.5F);
        btnLamMoiTKHD.setColorBottom(new java.awt.Color(255, 255, 255));
        btnLamMoiTKHD.setColorTop(new java.awt.Color(255, 255, 255));
        btnLamMoiTKHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLamMoiTKHD.setMaximumSize(new java.awt.Dimension(350, 40));
        btnLamMoiTKHD.setPreferredSize(new java.awt.Dimension(350, 40));
        btnLamMoiTKHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiTKHDActionPerformed(evt);
            }
        });
        panelTimKiemHD.add(btnLamMoiTKHD);

        tabQLHD.add(panelTimKiemHD, java.awt.BorderLayout.WEST);

        panelRight.setBackground(new java.awt.Color(255, 255, 255));
        panelRight.setMaximumSize(new java.awt.Dimension(32767, 212112));
        panelRight.setMinimumSize(new java.awt.Dimension(950, 879));
        panelRight.setPreferredSize(new java.awt.Dimension(950, 879));
        panelRight.setLayout(new javax.swing.BoxLayout(panelRight, javax.swing.BoxLayout.Y_AXIS));

        lblDSHD.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDSHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDSHD.setText("DANH SÁCH HÓA ĐƠN");
        lblDSHD.setAlignmentX(0.5F);
        lblDSHD.setMaximumSize(new java.awt.Dimension(900, 55));
        lblDSHD.setMinimumSize(new java.awt.Dimension(0, 0));
        lblDSHD.setPreferredSize(new java.awt.Dimension(900, 55));
        panelRight.add(lblDSHD);

        jScrollPaneDSHD.setMaximumSize(new java.awt.Dimension(32767, 280));
        jScrollPaneDSHD.setMinimumSize(new java.awt.Dimension(300, 200));
        jScrollPaneDSHD.setPreferredSize(new java.awt.Dimension(950, 250));

        tableDSHD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tableDSHD.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã hóa đơn", "Mã nhân viên", "Mã khách hàng", "Số điện thoại", "Ngày lập", "Tổng tiền",
                        "Mã khuyến mãi", "Hình thức thanh toán", "Trạng thái thanh toán"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class,
                    java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableDSHD.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableDSHD.setRowHeight(25);
        tableDSHD.setShowGrid(false);
        tableDSHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDSHDMouseClicked(evt);
            }
        });
        jScrollPaneDSHD.setViewportView(tableDSHD);

        panelRight.add(jScrollPaneDSHD);

        panelTrong7.setBackground(new java.awt.Color(255, 255, 252));
        panelTrong7.setForeground(new java.awt.Color(0, 51, 153));
        panelTrong7.setMaximumSize(new java.awt.Dimension(600, 15));
        panelTrong7.setMinimumSize(new java.awt.Dimension(600, 15));
        panelTrong7.setPreferredSize(new java.awt.Dimension(600, 15));

        javax.swing.GroupLayout panelTrong7Layout = new javax.swing.GroupLayout(panelTrong7);
        panelTrong7.setLayout(panelTrong7Layout);
        panelTrong7Layout.setHorizontalGroup(
                panelTrong7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        panelTrong7Layout.setVerticalGroup(
                panelTrong7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE));

        panelRight.add(panelTrong7);

        lblCTHD2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblCTHD2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCTHD2.setText("CHI TIẾT HÓA ĐƠN");
        lblCTHD2.setAlignmentX(0.5F);
        lblCTHD2.setMaximumSize(new java.awt.Dimension(200, 35));
        lblCTHD2.setMinimumSize(new java.awt.Dimension(0, 0));
        lblCTHD2.setPreferredSize(new java.awt.Dimension(200, 35));
        panelRight.add(lblCTHD2);

        jScrollPaneCTHD2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneCTHD2.setMaximumSize(new java.awt.Dimension(32767, 200));
        jScrollPaneCTHD2.setMinimumSize(new java.awt.Dimension(300, 100));
        jScrollPaneCTHD2.setPreferredSize(new java.awt.Dimension(767, 130));

        tableCTHD2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tableCTHD2.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã hóa đơn", "Mã tour", "Tên tour", "Số lượng vé", "Đơn giá", "Thành tiền", "Trạng thái"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class,
                    java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableCTHD2.setRowHeight(25);
        tableCTHD2.setShowGrid(false);
        tableCTHD2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCTHD2MouseClicked(evt);
            }
        });
        tableCTHD2.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    @Override
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        if (!evt.getValueIsAdjusting()) {
                            capNhatThongTinHuyVe();
                        }
                    }
                });
        jScrollPaneCTHD2.setViewportView(tableCTHD2);

        panelRight.add(jScrollPaneCTHD2);

        panelTrong17.setBackground(new java.awt.Color(255, 255, 252));
        panelTrong17.setForeground(new java.awt.Color(0, 51, 153));
        panelTrong17.setMaximumSize(new java.awt.Dimension(600, 15));
        panelTrong17.setMinimumSize(new java.awt.Dimension(600, 15));

        javax.swing.GroupLayout panelTrong17Layout = new javax.swing.GroupLayout(panelTrong17);
        panelTrong17.setLayout(panelTrong17Layout);
        panelTrong17Layout.setHorizontalGroup(
                panelTrong17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        panelTrong17Layout.setVerticalGroup(
                panelTrong17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE));

        panelRight.add(panelTrong17);

        panelbtnChucNang.setBackground(new java.awt.Color(255, 255, 255));
        panelbtnChucNang.setMaximumSize(new java.awt.Dimension(213323, 70));
        panelbtnChucNang.setMinimumSize(new java.awt.Dimension(0, 0));
        panelbtnChucNang.setPreferredSize(new java.awt.Dimension(200, 45));
        panelbtnChucNang.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 0));

        btnCNXemHD.setForeground(new java.awt.Color(0, 0, 0));
        btnCNXemHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/view.png"))); // NOI18N
        btnCNXemHD.setText(" Xem hóa đơn");
        btnCNXemHD.setColorBottom(new java.awt.Color(204, 204, 204));
        btnCNXemHD.setColorTop(new java.awt.Color(255, 255, 255));
        btnCNXemHD.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnCNXemHD.setPreferredSize(new java.awt.Dimension(205, 45));
        btnCNXemHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNXemHDActionPerformed(evt);
            }
        });
        panelbtnChucNang.add(btnCNXemHD);

        btnCNThanhToan.setForeground(new java.awt.Color(0, 0, 0));
        btnCNThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/wallet_btnCN.png"))); // NOI18N
        btnCNThanhToan.setText(" Thanh toán");
        btnCNThanhToan.setColorBottom(new java.awt.Color(184, 214, 239));
        btnCNThanhToan.setColorTop(new java.awt.Color(248, 251, 255));
        btnCNThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnCNThanhToan.setPreferredSize(new java.awt.Dimension(205, 45));
        btnCNThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNThanhToanActionPerformed(evt);
            }
        });
        panelbtnChucNang.add(btnCNThanhToan);

        btnCNHuyVe.setForeground(new java.awt.Color(0, 0, 0));
        btnCNHuyVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/delete_btnCN.png"))); // NOI18N
        btnCNHuyVe.setText(" Hủy vé");
        btnCNHuyVe.setColorBottom(new java.awt.Color(239, 184, 184));
        btnCNHuyVe.setColorTop(new java.awt.Color(255, 248, 248));
        btnCNHuyVe.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnCNHuyVe.setPreferredSize(new java.awt.Dimension(205, 45));
        btnCNHuyVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNHuyVeActionPerformed(evt);
            }
        });
        panelbtnChucNang.add(btnCNHuyVe);

        panelTrong16.setBackground(new java.awt.Color(255, 255, 252));
        panelTrong16.setForeground(new java.awt.Color(0, 51, 153));
        panelTrong16.setMaximumSize(new java.awt.Dimension(600, 15));
        panelTrong16.setMinimumSize(new java.awt.Dimension(600, 15));

        javax.swing.GroupLayout panelTrong16Layout = new javax.swing.GroupLayout(panelTrong16);
        panelTrong16.setLayout(panelTrong16Layout);
        panelTrong16Layout.setHorizontalGroup(
                panelTrong16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        panelTrong16Layout.setVerticalGroup(
                panelTrong16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE));

        panelbtnChucNang.add(panelTrong16);

        panelRight.add(panelbtnChucNang);

        panelChucNang.setBackground(new java.awt.Color(255, 255, 255));
        panelChucNang.setMaximumSize(new java.awt.Dimension(32767, 310));
        panelChucNang.setMinimumSize(new java.awt.Dimension(200, 100));
        panelChucNang.setPreferredSize(new java.awt.Dimension(950, 310));
        panelChucNang.setLayout(new java.awt.CardLayout());

        panelXemDSHD.setBackground(new java.awt.Color(255, 255, 255));
        panelXemDSHD.setMaximumSize(new java.awt.Dimension(32767, 310));
        panelXemDSHD.setMinimumSize(new java.awt.Dimension(950, 310));
        panelXemDSHD.setPreferredSize(new java.awt.Dimension(950, 310));
        panelXemDSHD.setLayout(new javax.swing.BoxLayout(panelXemDSHD, javax.swing.BoxLayout.Y_AXIS));

        panelTrong19.setBackground(new java.awt.Color(255, 255, 252));
        panelTrong19.setForeground(new java.awt.Color(0, 51, 153));
        panelTrong19.setMaximumSize(new java.awt.Dimension(600, 15));
        panelTrong19.setMinimumSize(new java.awt.Dimension(600, 15));

        javax.swing.GroupLayout panelTrong19Layout = new javax.swing.GroupLayout(panelTrong19);
        panelTrong19.setLayout(panelTrong19Layout);
        panelTrong19Layout.setHorizontalGroup(
                panelTrong19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        panelTrong19Layout.setVerticalGroup(
                panelTrong19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE));

        panelXemDSHD.add(panelTrong19);

        lblThongBaoHoanTien.setFont(new java.awt.Font("Segoe UI Semibold", 3, 22)); // NOI18N
        lblThongBaoHoanTien.setForeground(new java.awt.Color(255, 51, 51));
        lblThongBaoHoanTien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThongBaoHoanTien.setAlignmentX(0.5F);
        lblThongBaoHoanTien.setAlignmentY(0.0F);
        lblThongBaoHoanTien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblThongBaoHoanTien.setMaximumSize(new java.awt.Dimension(232323, 30));
        lblThongBaoHoanTien.setPreferredSize(new java.awt.Dimension(700, 30));
        panelXemDSHD.add(lblThongBaoHoanTien);

        panelTrong20.setBackground(new java.awt.Color(255, 255, 252));
        panelTrong20.setForeground(new java.awt.Color(0, 51, 153));
        panelTrong20.setMaximumSize(new java.awt.Dimension(600, 15));
        panelTrong20.setMinimumSize(new java.awt.Dimension(600, 15));

        javax.swing.GroupLayout panelTrong20Layout = new javax.swing.GroupLayout(panelTrong20);
        panelTrong20.setLayout(panelTrong20Layout);
        panelTrong20Layout.setHorizontalGroup(
                panelTrong20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        panelTrong20Layout.setVerticalGroup(
                panelTrong20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE));

        panelXemDSHD.add(panelTrong20);

        btnXemDSCanHoanTien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnXemDSCanHoanTien.setForeground(new java.awt.Color(0, 0, 0));
        btnXemDSCanHoanTien.setText("Nhấn để xem");
        btnXemDSCanHoanTien.setAlignmentX(0.5F);
        btnXemDSCanHoanTien.setAlignmentY(0.0F);
        btnXemDSCanHoanTien.setColorBottom(new java.awt.Color(255, 255, 255));
        btnXemDSCanHoanTien.setColorTop(new java.awt.Color(255, 255, 255));
        btnXemDSCanHoanTien.setFont(new java.awt.Font("Segoe UI", 2, 20)); // NOI18N
        btnXemDSCanHoanTien.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnXemDSCanHoanTien.setMaximumSize(new java.awt.Dimension(300, 34));
        btnXemDSCanHoanTien.setPreferredSize(new java.awt.Dimension(700, 45));
        btnXemDSCanHoanTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemDSCanHoanTienActionPerformed(evt);
            }
        });
        panelXemDSHD.add(btnXemDSCanHoanTien);

        panelChucNang.add(panelXemDSHD, "card1");

        panelThaoTacThanhToan.setBackground(new java.awt.Color(255, 255, 255));
        panelThaoTacThanhToan.setMaximumSize(new java.awt.Dimension(200000, 310));
        panelThaoTacThanhToan.setMinimumSize(new java.awt.Dimension(200, 100));
        panelThaoTacThanhToan.setPreferredSize(new java.awt.Dimension(950, 280));
        panelThaoTacThanhToan.setLayout(new javax.swing.BoxLayout(panelThaoTacThanhToan, javax.swing.BoxLayout.Y_AXIS));

        lblThaoTacThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblThaoTacThanhToan.setForeground(new java.awt.Color(0, 51, 102));
        lblThaoTacThanhToan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThaoTacThanhToan.setText("  Thao tác thanh toán");
        lblThaoTacThanhToan.setAlignmentX(0.5F);
        lblThaoTacThanhToan.setMaximumSize(new java.awt.Dimension(20000, 40));
        lblThaoTacThanhToan.setMinimumSize(new java.awt.Dimension(0, 0));
        lblThaoTacThanhToan.setPreferredSize(new java.awt.Dimension(930, 40));
        panelThaoTacThanhToan.add(lblThaoTacThanhToan);

        panelXacNhanThanhToan.setBackground(new java.awt.Color(255, 255, 252));
        panelXacNhanThanhToan.setMaximumSize(new java.awt.Dimension(32767, 180));
        panelXacNhanThanhToan.setMinimumSize(new java.awt.Dimension(0, 0));
        panelXacNhanThanhToan.setPreferredSize(new java.awt.Dimension(948, 180));
        panelXacNhanThanhToan.setLayout(new java.awt.BorderLayout(30, 0));

        panelXacNhanTTThanhToan.setBackground(new java.awt.Color(228, 241, 255));
        panelXacNhanTTThanhToan.setMaximumSize(new java.awt.Dimension(20000, 130));
        panelXacNhanTTThanhToan.setPreferredSize(new java.awt.Dimension(548, 130));
        panelXacNhanTTThanhToan.setLayout(new java.awt.GridLayout(4, 2, 0, 7));

        lblMaHoaDonTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblMaHoaDonTT.setText("Mã hóa đơn");
        lblMaHoaDonTT.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblMaHoaDonTT.setMinimumSize(new java.awt.Dimension(548, 30));
        lblMaHoaDonTT.setPreferredSize(new java.awt.Dimension(548, 30));
        panelXacNhanTTThanhToan.add(lblMaHoaDonTT);

        txtMaHDTT.setBackground(new java.awt.Color(228, 241, 255));
        txtMaHDTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtMaHDTT.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaHDTT.setBorder(null);
        txtMaHDTT.setMaximumSize(new java.awt.Dimension(390, 35));
        txtMaHDTT.setMinimumSize(new java.awt.Dimension(390, 35));
        txtMaHDTT.setPreferredSize(new java.awt.Dimension(390, 35));
        panelXacNhanTTThanhToan.add(txtMaHDTT);

        lblMaKHTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblMaKHTT.setText("Mã khách hàng");
        lblMaKHTT.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblMaKHTT.setMinimumSize(new java.awt.Dimension(548, 30));
        lblMaKHTT.setPreferredSize(new java.awt.Dimension(548, 30));
        panelXacNhanTTThanhToan.add(lblMaKHTT);

        txtMaKHTT.setBackground(new java.awt.Color(228, 241, 255));
        txtMaKHTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtMaKHTT.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaKHTT.setBorder(null);
        txtMaKHTT.setMaximumSize(new java.awt.Dimension(390, 35));
        txtMaKHTT.setMinimumSize(new java.awt.Dimension(390, 35));
        txtMaKHTT.setPreferredSize(new java.awt.Dimension(390, 35));
        panelXacNhanTTThanhToan.add(txtMaKHTT);

        lblSDTTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblSDTTT.setText("SĐT");
        lblSDTTT.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblSDTTT.setMinimumSize(new java.awt.Dimension(548, 30));
        lblSDTTT.setPreferredSize(new java.awt.Dimension(548, 30));
        panelXacNhanTTThanhToan.add(lblSDTTT);

        txtSDTTT.setBackground(new java.awt.Color(228, 241, 255));
        txtSDTTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtSDTTT.setForeground(new java.awt.Color(51, 204, 0));
        txtSDTTT.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSDTTT.setBorder(null);
        txtSDTTT.setMaximumSize(new java.awt.Dimension(390, 35));
        txtSDTTT.setMinimumSize(new java.awt.Dimension(390, 35));
        txtSDTTT.setPreferredSize(new java.awt.Dimension(390, 35));
        panelXacNhanTTThanhToan.add(txtSDTTT);

        lblTongTT.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTongTT.setText("Tổng thanh toán:");
        lblTongTT.setMaximumSize(new java.awt.Dimension(200000, 30));
        lblTongTT.setMinimumSize(new java.awt.Dimension(548, 30));
        lblTongTT.setPreferredSize(new java.awt.Dimension(600, 30));
        panelXacNhanTTThanhToan.add(lblTongTT);

        txtTongThanhToan2.setBackground(new java.awt.Color(228, 241, 255));
        txtTongThanhToan2.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        txtTongThanhToan2.setForeground(new java.awt.Color(0, 102, 204));
        txtTongThanhToan2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongThanhToan2.setText("0đ");
        txtTongThanhToan2.setBorder(null);
        txtTongThanhToan2.setMaximumSize(new java.awt.Dimension(390, 35));
        txtTongThanhToan2.setMinimumSize(new java.awt.Dimension(390, 35));
        txtTongThanhToan2.setPreferredSize(new java.awt.Dimension(390, 35));
        panelXacNhanTTThanhToan.add(txtTongThanhToan2);

        panelXacNhanThanhToan.add(panelXacNhanTTThanhToan, java.awt.BorderLayout.CENTER);

        panelHTTT.setBackground(new java.awt.Color(255, 255, 252));
        panelHTTT.setMaximumSize(new java.awt.Dimension(32767, 130));
        panelHTTT.setMinimumSize(new java.awt.Dimension(473, 130));
        panelHTTT.setPreferredSize(new java.awt.Dimension(400, 130));
        panelHTTT.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        lblHTTT.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblHTTT.setText("Hình thức thanh toán *");
        lblHTTT.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblHTTT.setAlignmentX(0.5F);
        lblHTTT.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblHTTT.setPreferredSize(new java.awt.Dimension(380, 30));
        panelHTTT.add(lblHTTT);

        radiobtnTienMat.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupHTTT.add(radiobtnTienMat);
        radiobtnTienMat.setFont(new java.awt.Font("Segoe UI Semilight", 1, 18)); // NOI18N
        radiobtnTienMat.setText("Tiền mặt");
        radiobtnTienMat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radiobtnTienMat.setMaximumSize(new java.awt.Dimension(3212, 30));
        radiobtnTienMat.setMinimumSize(new java.awt.Dimension(20, 20));
        radiobtnTienMat.setPreferredSize(new java.awt.Dimension(120, 30));
        panelHTTT.add(radiobtnTienMat);

        radiobtnChuyenKhoan.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupHTTT.add(radiobtnChuyenKhoan);
        radiobtnChuyenKhoan.setFont(new java.awt.Font("Segoe UI Semilight", 1, 18)); // NOI18N
        radiobtnChuyenKhoan.setText("Chuyển khoản");
        radiobtnChuyenKhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radiobtnChuyenKhoan.setMaximumSize(new java.awt.Dimension(122339, 30));
        radiobtnChuyenKhoan.setMinimumSize(new java.awt.Dimension(20, 20));
        radiobtnChuyenKhoan.setPreferredSize(new java.awt.Dimension(150, 30));
        panelHTTT.add(radiobtnChuyenKhoan);

        panelXacNhanThanhToan.add(panelHTTT, java.awt.BorderLayout.EAST);

        panelThaoTacThanhToan.add(panelXacNhanThanhToan);

        panelTrong23.setBackground(new java.awt.Color(255, 255, 252));
        panelTrong23.setForeground(new java.awt.Color(0, 51, 153));
        panelTrong23.setMaximumSize(new java.awt.Dimension(600, 15));
        panelTrong23.setMinimumSize(new java.awt.Dimension(600, 15));

        javax.swing.GroupLayout panelTrong23Layout = new javax.swing.GroupLayout(panelTrong23);
        panelTrong23.setLayout(panelTrong23Layout);
        panelTrong23Layout.setHorizontalGroup(
                panelTrong23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        panelTrong23Layout.setVerticalGroup(
                panelTrong23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE));

        panelThaoTacThanhToan.add(panelTrong23);

        btnThanhToan.setBackground(new java.awt.Color(0, 51, 102));
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/wallet.png"))); // NOI18N
        btnThanhToan.setText("  Thanh toán");
        btnThanhToan.setAlignmentX(0.5F);
        btnThanhToan.setColorBottom(new java.awt.Color(0, 51, 102));
        btnThanhToan.setColorTop(new java.awt.Color(0, 51, 102));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnThanhToan.setMaximumSize(new java.awt.Dimension(930, 40));
        btnThanhToan.setPreferredSize(new java.awt.Dimension(930, 40));
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });
        panelThaoTacThanhToan.add(btnThanhToan);

        panelChucNang.add(panelThaoTacThanhToan, "card2");

        panelThaoTacHuyVe.setBackground(new java.awt.Color(255, 255, 252));
        panelThaoTacHuyVe.setMaximumSize(new java.awt.Dimension(200000, 310));
        panelThaoTacHuyVe.setMinimumSize(new java.awt.Dimension(200, 100));
        panelThaoTacHuyVe.setPreferredSize(new java.awt.Dimension(952, 310));
        panelThaoTacHuyVe.setLayout(new javax.swing.BoxLayout(panelThaoTacHuyVe, javax.swing.BoxLayout.Y_AXIS));

        lblThaoTacHuyVe.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        lblThaoTacHuyVe.setForeground(new java.awt.Color(204, 0, 0));
        lblThaoTacHuyVe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThaoTacHuyVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/warning.png"))); // NOI18N
        lblThaoTacHuyVe.setText("  Thao tác hủy vé");
        lblThaoTacHuyVe.setAlignmentX(0.5F);
        lblThaoTacHuyVe.setMaximumSize(new java.awt.Dimension(20000, 55));
        lblThaoTacHuyVe.setMinimumSize(new java.awt.Dimension(100, 15));
        lblThaoTacHuyVe.setPreferredSize(new java.awt.Dimension(930, 55));
        panelThaoTacHuyVe.add(lblThaoTacHuyVe);

        panelThongTinHuyVe.setBackground(new java.awt.Color(255, 255, 252));
        panelThongTinHuyVe.setMaximumSize(new java.awt.Dimension(32767, 170));
        panelThongTinHuyVe.setPreferredSize(new java.awt.Dimension(948, 170));
        panelThongTinHuyVe.setLayout(new java.awt.BorderLayout(30, 0));

        panelLyDoHuy.setBackground(new java.awt.Color(255, 255, 252));
        panelLyDoHuy.setMaximumSize(new java.awt.Dimension(32767, 130));
        panelLyDoHuy.setMinimumSize(new java.awt.Dimension(473, 130));
        panelLyDoHuy.setPreferredSize(new java.awt.Dimension(400, 130));
        panelLyDoHuy.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        lblLyDo.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblLyDo.setForeground(new java.awt.Color(153, 0, 0));
        lblLyDo.setText("Lý do hủy *");
        lblLyDo.setAlignmentX(0.5F);
        lblLyDo.setMaximumSize(new java.awt.Dimension(20000, 40));
        lblLyDo.setPreferredSize(new java.awt.Dimension(380, 40));
        panelLyDoHuy.add(lblLyDo);

        txtLyDoHuy.setForeground(new java.awt.Color(51, 51, 51));
        txtLyDoHuy.setColorBottom(new java.awt.Color(238, 238, 238));
        txtLyDoHuy.setColorHoverBottom(new java.awt.Color(204, 204, 204));
        txtLyDoHuy.setColorHoverTop(new java.awt.Color(204, 204, 204));
        txtLyDoHuy.setColorTop(new java.awt.Color(238, 238, 238));
        txtLyDoHuy.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtLyDoHuy.setMaximumSize(new java.awt.Dimension(380, 120));
        txtLyDoHuy.setPreferredSize(new java.awt.Dimension(380, 120));
        txtLyDoHuy.setSelectionColor(new java.awt.Color(238, 238, 238));
        panelLyDoHuy.add(txtLyDoHuy);

        panelThongTinHuyVe.add(panelLyDoHuy, java.awt.BorderLayout.WEST);

        panelTienHuyVe.setBackground(new java.awt.Color(255, 255, 252));
        panelTienHuyVe.setMaximumSize(new java.awt.Dimension(20000, 130));
        panelTienHuyVe.setPreferredSize(new java.awt.Dimension(548, 130));
        panelTienHuyVe.setLayout(new java.awt.GridLayout(3, 2, 50, 15));

        lblMaHDHuy.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblMaHDHuy.setText("Mã hóa đơn:");
        lblMaHDHuy.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblMaHDHuy.setMinimumSize(new java.awt.Dimension(548, 30));
        lblMaHDHuy.setPreferredSize(new java.awt.Dimension(548, 30));
        panelTienHuyVe.add(lblMaHDHuy);

        txtMaHDHuy.setBackground(new java.awt.Color(255, 255, 252));
        txtMaHDHuy.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtMaHDHuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaHDHuy.setBorder(null);
        txtMaHDHuy.setMaximumSize(new java.awt.Dimension(390, 35));
        txtMaHDHuy.setMinimumSize(new java.awt.Dimension(390, 35));
        txtMaHDHuy.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTienHuyVe.add(txtMaHDHuy);

        lblSoTourHuy.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        lblSoTourHuy.setText("Số tour hủy:");
        lblSoTourHuy.setMaximumSize(new java.awt.Dimension(20000, 30));
        lblSoTourHuy.setMinimumSize(new java.awt.Dimension(548, 30));
        lblSoTourHuy.setPreferredSize(new java.awt.Dimension(548, 30));
        panelTienHuyVe.add(lblSoTourHuy);

        txtSoTourHuy.setBackground(new java.awt.Color(255, 255, 252));
        txtSoTourHuy.setFont(new java.awt.Font("Segoe UI Semibold", 1, 20)); // NOI18N
        txtSoTourHuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSoTourHuy.setBorder(null);
        txtSoTourHuy.setMaximumSize(new java.awt.Dimension(390, 35));
        txtSoTourHuy.setMinimumSize(new java.awt.Dimension(390, 35));
        txtSoTourHuy.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTienHuyVe.add(txtSoTourHuy);

        lblSoTienHoan.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblSoTienHoan.setForeground(new java.awt.Color(255, 0, 0));
        lblSoTienHoan.setText("Số tiền hoàn trả:");
        lblSoTienHoan.setAlignmentX(0.5F);
        lblSoTienHoan.setMaximumSize(new java.awt.Dimension(20000, 40));
        lblSoTienHoan.setPreferredSize(new java.awt.Dimension(380, 40));
        panelTienHuyVe.add(lblSoTienHoan);

        txtSoTienHoan.setBackground(new java.awt.Color(255, 255, 252));
        txtSoTienHoan.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        txtSoTienHoan.setForeground(new java.awt.Color(255, 0, 0));
        txtSoTienHoan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSoTienHoan.setBorder(null);
        txtSoTienHoan.setMaximumSize(new java.awt.Dimension(390, 35));
        txtSoTienHoan.setMinimumSize(new java.awt.Dimension(390, 35));
        txtSoTienHoan.setPreferredSize(new java.awt.Dimension(390, 35));
        panelTienHuyVe.add(txtSoTienHoan);

        panelThongTinHuyVe.add(panelTienHuyVe, java.awt.BorderLayout.CENTER);

        panelThaoTacHuyVe.add(panelThongTinHuyVe);

        jPanel19.setBackground(new java.awt.Color(255, 255, 252));
        jPanel19.setMaximumSize(new java.awt.Dimension(600, 30));
        jPanel19.setMinimumSize(new java.awt.Dimension(600, 25));
        jPanel19.setPreferredSize(new java.awt.Dimension(600, 30));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE));
        jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE));

        panelThaoTacHuyVe.add(jPanel19);

        btnHuyVe.setBackground(new java.awt.Color(0, 51, 102));
        btnHuyVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/delete.png"))); // NOI18N
        btnHuyVe.setText("  Hủy vé");
        btnHuyVe.setAlignmentX(0.5F);
        btnHuyVe.setColorBottom(new java.awt.Color(255, 153, 153));
        btnHuyVe.setColorTop(new java.awt.Color(255, 153, 153));
        btnHuyVe.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnHuyVe.setMaximumSize(new java.awt.Dimension(930, 40));
        btnHuyVe.setPreferredSize(new java.awt.Dimension(930, 40));
        btnHuyVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyVeActionPerformed(evt);
            }
        });
        panelThaoTacHuyVe.add(btnHuyVe);

        panelChucNang.add(panelThaoTacHuyVe, "card3");

        panelRight.add(panelChucNang);

        tabQLHD.add(panelRight, java.awt.BorderLayout.CENTER);

        panelTrong5.setBackground(new java.awt.Color(255, 255, 255));
        panelTrong5.setMaximumSize(new java.awt.Dimension(10, 32767));
        panelTrong5.setMinimumSize(new java.awt.Dimension(0, 879));
        panelTrong5.setPreferredSize(new java.awt.Dimension(10, 905));

        javax.swing.GroupLayout panelTrong5Layout = new javax.swing.GroupLayout(panelTrong5);
        panelTrong5.setLayout(panelTrong5Layout);
        panelTrong5Layout.setHorizontalGroup(
                panelTrong5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 10, Short.MAX_VALUE));
        panelTrong5Layout.setVerticalGroup(
                panelTrong5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 930, Short.MAX_VALUE));

        tabQLHD.add(panelTrong5, java.awt.BorderLayout.EAST);

        tabbedPaneQLDatVe.addTab("Quản lý hóa đơn", tabQLHD);

        add(tabbedPaneQLDatVe, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnChonMaKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChonMaKHActionPerformed
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        KhachHangDialog dialog = new KhachHangDialog((JFrame) parentWindow, true);
        dialog.setVisible(true);
        String makh = dialog.getSelectedMaKH();

        if (makh != null) {
            txtMaKH.setText(makh);
        }
    }// GEN-LAST:event_btnChonMaKHActionPerformed

    private void btnChonTourActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChonTourActionPerformed
        int selectrow = tableDSTour.getSelectedRow();
        if (selectrow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tour trong danh sách!",
                    "Chưa chọn tour", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maTour = tableDSTour.getValueAt(selectrow, 0).toString();
        String tenTour = tableDSTour.getValueAt(selectrow, 1).toString();
        double donGia = Double.parseDouble(tableDSTour.getValueAt(selectrow, 4).toString());
        int soChoCon = Integer.parseInt(tableDSTour.getValueAt(selectrow, 5).toString());

        String slinput = txtSoLuong.getText().trim();
        if (slinput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng vé!");
            txtSoLuong.requestFocus();
            return;
        }
        int soLuong;
        try {
            soLuong = Integer.parseInt(slinput);
            if (soLuong <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng vé phải là số nguyên dương!");
            txtSoLuong.requestFocus();
            txtSoLuong.selectAll();
            return;
        }
        if (soChoCon == 0) {
            JOptionPane.showMessageDialog(this, "Tour này đã hết chỗ!");
            return;
        }
        if (soLuong > soChoCon) {
            JOptionPane.showMessageDialog(this,
                    "Không đủ chỗ! Chỉ còn " + soChoCon + " chỗ.");
            txtSoLuong.requestFocus();
            txtSoLuong.selectAll();
            return;
        }
        boolean daTonTai = false;
        int rowTonTai = -1;
        for (int i = 0; i < modelcthd.getRowCount(); i++) {
            String maTourCTHD = modelcthd.getValueAt(i, 0).toString();
            if (maTourCTHD.equals(maTour)) {
                daTonTai = true;
                rowTonTai = i;
                break;
            }
        }
        if (daTonTai) {
            int soLuongCu = (Integer) modelcthd.getValueAt(rowTonTai, 2);
            int soLuongTong = soLuongCu + soLuong;
            modelcthd.setValueAt(soLuongTong, rowTonTai, 2);
            double thanhTienMoi = soLuongTong * donGia;
            modelcthd.setValueAt(thanhTienMoi, rowTonTai, 4);
        } else {
            double thanhTien = soLuong * donGia;
            modelcthd.addRow(new Object[] { maTour, tenTour, soLuong, donGia, thanhTien });
        }
        fillTinhTien(modelcthd);
        txtSoLuong.setText("");
    }// GEN-LAST:event_btnChonTourActionPerformed

    private void btnTaoHoaDonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaoHoaDonActionPerformed
        try {
            if (txtMaKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
                return;
            }
            if (modelcthd.getRowCount() <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tour!");
                return;
            }
            String cmboxkm = String.valueOf(cmboxKM.getSelectedItem());
            String[] partscmboxmakm = cmboxkm.split("-");
            String makm = String.valueOf(partscmboxmakm[0].trim());

            HoaDon hd = new HoaDon();
            hd.setMaNhanVien(String.valueOf(txtMaNV.getText()));
            hd.setMaKhachHang(String.valueOf(txtMaKH.getText()));
            hd.setNgayLapHD(LocalDate.parse(txtNgayLap.getText()));
            hd.setTongTien(Double.parseDouble(txtTongThanhToan.getText().replace(",", "")));
            hd.setMaKhuyenMai(makm);
            hd.setThue(thue);
            hd.setTrangThaiTT(false);

            int kq = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn tạo hóa đơn không?",
                    "XÁC NHẬN TẠO HÓA ĐƠN",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (kq == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, hdbus.insertHoaDon(hd));
                String maHD = hd.getMaHoaDon();
                txtMaHD.setText(hd.getMaHoaDon());
                ArrayList<CTHD> dscthd = new ArrayList<>();

                for (int i = 0; i < modelcthd.getRowCount(); i++) {
                    CTHD cthd = new CTHD();
                    cthd.setMaHoaDon(maHD);
                    cthd.setMaTour(String.valueOf(modelcthd.getValueAt(i, 0)));
                    cthd.setTenTour(String.valueOf(modelcthd.getValueAt(i, 1)));
                    cthd.setGiaTour(Double.parseDouble(String.valueOf(modelcthd.getValueAt(i, 3))));
                    cthd.setSoLuongVe(Integer.parseInt(String.valueOf(modelcthd.getValueAt(i, 2))));
                    cthd.setThanhTien(Double.parseDouble(String.valueOf(modelcthd.getValueAt(i, 4))));
                    cthd.setTrangThai("DA_DAT");
                    dscthd.add(cthd);
                    JOptionPane.showMessageDialog(this, cthdbus.insertCTHD(cthd));
                }
                loadDataHoaDon();
                loadDataTour();
                HoaDon hdfull = hdbus.getHoaDonByMaHD(maHD);
                hdbus.sendEmail(hdfull, dscthd);
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnTaoHoaDonActionPerformed

    private void btnTimKiemTourActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimKiemTourActionPerformed
        dstourSorter = new TableRowSorter(modeltour);
        tableDSTour.setRowSorter(dstourSorter);

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String matour = txtMaTourTK.getText().trim();
        if (!matour.isEmpty())
            filters.add(RowFilter.regexFilter("(?i)" + matour, 0));

        String tentour = txtTenTourTK.getText().trim();
        if (!tentour.isEmpty())
            filters.add(RowFilter.regexFilter("(?i)" + tentour, 1));

        if (filters.isEmpty())
            dstourSorter.setRowFilter(null);
        else
            dstourSorter.setRowFilter(RowFilter.andFilter(filters));
    }// GEN-LAST:event_btnTimKiemTourActionPerformed

    private void btnLamMoiTKTourActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLamMoiTKTourActionPerformed
        txtMaTourTK.setText("");
        txtTenTourTK.setText("");
        loadDataTour();
    }// GEN-LAST:event_btnLamMoiTKTourActionPerformed

    private void btnTiepTucTaoHDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTiepTucTaoHDActionPerformed
        modelcthd.setRowCount(0);
        txtMaHD.setText("");
        txtMaKH.setText("");
        txtTongTien.setText("");
        txtThue.setText("");
        txtKhuyenMai.setText("");
        txtTongThanhToan.setText("");
    }// GEN-LAST:event_btnTiepTucTaoHDActionPerformed

    private void tableDSHDMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tableDSHDMouseClicked
        if (evt.getClickCount() == 2) {
            String mahdtt = String.valueOf(tableDSHD.getValueAt(tableDSHD.getSelectedRow(), 0));
            loadTableCTHD2(mahdtt);
            txtMaHDTT.setText(mahdtt);
            txtMaKHTT.setText(String.valueOf(tableDSHD.getValueAt(tableDSHD.getSelectedRow(), 2)));
            txtSDTTT.setText(String.valueOf(tableDSHD.getValueAt(tableDSHD.getSelectedRow(), 3)));
            double tongtien = Double.parseDouble(String.valueOf(tableDSHD.getValueAt(tableDSHD.getSelectedRow(), 5)));
            txtTongThanhToan2.setText(String.valueOf(df.format(tongtien)));

            String mahd = String.valueOf(tableDSHD.getValueAt(tableDSHD.getSelectedRow(), 0));
            loadTableCTHD2(mahd);
            txtMaHDHuy.setText(mahd);

        }
    }// GEN-LAST:event_tableDSHDMouseClicked

    private void tableCTHD2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tableCTHD2MouseClicked
        if (evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt)) {
            int row = tableCTHD2.rowAtPoint(evt.getPoint());

            if (row >= 0 && row < tableCTHD2.getRowCount()) {
                tableCTHD2.setRowSelectionInterval(row, row);
                popupMenuHoanTien.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }// GEN-LAST:event_tableCTHD2MouseClicked

    private void bthTimKiemHDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bthTimKiemHDActionPerformed
        dshdSorter = new TableRowSorter(modelhd);
        tableDSHD.setRowSorter(dshdSorter);

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String maHD = txtMaHDTK.getText().trim();
        if (!maHD.isEmpty())
            filters.add(RowFilter.regexFilter("(?i)" + maHD, 0));

        String maKH = txtMaKHTK.getText().trim();
        if (!maKH.isEmpty())
            filters.add(RowFilter.regexFilter("(?i)" + maKH, 2));

        String sdt = txtSDTTK.getText().trim();
        if (!sdt.isEmpty())
            filters.add(RowFilter.regexFilter(sdt, 3));

        String selected = String.valueOf(cmboxTTTT.getSelectedItem()).trim().toLowerCase();
        if (cmboxTTTT.getSelectedIndex() >= 0 && !selected.isEmpty()) {
            String filterValue;

            if (selected.equals("đã thanh toán")) {
                filterValue = "true";
            } else if (selected.equals("chưa thanh toán")) {
                filterValue = "false";
            } else {
                filterValue = null;
            }

            if (filterValue != null) {
                filters.add(RowFilter.regexFilter("(?i)^" + filterValue + "$", 8));
            }
        }

        String ngayLap = txtNgayLap2.getText().trim();
        if (!ngayLap.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(ngayLap) + "$", 4));
        }

        if (filters.isEmpty())
            dshdSorter.setRowFilter(null);
        else
            dshdSorter.setRowFilter(RowFilter.andFilter(filters));
    }// GEN-LAST:event_bthTimKiemHDActionPerformed

    private void btnLamMoiTKHDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLamMoiTKHDActionPerformed
        txtMaHDTK.setText("");
        txtMaKHTK.setText("");
        txtSDTTK.setText("");
        cmboxTTTT.setSelectedIndex(-1);
        txtNgayLap2.setText("");
        loadDataHoaDon();
    }// GEN-LAST:event_btnLamMoiTKHDActionPerformed

    private void btnCNXemHDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCNXemHDActionPerformed
        menuItemHoanTien.setVisible(false);
        CardLayout c1 = (CardLayout) panelChucNang.getLayout();
        c1.show(panelChucNang, "card1");
        capNhatThongBaoHoanTien();
        try {
            cmboxTTTT.setSelectedIndex(-1);
            cmboxTTTT.setEnabled(true);
            tableDSHD.setRowSorter(null);
            modelhd.setRowCount(0);
            loadDataHoaDon();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnCNXemHDActionPerformed

    private void btnCNThanhToanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCNThanhToanActionPerformed
        menuItemHoanTien.setVisible(false);
        CardLayout c1 = (CardLayout) panelChucNang.getLayout();
        c1.show(panelChucNang, "card2");

        try {
            cmboxTTTT.setEnabled(false);
            txtMaHDTT.setText("");
            txtMaKHTT.setText("");
            txtSDTTT.setText("");
            txtTongThanhToan2.setText("");
            btnGroupHTTT.clearSelection();
            tableDSHD.setRowSorter(null);
            modelhd.setRowCount(0);
            modelcthd2.setRowCount(0);

            ArrayList<HoaDon> dshd = hdbus.getHoaDonThanhToan();
            loadTableDSHD(dshd);
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnCNThanhToanActionPerformed

    private void btnCNHuyVeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCNHuyVeActionPerformed
        menuItemHoanTien.setVisible(false);
        CardLayout c1 = (CardLayout) panelChucNang.getLayout();
        c1.show(panelChucNang, "card3");

        try {
            modelhd.setRowCount(0);
            modelcthd2.setRowCount(0);
            cmboxTTTT.setEnabled(true);
            txtMaHDHuy.setText("");
            txtSoTourHuy.setText("");
            txtSoTienHoan.setText("");
            txtLyDoHuy.setText("");
            tableDSHD.setRowSorter(null);

            ArrayList<HoaDon> dshd = hdbus.getHoaDonCoTheHuy();
            loadTableDSHD(dshd);
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnCNHuyVeActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThanhToanActionPerformed
        try {
            String mahd = txtMaHDTT.getText().trim();
            String httt = "";
            if (radiobtnTienMat.isSelected()) {
                httt = "Tiền mặt";
            } else if (radiobtnChuyenKhoan.isSelected()) {
                httt = "Chuyển khoản";
            }
            if (mahd.equals("") || txtMaKHTT.getText().equals("") || txtSDTTT.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhấn chọn hóa đơn từ bảng để thực hiện thanh toán!");
                return;
            }
            if (btnGroupHTTT.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hình thức thanh toán!");
                return;
            }

            int kq = JOptionPane.showConfirmDialog(this,
                    "Xác nhận thanh toán hóa đơn có mã: " + mahd,
                    "XÁC NHẬN THANH TOÁN",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (kq == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, hdbus.thanhToanHoaDon(mahd, httt));

                ArrayList<HoaDon> dshd = hdbus.getHoaDonByTrangThaiTT(false);
                modelhd.setRowCount(0);
                loadTableDSHD(dshd);
                ArrayList<CTHD> dscthd = cthdbus.getDSCTHDTheoMaHD(mahd);
                HoaDon hd = hdbus.getHoaDonByMaHD(mahd);
                hdbus.sendEmail(hd, dscthd);

                txtMaHDTT.setText("");
                txtMaKHTT.setText("");
                txtSDTTT.setText("");
                txtTongThanhToan2.setText("");
                btnGroupHTTT.clearSelection();
                modelcthd2.setRowCount(0);
            }
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnThanhToanActionPerformed

    private void btnHuyVeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyVeActionPerformed
        try {
            String mahd = txtMaHDHuy.getText().trim();
            String trangthai = "HUY_THEO_YC";
            String lydohuy = txtLyDoHuy.getText().trim();
            String tienhoan = txtSoTienHoan.getText();
            if (txtMaHDHuy.getText().equals("") || txtSoTourHuy.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết hóa đơn cần hủy từ bảng!");
                return;
            }
            if (lydohuy.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do hủy!");
                return;
            }

            int rows[] = tableCTHD2.getSelectedRows();
            StringBuilder confirmMsg = new StringBuilder();
            confirmMsg.append("Xác nhận hủy các tour sau thuộc hóa đơn: ").append(mahd).append("\n");
            confirmMsg.append("─────────────────────────────────\n");

            ArrayList<String> dsMaTour = new ArrayList<>();
            for (int row : rows) {
                String maTour = String.valueOf(modelcthd2.getValueAt(row, 1));
                String tenTour = String.valueOf(modelcthd2.getValueAt(row, 2));
                String soLuong = String.valueOf(modelcthd2.getValueAt(row, 3));
                confirmMsg.append("• Mã tour: ").append(maTour)
                        .append(" | ").append(tenTour)
                        .append(" | SL: ").append(soLuong).append("\n");
                dsMaTour.add(maTour);
            }

            confirmMsg.append("─────────────────────────────────\n");
            confirmMsg.append("Số tiền hoàn: ").append(tienhoan).append(" đ\n");
            confirmMsg.append("Lý do hủy: ").append(lydohuy).append("\n\n");
            confirmMsg.append("Bạn có chắc chắn muốn hủy không?");

            int kq = JOptionPane.showConfirmDialog(
                    this,
                    confirmMsg.toString(),
                    "XÁC NHẬN HỦY VÉ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (kq != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Đã hủy thao tác.");
                return;
            }

            ArrayList<CTHD> dscthdhuy = new ArrayList<>();
            for (String maTour : dsMaTour) {
                dscthdhuy.add(cthdbus.getCTHDTheoMaHDMaTour(mahd, maTour));
            }

            for (String maTour : dsMaTour) {
                cthdbus.huyDatVe(mahd, maTour, trangthai, lydohuy);
            }
            HoaDon hd = hdbus.getHoaDonByMaHD(mahd);
            if (!dscthdhuy.isEmpty()) {
                hdbus.sendEmailHoanTien(hd, dscthdhuy, tienhoan, lydohuy);

                if (!hd.isTrangThaiTT())
                    hdbus.updateTongTienHDSauHuy(mahd);
            }

            txtMaHDHuy.setText("");
            txtSoTourHuy.setText("");
            txtSoTienHoan.setText("");
            txtLyDoHuy.setText("");
            modelhd.setRowCount(0);
            modelcthd2.setRowCount(0);
            ArrayList<HoaDon> dshd = hdbus.getHoaDonCoTheHuy();
            loadTableDSHD(dshd);
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnHuyVeActionPerformed

    private void menuItemHoanTienActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuItemHoanTienActionPerformed
        try {
            int row = tableCTHD2.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tour trong bảng chi tiết hóa đơn để hoàn tiền!");
                return;
            }

            String mahd = String.valueOf(modelcthd2.getValueAt(row, 0));
            String matour = String.valueOf(modelcthd2.getValueAt(row, 1));
            CTHD cthd = cthdbus.getCTHDTheoMaHDMaTour(mahd, matour);
            if (cthd == null || !"HUY_DO_CONG_TY".equals(cthd.getTrangThai())) {
                JOptionPane.showMessageDialog(this, "Tour này không đủ điều kiện hoàn tiền!");
                return;
            }

            String lydohuy = "Tour đã bị hủy bởi công ty";

            int cf = JOptionPane.showConfirmDialog(this,
                    "Xác nhận hoàn tiền cho MÃ TOUR: " + matour + " thuộc MÃ HÓA ĐƠN: " + mahd,
                    "XÁC NHẬN HOÀN TIỀN", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (cf == JOptionPane.YES_OPTION) {
                boolean kq = cthdbus.hoanTien(mahd, matour);
                if (kq) {
                    JOptionPane.showMessageDialog(this, "Hoàn tiền thành công!");

                    double thanhTienChuaThue = cthd.getGiaTour() * cthd.getSoLuongVe();
                    double thuePhanHoan = thanhTienChuaThue * thue;
                    double tienHoan = thanhTienChuaThue + thuePhanHoan;

                    modelcthd2.setRowCount(0);
                    ArrayList<HoaDon> dshd = hdbus.getHoaDonCanHoanTienDoTourBiHuy();
                    loadTableDSHD(dshd);
                    capNhatThongBaoHoanTien();

                    ArrayList<CTHD> dscthdHoan = new ArrayList<>();
                    dscthdHoan.add(cthd);
                    HoaDon hd = hdbus.getHoaDonByMaHD(mahd);
                    hdbus.sendEmailHoanTien(hd, dscthdHoan, df.format(tienHoan), lydohuy);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể hoàn tiền cho trường hợp này!");
                }
            }
        } catch (BusException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }// GEN-LAST:event_menuItemHoanTienActionPerformed

    private void btnXemDSCanHoanTienActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXemDSCanHoanTienActionPerformed
        menuItemHoanTien.setVisible(true);
        try {
            modelcthd2.setRowCount(0);
            modelhd.setRowCount(0);
            ArrayList<HoaDon> dshd = hdbus.getHoaDonCanHoanTienDoTourBiHuy();
            loadTableDSHD(dshd);
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_btnXemDSCanHoanTienActionPerformed

    private void cmboxKMActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmboxKMActionPerformed
        try {
            double tongtien = Double.parseDouble(String.valueOf(txtTongTien.getText()).replace(",", ""));
            double tienthue = Double.parseDouble(String.valueOf(txtThue.getText()).replace(",", ""));
            double khuyenmai = 0;
            double tongtt = 0;

            if (cmboxKM.getSelectedIndex() >= 0) {
                String cmboxkm = String.valueOf(cmboxKM.getSelectedItem());
                String[] partscmboxmakm = cmboxkm.split("-");
                String makm = String.valueOf(partscmboxmakm[0].trim());
                double giatrikm = kmbus.getGiaTriKMByMaKM(makm);
                if (giatrikm <= 1)
                    khuyenmai = tongtien * giatrikm;
                else
                    khuyenmai = giatrikm;
            } else
                khuyenmai = 0;

            tienthue = tongtien * thue;
            tongtt = tongtien + tienthue - khuyenmai;

            txtKhuyenMai.setText(String.valueOf(df.format(khuyenmai)));
            txtTongThanhToan.setText(String.valueOf(df.format(tongtt)));
        } catch (BusException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }// GEN-LAST:event_cmboxKMActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelTrong2;
    private javax.swing.JPanel PanelTrong3;
    private GUI.Menu.ActionButton bthTimKiemHD;
    private GUI.Menu.ActionButton btnCNHuyVe;
    private GUI.Menu.ActionButton btnCNThanhToan;
    private GUI.Menu.ActionButton btnCNXemHD;
    private GUI.Menu.ActionButton btnChonMaKH;
    private GUI.Menu.ActionButton btnChonTour;
    private javax.swing.ButtonGroup btnGroupHTTT;
    private GUI.Menu.ActionButton btnHuyVe;
    private GUI.Menu.ActionButton btnLamMoiTKHD;
    private GUI.Menu.ActionButton btnLamMoiTKTour;
    private GUI.Menu.ActionButton btnTaoHoaDon;
    private GUI.Menu.ActionButton btnThanhToan;
    private GUI.Menu.ActionButton btnTiepTucTaoHD;
    private GUI.Menu.ActionButton btnTimKiemTour;
    private GUI.Menu.ActionButton btnXemDSCanHoanTien;
    private javax.swing.JComboBox<String> cmboxKM;
    private javax.swing.JComboBox<String> cmboxTTTT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPaneCTHD;
    private javax.swing.JScrollPane jScrollPaneCTHD2;
    private javax.swing.JScrollPane jScrollPaneDSHD;
    private javax.swing.JScrollPane jScrollPaneDanhSachTour;
    private javax.swing.JLabel lblCTHD2;
    private javax.swing.JLabel lblDSHD;
    private javax.swing.JLabel lblDanhSachTour;
    private javax.swing.JLabel lblDanhSachTour1;
    private javax.swing.JLabel lblHTTT;
    private javax.swing.JLabel lblKhuyenMai;
    private javax.swing.JLabel lblLapHoaDon;
    private javax.swing.JLabel lblLyDo;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaHD2;
    private javax.swing.JLabel lblMaHDHuy;
    private javax.swing.JLabel lblMaHoaDonTT;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaKH2;
    private javax.swing.JLabel lblMaKHTT;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblNgayLap;
    private javax.swing.JLabel lblNgayLap2;
    private javax.swing.JLabel lblSDT2;
    private javax.swing.JLabel lblSDTTT;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblSoTienHoan;
    private javax.swing.JLabel lblSoTourHuy;
    private javax.swing.JLabel lblTTTT;
    private javax.swing.JLabel lblTamTinh;
    private javax.swing.JLabel lblThaoTacHuyVe;
    private javax.swing.JLabel lblThaoTacThanhToan;
    private javax.swing.JLabel lblThongBaoHoanTien;
    private javax.swing.JLabel lblThue;
    private javax.swing.JLabel lblTimKiemHD;
    private javax.swing.JLabel lblTongTT;
    private javax.swing.JLabel lblTongThanhToan;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JMenuItem menuItemHoanTien;
    private javax.swing.JPanel panelChucNang;
    private javax.swing.JPanel panelDSHD;
    private javax.swing.JPanel panelHTTT;
    private javax.swing.JPanel panelLyDoHuy;
    private javax.swing.JPanel panelRight;
    private javax.swing.JPanel panelSL;
    private javax.swing.JPanel panelTTHD;
    private javax.swing.JPanel panelTamTinh;
    private javax.swing.JPanel panelThaoTacHuyVe;
    private javax.swing.JPanel panelThaoTacThanhToan;
    private javax.swing.JPanel panelThongTinHuyVe;
    private javax.swing.JPanel panelTienHuyVe;
    private javax.swing.JPanel panelTimKiemHD;
    private javax.swing.JPanel panelTrong;
    private javax.swing.JPanel panelTrong1;
    private javax.swing.JPanel panelTrong10;
    private javax.swing.JPanel panelTrong11;
    private javax.swing.JPanel panelTrong12;
    private javax.swing.JPanel panelTrong13;
    private javax.swing.JPanel panelTrong14;
    private javax.swing.JPanel panelTrong15;
    private javax.swing.JPanel panelTrong16;
    private javax.swing.JPanel panelTrong17;
    private javax.swing.JPanel panelTrong18;
    private javax.swing.JPanel panelTrong19;
    private javax.swing.JPanel panelTrong20;
    private javax.swing.JPanel panelTrong23;
    private javax.swing.JPanel panelTrong3;
    private javax.swing.JPanel panelTrong5;
    private javax.swing.JPanel panelTrong6;
    private javax.swing.JPanel panelTrong7;
    private javax.swing.JPanel panelTrong8;
    private javax.swing.JPanel panelTrong9;
    private javax.swing.JPanel panelXacNhanTTThanhToan;
    private javax.swing.JPanel panelXacNhanThanhToan;
    private javax.swing.JPanel panelXemDSHD;
    private javax.swing.JPanel panelbtnChucNang;
    private javax.swing.JPopupMenu popupMenuHoanTien;
    private javax.swing.JRadioButton radiobtnChuyenKhoan;
    private javax.swing.JRadioButton radiobtnTienMat;
    private javax.swing.JPanel tabDatVe;
    private javax.swing.JPanel tabQLHD;
    private GUI.Menu.TabbedPaneCustom tabbedPaneQLDatVe;
    private javax.swing.JTable tableCTHD;
    private javax.swing.JTable tableCTHD2;
    private javax.swing.JTable tableDSHD;
    private javax.swing.JTable tableDSTour;
    private javax.swing.JTextField txtKhuyenMai;
    private GUI.Menu.TextFieldCustom txtLyDoHuy;
    private GUI.Menu.TextFieldCustom txtMaHD;
    private javax.swing.JTextField txtMaHDHuy;
    private GUI.Menu.TextFieldCustom txtMaHDTK;
    private javax.swing.JTextField txtMaHDTT;
    private GUI.Menu.TextFieldCustom txtMaKH;
    private GUI.Menu.TextFieldCustom txtMaKHTK;
    private javax.swing.JTextField txtMaKHTT;
    private GUI.Menu.TextFieldCustom txtMaNV;
    private GUI.Menu.TextFieldCustom txtMaTourTK;
    private GUI.Menu.TextFieldCustom txtNgayLap;
    private GUI.Menu.TextFieldCustom txtNgayLap2;
    private GUI.Menu.TextFieldCustom txtSDTTK;
    private javax.swing.JTextField txtSDTTT;
    private GUI.Menu.TextFieldCustom txtSoLuong;
    private javax.swing.JTextField txtSoTienHoan;
    private javax.swing.JTextField txtSoTourHuy;
    private GUI.Menu.TextFieldCustom txtTenTourTK;
    private javax.swing.JTextField txtThue;
    private javax.swing.JTextField txtTongThanhToan;
    private javax.swing.JTextField txtTongThanhToan2;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
