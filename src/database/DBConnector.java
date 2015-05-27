package database;

import database.exceptions.ConnectionFailure;

import java.sql.*;

/**
 * Created by Light on 16/05/15.
 */
public class DBConnector {
        private Connection connection;
        private String dbUser;
        private String dbPassword;


        /**
         * Constructor wihtout params
         */
        public DBConnector(){
            this.connection = null;
            this.dbUser = "root"; // Usuario por defecto.
            this.dbPassword = "toor"; // MySQL, not database password
        }

        public boolean connectDB() throws ConnectionFailure {
            boolean success = false;
            try
            {
                Class.forName("com.mysql.jdbc.Driver"); //The driver that let us connect
                this.connection = DriverManager.getConnection("jdbc:mysql://localhost/fpchess", this.dbUser, this.dbPassword);
                success = true;
            }
            catch(ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(SQLException e)
            {
                throw new ConnectionFailure("Unable to connect to the database", e.getCause());
            }

            return success;
        }

        public boolean disconnectDB() throws ConnectionFailure {
            boolean success = false;
            try
            {
                this.connection.close();
                success = true;
            }
            catch(SQLException e)
            {
                throw new ConnectionFailure("An error with the database connection occurred", e.getCause());

            }
            return success;
        }

        public ResultSet executeSQL(String query) {
            ResultSet st = null;
            try
            {
                if(this.connection==null)
                    this.connectDB();
                Statement statement = this.connection.createStatement();
                st = statement.executeQuery(query);
            }
            catch(ConnectionFailure e)
            {
                e.printStackTrace();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }

            return st;
        }

        public boolean executeUpdate(String query) {
            boolean success = false;
            try
            {
                if(this.connection==null)
                {
                    try
                    {
                        this.connectDB();
                    }
                    catch(ConnectionFailure e)
                    {
                        e.printStackTrace();
                    }

                }
                Statement statement = this.connection.createStatement();
                statement.executeUpdate(query);
                success = true;
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }

            return success;
        }

        public Connection getConnection()
        {
            return this.connection;
        }

        /*Main test to connect
        public static void main(String[] args)
        {
            DBConnector db = new DBConnector();
            try {
                db.connectDB();
            } catch (ConnectionFailure connectionFailure) {
                connectionFailure.printStackTrace();
            }
            System.out.println("Connection Success");
            try {
                db.disconnectDB();
            } catch (ConnectionFailure connectionFailure) {
                connectionFailure.printStackTrace();
            }
            System.out.println("Connection Closed");
        }*/
}
