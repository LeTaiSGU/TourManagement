package GUI.Dialog;

import GUI.LoginForm.CustomTextField;
import GUI.Menu.ActionButton;
import GUI.Menu.CustomComboBox;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import BUS.LoaiTourBUS;
import DTO.LoaiTour;
import Exception.BusException;
import net.miginfocom.swing.MigLayout;

public class TourSearchDialog extends javax.swing.JPanel {

    public static class SearchCriteria {
        private String maTour;
        private String tenTour;
        private String maLoaiTour;
        private LocalDate tgKhoiHanh;
        private String noiKhoiHanh;
        private Double giaMin;
        private Double giaMax;
        private Boolean daKhoiHanh;

        public String getMaTour() {
            return maTour;
        }

        public void setMaTour(String maTour) {
            this.maTour = maTour;
        }

        public String getTenTour() {
            return tenTour;
        }

        public void setTenTour(String tenTour) {
            this.tenTour = tenTour;
        }

        public String getMaLoaiTour() {
            return maLoaiTour;
        }

        public void setMaLoaiTour(String maLoaiTour) {
            this.maLoaiTour = maLoaiTour;
        }

        public LocalDate getTgKhoiHanh() {
            return tgKhoiHanh;
        }

        public void setTgKhoiHanh(LocalDate tgKhoiHanh) {
            this.tgKhoiHanh = tgKhoiHanh;
        }

        public String getNoiKhoiHanh() {
            return noiKhoiHanh;
        }

        public void setNoiKhoiHanh(String noiKhoiHanh) {
            this.noiKhoiHanh = noiKhoiHanh;
        }

        public Double getGiaMin() {
            return giaMin;
        }

        public void setGiaMin(Double giaMin) {
            this.giaMin = giaMin;
        }

        public Double getGiaMax() {
            return giaMax;
        }

        public void setGiaMax(Double giaMax) {
            this.giaMax = giaMax;
        }

        public Boolean getDaKhoiHanh() {
            return daKhoiHanh;
        }

        public void setDaKhoiHanh(Boolean daKhoiHanh) {
            this.daKhoiHanh = daKhoiHanh;
        }

        public boolean isEmpty() {
            return maTour == null
                    && tenTour == null
                    && maLoaiTour == null
                    && tgKhoiHanh == null
                    && noiKhoiHanh == null
                    && giaMin == null
                    && giaMax == null
                    && daKhoiHanh == null;
        }
    }

    private LoaiTourBUS loaiTourBUS = new LoaiTourBUS();
    private List<LoaiTour> listLoaiTour = new ArrayList<>();
    private Consumer<SearchCriteria> onSearch;

    private CustomTextField txtMaTour;
    private CustomTextField txtTenTour;
    private CustomComboBox<String> cbMaLoaiTour;
    private JSpinner spTgKhoiHanh;
    private CustomTextField txtNoiKhoiHanh;
    private CustomTextField txtGiaMin;
    private CustomTextField txtGiaMax;
    private CustomComboBox<String> cbTrangThaiKhoiHanh;
    private ActionButton btnTimKiem;
    private ActionButton btnLamMoi;

    public TourSearchDialog() {
        initComponents();
        initGUI();
        try {
            listLoaiTour = loaiTourBUS.getAllLoaiTour();
        } catch (BusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        loadComboboxMaLoaiTour();
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Segoe UI", 0, 16));
    }

    private void initGUI() {
        setLayout(new MigLayout(
                "insets 20 24 20 24, gap 12 14",
                "[150px, right][grow, fill, sg f]28[150px, right][grow, fill, sg f]",
                "[]12[]12[]12[]18[]"));

        Font lblFont = new Font("Segoe UI", Font.BOLD, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);
        javax.swing.border.Border spinnerBorder = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 4));

        txtMaTour = new CustomTextField();
        txtMaTour.setFont(inputFont);
        txtMaTour.setPlaceholder("Nhập mã tour...");

        txtTenTour = new CustomTextField();
        txtTenTour.setFont(inputFont);
        txtTenTour.setPlaceholder("Nhập tên tour...");

        cbMaLoaiTour = new CustomComboBox<>();
        cbMaLoaiTour.setFont(inputFont);
        cbMaLoaiTour.addItem("...");

        spTgKhoiHanh = new JSpinner(new SpinnerDateModel());
        spTgKhoiHanh.setEditor(new JSpinner.DateEditor(spTgKhoiHanh, "dd/MM/yyyy"));
        spTgKhoiHanh.setFont(inputFont);
        spTgKhoiHanh.setBorder(spinnerBorder);
        ((JSpinner.DateEditor) spTgKhoiHanh.getEditor()).getTextField().setText("");

        txtNoiKhoiHanh = new CustomTextField();
        txtNoiKhoiHanh.setFont(inputFont);
        txtNoiKhoiHanh.setPlaceholder("Nhập nơi khởi hành...");

        txtGiaMin = new CustomTextField();
        txtGiaMin.setFont(inputFont);
        txtGiaMin.setPlaceholder("Giá từ...");

        txtGiaMax = new CustomTextField();
        txtGiaMax.setFont(inputFont);
        txtGiaMax.setPlaceholder("Đến...");

        cbTrangThaiKhoiHanh = new CustomComboBox<>();
        cbTrangThaiKhoiHanh.setFont(inputFont);
        cbTrangThaiKhoiHanh.addItem("...");
        cbTrangThaiKhoiHanh.addItem("Đã khởi hành");
        cbTrangThaiKhoiHanh.addItem("Chưa khởi hành");

        btnTimKiem = new ActionButton();
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/search2.png")));
        btnTimKiem.setPreferredSize(new java.awt.Dimension(190, 50));
        btnTimKiem.setColorTop(new Color(127, 140, 141));
        btnTimKiem.setColorBottom(new Color(99, 110, 114));
        btnTimKiem.addActionListener(e -> btnTimKiemActionPerformed());

        btnLamMoi = new ActionButton();
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload2.png")));
        btnLamMoi.setPreferredSize(new java.awt.Dimension(190, 50));
        btnLamMoi.setColorTop(new Color(52, 152, 219));
        btnLamMoi.setColorBottom(new Color(41, 128, 185));
        btnLamMoi.addActionListener(e -> btnLamMoiActionPerformed());

        add(makeLabel("Mã tour", lblFont));
        add(txtMaTour);
        add(makeLabel("Tên tour", lblFont));
        add(txtTenTour, "wrap");

        add(makeLabel("Mã loại tour", lblFont));
        add(cbMaLoaiTour);
        add(makeLabel("TG khởi hành", lblFont));
        add(spTgKhoiHanh, "growx, wrap");

        add(makeLabel("Nơi khởi hành", lblFont));
        add(txtNoiKhoiHanh);
        add(makeLabel("Trạng thái khởi hành", lblFont));
        add(cbTrangThaiKhoiHanh, "wrap");

        add(makeLabel("Giá tour", lblFont));
        add(txtGiaMin, "split 2, growx, wmin 150");
        add(txtGiaMax, "growx, wmin 150");
        add(new JLabel());
        add(new JLabel(), "wrap");

        add(btnTimKiem, "span 2, right");
        add(btnLamMoi, "span 2, left");

    }

    private JLabel makeLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    public void loadComboboxMaLoaiTour() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("...");
        for (LoaiTour lt : listLoaiTour) {
            model.addElement(lt.getMaLoaiTour());
        }
        cbMaLoaiTour.setModel(model);
    }

    public void setOnSearch(Consumer<SearchCriteria> onSearch) {
        this.onSearch = onSearch;
    }

    private void btnTimKiemActionPerformed() {
        try {
            SearchCriteria criteria = collectCriteria();
            if (criteria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất một điều kiện tìm kiếm.");
                return;
            }
            if (onSearch != null) {
                onSearch.accept(criteria);
            }
            closeDialog();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void btnLamMoiActionPerformed() {
        txtMaTour.setText("");
        txtTenTour.setText("");
        txtNoiKhoiHanh.setText("");
        txtGiaMin.setText("");
        txtGiaMax.setText("");
        if (cbMaLoaiTour.getItemCount() > 0) {
            cbMaLoaiTour.setSelectedIndex(0);
        }
        if (cbTrangThaiKhoiHanh.getItemCount() > 0) {
            cbTrangThaiKhoiHanh.setSelectedIndex(0);
        }
        JSpinner.DateEditor editor = (JSpinner.DateEditor) spTgKhoiHanh.getEditor();
        editor.getTextField().setText("");
    }

    private SearchCriteria collectCriteria() {
        SearchCriteria criteria = new SearchCriteria();

        String maTour = normalizeInput(txtMaTour.getText());
        String tenTour = normalizeInput(txtTenTour.getText());
        String noiKhoiHanh = normalizeInput(txtNoiKhoiHanh.getText());
        String maLoaiTour = normalizeInput(Objects.toString(cbMaLoaiTour.getSelectedItem(), ""));

        if (maTour != null) {
            criteria.setMaTour(maTour);
        }
        if (tenTour != null) {
            criteria.setTenTour(tenTour);
        }
        if (noiKhoiHanh != null) {
            criteria.setNoiKhoiHanh(noiKhoiHanh);
        }
        if (maLoaiTour != null && !"...".equals(maLoaiTour)) {
            criteria.setMaLoaiTour(maLoaiTour);
        }

        JSpinner.DateEditor editor = (JSpinner.DateEditor) spTgKhoiHanh.getEditor();
        String dateText = normalizeInput(editor.getTextField().getText());
        if (dateText != null) {
            java.util.Date date = (java.util.Date) spTgKhoiHanh.getValue();
            criteria.setTgKhoiHanh(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        Double giaMin = parsePrice(txtGiaMin.getText(), "Giá min");
        Double giaMax = parsePrice(txtGiaMax.getText(), "Giá max");
        if (giaMin != null && giaMax != null && giaMin > giaMax) {
            throw new IllegalArgumentException("Giá min không được lớn hơn giá max.");
        }
        criteria.setGiaMin(giaMin);
        criteria.setGiaMax(giaMax);

        String trangThai = normalizeInput(Objects.toString(cbTrangThaiKhoiHanh.getSelectedItem(), ""));
        if ("Đã khởi hành".equals(trangThai)) {
            criteria.setDaKhoiHanh(Boolean.TRUE);
        } else if ("Chưa khởi hành".equals(trangThai)) {
            criteria.setDaKhoiHanh(Boolean.FALSE);
        }

        return criteria;
    }

    private String normalizeInput(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Double parsePrice(String rawValue, String fieldLabel) {
        String value = normalizeInput(rawValue);
        if (value == null) {
            return null;
        }
        try {
            double parsed = Double.parseDouble(value.replace(",", ""));
            if (parsed < 0) {
                throw new IllegalArgumentException(fieldLabel + " phải >= 0.");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldLabel + " không đúng định dạng số.");
        }
    }

    private void closeDialog() {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
