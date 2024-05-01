package com.oracle.team2.dao.MK_Dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.oracle.team2.model.Alarm;
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

@Repository
@RequiredArgsConstructor
public class MK_DaoImpl implements MK_Dao_Interface {

	private final SqlSession session;
 
	 // 자기 소개 조회
	@Override
	public List<User> getintroduction(String loggedInUser) {
		List<User> getintroduction = null;
		System.out.println("mk getintroduction dao start");
		try {
			getintroduction = session.selectList("getintroduction", loggedInUser);
			System.out.println("getintroduction ->" + getintroduction);
		} catch (Exception e) {
			System.out.println("에러 메세지" + e.getMessage());
		}
		return getintroduction;
	}

	//내 스킬 조회
	@Override
	public List<User> getskill(String loggedInUser) {
		List<User> getskill = null;
		System.out.println("MK getskill dao start");
		try {
			getskill = session.selectList("getskill", loggedInUser);

		} catch (Exception e) {
			System.out.println("에러 메세지" + e.getMessage());
		}
		return getskill;
	}

	//모든 스킬 조회 
	@Override
	public List<Skill> allSkill() {
		List<Skill> allSkill = null;
		System.out.println("MK allSkill dao start");
		try {
			allSkill = session.selectList("allSkill");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("에러 메세지" + e.getMessage());
		}
		return allSkill;
	}

	//스킬 추가
	@Override
	public void addSkill(User_skill user_skill) {

		try {
			System.out.println("MK_DaoImpl addSkill skillId->" + user_skill);
			session.insert("insertSkill", user_skill);
			System.out.println("저장완료");
		} catch (Exception e) {
			System.out.println("add skill 에러메세지->" + e.getMessage());
		}
	}
 
	//자기소개 업데이트
	@Override
	public void introUpdate(User user) {

		try {
			System.out.println("MK_DaoImpl introUpdate->" + user);
			session.update("introUpdate", user);
		} catch (Exception e) {
			System.out.println("updateUserIntro->" + e.getMessage());
		}

	}

	//깃허브 업데이트
	@Override
	public void githubUpdate(User user) {
		try {
			System.out.println("MK_DaoImpl githubUpdate->" + user);
			session.update("githubUpdate", user);
		} catch (Exception e) {
			System.out.println("githubUpdate->" + e.getMessage());
		}

	}

	//스킬 삭제
	@Override
	public void deleteSkill(User_skill user_skill) {
		int result = 0;

		try {
			System.out.println("MK_DaoImpl deleteSkill->" + user_skill);
			result = session.delete("deletetSkill", user_skill);
			System.out.println("MK_DaoImpl deleteSkill result->" + result);
		} catch (Exception e) {
			System.out.println("delete 에러메세지->" + e.getMessage());
		}

	}

	//내가 지원한 프로젝트 카드 
	@Override
	public List<ProjectResultDTO> myProjectBoard(String loggedInUser) {
		List<ProjectResultDTO> myProjectBoard = null;
		try {

			myProjectBoard = session.selectList("MyProject", loggedInUser);
			System.out.println("mk_daoImpl->" + myProjectBoard);
		} catch (Exception e) {
			System.out.println("MyProject 조회 " + e.getMessage());
		}
		return myProjectBoard;
	}

	//내 점수 조회 
	@Override
	public List<Score> myScore(String loggedInUser) {
		List<Score> myScore = null;
		try {
			myScore = session.selectList("MyScore", loggedInUser);
			System.out.println("mk_daoImpl->" + myScore);
		} catch (Exception e) {
			System.err.println("myScore 조회" + e.getMessage());
		}
		return myScore;
	}

	//user 평가 조회 
	@Override
	public List<Score> userScore(String loggedInUser) {
		List<Score> userScore = null;
		try {
			userScore = session.selectList("userScore", loggedInUser);
			System.out.println("mk_dao_impl->" + userScore);
		} catch (Exception e) {
			System.out.println("userScore 조회" + e.getMessage());
		}
		return userScore;
	}

	//멘토 점수 insert
	@Override
	public void InsertMentorScore(Score score) {
		try {
			System.out.println("MK_DaoImpl MentorScoreUpdate->" + score);
			session.insert("MentorScoreinsert", score);
		} catch (Exception e) {
			System.out.println("MentorScoreinsert->" + e.getMessage());
		}
		
	}

	//mentor 코멘트 수정
	@Override
	public void saveMentorComment(Score scoreComment) {
		try {
			System.out.println("MK_DaoImpl MentorCommentUpdate->" + scoreComment);
			session.update("MentorCommentUpdate", scoreComment);
		} catch (Exception e) {
			System.out.println("MentorCommentUpdate->" + e.getMessage());
		}
		
	}

	//카드 총 조회 
	@Override
	public int getTotalMyprojectCnt(ProjectResultDTO projectResultDTO) {
		System.out.println("MK_DaoImpl getTotalPointCnt start...");
		int totMyprojectCnt = 0;
		try {
			totMyprojectCnt = session.selectOne("MyProjectCnt", projectResultDTO);
			System.out.println("MK_DAO_IMPL getTotalPointCnt totCount->"+ totMyprojectCnt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return totMyprojectCnt;
	}

	//카드 리스트 조회 
	@Override
	public List<ProjectResultDTO> getMyprojectList(ProjectResultDTO projectResultDTO) {
		System.out.println("MK_DaoImpl getMyProjectList start...");
		List<ProjectResultDTO> projectResultDTOs = null;
		try {
			System.out.println("MK_DaoImpl ProjectResultDto->"+ projectResultDTO);
			projectResultDTOs = session.selectList("MyProjectss", projectResultDTO);
			System.out.println("MK_DaoImpl ProjectResultDtos->"+ projectResultDTOs);
			System.out.println("MK_DaoImpl projectResultDTOs.size()->"+ projectResultDTOs.size());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return projectResultDTOs;
	}

	//코멘트 insert
	@Override
	public void insertMentorComment(Score scoreComment) {
		try {
			System.out.println("MK_DaoImpl MentorCommentUpdate->" + scoreComment);
			session.insert("MentorCommentInsert", scoreComment);
			System.out.println("insertComment-->"+scoreComment);
		} catch (Exception e) {
			System.out.println("MentorCommentUpdate->" + e.getMessage());
		}
		
	}

	
	//수락된 프로젝트 카드 조회 
	@Override
	public List<ProjectResultDTO> getMyProjectAccepted(ProjectResultDTO projectResultDTO) {
		List<ProjectResultDTO> projectResultDTOs = null;
		try { 
			System.out.println("MK_DaoImpl accepted result->"+projectResultDTO);
			projectResultDTOs =session.selectList("acceptedProject", projectResultDTO);
			}catch (Exception e) {
				e.printStackTrace();
			}
		return projectResultDTOs;
	}

	//수락된 카드 조회 카운트 
	@Override
	public int getAcceptedCnt(ProjectResultDTO projectResultDTO) {
		System.out.println("MK_DaoImpl getAcceptedCnt start...");
		int totMyprojectCnt = 0;
		try {
			totMyprojectCnt = session.selectOne("getAcceptedCnt", projectResultDTO);
			System.out.println("MK_DAO_IMPL getTotalPointCnt totCount->"+ totMyprojectCnt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return totMyprojectCnt;
	}

	// 결제상황 
	@Override
	public List<ProjectResultDTO> payStatus(ProjectResultDTO projectResultDTO) {
		System.out.println("MK_DaoIMPL payStatus start");
		List<ProjectResultDTO> payStatus = null;
		try {
			payStatus= session.selectList("payStatus", payStatus);
			System.out.println("MK_DAO_IMPL payStatus"+ payStatus);
		}catch (Exception e) {
		   e.printStackTrace();
		}
		return payStatus;
	}

	// 평가 결과 조회 
	@Override
	public int dataResult(Score score) {
		int resultCount = session.selectOne("resultCount",score);
		return resultCount;
	}

	// 멘토 점수 업데이트 
	@Override
	public void updateMentorScore(Score score) {
		try {
			System.out.println("MK_DaoImpl MentorScoreUpdate->" + score);
			session.update("MentorScoreUpdate", score);
		} catch (Exception e) {
			System.out.println("MentorScoreUpdate->" + e.getMessage());
		}
		
	}

	@Override
	public int CommentdataResult(Score scoreComment) {
		int resultCommentCount = session.selectOne("resultCommentCount",scoreComment);
		return resultCommentCount;
	}


}


//	}


