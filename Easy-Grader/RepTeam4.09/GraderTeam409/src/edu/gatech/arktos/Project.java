package edu.gatech.arktos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Project implements ProjectInterface{
	private String projectName;
	private String projDescription;
	private int averageGrade;
	private HashMap<String, Integer> TeamList = new HashMap<String, Integer>();
	private HashSet<Team> teams = new HashSet<Team>();

	public Project() {
		this.projectName = null;
		this.projDescription = null;
		this.averageGrade = 0;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getAverageGrade() {
		return averageGrade;
	}

	public void setAverageGrade(int averageGrade) {
		this.averageGrade = averageGrade;
	}

	public HashMap<String, Integer> getTeamList() {
		return TeamList;
	}

	public void setTeamList(HashMap<String,Integer> TeamList) {
		this.TeamList = TeamList;
	}

	@Override
	public String getProjectDescription() {
		return this.projDescription;
	}

	@Override
	public void setProjectDescription(String projectDescription) {
		this.projDescription = projectDescription;
	}

	@Override
	public Team getTeamByName(String teamName) {
		Iterator<Team> it = this.teams.iterator();
		while(it.hasNext()){
			Team team = it.next();
			if(team.getName().equals(teamName))
				return team;
		}
		return null;
	}

	@Override
	public int getAverageScore() {
		return this.averageGrade;
	}

	@Override
	public void addTeam(Team team) {
		this.teams.add(team);
	}

	@Override
	public void setTeams(HashSet<Team> teams) {
		this.teams = teams;
	}

	@Override
	public HashSet<Team> getTeams() {
		return this.teams;
	}
}