package com.oracle.team2.service.MK_Service;

import java.util.List;

import org.springframework.ui.Model;

import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.ProjectResultDTO;
import com.oracle.team2.model.Score;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;
import com.oracle.team2.model.User_skill;

public interface MK_Service_Interface {

	List<User> getintroduction(String loggedInUser);

	List<User> getskill(String loggedInUser);

	List<Skill> allSkList();

	void addSkills(String loggedInUser, String[] skillIds);


	void introUpdate(String loggedInUser,String userIntro);

	void githubUpdate(String loggedInUser,String gitData);

	void deleteSkill(String loggedInUser,String skill_id);


	List<ProjectResultDTO> myProjectBoard(String loggedInUser);

	List<Score> myScore(String loggedInUser);

	List<Score> userScore(String loggedInUser);


	void saveMentorScore(Score score);

	void saveMentorComment(Score scoreComment);

	int getTotalCardCnt(ProjectResultDTO projectResultDTO);

	List<ProjectResultDTO> getMyprojectList(ProjectResultDTO projectResultDTO);

	void insertMentorComment(Score scoreComment);

	List<ProjectResultDTO> payStatus(ProjectResultDTO projectResultDTO);



	void updateMentorScore(Score score);

	int dataResult(Score score);

	int CommentdataResult(Score scoreComment);





	
	




}
