package edu.gatech.arktos;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class GradesDB {
	private SpreadsheetService service = null;
	private SpreadsheetEntry spreadsheet = null;

	public GradesDB() {
		this.service = null;
		this.spreadsheet = null;
	}

	public GradesDB(SpreadsheetService service, SpreadsheetEntry entry) {
		this.service = service;
		this.spreadsheet = entry;
	}

	public int getNumStudents() throws Exception, MalformedURLException,
			IOException, ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		int numStudents = 0;
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Details")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				numStudents = listFeed.getTotalResults();
			}
		}

		return numStudents;
	}

	public int getNumAssignments() throws Exception, MalformedURLException,
			IOException, ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		int numAssignments = 0;

		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Data")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					if (row.getCustomElements().getValue("Assignments") != null) {
						numAssignments++;
					}
				}
			}
		}
		return numAssignments;
	}

	public int getNumProjects() throws Exception, MalformedURLException,
			IOException, ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		int numProjects = 0;

		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Data")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					if (row.getCustomElements().getValue("Projects") != null) {
						numProjects++;
					}
				}
			}
		}
		return numProjects;
	}

	/**
	 * Get the list of all student objects
	 * @return List of all students(name, gtid, email, attendance, involved assignments, involved projects)
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	public HashSet<Student> getStudents() throws Exception,
			MalformedURLException, IOException, ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		HashSet<Student> students = new HashSet<Student>();

		//Get all attendance list, projects list, team list, and assignments list for all students
		HashMap<String, Integer> attandenceList = getAttendance();
		ArrayList<Project> projects = getProjects();
		ArrayList<Assignment> assignments = getAssignments();
		
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Details")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					Student student = new Student();
					String studentName = row.getCustomElements().getValue(
							"NAME");
					student.setName(studentName);
					student.setGtid(row.getCustomElements().getValue("GTID"));
					student.setEmail(row.getCustomElements().getValue("EMAIL"));
					student.setAttendance(attandenceList.get(studentName));
					student.setInvolvedAssignment(getStudentAssignmentsByName(
							studentName, assignments));
					student.setInvolvedProjects(getStudentProjectByName(studentName,
							projects));
					students.add(student);
				}
				break;
			}
		}

		return students;
	}

	/**
	 * Get student object by his name
	 * @param student name
	 * @return student object whose name equals to input parameter
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	public Student getStudentByName(String string) throws Exception,
			MalformedURLException, IOException, ServiceException {
		Student student = null;

		HashSet<Student> students = this.getStudents();
		if (students != null) {
			for (Student s : students) {
				if (s.getName().equals(string)) {
					student = s;
					break;
				}
			}
		}

		return student;
	}

	/**
	 * Get student object by his gtid
	 * @param gt id
	 * @return student object whose gtid equals to the input parameter
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	public Student getStudentByID(String string) throws Exception,
			MalformedURLException, IOException, ServiceException {
		Student student = null;
		HashSet<Student> students = this.getStudents();
		if (students != null) {
			for (Student s : students) {
				if (s.getGtid().equals(string)) {
					student = s;
					break;
				}
			}
		}

		return student;
	}


	/**
	 * Get all assignments from "Grades" and "Data" sheets
	 * 
	 * @return Assignments List
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	public ArrayList<Assignment> getAssignments() throws Exception,
			MalformedURLException, IOException, ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		HashMap<String, Student> stuMaps = this.getStudentsMap();
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();

		// Get assignments list from "Data" sheet first(only assignment name and
		// description)
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Data")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					String assignmentName = row.getCustomElements().getValue(
							"assignments");
					String desc = row.getCustomElements().getValue(
							"description_2");
					if (assignmentName != null) {
						Assignment ass = new Assignment();
						ass.setAssignmentNumber(Integer
								.parseInt(assignmentName.toLowerCase()
										.replace("assignment", "").trim()));
						ass.setAssignmentDescription(desc);
						assignments.add(ass);
					}
				}
				break;
			}
		}

		// Get details(student, scores) for each assignment from "Grades" sheet
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Grades")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);

				for (ListEntry row : listFeed.getEntries()) {
					String stuName = row.getCustomElements().getValue("NAME");
					Student student = stuMaps.get(stuName);
					Iterator<Assignment> it = assignments.iterator();
					while (it.hasNext()) {
						Assignment assignment = it.next();
						int stuScore = Integer
								.parseInt(row.getCustomElements().getValue(
										"assignment"
												+ assignment
														.getAssignmentNumber()));
						assignment.setTotalScore(assignment.getTotalScore()
								+ stuScore);
						assignment.getStudentList().put(stuName, stuScore);
						assignment.getScores().put(student, stuScore);
					}
				}
				break;
			}
		}

		// Calculate average score for each assignment
		Iterator<Assignment> it = assignments.iterator();
		while (it.hasNext()) {
			Assignment assignment = it.next();
			assignment.setAverageGrade(getAvg(assignment.getTotalScore(),
					assignment.getScores().size()));
		}

		return assignments;
	}

	/**
	 * Get assignment by the given assignment number
	 * @param i: assignment number
	 * @return the assignment object whose number equals to the given parameter
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public Assignment getAssignmentByNumber(int i) throws Exception {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		ArrayList<Assignment> assignments = getAssignments();
		for (Assignment ass : assignments) {
			if (ass.getAssignmentNumber() == i) {
				return ass;
			}
		}

		return null;
	}

	/**
	 * Get all the teams for the given project
	 * @param projName: project name
	 * @return list of teams whose project name equals to the input parameter
	 * @throws Exception
	 */
	public HashSet<Team> getTeamsByProject(String projName) throws Exception{
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		HashMap<String, Student> stuMaps = this.getStudentsMap();
		HashSet<Team> teams = new HashSet<Team>();
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().contains("Teams")
					&& worksheet.getTitle().getPlainText().contains(projName)) {
				HashMap<String, String> contributionMap = getContributionByProject(projName);
				HashMap<String, Integer> teamGradesMap = getTeamGradesByProject(projName);

				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					Team team = new Team();
					team.setProjectName(projName);
					String teamName = row.getTitle().getPlainText();
					team.setTeamName(teamName);
					team.setName(teamName);
					team.setTeamScore(teamGradesMap.get(teamName));

					HashMap<String, String> studentsList = new HashMap<String, String>();
					HashSet<Student> members = new HashSet<Student>();
					HashMap<Student, Float> ratings = new HashMap<Student, Float>();

					for (String tag : row.getCustomElements().getTags()) {
						if (tag.equals("teamname"))
							continue;
						String name = row.getCustomElements().getValue(tag);
						if (name != null) {
							Student s = stuMaps.get(name);
							String rating = contributionMap.get(name);
							studentsList.put(name, rating);
							members.add(s);
							ratings.put(s,
									formatScore(Float.parseFloat(rating)));
						}
					}
					team.setStudentList(studentsList);
					team.setMembers(members);
					team.setRatings(ratings);
					teams.add(team);
				}
				break;
			}
		}
		return teams;
	}

	/**
	 * Get team by given project name and team name
	 * @param projName
	 * @param teamName
	 * @return the team whose project name = projName, team name = teamName
	 */
	public Team getTeam(String projName, String teamName) {
		try {
			HashSet<Team> teams = getTeamsByProject(projName);

			for (Team team : teams) {
				if (team.getProjectName().equals(projName)
						&& team.getName().equals(teamName)) {
					return team;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get all the projects
	 * @return A list of all the projects(name, desc, avg score, teams list)
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	public ArrayList<Project> getProjects() throws Exception,
			MalformedURLException, IOException, ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		ArrayList<Project> projects = new ArrayList<Project>();

		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Data")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					String name = row.getCustomElements().getValue("projects");
					String desc = row.getCustomElements().getValue(
							"description");
					if (name != null) {
						Project p = new Project();
						p.setProjectName(name);
						p.setProjectDescription(desc);
						
						int a = 0;
						HashMap<String, Integer> List = new HashMap<String, Integer>();
						HashSet<Team> teams = getTeamsByProject(name);

						// Calculate avg grade for all teams
						Iterator<Team> it = teams.iterator();
						while (it.hasNext()) {
							Team team = it.next();
							a += team.getTeamScore();
							List.put(team.getName(), team.getTeamScore());
						}

						p.setAverageGrade(this.getAvg(a, teams.size()));
						p.setTeamList(List);
						p.setTeams(teams);
						projects.add(p);
					}
				}
				break;
			}
		}
		return projects;
	}

	/**
	 * Get the project whose name equals to the given input parameter
	 * @param projName
	 * @return project whose name equals to the given input parameter
	 */
	public Project getProjectByName(String projName) {
		try {
			ArrayList<Project> projects = getProjects();
			Iterator<Project> it = projects.iterator();
			while (it.hasNext()) {
				Project p = it.next();
				if (p.getProjectName().equals(projName))
					return p;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	


	public SpreadsheetService getService() {
		return service;
	}

	public void setService(SpreadsheetService service) {
		this.service = service;
	}

	public SpreadsheetEntry getSpreadsheet() {
		return spreadsheet;
	}

	public void setSpreadsheet(SpreadsheetEntry spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	// Dispose the gradesdb if logout
	public void close() {
		this.service = null;
		this.spreadsheet = null;
	}

	private int parseAttandence(String string) {
		string = string.replace("%", "");
		return (int) Math.floor(Double.parseDouble(string));
	}

	/**
	 * Get the contribution for each student in each team belonged to the given project
	 * @param projName
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> getContributionByProject(String projName)
			throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().contains(projName)
					&& worksheet.getTitle().getPlainText().contains("Contri")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					if (row.getCustomElements().getValue("students") != null) {
						float value = Float.parseFloat(row.getCustomElements()
								.getValue("average"));
						BigDecimal newValue = new java.math.BigDecimal(value)
								.setScale(2, java.math.RoundingMode.HALF_UP);

						map.put(row.getCustomElements().getValue("students"),
								newValue.toString());

					}
				}
			}
		}

		return map;
	}

	/**
	 * Get the grade for each team belonged to the given project
	 * @param projName
	 * @return
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private HashMap<String, Integer> getTeamGradesByProject(String projName)
			throws Exception, MalformedURLException, IOException,
			ServiceException {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		HashMap<String, Integer> List = new HashMap<String, Integer>();
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().contains("Grades")
					&& worksheet.getTitle().getPlainText().contains(projName)) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);

				for (ListEntry row : listFeed.getEntries()) {
					if (row.getCustomElements().getValue("Criteria")
							.equals("TOTAL:")) {
						List.put("Team 1", Integer.parseInt(row
								.getCustomElements().getValue("Team1")));
						List.put("Team 2", Integer.parseInt(row
								.getCustomElements().getValue("Team2")));
						List.put("Team 3", Integer.parseInt(row
								.getCustomElements().getValue("Team3")));
					}
				}
			}
		}

		return List;
	}


	/**
	 * Format score to be like 0.00, and half up
	 * @param score
	 * @return
	 */
	private float formatScore(float score) {
		BigDecimal b = new BigDecimal(score);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * Get attendance for each student from "Attendance" sheet
	 * 
	 * @return a map of attendance<Student name, attendance>
	 * @throws Exception
	 */
	private HashMap<String, Integer> getAttendance() throws Exception {
		if (this.service == null) {
			throw new Exception(
					"No active user session to access google services!");
		}

		if (this.spreadsheet == null) {
			throw new Exception("Can not find the grades DB spreadsheet!");
		}

		HashMap<String, Integer> attandenceList = new HashMap<String, Integer>();
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Attendance")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);

				for (ListEntry row : listFeed.getEntries()) {
					String studentName = row.getCustomElements().getValue(
							"studentname");
					String attendance = row.getCustomElements().getValue(
							"Total");
					if (studentName != null && attendance != null) {
						if (!studentName.equals("CLASSES TOTAL")) {
							attandenceList.put(studentName,
									parseAttandence(attendance));
						}
					}
				}
				break;
			}
		}
		return attandenceList;
	}

	/**
	 * Get student map for further quick query
	 * 
	 * @return a map of student<student name, student object>
	 * @throws Exception
	 */
	private HashMap<String, Student> getStudentsMap() throws Exception {
		HashMap<String, Student> map = new HashMap<String, Student>();

		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		// Get student basic information first
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Details")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);
				for (ListEntry row : listFeed.getEntries()) {
					String studentName = row.getCustomElements().getValue(
							"NAME");
					if (studentName != null) {
						Student student = new Student();
						student.setName(studentName);
						student.setGtid(row.getCustomElements()
								.getValue("GTID"));
						student.setEmail(row.getCustomElements().getValue(
								"EMAIL"));
						map.put(studentName, student);
					}
				}
			}
			break;
		}
		// Get student attendance
		for (WorksheetEntry worksheet : worksheets) {
			if (worksheet.getTitle().getPlainText().equals("Attendance")) {
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = service
						.getFeed(listFeedUrl, ListFeed.class);

				for (ListEntry row : listFeed.getEntries()) {
					String name = row.getCustomElements().getValue(
							"studentname");
					if (name.toLowerCase().contains("classes"))
						continue;
					String attendance = row.getCustomElements().getValue(
							"Total");
					map.get(name).setAttendance(
							this.parseAttandence(attendance));
				}

				break;
			}
		}
		return map;
	}

	/**
	 * Get the assignments list the specified student finished
	 * @param stuName: student name
	 * @param assignments: all assignments list
	 * @return the assignments list the given student finished
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private ArrayList<StudentAssignment> getStudentAssignmentsByName(
			String stuName, ArrayList<Assignment> assignments)
			throws Exception, MalformedURLException, IOException,
			ServiceException {
		ArrayList<StudentAssignment> studentassignments = new ArrayList<StudentAssignment>();
		for (Assignment assignment : assignments) {
			HashMap<String, Integer> studentsList = assignment.getStudentList();
			Set studentsSet = studentsList.entrySet();
			Iterator it = studentsSet.iterator();
			while (it.hasNext()) {
				Entry<String, Integer> stu = (Entry<String, Integer>) it.next();
				String name = stu.getKey();
				int grade = stu.getValue();
				if (name.equals(stuName)) {
					StudentAssignment studentAss = new StudentAssignment();
					studentAss.setAssignmentNumber(assignment
							.getAssignmentNumber());
					studentAss.setAverageGrade(assignment.getAverageGrade());
					studentAss.setStudentName(stuName);
					studentAss.setGrade(grade);
					studentassignments.add(studentAss);
				}
			}
		}

		return studentassignments;
	}

	/**
	 * Get all the projects a student finished
	 * @param studentName
	 * @param projects: all the projects list
	 * @return projects list the given student finished
	 * @throws Exception
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private ArrayList<StudentProject> getStudentProjectByName(
			String studentName, ArrayList<Project> projects) throws Exception, MalformedURLException,
			IOException, ServiceException {
		ArrayList<StudentProject> studentProjects = new ArrayList<StudentProject>();
		for (Project project : projects) {
			StudentProject studentProject = new StudentProject();
			studentProject.setProjectName(project.getProjectName());
			studentProject.setStudentName(studentName);
			studentProject.setAverageGrade(project.getAverageGrade());

			HashSet<Team> teams = project.getTeams();
			for (Team team : teams) {
				HashMap<String, String> evaluationMap = team.getStudentList();
				Set evaSet = evaluationMap.entrySet();
				Iterator it = evaSet.iterator();
				while (it.hasNext()) {
					Entry<String, String> stu = (Entry<String, String>) it
							.next();
					String name = stu.getKey();
					String eva = stu.getValue();
					if (name.equals(studentName)) {
						studentProject.setTeamName(team.getTeamName());
						studentProject.setIndEvaluation(eva);
						studentProject.setTeamGrade(team.getTeamScore());
						studentProjects.add(studentProject);
					}
				}
			}

		}

		return studentProjects;
	}
	
	/**
	 * Calculate average score for a list of scores(format: 0.00, and half up)s
	 * 
	 * @param a
	 *            : total score
	 * @param size
	 *            : total size of score list
	 * @return average score for the given list
	 */
	private int getAvg(int a, int size) {
		double num1 = (double) a;
		double num2 = size;
		double result = num1 / num2;
		a = (int) result;
		String resultStr = Double.toString(result);
		int idx = resultStr.indexOf(".");
		char x = resultStr.charAt(idx + 1);
		String xx = x + "";
		if (Integer.parseInt(xx) >= 5) {
			a++;
		}

		return a;
	}
}
