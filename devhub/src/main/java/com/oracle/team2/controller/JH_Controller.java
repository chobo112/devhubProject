package com.oracle.team2.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.team2.model.Alarm;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.Point_log;
import com.oracle.team2.model.Refund;
import com.oracle.team2.model.User;
import com.oracle.team2.service.JH_Service.JH_Service_Interface;
import com.oracle.team2.service.JH_Service.Paging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class JH_Controller {
	private final JH_Service_Interface service;
	private final CommonController cc;
	/**
	 * 포인트 뷰로 이동하는 메소드
	 * @param point_log 유저아이디, 조회기간, 페이지정보를 담을 객체
	 * @param model 뷰에 정보를 저장해서 가지고갈 객체
	 * @return point.html
	 */
	@RequestMapping("pointView")
	public String pointView( HttpServletRequest request, Point_log point_log,Model model) {
		System.out.println("point view 호출");
		if(!cc.loginCheck(request, "pointView")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		
		point_log.setUser_id(cc.getUserIdInSession(request));
		System.out.println("JH_Controller pointView 입력데이터->"+ point_log);
		User user = service.getUser(point_log.getUser_id());

		model.addAttribute("userPoint", user.getPoint());
		model.addAttribute("userName",user.getUser_name());
		model.addAttribute("user_id", user.getUser_id());
		
		pointLogIntoModel(point_log, model);
		
		long point = service.getUserPoint(cc.getUserIdInSession(request));
		request.getSession().setAttribute("userPoint", point);
		
		return "JH_views/point";
	}
	
	@GetMapping("point")
	public String tymeleaf(HttpServletRequest request,Point_log point_log,Model model) {
		if(!cc.loginCheck(request, "pointView")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		System.out.println("JH_Controller tymeleaf start...");
		System.out.println("JH_Controller tymeleaf point_log->"+ point_log);
		pointLogIntoModel(point_log, model);
		return "JH_views/point::#pointTable";
	}
	
	/**
	 * 포인트 로그의 페이징 작업과 model에 정보를 저장하는 메소드
	 * @param point_log 페이지 정보와, 조회 정보를 담은 객체
	 * @param model 뷰에 가지고 갈 객체
	 */
	private void pointLogIntoModel(Point_log point_log, Model model) {
		int totPointCnt = service.getTotalPointCnt(point_log);
		System.out.println("JH_Controller pointLogIntoModel ->"+ totPointCnt);
		
		Paging page = new Paging(totPointCnt, point_log.getCurrentPage());
		point_log.setStart(page.getStart());
		point_log.setEnd(page.getEnd());
		
		List<Point_log> data = service.getPointLogList(point_log);
		model.addAttribute("datas", data);
		model.addAttribute("totCnt", totPointCnt);
		model.addAttribute("page",page);
	}
	
	@ResponseBody
	@GetMapping("userPoint")
	public String getUserPoint(@RequestParam("user_id") String user_id) {
		System.out.println("JH_Controller getUserPoint start...");
		System.out.println("JH_Controller getUserPoint user_id->"+ user_id);
		
		long point = service.getUserPoint(user_id);
		return String.valueOf(point);
	}
	
	@PostMapping("refund")
	public String regRefund(HttpServletRequest request, Refund refund, Model model) {
		System.out.println("JH_Controller regRefund start...");
		System.out.println("JH_Controller regRefund refund->"+ refund);
		
		boolean isError = service.validationRefund(refund);
		
		if(isError) {
			model.addAttribute("error", 1);
			return "forward:pointView";
		} 
		
		if(service.refundProcess(refund) != 1) {
			model.addAttribute("error",1);
		}
		
		long point = service.getUserPoint(cc.getUserIdInSession(request));
		request.getSession().setAttribute("userPoint", point);
		
		return "forward:pointView";
	}
	
	@RequestMapping("payView")
	public String payView(HttpServletRequest request,Pay pay,Model model) {
		System.out.println("JH_Controller payView start...");
		if(!cc.loginCheck(request, "payView?board_id="+pay.getBoard_id())) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		String preUrl = request.getHeader("Referer");
		System.out.println("JH_Controller payView preUrl->"+ preUrl);
		model.addAttribute("preUrl", preUrl);
		pay.setUser_id(request.getSession().getAttribute("userId").toString());
		System.out.println("JH_Controller payView pay->"+ pay);
		
		
		// 유저가 결제 가능한지 검증
		boolean isContinue = service.isPosiblePayment(pay);
		System.out.println("JH_Controller payView isContinue->"+ isContinue);
		
		// 계속 진행 불가 시
		if(!isContinue) {
			model.addAttribute("isContinue", isContinue);
			model.addAttribute("errorMsg", "유효한 결제정보가 아닙니다.");
			return "JH_views/paymentView";
		}
		
		// 가능하다면 결제 전처리과정 수행 (pay테이블에 insert 및 )
		int result = service.initPay(pay);
		System.out.println("JH_Controller payView pay->"+ pay);
		
		// 인서트 오류
		if(result == 0) {
			model.addAttribute("isContinue", false);
			model.addAttribute("errorMsg", "잠시 후 다시 시도해주세요");
			return "JH_views/paymentView";
		}
		model.addAttribute("isContinue", true);
		pay.setMuid("DevHub_"+pay.getPay_id());
		model.addAttribute("pay", pay);
		
		return "JH_views/paymentView";
	}

	@ResponseBody
	@PostMapping("conformPay")
	public Map<String,Object> conformPay(HttpServletRequest request,@RequestBody Map<String, Object> map){
		System.out.println("JH_Controller conformPay start...");
		System.out.println("JH_Controller conformPay map->"+ map);
		System.out.println("JH_Controller conformPay ->"+ request.getHeader("Referer"));
		Map<String, Object> response = new HashMap<String,Object>();
		
		if(map.get("success").toString().equals("false")) {
			response.put("success", false);
			return response;
		}
		
		if(service.validationPayment(map)) {
			// 결제한 금액이 프로젝트 금액과 맞을 때
			response.put("success", true);
			return response;
		}
		
		response.put("success", false);
		System.out.println(response);
		return response;
	}
	
	@GetMapping("recentAlarms")
	public String recentAlarms(HttpServletRequest request, Model model){
		System.out.println("JH_Controller recentAlarms start...");
		List<Alarm> alarms = service.recentAlarms(cc.getUserIdInSession(request));
		System.out.println("JH_Controller recentAlarms alarms->"+ alarms);
		model.addAttribute("alarms", alarms);
		
		return "fragments/header::#alarmList";
	}
	
	@ResponseBody
	@DeleteMapping("deleteAlarm")
	public String deleteAlarm(Alarm alarm) {
		System.out.println("JH_Controller deleteAlarm start...");
		System.out.println("JH_Controller deleteAlarm alarm->"+ alarm);
		int result = service.deleteAlarm(alarm);
		return result>0? "success":"fail";
	}
	
	@ResponseBody
	@PatchMapping("readAlarm")
	public String readAlarm(Alarm alarm) {
		System.out.println("JH_Controller readAlarm start...");
		System.out.println("JH_Controller readAlarm alarm->"+ alarm);
		int result = service.readAlarm(alarm); 
		return result>0? "success":"fail";
	}
	
	
	@RequestMapping("alarmView")
	public String alarmView(HttpServletRequest request, Alarm alarm, @RequestParam(name="option", required = false, defaultValue = "none") String option, Model model) {
		System.out.println("JH_Controller alarmView start...");
		if(!cc.loginCheck(request, "alarmView")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		alarm.setUser_id(cc.getUserIdInSession(request));
		
		int totalAlarmCnt = service.getTotalAlarmCnt(alarm);
		System.out.println("JH_Controller alarmView totalAlarmCnt->"+totalAlarmCnt );
		Paging page = new Paging(totalAlarmCnt, alarm.getCurrentPage());
		alarm.setStart(page.getStart());
		alarm.setEnd(page.getEnd());
		
		List<Alarm> alarms = service.getAlarmList(alarm);
		System.out.println("JH_Controller alarmView alarmList Size->"+ alarms.size());
		
		model.addAttribute("alarms", alarms);
		model.addAttribute("page", page);
		
		if(!"none".equals(option)) {
			return "JH_views/alarm::#alarmSection";
		}
		
		return "JH_views/alarm";
	}
		
	@ResponseBody
	@RequestMapping(value = "readAlarmList",  	consumes = MediaType.APPLICATION_JSON_VALUE)
	public String readAlarmList(@RequestBody List<Alarm>  alarmList, HttpServletRequest request) {
		System.out.println("JH_Controller readAlarmList start...");
		System.out.println("JH_Controller readAlarmList alarmList->"+ alarmList.size());
		String user_id = cc.getUserIdInSession(request);
		if(alarmList != null | alarmList.size() != 0) {
			for(Alarm alarm : alarmList) {
				alarm.setUser_id(user_id);
			}
			
			service.readAlarmList(alarmList);
			return "success";
		}
		return "fail";
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteAlarmList",  	consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteAlarmList(@RequestBody List<Alarm>  alarmList, HttpServletRequest request) {
		System.out.println("JH_Controller deleteAlarmList start...");
		System.out.println("JH_Controller deleteAlarmList alarmList.size->"+ alarmList.size());
		String user_id = cc.getUserIdInSession(request);
		if(alarmList != null | alarmList.size() != 0) {
			for(Alarm alarm : alarmList) {
				alarm.setUser_id(user_id);
			}
			
			service.deleteAlarmList(alarmList);
			return "success";
		}
		return "fail";
	}
	
	
}
