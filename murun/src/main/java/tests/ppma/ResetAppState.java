package tests.ppma;

import utils.Properties;

public class ResetAppState {

    public static void reset(){
        int port = Integer.valueOf(Properties.getInstance().getProperty("db_port"));
        resetDB("root","root","ppma", port,
                Properties.user_dir + "/src/main/resources/ppma_db.sql");
    }

    private static void resetDB(String username, String password, String dbName, int port, String aSQLScriptFilePath){
        SqlConnection sqlConnection = new SqlConnection();
        sqlConnection.deleteAllTables(username, password, dbName, port);
        sqlConnection.resetUsingSqlScript(username, password, dbName, port, aSQLScriptFilePath);
    }
}
