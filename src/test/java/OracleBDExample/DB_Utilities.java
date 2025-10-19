package OracleBDExample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB_Utilities {
    static Connection conn;
    static ResultSet rs;
    static Statement stmt;

    /*
     * a static method to create connection
     * with valid url and username password
     */
    public static void createConnection() {
        String connectionStr = "jdbc:oracle:thin:@52.201.187.226:1521:XE";
        String username = "hr1";
        String password = "hr";

        try {
            conn = DriverManager.getConnection(connectionStr, username, password);
            System.out.println("CONNECTION SUCCESSFUL");
        } catch (SQLException e) {
            System.out.println("Connection has failed " + e.getMessage());
        }
    }


    /**
     * a static method to get the ResultSet object
     * with valid connection by executing query
     * @param query
     * @return ResultSet object that contain the result just in cases needed outside the class
     */
    public static ResultSet runQuery(String query) {
         rs = null;

        try {
                     stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                      rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("ERROR WHILE GETTING RESULTSET " + e.getMessage());
        }

        return rs;
    }
// close connection
    public static void destroy() {

        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get the row count of the resultSet
     * @return the row number of the resultSet given
     */
    public static int getRowCount() {

        int rowCount = 0;

        try {
            rs.last();
            rowCount = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {
            System.out.println("ERROR WHILE GETTING ROW COUNT " + e.getMessage());
        }

        return rowCount;
    }

    // a method to get the column count of the current ResultSet
    // getColumnCNT()

    public static int getColumnCNT() {
        int colCount = 0 ;

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();
        } catch (SQLException e) {
            System.out.println("ERROR WHILE COUNTING THE COLUMNS" + e.getMessage());
        }
        return colCount;
    }
//  retrieves the names of all columns from a ResultSet object and returns them as a list of strings.
    public static List<String> getColumnNames() {
        List<String> colNamesList = new ArrayList<>();

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int colNum = 1; colNum <= rsmd.getColumnCount(); colNum++) {
                String colName = rsmd.getColumnLabel(colNum);
                colNamesList.add(colName);
            }
        } catch (SQLException e) {
            System.out.println("ERROR WHILE GETTING COLUMN NAMES " + e.getMessage());
        }

        return colNamesList;
    }
//fetches the data for a specific row in the ResultSet and returns it as a list of strings
    public static List<String> getRowDataAsList(int rowNum) {
        List<String> rowDataList = new ArrayList<>();

        try {
            rs.absolute(rowNum);
            for (int colNum = 1; colNum <= getColumnCNT(); colNum++) {
                String cellValue = rs.getString(colNum);
                rowDataList.add(cellValue);
            }
        } catch (SQLException e) {
            System.out.println("ERROR WHILE getRowDataAsList " + e.getMessage());
        }

        return rowDataList;
    }

    public static void main(String[] args) throws SQLException {
        createConnection(); // Bağlantı metodunu çağır
        ResultSet myresult = runQuery("SELECT * FROM REGIONS");
        rs.next();
        System.out.println(rs.getString(1));
        System.out.println(getRowCount());
        System.out.println(getColumnCNT());
        System.out.println(getColumnNames());
        System.out.println(getRowDataAsList(2));


        // Metadata
        DatabaseMetaData dbmetadata = conn.getMetaData();
        System.out.println(dbmetadata.getUserName());
        System.out.println(dbmetadata.getConnection());
        System.out.println(dbmetadata.getDatabaseProductName());
        System.out.println(dbmetadata.getDriverName());

        // Resultset object metadata
        ResultSetMetaData rsmetedata = rs.getMetaData();
        System.out.println(rsmetedata.getColumnName(1));
        System.out.println(rsmetedata.getColumnCount());
        System.out.println(rsmetedata.getColumnLabel(1));
        
        destroy();

    }

}
