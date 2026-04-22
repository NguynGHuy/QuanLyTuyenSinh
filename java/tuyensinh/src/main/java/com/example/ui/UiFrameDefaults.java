package com.example.ui;

import java.awt.*;

/** Kích thước mặc định thống nhất giữa màn đăng nhập và khung làm việc (~+10% so với 1200×720). */
public final class UiFrameDefaults {

    /** Cửa sổ mở mặc định: 1200×720 → ~1320×800 (bộ số chẵn, gần 11:6.67). */
    public static final Dimension MAIN_SIZE = new Dimension(1320, 800);
    /** Kích thước tối thiểu co được: ~+10% so với 960×560. */
    public static final Dimension MAIN_MIN = new Dimension(1060, 620);

    /** Thẻ form đăng nhập (LoginFrame), cùng nhịp tỷ lệ với cửa sổ. */
    public static final Dimension LOGIN_CARD = new Dimension(480, 572);

    private UiFrameDefaults() {
    }
}
