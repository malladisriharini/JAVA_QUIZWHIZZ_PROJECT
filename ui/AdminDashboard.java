
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame {

    CardLayout cardLayout;
    JPanel contentPanel;

    JLabel totalUsersLabel;
    JLabel totalQuestionsLabel;
    JLabel totalQuizzesLabel;
    JLabel avgScoreLabel;

    String DB_URL = "jdbc:mysql://localhost:3306/quizwhizz";
    String DB_USER = "root";
    String DB_PASS = "Harini@2007";

    public AdminDashboard(){

        setTitle("QuizWhizz Admin Dashboard");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(0,102,204),
                        getWidth(),0,new Color(0,153,153));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };

        header.setPreferredSize(new Dimension(1200,80));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("QUIZWHIZZ ADMIN PANEL");
        title.setFont(new Font("Segoe UI",Font.BOLD,30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));

        JLabel clock = new JLabel();
        clock.setFont(new Font("Segoe UI",Font.BOLD,16));
        clock.setForeground(Color.WHITE);
        clock.setBorder(BorderFactory.createEmptyBorder(0,0,0,30));

        header.add(title,BorderLayout.WEST);
        header.add(clock,BorderLayout.EAST);

        add(header,BorderLayout.NORTH);

        // Live clock
        new Timer(1000, e -> {
            clock.setText(
                    new SimpleDateFormat("dd MMM yyyy  HH:mm:ss")
                            .format(new Date())
            );
        }).start();

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(34,40,49));
        sidebar.setPreferredSize(new Dimension(240,600));
        sidebar.setLayout(new GridLayout(10,1,10,20));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30,15,30,15));

        JButton dashboardBtn = createButton("Dashboard");
        JButton addBtn = createButton("Add Question");
        JButton deleteBtn = createButton("Delete Question");
        JButton resultsBtn = createButton("View Results");
        JButton logoutBtn = createButton("Logout");

        sidebar.add(dashboardBtn);
        sidebar.add(addBtn);
        sidebar.add(deleteBtn);
        sidebar.add(resultsBtn);
        sidebar.add(logoutBtn);

        add(sidebar,BorderLayout.WEST);

        // CONTENT
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(dashboardPanel(),"dashboard");
        contentPanel.add(addQuestionPanel(),"add");
        contentPanel.add(deleteQuestionPanel(),"delete");
        contentPanel.add(viewResultsPanel(),"results");

        add(contentPanel,BorderLayout.CENTER);

        dashboardBtn.addActionListener(e->cardLayout.show(contentPanel,"dashboard"));
        addBtn.addActionListener(e->cardLayout.show(contentPanel,"add"));
        deleteBtn.addActionListener(e->cardLayout.show(contentPanel,"delete"));
        resultsBtn.addActionListener(e->cardLayout.show(contentPanel,"results"));

        logoutBtn.addActionListener(e->{
            new LoginFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    JButton createButton(String text){

        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI",Font.BOLD,18));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52,73,94));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                btn.setBackground(new Color(26,188,156));
            }
            public void mouseExited(MouseEvent e){
                btn.setBackground(new Color(52,73,94));
            }
        });

        return btn;
    }

    JPanel dashboardPanel(){

        JPanel panel = new JPanel(new GridLayout(2,2,40,40));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.setBackground(new Color(245,247,250));

        totalUsersLabel = new JLabel("0",SwingConstants.CENTER);
        totalQuestionsLabel = new JLabel("0",SwingConstants.CENTER);
        totalQuizzesLabel = new JLabel("0",SwingConstants.CENTER);
        avgScoreLabel = new JLabel("0",SwingConstants.CENTER);

        panel.add(statCard("Total Users", totalUsersLabel));
        panel.add(statCard("Total Questions", totalQuestionsLabel));
        panel.add(statCard("Total Quizzes", totalQuizzesLabel));
        panel.add(statCard("Average Score", avgScoreLabel));

        loadDashboardData();

        return panel;
    }

    JPanel statCard(String title, JLabel value){

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220),2));

        JLabel label = new JLabel(title,SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI",Font.BOLD,22));

        value.setFont(new Font("Segoe UI",Font.BOLD,40));
        value.setForeground(new Color(26,188,156));

        card.add(label,BorderLayout.NORTH);
        card.add(value,BorderLayout.CENTER);

        return card;
    }

    void animateCounter(JLabel label, int target){

        new Thread(() -> {
            int count = 0;
            try{
                while(count <= target){
                    int finalCount = count;
                    SwingUtilities.invokeLater(() ->
                            label.setText(String.valueOf(finalCount))
                    );
                    Thread.sleep(20);
                    count++;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    void loadDashboardData(){

        try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
            Statement st = con.createStatement()){

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM users");
            if(rs1.next()) animateCounter(totalUsersLabel, rs1.getInt(1));

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM questions");
            if(rs2.next()) animateCounter(totalQuestionsLabel, rs2.getInt(1));

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM results");
            if(rs3.next()) animateCounter(totalQuizzesLabel, rs3.getInt(1));

            ResultSet rs4 = st.executeQuery("SELECT AVG(score) FROM results");
            if(rs4.next()){
                String avg = rs4.getString(1);
                avgScoreLabel.setText(avg == null ? "0" : avg);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    JPanel addQuestionPanel(){

        JPanel panel = new JPanel(new GridLayout(8,2,15,15));
        panel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));

        Font labelFont = new Font("Segoe UI",Font.BOLD,18);
        Font fieldFont = new Font("Segoe UI",Font.PLAIN,17);

        JTextField concept = new JTextField();
        JTextField question = new JTextField();
        JTextField a = new JTextField();
        JTextField b = new JTextField();
        JTextField c = new JTextField();
        JTextField d = new JTextField();
        JTextField correct = new JTextField();

        concept.setFont(fieldFont);
        question.setFont(fieldFont);
        a.setFont(fieldFont);
        b.setFont(fieldFont);
        c.setFont(fieldFont);
        d.setFont(fieldFont);
        correct.setFont(fieldFont);

        JButton addBtn = new JButton("Add Question");
        addBtn.setFont(new Font("Segoe UI",Font.BOLD,18));
        addBtn.setBackground(new Color(0,150,136));
        addBtn.setForeground(Color.WHITE);

        panel.add(new JLabel("Concept")).setFont(labelFont);
        panel.add(concept);
        panel.add(new JLabel("Question")).setFont(labelFont);
        panel.add(question);
        panel.add(new JLabel("Option A")).setFont(labelFont);
        panel.add(a);
        panel.add(new JLabel("Option B")).setFont(labelFont);
        panel.add(b);
        panel.add(new JLabel("Option C")).setFont(labelFont);
        panel.add(c);
        panel.add(new JLabel("Option D")).setFont(labelFont);
        panel.add(d);
        panel.add(new JLabel("Correct Option")).setFont(labelFont);
        panel.add(correct);
        panel.add(new JLabel());
        panel.add(addBtn);

        addBtn.addActionListener(e->{

            if(concept.getText().isEmpty() || question.getText().isEmpty()){
                JOptionPane.showMessageDialog(this,"Please fill all fields");
                return;
            }

            try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS)){

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO questions(concept,question,option_a,option_b,option_c,option_d,correct) VALUES(?,?,?,?,?,?,?)");

                ps.setString(1,concept.getText());
                ps.setString(2,question.getText());
                ps.setString(3,a.getText());
                ps.setString(4,b.getText());
                ps.setString(5,c.getText());
                ps.setString(6,d.getText());
                ps.setString(7,correct.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Question Added");

                concept.setText("");
                question.setText("");
                a.setText("");
                b.setText("");
                c.setText("");
                d.setText("");
                correct.setText("");

                loadDashboardData();

            }catch(Exception ex){
                ex.printStackTrace();
            }

        });

        return panel;
    }

    JPanel deleteQuestionPanel(){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,40));

        JLabel label = new JLabel("Question ID");
        label.setFont(new Font("Segoe UI",Font.BOLD,20));

        JTextField id = new JTextField(12);
        id.setFont(new Font("Segoe UI",Font.PLAIN,18));

        JButton deleteBtn = new JButton("Delete Question");
        deleteBtn.setFont(new Font("Segoe UI",Font.BOLD,18));
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);

        panel.add(label);
        panel.add(id);
        panel.add(deleteBtn);

        deleteBtn.addActionListener(e->{

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this question?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if(confirm == JOptionPane.YES_OPTION){

                try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS)){

                    PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM questions WHERE id=?");

                    ps.setInt(1,Integer.parseInt(id.getText()));

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this,"Question Deleted");

                    loadDashboardData();

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        return panel;
    }

    JPanel viewResultsPanel(){

        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Username","Concept","Score","Total","Date"};

        DefaultTableModel model = new DefaultTableModel(columns,0);
        JTable table = new JTable(model);

        table.setFont(new Font("Segoe UI",Font.PLAIN,16));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,17));

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll,BorderLayout.CENTER);

        try(Connection con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
            Statement st = con.createStatement()){

            ResultSet rs = st.executeQuery("SELECT * FROM results");

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("concept"),
                        rs.getInt("score"),
                        rs.getInt("total"),
                        rs.getTimestamp("test_date")
                });
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return panel;
    }

    public static void main(String[] args){
        new AdminDashboard();
    }
}

