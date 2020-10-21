package model;

import java.io.FileWriter;
import java.io.IOException;

public class ProjectOwner {
	private String id;
	private String firstName;
	private String surname;
	private String role;
	private String email;
	private String company;

	public ProjectOwner(String id, String firstName, String surname, String role, String email, String company) {

		this.id = id;
		this.firstName = firstName;
		this.surname = surname;
		this.role = role;
		this.email = email;
		this.company = company;
	}

	public String toString() {

		return id + "; " + firstName + surname + "; " + role + "; " + email + "; " + company + ";";
	}

	public void writeToFile() throws IOException {

		FileWriter fw = null;
		try {
			String filename = "owners.txt";
			fw = new FileWriter(filename, true);
			fw.write(this.toString() + "\n");

		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		} finally {
			fw.close();
		}
	}

}
