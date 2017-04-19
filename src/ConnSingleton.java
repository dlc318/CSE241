/**
 * Created by Dana on 4/19/2017.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Uses the singleton pattern to create a global connection.
 */
public class ConnSingleton {
    private static Connection conn = null;

    /**
     * getConn: Gets the connection
     * @return Connection - returns the global connection
     * @throws ClassNotFoundException
     */
    static Connection getConn() throws ClassNotFoundException {
        if (conn == null)
            initConn();
        return conn;
    }

    /**
     * initConn: used to establish the connection with SQL Developer
     * @throws java.lang.ClassNotFoundException
     */
    public static void initConn() throws java.lang.ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        // Get the user's Oracle signin information
        // This will be passed to the CarEnterprise task
        while(true) {
            System.out.println("enter Oracle user id:");
            String user_id = scan.nextLine();
            System.out.println("enter Oracle password for " + user_id + ":");
            String password = scan.nextLine();

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");

                conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241", user_id, password);
                break;
            } catch (SQLException e) {
                System.out.println("Incorrect username/password.");
            }
        }
    }
}

