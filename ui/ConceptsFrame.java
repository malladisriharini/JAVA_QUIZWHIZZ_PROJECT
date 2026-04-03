package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ConceptsFrame extends JFrame {

    private String username;
    private Image backgroundImage;

    public ConceptsFrame(String username) {

        this.username = username;

        backgroundImage =
                new ImageIcon("resources/background_concepts.png").getImage();

        setTitle("QuizWhizz - Concepts");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen safe
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout()); // Responsive center layout

        // ===== GLASS PANEL =====
        JPanel glass = new GlassPanel();
        glass.setLayout(null);
        glass.setPreferredSize(new Dimension(600, 580));

        add(glass); // Automatically centered

        // ===== TITLE =====
        JLabel title = new JLabel("CONCEPTS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 55));
        title.setForeground(new Color(120, 255, 120));
        title.setBounds(0, 40, 600, 80);
        glass.add(title);

        // ===== TOP ROW =====
        glass.add(createButton("JAVA BASICS",
                new Color(220,70,70),
                60, 160, 140, 50));

        glass.add(createButton("OOPS",
                new Color(60,190,200),
                230, 160, 140, 50));

        glass.add(createButton("INHERITANCE",
                new Color(150,80,180),
                400, 160, 170, 50));

        // ===== SECOND ROW =====
        glass.add(createButton("POLYMORPHISM",
                new Color(255,160,60),
                80, 240, 200, 60));

        glass.add(createButton("ENCAPSULATION",
                new Color(255,80,150),
                320, 240, 200, 60));

        // ===== THIRD ROW =====
        glass.add(createButton("ABSTRACTION",
                new Color(0,200,200),
                80, 330, 200, 60));

        glass.add(createButton("EXCEPTION HANDLING",
                new Color(150,230,80),
                320, 330, 240, 60));

        // ===== FOURTH ROW =====
        glass.add(createButton("COLLECTIONS",
                new Color(255,80,150),
                200, 410, 200, 60));

        // ===== BACK BUTTON =====
        JButton backBtn = new RoundedButton(
                "BACK TO DASHBOARD",
                new Color(0,150,120));

        backBtn.setBounds(200, 490, 200, 50);
        glass.add(backBtn);

        backBtn.addActionListener(e -> {
            new DashboardFrame(username);
            dispose();
        });

        setVisible(true);
    }

    // ===== BACKGROUND PANEL =====
    class BackgroundPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0,
                    getWidth(), getHeight(), this);
        }
    }

    // ===== GLASS PANEL =====
    class GlassPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(15, 35, 55, 220));
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    40, 40);

            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(0, 255, 200));
            g2.drawRoundRect(2, 2,
                    getWidth()-4, getHeight()-4,
                    40, 40);
        }
    }

    // ===== BUTTON CREATOR =====
    private JButton createButton(String text,
                                 Color color,
                                 int x, int y,
                                 int w, int h) {

        JButton btn = new RoundedButton(text, color);
        btn.setBounds(x, y, w, h);

        btn.addActionListener(e -> {
            new InstructionsFrame(username, text);
            dispose();
        });

        return btn;
    }

    // ===== ROUNDED BUTTON =====
    class RoundedButton extends JButton {

        private Color color;

        public RoundedButton(String text, Color color) {
            super(text);
            this.color = color;
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(
                    0, 0, color.brighter(),
                    0, getHeight(), color.darker());

            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth(), getHeight(),
                    25, 25));

            super.paintComponent(g);
        }
    }
}