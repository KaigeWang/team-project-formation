package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.experimental.theories.FromDataPoints;

import exception.BadTeamException;

public class DataHandler {

	private Map<String, Company> companies;
	private Map<String, ProjectOwner> projectOweners;
	private Map<String, Project> projects;
	private Map<String, Student> students;
	private Map<String, Integer> projectPreferences;
	private Map<String, Team> teams;
	private Set<String> studentPool;

	public DataHandler() throws IOException, ClassNotFoundException {
		companies = new HashMap<String, Company>();
		projectOweners = new HashMap<String, ProjectOwner>();
		students = new HashMap<String, Student>();
		projects = new HashMap<String, Project>();
		projectPreferences = new HashMap<String, Integer>();
		teams = new HashMap<String, Team>();
		new HashSet<String>();

		loadData();

		studentPool = new HashSet<String>();
		studentPool = students.keySet().stream().collect(Collectors.toSet());

		stack = new Stack<List<String>>();

	}

	@SuppressWarnings("unchecked")
	public void loadData() throws IOException, ClassNotFoundException {

		try {

			ObjectInputStream in = new ObjectInputStream(new FileInputStream("students.ser"));
			students = (Map<String, Student>) in.readObject();
			System.out.println("Students de-serialized!");

			ObjectInputStream in1 = new ObjectInputStream(new FileInputStream("projects.ser"));
			projects = (Map<String, Project>) in1.readObject();
			System.out.println("Projects de-serialized!");

			in.close();
			in1.close();

		} catch (Exception e) {
			readStudents();
			serializeStudents();
			readProjects();
			serializepProjects();
			System.err.println(e);

		} finally {

			writeStudentsToFile();
			writeToPreference();
		}

	}

	public void readProjects() throws IOException {

		Scanner scan = new Scanner(new File("projects.txt"));
		String title;
		String projectID;
		String desc;
		String poID;
		String ranking;

		while (scan.hasNext()) {

			List<String> data = Arrays.asList(scan.nextLine().trim().split(";"));

			title = data.get(0).trim();
			projectID = data.get(1).trim();
			desc = data.get(2).trim();
			poID = data.get(3).trim();
			ranking = data.get(4).trim();
			Project project = new Project(title, projectID, desc, poID, ranking);
			projects.put(projectID, project);

		}

		System.out.println("Projects loaded!");

	}

	public void readStudents() throws IOException {

		System.out.println("First time run the program.");
		Scanner scan = new Scanner(new File("students.txt"));

		String ranking = "";
		// load student information;
		while (scan.hasNextLine()) {

			StringTokenizer studentInfo = new StringTokenizer(scan.nextLine().trim());

			String studentID = studentInfo.nextToken();

			for (int i = 0; i < 8; i++) {

				String token = studentInfo.nextToken();

				ranking += token + " ";

			}

			students.put(studentID, new Student(studentID, ranking.trim()));

			ranking = "";
		}

		System.out.println("Students loaded!");

		scan.close();

	}

	public void readSelections() throws BadTeamException, IOException {

		Scanner scan = new Scanner(new File("preferences_shortlist.txt"));

		String projectID;
		while (scan.hasNext()) {

			List<String> data = Arrays.asList(scan.nextLine().trim().split(";"));

			projectID = data.get(1).trim();
			teams.put(projectID, new Team(projects.get(projectID)));
		}

		studentPool = students.keySet().stream().collect(Collectors.toSet());

		if (teams.isEmpty()) {

			writeToSelection();
			throw new BadTeamException("Not yet shortlist any projects!");

		} else {
			System.out.println("Shortlisted Projects loaded!");

		}

		Scanner scan1 = new Scanner(new File("selections.txt"));

		while (scan1.hasNextLine()) {
			String[] a = scan1.nextLine().split(";");
			String b = scan1.nextLine();
			String[] c = new String[4];
			if (b.equals("")) {
				continue;
			} else {
				c = b.split(";");
			}

			for (int i = 0; i < c.length; i++) {
				teams.get(a[1].trim()).addStudent(students.get(c[i].trim()));
				studentPool.remove(c[i].trim());
			}

		}

		System.out.println("Selection loaded!");
	}

	public Map<String, Project> getProject() {
		return projects;
	}

	public Map<String, Team> getShortlist() {
		return teams;
	}

	public Map<String, Student> getStudent() {
		return students;
	}

	// main functions;
	public void addCompany() throws IOException {

		Scanner scan = new Scanner(System.in);

		String companyID = "";
		String companyName = "";
		int ABN = 0;
		String URL = "";
		String address = "";

		Boolean ifLoop = false;

		// input companyID;
		do {
			System.out.println("Unique company ID: C");
			try {
				String input = scan.nextLine().trim();
				int id = Integer.parseInt(input);
				companyID = "C" + id;
				ifLoop = false;
			} catch (Exception e) {
				System.out.println("Please enter an interger number.");
				ifLoop = true;
			}

		} while (ifLoop);

		// input company name;
		System.out.println("Company name: ");
		companyName = scan.nextLine().trim();
		ifLoop = false;

		// input ABN number;
		do {
			System.out.println("ABN number: ");
			try {
				String input = scan.nextLine().trim();
				ABN = Integer.parseInt(input);
				ifLoop = false;
			} catch (Exception e) {
				System.out.println("Please enter an interger number.");
				ifLoop = true;
			}

		} while (ifLoop);

		// input URL;
		System.out.println("Company URL: ");
		URL = scan.nextLine().trim();

		// input company address;
		System.out.println("Company address: ");
		address = scan.nextLine().trim();

		Company company = new Company(companyID, companyName, ABN, URL, address);
		companies.put(companyName, company);
		company.writeToFile();

	}

	// add project owners
	public void addProjectOwner() throws IOException {

		String projectOwnerID = null;
		String firstName;
		String surname;
		String role;
		String email;
		String company;

		Scanner scan = new Scanner(System.in);

		Boolean ifLoop = false;

		// input project owner id;
		do {
			System.out.println("Unique project owner ID: Own");
			try {
				String input = scan.nextLine().trim();
				int id = Integer.parseInt(input);
				projectOwnerID = "Own" + id;
				ifLoop = false;
			} catch (Exception e) {
				System.out.println("Please enter an interger number.");
				ifLoop = true;
			}

		} while (ifLoop);

		System.out.println("First name: ");
		firstName = scan.nextLine().trim();

		System.out.println("Surname: ");
		surname = scan.nextLine().trim();

		System.out.println("Role: ");
		role = scan.nextLine().trim();

		System.out.println("Email: ");
		email = scan.nextLine().trim();

		System.out.println("company: ");
		company = scan.nextLine().trim();

		ProjectOwner projectOwner = new ProjectOwner(projectOwnerID, firstName, surname, role, email, company);
		projectOweners.put(projectOwnerID, projectOwner);
		projectOwner.writeToFile();

	}

	// add projects
	public void addProject() throws IOException {

		String title = "";
		String projectID = "";
		String desc = "";
		String poID = "";
		String ranking = "";

		Scanner scan = new Scanner(System.in);

		System.out.println("Title: ");
		title = scan.nextLine().trim();

		Boolean ifLoop = false;
		// input project id;
		do {
			System.out.println("Unique project ID: Pr");
			try {
				String input = scan.nextLine().trim();
				int id = Integer.parseInt(input);
				projectID = "Pr" + id;
				ifLoop = false;
			} catch (Exception e) {
				System.out.println("Please enter an interger number.");
				ifLoop = true;
			}

		} while (ifLoop);

		// input description;
		System.out.println("Brief description: ");
		desc = scan.nextLine().trim();

		// input project owner ID;
		do {
			System.out.println("Project Owner ID: Own");
			try {
				String input = scan.nextLine().trim();
				int id = Integer.parseInt(input);
				poID = "Own" + id;
				ifLoop = false;
			} catch (Exception e) {
				System.out.println("Please enter an interger number.");
				ifLoop = true;
			}

		} while (ifLoop);

		// input ranking;
		String[] categories = new String[4];
		categories[0] = "Programming & Software Engineering";
		categories[1] = "Networking and Security";
		categories[2] = "Analytics and Big Data";
		categories[3] = "Web & Mobile applications";

		String[] abb = { "P", "N", "A", "W" };

		System.out.println("Ranking: ");
		for (int i = 0; i < 4; i++) {
			System.out.println(categories[i] + ": " + abb[i]);

			do {
				try {
					String input = scan.nextLine().trim();
					int score = Integer.parseInt(input);
					if (score >= 1 && score <= 4) {
						ranking += abb[i] + " " + score + " ";
						ifLoop = false;
					} else {
						System.out.println("Please enter an interger number (1 to 4).");
						ifLoop = true;
					}

				} catch (Exception e) {
					System.out.println("Please enter an interger number (1 to 4).");
					ifLoop = true;
				}

			} while (ifLoop);

		}

		Project project = new Project(title, projectID, desc, poID, ranking);
		project.writeToFile();

		readProjects();

	}

	public void captureStudentPersonalities() throws IOException {

		Scanner scan = new Scanner(System.in);

		boolean ifLoop = false;

		String studentID;

		// update personality type;
		do {
			System.out.println("Which student do you want to update? (N to cancel)");
			printAvailableStudent();
			studentID = scan.nextLine().trim();

			// N to exit;
			if (studentID.equals("n") || studentID.equals("N")) {

				return;
			}
			// if the student exists;
			else if (students.get(studentID) != null) {

				break;

			}
			// if the student doesn't exist;
			else {
				System.out.println("Student " + studentID + " doesn't exist!");
				ifLoop = true;
			}

		} while (ifLoop);

		System.out.println("Matching student as follows: \n" + students.get(studentID) + "\nWhat is " + studentID
				+ "'s personality type: ");

		// set personality type;
		do {

			System.out.println(
					"A.Likes to be a Leader (Director)\n" + "B.Outgoing and maintains good relationships (Socializer)\n"
							+ "C.Detail oriented (Thinker)\n" + "D.Less assertive (Supporter)");
			String input1 = scan.nextLine().trim();

			if (input1.equals("A") || input1.equals("B") || input1.equals("C") || input1.equals("D")) {

				char personalityType = input1.charAt(0);
				// char personalityType = Character.toUpperCase(input2);
				students.get(studentID).setPersonalityType(personalityType);
				System.out.println("Personality type updated\n" + students.get(studentID));
				ifLoop = false;
			} else {
				System.out.println("Personality type " + input1 + " doesn't exist!");
				ifLoop = true;
			}

		} while (ifLoop);

		// update conflict;
		String conflict = "";
		for (int i = 0; i < 2; i++) {
			System.out.println("Enter students IDs that have potential conflict (N to skip): ");

			String a = scan.nextLine().trim();
			if (a.equals("n") || a.equals("N")) {

				break;
			}

			if (students.containsKey(a)) {
				conflict += a + " ";

			} else {
				System.out.println("Student doesn't exist!");
			}

		}

		students.get(studentID).setConflict(conflict.trim());
		System.out.println(students.get(studentID) + " updated!");

		serializeStudents();

	}

	public void captureStudentPreferences() throws IOException, ClassNotFoundException {

		readProjects();

		Scanner scan = new Scanner(System.in);

		boolean ifLoop = false;

		String studentID;

		// input student;
		do {
			System.out.println("Which student do you want to update? (N to cancel)");

			printAvailableStudent();
			studentID = scan.nextLine().trim();

			// N to exit;
			if (studentID.equals("n") || studentID.equals("N")) {

				return;
			}
			// if the student exists;
			else if (students.get(studentID) != null) {

				ifLoop = false;

			}
			// if the student doesn't exist;
			else {
				System.out.println("Student " + studentID + " doesn't exist!");
				ifLoop = true;
			}

		} while (ifLoop);

		System.out.println("Matching student as follows: \n" + students.get(studentID).toString());

		printAvailableProjects();

		// input preferences;

		String projectID;
		String preference = "";

		for (int i = 0; i < 4; i++) {

			System.out.println("Choose preference " + (4 - i) + " project ID:");
			projectID = scan.nextLine().trim();
			if (projects.get(projectID) != null) {
				preference += projectID + " " + (4 - i) + " ";
				System.out.println(preference);
			} else {
				System.out.println("Project ID " + projectID + " doesn't exist!");
				return;
			}

		}

		students.get(studentID).setPreferences(preference.trim());

		addUpStudentsPreferencesToProjects();

		serializeStudents();

		writeStudentsToFile();

		writeToPreference();

	}

	public void writeStudentsToFile() throws IOException {

		File file = new File("studentInfo.txt");

		BufferedWriter bf = null;

		try {
			bf = new BufferedWriter(new FileWriter(file));

			for (String i : students.keySet().stream()
					.sorted((a, b) -> Integer.parseInt(a.substring(1)) - Integer.parseInt(b.substring(1)))
					.collect(Collectors.toList())) {
				bf.write(students.get(i).toString() + "\n");
			}

			System.out.println("Successfully worte to the studentInfo.txt");

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			bf.close();
		}

	}

	public void writeToPreference() throws IOException {

		File file = new File("preferences.txt");

		BufferedWriter bf = null;

		try {
			bf = new BufferedWriter(new FileWriter(file));

			for (String i : students.keySet().stream()
					.sorted((a, b) -> Integer.parseInt(a.substring(1)) - Integer.parseInt(b.substring(1)))
					.collect(Collectors.toList())) {
				bf.write(i + "\n");
				bf.write(students.get(i).getPreferences() + "\n");
			}

			System.out.println("Successfully worte to the preferences.txt");

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			bf.close();
		}

	}

	public void serializepProjects() throws FileNotFoundException, IOException {

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("projects.ser"));

		out.writeObject(projects);

		System.out.println("Projects information serialized!");

		out.close();
	}

	public void serializeStudents() throws FileNotFoundException, IOException {

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("students.ser"));

		out.writeObject(students);

		System.out.println("Students information serialized!");

		out.close();
	}

	public void shorlistingPrefereneces() throws IOException, ClassNotFoundException {

		addUpStudentsPreferencesToProjects();

		System.out.println("The total preferences as follows: ");

		projectPreferences.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.forEach(System.out::println);

		System.out.println("Choose projects that you want to keep (N to stop)(X to cancel): ");

		Scanner scan = new Scanner(System.in);

		boolean ifLoop = true;
		String choice;
		String total = "";
		while (ifLoop) {
			choice = scan.nextLine().trim();

			if (choice.equals("x") || choice.equals("X")) {
				return;
			}


			if (choice.equals("n") || choice.equals("N")) {
				break;
			} else if (projectPreferences.get(choice) != null) {
				total += choice + " ";
				System.out.println(total + "selected.");
				continue;
			} else {
				System.out.println("Project doesn't exist!");
				continue;
			}

		}

		File file = new File("preferences_shortlist.txt");

		BufferedWriter bf = null;

		try {
			bf = new BufferedWriter(new FileWriter(file));

			System.out.println("Shortlisted projects: " + total);
			if (total.equals("")) {
				bf.write("");
				System.out.println("None.");
			} else {
				readProjects();
				StringTokenizer pr = new StringTokenizer(total);

				while (pr.hasMoreTokens()) {
					bf.write(projects.get(pr.nextToken()).toString());
				}
				System.out.println("Successfully worte to the preferences_shortlist.txt");
			}

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			bf.close();
		}

	}

	// print available students left
	public void printStudentPool() {

		System.out.println("Available students as follow: ");
		studentPool.stream().sorted((a, b) -> Integer.parseInt(a.substring(1)) - Integer.parseInt(b.substring(1)))
				.forEach(x -> {
					System.out.println(students.get(x));
				});

	}

	// Show up recommendations for console mode;
	public void printRecommedations(String projectID) {

		HashMap<String, Integer> ranking = new LinkedHashMap<String, Integer>();
		StringTokenizer st = new StringTokenizer(projects.get(projectID).getRanking());

		while (st.hasMoreTokens()) {
			ranking.put(st.nextToken(), Integer.parseInt(st.nextToken()));

		}

		Set<String> reco = new HashSet<String>();

		reco = studentPool.stream().filter(a -> students.get(a).getPreferences().contains(projectID))
				.collect(Collectors.toSet());

		for (String i : reco) {
			System.out.println("========================");
			System.out.println("Recommanded Student: " + i);

			StringTokenizer st1 = new StringTokenizer(students.get(i).getPreferences());

			while (st1.hasMoreTokens()) {
				if (st1.nextToken().equals(projectID)) {
					switch (Integer.parseInt(st1.nextToken())) {
					case 4:
						System.out.println("Preference level: 4");
						break;
					case 3:
						System.out.println("Preference level: 3");
						break;
					case 2:
						System.out.println("Preference level: 2");
						break;
					case 1:
						System.out.println("Preference level: 1");
						break;
					}
					break;
				}
			}

			HashMap<String, Integer> rankingS = new LinkedHashMap<String, Integer>();
			StringTokenizer st2 = new StringTokenizer(students.get(i).getRanking());

			while (st2.hasMoreTokens()) {

				rankingS.put(st2.nextToken(), Integer.parseInt(st2.nextToken()));

			}

			int m = 0;

			for (String j : rankingS.keySet()) {

				int n = Math.abs(ranking.get(j) - rankingS.get(j));
				System.out.println("Gap of " + j + " is |" + ranking.get(j) + " - " + rankingS.get(j) + "| = " + n);

				m += n;
			}

			System.out.println("Total gap is: " + m);

			System.out.println(students.get(i));

		}

		System.out.println("Recommanded students as follow: ");
		reco.forEach(x -> System.out.print(x + " "));
		System.out.println("\n");

	}

	// print out students in ascending order
	public void printAvailableStudent() {

		Stream.of(students.keySet().toArray()).sorted(
				(a, b) -> Integer.parseInt(((String) a).substring(1)) - Integer.parseInt(((String) b).substring(1)))
				.forEach(a -> System.out.println(students.get(a).toString()));

	}

	// print out projects in ascending order
	public void printAvailableProjects() {

		System.out.println("Available Projects as follows: ");

		Stream.of(projects.keySet().toArray()).sorted(
				(a, b) -> Integer.parseInt(((String) a).substring(2)) - Integer.parseInt(((String) b).substring(2)))
				.forEach(System.out::println);

	}

	public void formTeams() throws IOException, BadTeamException {

		try {
			readSelections();
			for (String i : teams.keySet()) {
				System.out.println(i + " ===> " + teams.get(i).toString());
			}

			Scanner scan = new Scanner(System.in);

			System.out.println("Please choose project to updata (N to cancel): ");

			String projectID = scan.nextLine().trim();

			if (projectID.equals("N") || projectID.equals("n")) {
				return;
			}



			if (teams.containsKey(projectID)) {

				System.out.println("Project " + projectID + " selected!");

				printRecommedations(projectID);

				System.out.println("Please select 4 students: ");

				for (int i = 0; i < 4; i++) {

					String studentID;
					boolean ifLoop = true;
					while (ifLoop) {
						printStudentPool();

						System.out.println("Add the " + (i + 1) + " student:");

						studentID = scan.nextLine().trim();

						if (!students.containsKey(studentID)) {
							ifLoop = true;
						} else if (studentPool.contains(studentID)) {
							teams.get(projectID).addStudent(students.get(studentID));
							studentPool.remove(studentID);
							System.out.println(studentID + " added!");
							ifLoop = false;
						} else {

							throw new BadTeamException("Student has been assigned!");
						}
					}
				}
				// Now team formed;
				teams.get(projectID).validationstudents();

			} else {
				System.out.println("Project doesn't on the shortlist.");
			}

			writeToSelection();
		} catch (BadTeamException e) {
			readSelections();
			System.err.println(e);
		}

	}

	public void writeToSelection() throws IOException {

		File file = new File("selections.txt");

		BufferedWriter bf = null;

		try {
			bf = new BufferedWriter(new FileWriter(file));

			for (String i : teams.keySet()) {
				bf.write(teams.get(i).toString());
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			bf.close();
		}
	}

	public void displayTeamFitnessMetrics() throws IOException, BadTeamException {

		try {
			Scanner scan = new Scanner(System.in);

			readSelections();

			if (studentPool.size() != 0) {
				throw new BadTeamException("Team not completely formed yet!");
			}

			List<Double> gapList = averageGapList();
			List<Double> totalList = averageComList();
			List<Double> percentList = averagePercentList();

			boolean ifLoop = false;
			int inputOption = 0;
			Metrics option = null;

			do {
				System.out
						.println("*********Menu*********\n" + "1.Individual Project Information\n" + "2.CompetencySD\n"
								+ "3.ShortfallSD\n" + "4.PercentageSD\n" + "5.Eixt\n" + "**********************\n");

				do {
					try {
						System.out.println("Please enter options:");

						inputOption = Integer.parseInt(scan.next());

						if (inputOption <= 5 && inputOption >= 1) {

							option = Metrics.values()[inputOption - 1];
							ifLoop = false;
						} else {
							ifLoop = true;
						}

					} catch (Exception e) {
						ifLoop = true;
					}

				} while (ifLoop);

				ifLoop = false;

				switch (option) {

				case individualProjectInformation:

					System.out.println("Available projects as follows: ");

					teams.keySet().forEach(System.out::println);

					System.out.println("Select project: ");
					String choice = scan.next().trim();

					if (!teams.containsKey(choice)) {
						throw new BadTeamException("Project doexn't exist or is not in the list!");
					} else {

						System.out
								.println("The team average competence is: " + teams.get(choice).getAverageCompetency());

						teams.get(choice).printComputation();
						System.out.println(
								"Pecentage of 1st and 2nd preference: " + teams.get(choice).getPercentage() + "%");
					}

					break;

				case competencySD:

					System.out.println("SD of average competency of teams: " + calculateSD(totalList));

					break;
				case percentageSD:
					System.out.println("SD of top percentage of teams: " + calculateSD(percentList) + "%");

					break;

				case shortfallSD:
					System.out.println("SD of shortfall of teams: " + calculateSD(gapList));

					break;

				case Eixt:

					break;

				}

			} while (option != Metrics.Eixt);

		} catch (BadTeamException e) {
			System.err.println(e);
		}

	}

	// sub-functions;

	public List<Double> averagePercentList() {

		List<Double> a = new ArrayList<Double>();

		for (String i : teams.keySet()) {
			a.add(teams.get(i).getPercentage());
		}

		return a;

	}

	public List<Double> averageComList() {

		List<Double> a = new ArrayList<Double>();

		for (String i : teams.keySet()) {
			a.add(teams.get(i).getAverageCompetency());
		}

		return a;

	}

	public List<Double> averageGapList() {

		List<Double> a = new ArrayList<Double>();

		for (String i : teams.keySet()) {
			a.add(teams.get(i).getShortfall());
		}

		return a;

	}

	public static double calculateSD(List<Double> a) {

		System.out.println(a);

		double sum = 0;
		double standardDeviation = 0;
		int length = a.size();

		for (double num : a) {
			sum += num;
		}

		double mean = sum / length;

		for (double num : a) {
			standardDeviation += Math.pow((num - mean), 2);
		}

		return Math.sqrt(standardDeviation / length);
	}

	public void addUpStudentsPreferencesToProjects() throws FileNotFoundException, IOException, ClassNotFoundException {

		// set the total project preference value to 0;
		Scanner scan = new Scanner(new File("projects.txt"));

		String projectID;

		while (scan.hasNext()) {

			List<String> data = Arrays.asList(scan.nextLine().trim().split(";"));

			projectID = data.get(1).trim();

			this.projectPreferences.put(projectID, 0);

		}

		System.out.println("Project preference loaded!");

		// add up students preferences to the project;
		for (String i : students.keySet()) {

			String preference = "";
			StringTokenizer st = new StringTokenizer(students.get(i).getPreferences());
			// projectPreferences is a map that stores a particular student's
			// project preferences
			LinkedHashMap<String, Integer> projectPreferences = new LinkedHashMap<String, Integer>();

			while (st.hasMoreTokens()) {
				projectPreferences.put(st.nextToken(), Integer.parseInt(st.nextToken()));
			}

			for (String j : projectPreferences.keySet()) {

				preference += j + " " + projectPreferences.get(j) + " ";
				int n = projectPreferences.get(j) + this.projectPreferences.get(j);
				this.projectPreferences.put(j, n);
			}

			StringTokenizer st1 = new StringTokenizer(preference);

			int m = st1.countTokens();
			if (m == 8 || m == 0) {
				students.get(i).setPreferences(preference.trim());

			} else {
				System.out.println("Invalid input: " + preference + " \nPlease add 4 idential references.");
				students.get(i).setPreferences("");
				// transfer the preferences to the total project preferences;
				for (String j : projectPreferences.keySet()) {

					this.projectPreferences.put(j, 0);
				}
			}
		}

	}

	//for GUI
	public boolean swapStudents(List<String> swapList) throws Exception {
		String student1 = swapList.get(0).trim();
		String student2 = swapList.get(1).trim();
		String project1 = null;
		String project2 = null;

		for (String i : teams.keySet()) {
			for (String j : teams.get(i).getStudentMap().keySet()) {
				if (j.equals(student1)) {
					project1 = i;

				}
				if (j.equals(student2)) {
					project2 = i;
				}

			}
		}

		if (project1.equals(project2)) {
			return false;
		}

		teams.get(project1).getStudentMap().remove(student1);
		teams.get(project1).getStudentMap().put(student2, students.get(student2));

		teams.get(project2).getStudentMap().remove(student2);
		teams.get(project2).getStudentMap().put(student1, students.get(student1));

		if (!teams.get(project1).validationstudentswithoutclear()) {
			readSelections();
			return false;
		}

		if (!teams.get(project2).validationstudentswithoutclear()) {
			readSelections();
			return false;
		}

		writeToSelection();
		stack.push(swapList);
		return true;

	}

	//for algorithm
	public boolean swapStudentsForAlgorithm(List<String> swapList) throws Exception {
		String student1 = swapList.get(0).trim();
		String student2 = swapList.get(1).trim();
		String project1 = null;
		String project2 = null;

		for (String i : teams.keySet()) {
			for (String j : teams.get(i).getStudentMap().keySet()) {
				if (j.equals(student1)) {
					project1 = i;

				}
				if (j.equals(student2)) {
					project2 = i;
				}

			}
		}

		if (project1.equals(project2)) {
			return false;
		}

		teams.get(project1).getStudentMap().remove(student1);
		teams.get(project1).getStudentMap().put(student2, students.get(student2));

		teams.get(project2).getStudentMap().remove(student2);
		teams.get(project2).getStudentMap().put(student1, students.get(student1));

		if (!teams.get(project1).validationstudentswithouterrorLog()) {
			readSelections();
			return false;
		}

		if (!teams.get(project2).validationstudentswithouterrorLog()) {
			readSelections();
			return false;
		}

		writeToSelection();
		return true;

	}

	private Stack<List<String>> stack;

	List<Double> sdOdSD() {

		List<Double> n = new ArrayList<Double>();

		n.add(calculateSD(this.averageComList()));
		n.add(calculateSD(this.averagePercentList()));
		n.add(calculateSD(this.averageGapList()));
		return n;

	}

	public String AutoBalance() throws Exception {

		List<String> swapList = new ArrayList<String>();

		String result = "";
		for (String i : teams.keySet()) {
			Set<String> a = teams.get(i).getStudentMap().keySet().stream().collect(Collectors.toSet());
			for (String j : a) {
				for (String l : students.keySet()) {
					if (!l.equals(j)) {
						if (swapList.size() == 2) {
							swapList.clear();
						}

						swapList.add(l);
						swapList.add(j);

						double SD = calculateSD(this.sdOdSD());

						boolean n = swapStudentsForAlgorithm(swapList);

						double SD1 = calculateSD(this.sdOdSD());

						if (n == true && SD1 < SD) {
							stack.push(swapList);
							result = " " + swapList.get(0) + " to " + swapList.get(1);

							return result;
						}

						if (n == true && SD1 >= SD) {
							swapStudentsForAlgorithm(swapList);
							result = "Already the best solution";
						}

						if (n == false) {
							result = "Already the best solution";
						}
					}
				}

			}

		}

		return result;
	}

	public String increasePriority() throws Exception {

		List<String> swapList = new ArrayList<String>();

		String result = "";
		for (String i : teams.keySet()) {
			Set<String> a = teams.get(i).getStudentMap().keySet().stream().collect(Collectors.toSet());
			for (String j : a) {
				for (String l : students.keySet()) {
					if (!l.equals(j)) {
						if (swapList.size() == 2) {
							swapList.clear();
						}

						swapList.add(l);
						swapList.add(j);

						double totalPriority = this.getTotalPriority();

						boolean n = swapStudentsForAlgorithm(swapList);

						double totalPriorityAfter = this.getTotalPriority();

						if (n == true && totalPriority < totalPriorityAfter) {
							stack.push(swapList);
							result = " " + swapList.get(0) + " to " + swapList.get(1) + "\n Priority rise from "
									+ String.format("%.4f", totalPriority) + " to "
									+ String.format("%.4f", totalPriorityAfter);

							return result;
						}

						if (n == true && totalPriority >= totalPriorityAfter) {
							swapStudentsForAlgorithm(swapList);
							result = "Already the best solution";
						}

						if (n == false) {
							result = "Already the best solution";
						}
					}
				}

			}

		}

		return result;
	}

	public String balancePriority() throws Exception {

		List<String> swapList = new ArrayList<String>();

		String result = "";
		for (String i : teams.keySet()) {
			Set<String> a = teams.get(i).getStudentMap().keySet().stream().collect(Collectors.toSet());
			for (String j : a) {
				for (String l : students.keySet()) {
					if (!l.equals(j)) {
						if (swapList.size() == 2) {
							swapList.clear();
						}

						swapList.add(l);
						swapList.add(j);

						double SD = calculateSD(this.averagePercentList());

						boolean n = swapStudentsForAlgorithm(swapList);

						double SD1 = calculateSD(this.averagePercentList());

						if (n == true && SD1 < SD) {
							stack.push(swapList);
							result = " " + swapList.get(0) + " to " + swapList.get(1) + "\n SD drop from "
									+ String.format("%.4f", SD) + " to " + String.format("%.4f", SD1);

							return result;
						}

						if (n == true && SD1 >= SD) {
							swapStudentsForAlgorithm(swapList);
							result = "Already the best solution";
						}

						if (n == false) {
							result = "Already the best solution";
						}
					}
				}

			}

		}

		return result;
	}

	public String balanceGap() throws Exception {

		List<String> swapList = new ArrayList<String>();

		String result = "";
		for (String i : teams.keySet()) {
			Set<String> a = teams.get(i).getStudentMap().keySet().stream().collect(Collectors.toSet());
			for (String j : a) {
				for (String l : students.keySet()) {
					if (!l.equals(j)) {
						if (swapList.size() == 2) {
							swapList.clear();
						}

						swapList.add(l);
						swapList.add(j);

						double SD = calculateSD(this.averageGapList());

						boolean n = swapStudentsForAlgorithm(swapList);

						double SD1 = calculateSD(this.averageGapList());

						if (n == true && SD1 < SD) {
							stack.push(swapList);
							result = " " + swapList.get(0) + " to " + swapList.get(1) + "\n SD drop from "
									+ String.format("%.4f", SD) + " to " + String.format("%.4f", SD1);

							return result;
						}

						if (n == true && SD1 >= SD) {
							swapStudentsForAlgorithm(swapList);
							result = "Already the best solution";
						}

						if (n == false) {
							result = "Already the best solution";
						}
					}
				}

			}

		}

		return result;
	}

	public double getTotalPriority() {

		double n = 0;
		for (String i : teams.keySet()) {
			n += teams.get(i).getPercentage();
		}

		return n;

	}

	public String balanceCompetency() throws Exception {

		// get the least competence project and the first one;
		Map<String, Double> dataCollection = new HashMap<String, Double>();

		teams.keySet().forEach(k -> {
			double n = teams.get(k).getAverageCompetency();
			dataCollection.put(k, n);
		});
		// sort the map by ascending order;
		Map<String, Double> sortedDataCollection = dataCollection.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey,
						Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		Team leastpr = teams.get(sortedDataCollection.keySet().toArray()[0]);

		Team firstpr = teams.get(sortedDataCollection.keySet().toArray()[4]);

		List<String> swapList = new ArrayList<String>();
		Set<String> a = new HashSet<String>();
		leastpr.getStudentMap().keySet().forEach(e -> {
			a.add(e);
		});

		Set<String> b = new HashSet<String>();
		firstpr.getStudentMap().keySet().forEach(e -> {
			b.add(e);
		});

		String result = "";
		for (String i : a) {
			for (String j : b) {

				if (swapList.size() == 2) {
					swapList.clear();
				}

				swapList.add(i);
				swapList.add(j);
				double SD = calculateSD(this.averageComList());
				boolean n = swapStudentsForAlgorithm(swapList);
				double SD1 = calculateSD(this.averageComList());
				if (n == true && SD1 < SD) {
					stack.push(swapList);
					result = " " + swapList.get(0) + " to " + swapList.get(1) + "\n SD drop from "
							+ String.format("%.4f", SD) + " to " + String.format("%.4f", SD1);
					return result;
				}

				if (n == true && SD1 >= SD) {
					swapStudentsForAlgorithm(swapList);
					result = "Already the best solution";
				}

				if (n == false) {
					result = "Already the best solution";
				}
			}
		}

		return result;

	}

	public String getHistory() {
		List<List<String>> history = stack.getHistory();
		if (history.size() == 0) {
			return "";
		}

		String result = "";
		for (List<String> i : history) {

			result += " " + i.get(0) + " to " + i.get(1) + "\n";
		}

		return result;

	}

	public String undo() throws Exception {
		List<String> pop = stack.pop();
		if (pop == null) {
			return " ";
		}
		swapStudentsForAlgorithm(pop);
		return "Discard " + pop.get(0) + " to " + pop.get(1) + "\n";
	}

}
