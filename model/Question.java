package model;

public class Question {

    private int id;
    private String concept;
    private String question;
    private String a,b,c,d;
    private String correct;

    public Question(int id, String concept, String question,
                    String a, String b, String c, String d, String correct) {
        this.id = id;
        this.concept = concept;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
    }

    public int getId() { return id; }
    public String getConcept() { return concept; }
    public String getQuestion() { return question; }
    public String getA() { return a; }
    public String getB() { return b; }
    public String getC() { return c; }
    public String getD() { return d; }
    public String getCorrect() { return correct; }
}