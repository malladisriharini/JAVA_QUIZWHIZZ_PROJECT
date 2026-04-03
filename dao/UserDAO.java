package dao;

import db.DBConnection;
import java.sql.*;

public class UserDAO {

    // LOGIN
    public static boolean login(String username, String password) {

        if (username == null || password == null)
            return false;

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty())
            return false;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // REGISTER
    public static boolean register(String username, String password) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO users(username,password) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // username already exists
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== FORGOT PASSWORD / RESET PASSWORD =====
    public static boolean updatePassword(String username, String newPassword) {

        if (username == null || newPassword == null)
            return false;

        username = username.trim();
        newPassword = newPassword.trim();

        if (username.isEmpty() || newPassword.isEmpty())
            return false;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "UPDATE users SET password=? WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setString(2, username);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // TOTAL TESTS
    public static int getTotalTests(String username) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT COUNT(*) FROM results WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // TOTAL SCORE
    public static int getTotalScore(String username) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT COALESCE(SUM(score),0) FROM results WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // USER RANK
    public static int getUserRank(String username) {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                    "SELECT username, SUM(score) AS total_score " +
                    "FROM results GROUP BY username ORDER BY total_score DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int rank = 1;

            while (rs.next()) {
                if (rs.getString("username").equals(username)) {
                    return rank;
                }
                rank++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}