package com.oracle.team2.dao.YS_Dao;

import java.util.List;

import com.oracle.team2.model.Region;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;

public interface YS_Dao_Interface {
	// 회원가입
	int 	insertUser	(User user);
	// 회원가입전 지역정보 
	List	<Region> 	getRegionListAll();
	// 회원가입 전 스킬정보
	List	<Skill> 	getSkillListAll();
	// 로그인
	User 	login		(User user);
	// 회원가입 ID 비교
	int 	chkId		(String userId);
	// 회원가입 Name 비교
	int 	chkName		(String userName);
	// 회원정보 수정 전 현아이디 정보 조회
	User findUser(String user_id);
	// 회원정보 수정 다른 닉네임이 있는지 확인
	int modifyChkName(String modifyUserName);
	// 회원정보 수정 
	int modifyUpdate(User user);
	// 회원 탈퇴
	String withdrawUser(User user);
	// 임시 비밀번호 
	String updateTempPwd(User user, String tempPassword);
	

}
