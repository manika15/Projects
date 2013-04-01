package edu.gatech.arktos;

import java.util.HashMap;
import java.util.Map;

public class Assignment implements AssignmentInterface{
private int assignmentNumber;
private String assignmentDescription;
private int totalScore;
private int averageGrade;
private HashMap<String, Integer> StudentList;
private Map<Student, Integer> scores;

public Assignment(){
	this.assignmentNumber=0;
	this.averageGrade=0;
	this.StudentList=new HashMap<String, Integer>();
	this.scores = new HashMap<Student, Integer>();
	this.totalScore = 0;
}



public int getTotalScore() {
	return totalScore;
}



public void setTotalScore(int totalScore) {
	this.totalScore = totalScore;
}



@Override
public int getAssignmentNumber(){
	return assignmentNumber;
}

@Override
public void setAssignmentNumber(int assignmentNumber){
	this.assignmentNumber=assignmentNumber;
}

public int getAverageGrade(){
	return averageGrade;
}
public void setAverageGrade(int averageGrade){
	this.averageGrade=averageGrade;
}

public HashMap<String, Integer> getStudentList(){
	return StudentList;
}
public void setStudentList(HashMap<String,Integer> StudentList){
	this.StudentList=StudentList;
}

@Override
public String getAssignmentDescription() {
	return assignmentDescription;
}

@Override
public void setAssignmentDescription(String assignmentDescription) {
	this.assignmentDescription = assignmentDescription;
}

@Override
public Map<Student, Integer> getScores() {
	return this.scores;
}

@Override
public void setScores(Map<Student, Integer> scores) {
	this.scores = scores;
	
}

}
