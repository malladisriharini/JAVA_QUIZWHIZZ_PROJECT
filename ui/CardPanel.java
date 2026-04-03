package ui;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    public CardPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g); // Important for proper resizing

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Theme.CARD_BG);
        g2.fillRoundRect(0, 0,
                getWidth(), getHeight(),
                20, 20);

        g2.dispose();
    }
}