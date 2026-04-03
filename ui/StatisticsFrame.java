package ui;

import dao.ResultDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StatisticsFrame extends JFrame {

    private String username;
    private Image backgroundImage;

    public StatisticsFrame(String username) {

        this.username = username;

        backgroundImage =
                new ImageIcon("resources/background_statistics.png").getImage();

        setTitle("QuizWhizz - Statistics");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel rootPanel = new JPanel();
        rootPanel.setOpaque(false);
        rootPanel.setLayout(null);
        rootPanel.setPreferredSize(new Dimension(1100, 750));
        add(rootPanel, gbc);

        JPanel mainPanel = new GlassPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(150, 80, 800, 550);
        rootPanel.add(mainPanel);

        JLabel title = new JLabel("STATISTICS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 45));
        title.setForeground(Color.WHITE);
        title.setBounds(200, 30, 400, 60);
        mainPanel.add(title);

        JPanel contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBounds(50, 110, 700, 350);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane);

        Map<String, Double> stats =
                ResultDAO.getConceptWisePercentage(username);

        int y = 20;

        for (String concept : stats.keySet()) {

            double percent = stats.get(concept);

            JLabel name = new JLabel(toCamelCase(concept)); // Camel Case applied
            name.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            name.setForeground(Color.WHITE);
            name.setBounds(30, y, 200, 30);
            contentPanel.add(name);

            JPanel bar = new PercentageBar(percent);
            bar.setBounds(200, y, 350, 30);
            contentPanel.add(bar);

            JLabel percentLabel =
                    new JLabel(String.format("%.0f%%", percent));
            percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            percentLabel.setForeground(new Color(0, 255, 180));
            percentLabel.setBounds(570, y, 80, 30);
            contentPanel.add(percentLabel);

            y += 60;
        }

        contentPanel.setPreferredSize(new Dimension(680, y + 20));

        JButton back = new JButton("BACK TO DASHBOARD");
        back.setBounds(280, 480, 240, 45);
        back.setBackground(new Color(0, 200, 100));
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        mainPanel.add(back);

        back.addActionListener(e -> {
            new DashboardFrame(username);
            dispose();
        });

        setVisible(true);
    }

    // ===== Camel Case Method =====
    private String toCamelCase(String text) {

        if (text == null || text.isEmpty()) return text;

        String[] words = text.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }

        return result.toString().trim();
    }

    class BackgroundPanel extends JPanel {

        public BackgroundPanel() {
            setLayout(new GridBagLayout());
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0,
                    getWidth(), getHeight(), this);
        }
    }

    class GlassPanel extends JPanel {

        public GlassPanel() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(20, 40, 60, 230));
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    40, 40);

            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(0, 255, 200));
            g2.drawRoundRect(2, 2,
                    getWidth() - 4, getHeight() - 4,
                    40, 40);
        }
    }

    class PercentageBar extends JPanel {

        double percent;

        public PercentageBar(double percent) {
            this.percent = percent;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(60, 80, 100));
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    20, 20);

            int width = (int) (getWidth() * (percent / 100));

            GradientPaint gp =
                    new GradientPaint(0, 0,
                            new Color(0, 170, 255),
                            width, 0,
                            new Color(0, 255, 150));

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0,
                    width, getHeight(),
                    20, 20);
        }
    }
}