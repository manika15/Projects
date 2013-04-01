package edu.gatech.arktos;

import java.util.HashSet;
import java.util.Map;

public interface TeamInterface {
	public Map<Student, Float> getRatings();
	public void setRatings(Map<Student, Float> ratings);
	public Float getRatingForStudent(Student s);
	public void setRatingForStudent(Student s, Float score);
	public HashSet<Student> getMembers();
	public void setMembers(HashSet<Student> members);
	public void addStudent(Student student);
	public String getName();
	public void setName(String name);
	public int getSize();
	public int getTeamScore();
	public void setTeamScore(int teamScore);
}