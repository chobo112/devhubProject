package com.oracle.team2.controller;

import java.util.List;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.team2.model.Region;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;
import com.oracle.team2.service.YS_Service.YS_Service_Interface;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class YS_Controller {
	private final YS_Service_Interface service;
	private final JavaMailSender mailSender;
	private final CommonController cc;
	
	// 로그인뷰
	   @RequestMapping("/loginView")
	   public String login(HttpServletRequest request) {
	      // 로그인된 상태이면서 로그인페이지를 접근할 때 로그아웃 페이지로 이동
	      cc.deleteSession(request);
	      
	      return "YS_views/login";
	   }
	
	// 로그인
	   @RequestMapping(value = "/login")
	   public String login(User user, HttpServletRequest request, Model model) {
	       System.out.println("YS_Controller login start");
	       System.out.println("login 개체 확인->" + user);
	       User loginResult = service.login(user);

	       System.out.println("YS_Controller login session start->" + request.getSession(false));
	       if (loginResult == null) {
	    	   System.out.println("값을 찍어보자 !! : "+loginResult);
	    		   model.addAttribute("userIsNot", "noid");
	    		   return "/YS_views/login";
	           // 아이디도 없는 경우
	       } else if (loginResult.isResultYN()) {
	           // 로그인 성공 시
	           // 세션체크
	           // 세션없으면 아이디 저장
	           cc.setUserSession(request, loginResult);
	           // c.setUserSession(user);
	           model.addAttribute("userId", loginResult.getUser_id());
	           model.addAttribute("userPoint", loginResult.getPoint());
	           model.addAttribute("userName", loginResult.getUser_name());
	           return "redirect:" + cc.getPrePage(request);
	       } else if ("0".equals(loginResult.getDel_status())) {
	           System.out.println("값이 넘어오는 : " + loginResult.getLogin_cnt());
	           model.addAttribute("msg", loginResult.getLogin_cnt());
	           return "/YS_views/login";
	       } else {
	           return "/YS_views/login";
	       }
	   }
	
	//로그아웃
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		cc.deleteSession(request);
		return "redirect:/";
	}//로그아웃
	
	
	
	// 동의폼
	@RequestMapping(value = "/agree")
	public String agree() {
		System.out.println("YS_Controller agree  start" );
		
		return "/YS_views/agree";
	}// 동의폼
	
	// 회원가입 화면 가져옴 skill legion List를 가져옴
	@RequestMapping(value = "/createUser")
	public String createUser(Region region,Skill skill,Model model) {
		System.out.println("YS_Controller 회원가입 뷰 시작");
		List<Region> regionList= service.getRegionListAll();
		model.addAttribute("regions", regionList);
		
		List<Skill> skillList = service.getSkillListAll();
		model.addAttribute("skills", skillList);
		
		return "YS_views/createuser";
	}// 회원가입 화면 가져옴 skill legion List를 가져옴
	
	//  회원가입 정보 insert 하는 작업수행 
	@ResponseBody
	@RequestMapping(value = "/createUser/save")
	public String createUserSave(@RequestBody User user,Model model) {
		System.out.println("YS_Controller createUserSave  start" );
		int insertResult = service.ysinsert(user);
		if(insertResult>0) { // 인서트할 회원정보가 있으면 화면에서 ajax로 뿌려줌
			return "true";
		}
		else {
			return "forward:createUser";
		}
	}//  회원가입 정보 insert 하는 작업수행 	
	
	// 회원가입 ajax chkId
	@RequestMapping(value = "/createUser/chkID")
	@ResponseBody
	public int idChk(Model model,@RequestBody String userId) {
		int chkIdYN =0;
		chkIdYN = service.chkId(userId);
		System.out.println("YS_Controller idChk chkIdYN ->" + chkIdYN);
		
		model.addAttribute("chkIdYN", chkIdYN);
		return chkIdYN;
	}// 회원가입 ajax chkId
	
	// 회원가입 ajax ChknickName 
	@RequestMapping(value = "/createUser/chkNmae")
	@ResponseBody
	public int userNameChk(Model model,@RequestBody String userName) {
		int chkNameYN = 0;
		chkNameYN = service.chkName(userName);
		System.out.println("YS_Controller userNameChk userName->" + userName);
		
		model.addAttribute("chkNameYN", chkNameYN);
		return chkNameYN;
	}// 회원가입 ajax ChknickName 
	
	
	
	// 회원정보 화면만 뿌려줌
	@RequestMapping("/modifyView")
	public String modifyView(User user, Model model,HttpServletRequest request) {
		if(!cc.loginCheck(request, "modifyView")) {
	         // 로그인 안 되어있으면 로그인 페이지로
	         return "forward:loginView";
	      }
		user.setUser_id(cc.getUserIdInSession(request));
		
		User findUser = service.findUser(user.getUser_id());
		
		System.out.println("YS_Controller modify findUser : "+findUser);
		
		// 회원정보 화면에 뿌려줌
		List<Region> regionList= service.getRegionListAll();
		model.addAttribute("regions", regionList); // 스킬리스트
		model.addAttribute("user", findUser); // 회원정보
		
		return"YS_views/modify";
	}
	
	// 회원정보수정 update
	@RequestMapping(value = "/modifyUdate")
	public String modifyUpdate(User user, Model model,HttpServletRequest request) {
		
		System.out.println("YS_Controller modifyUdate  start" );
		if(!cc.loginCheck(request, "modify")) {
	         // 로그인 안 되어있으면 로그인 페이지로
	         return "forward:loginView";
	      }
		user.setUser_id(cc.getUserIdInSession(request));
		
		int modifyUpdateResult = service.modifyUpdate(user);
		
		if(modifyUpdateResult > 0)
			request.getSession().setAttribute("userName", user.getUser_name());

		return "/index";
	}
	
	// ajax 회정정보수정 닉네임  
	@RequestMapping(value = "/modify/chkName")
	@ResponseBody
	public int modifyChkName(Model model,@RequestBody String modifyUserName) {
		int modifyChkNameYN =0;
		modifyChkNameYN = service.modifyChkName(modifyUserName);
		System.out.println("YS_Controller idChk modifyChkNameYN ->" + modifyChkNameYN);
		
		model.addAttribute("modifyChkNameYN", modifyChkNameYN);
		return modifyChkNameYN;
	}
	
	// 회원탈퇴 뷰 
	@RequestMapping("/withdrawView")
	public String withdrawView(User user,HttpServletRequest request) {
		System.out.println("YS_Controller withdrawView start");
		if(!cc.loginCheck(request, "withdrawView")) {
	         // 로그인 안 되어있으면 로그인 페이지로
	         return "forward:loginView";
	      }
		user.setUser_id(cc.getUserIdInSession(request));
		return "YS_views/withdraw";
	}
	
	// 회원탈퇴
	@RequestMapping("/withdraw")
	public String withdrawUser(User user ,HttpServletRequest request) {
		System.out.println("YS_Controller withdrawUser start");
		
		if(!cc.loginCheck(request, "withdraw")) {
	         // 로그인 안 되어있으면 로그인 페이지로
	         return "forward:loginView";
	      }
		user.setUser_id(cc.getUserIdInSession(request));
		service.withdrawUser(user);
		
		cc.deleteSession(request);
		return "/index";
	}
	// 비밀번호 찾기 뷰 
	@RequestMapping("/findpasswordView")
	public String findPwdView() {
		System.out.println("YS_Controller findPwdView start");
		return "YS_views/findpassword";
	}
	
	// 비밀번호 찾기 로직
	@RequestMapping("/findpassword")
	@ResponseBody
	public String findPwd(HttpServletRequest request, User user, Model model) {
	    System.out.println("YS_Controller findPwd mailSending start");
	    //
	    String tomail = user.getUser_email(); // 해당 유저의 입력한 이메일 		
	    System.out.println(tomail);
	    String setfrom = "dladyd1119@gamil.com";// 보내는 사람 
	    String title = "DevHub 임시비밀번호 입니다.";// 보내는 제목 title
	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper messageHelper = new MimeMessageHelper(message,true,"UTF-8");
	        messageHelper.setFrom(setfrom); // 보내는 사람
	        messageHelper.setTo(tomail);    // 받는사람 이메일
	        messageHelper.setSubject(title);// 메일 제목 생략 가능
	        String tempPassword = (int)(Math.random()*999999)+1+""; // 6자리 랜덤 숫자
	        messageHelper.setText("임시 비밀번호 입니다. : "+tempPassword); // 내용
	        System.out.println("임시 비밀번호 입니다. : "+tempPassword);
	        mailSender.send(message);
	        
	        String tempPwdEmail = service.updateTempPwd(user,tempPassword); // 쿼리문
	        System.out.println("값 받아오는지 확인 : "+tempPassword);
	        
	        if(tempPwdEmail != null) {
	            return "true";
	        } else {
	            return "false";
	        }
	        
	    } catch (Exception e) {
	        System.out.println("YS_Controller findPwd e.getMessage() ->" + e.getMessage());
	        return "false"; // 예외 발생 시에도 false 반환
	    }
	}
	
}//class 
