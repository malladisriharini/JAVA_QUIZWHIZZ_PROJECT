package ui;

import dao.ResultDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class LeaderboardFrame extends JFrame {

    private String username;

    public LeaderboardFrame(String username) {

        this.username = username;

        setTitle("Leaderboard - QuizWhizz");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1100, 750));
        add(mainPanel, gbc);

        JLabel title = new JLabel("QUIZWHIZZ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setBounds(300, 60, 500, 60);
        mainPanel.add(title);

        JLabel subtitle = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        subtitle.setForeground(new Color(0, 200, 255));
        subtitle.setBounds(350, 120, 400, 40);
        mainPanel.add(subtitle);

        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };

        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBounds(180, 200, 740, 350);
        mainPanel.add(card);

        String[] columns = {"RANK", "USERNAME", "TESTS ATTEMPTED", "SCORE"};

        List<String[]> data = ResultDAO.getLeaderboardData();

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        int rank = 1;

        for (String[] row : data) {
            String name = row[0];
            String score = row[1];
            String tests = row[2];
            model.addRow(new Object[]{rank, name, tests, score});
            rank++;
        }

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(20, 40, 70));
        table.setGridColor(new Color(0, 200, 255, 80));
        table.setSelectionBackground(new Color(0, 170, 255));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(new Color(0, 200, 255));
        header.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        card.add(scroll, BorderLayout.CENTER);

        JButton back = new RoundedButton("BACK TO DASHBOARD",
                new Color(60, 200, 100));

        back.setBounds(420, 600, 260, 50);
        mainPanel.add(back);

        back.addActionListener(e -> {
            new DashboardFrame(username);
            dispose();
        });

        setVisible(true);
    }

    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {

            setLayout(new GridBagLayout());

            // Load background image
            backgroundImage = new ImageIcon("resources/background_leaderboard.png").getImage();
        }

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            // Draw background image
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            // Optional glow border (same as before)
            //g2.setColor(new Color(0, 255, 255, 60));
            //g2.drawRoundRect(150, 180, 800, 400, 50, 50);
        }
    }

    class RoundedButton extends JButton {

        Color color;

        public RoundedButton(String text, Color c) {
            super(text);
            this.color = c;
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder());
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }
    }
}