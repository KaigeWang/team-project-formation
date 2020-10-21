package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import exception.BadTeamException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Team {

	private Map<String, Student> studentMap;

	public Map<String, Student> getStudentMap() {
		return studentMap;
	}

	private Project project;

	public Project getProject() {
		return project;
	}

	private Map<String, Double> projectCompetency;

	public Team(Project project) {
		studentMap = new HashMap<String, Student>();
		this.project = project;
		projectCompetency = new LinkedHashMap<String, Double>();
		projectCompetency();
	}

	public void addStudent(Student student) {
		studentMap.put(student.getID(), student);
	}

	public void clear() {
		studentMap.clear();
	}

	public int getSize() {
		return studentMap.size();
	}

	public String toString() {

		String a = "";
		for (String i : studentMap.keySet()) {
			a += i + "; ";
		}

		return project.toString() + a.trim() + "\n";
	}


	// if the team has conflict clear the team
	public void validationstudents() throws BadTeamException {

		Set<Character> personalityType = new HashSet<Character>();

		for (String i : studentMap.keySet()) {
			personalityType.add(studentMap.get(i).getPersonalityTypy());

		}

		if (personalityType.size() < 3) {
			studentMap.clear();

			throw new BadTeamException("Need at least 3 kinds of personality typy! \nForming team failed!");
		}

		if (!personalityType.contains('A')) {
			studentMap.clear();

			throw new BadTeamException("Need at least one A personality type of student! \nForming team failed!");
		}

		for (String i : studentMap.keySet()) {
			StringTokenizer st = new StringTokenizer(studentMap.get(i).getConflict().trim());
			while (st.hasMoreTokens()) {
				if (studentMap.keySet().contains(st.nextToken().trim())) {
					studentMap.clear();
					throw new BadTeamException("Confilicts crashed! \nForming team failed!");
				}
			}
		}

	}

	// if the team has conflict pop up error dialog
	public boolean validationstudentswithoutclear() {
		try {
			Set<Character> personalityType = new HashSet<Character>();

			for (String i : studentMap.keySet()) {
				personalityType.add(studentMap.get(i).getPersonalityTypy());

			}

			if (personalityType.size() < 3) {
				// studentMap.clear();

				throw new BadTeamException("Need at least 3 kinds of personality typy! \nSwapping team failed!");
			}

			if (!personalityType.contains('A')) {
				// studentMap.clear();

				throw new BadTeamException("Need at least one A personality type of student! \nSwapping team failed!");
			}

			for (String i : studentMap.keySet()) {
				StringTokenizer st = new StringTokenizer(studentMap.get(i).getConflict().trim());
				while (st.hasMoreTokens()) {
					if (studentMap.keySet().contains(st.nextToken().trim())) {

						throw new BadTeamException("Confilicts crashed! \nSwapping team failed!");

					}
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(e.getMessage());
			alert.showAndWait();

			System.err.println(e);
			return false;
		}

	}

	// validate team conflict for algorithm
	public boolean validationstudentswithouterrorLog() {

		Set<Character> personalityType = new HashSet<Character>();

		for (String i : studentMap.keySet()) {
			personalityType.add(studentMap.get(i).getPersonalityTypy());

		}

		if (personalityType.size() < 3) {

			return false;

		}

		if (!personalityType.contains('A')) {

			return false;

		}

		for (String i : studentMap.keySet()) {
			StringTokenizer st = new StringTokenizer(studentMap.get(i).getConflict().trim());
			while (st.hasMoreTokens()) {
				if (studentMap.keySet().contains(st.nextToken().trim())) {
					return false;

				}
			}
		}

		return true;

	}

	public void projectCompetency() {

		double p = 0;
		double n = 0;
		double a = 0;
		double w = 0;

		StringTokenizer st = new StringTokenizer(project.getRanking().trim());

		st.nextToken();
		p = Integer.parseInt(st.nextToken().trim());

		st.nextToken();
		n = Integer.parseInt(st.nextToken().trim());

		st.nextToken();
		a = Integer.parseInt(st.nextToken().trim());

		st.nextToken();
		w = Integer.parseInt(st.nextToken().trim());

		projectCompetency.put("P", p);
		projectCompetency.put("N", n);
		projectCompetency.put("A", a);
		projectCompetency.put("W", w);

	}

	public void printComputation() {
		// P N A W average competence skills;
		Map<String, Double> competency = new LinkedHashMap<String, Double>();
		double p = 0;
		double n = 0;
		double a = 0;
		double w = 0;

		for (String i : studentMap.keySet()) {

			StringTokenizer st = new StringTokenizer(studentMap.get(i).getRanking().trim());

			st.nextToken();
			p += Integer.parseInt(st.nextToken().trim());

			st.nextToken();
			n += Integer.parseInt(st.nextToken().trim());

			st.nextToken();
			a += Integer.parseInt(st.nextToken().trim());

			st.nextToken();
			w += Integer.parseInt(st.nextToken().trim());

		}

		p = p / 4;
		n = n / 4;
		a = a / 4;
		w = w / 4;

		competency.put("P", p);
		competency.put("N", n);
		competency.put("A", a);
		competency.put("W", w);

		System.out.println("Skills ==> Project level - Average level = short fall");
		for (String i : competency.keySet()) {

			System.out.println(i + " ==> " + projectCompetency.get(i) + " - " + competency.get(i) + " = "
					+ (projectCompetency.get(i) - competency.get(i)));

		}

		// compute shortfall;
		double shortfall = 0;

		for (String i : competency.keySet()) {
			shortfall += (projectCompetency.get(i) - competency.get(i));
		}

		System.out.println("Total short fall: " + shortfall);

	}

	public double getAverageCompetency() {

		double p = 0;
		double n = 0;
		double a = 0;
		double w = 0;

		for (String i : studentMap.keySet()) {

			StringTokenizer st = new StringTokenizer(studentMap.get(i).getRanking().trim());

			st.nextToken();
			p += Integer.parseInt(st.nextToken().trim());

			st.nextToken();
			n += Integer.parseInt(st.nextToken().trim());

			st.nextToken();
			a += Integer.parseInt(st.nextToken().trim());

			st.nextToken();
			w += Integer.parseInt(st.nextToken().trim());

		}

		return (p + n + a + w) / 16;

	}

	public double getInividualGap(String studentID) {

		Map<String, Double> competency = new LinkedHashMap<String, Double>();
		double p = 0;
		double n = 0;
		double a = 0;
		double w = 0;

		StringTokenizer st = new StringTokenizer(studentMap.get(studentID).getRanking().trim());

		st.nextToken();
		p += Integer.parseInt(st.nextToken().trim());

		st.nextToken();
		n += Integer.parseInt(st.nextToken().trim());

		st.nextToken();
		a += Integer.parseInt(st.nextToken().trim());

		st.nextToken();
		w += Integer.parseInt(st.nextToken().trim());

		competency.put("P", p);
		competency.put("N", n);
		competency.put("A", a);
		competency.put("W", w);

		double shortfall = 0;

		for (String i : competency.keySet()) {

			if (projectCompetency.get(i) > competency.get(i)) {
				shortfall += projectCompetency.get(i) - competency.get(i);
			} else {
				continue;
			}

		}

		return shortfall;

	}

	public double getShortfall() {

		double shortfall = 0;
		for (String i : studentMap.keySet()) {

			shortfall += getInividualGap(i);

		}

		return shortfall / 4;

	}

	public boolean getIndividulPercent(String studentID) {

		StringTokenizer st = new StringTokenizer(studentMap.get(studentID).getPreferences().trim());

		for (int j = 0; j < 4; j++) {

			if (st.nextToken().equals(project.getID())) {
				return true;
			} else {
				continue;
			}
		}

		return false;

	}

	public double getPercentage() {

		double percentage = 0;

		for (String i : studentMap.keySet()) {

			if (studentMap.get(i).getPreferences().equals("")) {
				percentage += 0;
				continue;

			}
			String level = "";
			StringTokenizer st = new StringTokenizer(studentMap.get(i).getPreferences().trim());

			for (int j = 0; j < 4; j++) {

				if (st.nextToken().equals(project.getID())) {
					percentage += 25;
				}
			}

		}

		return percentage;

	}

}
