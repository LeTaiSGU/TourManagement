package GUI.Dialog;

import GUI.LoginForm.CustomTextField;
import GUI.Menu.ActionButton;
import java.awt.Color;
import java.awt.Font;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

public class HuyTourDialog extends javax.swing.JPanel {

    private Consumer<String> onConfirm;
    private Runnable onClose;

    private CustomTextField txtLyDoHuy;
    private ActionButton btnXacNhan;
    private ActionButton btnDong;

    public HuyTourDialog() {
        initComponents();
        initGUI();
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(600, 250));
    }

    private void initGUI() {
        setLayout(new MigLayout(
                "insets 20 28 20 28, gap 10 12, align center center, fillx",
                "[right, 130!][400!, fill]",
                "[]8[]14[]20[]"));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 17);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblTitle = new JLabel("Xác nhận hủy tour");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(new Color(192, 57, 43));
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblQuestion = new JLabel("Bạn có chắc muốn hủy tour này không?");
        lblQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblQuestion.setForeground(new Color(55, 71, 79));

        JLabel lblLyDo = new JLabel("Lý do hủy");
        lblLyDo.setFont(labelFont);
        lblLyDo.setHorizontalAlignment(SwingConstants.RIGHT);

        txtLyDoHuy = new CustomTextField();
        txtLyDoHuy.setFont(inputFont);
        txtLyDoHuy.setPlaceholder("Nhập lý do hủy tour...");

        btnXacNhan = new ActionButton();
        btnXacNhan.setText("Xác nhận hủy");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnXacNhan.setPreferredSize(new java.awt.Dimension(200, 50));
        btnXacNhan.setColorTop(new Color(231, 76, 60));
        btnXacNhan.setColorBottom(new Color(192, 47, 34));
        btnXacNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/close.png")));
        btnXacNhan.addActionListener(e -> btnXacNhanActionPerformed());

        btnDong = new ActionButton();
        btnDong.setText("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnDong.setPreferredSize(new java.awt.Dimension(150, 50));
        btnDong.setColorTop(new Color(127, 140, 141));
        btnDong.setColorBottom(new Color(99, 110, 114));
        btnDong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon/reload2.png")));
        btnDong.addActionListener(e -> btnDongActionPerformed());

        add(lblTitle, "span 2, left, wrap");
        add(lblQuestion, "span 2, left, wrap");
        add(lblLyDo);
        add(txtLyDoHuy, "growx, wrap");
        add(btnXacNhan, "span 2, split 2, center, w 210!, h 50!");
        add(btnDong, "w 150!, h 50!");
    }

    private void btnXacNhanActionPerformed() {
        String lyDoHuy = txtLyDoHuy.getText() != null ? txtLyDoHuy.getText().trim() : "";
        if (lyDoHuy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do hủy tour.");
            return;
        }
        if (onConfirm != null) {
            onConfirm.accept(lyDoHuy);
        }
        closeDialog();
    }

    private void btnDongActionPerformed() {
        if (onClose != null) {
            onClose.run();
        }
        closeDialog();
    }

    public void setOnConfirm(Consumer<String> onConfirm) {
        this.onConfirm = onConfirm;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public String getLyDoHuy() {
        return txtLyDoHuy.getText() != null ? txtLyDoHuy.getText().trim() : "";
    }

    private void closeDialog() {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
