package com.oracle.team2.service.YS_Service;

import java.util.List;

import com.oracle.team2.model.Region;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;

public interface YS_Service_Interface {
	// 회원정보 insert 
	int 	ysinsert	(User user);
	// 지역 리스트 
	List	<Region> 	getRegionListAll();
	// 스킬리스트
	List	<Skill> 	getSkillListAll();
	// 로그인
	User 	login		(User user);
	// 회원가입 ajax Id 비교
	int 	chkId		(String userId);
	// 회원가입 ajax Name 비교
	int 	chkName		(String userName);
	// modify 에서 수정전 유저정보를 뿌려줌
	User 	findUser	(String user_id);
	// modify 에서 수정전 현 Name ajax 에서 비교
	int 	modifyChkName(String modifyUserName);
	// 회원정보 수정
	int modifyUpdate(User user);
	// 회원 탈퇴 update 
	String withdrawUser(User user);
	// 이메일으로 임시비밀번호 update 
	String updateTempPwd(User user, String tempPassword);
	
	

}
