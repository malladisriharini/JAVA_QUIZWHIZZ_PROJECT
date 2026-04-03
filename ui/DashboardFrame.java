package ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private String username;

    public DashboardFrame(String username) {

        this.username = username;

        setTitle("QuizWhizz Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen safe
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout()); // Responsive center layout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // ===== MAIN PANEL (HOLDS EVERYTHING) =====
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1100, 750));
        add(mainPanel, gbc);

        // ===== TITLE =====
        JLabel title = new JLabel(
                "<html><span style='color:white;'>QUIZ</span>" +
                "<span style='color:#00E5FF;'>WHIZZ</span></html>",
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 58));
        title.setBounds(300, 90, 500, 70);
        mainPanel.add(title);

        // ===== SUBTITLE =====
        JLabel subtitle = new JLabel("DASHBOARD", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        subtitle.setForeground(Color.WHITE);
        subtitle.setBounds(350, 160, 400, 40);
        mainPanel.add(subtitle);

        // ===== GLOW CONTAINER =====
        JPanel container = new GlowPanel();
        container.setLayout(null);
        container.setBounds(120, 250, 860, 240);
        mainPanel.add(container);

        // ===== BUTTONS =====
        JButton concepts = new NeonButton("CONCEPTS",
                new Color(255,140,0));
        concepts.setBounds(70, 80, 170, 70);
        container.add(concepts);

        JButton leaderboard = new NeonButton("LEADERBOARD",
                new Color(0,170,255));
        leaderboard.setBounds(270, 80, 170, 70);
        container.add(leaderboard);

        JButton stats = new NeonButton("STATISTICS",
                new Color(0,200,120));
        stats.setBounds(470, 80, 170, 70);
        container.add(stats);

        JButton logout = new NeonButton("LOG OUT",
                new Color(255,70,70));
        logout.setBounds(670, 80, 170, 70);
        container.add(logout);

        // ===== BUTTON ACTIONS =====
        concepts.addActionListener(e -> {
            new ConceptsFrame(username);
            dispose();
        });

        leaderboard.addActionListener(e -> {
            new LeaderboardFrame(username);
            dispose();
        });

        stats.addActionListener(e -> {
            new StatisticsFrame(username);
            dispose();
        });

        logout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    // ===== BACKGROUND PANEL =====
    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("resources/background_dashboard.png").getImage();
            setLayout(new GridBagLayout()); // Important for centering
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(backgroundImage,
                    0, 0,
                    getWidth(), getHeight(),
                    this);
        }
    }

    // ===== GLOW PANEL =====
    class GlowPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(15, 30, 45));
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    40, 40);

            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(0, 200, 255));
            g2.drawRoundRect(2, 2,
                    getWidth()-4, getHeight()-4,
                    40, 40);
        }
    }

    // ===== NEON BUTTON =====
    class NeonButton extends JButton {

        private Color color;

        public NeonButton(String text, Color color) {
            super(text);
            this.color = color;
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(
                    0, 0, color.brighter(),
                    0, getHeight(), color.darker()
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    25, 25);

            super.paintComponent(g);
        }
    }
}