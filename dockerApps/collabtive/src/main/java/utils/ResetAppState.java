//package utils;
//
//public class ResetAppState {
//
//    public static void reset(){
//        int port = Integer.valueOf(Properties.getInstance().getProperty("db_port"));
//        resetDB("root","root","collabtive", port,
//                Properties.user_dir + "/src/main/resources/collabtive_db.sql");
//    }
//
//    private static void resetDB(String username, String password, String dbName, int port, String aSQLScriptFilePath){
//        SqlConnection sqlConnection = new SqlConnection();
//        sqlConnection.resetUsingSqlScript(username, password, dbName, port, aSQLScriptFilePath);
//    }
//}

package utils;

import java.util.Arrays;
import java.util.List;

public class ResetAppState {

	public static void reset() {
		int port = Integer.valueOf(Properties.getInstance().getProperty("db_port"));
		resetDB("root", "root", "collabtive", port);
	}

	private static void resetDB(String username, String password, String dbName, int port) {
		SqlConnection sqlConnection = new SqlConnection();
		List<String> tables = Arrays.asList("chat", "company", "company_assigned", "files", "files_attached", "log",
				"messages", "milestones", "milestones_assigned", "projectfolders", "projekte", "projekte_assigned",
				"roles", "roles_assigned", "settings", "tasklist", "tasks", "tasks_assigned", "timetracker", "user");
		sqlConnection.resetTables(username, password, port, dbName, tables);
		sqlConnection.resetTablesWithData(username, password, port, dbName);
	}
}
