package dao;

import db.DBConnection;
import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public static List<Question> getQuestionsByConcept(String concept) {

        List<Question> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM questions WHERE concept = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, concept);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("concept"),
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct")
                );

                list.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}