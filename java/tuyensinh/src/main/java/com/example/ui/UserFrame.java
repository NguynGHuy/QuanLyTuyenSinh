package com.example.ui;

import com.example.entity.ThiSinh;
import com.example.ui.UiShellTheme.RoundedCardPanel;
import com.example.ui.UiShellTheme.ShellGradientPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserFrame extends JFrame {

    public UserFrame(ThiSinh ts) {
        setTitle("Cổng thông tin Thí sinh - SGU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UiFrameDefaults.MAIN_SIZE);
        setLocationRelativeTo(null);
        setMinimumSize(UiFrameDefaults.MAIN_MIN);

        ShellGradientPanel root = new ShellGradientPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);

        int gap = 20;
        int outer = 28;
        JPanel mainRow = new JPanel(new BorderLayout(gap, 0));
        mainRow.setOpaque(false);
        mainRow.setBorder(new EmptyBorder(outer, outer, outer, outer));

        RoundedCardPanel sidebarCard = new RoundedCardPanel(new BorderLayout());
        sidebarCard.setBorder(new EmptyBorder(18, 16, 18, 16));
        sidebarCard.setPreferredSize(new Dimension(280, 0));

        JPanel sideCanvas = new JPanel(new BorderLayout(0, 6));
        sideCanvas.setOpaque(true);
        sideCanvas.setBackground(SidebarUi.SIDEBAR_CANVAS);
        CardLayout cardLayout = new CardLayout();
        JPanel deck = new JPanel(cardLayout);
        deck.add(new TraCuuPanel(), "tracuu");
        deck.add(new StudentNguyenVongPanel(ts), "nv");

        List<String> labels = new ArrayList<>(List.of("Xem Kết Quả Chung", "Đăng Ký Nguyện Vọng"));
        List<String> icons = new ArrayList<>(List.of("🔍", "📄"));
        List<String> cardIds = new ArrayList<>(List.of("tracuu", "nv"));

        JButton btnLogout = SidebarUi.createOutlineLogoutButton();
        sideCanvas.add(
                SidebarUi.buildCollapsibleNavBlock(sidebarCard, labels, icons,
                        i -> cardLayout.show(deck, cardIds.get(i)), btnLogout),
                BorderLayout.CENTER);

        sidebarCard.add(sideCanvas, BorderLayout.CENTER);
        sidebarCard.add(btnLogout, BorderLayout.SOUTH);

        RoundedCardPanel contentCard = new RoundedCardPanel(new BorderLayout());
        contentCard.setBorder(new EmptyBorder(22, 22, 22, 22));
        contentCard.add(deck, BorderLayout.CENTER);

        mainRow.add(sidebarCard, BorderLayout.WEST);
        mainRow.add(contentCard, BorderLayout.CENTER);
        root.add(mainRow, BorderLayout.CENTER);

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
    }
}
