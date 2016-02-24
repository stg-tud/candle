import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;
import questionnaire.data.PlayerStatus;
import questionnaire.data.Study;
import questionnaire.data.Task;

/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */

public class populate_database {
    private static IOHelper ioHelper = new IOHelper(new File("data"), ObjectMapperSetup.getObjectMapper());

	static Connection con = null;
	static Statement st = null;
	static PreparedStatement stmt = null;
	static ResultSet rs = null;
    
    
	public static void main(String[] args) {

		try {

			opendDb();
			createEventsTable();
			createStudyTables();
			createTasksTables();
			createUsersTable();
			
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
			}
		}

	} 
    
    
	static void old() {

		for (Event e : ioHelper.readLogs()) {
			Task task = ioHelper.readTask(e.taskId);

			if (EventType.SUBMISSION.equals(e.type)) {
				String givenAnswer = e.answer;
				String correctAnswer = task.answer;
			}
			// System.out.printf("%s -- %8s : %-12s @ %-18s -- %s\n", e.time,
			// e.auth, e.type, e.taskId, e.answer);
		}

	}
    
	static void opendDb() throws SQLException {
		
		String url = "jdbc:mysql://localhost:3306/empiricalstudydb2";
		String user = "emp-study-usr";
		String password = "d4t43xp3rt";

		con = DriverManager.getConnection(url, user, password);
		st = con.createStatement();
		rs = st.executeQuery("SELECT VERSION()");

		// Test it works
		if (rs.next()) {
			System.out.println(rs.getString(1));
		}

		// Cleanup if table already filled
		PreparedStatement cleanup;
		cleanup = con.prepareStatement("DELETE FROM events");
		cleanup.executeUpdate();

		List<Event> events = ioHelper.readLogs();

	}

	/**
	 * The tables that overall model the study. 
	 * Even if we don't have a "study" table for now.
	 * @throws SQLException
	 */
	static void createStudyTables() throws SQLException {

		Study study = ioHelper.readStudy("REScala");
		Set<String> introTasks = new HashSet<String>(study.getIntroTasks());
		Set<String> ooTasks = new HashSet<String>(study.getOopTasks());
		Set<String> rpTasks = new HashSet<String>(study.getRpTasks());

		
		stmt  = con.prepareStatement("DROP TABLE IF EXISTS tests");
		stmt.executeUpdate();
		stmt = con.prepareStatement("CREATE TABLE tests (name char(20), type char(20))");
	    stmt.executeUpdate();
		
		for (String t : ooTasks) {
			stmt = con.prepareStatement("INSERT INTO tests "
					+ "VALUES (?, ?)");
			stmt.setString(1, t);
			stmt.setString(2, "OO");
			stmt.executeUpdate();
		}
		for (String t : rpTasks) {
			stmt = con.prepareStatement("INSERT INTO tests "
					+ "VALUES (?, ?)");
			stmt.setString(1, t);
			stmt.setString(2, "RP");
			stmt.executeUpdate();
		}
		
		stmt  = con.prepareStatement("DROP TABLE IF EXISTS benchmarks");
		stmt.executeUpdate();
		stmt = con.prepareStatement("CREATE TABLE benchmarks (name char(20))");
	    stmt.executeUpdate();
		
		for (String t : introTasks) {
			stmt = con.prepareStatement("INSERT INTO benchmarks "
					+ "VALUES (?)");
			stmt.setString(1, t);
			//stmt.setString(2, "RP");
			stmt.executeUpdate();
		}

	}
	
	
	static void createTasksTables() throws SQLException {
		
		

		List<Task> tasks = ioHelper.readTasks();

		stmt  = con.prepareStatement("DROP TABLE IF EXISTS tasks");
		stmt.executeUpdate();
		stmt = con.prepareStatement("CREATE TABLE tasks "
				+ "(id char(20), "
				+ "title char(50), "
				+ "description char(50), "
				+ "code char(20), "
				+ "question char(255), "
				+ "answer text, "
				+ "questionTime long)");
	    stmt.executeUpdate();
		
		for (Task t : tasks) {
			stmt = con.prepareStatement("INSERT INTO tasks "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, t.id);
			stmt.setString(2, t.title);
			stmt.setString(3, t.description);
			stmt.setString(4, t.code);
			stmt.setString(5, t.question);
			stmt.setString(6, t.answer);
			stmt.setLong(7, t.taskType.questionTime);
			stmt.executeUpdate();
		}
		
		
	}
	

	static void createEventsTable() throws SQLException {

		List<Event> events = ioHelper.readLogs();

		stmt  = con.prepareStatement("DROP TABLE IF EXISTS events");
		stmt.executeUpdate();
		stmt = con.prepareStatement("CREATE TABLE events ("
				+ "type char(20), "
				+ "auth char(20), "
				+ "taskId char(20), "
				+ "answer text, "
				+ "time long);");
	    stmt.executeUpdate();
		
		for (Event e : events) {
			stmt = con.prepareStatement("INSERT INTO events "
					+ "VALUES (?, ?, ?, ?, ?)");
			stmt.setString(1, e.type.toString());
			stmt.setString(2, e.auth);
			stmt.setString(3, e.taskId);
			stmt.setString(4, e.answer);
			stmt.setLong(5, e.time.getTime());
			stmt.executeUpdate();

		}

	}
	
	
	static void createUsersTable() throws SQLException {

		// Map<String,PlayerStatus> events = ioHelper.readPlayers();

		/*
		stmt  = con.prepareStatement("DROP TABLE IF EXISTS events");
		stmt.executeUpdate();
		stmt = con.prepareStatement("CREATE TABLE events ("
				+ "type char(20), "
				+ "auth char(20), "
				+ "taskId char(20), "
				+ "answer char(255), "
				+ "time long);");
	    stmt.executeUpdate();
		
		for (Event e : events) {
			stmt = con.prepareStatement("INSERT INTO events "
					+ "VALUES (?, ?, ?, ?, ?)");
			stmt.setString(1, e.type.toString());
			stmt.setString(2, e.auth);
			stmt.setString(3, e.taskId);
			stmt.setString(4, e.answer);
			stmt.setLong(5, e.time.getTime());
			stmt.executeUpdate();

		}
*/
	}

}









