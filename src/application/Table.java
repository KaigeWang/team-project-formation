package application;

import javafx.beans.property.*;

public class Table {

	private SimpleStringProperty conflict;
	private SimpleStringProperty studentSkill;
	private SimpleStringProperty preferences;
	private SimpleStringProperty studentID;
	private SimpleStringProperty personality;
	private SimpleStringProperty priority;
	private SimpleDoubleProperty skillGap;
	private SimpleDoubleProperty competence;

	public Table(String studentID, String studentSkill, String conflict, String preferences, char personality,
			double competence, double skillGap, boolean priority) {
		this.conflict = new SimpleStringProperty(conflict);
		this.studentSkill = new SimpleStringProperty(studentSkill);
		this.preferences = (new SimpleStringProperty(preferences));
		this.studentID = (new SimpleStringProperty(studentID));
		this.personality = (new SimpleStringProperty(String.valueOf(personality)));

		this.skillGap = (new SimpleDoubleProperty(skillGap));
		this.competence = (new SimpleDoubleProperty(competence));

		this.priority = (new SimpleStringProperty(String.valueOf(priority)));
	}

	public String getConflict() {
		return conflict.get();
	}

	public void setConflict(String c) {
		conflict.set(c);
		;
	}

	public String getStudentSkill() {
		return studentSkill.get();
	}

	public void setStudentSkill(String c) {
		studentSkill.set(c);
	}

	public String getPreferences() {
		return preferences.get();
	}

	public void setPreferences(String c) {
		preferences.set(c);
	}

	public String getStudentID() {
		return studentID.get();
	}

	public void setStudentID(String c) {
		studentID.set(c);
	}

	public String getPersonality() {
		return personality.get();
	}

	public void setPersonality(char c) {
		personality.set(String.valueOf(c));
	}

	public String getPriority() {
		return priority.get();
	}

	public void setPriority(boolean c) {
		priority.set(String.valueOf(c));
	}

	public double getSkillGap() {
		return skillGap.get();
	}

	public void setSkillGap(double c) {
		skillGap.set(c);
	}

	public double getCompetence() {
		return competence.get();
	}

	public void setCompetence(double c) {
		competence.set(c);
	}

}
