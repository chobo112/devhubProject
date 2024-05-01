package com.oracle.team2.service.MK_Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.oracle.team2.controller.MK_Controller;
import com.oracle.team2.dao.MK_Dao.MK_Dao_Interface;
import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.ProjectResultDTO;
import com.oracle.team2.model.Score;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;
import com.oracle.team2.model.User_skill;

import lombok.RequiredArgsConstructor;
import oracle.net.aso.b;


@Service
@RequiredArgsConstructor
public class MK_ServiceImpl implements MK_Service_Interface {
	private final MK_Dao_Interface mk_Dao_Interface;

	// 자기소개

	@Override
	public List<User> getintroduction(String loggedInUser) {
		List<User> getintroduction = null;
		System.out.println("MkServiceimpl start");
		getintroduction = mk_Dao_Interface.getintroduction(loggedInUser);
		System.out.println("serviceImpl getintroduction.size()->" + getintroduction.size());
		return getintroduction;
	}

	// user_id에 대한 스킬 불러오기

	@Override
	public List<User> getskill(String loggedInUser) {
		List<User> getskill = null;
		System.out.println("MK Serviceimpl getskill start");
		getskill = mk_Dao_Interface.getskill(loggedInUser);
		System.out.println("serviceimpl getskill.size()->" + getskill.size());
		return getskill;
	}

	// skill 모두 불러오기

	@Override
	public List<Skill> allSkList() {
		List<Skill> allSkills = null;
		allSkills = mk_Dao_Interface.allSkill();
		System.out.println("serviceimpl allskill.size()->" + allSkills.size());

		return allSkills;
	}

	// 스킬추가

	@Override
	public void addSkills(String loggedInUser, String[] skillIds) {
	    for (String skillId : skillIds) {
	        // skilId를 이용하여 새로운 User_skill 객체를 생성하고 데이터베이스에 저장하는 작업
	        User_skill user_skill = new User_skill();
	        user_skill.setSkill_id(skillId);
	        user_skill.setUser_id(loggedInUser);
	        System.out.println("service impl" + user_skill);

	        // repository를 사용하여 데이터베이스에 저장
	        mk_Dao_Interface.addSkill(user_skill);
	    }
	}

	//선택한 스킬 삭제 
	@Override
	public void deleteSkill(String loggedInUser,String skill_id) {
		// skilId를 이용하여 새로운 User_skill 객체를 생성하고 데이터베으시에 저장하는 작업

		User_skill user_skill=new User_skill();
		user_skill.setSkill_id(skill_id);
		user_skill.setUser_id(loggedInUser);
		System.out.println("service impl" + user_skill);

		// repository를 사용하여 데이터베이스에 저장
		mk_Dao_Interface.deleteSkill(user_skill);

	}

	//자기소개 수정

	@Override
	public void introUpdate(String loggedInUser,String introData) {

		User user = new User();
		user.setUser_intro(introData);
		user.setUser_id(loggedInUser);
		System.out.println("service impl" + user);

		mk_Dao_Interface.introUpdate(user);

	}

	// 깃허브 수정
	@Override
	public void githubUpdate(String loggedInUser,String gitData) {

		User user = new User();
		user.setGithub(gitData);
		user.setUser_id(loggedInUser);
		System.out.println("service impl" + user);
		mk_Dao_Interface.githubUpdate(user);

	}

	//프로젝트 게시판 카드부분 데이터 조회 
	@Override
	public List<ProjectResultDTO> myProjectBoard(String loggedInUser) {
		List<ProjectResultDTO> myProjectBoard = null;
		myProjectBoard = mk_Dao_Interface.myProjectBoard(loggedInUser);
		System.out.println("mk_dao_interface start->" + loggedInUser);
		return myProjectBoard;
	}
	
	//내 평균점수 데이터 
	@Override
	public List<Score> myScore(String loggedInUser) {
		List<Score> myScore = null;
		myScore = mk_Dao_Interface.myScore(loggedInUser);
		System.out.println("MK_SERIVICE_IMPL start->" + myScore);
		return myScore;
	}
	
	//user가 평가한 데이터 
	@Override
	public List<Score> userScore(String loggedInUser) {
		List<Score> userScore = null;
		userScore = mk_Dao_Interface.userScore(loggedInUser);
		System.out.println("MK_SERVICE_IMPL start->" + userScore);
		return userScore;
	}

   // 페이징 처리를 위한 카트 토탈 조회 
	@Override
	public int getTotalCardCnt(ProjectResultDTO projectResultDTO) {
		int totMyprojectCards = 0;
		System.out.println("MK Service getTotalCardCnt start");
		if( projectResultDTO.getAp_acceptPage() == null ||"0".equals(projectResultDTO.getAp_acceptPage())) {
			totMyprojectCards = mk_Dao_Interface.getTotalMyprojectCnt(projectResultDTO);
			System.out.println("MK_ServiceImpl getMyprojectList size->"+ totMyprojectCards);
		} else {
			totMyprojectCards =mk_Dao_Interface.getAcceptedCnt(projectResultDTO);
			System.out.println("MK_ServiceImpl getAcceptedCnt size->"+totMyprojectCards);
			System.out.println("ap_accept 수락된 결과 조회 ");
		}
			
		return totMyprojectCards;
	}
   
	// 내가 지원한 프로젝트 카드 리스트 조회 
	@Override
	public List<ProjectResultDTO> getMyprojectList(ProjectResultDTO projectResultDTO) {
		System.out.println("MK_ServiceImpl getMyProjectList start");
	
		List<ProjectResultDTO> projectResultDTOs =null;
		
		
		if( projectResultDTO.getAp_acceptPage() == null ||"0".equals(projectResultDTO.getAp_acceptPage())) {
			
			projectResultDTOs =mk_Dao_Interface.getMyprojectList(projectResultDTO);
			System.out.println("ap_acceptPage 0이면 전체 리스트 출력 ");
		
		} else {
			
			System.out.println("ap_accept 수락된 결과 조회 ");
		    projectResultDTOs =mk_Dao_Interface.getMyProjectAccepted(projectResultDTO);
			System.out.println(projectResultDTOs.size());
			System.out.println("수락된 리스트 조회->"+projectResultDTOs.size());
			System.out.println("projectResutDTOs->"+projectResultDTOs);
			System.out.println("MK_Service getMyProjectList 수락되었을 때  ->" +projectResultDTO.getAp_acceptPage());
		
		}
		return projectResultDTOs;
	}


	
	   //멘토 점수 저장 
		@Override
		public void saveMentorScore(Score score) {
			
			/* user.setUser_id(loggedInUser); */
			System.out.println("service impl saveMentorScore" + score);

			mk_Dao_Interface.InsertMentorScore(score);
			
		}
	 
		// 멘토 코멘트 저장 
		@Override
		public void saveMentorComment(Score scoreComment) {
			System.out.println("service impl seveMentorComment"+scoreComment);
			
			mk_Dao_Interface.saveMentorComment(scoreComment);

			
		}


	//점수 데이터 유무 조회 
	@Override
	public int dataResult(Score score) {
		System.out.println("Service Impl dataResult start");
		int count =mk_Dao_Interface.dataResult(score);
		System.out.println("count--->"+count);
		return count;
	}

	//점수 수정 
	@Override
	public void updateMentorScore(Score score) {
		System.out.println("Service Impl update Mentor Score start");
		
		System.out.println("service impl saveMentorScore" + score);

		mk_Dao_Interface.updateMentorScore(score);
		
		
	}
	
	
	//멘토 코멘트 삽입
	@Override
	public void insertMentorComment(Score scoreComment) {
		System.out.println("service impl seveMentorComment"+scoreComment);
		
		mk_Dao_Interface.insertMentorComment(scoreComment);
		
	}

	//pay_status
	@Override
	public List<ProjectResultDTO> payStatus(ProjectResultDTO projectResultDTO) {
		System.out.println("Service Impl payStatus"+projectResultDTO);
		
		List<ProjectResultDTO> payStatus=null;
		
		payStatus=mk_Dao_Interface.payStatus(projectResultDTO);
		return payStatus;
	}

	@Override
	public int CommentdataResult(Score scoreComment) {
		System.out.println("Service Impl CommentdataResultt start");
		int Commentdatacount =mk_Dao_Interface.CommentdataResult(scoreComment);
		System.out.println("count--->"+Commentdatacount);
		return Commentdatacount;
	}





//	@Override
//	public void saveMentorScore(Long boardId, int scScore, String loggedInUser) {
//		Score mentorScore = new Score();
//		mentorScore.setBoard_id(boardId);
//		mentorScore.setSc_score(scScore);
//		mentorScore.setUser_id(loggedInUser);
//		System.out.println("mentorScore"+mentorS);
//		mk_Dao_Interface.mentorScoreUpdate(mentorScore);
//	}


	// 멘토 평가 저장 


}
