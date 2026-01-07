import java.sql.*;
import java.util.Scanner;

public class CareerConsoleApp {

    static final String DB_URL = "jdbc:sqlite:career.db";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        createTable();

        System.out.println("\n=== AI-Based Career Recommendation System ===");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        int tech = 0, creative = 0, social = 0;

        System.out.println("\nQ1: What do you enjoy most?");
        System.out.println("1. Coding / Logical work");
        System.out.println("2. Designing / Creative work");
        System.out.println("3. Helping people");
        int q1 = sc.nextInt();

        if (q1 == 1) tech += 2;
        else if (q1 == 2) creative += 2;
        else social += 2;

        System.out.println("\nQ2: What type of problem do you like?");
        System.out.println("1. Logical problems");
        System.out.println("2. Creative problems");
        System.out.println("3. People-related problems");
        int q2 = sc.nextInt();

        if (q2 == 1) tech += 2;
        else if (q2 == 2) creative += 2;
        else social += 2;

        System.out.println("\nQ3: Preferred work environment?");
        System.out.println("1. Computer-based");
        System.out.println("2. Design tools");
        System.out.println("3. Team interaction");
        int q3 = sc.nextInt();

        if (q3 == 1) tech += 1;
        else if (q3 == 2) creative += 1;
        else social += 1;

        String career;
        String description;

        if (tech >= creative && tech >= social) {
            career = "Software Engineer";
            description =
                    "You have strong logical thinking and problem-solving skills.\n" +
                    "Suitable for programming, development, and technical roles.";
        } else if (creative >= tech && creative >= social) {
            career = "UI/UX Designer";
            description =
                    "You are creative and imaginative.\n" +
                    "Suitable for design, user experience, and creative fields.";
        } else {
            career = "HR / Management Professional";
            description =
                    "You have good communication and people skills.\n" +
                    "Suitable for leadership, coordination, and management roles.";
        }

        System.out.println("\n=== RECOMMENDED CAREER ===");
        System.out.println("Career Name : " + career);
        System.out.println("Career Info : " + description);

        saveResult(name, career);

        System.out.println("\n=== PREVIOUS RESULTS ===");
        showHistory();

        sc.close();
    }

    static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql =
                    "CREATE TABLE IF NOT EXISTS results (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "career TEXT)";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("Database creation error");
        }
    }

    static void saveResult(String name, String career) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO results(name, career) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, career);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error saving result");
        }
    }

    static void showHistory() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT name, career FROM results";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(
                        rs.getString("name") +
                        " => " +
                        rs.getString("career")
                );
            }
        } catch (Exception e) {
            System.out.println("Error reading history");
        }
    }
}
