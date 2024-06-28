package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    /**
     * Connect to a sample database
     */
    public Connection getConnection() {
        Connection connection = null;
        try {
            // db parameters
            DriverManager.registerDriver(new org.sqlite.JDBC());
            String url = "jdbc:sqlite:C:\\Users\\Oleg Kofevar\\IdeaProjects\\ru\\course\\currency-exchanger\\src\\main\\resources\\db\\currencyExchangerDB";
            // create a connection to the database
            connection = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*} finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
        return connection;
    }
}