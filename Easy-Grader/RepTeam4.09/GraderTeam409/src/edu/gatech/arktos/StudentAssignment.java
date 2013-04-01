package edu.gatech.arktos;

public class StudentAssignment {
private String studentName;
private int assignmentNumber;
private int grade;
private int averageGrade;

public StudentAssignment(){
       this.studentName=null;
       this.assignmentNumber=0;
       this.grade=0;
       this.averageGrade=0;
}

public String getStudentName(){
	return studentName;
}
public void setStudentName(String studentName){
	this.studentName=studentName;
}

public int getAssignmentNumber(){
	return assignmentNumber;
}
public void setAssignmentNumber(int AssignmentNumber){
	this.assignmentNumber=AssignmentNumber;
}

public int getGrade(){
	return grade;
}
public void setGrade(int grade){
	this.grade=grade;
}

public int getAverageGrade(){
	return averageGrade;
}
public void setAverageGrade(int averageGrade){
	this.averageGrade=averageGrade;
}

public Object getAssignmentName() {
	return "Assignment " + this.assignmentNumber;
}

}