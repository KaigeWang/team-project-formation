package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DataHandler;
import model.Project;
import model.Student;
import model.Team;

public class Controller {

	@FXML
	private Label overallSkillGap, competency;

	@FXML
	private BarChart<?, ?> bc2;

	@FXML
	private BarChart<?, ?> bc1;

	@FXML
	private BarChart<?, ?> bc3;

	@FXML
	private Button swapping;

	@FXML
	private Label idBlank;

	@FXML
	private TableColumn<Table, String> priority;

	@FXML
	private CheckBox p1s1, p1s2, p1s3, p1s4;

	@FXML
	private CheckBox p4s1, p4s2, p4s3, p4s4;

	@FXML
	private CheckBox p3s1, p3s2, p3s3, p3s4;

	@FXML
	private CheckBox p2s1, p2s2, p2s3, p2s4;

	@FXML
	private CheckBox p5s1, p5s2, p5s3, p5s4;

	@FXML
	private TableColumn<Table, Double> skillGpa, competence;

	@FXML
	private CategoryAxis xaxis2, xaxis1, xaxis3;

	@FXML
	private TableColumn<Table, String> conflict, studentSkill, preferences, studentID;

	@FXML
	private TableColumn<Table, String> personality;

	@FXML
	private Label projectID, projectTitle, desc, ownerID, ranking;

	@FXML
	private Label team1, team2, team3, team4, team5;

	@FXML
	private NumberAxis yaxis1, yaxis2, yaxis3;

	@FXML
	private TableView<Table> inforTable;
	@FXML
	private Button project1, project2, project3, project4, project5;

	@FXML
	private TextArea queryText, resultText;
	@FXML
	private Label suggestion;

	@FXML
	private TextArea History;

	private Table student1, student2, student3, student4;

	// create table data;
	private ObservableList<Table> data;
	private DataHandler dh;
	private Map<String, Project> projects;
	private Map<String, Student> students;
	private Map<String, Team> shortlist;

	@FXML
	public void initialize() throws Exception {
		// configure tables
		conflict.setCellValueFactory(new PropertyValueFactory<Table, String>("conflict"));
		studentSkill.setCellValueFactory(new PropertyValueFactory<Table, String>("studentSkill"));
		preferences.setCellValueFactory(new PropertyValueFactory<Table, String>("preferences"));
		studentID.setCellValueFactory(new PropertyValueFactory<Table, String>("studentID"));
		personality.setCellValueFactory(new PropertyValueFactory<Table, String>("personality"));

		skillGpa.setCellValueFactory(new PropertyValueFactory<Table, Double>("skillGap"));
		competence.setCellValueFactory(new PropertyValueFactory<Table, Double>("competence"));

		priority.setCellValueFactory(new PropertyValueFactory<Table, String>("priority"));

		dh = new DataHandler();
		projects = dh.getProject();
		students = dh.getStudent();
		shortlist = dh.getShortlist();
		// show team details
		updateTeam();
		// default table
		data = FXCollections.observableArrayList(

				student1 = new Table("-", "-", "-", "-", '-', 0.0, 0.0, false),
				student2 = new Table("-", "-", "-", "-", '-', 0.0, 0.0, false),
				student3 = new Table("-", "-", "-", "-", '-', 0.0, 0.0, false),
				student4 = new Table("-", "-", "-", "-", '-', 0.0, 0.0, false));
		inforTable.setItems(data);

		// initialize charts
		xaxis1.setLabel("Standard deviation: " + String.format("%.2f", dh.calculateSD(dh.averageComList())));
		xaxis2.setLabel("Standard deviation: " + String.format("%.2f", dh.calculateSD(dh.averageGapList())));
		xaxis3.setLabel("Standard deviation: " + String.format("%.2f", dh.calculateSD(dh.averagePercentList()) / 100));
		yaxis1.setLabel("Competency Level");
		yaxis2.setLabel("Skills Gap");
		yaxis3.setLabel("Percentage %");

		updateChart();
		setStudentBox();

	}

	public void updateTable(Button projectLabel) {

		Table[] sttable = { student1, student2, student3, student4 };

		String projectID = projectLabel.getText();

		Team studentset = shortlist.get(projectID);
		Map<String, Student> studentMap = studentset.getStudentMap();
		ArrayList<Student> studentlist = new ArrayList<Student>();

		for (String i : studentMap.keySet()) {
			studentlist.add(students.get(i));
		}

		for (int i = 0; i < 4; i++) {
			String studentID = studentlist.get(i).getID();
			String skills = studentlist.get(i).getRanking();
			char personality = studentlist.get(i).getPersonalityTypy();
			String conflict = studentlist.get(i).getConflict();
			String preferences = studentlist.get(i).getPreferences();
			double competence = studentlist.get(i).getCompetence();
			// double competence = studentset.getAverageCompetency();
			double skillGap = studentset.getInividualGap(studentlist.get(i).getID());
			boolean priority = studentset.getIndividulPercent(studentlist.get(i).getID());

			sttable[i] = new Table(studentID, skills, conflict, preferences, personality, competence, skillGap,
					priority);

		}

		data = FXCollections.observableArrayList(sttable[0], sttable[1], sttable[2], sttable[3]);

		inforTable.setItems(data);

		competency.setText("Competency: " + String.format("%.4f", studentset.getAverageCompetency()));
		overallSkillGap.setText("Skill gap: " + studentset.getShortfall());

	}

	public void updateTeamDetails(Button projectLabel) {
		String projectID = projectLabel.getText();

		// projectID,projectTitle,desc,ownerID,ranking;
		this.projectID.setText(projectID);
		projectTitle.setText(projects.get(projectID).getTitle());
		desc.setText(projects.get(projectID).getDesc());
		ownerID.setText(projects.get(projectID).getPoID());
		ranking.setText(projects.get(projectID).getRanking());

	}

	public void setStudentBox() {

		Label[] teams = { team1, team2, team3, team4, team5 };
		CheckBox[] teambox1 = { p1s1, p1s2, p1s3, p1s4 };
		CheckBox[] teambox2 = { p2s1, p2s2, p2s3, p2s4 };
		CheckBox[] teambox3 = { p3s1, p3s2, p3s3, p3s4 };
		CheckBox[] teambox4 = { p4s1, p4s2, p4s3, p4s4 };
		CheckBox[] teambox5 = { p5s1, p5s2, p5s3, p5s4 };
		CheckBox[][] teamboxTotal = { teambox1, teambox2, teambox3, teambox4, teambox5 };

		int count = 0;
		for (String i : shortlist.keySet()) {
			teams[count].setText(i);
			int count1 = 0;
			for (String j : shortlist.get(i).getStudentMap().keySet()) {
				teamboxTotal[count][count1].setText(j);
				count1++;
			}
			count++;
		}

	}

	public void updateTeam() throws Exception {
		dh.readSelections();

		Button[] buttonArray = { project1, project2, project3, project4, project5 };

		int j = 0;

		for (String i : shortlist.keySet()) {

			buttonArray[j].setText(i);
			j++;
		}

	}

	public void updateChart() {

		xaxis1.setLabel("Standard deviation: " + String.format("%.4f", dh.calculateSD(dh.averageComList())));
		xaxis2.setLabel("Standard deviation: " + String.format("%.4f", dh.calculateSD(dh.averageGapList())));
		xaxis3.setLabel("Standard deviation: " + String.format("%.4f", dh.calculateSD(dh.averagePercentList())) + "%");

		XYChart.Series[] dataArray = { new XYChart.Series(), new XYChart.Series(), new XYChart.Series(),
				new XYChart.Series(), new XYChart.Series() };
		bc1.getData().clear();
		int counts = 0;
		for (String i : shortlist.keySet()) {
			dataArray[counts].getData().add(new XYChart.Data(i, shortlist.get(i).getAverageCompetency()));
			dataArray[counts].setName(i);
			bc1.getData().add(dataArray[counts]);
			counts++;
		}

		XYChart.Series[] dataArray1 = { new XYChart.Series(), new XYChart.Series(), new XYChart.Series(),
				new XYChart.Series(), new XYChart.Series() };
		bc2.getData().clear();
		int counts1 = 0;
		for (String i : shortlist.keySet()) {
			dataArray1[counts1].getData().add(new XYChart.Data(i, shortlist.get(i).getShortfall()));
			dataArray1[counts1].setName(i);
			bc2.getData().add(dataArray1[counts1]);
			counts1++;
		}

		XYChart.Series[] dataArray2 = { new XYChart.Series(), new XYChart.Series(), new XYChart.Series(),
				new XYChart.Series(), new XYChart.Series() };
		bc3.getData().clear();
		int counts2 = 0;
		for (String i : shortlist.keySet()) {
			dataArray2[counts2].getData().add(new XYChart.Data(i, shortlist.get(i).getPercentage()));
			dataArray2[counts2].setName(i);
			bc3.getData().add(dataArray2[counts2]);
			counts2++;
		}

	}

	@FXML
	void updateTeamInfor(ActionEvent event) throws Exception {

		updateTeam();
		updateChart();
	}

	@FXML
	void showProjectMembers1(ActionEvent event) throws Exception {
		updateTable(project1);
		updateTeamDetails(project1);
	}

	@FXML
	void showProjectMembers2(ActionEvent event) {
		updateTable(project2);
		updateTeamDetails(project2);
	}

	@FXML
	void showProjectMembers3(ActionEvent event) {
		updateTable(project3);
		updateTeamDetails(project3);
	}

	@FXML
	void showProjectMembers4(ActionEvent event) {
		updateTable(project4);
		updateTeamDetails(project4);
	}

	@FXML
	void showProjectMembers5(ActionEvent event) {
		updateTable(project5);
		updateTeamDetails(project5);
	}

	@FXML
	void checkbox(ActionEvent event) {

		String message = "";

		CheckBox[] teambox1 = { p1s1, p1s2, p1s3, p1s4 };
		CheckBox[] teambox2 = { p2s1, p2s2, p2s3, p2s4 };
		CheckBox[] teambox3 = { p3s1, p3s2, p3s3, p3s4 };
		CheckBox[] teambox4 = { p4s1, p4s2, p4s3, p4s4 };
		CheckBox[] teambox5 = { p5s1, p5s2, p5s3, p5s4 };
		CheckBox[][] teamboxTotal = { teambox1, teambox2, teambox3, teambox4, teambox5 };

		for (int i = 0; i < teamboxTotal.length; i++) {
			for (int j = 0; j < 4; j++) {
				if (teamboxTotal[i][j].isSelected()) {
					message += teamboxTotal[i][j].getText() + " ";
				}
			}
		}

		idBlank.setText(message);

	}

	@FXML
	void swapstudents(ActionEvent event) throws Exception {

		String swapstring = idBlank.getText().trim();
		List<String> swaplist = Arrays.asList(swapstring.trim().split("\\s+"));
		if (swaplist.size() != 2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Please select exactly two students to swap!");
			alert.showAndWait();
		} else {

			dh.swapStudents(swaplist);

		}
		setStudentBox();
		updateChart();
		History.setText(dh.getHistory());

	}

	@FXML
	void writeToDatabase(ActionEvent event) {

		Database.createProjectTable(shortlist);

	}

	@FXML
	void query(ActionEvent event) {

		resultText.setText(Database.query(queryText.getText()));

		Database.query1(queryText.getText());

	}

	@FXML
	void clearQuery(ActionEvent event) {
		queryText.setText("");
		resultText.setText("");
	}

	@FXML
	void balanceCompentency(ActionEvent event) throws Exception {
		suggestion.setText(dh.balanceCompetency());
		History.setText(dh.getHistory());
		updateChart();
		setStudentBox();

	}

	@FXML
	void balanceGap(ActionEvent event) throws Exception {

		suggestion.setText(dh.balanceGap());

		History.setText(dh.getHistory());
		updateChart();
		setStudentBox();

	}

	@FXML
	void balancePriority(ActionEvent event) throws Exception {
		suggestion.setText(dh.balancePriority());
		History.setText(dh.getHistory());
		updateChart();
		setStudentBox();

	}

	@FXML
	void undo(ActionEvent event) throws Exception {
		suggestion.setText(dh.undo());
		History.setText(dh.getHistory());
		updateChart();
		setStudentBox();

	}

	@FXML
	void autoBalance(ActionEvent event) throws Exception {
		suggestion.setText(dh.AutoBalance());
		History.setText(dh.getHistory());
		updateChart();
		setStudentBox();

	}

	@FXML
	void increasePriority(ActionEvent event) throws Exception {
		suggestion.setText(dh.increasePriority());
		History.setText(dh.getHistory());
		updateChart();
		setStudentBox();

	}

}
