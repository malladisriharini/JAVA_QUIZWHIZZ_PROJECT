package ui;

import javax.swing.*;
import java.awt.*;

public class ResultFrame extends JFrame {

    public ResultFrame(String username,
                       int score,
                       int total) {

        setTitle("Result - QuizWhizz");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen safe
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout()); // Center everything

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // ===== MAIN DESIGN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1100, 750));
        add(mainPanel, gbc);

        int percent = (score * 100) / total;

        // ===== TITLE =====
        JLabel title = new JLabel("QUIZ RESULT",
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI",
                Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBounds(350, 100, 400, 60);
        mainPanel.add(title);

        // ===== GLASS CARD =====
        JPanel card = new GlassPanel();
        card.setLayout(null);
        card.setBounds(375, 250, 350, 300);
        mainPanel.add(card);

        JLabel scoreLabel = new JLabel(
                "Score: " + score + " / " + total,
                SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI",
                Font.BOLD, 26));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(50, 60, 250, 40);
        card.add(scoreLabel);

        JLabel percentLabel = new JLabel(
                percent + "%",
                SwingConstants.CENTER);
        percentLabel.setFont(new Font("Segoe UI",
                Font.BOLD, 48));
        percentLabel.setForeground(new Color(0, 255, 150));
        percentLabel.setBounds(50, 110, 250, 60);
        card.add(percentLabel);

        JButton reviewBtn = new RoundedButton(
                "REVIEW",
                new Color(0, 170, 255));
        reviewBtn.setBounds(80, 190, 190, 45);
        card.add(reviewBtn);

        JButton dashboardBtn = new RoundedButton(
                "BACK TO DASHBOARD",
                new Color(60, 200, 100));
        dashboardBtn.setBounds(400, 600, 260, 50);
        mainPanel.add(dashboardBtn);

        reviewBtn.addActionListener(e -> {
            // handled from QuizFrame
        });

        dashboardBtn.addActionListener(e -> {
            new DashboardFrame(username);
            dispose();
        });

        setVisible(true);
    }

    // ===== Background =====
    class BackgroundPanel extends JPanel {

        public BackgroundPanel() {
            setLayout(new GridBagLayout()); // Important for centering
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(5, 20, 50),
                    0, getHeight(), new Color(0, 10, 30)
            );

            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ===== Glass =====
    class GlassPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }
    }

    class RoundedButton extends JButton {
        Color color;

        public RoundedButton(String text, Color c) {
            super(text);
            color = c;
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }
    }
}