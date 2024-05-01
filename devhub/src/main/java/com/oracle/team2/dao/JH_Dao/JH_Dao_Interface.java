package com.oracle.team2.dao.JH_Dao;

import java.util.List;

import com.oracle.team2.model.Alarm;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.Point_log;
import com.oracle.team2.model.Refund;
import com.oracle.team2.model.User;

public interface JH_Dao_Interface {

	List<Point_log> getPointLogList(Point_log point_log);

	User getUser(String user_id);

	int getTotalPointCnt(Point_log point_log);

	long getUserPoint(String user_id);

	int insertRefund(Refund refund);

	boolean isPossiblePayment(Pay pay);

	int initPay(Pay pay);

	boolean isFullGroup(Pay pay);

	Pay getPayByID(String muid);

	void setPayUpdate(Pay pay);

	List<Alarm> recentAlarms(String user_id);

	int deleteAlarm(Alarm alarm);

	int readAlarm(Alarm alarm);

	List<Alarm> getAlarmList(Alarm alarm);

	int getTotalPointCnt(Alarm alarm);

}
