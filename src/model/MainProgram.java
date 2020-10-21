package model;

import java.io.IOException;
import java.util.Scanner;

import exception.BadTeamException;

public class MainProgram {

	public static void main(String[] args) throws IOException, ClassNotFoundException, BadTeamException {

		MenuOption option = null;
		DataHandler dh = new DataHandler();
		Scanner input = new Scanner(System.in);


		do {System.out.println(	"*********Menu*********\n" +
								"1.Add Company\n" +
								"2.Add Project Owner\n" +
								"3.Add Project\n" +
								"4.Capture Student Personalities\n" +
								"5.Add Student Preferences\n" +
								"6.Shortlist Projects\n" +
								"7.Form Team\n" +
								"8.Display Team Fitness Metrics\n" +
								"9.Exit\n" +
								"**********************\n");

			boolean ifLoop = false;
			int inputOption = 0;


			do {

				try {System.out.println("Please enter options:");

					 inputOption = Integer.parseInt(input.next());

					 if (inputOption <= 9 && inputOption >= 1) {

						 option = MenuOption.values()[inputOption - 1];
						 ifLoop = false;
					 }
					 else {
						 ifLoop = true;
					 }

				}
				catch (Exception e) {
					ifLoop = true;
				}

			} while (ifLoop);

			ifLoop = false;

			switch (option) {
				case AddCompany:

					dh.addCompany();
					break;
				case AddProjectOwner:

					dh.addProjectOwner();
					break;
				case AddProject:

					dh.addProject();
					break;
				case CaptureStudentPersonalities:

					dh.captureStudentPersonalities();
					break;
				case AddStudentPreferences:

					dh.captureStudentPreferences();
					break;
				case ShortlistProjects:

					dh.shorlistingPrefereneces();
					break;

				case FormTeam:

					dh.formTeams();
					break;
				case DisplayTeamFitnessMetrics:

					dh.displayTeamFitnessMetrics();
					break;
				case Exit:

					System.out.println("Thank you!");
					break;

			}



		} while (option != MenuOption.Exit);

		input.close();


	}


}
