package model;

import java.io.Serializable;
import java.util.StringTokenizer;

public class Student implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7867759677624696863L;
	private String id;
	private String ranking;
	private char personalityType;
	private String conflict;
	private String preferences;

	public Student(String id, String ranking) {
		this.id = id;
		this.ranking = ranking;
		this.personalityType = ' ';
		this.conflict = "";
		this.preferences = "";

	}

	public String getID() {
		return id;
	}

	public String toString() {

		if (conflict.equals("") && preferences.equals("")) {
			return id + "; " + ranking + "; " + personalityType + "; " + "**" + "; " + "**" + ";";
		} else if (conflict.equals("")) {
			return id + "; " + ranking + "; " + personalityType + "; " + "**" + "; " + preferences + ";";
		} else if (preferences.equals("")) {
			return id + "; " + ranking + "; " + personalityType + "; " + conflict + "; " + "**" + ";";
		} else {
			return id + "; " + ranking + "; " + personalityType + "; " + conflict + "; " + preferences + ";";
		}

	}

	public String getRanking() {
		return ranking;
	}

	public void setPersonalityType(char personalityType) {
		this.personalityType = personalityType;
	}

	public void setConflict(String conflict) {
		this.conflict = conflict;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getPreferences() {

		return preferences;
	}

	public char getPersonalityTypy() {
		return personalityType;
	}

	public String getConflict() {
		return conflict;
	}

	public double getCompetence() {

		StringTokenizer st = new StringTokenizer(ranking.trim());

		double sum = 0;

		while (st.hasMoreTokens()) {
			st.nextToken();
			sum += Integer.parseInt(st.nextToken());
		}

		return sum / 4;

	}

}
