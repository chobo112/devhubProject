package com.oracle.team2.dao.MK_Dao;

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


public interface MK_Dao_Interface {

	
	List<User> getintroduction(String loggedInUser);

	List<User> getskill(String loggedInUser);

	List<Skill> allSkill();
	
	void deleteSkill(User_skill user_skill);

	void addSkill(User_skill user_skill);

	void introUpdate(User user);

	void githubUpdate(User user);



	List<ProjectResultDTO> myProjectBoard(String loggedInUser);

	List<Score> myScore(String loggedInUser);

	List<Score> userScore(String loggedInUser);

	/* void updqteMentorScore(Score score); */

	void saveMentorComment(Score scoreComment);

	int getTotalMyprojectCnt(ProjectResultDTO projectResultDTO);

	List<ProjectResultDTO> getMyprojectList(ProjectResultDTO projectResultDTO);

	void insertMentorComment(Score scoreComment);

	List<ProjectResultDTO> getMyProjectAccepted(ProjectResultDTO projectResultDTO);

	int getAcceptedCnt(ProjectResultDTO projectResultDTO);

	List<ProjectResultDTO> payStatus(ProjectResultDTO projectResultDTO);

	void InsertMentorScore(Score score);

	int dataResult(Score score);

	void updateMentorScore(Score score);

	int CommentdataResult(Score scoreComment);

	


	
//	void mentorScoreUpdate(Score mentorScore);





	
}

	

	

	

	

