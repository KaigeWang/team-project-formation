package database;

import java.sql.*;

import java.util.Map;

import model.DataHandler;
import model.Project;
import model.Team;

public class Database {




	public static String createProjectTable(Map <String, Team> Shortlist){
		String url = "jdbc:sqlite:C://sqlite/APassignment.db";


		String dropAndCreateTable = "Drop table Projects;"
				+ "create table Projects("
				+ "PID char(8) primary key, "
				+ "Title varchar(50), "
				+ "Desc varchar(50), "
				+ "OwnerID char(8), "
				+ "Ranking char(20)"
				+ ");"
				+ "Drop table Students;"
				+ "create table Students("
				+ "SID char(8) primary key, "
				+ "Ranking char(20), "
				+ "Personality char(1), "
				+ "Conflict varchar(10), "
				+ "Reference char(50),"
				+ "PID char(8),"
				+ "foreign key(PID) references Projects(PID)"
				+ ");";

		String insertProjects = "";
		String insertStudents = "";

		for(String i : Shortlist.keySet()){

			Project p = Shortlist.get(i).getProject();

			String insert1 = "insert into Projects values"
					+ "('"
					+ p.getID()
					+ "','"
					+ p.getTitle()
					+ "','"
					+ p.getDesc()
					+ "','"
					+ p.getPoID()
					+ "','"
					+ p.getRanking()
					+ "');";

			insertProjects += insert1;

			for (String j: Shortlist.get(i).getStudentMap().keySet()){
				String insert2 = "insert into Students values('"
						+ Shortlist.get(i).getStudentMap().get(j).getID()
						+ "','"
						+ Shortlist.get(i).getStudentMap().get(j).getRanking()
						+ "','"
						+ Shortlist.get(i).getStudentMap().get(j).getPersonalityTypy()
						+ "','"
						+ Shortlist.get(i).getStudentMap().get(j).getConflict()
						+ "','"
						+ Shortlist.get(i).getStudentMap().get(j).getPreferences()
						+ "','"
						+ i
						+ "');";

				insertStudents += insert2;
			}

		}

		try(Connection con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement()) {
			stmt.executeUpdate(dropAndCreateTable);
			stmt.executeUpdate(insertProjects);
			stmt.executeUpdate(insertStudents);
			System.out.println("successful!");
			return "successful!";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();
		}

	}


	public static String query(String message){

		String url = "jdbc:sqlite:C://sqlite/APassignment.db";

		try(Connection con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement()) {
			ResultSet sr = stmt.executeQuery(message);


			ResultSetMetaData rsmd = sr.getMetaData();
			String result = "";

			for (int i = 0; i < rsmd.getColumnCount(); i++) {

				result += rsmd.getColumnName(i+1) + ";";

			}

			result += "\n";

			while (sr.next()) {
				for (int i = 0; i < rsmd.getColumnCount(); i++) {

					result +=  sr.getString(i+1) + ";";

				}

				result += "\n";

			}



			return result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();
		}

	}

	public static void query1(String message){

		String url = "jdbc:sqlite:C://sqlite/APassignment.db";

		try(Connection con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement()) {
			ResultSet sr = stmt.executeQuery(message);

			ResultSetMetaData rsmd = sr.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				System.out.printf("%-30s", rsmd.getColumnName(i+1));
			}

			System.out.println();

			while (sr.next()) {
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					System.out.printf("%-30s", sr.getString(i+1));
				}
				System.out.println();

			}
	} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}

}
