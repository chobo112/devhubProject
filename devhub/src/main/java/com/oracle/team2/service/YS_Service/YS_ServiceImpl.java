package com.oracle.team2.service.YS_Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.team2.controller.YS_Controller;
import com.oracle.team2.dao.YS_Dao.YS_Dao_Interface;
import com.oracle.team2.model.Region;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YS_ServiceImpl implements YS_Service_Interface {
	private final YS_Dao_Interface ysd;
	// 회원 가입
	@Override
	public int ysinsert(User user) {
		int insertResult = 0;
		System.out.println("YS_ServiceImpl ysinsert start");
		insertResult = ysd.insertUser(user);
		
		return insertResult;
	}
	// 지역 리스트 select
	@Override
	public List<Region> getRegionListAll() {
		System.out.println("YS_ServiceImpl getRegionListAll start");
		List<Region> regionList = ysd.getRegionListAll();
		System.out.println("YS_ServiceImpl getRegionListAll regionList->" + regionList);
		return regionList;
	}
	// 스킬 리스트 select
	@Override
	public List<Skill> getSkillListAll() {
		System.out.println("YS_ServiceImpl getSkillListAll start");
		List<Skill> skillList = ysd.getSkillListAll();
		System.out.println("YS_ServiceImpl getSkillListAll skillList ->" + skillList);
		return skillList;
	}

	
	// 로그인
	@Override
	public User login(User user) {
		System.out.println("YS_ServiceImpl login  start");
		User loginResult = ysd.login(user);
		System.out.println("YS_ServiceImpl loginResult->"+loginResult);
		return loginResult;
	}
	// 아이디 체크
	@Override
	public int chkId(String userId) {
		int idChkResult = ysd.chkId(userId);
		return idChkResult;
	}

	// 체크 닉네임
	@Override
	public int chkName(String userName) {
		int nameChkResult = ysd.chkName(userName);
		return nameChkResult;
	}

	// 수정 전 조회
	@Override
	public User findUser(String user_id) {
		System.out.println("YS_ServiceImpl findUser  start" );
		User findUser = ysd.findUser(user_id);
		return findUser;
	}

	
	// modify 에서 user_name 비교 
	@Override
	public int modifyChkName(String modifyUserName) {
		int resultChkName = ysd.modifyChkName(modifyUserName);
		return resultChkName;
	}
	// 회원 정보 수정 
	@Override
	public int modifyUpdate(User user) {
		System.out.println("YS_ServiceImpl modifyUpdate start");
		System.out.println("YS_ServiceImpl modifyUpdate user start" + user);
		int resultUpdate = ysd.modifyUpdate(user);
		
		return resultUpdate;
	}

	
	@Override
	public String withdrawUser(User user) {
		System.out.println("YS_ServiceImpl withdrawUser start");
		
		String delstutas = ysd.withdrawUser(user);
		
		return delstutas;
	}
	@Override
	public String updateTempPwd(User user, String tempPassword) {
		System.out.println("YS_ServiceImpl updateTempPwd start");
		
		String tempPwdEmail = ysd.updateTempPwd(user,tempPassword);
		
		return tempPwdEmail;
	}
	
	

	


}
