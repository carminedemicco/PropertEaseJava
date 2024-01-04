package main.propertease;

import java.sql.Connection;
import java.sql.DriverManager;


/* SINGLETON */
// Contiene l'istanza di connessione al database
public class DBConnection {
    private static Connection connection = null;

    private DBConnection(){}

    public static Connection getDBConnection()throws Exception{
        if (connection == null){
            connection = DriverManager.getConnection("jdbc:sqlite:properteasedb.sqlite");
        }
        return connection;
    }
}
