/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuDrop extends javax.swing.JPanel {

    private String userName = "Admin Atlas";
    private String userEmail = "admin@atlasagency.com";

    public MenuDrop() {
        initComponents();
        setupMenuDrop();
    }

    private void setupMenuDrop() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(240, 280));

        // Profile Section with blue background (Avatar + Name + Email)
        JPanel profileSection = createProfileSection();
        add(profileSection);

        // Menu Items Section
        JPanel menuSection = new JPanel();
        menuSection.setLayout(new BoxLayout(menuSection, BoxLayout.Y_AXIS));
        menuSection.setBackground(Color.WHITE);
        menuSection.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menuSection.add(createMenuItem("Profile Settings", "profile"));
        menuSection.add(Box.createRigidArea(new Dimension(0, 5)));
        menuSection.add(createMenuItem("Security", "security"));
        menuSection.add(Box.createRigidArea(new Dimension(0, 5)));
        menuSection.add(createLogoutButton());

        add(menuSection);
    }

    private JPanel createProfileSection() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient từ #2980B9 đến #6DD5FA giống Menu
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.decode("#2980B9"), 
                    0, getHeight(), Color.decode("#6DD5FA")
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(240, 140));

        // Avatar with white circle background
        JLabel avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw white circle background
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 60, 60);

                // Draw user icon
                g2.setColor(new Color(189, 195, 199));
                // Head
                g2.fillOval(18, 12, 24, 24);
                // Body (arc shape)
                g2.fillArc(10, 32, 40, 30, 0, -180);

                g2.dispose();
            }
        };
        avatarLabel.setPreferredSize(new Dimension(60, 60));
        avatarLabel.setMaximumSize(new Dimension(60, 60));
        avatarLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Name
        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(new Color(33, 33, 33));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Email
        JLabel emailLabel = new JLabel(userEmail);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        emailLabel.setForeground(new Color(100, 100, 100));
        emailLabel.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(avatarLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        panel.add(emailLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        return panel;
    }

    private JPanel createMenuItem(String text, String iconType) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(220, 36));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (iconType.equals("profile")) {
                    // User icon
                    g2.setColor(new Color(52, 152, 219));
                    g2.fillOval(3, 2, 10, 10);
                    g2.fillArc(0, 10, 16, 10, 0, -180);
                } else if (iconType.equals("security")) {
                    // Lock icon
                    g2.setColor(new Color(127, 140, 141));
                    g2.fillRoundRect(3, 8, 10, 8, 2, 2);
                    g2.drawArc(5, 4, 6, 6, 0, 180);
                }

                g2.dispose();
            }
        };
        iconLabel.setPreferredSize(new Dimension(20, 20));

        // Text
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textLabel.setForeground(new Color(70, 70, 70));

        panel.add(iconLabel);
        panel.add(textLabel);

        // Hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(248, 248, 248));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMenuClick(text);
            }
        });

        return panel;
    }

    private JPanel createLogoutButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(new Color(255, 245, 245));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 200), 1));
        panel.setMaximumSize(new Dimension(220, 36));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Logout icon
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Arrow icon
                g2.setColor(new Color(231, 76, 60));
                g2.drawLine(4, 8, 10, 8);
                g2.drawLine(8, 5, 10, 8);
                g2.drawLine(8, 11, 10, 8);
                // Door frame
                g2.drawLine(12, 4, 12, 12);
                g2.drawLine(12, 4, 14, 4);
                g2.drawLine(12, 12, 14, 12);

                g2.dispose();
            }
        };
        iconLabel.setPreferredSize(new Dimension(20, 20));

        // Text
        JLabel textLabel = new JLabel("Logout");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textLabel.setForeground(new Color(231, 76, 60));

        panel.add(iconLabel);
        panel.add(textLabel);

        // Hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(255, 235, 235));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(255, 245, 245));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMenuClick("Logout");
            }
        });

        return panel;
    }

    private void handleMenuClick(String menuItem) {
        switch (menuItem) {
            case "Profile Settings":
                // TODO: Open profile settings
                System.out.println("Profile Settings clicked");
                break;
            case "Security":
                // TODO: Open security settings
                System.out.println("Security clicked");
                break;
            case "Logout":
                // TODO: Handle logout
                System.out.println("Logout clicked");
                break;
        }
    }

    public void setUserInfo(String name, String email) {
        this.userName = name;
        this.userEmail = email;
        removeAll();
        setupMenuDrop();
        revalidate();
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
