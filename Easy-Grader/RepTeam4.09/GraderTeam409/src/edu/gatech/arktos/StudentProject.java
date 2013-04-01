package edu.gatech.arktos;

public class StudentProject {
private String studentName;
private String projectName;
private String teamName;
private int averageGrade;
//private int averageProjectGrade;
//private int averageContribution;
private int teamGrade;
private String indEvaluation;

public StudentProject(){
	this.studentName=null;
	this.projectName=null;
	this.teamName=null;
//	this.averageGrade=0;
	this.teamGrade=0;
	this.indEvaluation=null;
}

public String getStudentName(){
	return studentName;
}
public void setStudentName(String studentName){
	this.studentName=studentName;
}

public String getProjectName() {
	return projectName;
}

public void setProjectName(String projectName) {
	this.projectName = projectName;
}

public String getTeamName(){
	return teamName;
}
public void setTeamName(String teamName){
	this.teamName=teamName;
}

public int getAverageGrade(){
	return averageGrade;
}
public void setAverageGrade(int averageGrade){
	this.averageGrade=averageGrade;
}

public int getTeamGrade(){
	return teamGrade;
}
public void setTeamGrade(int teamGrade){
	this.teamGrade=teamGrade;
}

public String getIndEvaluation(){
	return indEvaluation;
}
public void setIndEvaluation(String indEvaluation){
	this.indEvaluation=indEvaluation;
}

//public int averageProjectGrade(){
//	return averageProjectGrade;
//}
//
//public int averageContribution(){
//	return averageContribution;
//}

}

