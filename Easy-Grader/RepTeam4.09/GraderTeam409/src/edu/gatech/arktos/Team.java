package edu.gatech.arktos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Team implements TeamInterface {
	private String teamName;
	private String projectName;
	private HashMap<String, String> StudentList= new HashMap<String, String>();
	
	private int teamScore;
	private HashSet<Student> members = new HashSet<Student>();
	private Map<Student, Float> ratings = new HashMap<Student, Float>();

	public Team(){
		this.teamName=null;
		this.projectName=null;
		this.StudentList=null;
		this.teamScore = 0;
	}
	public String getTeamName(){
		return teamName;
	}
	public void setTeamName(String teamName){
		this.teamName=teamName;
	}

	public String getProjectName(){
		return projectName;
	}
	public void setProjectName(String projectName){
		this.projectName=projectName;
	}

	public HashMap<String, String> getStudentList(){
		return StudentList;
	}
	public void setStudentList(HashMap<String,String> StudentList){
		this.StudentList=StudentList;
	}
	
	@Override
	public Map<Student, Float> getRatings() {
		return this.ratings;
	}
	
	@Override
	public void setRatings(Map<Student, Float> ratings) {
		this.ratings = ratings;
	}
	
	@Override
	public Float getRatingForStudent(Student s) {
		if(ratings.containsKey(s)){
			return (Float) ratings.get(s);
		}
		return null;
	}
	
	@Override
	public void setRatingForStudent(Student s, Float score) {
		ratings.put(s, score);
	}
	
	@Override
	public HashSet<Student> getMembers() {
		return this.members;
	}
	
	@Override
	public void setMembers(HashSet<Student> members) {
		this.members = members;
	}
	
	@Override
	public void addStudent(Student student) {
		if(this.members == null)
			members = new HashSet<Student>();
		members.add(student);
	}
	
	@Override
	public String getName() {
		return this.teamName;
	}
	
	@Override
	public void setName(String name) {
		this.teamName = name;
	}
	
	@Override
	public int getSize() {
		if(this.members == null)
			return 0;
		
		return members.size();
	}
	
	@Override
	public int getTeamScore() {
		return this.teamScore;
	}
	
	@Override
	public void setTeamScore(int teamScore) {
		this.teamScore = teamScore;
	}

}
