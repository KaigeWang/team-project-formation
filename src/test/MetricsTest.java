package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exception.BadTeamException;
import model.DataHandler;
import model.Project;
import model.Student;
import model.Team;

public class MetricsTest {

	DataHandler dh;
	Map<String, Team> shortlist;
	Map<String, Project> projects;
	Map<String, Student> students;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		dh = new DataHandler();
		dh.readSelections();
		shortlist = dh.getShortlist();
		projects = dh.getProject();
		students = dh.getStudent();
	}

	@After
	public void tearDown() throws Exception {

	}

	//test1
	@Test
	public void ShortFall() throws Exception {

		assertEquals(shortlist.get("Pr3").getShortfall(), 2, 0.01);
		assertEquals(shortlist.get("Pr5").getShortfall(), 2.5, 0.01);
		assertEquals(shortlist.get("Pr6").getShortfall(), 2.75, 0.01);
		assertEquals(shortlist.get("Pr9").getShortfall(), 2.25, 0.01);
		assertEquals(shortlist.get("Pr1").getShortfall(), 3, 0.01);
	}

	//test2
	@Test
	public void averageCompetence() throws Exception {
		assertEquals(shortlist.get("Pr3").getAverageCompetency(), 2.8125, 0.01);
		assertEquals(shortlist.get("Pr5").getAverageCompetency(), 2.3125, 0.01);
		assertEquals(shortlist.get("Pr6").getAverageCompetency(), 2.1875, 0.01);
		assertEquals(shortlist.get("Pr9").getAverageCompetency(), 2.625, 0.01);
		assertEquals(shortlist.get("Pr1").getAverageCompetency(), 2.5625, 0.01);
	}

	//test3
	@Test
	public void percentage() throws Exception {
		assertEquals(shortlist.get("Pr3").getPercentage(), 75, 0.01);
		assertEquals(shortlist.get("Pr5").getPercentage(), 75, 0.01);
		assertEquals(shortlist.get("Pr6").getPercentage(), 50, 0.01);
		assertEquals(shortlist.get("Pr9").getPercentage(), 75, 0.01);
		assertEquals(shortlist.get("Pr1").getPercentage(), 75, 0.01);
	}

	//test4
	@Test
	public void gapSD() throws Exception, BadTeamException {
		dh.readSelections();
		assertEquals(DataHandler.calculateSD(dh.averageGapList()), 0.3536, 0.001);

	}

	//test5
	@Test
	public void competenceSD() throws Exception {
		assertEquals(DataHandler.calculateSD(dh.averageComList()), 0.2236, 0.001);

	}

	//test6
	@Test
	public void percentageSD() throws Exception {
		assertEquals(DataHandler.calculateSD(dh.averagePercentList()), 10, 0.001);
	}

	//test7
	@Test
	public void formTeam() throws Exception {
		// Will print successful
		Team ss = new Team(projects.get("Pr3"));
		ss.addStudent(students.get("S1"));
		ss.addStudent(students.get("S2"));
		ss.addStudent(students.get("S3"));
		ss.addStudent(students.get("S4"));
		ss.validationstudents();
		System.out.println("successful");
	}

	//test8
	@Test(expected = BadTeamException.class)
	public void formTeamExceptions() throws Exception {
		// S1 and S20 have conflict
		Team ss = new Team(projects.get("Pr10"));
		ss.addStudent(students.get("S5"));//type B conflict S20
		ss.addStudent(students.get("S20"));//type A
		ss.addStudent(students.get("S3"));//type A
		ss.addStudent(students.get("S4"));//type D
		ss.validationstudents();
		System.out.println("fail");
	}

	//test9
	@Test(expected = BadTeamException.class)
	public void formTeamExceptions1() throws Exception {
		// Less than 3 kinds of type
		Team ss = new Team(projects.get("Pr10"));
		ss.addStudent(students.get("S6"));//type C
		ss.addStudent(students.get("S13"));//type C
		ss.addStudent(students.get("S9"));//type C
		ss.addStudent(students.get("S3"));//type A
		ss.validationstudents();
		System.out.println("fail");
	}

	//test10
	@Test(expected = BadTeamException.class)
	public void formTeamExceptions2() throws Exception {
		// No A type
		Team ss = new Team(projects.get("Pr10"));
		ss.addStudent(students.get("S6"));//type
		ss.addStudent(students.get("S13"));
		ss.addStudent(students.get("S10"));
		ss.addStudent(students.get("S4"));
		ss.validationstudents();
		System.out.println("fail");
	}

}
