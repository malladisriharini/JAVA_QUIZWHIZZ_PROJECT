package dao;

import db.DBConnection;
import java.sql.*;
import java.util.*;

public class ResultDAO {

    // ================= SAVE RESULT =================
    public static void saveResult(String username,
                                  String concept,
                                  int score,
                                  int total) {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "INSERT INTO results(username, concept, score, total) " +
                "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, concept);
            ps.setInt(3, score);
            ps.setInt(4, total);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LEADERBOARD =================
    public static List<String[]> getLeaderboardData() {

        List<String[]> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT username, SUM(score) AS total_score, COUNT(*) AS total_tests " +
                "FROM results " +
                "GROUP BY username " +
                "ORDER BY total_score DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String username = rs.getString("username");
                String score = rs.getString("total_score");
                String tests = rs.getString("total_tests");

                list.add(new String[]{username, score, tests});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= STATISTICS (FIXED METHOD) =================
    public static Map<String, Double> getConceptWisePercentage(String username) {

        Map<String, Double> map = new LinkedHashMap<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT concept, SUM(score) AS total_score, SUM(total) AS total_questions " +
                "FROM results " +
                "WHERE username = ? " +
                "GROUP BY concept";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String concept = rs.getString("concept");
                int totalScore = rs.getInt("total_score");
                int totalQuestions = rs.getInt("total_questions");

                double percentage = 0;

                if (totalQuestions > 0) {
                    percentage = ((double) totalScore / totalQuestions) * 100;
                }

                map.put(concept, percentage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}