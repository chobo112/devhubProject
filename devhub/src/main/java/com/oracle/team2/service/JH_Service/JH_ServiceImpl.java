package com.oracle.team2.service.JH_Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.oracle.team2.controller.JH_Controller;
import com.oracle.team2.dao.JH_Dao.JH_Dao_Interface;
import com.oracle.team2.model.Alarm;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.Point_log;
import com.oracle.team2.model.Refund;
import com.oracle.team2.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JH_ServiceImpl implements JH_Service_Interface {
	
	private final JH_Dao_Interface dao;

	@Override
	public List<Point_log> getPointLogList(Point_log point_log) {
		System.out.println("JH_ServiceImpl getPointLogList start...");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		System.out.println("JH_ServiceImpl getPointLogList point_log.date->"+ point_log.getDate());
		List<Point_log> pointList =  dao.getPointLogList(point_log);
		
		if(pointList != null) {
			for(Point_log point : pointList)
				point.setDate(dateFormat.format(point.getPoint_date()));
		}
		System.out.println("JH_ServiceImpl getPointLogList pointLogList->"+ pointList);
		
		return pointList;
	}

	@Override
	public User getUser(String user_id) {
		System.out.println("JH_ServiceImpl getUser start...");
		User user = dao.getUser(user_id);
		
		return user;
	}

	@Override
	public int getTotalPointCnt(Point_log point_log) {
		System.out.println("JH_ServiceImpl getTotalPointCnt start...");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		switch (point_log.getPeriodDays()) {
		case 0:
			calendar.add(Calendar.YEAR, -30);
			break;
		case 1:
			calendar.add(Calendar.DATE, -1);
			break;
		case 7:
			calendar.add(Calendar.DATE, -7);
			break;
		case 30:
			calendar.add(Calendar.MONTH, -1);
			break;
		case 365:
			calendar.add(Calendar.YEAR, -1);
			break;

		default:
			calendar.add(Calendar.YEAR, -30);
			break;
		}
		point_log.setDate(dateFormat.format(calendar.getTime()));
		int totalPointCnt = dao.getTotalPointCnt(point_log);
		System.out.println("JH_ServiceImpl getTotalPointCnt totalPointCnt->"+ totalPointCnt);
		return totalPointCnt;
	}

	@Override
	public long getUserPoint(String user_id) {
		System.out.println("JH_ServiceImpl getUserPoint start...");
		
		return dao.getUserPoint(user_id);
	}

	@Override
	public boolean validationRefund(Refund refund) {
		System.out.println("JH_ServiceImpl validationRefund start...");
		
		long point = dao.getUserPoint(refund.getUser_id());
		boolean isError = true;
		if(point == refund.getPoint()) {
			isError = false;
		}
		
		return isError;
	}

	@Override
	public int refundProcess(Refund refund) {
		System.out.println("JH_ServiceImpl refundProcess start...");
		int result = dao.insertRefund(refund);
		System.out.println("JH_ServiceImpl refundProcess result->"+ result);
		return result;
	}

	@Override
	public boolean isPosiblePayment(Pay pay) {
		System.out.println("JH_ServiceImpl isPosiblePayment start...");
		// 유저가 결제 가능한지 검증 
		// 유저가 해당 보드에 수락되었는지 체크 & 유저가 결제했는지 & 유저가 멘토인지 체크
		// 유저가 결제하는 프로젝트 그룹의 인원이 찼을 경우 체크;
		boolean isContinue = false;
		if(dao.isPossiblePayment(pay) && !dao.isFullGroup(pay))	{
			isContinue = true;
		}
		return isContinue;
	}

	@Override
	public int initPay(Pay pay) {
		System.out.println("JH_ServiceImpl initPay start...");
		return dao.initPay(pay);
	}

	@Override
	public boolean validationPayment(Map<String, Object> map) {
		System.out.println("JH_ServiceImpl validationPayment start...");
		String muid =  (String)map.get("merchant_uid");
		muid = muid.substring(muid.lastIndexOf("_")+1);
		System.out.println("JH_ServiceImpl validationPayment muid->"+ muid);
		Pay pay = dao.getPayByID(muid);
		System.out.println("JH_ServiceImpl validationPayment pay->"+ pay);
		
		if(pay == null) {
			System.out.println("JH_ServiceImpl validationPayment 결제정보 없음");
			return false;
		}
		
		System.out.println("pay.getPr_price() 타입->"+pay.getPr_price().getClass().getName());
		System.out.println("map.get(paid_amount) 타입->"+map.get("paid_amount").getClass().getName());
		
		if(pay.getPr_price().toString().equals(map.get("paid_amount").toString())) {
			System.out.println("비교결과 같음");
			pay.setPay_status("1");
			pay.setPay_log(map.toString());
			dao.setPayUpdate(pay);
			return true;
		}
		System.out.println("JH_ServiceImpl validationPayment 결제금액 불일치");
		return false;
	}

	@Override
	public List<Alarm> recentAlarms(String user_id) {
		System.out.println("JH_ServiceImpl recentAlarms start...");
		List<Alarm> alarms = dao.recentAlarms(user_id);
		System.out.println("JH_ServiceImpl recentAlarms alarms.size->"+ alarms.size());
		
		return alarms;
	}

	@Override
	public int deleteAlarm(Alarm alarm) {
		System.out.println("JH_ServiceImpl deleteAlarm start...");
		return dao.deleteAlarm(alarm);
	}

	@Override
	public int readAlarm(Alarm alarm) {
		System.out.println("JH_ServiceImpl readAlarm start...");
		return dao.readAlarm(alarm);
	}

	@Override
	public List<Alarm> getAlarmList(Alarm alarm) {
		System.out.println("JH_ServiceImpl getAlarmList start...");
		List<Alarm> alarms = dao.getAlarmList(alarm);
		System.out.println("JH_ServiceImpl getAlarmList alarms.size->"+ alarms.size());
		return alarms;
	}

	@Override
	public int getTotalAlarmCnt(Alarm alarm) {
		System.out.println("JH_ServiceImpl getTotalAlarmCnt start...");
		int totAlarmCnt = dao.getTotalPointCnt(alarm);
		System.out.println("JH_ServiceImpl getTotalAlarmCnt totAlarmCnt->"+ totAlarmCnt);
		return totAlarmCnt;
	}

	@Override
	public void readAlarmList(List<Alarm> alarmList) {
		System.out.println("JH_ServiceImpl readAlarmList start...");
		for(Alarm alarm : alarmList)
			dao.readAlarm(alarm);
	}

	@Override
	public void deleteAlarmList(List<Alarm> alarmList) {
		System.out.println("JH_ServiceImpl deleteAlarmList start...");
		for(Alarm alarm : alarmList)
			dao.deleteAlarm(alarm);
	}
	
	
	
}
