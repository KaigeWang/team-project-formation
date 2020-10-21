package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class Project implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String id;
	private String desc;
	private String poID;
	private String ranking;

	public Project(String title, String id, String desc, String poID, String ranking) {

		this.title = title;
		this.id = id;
		this.desc = desc;
		this.poID = poID;
		this.ranking = ranking;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPoID() {
		return poID;
	}

	public void setPoID(String poID) {
		this.poID = poID;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public String toString() {
		return title + "; " + id + "; " + desc + "; " + poID + "; " + ranking + ";\n";
	}

	public String getID() {
		return id;
	}

	public String getRanking() {

		return ranking.trim();
	}

	public void writeToFile() throws IOException {

		File file = new File("projects.txt");

		BufferedWriter bf = null;

		try {
			bf = new BufferedWriter(new FileWriter(file, true));

			bf.write(toString());

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			bf.close();
		}
	}

}
