package mypackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {
    private Connection con;
    private Statement st;

    public Conn() {
        String url = "jdbc:mysql://localhost:3306/BDTest";
        String user = "root";
        String password = "";
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getConn() {
        return st;
    }

    public Connection getConnection() {
        return con;
    }
}
