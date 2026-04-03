package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    private Image backgroundImage;

    public RegisterFrame() {

        backgroundImage = new ImageIcon("resources/backgrounds.png").getImage();

        setTitle("Create Account - QuizWhizz");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout());

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

        // ===== TITLE =====
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 55));
        title.setForeground(Color.WHITE);
        title.setBounds(250, 70, 600, 80);
        mainPanel.add(title);

        // ===== GLASS CARD (UPDATED) =====
        JPanel card = new GlassPanel();
        card.setLayout(null);
        card.setBounds(375, 220, 350, 420);
        mainPanel.add(card);

        // ===== USERNAME LABEL =====
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 40, 250, 20);
        card.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBounds(50, 60, 250, 40);
        usernameField.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        card.add(usernameField);

        // ===== PASSWORD LABEL =====
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(50, 120, 250, 20);
        card.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBounds(50, 140, 250, 40);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        card.add(passwordField);

        // ===== CONFIRM LABEL =====
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setBounds(50, 200, 250, 20);
        card.add(confirmLabel);

        confirmField = new JPasswordField();
        confirmField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmField.setBounds(50, 220, 250, 40);
        confirmField.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        card.add(confirmField);

        // ===== SIGN UP BUTTON =====
        JButton registerBtn = new RoundedButton("SIGN UP",
                new Color(0, 200, 255));
        registerBtn.setBounds(80, 290, 190, 50);
        card.add(registerBtn);

        // ===== LOGIN LINK =====
        JLabel loginLink = new JLabel("<HTML><U>Already have an account? Login</U></HTML>");
        loginLink.setForeground(Color.CYAN);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setBounds(70, 360, 250, 20);
        card.add(loginLink);

        registerBtn.addActionListener(e -> register());

        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginFrame();
                dispose();
            }
        });

        setVisible(true);
    }

    private void register() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required");
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match");
            return;
        }

        boolean success = UserDAO.register(username, password);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Registration Successful!");
            new LoginFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username already exists");
        }
    }

    class BackgroundPanel extends JPanel {
        public BackgroundPanel(){
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

    // ===== UPDATED DARK GLASS PANEL =====
    class GlassPanel extends JPanel {

        public GlassPanel() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Dark navy translucent background
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