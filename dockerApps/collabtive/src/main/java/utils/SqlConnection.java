//package utils;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.Reader;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class SqlConnection {
//
//    public void deleteAllTables(String username, String password, String dbName, int port){
//        Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
//        if(optionalConnection.isPresent()){
//            Connection connection = optionalConnection.get();
//            try(Statement statement = connection.createStatement()){
//                ResultSet allTables = statement.executeQuery("SELECT TABLE_NAME \n" +
//                        "FROM INFORMATION_SCHEMA.TABLES\n" +
//                        "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='" + dbName + "'");
//                List<String> tableNames = new ArrayList<>();
//                while(allTables.next()){
//                    String tableName = allTables.getString(1);
//                    if(tableName != null && !tableName.isEmpty())
//                        tableNames.add(tableName);
//                    else
//                        throw new RuntimeException("Table name not defined!");
//                }
//                for (String tableName : tableNames) {
//                    statement.executeUpdate("DROP table " + tableName + ";");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }else{
//            throw new IllegalStateException(this.getClass().getName() + ": connection failed");
//        }
//    }
//
//    public void resetUsingSqlScript(String username, String password, String dbName, int port, String aSQLScriptFilePath){
//        Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
//        if(optionalConnection.isPresent()){
//            Connection connection = optionalConnection.get();
//            ScriptRunner scriptRunner = new ScriptRunner(connection, false);
//            try {
//                Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
//                scriptRunner.setStopOnError(true);
//                scriptRunner.runScript(reader);
//                scriptRunner.closeConnection();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }else{
//            throw new IllegalStateException(this.getClass().getName() + ": connection failed");
//        }
//    }
//
//    private Optional<Connection> establishDBConnection(String username, String password, int port, String dbName){
//        // This will load the MySQL driver, each DB has its own driver
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//            // Setup the connection with the DB
//            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:"
//                    + port + "/" + dbName, username, password);
//            return Optional.of(connection);
//        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }
//
//    private void close(Statement statement){
//        try {
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
//

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

	public void deleteAllTables(String username, String password, String dbName, int port) {
		Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
		if (optionalConnection.isPresent()) {
			Connection connection = optionalConnection.get();
			try (Statement statement = connection.createStatement()) {
				ResultSet allTables = statement.executeQuery("SELECT TABLE_NAME \n" + "FROM INFORMATION_SCHEMA.TABLES\n"
						+ "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='" + dbName + "'");
				List<String> tableNames = new ArrayList<>();
				while (allTables.next()) {
					String tableName = allTables.getString(1);
					if (tableName != null && !tableName.isEmpty())
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
		} else {
			throw new IllegalStateException(this.getClass().getName() + ": connection failed");
		}
	}

	public void resetUsingSqlScript(String username, String password, String dbName, int port,
			String aSQLScriptFilePath) {
		Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
		if (optionalConnection.isPresent()) {
			Connection connection = optionalConnection.get();
			ScriptRunner scriptRunner = new ScriptRunner(connection, false);
			try {
				Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
				scriptRunner.runScript(reader);
				scriptRunner.closeConnection();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException(this.getClass().getName() + ": connection failed");
		}
	}

	private Optional<Connection> establishDBConnection(String username, String password, int port, String dbName) {
		// This will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// Setup the connection with the DB
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:" + port + "/" + dbName,
					username, password);
			return Optional.of(connection);
		} catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public void resetTables(String username, String password, int port, String dbName, List<String> tables) {
		Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
		if (optionalConnection.isPresent()) {
			try {
				Connection connection = optionalConnection.get();
				Statement statement = connection.createStatement();
				tables.forEach(table -> {
					try {
						statement.executeUpdate("set foreign_key_checks=0;");
						statement.executeUpdate("truncate table " + table);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
				this.close(statement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException(this.getClass().getName() + ": connection failed");
		}

	}

	public void resetTablesWithData(String username, String password, int port, String dbName) {
		Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
		if (optionalConnection.isPresent()) {
			try {
				Connection connection = optionalConnection.get();
				Statement statement = connection.createStatement();
				// roles
				statement.executeUpdate("set foreign_key_checks=0;");
				statement.executeUpdate("truncate table roles;");
				statement.executeUpdate(
						"INSERT INTO `roles` (`ID`, `name`, `projects`, `tasks`, `milestones`, `messages`, `files`, `chat`, `timetracker`, `admin`)\n"
								+ "VALUES\n"
								+ "\t(1,'Admin','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:4:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"read\\\";i:1;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:1;}'),\n"
								+ "\t(2,'User','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:4:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"read\\\";i:0;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:0;}'),\n"
								+ "\t(3,'Client','a:5:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;s:4:\\\"view\\\";i:0;}','a:5:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;s:4:\\\"view\\\";i:0;}','a:5:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;s:4:\\\"view\\\";i:0;}','a:4:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:0;}','a:5:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:4:\\\"read\\\";i:0;s:4:\\\"view\\\";i:0;}','a:1:{s:3:\\\"add\\\";i:0;}');");
				// roles_assigned
				statement.executeUpdate("set foreign_key_checks=0;");
				statement.executeUpdate("truncate table roles_assigned;");
				statement.executeUpdate(
						"INSERT INTO `roles_assigned` (`ID`, `user`, `role`)\n" + "VALUES\n" + "\t(1,1,1);");
				// user
				statement.executeUpdate("set foreign_key_checks=0;");
				statement.executeUpdate("truncate table user;");
				statement.executeUpdate(
						"INSERT INTO `user` (`ID`, `name`, `email`, `tel1`, `tel2`, `pass`, `company`, `lastlogin`, `zip`, `gender`, `url`, `adress`, `adress2`, `state`, `country`, `tags`, `locale`, `avatar`, `rate`)\n"
								+ "VALUES\n"
								+ "	(1,'admin','',NULL,NULL,'d033e22ae348aeb5660fc2140aec35850c4da997','0','1545724500',NULL,'','','','','','','','','','0');");
				// settings
				statement.executeUpdate("set foreign_key_checks=0;");
				statement.executeUpdate("truncate table settings;");
				statement.executeUpdate("INSERT INTO `settings` (`ID`, `settingsKey`, `settingsValue`)\n" + "VALUES\n"
						+ "\t(1,'name','Collabtive'),\n" + "\t(2,'subtitle','Projectmanagement'),\n"
						+ "\t(3,'locale','en'),\n" + "\t(4,'timezone','Europe/Rome'),\n"
						+ "\t(5,'dateformat','d.m.Y'),\n" + "\t(6,'template','standard'),\n"
						+ "\t(7,'mailnotify','1'),\n" + "\t(8,'mailfrom','collabtive@localhost'),\n"
						+ "\t(9,'mailfromname',''),\n" + "\t(10,'mailmethod','mail'),\n" + "\t(11,'mailhost',''),\n"
						+ "\t(12,'mailuser',''),\n" + "\t(13,'mailpass',''),\n" + "\t(14,'rssuser',''),\n"
						+ "\t(15,'rsspass',''),\n" + "\t(16,'theme','standard'),\n"
						+ "\t(17,'filePass','kVHI9oZkP2N8rJq3');");

				this.close(statement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException(this.getClass().getName() + ": connection failed");
		}
	}

	private void close(Statement statement) {
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
