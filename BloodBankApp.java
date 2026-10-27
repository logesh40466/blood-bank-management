import java.sql.*;
import java.util.Scanner;

public class BloodBankApp {
    // XAMPP default settings: user is "root" and password is empty ""
    static final String DB_URL = "jdbc:mysql://localhost:3306/blood_bank";
    static final String USER = "root";
    static final String PASS = ""; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            while (true) {
                System.out.println("\n=================================");
                System.out.println("  BLOOD BANK MANAGEMENT SYSTEM   ");
                System.out.println("=================================");
                System.out.println("1. Add Donor");
                System.out.println("2. View All Donors");
                System.out.println("3. Search Blood Group");
                System.out.println("4. Exit");
                System.out.print("Enter choice (1-4): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // clear input buffer

                if (choice == 1) {
                    System.out.print("Enter Donor Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Blood Group (A+, B+, O+, AB+, etc.): ");
                    String bg = scanner.nextLine().toUpperCase();
                    System.out.print("Enter Blood Units (Count): ");
                    int units = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String phone = scanner.nextLine();

                    String sql = "INSERT INTO donors (name, blood_group, units, phone) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, bg);
                    pstmt.setInt(3, units);
                    pstmt.setString(4, phone);
                    pstmt.executeUpdate();
                    System.out.println(">> Donor Added Successfully!");

                } else if (choice == 2) {
                    String sql = "SELECT * FROM donors";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);

                    System.out.println("\n------------------------------------------------------------");
                    System.out.printf("%-5s %-15s %-12s %-8s %-15s\n", "ID", "Name", "Blood Group", "Units", "Phone");
                    System.out.println("------------------------------------------------------------");
                    boolean hasData = false;
                    while (rs.next()) {
                        hasData = true;
                        System.out.printf("%-5d %-15s %-12s %-8d %-15s\n",
                                rs.getInt("id"), rs.getString("name"),
                                rs.getString("blood_group"), rs.getInt("units"),
                                rs.getString("phone"));
                    }
                    if (!hasData) {
                        System.out.println("No records found in blood bank.");
                    }
                    System.out.println("------------------------------------------------------------");

                } else if (choice == 3) {
                    System.out.print("Enter Blood Group to search: ");
                    String searchBg = scanner.nextLine().toUpperCase();

                    String sql = "SELECT * FROM donors WHERE blood_group = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, searchBg);
                    ResultSet rs = pstmt.executeQuery();

                    System.out.println("\n--- Matching Donors ---");
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("Name: " + rs.getString("name") + " | Units: " + rs.getInt("units") + " | Phone: " + rs.getString("phone"));
                    }
                    if (!found) {
                        System.out.println(">> No donors found for blood group " + searchBg);
                    }

                } else if (choice == 4) {
                    System.out.println("Exiting System... Thank you!");
                    conn.close();
                    break;
                } else {
                    System.out.println("Invalid choice, please enter 1, 2, 3, or 4.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}