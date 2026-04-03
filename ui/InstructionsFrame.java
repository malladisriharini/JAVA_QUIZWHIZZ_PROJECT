package ui;

import javax.swing.*;
import java.awt.*;

public class InstructionsFrame extends JFrame {

    private String username;
    private String concept;
    private Image backgroundImage;

    public InstructionsFrame(String username, String concept) {

        this.username = username;
        this.concept = concept;

        backgroundImage =
                new ImageIcon("resources/background_instructions.png").getImage();

        setTitle("Quiz Instructions");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen safe
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout()); // Responsive centering

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // ===== MAIN NEON PANEL =====
        JPanel panel = new NeonPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(450, 520));
        add(panel, gbc);

        // ===== TITLE =====
        JLabel title = new JLabel("QUIZ INSTRUCTIONS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(50, 40, 350, 40);
        panel.add(title);

        // ===== INSTRUCTIONS TEXT =====
        String text =
                "<html>" +
                "&#x1F552;  Total Questions: 10<br><br>" +
                "&#x23F1;  Time Limit: 5 minutes<br><br>" +
                "&#x2705;  Correct answer: +1 point<br><br>" +
                "&#x26A0;  No negative points<br><br>" +
                "&#x1F4CC;  Select one correct answer per question<br><br>" +
                "The test will automatically submit<br>" +
                "when the timer ends.<br><br>" +
                "Click \"START\" to begin." +
                "</html>";

        JLabel instructions = new JLabel(text);
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        instructions.setForeground(new Color(200, 255, 255));
        instructions.setBounds(60, 120, 330, 280);
        panel.add(instructions);

        // ===== START BUTTON =====
        JButton startBtn = new GlowButton("START QUIZ");
        startBtn.setBounds(60, 420, 160, 50);
        panel.add(startBtn);

        startBtn.addActionListener(e -> {
            new QuizFrame(username, concept);
            dispose();
        });

        // ===== BACK BUTTON =====
        JButton backBtn = new GlowButton("BACK");
        backBtn.setBounds(230, 420, 160, 50);
        panel.add(backBtn);

        backBtn.addActionListener(e -> {
            new DashboardFrame(username);
            dispose();
        });

        setVisible(true);
    }

    // ===== BACKGROUND PANEL =====
    class BackgroundPanel extends JPanel {
        public BackgroundPanel() {
            setLayout(new GridBagLayout()); // Important for centering
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0,
                    getWidth(), getHeight(), this);
        }
    }

    // ===== NEON PANEL =====
    class NeonPanel extends JPanel {
        public NeonPanel() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(10, 30, 50, 230));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(0, 255, 255));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
        }
    }

    // ===== GLOW BUTTON =====
    class GlowButton extends JButton {

        public GlowButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(0, 255, 150),
                    0, getHeight(), new Color(0, 180, 120)
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            super.paintComponent(g);
        }
    }
}