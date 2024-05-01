package com.oracle.team2.dao.JH_Dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.oracle.team2.model.Alarm;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.Point_log;
import com.oracle.team2.model.Refund;
import com.oracle.team2.model.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JH_DaoImpl implements JH_Dao_Interface {
	
	private final PlatformTransactionManager transactionManager;
	private final SqlSession session;

	@Override
	public List<Point_log> getPointLogList(Point_log point_log) {
		System.out.println("JH_DaoImpl getPointLogList start...");
		List<Point_log> pointList = null;
		try {
			pointList =  session.selectList("jhPointLogList", point_log);
			System.out.println("JH_DaoImpl getPointLogList pointList.size()->"+ pointList.size());
		}catch (Exception e) {
			System.out.println("JH_DaoImpl getPointLogList Exception->"+ e.getMessage());
		}
		return pointList;
	}

	@Override
	public User getUser(String user_id) {
		System.out.println("JH_DaoImpl getUser start...");
		User user = null;
		try {
			user = session.selectOne("jhGetUser", user_id);
			System.out.println("JH_DaoImpl getUser user->"+ user);
		} catch (Exception e) {
			System.out.println("JH_DaoImpl getUser Exception->"+ e.getMessage());
		}
		return user;
	}

	@Override
	public int getTotalPointCnt(Point_log point_log) {
		System.out.println("JH_DaoImpl getTotalPointCnt start...");
		int totalPointCnt = 0;
		try {
			totalPointCnt = session.selectOne("jhTotalPointCnt", point_log);
			System.out.println("JH_DaoImpl getTotalPointCnt totalPointCnt->"+ totalPointCnt);
		} catch (Exception e) {
			System.out.println("JH_DaoImpl getTotalPointCnt Exception->"+ e.getMessage());
		}
		
		return totalPointCnt;
	}

	@Override
	public long getUserPoint(String user_id) {
		System.out.println("JH_DaoImpl getUserPoint start...");
		
		long point = 0L;
		
		try {
			point = session.selectOne("jhUserPoint",user_id);
			System.out.println("JH_DaoImpl getUserPoint point->"+ point);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return point;
	}

	@Override
	public int insertRefund(Refund refund) {
		System.out.println("JH_DaoImpl insertRefund start...");
		int result = 0;

		TransactionStatus txStatus = 
				transactionManager.getTransaction(new DefaultTransactionDefinition());
		
	    try {
			// 두개의 transaction Test 성공과 실패
			// 결론 --> SqlSession 은 하나 실행 할 때마다 자동 Commit
			// Transaction관리는 transactionManager의 getTransaction을가지고 상태따라 설정 
			refund.setPoint_id(session.selectOne("jhNextPointId", refund));
			result = session.insert("jhInsertPointLog", refund);
			System.out.println("JH_DaoImpl insertRefund pointLogResult->"+ result);
	    	result *= session.insert("jhInsertRefund", refund);
	    	System.out.println("JH_DaoImpl insertRefund refundResult->"+ result);
	    	result *= session.update("jhUpdatePointUser", refund);
	    	System.out.println("JH_DaoImpl insertRefund userResult->"+ result);
			transactionManager.commit(txStatus);
			System.out.println("JH_DaoImpl insertRefund result->"+ result);
	    } catch (Exception e) {
	        transactionManager.rollback(txStatus);
	        e.printStackTrace();
			result = 0;
	    }

	    return result;	
	}

	@Override
	public boolean isPossiblePayment(Pay pay) {
		System.out.println("JH_DaoImpl IsPossiblePayment start...");
		boolean isPossible = false;
		try {
			// 유저가 멘토가 아니어야하고, 유저가 해당 어플라이에 수락되어야함, 해당 보드가 모집마감일 안쪽에 있어야함
			int count = session.selectOne("jhIsPossiblePayment", pay);
			// 유저가 해당 결제를 이미 수행한 경우
			int count2 = session.selectOne("jhAlreadyPay",pay);
			if( count != 0 && count2 == 0)
				isPossible = true;
			
			System.out.println("JH_DaoImpl IsPossiblePayment isPossible->"+ isPossible);
		} catch (Exception e) {
			e.printStackTrace();
			isPossible = false;
		}
		
		return isPossible;
	}

	@Override
	public int initPay(Pay pay) {
		System.out.println("JH_DaoImpl initPay start...");
		System.out.println("JH_DaoImpl initPay before pay->"+ pay);
		int result = 0;
		try {
			result = session.insert("jhInsertPay", pay);
			System.out.println("JH_DaoImpl initPay after pay->"+ pay);
			if(result > 0) {
				System.out.println("JH_DaoImpl initPay insert성공");
				Pay insertedPay = session.selectOne("jhGetPayOne", pay);
				pay.setPr_price(insertedPay.getPr_price());
				pay.setPr_title(insertedPay.getPr_title());
				pay.setUser_name(insertedPay.getUser_name());
				System.out.println("JH_DaoImpl initPay select after pay->"+ pay);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		
		return result;
	}

	@Override
	public boolean isFullGroup(Pay pay) {
		System.out.println("JH_DaoImpl IsFullGroup start...");
		boolean result = true;
		try {
			int fullGroupNum = session.selectOne("jhFullGroupNum", pay);
			System.out.println("JH_DaoImpl IsFullGroup fullGroupNum->"+ fullGroupNum);
			int curAcceptedGroupNum = session.selectOne("jhCurAcceptedGroupNum",pay);
			System.out.println("JH_DaoImpl IsFullGroup curAcceptedGroupNum->"+ curAcceptedGroupNum);
			if(curAcceptedGroupNum<fullGroupNum) {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = true;
		}
		System.out.println("JH_DaoImpl IsFullGroup result->"+ result);
		return result;
	}

	@Override
	public Pay getPayByID(String muid) {
		System.out.println("JH_DaoImpl getPayByID start...");
		Pay pay = null;
		try {
			pay = session.selectOne("jhPayById", muid);
			System.out.println("JH_DaoImpl getPayByID pay->"+ pay);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return pay;
	}

	@Override
	public void setPayUpdate(Pay pay) {
		System.out.println("JH_DaoImpl setPayUpdate start...");
		try {
			int result = session.update("jhPayUpdate", pay);
			System.out.println("JH_DaoImpl setPayUpdate result->"+ result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Alarm> recentAlarms(String user_id) {
		System.out.println("JH_DaoImpl recentAlarms start...");
		List<Alarm> alarms = null;
		try {
			alarms = session.selectList("jhRecentAlarms", user_id);
			System.out.println("JH_DaoImpl recentAlarms alarms->"+ alarms);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return alarms;
	}

	@Override
	public int deleteAlarm(Alarm alarm) {
		System.out.println("JH_DaoImpl deleteAlarm start...");
		int result = 0;
		try {
			result = session.delete("jhDeleteAlarm", alarm);
			System.out.println("JH_DaoImpl deleteAlarm 삭제결과->"+ (result>0? "삭제완료":"삭제실패"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int readAlarm(Alarm alarm) {
		System.out.println("JH_DaoImpl readAlarm start...");
		int result =0;
		try {
			result = session.update("jhAlarmRead",alarm);
			System.out.println("JH_DaoImpl readAlarm result->"+ (result>0?"성공":"실패"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Alarm> getAlarmList(Alarm alarm) {
		System.out.println("JH_DaoImpl getAlarmList start...");
		List<Alarm> alarms = null;
		try {
			System.out.println("JH_DaoImpl getAlarmList alarm->"+ alarm);
			alarms = session.selectList("jhAlarmList", alarm);
			System.out.println("JH_DaoImpl getAlarmList alarms->"+ alarms);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return alarms;
	}

	@Override
	public int getTotalPointCnt(Alarm alarm) {
		System.out.println("JH_DaoImpl getTotalPointCnt start...");
		int totAlarmCnt = 0;
		try {
			totAlarmCnt = session.selectOne("jhAlarmTotalCnt", alarm);
			System.out.println("JH_DaoImpl getTotalPointCnt totAlarmCount->"+ totAlarmCnt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return totAlarmCnt;
	}

	 
	
}
