package OracleBDExample;

import java.sql.*;

public class WithAutoClosing {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@54.236.150.168:1521:XE";
        String username = "hr";
        String password = "hr";

        // try-with-resources: Otomatik kapatma için
        try (Connection con = DriverManager.getConnection(url, username, password);
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT * FROM REGIONS")) {

            rs.next(); // İlk satıra geç
            System.out.println(rs.getString(2)); // 2. kolonu yazdır (örnek: region_name)

        } catch (SQLException e) {
            System.out.println("Error occurred");
            e.printStackTrace();
        }
    }

}
