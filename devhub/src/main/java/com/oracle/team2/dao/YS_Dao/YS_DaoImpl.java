package com.oracle.team2.dao.YS_Dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.oracle.team2.model.Region;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;
import com.oracle.team2.model.User_skill;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class YS_DaoImpl implements YS_Dao_Interface {

	private final SqlSession session;
	private final PlatformTransactionManager transactionManager; // 한로직당 동시에 사용 할 경우

	// 회원가입
	@Override
	public int insertUser(User user) {
		int insertResult = 0;
		System.out.println("YS_DaoImpl insertUser start");
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

		System.out.println("인서트1:" + user.toString());
		try {
			insertResult = session.insert("ysinsertUser", user); // 회원 정보 인서트
			System.out.println("인서트1:" + insertResult);
			String[] skillList = user.getSkill_id();
			for (int i = 0; i < skillList.length; i++) { // 해당 user_tbl에 컬럼이 없으므로 join 테이블에 n값을 붙여서 인서트됨
				User_skill tmpSkill = new User_skill();
				tmpSkill.setUser_id(user.getUser_id());
				tmpSkill.setSkill_id(skillList[i]);
				System.out.println("인서트" + i + ":" + insertResult);

				insertResult = insertResult + session.insert("ysinsertSkill", tmpSkill);
			}

			transactionManager.commit(txStatus);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			System.out.println("YS_DaoImpl getRegionListAll Exception->" + e.getMessage());
		}

		return insertResult;
	}// 회원가입

	// 지역 리스트
	@Override
	public List<Region> getRegionListAll() {
		System.out.println("YS_DaoImpl getRegionListAll start");
		List<Region> regionList = null;
		try {
			regionList = session.selectList("ysRegionAll");
			System.out.println("YS_DaoImpl getRegionListAll regionList size->" + regionList.size());
		} catch (Exception e) {
			System.out.println("YS_DaoImpl getRegionListAll Exception->" + e.getMessage());
		}
		return regionList;
	}

	// 스킬리스트
	@Override
	public List<Skill> getSkillListAll() {
		System.out.println("YS_DaoImpl getSkillListAll start");
		List<Skill> skillList = null;
		try {
			skillList = session.selectList("ysSkillAll");
			System.out.println("YS_DaoImpl getSkillListAll skillList.size() ->" + skillList.size());
		} catch (Exception e) {
			System.out.println("YS_DaoImpl getSkillListAll e.getMessage()->" + e.getMessage());
		}
		return skillList;
	}// 스킬리스트

	// 로그인
	@Override
	public User login(User user) {
		System.out.println("YS_DaoImpl login Start...");

		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		User loginResult = null;
		try {
			// 로그인 시도
			// user가 있으면 로그인
			loginResult = session.selectOne("ysLogin", user);

			// 로그인 정보가 없을 경우 update
			if (loginResult == null) { // 아이디 있는 경우 로그인카운터가 1씩오름 
				System.out.println("----1----");
				session.update("userLoginFail", user);
				System.out.println("----2----");
				loginResult = session.selectOne("YSgetid", user); //유저가 누구인지 알아야 update 되기때문에 select 먼저해줌
				if (loginResult.getUser_id() == null) {
					// 아이디도 없는 경우 로그인 실패
					return null;
				}
				System.out.println("YS_DaoImpl login user.getLogin_cnt() ->" + loginResult.getLogin_cnt()); //로그인 시도횟수
				loginResult.setResultYN(false);
			} else if (loginResult.getLogin_cnt() >= 5) { // 로그인 카운터가 5 이상이여도 로그인 실패
				loginResult.setResultYN(false);
			} else { // 로그인 성공시 로그인카운터 초기화
				System.out.println("----3----");
				session.update("userSuccese", user); 
				System.out.println("----4----");
				User user3 = session.selectOne("YSgetid", user);
				System.out.println("YS_DaoImpl login user.getLogin_cnt() ->" + user3.getLogin_cnt());
				loginResult.setResultYN(true);
			}
			transactionManager.commit(txStatus);
			System.out.println("YS_DaoImpl loginResult->" + loginResult);

		} catch (Exception e) {
			System.out.println("YS_DaoImpl login Exception->" + e.getMessage());
			transactionManager.rollback(txStatus);
		}
		return loginResult;
	}// 로그인

	// 아이디 중복 체크
	@Override
	public int chkId(String userId) {
		int chkIdResult = 0;
		try {
			System.out.println("YS_DaoImpl chkId  start->" + userId);
			User result = session.selectOne("chkId", userId);
			System.out.println("YS_DaoImpl chkId result start" + result);
			if (result != null) {
				chkIdResult = 1;
				System.out.println("YS_DaoImpl loginResult->" + chkIdResult);
			}
		} catch (Exception e) {
			System.out.println("YS_DaoImpl login Exception->" + e.getMessage());
			e.printStackTrace();
		}
		return chkIdResult;
	}// 아이디 중복 체크

	// ajax 닉네임 체크
	@Override
	public int chkName(String userName) {
		int chkNameResult = 0;
		try {
			System.out.println("YS_DaoImpl chkId  start->" + userName);
			User result = session.selectOne("chkName", userName);
			System.out.println("YS_DaoImpl chkId result start" + result);
			if (result != null) {
				chkNameResult = 1;
				System.out.println("YS_DaoImpl chkNameResult->" + chkNameResult);
			}
		} catch (Exception e) {
			System.out.println("YS_DaoImpl chkNameResult Exception->" + e.getMessage());
			e.printStackTrace();
		}
		return chkNameResult;
	}// ajax 닉네임 체크

	// 개인 정보 수정 정보 화면에 뿌려줌
	@Override
	public User findUser(String user_id) {
		System.out.println("YS_DaoImpl findUser user_id start" + user_id);
		User findUser = null;
		try {
			findUser = session.selectOne("ysdetailmodify", user_id);

		} catch (Exception e) {
			System.out.println("YS_DaoImpl userUpdate e.getMessage()=>" + e.getMessage());
		}
		return findUser;

	}// 개인 정보 수정 정보 화면에 뿌려줌

	// modify 회원정보 수정 전 다른 닉네임 있는지 ajax 확인
	@Override
	public int modifyChkName(String modifyUserName) {
		int chkNameResult = 0;
		try {
			System.out.println("YS_DaoImpl modifyChkName  start->" + modifyUserName);
			User result = session.selectOne("modifyChkName", modifyUserName);
			System.out.println("YS_DaoImpl chkName result start" + result);
			if (result != null) {
				chkNameResult = 1;
				System.out.println("YS_DaoImpl modifyChkName->" + chkNameResult);
			}
		} catch (Exception e) {
			System.out.println("YS_DaoImpl modifyChkName Exception->" + e.getMessage());

		}
		return chkNameResult;
	}// modify 회원정보 수정 전 다른 닉네임 있는지 ajax 확인

	// 회원정보 수정
	@Override
	public int modifyUpdate(User user) {
		int modifyUpdate = 0;
		System.out.println("YS_DaoImpl modifyUpdate user start" + user);
		try {
			modifyUpdate = session.update("updateUser", user);
		} catch (Exception e) {
			System.out.println("YS_DaoImpl modifyUpdate e.getMessage()->" + e.getMessage());
		}
		return modifyUpdate;
	}// 회원정보 수정

	// 회원 탈퇴
	@Override
	public String withdrawUser(User user) {
		String delStatus = null;
		System.out.println("YS_DaoImpl withdrawUser start");

		try {
			int result = session.update("yswithdraw", user);
			if (result > 0) {
				delStatus = "success"; // 성공적으로 탈퇴한 경우
			} else {
				delStatus = "fail"; // 탈퇴에 실패한 경우
			}
		} catch (Exception e) {
			System.out.println("YS_DaoImpl withdrawUser e.getMessage()->" + e.getMessage());
			delStatus = "error"; // 예외 발생 시
		}
		return delStatus;
	}// 회원탈퇴

	// 임시 비밀번호
	@Override
	public String updateTempPwd(User user, String tempPassword) {
		User foundUser = null;
		System.out.println("YS_DaoImpl updateTempPwd start");
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			foundUser = session.selectOne("ystempfinduser", user); // 아이디와 이메일로 유저확인
			System.out.println("사용자 확인" + foundUser);
			if (foundUser != null) {// 사용자가 존재하면 임시비밀번호 update
				foundUser.setUser_pw(tempPassword); // 임시비밀번호를 해당 아이디에 set 해줌
				System.out.println("set 후 값 보기 : " + foundUser);
				int updateCnt = session.update("updateUserTempPassword", foundUser); // update
				System.out.println("update 후 updateCnt: " + updateCnt);
			} else {
				throw new RuntimeException("사용자가 존재하지 않습니다.");
			}
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			System.out.println("YS_DaoImpl updateTempPwd e.getMessage() ->" + e.getMessage());
			transactionManager.rollback(txStatus);
		}
		return foundUser.getUser_id();
	}// 임시비밀번호

}// class
