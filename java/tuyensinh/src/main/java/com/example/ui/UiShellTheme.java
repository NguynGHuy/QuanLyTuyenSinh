package com.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Nền gradient + thẻ bo góc dùng chung cho đăng nhập và khung làm việc (hai thẻ tách khe).
 */
public final class UiShellTheme {

    public static final Color ACCENT = new Color(13, 148, 136);
    public static final Color ACCENT_HOVER = new Color(15, 118, 110);
    public static final Color CARD_BORDER = new Color(226, 232, 240);

    private UiShellTheme() {
    }

    /**
     * Nền gradient tuyến tính nghiêng (~12°, cùng hướng đường tham chiếu trong mockup) + vòng tròn mờ;
     * đăng nhập/user màu chuẩn; admin có bản sáng hơn.
     */
    public static class ShellGradientPanel extends JPanel {

        private static final Color SHELL_LEFT = new Color(15, 23, 42);
        private static final Color SHELL_RIGHT = new Color(30, 64, 110);
        /** Cùng tông, sáng hơn một chút (trang admin). */
        private static final Color SHELL_LEFT_BRIGHT = new Color(52, 73, 102);
        private static final Color SHELL_RIGHT_BRIGHT = new Color(72, 115, 169);

        /**
         * Góc trục gradient / sọc (từ trục X sang trục Y, tọa độ màn hình): xuống–phải, ~10–15°.
         */
        private static final double GRADIENT_AND_STRIPE_TILT_DEG = 100.0;

        private final boolean brighten;

        public ShellGradientPanel() {
            this(false);
        }

        /**
         * @param brighten {@code true}: nền sáng hơn (admin); {@code false}: màu chuẩn (đăng nhập / user).
         */
        public ShellGradientPanel(boolean brighten) {
            this.brighten = brighten;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Color left = brighten ? SHELL_LEFT_BRIGHT : SHELL_LEFT;
            Color right = brighten ? SHELL_RIGHT_BRIGHT : SHELL_RIGHT;
            paintTiltedGradient(g2, w, h, left, right);

            g2.setColor(new Color(56, 189, 248, 28));
            g2.fillOval(-w / 5, h / 6, w / 2, w / 2);
            g2.setColor(new Color(45, 212, 191, 22));
            g2.fillOval(w / 3, -h / 8, w / 2, w / 2);
            g2.setColor(new Color(99, 102, 241, 18));
            g2.fillOval(w * 2 / 3, h / 2, w / 3, w / 3);

            paintNearHorizontalStripes(g2, w, h);

            g2.dispose();
        }

        /** Gradient tuyến tính dọc trục nghiêng (cùng {@link #GRADIENT_AND_STRIPE_TILT_DEG}), xuống–phải. */
        private static void paintTiltedGradient(Graphics2D g2, int w, int h, Color start, Color end) {
            if (w <= 0 || h <= 0) {
                return;
            }
            double rad = Math.toRadians(GRADIENT_AND_STRIPE_TILT_DEG);
            float len = (float) (Math.hypot(w, h) * 1.35);
            float cx = w * 0.5f;
            float cy = h * 0.5f;
            float c = (float) Math.cos(rad);
            float s = (float) Math.sin(rad);
            float x1 = cx - c * len * 0.5f;
            float y1 = cy - s * len * 0.5f;
            float x2 = cx + c * len * 0.5f;
            float y2 = cy + s * len * 0.5f;
            g2.setPaint(new GradientPaint(x1, y1, start, x2, y2, end));
            g2.fillRect(0, 0, w, h);
        }

        /** 5 đường song song, cùng góc nghiêng với trục gradient. */
        private static void paintNearHorizontalStripes(Graphics2D g2, int w, int h) {
            if (w <= 0 || h <= 0) {
                return;
            }
            float diag = (float) Math.hypot(w, h);
            AffineTransform old = g2.getTransform();
            g2.translate(w * 0.5, h * 0.5);
            g2.rotate(Math.toRadians(GRADIENT_AND_STRIPE_TILT_DEG));
            float len = diag * 1.35f;
            float step = Math.max(w, h) / 6f;
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            g2.setColor(new Color(241, 245, 249, 40));
            for (int i = -2; i <= 2; i++) {
                float x = i * step;
                g2.draw(new Line2D.Float(x, -len * 0.5f, x, len * 0.5f));
            }
            g2.setTransform(old);
        }
    }

    /**
     * Thẻ đăng nhập: bo góc ~12–14px, bóng rất nhẹ (mockup).
     */
    public static class LoginCardPanel extends JPanel {
        public static final int ARC = 14;
        private static final Color SHADOW = new Color(15, 40, 55);

        public LoginCardPanel(LayoutManager lm) {
            super(lm);
            setOpaque(false);
        }

        private static RoundRectangle2D.Float shape(float x, float y, float w, float h) {
            return new RoundRectangle2D.Float(x, y, Math.max(0, w), Math.max(0, h), ARC, ARC);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float w = getWidth() - 1f;
            float h = getHeight() - 1f;

            g2.setColor(new Color(SHADOW.getRed(), SHADOW.getGreen(), SHADOW.getBlue(), 32));
            g2.fill(shape(5, 7, w - 3, h - 3));
            g2.setColor(new Color(SHADOW.getRed(), SHADOW.getGreen(), SHADOW.getBlue(), 18));
            g2.fill(shape(2, 4, w - 1, h - 1));

            RoundRectangle2D.Float card = shape(0, 0, w, h);
            g2.setColor(Color.WHITE);
            g2.fill(card);
            g2.setColor(new Color(228, 231, 236));
            g2.draw(card);

            g2.dispose();
        }
    }

    /**
     * Thẻ nền trắng bo góc.
     * Không clip {@code paintChildren}: trên một số môi trường (Windows + LAF hệ thống),
     * {@code Graphics2D.clip(RoundRectangle2D)} làm toàn bộ con không được vẽ.
     */
    public static class RoundedCardPanel extends JPanel {
        public static final int ARC = 22;

        public RoundedCardPanel(LayoutManager lm) {
            super(lm);
            setOpaque(false);
        }

        public RoundedCardPanel() {
            this(new BorderLayout());
        }

        private static RoundRectangle2D.Float shape(RoundedCardPanel p) {
            float w = Math.max(0, p.getWidth() - 1);
            float h = Math.max(0, p.getHeight() - 1);
            return new RoundRectangle2D.Float(0, 0, w, h, ARC, ARC);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            RoundRectangle2D.Float rr = shape(this);
            g2.setColor(Color.WHITE);
            g2.fill(rr);
            g2.setColor(CARD_BORDER);
            g2.draw(rr);
            g2.dispose();
        }
    }
}
