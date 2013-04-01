package edu.gatech.arktos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Student {
	private String name;
	private String gtid;
	private String email;
	private Object attendance;
	private ArrayList<StudentProject> StudentProjects;
	private ArrayList<StudentAssignment> StudentAssignments;

	public Student(){
		this.name = null;
		this.gtid = null;
		this.email = null;
		this.attendance = null;
		this.StudentProjects = null;
		this.StudentAssignments=null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGtid() {
		return gtid;
	}

	public void setGtid(String gtid) {
		this.gtid = gtid;
	}

	public Object getAttendance() {
		return attendance;
	}

	public void setAttendance(Object attendance) {
		this.attendance = attendance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<StudentAssignment> getInvolvedAssignments(){
		return StudentAssignments;
	}
	
	public void setInvolvedAssignment(ArrayList<StudentAssignment> StudentAssignments){
		this.StudentAssignments = StudentAssignments;
	}
	
    public ArrayList<StudentProject> getInvolvedProjects(){
    	return StudentProjects;
    }
    
    public void setInvolvedProjects(ArrayList<StudentProject> StudentProjects){
    	this.StudentProjects = StudentProjects;
    }

	public void saveInfoToFile() throws IOException{
		File file = new File(this.getName()+".txt");
		FileWriter fw = new FileWriter(file);
		fw.write(this.toString());
		fw.close();
	}
	
	
	public String readInfoFromFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(this.getName()+".txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        String text = sb.toString();
	        return text;
	    } finally {
	        br.close();
	    }
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("NAME: " + this.getName() + "\n");
		builder.append("GTID: " + this.getGtid() + "\n");
		builder.append("EMAIL: " + this.getEmail() + "\n");
		builder.append("ATTENDANCE: " + this.getAttendance() + "\n");
		builder.append("PROJECT DETAILS:\n");
		
		int i = 0,j =0;
		ArrayList<StudentAssignment> sa = new ArrayList<StudentAssignment>();
		ArrayList<StudentProject> sp = new ArrayList<StudentProject>();
		sp = getInvolvedProjects();
		if(sp != null){
			while(j < sp.size() && sp.get(j)!=null){
				StudentProject proj = sp.get(j);
				builder.append("- " + proj.getProjectName() + "\n");
				builder.append("  Team Grade: " + proj.getTeamGrade() + "\n");
				builder.append("  Project Average Grade: " + proj.getAverageGrade() +"\n");
				builder.append("  Individual Contribution: " + proj.getIndEvaluation() + "\n");
				j++;
			}
		}
		
		builder.append("ASSIGNMENT DETAILS:\n");
		sa = this.getInvolvedAssignments();
		if(sa != null){
			while( i< sa.size() && sa.get(i)!=null){
				StudentAssignment ass = sa.get(i);
				builder.append("- " + "Assignment " + ass.getAssignmentNumber() + "\n");
				builder.append("  Grade: " + ass.getGrade() + "\n");
				builder.append("  Assignment Average Grade: " + ass.getAverageGrade() + "\n");
				i++;
			}
		}
	
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj1){
		Student newSt = (Student) obj1;
		return this.name.equals(newSt.getName()) && this.gtid.equals(newSt.getGtid());
		
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode() + this.gtid.hashCode();
	}
	
}
