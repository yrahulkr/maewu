package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlConnection {

    public void deleteAllTables(String username, String password, String dbName, int port){
        Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
        if(optionalConnection.isPresent()){
            Connection connection = optionalConnection.get();
            try(Statement statement = connection.createStatement()){
                ResultSet allTables = statement.executeQuery("SELECT TABLE_NAME \n" +
                        "FROM INFORMATION_SCHEMA.TABLES\n" +
                        "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='" + dbName + "'");
                List<String> tableNames = new ArrayList<>();
                while(allTables.next()){
                    String tableName = allTables.getString(1);
                    if(tableName != null && !tableName.isEmpty())
                        tableNames.add(tableName);
                    else
                        throw new RuntimeException("Table name not defined!");
                }
                for (String tableName : tableNames) {
                    statement.executeUpdate("DROP table " + tableName + ";");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            throw new IllegalStateException(this.getClass().getName() + ": connection failed");
        }
    }

    public void resetUsingSqlScript(String username, String password, String dbName, int port, String aSQLScriptFilePath){
        Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
        if(optionalConnection.isPresent()){
            Connection connection = optionalConnection.get();
            ScriptRunner scriptRunner = new ScriptRunner(connection, false);
            try {
                Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
                scriptRunner.runScript(reader);
                scriptRunner.closeConnection();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            throw new IllegalStateException(this.getClass().getName() + ": connection failed");
        }
    }

    private Optional<Connection> establishDBConnection(String username, String password, int port, String dbName){
        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // Setup the connection with the DB
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:"
                    + port + "/" + dbName, username, password);
            return Optional.of(connection);
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void close(Statement statement){
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

