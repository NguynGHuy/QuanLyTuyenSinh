package com.example.ui;

import com.example.ui.UiShellTheme.RoundedCardPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Sidebar: danh mục thu gọn có animation, icon + chữ, header có nút đóng/mở.
 */
public final class SidebarUi {

    static final Color SIDEBAR_CANVAS = new Color(241, 245, 249);
    private static final Color LABEL_MUTED = new Color(100, 116, 139);
    private static final Color BTN_IDLE_TEXT = new Color(51, 65, 85);
    private static final Color BTN_IDLE_BORDER = new Color(226, 232, 240);
    private static final Color BTN_IDLE_BG = Color.WHITE;
    private static final Color BTN_SEL_BG = new Color(236, 253, 245);
    /** Chiều rộng sidebar khi mở / khi thu (chỉ icon). Thu gọn đủ rộng để badge không bị cắt. */
    public static final int SIDEBAR_EXPANDED_W = 280;
    public static final int SIDEBAR_COLLAPSED_W = 92;
    /** Khi rộng &lt; mốc này: ẩn chữ menu, tắt scroll dọc, thu gọn nút. */
    private static final int SIDEBAR_SHOW_LABELS_MIN_W = 112;
    private static final int NAV_BTN_MAX_H = 54;
    private static final int GAP_Y = 6;

    private SidebarUi() {
    }

    /**
     * Khối menu (header + scroll). Gắn vào {@code sideCanvas} ở {@link BorderLayout#CENTER}.
     *
     * @param sidebarShell thẻ bo góc chứa sidebar — dùng để animate chiều rộng
     */
    public static JPanel buildCollapsibleNavBlock(RoundedCardPanel sidebarShell, List<String> labels,
            List<String> iconChars, IntConsumer onSelect) {
        return buildCollapsibleNavBlock(sidebarShell, labels, iconChars, onSelect, null);
    }

    /**
     * @param logoutButton nút đăng xuất (nếu có) — khi thu gọn sidebar chỉ hiển thị icon, tooltip đầy đủ
     */
    public static JPanel buildCollapsibleNavBlock(RoundedCardPanel sidebarShell, List<String> labels,
            List<String> iconChars, IntConsumer onSelect, JButton logoutButton) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);

        JLabel titleLbl = new JLabel("Danh mục", SwingConstants.CENTER);
        titleLbl.setFont(titleLbl.getFont().deriveFont(Font.BOLD, 13.5f));
        titleLbl.setForeground(LABEL_MUTED);

        JButton toggle = new JButton();
        toggle.setIcon(new ChevronRailIcon(true));
        styleHeaderToggle(toggle);

        JPanel header = new JPanel(new BorderLayout(0, 0));
        header.setOpaque(false);
        header.add(titleLbl, BorderLayout.CENTER);
        header.add(toggle, BorderLayout.EAST);

        JPanel navCol = new JPanel();
        navCol.setLayout(new BoxLayout(navCol, BoxLayout.Y_AXIS));
        navCol.setOpaque(true);
        navCol.setBackground(SIDEBAR_CANVAS);
        navCol.setBorder(new EmptyBorder(2, 0, 0, 0));

        List<JButton> buttons = new ArrayList<>();
        List<JComponent> navSpacers = new ArrayList<>();
        final int[] selectedNav = {0};

        for (int i = 0; i < labels.size(); i++) {
            String icon = (iconChars != null && i < iconChars.size()) ? iconChars.get(i) : "•";
            JButton b = new JButton(labels.get(i));
            b.setHorizontalTextPosition(SwingConstants.RIGHT);
            b.setVerticalTextPosition(SwingConstants.CENTER);
            b.setIconTextGap(6);
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.putClientProperty("nav.fullLabel", labels.get(i));
            b.putClientProperty("nav.iconEmoji", icon);
            int idx = i;
            b.addActionListener(e -> {
                selectedNav[0] = idx;
                boolean compact = sidebarShell.getPreferredSize().width < SIDEBAR_SHOW_LABELS_MIN_W;
                for (int j = 0; j < buttons.size(); j++) {
                    styleNavButton(buttons.get(j), j == idx, compact);
                }
                onSelect.accept(idx);
            });
            styleNavButton(b, false, false);
            buttons.add(b);
            navCol.add(b);
            if (i < labels.size() - 1) {
                JComponent strut = (JComponent) Box.createVerticalStrut(GAP_Y);
                navSpacers.add(strut);
                navCol.add(strut);
            }
        }

        if (!buttons.isEmpty()) {
            styleNavButton(buttons.get(0), true, false);
            onSelect.accept(0);
        }

        JScrollPane scroll = wrapNav(navCol);
        syncNavScrollDensity(scroll, navSpacers, buttons, selectedNav[0], true);

        block.add(header, BorderLayout.NORTH);
        block.add(scroll, BorderLayout.CENTER);

        final boolean[] expanded = {true};

        toggle.addActionListener(e -> {
            int from = expanded[0] ? SIDEBAR_EXPANDED_W : SIDEBAR_COLLAPSED_W;
            int to = expanded[0] ? SIDEBAR_COLLAPSED_W : SIDEBAR_EXPANDED_W;
            expanded[0] = !expanded[0];
            animateSidebarWidth(sidebarShell, from, to, labels, buttons, titleLbl, toggle, expanded[0],
                    logoutButton, scroll, navSpacers, selectedNav);
        });

        return block;
    }

    private static void styleHeaderToggle(JButton t) {
        t.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 2));
        t.setContentAreaFilled(false);
        t.setFocusPainted(false);
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        t.setToolTipText("Thu gọn / mở rộng menu");
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (t.getIcon() instanceof ChevronRailIcon) {
                    ((ChevronRailIcon) t.getIcon()).setHover(true);
                    t.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (t.getIcon() instanceof ChevronRailIcon) {
                    ((ChevronRailIcon) t.getIcon()).setHover(false);
                    t.repaint();
                }
            }
        });
    }

    private static void animateSidebarWidth(RoundedCardPanel shell, int from, int to, List<String> labels,
            List<JButton> navButtons, JLabel titleLbl, JButton toggle, boolean nowExpanded,
            JButton logoutButton, JScrollPane navScroll, List<JComponent> navSpacers, int[] selectedNav) {
        final int steps = 20;
        final int frameMs = 14;
        final int[] step = {0};
        Timer timer = new Timer(frameMs, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                step[0]++;
                float t = Math.min(1f, step[0] / (float) steps);
                float eased = 1f - (1f - t) * (1f - t);
                int w = Math.round(from + (to - from) * eased);
                shell.setPreferredSize(new Dimension(w, 0));
                shell.revalidate();
                Container par = shell.getParent();
                if (par != null) {
                    par.revalidate();
                    par.repaint();
                }

                boolean showText = w >= SIDEBAR_SHOW_LABELS_MIN_W;
                titleLbl.setVisible(showText);
                for (int k = 0; k < navButtons.size(); k++) {
                    JButton b = navButtons.get(k);
                    String full = labels.get(k);
                    if (showText) {
                        b.setText(full);
                        b.setToolTipText(null);
                    } else {
                        b.setText("");
                        b.setToolTipText(full);
                    }
                }
                applyLogoutCompact(logoutButton, showText);
                syncNavScrollDensity(navScroll, navSpacers, navButtons, selectedNav[0], showText);

                if (step[0] >= steps) {
                    timer.stop();
                    shell.setPreferredSize(new Dimension(to, 0));
                    toggle.setIcon(new ChevronRailIcon(nowExpanded));
                    boolean wideDone = to >= SIDEBAR_SHOW_LABELS_MIN_W;
                    syncNavScrollDensity(navScroll, navSpacers, navButtons, selectedNav[0], wideDone);
                    shell.revalidate();
                    if (par != null) {
                        par.revalidate();
                        par.repaint();
                    }
                }
            }
        });
        timer.start();
    }

    /**
     * Thu gọn: tắt scroll dọc (tránh thanh chồng lên icon), thu khoảng cách dọc, icon to & căn giữa.
     */
    private static void syncNavScrollDensity(JScrollPane scroll, List<JComponent> spacers,
            List<JButton> navButtons, int selectedIdx, boolean wide) {
        boolean compact = !wide;
        scroll.setVerticalScrollBarPolicy(compact
                ? ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
                : ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        int gap = compact ? 2 : GAP_Y;
        for (JComponent s : spacers) {
            s.setPreferredSize(new Dimension(0, gap));
            s.setMinimumSize(new Dimension(0, gap));
            s.setMaximumSize(new Dimension(Integer.MAX_VALUE, gap));
        }
        for (int j = 0; j < navButtons.size(); j++) {
            styleNavButton(navButtons.get(j), j == selectedIdx, compact);
        }
        scroll.revalidate();
    }

    private static void applyLogoutCompact(JButton logoutButton, boolean showFullLabel) {
        if (logoutButton == null) {
            return;
        }
        Object full = logoutButton.getClientProperty("logout.fullLabel");
        String label = full instanceof String ? (String) full : "Đăng xuất";
        if (showFullLabel) {
            logoutButton.setText(label);
            logoutButton.setToolTipText(null);
        } else {
            logoutButton.setText("");
            logoutButton.setToolTipText(label);
        }
        logoutButton.setHorizontalAlignment(SwingConstants.CENTER);
        logoutButton.setIconTextGap(showFullLabel ? 10 : 0);
    }

    public static JScrollPane wrapNav(JPanel navColumn) {
        JScrollPane sp = new JScrollPane(navColumn,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(SIDEBAR_CANVAS);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        return sp;
    }

    /** Đăng xuất: nền hồng nhạt, viền, icon gradient đỏ–cam, hover mượt. */
    public static JButton createOutlineLogoutButton() {
        JButton btn = new JButton("Đăng xuất");
        btn.putClientProperty("logout.fullLabel", "Đăng xuất");
        btn.setIcon(WarmBadgeIcon.logoutDoor(30));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        btn.setForeground(new Color(185, 28, 28));
        Color bg = new Color(254, 242, 242);
        btn.setBackground(bg);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(10);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(252, 165, 165), 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(254, 226, 226));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    /** Icon nút đăng nhập: badge đỏ–cam nổi, mũi tên trắng. */
    public static Icon loginEntryIcon(int sizePx) {
        return WarmBadgeIcon.loginArrow(sizePx);
    }

    private static void styleNavButton(JButton b, boolean selected, boolean compact) {
        Object iconObj = b.getClientProperty("nav.iconEmoji");
        String icon = iconObj instanceof String ? (String) iconObj : "•";
        float glyphPt = compact ? 23f : 21f;
        int badge = compact ? 40 : 36;
        b.setIcon(new NavBadgeIcon(icon, glyphPt, badge));
        b.setHorizontalAlignment(compact ? SwingConstants.CENTER : SwingConstants.LEFT);
        int vt = compact ? 5 : 10;
        int hl = compact ? 4 : 8;
        int hr = compact ? 4 : 12;
        int maxH = compact ? 50 : NAV_BTN_MAX_H;
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxH));

        if (selected) {
            b.setBackground(BTN_SEL_BG);
            b.setForeground(UiShellTheme.ACCENT.darker());
            b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 3, 0, 0, UiShellTheme.ACCENT),
                            BorderFactory.createLineBorder(new Color(167, 243, 208), 1, true)),
                    new EmptyBorder(vt, hl, vt, hr)));
        } else {
            b.setBackground(BTN_IDLE_BG);
            b.setForeground(BTN_IDLE_TEXT);
            b.setFont(b.getFont().deriveFont(Font.PLAIN, 13f));
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BTN_IDLE_BORDER, 1, true),
                    new EmptyBorder(vt, hl, vt, hr)));
        }
    }

    /**
     * Icon menu: nền trắng bo góc, viền nhạt, glyph tối — tách khỏi nền sidebar.
     */
    static final class NavBadgeIcon implements Icon {
        private final String ch;
        private final float glyphPt;
        private final int badge;

        NavBadgeIcon(String ch, float glyphPt, int badgePx) {
            this.ch = ch != null && !ch.isEmpty() ? ch : "•";
            this.glyphPt = glyphPt;
            this.badge = Math.max(22, badgePx);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getIconWidth();
            int h = getIconHeight();
            int arc = Math.min(10, w / 3);

            g2.setColor(new Color(15, 23, 42, 30));
            g2.fillRoundRect(x + 1, y + 2, w - 2, h - 2, arc, arc);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, w - 2, h - 2, arc, arc);

            g2.setColor(new Color(203, 213, 225));
            g2.drawRoundRect(x, y, w - 2, h - 2, arc, arc);

            g2.setFont(c.getFont().deriveFont(Font.PLAIN, glyphPt));
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(ch);
            int tx = x + (w - 2 - sw) / 2;
            int ty = y + (h - 2 - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(new Color(15, 23, 42));
            g2.drawString(ch, tx, ty);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return badge;
        }

        @Override
        public int getIconHeight() {
            return badge;
        }
    }

    /** Thanh đứng + hai chevron — thu gọn / mở rộng, kích thước lớn dễ nhìn. */
    static final class ChevronRailIcon implements Icon {
        private static final int W = 38;
        private static final int H = 32;
        /** {@code true} = sidebar đang mở (bấm để thu — chevron hướng trái). */
        private final boolean expanded;
        private boolean hover;

        ChevronRailIcon(boolean expanded) {
            this.expanded = expanded;
        }

        void setHover(boolean h) {
            this.hover = h;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Color rail = hover ? new Color(148, 163, 184) : new Color(203, 213, 225);
            g2.setColor(rail);
            int rx = x + 6;
            g2.drawLine(rx, y + 6, rx, y + H - 6);

            Color chev = hover ? UiShellTheme.ACCENT.darker() : new Color(71, 85, 105);
            g2.setColor(chev);
            float cx = x + 22f;
            float cy = y + H / 2f;
            float d = 7f;
            if (expanded) {
                paintChev(g2, cx + d * 0.35f, cy, -1, d);
                paintChev(g2, cx - d * 0.35f, cy, -1, d);
            } else {
                paintChev(g2, cx - d * 0.35f, cy, 1, d);
                paintChev(g2, cx + d * 0.35f, cy, 1, d);
            }
            g2.dispose();
        }

        private static void paintChev(Graphics2D g2, float cx, float cy, int dir, float d) {
            Path2D p = new Path2D.Float();
            p.moveTo(cx - dir * d * 0.45f, cy - d * 0.55f);
            p.lineTo(cx + dir * d * 0.65f, cy);
            p.lineTo(cx - dir * d * 0.45f, cy + d * 0.55f);
            g2.draw(p);
        }

        @Override
        public int getIconWidth() {
            return W;
        }

        @Override
        public int getIconHeight() {
            return H;
        }
    }

    /** Badge tròn gradient cam–đỏ, họa tiết trắng (đăng nhập / đăng xuất). */
    public static final class WarmBadgeIcon implements Icon {
        private final int size;
        private final int mode;

        private static final int MODE_LOGIN = 0;
        private static final int MODE_LOGOUT = 1;

        private WarmBadgeIcon(int size, int mode) {
            this.size = Math.max(24, size);
            this.mode = mode;
        }

        static WarmBadgeIcon loginArrow(int sizePx) {
            return new WarmBadgeIcon(sizePx, MODE_LOGIN);
        }

        static WarmBadgeIcon logoutDoor(int sizePx) {
            return new WarmBadgeIcon(sizePx, MODE_LOGOUT);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getIconWidth();
            int h = getIconHeight();
            int arc = Math.min(w, h);

            Paint gp = new GradientPaint(
                    x, y, new Color(249, 115, 22),
                    x + w, y + h, new Color(220, 38, 38));
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, w - 1, h - 1, arc, arc);

            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, w - 2, h - 2, arc - 2, arc - 2));

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            float mx = x + w / 2f;
            float my = y + h / 2f;
            if (mode == MODE_LOGIN) {
                float s = w * 0.22f;
                Path2D arr = new Path2D.Float();
                arr.moveTo(mx - s * 0.9f, my);
                arr.lineTo(mx + s * 0.5f, my);
                arr.moveTo(mx + s * 0.15f, my - s * 0.65f);
                arr.lineTo(mx + s * 0.85f, my);
                arr.lineTo(mx + s * 0.15f, my + s * 0.65f);
                g2.draw(arr);
            } else {
                float d = w * 0.14f;
                g2.draw(new Rectangle2D.Float(mx - d * 1.1f, my - d * 1.2f, d * 1.4f, d * 2.4f));
                Path2D door = new Path2D.Float();
                door.moveTo(mx + d * 0.35f, my - d * 0.2f);
                door.lineTo(mx + d * 1.45f, my);
                door.lineTo(mx + d * 0.35f, my + d * 0.2f);
                g2.draw(door);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}
