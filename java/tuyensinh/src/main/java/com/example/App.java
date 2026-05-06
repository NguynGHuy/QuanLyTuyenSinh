package com.example;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.ui.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings({"UseSpecificCatch", "override"})
            public void run() {
                try {
                    FlatLightLaf.setup();
                } catch (Exception e) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
                // Mở cửa sổ đăng nhập
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}