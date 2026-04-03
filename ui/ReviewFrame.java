package ui;

import model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReviewFrame extends JFrame {

    private List<Question> questions;
    private String[] answers;
    private int current = 0;
    private String username;

    private JLabel questionLabel;
    private JPanel contentPanel;
    private JButton[] numberButtons;
    private JLabel scoreLabel;

    public ReviewFrame(String username, String concept,
                       List<Question> questions, String[] answers) {

        this.username = username;
        this.questions = questions;
        this.answers = answers;

        setTitle("QuizWhizz - Review");
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
        rootPanel.setPreferredSize(new Dimension(1100, 700));
        add(rootPanel, gbc);

        JLabel reviewTitle = new JLabel("REVIEW YOUR ANSWERS", SwingConstants.CENTER);
        reviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        reviewTitle.setForeground(Color.WHITE);
        reviewTitle.setBounds(350, 20, 400, 30);
        rootPanel.add(reviewTitle);

        JPanel mainPanel = new GlassPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(120, 70, 850, 560);
        rootPanel.add(mainPanel);

        JLabel title = new JLabel("QUIZWHIZZ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBounds(250, 20, 350, 50);
        mainPanel.add(title);

        int score = calculateScore();

        scoreLabel = new JLabel("SCORE: " + score + " / 10");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setForeground(new Color(0,255,180));
        scoreLabel.setBounds(650, 25, 180, 30);
        mainPanel.add(scoreLabel);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBounds(50, 90, 300, 30);
        mainPanel.add(questionLabel);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(40, 130, 650, 360);
        mainPanel.add(contentPanel);

        JPanel numberPanel = new JPanel(new GridLayout(10,1,8,8));
        numberPanel.setBounds(730, 100, 60, 380);
        numberPanel.setOpaque(false);
        mainPanel.add(numberPanel);

        numberButtons = new JButton[10];

        for(int i=0;i<10;i++){
            int index = i;
            numberButtons[i] = new CircleButton(String.valueOf(i+1));
            numberPanel.add(numberButtons[i]);

            numberButtons[i].addActionListener(e -> {
                current = index;
                loadReview();
            });
        }

        JButton prev = new GradientButton("PREVIOUS",
                new Color(120,120,120));
        prev.setBounds(180, 500, 140, 45);
        mainPanel.add(prev);

        prev.addActionListener(e -> {
            if(current > 0){
                current--;
                loadReview();
            }
        });

        JButton next = new GradientButton("NEXT",
                new Color(0,150,255));
        next.setBounds(340, 500, 140, 45);
        mainPanel.add(next);

        next.addActionListener(e -> {
            if(current < 9){
                current++;
                loadReview();
            }
        });

        JButton back = new GradientButton("BACK TO DASHBOARD",
                new Color(0,150,120));
        back.setBounds(500, 500, 220, 45);
        mainPanel.add(back);

        back.addActionListener(e -> {
            new DashboardFrame(username);
            dispose();
        });

        loadReview();
        setVisible(true);
    }

    private int calculateScore(){
        int score = 0;
        for(int i=0;i<questions.size();i++){
            if(answers[i] != null &&
               answers[i].equalsIgnoreCase(
                       questions.get(i).getCorrect())){
                score++;
            }
        }
        return score;
    }

    private void loadReview(){
        Question q = questions.get(current);
        questionLabel.setText("QUESTION " + (current+1) + "/10");

        contentPanel.removeAll();

        JLabel questionText = new JLabel("<html>"+q.getQuestion()+"</html>");
        questionText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        questionText.setForeground(Color.WHITE);
        questionText.setBounds(10, 0, 600, 40);
        contentPanel.add(questionText);

        String[] opts = { q.getA(), q.getB(), q.getC(), q.getD() };

        int y = 60;

        for(int i=0;i<4;i++){

            JPanel optionCard = new OptionCard();
            optionCard.setBounds(0, y, 620, 55);
            optionCard.setLayout(null);

            JLabel text = new JLabel("<html>"+(char)('A'+i) + ") " + opts[i]+"</html>");
            text.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            text.setForeground(Color.WHITE);
            text.setBounds(20, 10, 420, 35);
            optionCard.add(text);

            String correct = q.getCorrect();
            String userAns = answers[current];

            if(correct.equalsIgnoreCase(String.valueOf((char)('A'+i)))){

                JLabel correctTag = new JLabel("Correct");
                correctTag.setOpaque(true);
                correctTag.setBackground(new Color(0,180,90));
                correctTag.setForeground(Color.WHITE);
                correctTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
                correctTag.setBounds(470, 12, 110, 30);
                optionCard.add(correctTag);
            }

            if(userAns != null &&
               userAns.equalsIgnoreCase(String.valueOf((char)('A'+i))) &&
               !userAns.equalsIgnoreCase(correct)){

                JLabel wrongTag = new JLabel("Your Answer");
                wrongTag.setOpaque(true);
                wrongTag.setBackground(new Color(200,50,50));
                wrongTag.setForeground(Color.WHITE);
                wrongTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
                wrongTag.setBounds(470, 12, 120, 30);
                optionCard.add(wrongTag);
            }

            contentPanel.add(optionCard);
            y += 75;
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ===== Background Panel with Image =====
    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(){
            setLayout(new GridBagLayout());
            backgroundImage = new ImageIcon("resources/background_review.png").getImage();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            g2.setColor(new Color(0,0,0,120));
            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }

    class GlassPanel extends JPanel {
        public GlassPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(20,40,60,230));
            g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
        }
    }

    class OptionCard extends JPanel {
        public OptionCard() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(40,70,90));
            g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
        }
    }

    class GradientButton extends JButton {
        Color color;
        public GradientButton(String text, Color c){
            super(text);
            this.color = c;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI",Font.BOLD,14));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
        }
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
            super.paintComponent(g);
        }
    }

    class CircleButton extends JButton{
        public CircleButton(String text){
            super(text);
            setForeground(Color.WHITE);
            setBackground(new Color(70,70,70));
            setFocusPainted(false);
            setBorderPainted(false);
        }
    }
}