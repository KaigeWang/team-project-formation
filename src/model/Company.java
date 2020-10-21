package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Company {
	private String id;
	private String name;
	private int abn;
	private String url;
	private String address;

	public Company(String id, String name, int abn, String url, String address) {
		this.id = id;
		this.name = name;
		this.abn = abn;
		this.url = url;
		this.address = address;

	}

	public String toString() {
		return id + "; " + name + "; " + abn + "; " + url + "; " + address + ";";
	}

	public void writeToFile() throws IOException {

		File file = new File("company.txt");

		BufferedWriter bf = null;

		try {
			bf = new BufferedWriter(new FileWriter(file, true));

			bf.write(toString() + "\n");

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			bf.close();
		}

	}

}
