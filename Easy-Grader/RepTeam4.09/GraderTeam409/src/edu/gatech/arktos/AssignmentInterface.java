package edu.gatech.arktos;

import java.util.Map;

public interface AssignmentInterface {
	public abstract int getAssignmentNumber();
	public abstract void setAssignmentNumber(int assignmentNumber);
	public abstract String getAssignmentDescription();
	public abstract void setAssignmentDescription(String assignmentDescription);
	public abstract Map<Student, Integer> getScores();
	public abstract void setScores(Map<Student, Integer> scores);
}