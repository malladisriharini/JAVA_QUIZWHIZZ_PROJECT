package ui;

import dao.QuestionDAO;
import dao.ResultDAO;
import model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class QuizFrame extends JFrame {

    private String username;
    private String concept;

    private List<Question> questions;
    private String[] answers;
    private int current = 0;
    private int totalQuestions;

    private JLabel questionLabel, timerLabel;
    private JButton[] optionButtons;
    private JButton[] numberButtons;

    private Timer timer;
    private int timeRemaining = 300;

    private Image backgroundImage;

    public QuizFrame(String username, String concept) {

        this.username = username;
        this.concept = concept.trim();

        backgroundImage =
                new ImageIcon("resources/background_quiz.png").getImage();

        setTitle("QuizWhizz - Java Quiz");
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

        questions = QuestionDAO.getQuestionsByConcept(this.concept);

        if (questions == null || questions.size() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No questions found for: " + this.concept);
            new ConceptsFrame(username);
            dispose();
            return;
        }

        Collections.shuffle(questions);
        totalQuestions = Math.min(10, questions.size());
        questions = questions.subList(0, totalQuestions);
        answers = new String[totalQuestions];

        JPanel glass = new GlassPanel();
        glass.setLayout(null);
        glass.setBounds(180, 80, 700, 580);
        mainPanel.add(glass);

        JLabel title = new JLabel("JAVA QUIZ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(new Color(120,255,120));
        title.setBounds(0, 20, 700, 50);
        glass.add(title);

        timerLabel = new JLabel("05:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setBounds(520, 20, 120, 40);
        glass.add(timerLabel);

        startTimer();

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBounds(50, 100, 600, 90);
        glass.add(questionLabel);

        optionButtons = new JButton[4];
        int y = 210;

        for(int i=0;i<4;i++){

            int index = i;

            optionButtons[i] = new JButton();
            optionButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 18));
            optionButtons[i].setBounds(80, y, 540, 50);
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setBackground(new Color(40,70,90));
            optionButtons[i].setForeground(Color.WHITE);

            optionButtons[i].addActionListener(e -> {
                answers[current] = String.valueOf((char)('A'+index));
                highlightSelected();
                highlightNumbers();
            });

            glass.add(optionButtons[i]);
            y += 65;
        }

        JButton prev = createNavButton("Previous", new Color(150,150,150));
        prev.setBounds(180, 500, 120, 45);
        glass.add(prev);

        JButton next = createNavButton("Next", new Color(30,170,220));
        next.setBounds(320, 500, 120, 45);
        glass.add(next);

        JButton submit = createNavButton("Submit", new Color(0,200,100));
        submit.setBounds(460, 500, 120, 45);
        glass.add(submit);

        prev.addActionListener(e -> {
            if(current > 0){
                current--;
                loadQuestion();
            }
        });

        next.addActionListener(e -> {
            if(current < totalQuestions - 1){
                current++;
                loadQuestion();
            }
        });

        submit.addActionListener(e -> submitQuiz());

        numberButtons = new JButton[totalQuestions];
        int ny = 130;

        for(int i=0;i<totalQuestions;i++){

            int index = i;

            numberButtons[i] = new CircleButton(String.valueOf(i+1));
            numberButtons[i].setBounds(930, ny, 55, 55);
            mainPanel.add(numberButtons[i]);

            numberButtons[i].addActionListener(e -> {
                current = index;
                loadQuestion();
            });

            ny += 60;
        }

        loadQuestion();
        setVisible(true);
    }

    private void loadQuestion(){

        Question q = questions.get(current);

        questionLabel.setText(
                "<html>Question " + (current+1) + "/" + totalQuestions +
                ":<br>" + q.getQuestion() + "</html>");

        optionButtons[0].setText("A. " + q.getA());
        optionButtons[1].setText("B. " + q.getB());
        optionButtons[2].setText("C. " + q.getC());
        optionButtons[3].setText("D. " + q.getD());

        highlightSelected();
        highlightNumbers();
    }

    private void highlightSelected(){
        for(int i=0;i<4;i++){
            optionButtons[i].setBackground(new Color(40,70,90));
            if(answers[current] != null &&
               answers[current].equals(String.valueOf((char)('A'+i)))){
                optionButtons[i].setBackground(new Color(0,200,255));
            }
        }
    }

    private void highlightNumbers(){
        for(int i=0;i<totalQuestions;i++){
            numberButtons[i].setBackground(new Color(40,40,40));
            if(answers[i] != null){
                numberButtons[i].setBackground(new Color(0,200,255));
            }
            if(i == current){
                numberButtons[i].setBackground(new Color(120,255,120));
            }
        }
    }

    private void startTimer(){
        timer = new Timer(1000, e -> {
            timeRemaining--;
            int min = timeRemaining / 60;
            int sec = timeRemaining % 60;
            timerLabel.setText(String.format("%02d:%02d", min, sec));
            if(timeRemaining <= 0){
                timer.stop();
                submitQuiz();
            }
        });
        timer.start();
    }

    private void submitQuiz(){
        timer.stop();
        int score = 0;
        for(int i=0;i<totalQuestions;i++){
            if(answers[i] != null &&
               answers[i].equalsIgnoreCase(
                       questions.get(i).getCorrect())){
                score++;
            }
        }
        ResultDAO.saveResult(username, concept, score, totalQuestions);
        new ReviewFrame(username, concept, questions, answers);
        dispose();
    }

    class BackgroundPanel extends JPanel {
        public BackgroundPanel(){
            setLayout(new GridBagLayout());
        }
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0,
                    getWidth(), getHeight(), this);
        }
    }

    class GlassPanel extends JPanel {
        public GlassPanel(){ setOpaque(false); }
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(15,35,55,220));
            g2.fillRoundRect(0,0,getWidth(),getHeight(),40,40);
            g2.setColor(new Color(0,255,200));
            g2.drawRoundRect(2,2,getWidth()-4,getHeight()-4,40,40);
        }
    }

    private JButton createNavButton(String text, Color color){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        return btn;
    }

    class CircleButton extends JButton {
        public CircleButton(String text){
            super(text);
            setForeground(Color.WHITE);
            setBackground(new Color(40,40,40));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getBackground());
            g2.fillOval(0,0,getWidth(),getHeight());
            super.paintComponent(g);
        }
    }
}