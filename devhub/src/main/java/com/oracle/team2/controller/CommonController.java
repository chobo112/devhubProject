package com.oracle.team2.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oracle.team2.model.SK_Board_Card;
import com.oracle.team2.model.User;
import com.oracle.team2.service.SK_Service.SK_Service_Interface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CommonController {
	HttpSession session = null;
	
	@Autowired
	private SK_Service_Interface sk_Service_Interface;
	
	@RequestMapping("/")
	public String index(Model model) {

		SK_Board_Card sk_Board_Card = new SK_Board_Card();
		sk_Board_Card.setStart(1);
		sk_Board_Card.setEnd(6);
		sk_Board_Card.setBoard_type("0");
		sk_Board_Card.setUser_id("");

		sk_Board_Card.setCardSort("popular");
		//
		// 필터링 해보자
		List<SK_Board_Card> skBoardCardList = sk_Service_Interface.doFilter(sk_Board_Card);
		System.out.println("skBoardCardList-->" + skBoardCardList);


		List<String> skillImageUrlList = new ArrayList<>();
		for (SK_Board_Card skCard : skBoardCardList) { // 일단 한번 타입을 맞춘 그릇에다가 리스트를 각각 꺼내와서 담은뒤에

			// model에 skills이미지를 넘겨줄 객체

			String skillImageUrls = skCard.getSkill_images(); // 위에서 담은 그릇인 skillListImg를 꺼내서 접시위(SkillImages)에 올려주고
			// System.out.println("여기에는 이미지들이 1개씩 분리되서
			// 담겨있으려니????????????????????????????"+skillImageUrls);

			if (skillImageUrls != null && !skillImageUrls.isEmpty()) {// 접시위에 아무것도 없지 않은 경우에만
				String[] skillImageUrlArray = skillImageUrls.split(","); // , ~~ ,~~ , 이런식으로 담긴 이미지를을 , 로 잘라서 배열로 다시
																			// 넣어주고
				// System.out.println("여기에다가는 이미지배열에다가 넣었을 애들인데 여기서는 ,가 잘린애들이
				// 담겨있을거임"+skillImageUrlArray);

				skillImageUrlList = Arrays.asList(skillImageUrlArray);
				// System.out.println("skillImageUrlList-->"+skillImageUrlList);
				// System.out.println("skillImageUrlList.size(): "+ skillImageUrlList.size());

				skCard.setSkillImgUrlList(skillImageUrlList);

			}
		}
		model.addAttribute("sk_board", skBoardCardList);
		return "index";
	}
	
	// 진형: 페이지에서 로그인 되었는지 확인 후 로그인 페이지로 이동할 때
	/**
	 * 로그인 되어있는지 체크하는 메소드
	 * @param request Controller의 request
	 * @param afterLoginPage 로그인 성공 후 들어갈 페이지, "": index페이지로 이동
	 * @return true: 로그인 되어있음, false: 로그인 되어있지 않음
	 */
	public boolean loginCheck(HttpServletRequest request, String afterLoginPage) {
		System.out.println("CommonController loginCheck start...");
		HttpSession session = request.getSession(true); // 세션이 있으면 받아오고, 없으면 새로 만듬
		
		if(session.getAttribute("userId")==null) {
			// 로그인 되지 않았으면 요청한 페이지 url을 가지고 로그인 페이지로 이동
			if(afterLoginPage == null) {
				afterLoginPage = "";
			}
			session.setAttribute("prePage", afterLoginPage);
			return false;
		} else {
			// 로그인 되어있을 때
			session.setMaxInactiveInterval(1800);	// 세션 유지시간 재설정
			return true;
		}
	}
	
	public void setUserSession(HttpServletRequest request, User user) {
		session = request.getSession(true);
		session.setAttribute("userId", user.getUser_id());	//세션에 userId라는 Key로 아이디 담음
		session.setAttribute("userPoint", user.getPoint());
		session.setAttribute("userName", user.getUser_name());
		session.setMaxInactiveInterval(1800);	//세션유지시간
		System.out.println(session.getAttribute("userId")); //세션에 담긴 아이디 확인
	}
	
	public void deleteSession(HttpServletRequest request) {
		System.out.println("CommonController deleteSession start");
		session = request.getSession(true);
		if(session.getAttribute("userId") != null) {
			session.invalidate();
		}
	}
	
	public String getUserIdInSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("userId") == null)
			return "";
		String userId = session.getAttribute("userId").toString();
		System.out.println("CommonController getUserIdInsession userId->"+ userId);
		return userId; 
	}
	
	public String getPrePage(HttpServletRequest request) {
	      String prePage = "";
	      HttpSession session = request.getSession(true);
	      if(session.getAttribute("prePage") != null)
	         prePage = session.getAttribute("prePage").toString();
	      return prePage;
	   }
	
}
