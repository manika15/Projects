package edu.gatech.arktos;

import java.util.HashSet;

public interface ProjectInterface {
	public String getProjectName();
	public void setProjectName(String projectName);
	public String getProjectDescription();
	public void setProjectDescription(String projectDescription);
	public Team getTeamByName(String teamName);
	public int getAverageScore();
	public void addTeam(Team team);
	public void setTeams(HashSet<Team> teams);
	public HashSet<Team> getTeams();
}