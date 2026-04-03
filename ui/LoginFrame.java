package ui;

import dao.UserDAO;
import ui.AdminDashboard;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private Image backgroundImage;
    private Image logoImage;

    public LoginFrame() {

        backgroundImage = new ImageIcon("resources/backgrounds.png").getImage();
        logoImage = new ImageIcon("resources/logo.png").getImage();

        setTitle("QuizWhizz");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1100, 750));
        add(mainPanel, gbc);

        JLabel title = new JLabel("QuizWhizz", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 75));
        title.setForeground(Color.WHITE);
        title.setBounds(250, 50, 600, 100);
        mainPanel.add(title);

        JLabel tagline = new JLabel("Learn, Play and Win", SwingConstants.CENTER);
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tagline.setForeground(new Color(0, 255, 220));
        tagline.setBounds(300, 140, 500, 50);
        mainPanel.add(tagline);

        JLabel logo = new JLabel();
        logo.setBounds(100, 220, 400, 400);
        logo.setIcon(new ImageIcon(
                logoImage.getScaledInstance(400, 400, Image.SCALE_SMOOTH)
        ));
        mainPanel.add(logo);

        JPanel card = new GlassPanel();
        card.setLayout(null);
        card.setBounds(650, 280, 350, 350);
        mainPanel.add(card);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 40, 200, 20);
        card.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBounds(50, 60, 250, 40);
        card.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(50, 120, 200, 20);
        card.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBounds(50, 140, 250, 40);
        card.add(passwordField);

        JButton loginBtn = new RoundedButton("LOGIN",
                new Color(0, 200, 255));
        loginBtn.setBounds(80, 200, 190, 45);
        card.add(loginBtn);

        JLabel forgotPassword = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotPassword.setForeground(Color.CYAN);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.setBounds(110, 255, 150, 20);
        card.add(forgotPassword);

        JLabel signup = new JLabel("<HTML><U>Don't have an account? Sign Up</U></HTML>");
        signup.setForeground(Color.CYAN);
        signup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signup.setBounds(60, 290, 250, 20);
        card.add(signup);

        // ===== LOGIN ACTION =====
        loginBtn.addActionListener(e -> {

            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter Username and Password");
                return;
            }

            // ===== ADMIN LOGIN =====
            if (user.equals("admin") && pass.equals("admin123")) {
                new AdminDashboard();
                dispose();
                return;
            }

            // ===== NORMAL USER LOGIN =====
            boolean valid = UserDAO.login(user, pass);

            if (valid) {
                new DashboardFrame(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password");
            }
        });

        // ===== FORGOT PASSWORD =====
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String username = JOptionPane.showInputDialog(
                        LoginFrame.this,
                        "Enter your username:"
                );

                if (username == null || username.trim().isEmpty())
                    return;

                String newPass = JOptionPane.showInputDialog(
                        LoginFrame.this,
                        "Enter your new password:"
                );

                if (newPass == null || newPass.trim().isEmpty())
                    return;

                boolean updated = UserDAO.updatePassword(username, newPass);

                if (updated) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Username not found!");
                }
            }
        });

        signup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame();
                dispose();
            }
        });

        setVisible(true);
    }

    class BackgroundPanel extends JPanel {

        public BackgroundPanel() {
            setLayout(new GridBagLayout());
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage,
                    0, 0,
                    getWidth(), getHeight(),
                    this);
        }
    }

    class GlassPanel extends JPanel {

        public GlassPanel() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(10, 25, 50, 190));
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    30, 30);

            super.paintComponent(g);
        }
    }

    class RoundedButton extends JButton {

        Color color;

        public RoundedButton(String text, Color c) {
            super(text);
            this.color = c;
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    30, 30);
            super.paintComponent(g);
        }
    }
}