package com.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Kiểu nút giống {@link LoginFrame}: primary đặc teal, secondary viền teal, danger viền đỏ nhạt.
 */
public final class UiButtons {

    private static final String PRIMARY_HOVER = "UiButtons.primaryHover";

    private UiButtons() {
    }

    /** Giống nút "Đăng nhập". */
    public static void stylePrimary(JButton b) {
        b.setFont(b.getFont().deriveFont(Font.BOLD, 15f));
        b.setBackground(UiShellTheme.ACCENT);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(14, 18, 14, 18));
        attachPrimaryHover(b);
    }

    /** Giống nút "Tra cứu nhanh". */
    public static void styleSecondary(JButton b) {
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 14f));
        b.setForeground(UiShellTheme.ACCENT);
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiShellTheme.ACCENT, 1, true),
                new EmptyBorder(13, 18, 13, 18)));
    }

    /** Xóa / đăng xuất — chữ đỏ, viền hồng nhạt (cùng padding với secondary để đồng chiều cao). */
    public static void styleDanger(JButton b) {
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 14f));
        b.setForeground(new Color(185, 28, 28));
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(254, 202, 202), 1, true),
                new EmptyBorder(13, 18, 13, 18)));
    }

    public static void styleDangerOutline(JButton b) {
        styleDanger(b);
    }

    private static void attachPrimaryHover(JButton b) {
        if (Boolean.TRUE.equals(b.getClientProperty(PRIMARY_HOVER))) {
            return;
        }
        b.putClientProperty(PRIMARY_HOVER, Boolean.TRUE);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(UiShellTheme.ACCENT_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(UiShellTheme.ACCENT);
            }
        });
    }

    /**
     * Đồng bộ chiều cao, giữ chiều rộng theo nội dung — tránh cắt chữ do ép cùng width.
     */
    public static void equalizeHeightsOnly(JButton... buttons) {
        if (buttons.length == 0) {
            return;
        }
        int maxH = 0;
        for (JButton b : buttons) {
            if (b == null) {
                continue;
            }
            Dimension d = preferredFromUi(b);
            maxH = Math.max(maxH, d.height);
        }
        final int padW = 6;
        for (JButton b : buttons) {
            if (b == null) {
                continue;
            }
            Dimension d = preferredFromUi(b);
            int w = d.width + padW;
            b.setPreferredSize(new Dimension(w, maxH));
            b.setMinimumSize(new Dimension(w, maxH));
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxH));
        }
    }

    private static Dimension preferredFromUi(JButton b) {
        ButtonUI ui = b.getUI();
        if (ui != null) {
            Dimension d = ui.getPreferredSize(b);
            if (d != null && d.width > 0 && d.height > 0) {
                return new Dimension(d);
            }
        }
        return b.getPreferredSize();
    }

    /** Giữ tương thích: chỉ đồng chiều cao (khuyến nghị cho thanh công cụ). */
    public static void equalizeButtonsInContainer(Container c) {
        List<JButton> list = new ArrayList<>();
        for (Component comp : c.getComponents()) {
            if (comp instanceof JButton) {
                list.add((JButton) comp);
            }
        }
        equalizeHeightsOnly(list.toArray(new JButton[0]));
    }

    /** Cùng width+height khi thật sự cần (ví dụ cặp Trước/Sau). */
    public static void equalizeButtonSizes(JButton... buttons) {
        if (buttons.length == 0) {
            return;
        }
        int maxW = 0;
        int maxH = 0;
        for (JButton b : buttons) {
            if (b == null) {
                continue;
            }
            Dimension d = b.getPreferredSize();
            maxW = Math.max(maxW, d.width);
            maxH = Math.max(maxH, d.height);
        }
        Dimension u = new Dimension(maxW, maxH);
        for (JButton b : buttons) {
            if (b == null) {
                continue;
            }
            b.setPreferredSize(new Dimension(u));
            b.setMinimumSize(new Dimension(u));
        }
    }
}
