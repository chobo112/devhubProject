package com.oracle.team2.service.JH_Service;

import java.util.List;
import java.util.Map;

import com.oracle.team2.model.Alarm;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.Point_log;
import com.oracle.team2.model.Refund;
import com.oracle.team2.model.User;

public interface JH_Service_Interface {

	List<Point_log> getPointLogList(Point_log point_log);

	User getUser(String user_id);

	int getTotalPointCnt(Point_log point_log);

	long getUserPoint(String user_id);

	boolean validationRefund(Refund refund);

	int refundProcess(Refund refund);

	boolean isPosiblePayment(Pay pay);

	int initPay(Pay pay);

	boolean validationPayment(Map<String, Object> map);

	List<Alarm> recentAlarms(String user_id);

	int deleteAlarm(Alarm alarm);

	int readAlarm(Alarm alarm);

	List<Alarm> getAlarmList(Alarm alarm);

	int getTotalAlarmCnt(Alarm alarm);

	void readAlarmList(List<Alarm> alarmList);

	void deleteAlarmList(List<Alarm> alarmList);

}
