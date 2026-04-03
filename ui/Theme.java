package ui;

import javax.swing.*;
import java.awt.*;

public class Theme {

    public static final Color BACKGROUND = new Color(10,25,60);
    public static final Color PRIMARY = new Color(0,200,255);
    public static final Color SECONDARY = new Color(0,150,220);
    public static final Color ACCENT = new Color(0,255,180);
    public static final Color SUCCESS = new Color(0,180,120);
    public static final Color DANGER = new Color(220,60,60);
    public static final Color CARD_BG = new Color(20,40,80);

    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color TEXT_DARK = Color.BLACK;

    // DPI-aware font scaling
    private static final int BASE_SIZE =
            UIManager.getFont("Label.font").getSize();

    public static Font TITLE_FONT =
            new Font("Segoe UI", Font.BOLD, BASE_SIZE + 12);

    public static Font NORMAL_FONT =
            new Font("Segoe UI", Font.PLAIN, BASE_SIZE);

    public static Font BUTTON_FONT =
            new Font("Segoe UI", Font.BOLD, BASE_SIZE);

    public static JButton styledButton(String text, Color color) {

        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(BUTTON_FONT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        return btn;
    }
}