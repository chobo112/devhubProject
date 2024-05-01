package com.oracle.team2.controller;


import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.Pay;
import com.oracle.team2.model.ProjectResultDTO;
import com.oracle.team2.model.Score;

import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;

import com.oracle.team2.service.MK_Service.MK_Service_Interface;
import com.oracle.team2.service.MK_Service.Paging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MK_Controller {
	private final MK_Service_Interface MK_Service_Interface;
	private final CommonController cc;

	// 자기소개 조회

	@GetMapping("SelfIntroduction")
	public String SelfIntroduction(User user, Model model, HttpServletRequest request) {
		System.out.println("MKController start SelafIntroduction");

		// 세션에서 로그인된 사용자 정보를 읽어옴
		String loggedInUser = user.getUser_id();

		List<User> introduction = MK_Service_Interface.getintroduction(loggedInUser);
		List<User> skill = MK_Service_Interface.getskill(loggedInUser);
		
		if(introduction == null || introduction.size() == 0)
			return "redirect:" + request.getHeader("Referer");
		
		introduction.get(0).setUser_id(loggedInUser);
		System.out.println("MKController list.size()" + introduction);
		model.addAttribute("datas", introduction);
		model.addAttribute("skills", skill);
		System.out.println(skill);
		return "MK_views/SelfIntroduction";
	}
	/*
	@GetMapping("SelfIntroduction")
	public String SelfIntroduction(HttpSession session, Model model, HttpServletRequest request) {
		System.out.println("MKController start SelafIntroduction");
		
		// 세션에서 로그인된 사용자 정보를 읽어옴
		String loggedInUser = (String) session.getAttribute("userId");
		System.out.println("logedInUser");
		
		if (loggedInUser != null) {
			List<User> introduction = MK_Service_Interface.getintroduction(loggedInUser);
			List<User> skill = MK_Service_Interface.getskill(loggedInUser);
			System.out.println("MKController list.size()" + introduction.size());
			model.addAttribute("datas", introduction);
			model.addAttribute("skills", skill);
			System.out.println(skill);
			
			return "MK_views/SelfIntroduction";
		} else {
			String requestURL = request.getRequestURL().toString();
			session.setAttribute("redirectUrl", requestURL);
			return "/YS_views/login";
		}
	}
*/
	// 자기소개 수정 수정 페이지

	@GetMapping("SelfIntroEditForm")
	public String SelfIntroEdit(HttpSession session, Model model) {
		System.out.println("MKController start SelfIntroduction");

		// 세션에서 로그인된 사용자 정보를 읽어옴
		String loggedInUser = (String) session.getAttribute("userId");
		System.out.println("MKController start SelfIntroduction user->" + loggedInUser);

		if (loggedInUser != null) {
			List<User> introduction = MK_Service_Interface.getintroduction(loggedInUser);
			List<User> skill = MK_Service_Interface.getskill(loggedInUser);

			System.out.println("MKController list.size()" + introduction.size());
			model.addAttribute("datas", introduction);
			model.addAttribute("skills", skill);
			System.out.println(skill);
			return "MK_views/SelfIntroEditForm";
		} else {
			return "/YS_views/login";
		}
	}

	// 자기소개 수정에서 skill에 대한 데이터 조회
	@ResponseBody
	@GetMapping("allskillJson")
	public List<Skill> allskList(Model model) {
		System.out.println("MK Controller Start Allksill");
		List<Skill> allSkList = MK_Service_Interface.allSkList();
		System.out.println("MK Controller list.size()" + allSkList.size());
		model.addAttribute("allSkList", allSkList);
		return allSkList;
	}

	// 자기소개 수정에서 skill 추가 부분
	@ResponseBody
	@PostMapping("/addSkill")
	public String addSkills(HttpSession session, @RequestBody String[] skillIds) {
		System.out.println("MK controller addskill start");

		// 세션에서 로그인된 사용자 아이디를 가져옴
		String loggedInUser = (String) session.getAttribute("userId");

		System.out.println("skillId->" + skillIds);
		MK_Service_Interface.addSkills(loggedInUser, skillIds);
		// return ResponseEntity.ok("skill added succed");
		System.out.println("매퍼 완료");
		return "redirect:/MK_views/SelfIntroduction";

	}

	// 스킬 삭제
	@ResponseBody
	@PostMapping("/delete_skill")
	public String delete_skill(HttpSession session, @RequestBody String skill_id) {
		System.out.println("MK Controller delete_skill start");

		// 세션에서 로그인된 사용자 아이디를 가져옴
		String loggedInUser = (String) session.getAttribute("userId");

		System.out.println("MK Controller delete_skill skill_id->" + skill_id + "user_id->" + loggedInUser);
		MK_Service_Interface.deleteSkill(loggedInUser, skill_id);
		System.out.println("매퍼 완료");
		return "redirect:MK_views/SelfIntroEditForm";
	}

	// 자기소개 부분 저장
	@ResponseBody
	@PostMapping("/user_intro_update")
	public String intro_update(HttpSession session, @RequestBody String introData) {
		System.out.println("MK Controller intro_update start");

		// 세션에서 로그인된 사용자 아이디를 가져옴
		String loggedInUser = (String) session.getAttribute("userId");

		System.out.println("userIntro->" + introData);
		MK_Service_Interface.introUpdate(loggedInUser, introData);
		System.out.println("매퍼 완료");
		return "redirect:MK_views/SelfIntroEditForm";
	}

	// 깃허브 부분 저장
	@ResponseBody
	@PostMapping("/github_update")
	public String github_update(HttpSession session, @RequestBody String gitData) {
		System.out.println("MK Controller github_update start");

		// 세션에서 로그인된 사용자 아이디를 가져옴
		String loggedInUser = (String) session.getAttribute("userId");
		System.out.println(gitData);
		MK_Service_Interface.githubUpdate(loggedInUser, gitData);
		System.out.println("매퍼 완료");
		return "redirect:MK_views/SelfIntroEditForm";
	}


	//내가 지원한 프로젝트 
	@GetMapping("MyProjectMain")
	
	public String MyProjectBoard(Model model, HttpServletRequest request,ProjectResultDTO projectResultDTO) {
		System.out.println("myProjectMain 호출");
		if(!cc.loginCheck(request, "MyProjectMain")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		
		projectResultDTO.setUser_id(cc.getUserIdInSession(request));
		System.out.println("MyProjectMain parameter : "+ projectResultDTO);
		System.out.println("MyProjectMain size->"+ projectResultDTO);
		handleCardPaging(model,projectResultDTO,request);
		return "MK_views/ProjectApplicationList";
	}
	
	//ajax 카드 조회
	@GetMapping("MyProject")
	public String MyProjectBoard_All(
		Model model,HttpServletRequest request,ProjectResultDTO projectResultDTO) {
		
		projectResultDTO.setUser_id(cc.getUserIdInSession(request));
		System.out.println("MK_Controller / MyProjectBoard_All Start.... projectResultDTO->  "+projectResultDTO);
		
		handleCardPaging(model,projectResultDTO, request);
		return "MK_views/ProjectApplicationList::#content";
	}
	
	//핸들러 
	public void handleCardPaging(Model model,ProjectResultDTO projectResultDTO,HttpServletRequest request) {
		 System.out.println("MK_Controller / handleCardPaging Start....  ");
		 int totalMyproject = MK_Service_Interface.getTotalCardCnt(projectResultDTO); 
		  String  acceptPage =  projectResultDTO.getAp_acceptPage();
		
		  System.out.println("MK_Controller myCard tatalCnt->"+totalMyproject);  // ->14
		  
		  //페이징 처리
		  Paging page = new Paging(totalMyproject, projectResultDTO.getCurrentPage());
		  projectResultDTO.setStart(page.getStart());
		  projectResultDTO.setEnd(page.getEnd()); 
		  projectResultDTO.setAp_acceptPage(projectResultDTO.getAp_acceptPage());
		  projectResultDTO.setUser_id(cc.getUserIdInSession(request));
		  //Paging : Paging(currentPage=1, rowPage=9, pageBlock=10, start=1, end=9, startPage=1, endPage=2, total=14, totalPage=2)
		  System.out.println("Paging : " + page);
		  
		  //projectResultDTOProjectResultDTO(board_id=null, user_id=user2, board_edate=null, board_type=null, board_title=null, pr_price=null, pr_title=null, mentor_id=null, pr_sdate=null, ap_accept=null, ap_result=null, skill_id=null, skill_name=null, skill_img=null, skills=null, sc_score=null, sc_cmt=null, pay_status=null, pageNum=null, start=1, end=9, currentPage=null, ap_acceptPage=null)
		  System.out.println("projectResultDTO"+projectResultDTO);
		  
		  //projectResultDTOProjectResultDTO(board_id=null, user_id=user2, board_edate=null, board_type=null, board_title=null, pr_price=null, pr_title=null, mentor_id=null, pr_sdate=null, ap_accept=null, ap_result=null, skill_id=null, skill_name=null, skill_img=null, skills=null, sc_score=null, sc_cmt=null, pay_status=null, pageNum=null, start=1, end=9, currentPage=0, ap_acceptPage=0)

		  //카드 조회 
		  List<ProjectResultDTO> projectResultDTOs=MK_Service_Interface.getMyprojectList(projectResultDTO);
		  System.out.println("MK Controller cardList Size->"+projectResultDTOs.size());

		  projectResultDTO.setAp_acceptPage(acceptPage);
		  
		  model.addAttribute("page", page); 
		  model.addAttribute("boards",projectResultDTOs);
		  model.addAttribute("board",projectResultDTO);
		

	}


//		프로젝트 지원 현황 리스트 게시판 

	@GetMapping("MyScore")
	public String ProjectApplicationList(HttpSession session, Model model, HttpServletRequest request) {
		System.out.println("MK Controller start MyScore");

		// 세션에서 로그인된 사용자 아이디를 가져옴
			String loggedInUser = (String) session.getAttribute("userId");
		
		if (loggedInUser != null) {
			List<Score> myScore = MK_Service_Interface.myScore(loggedInUser);
			List<Score> userScore = MK_Service_Interface.userScore(loggedInUser);
			model.addAttribute("myScore", myScore);
			model.addAttribute("userScore", userScore);
			System.out.println("MyController MyScore list.size" + myScore.size());
			System.out.println("MyController MyScore list.size" + userScore.size());
			System.out.println(myScore);
			System.out.println(userScore);
			return "MK_views/MyScore";
		} else {
			// 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
			String requestURL = request.getRequestURL().toString();
			session.setAttribute("redirectUrl", requestURL);

			return "/YS_views/login";
		}
	}
	
	// 별점 데이터 수정 
	@ResponseBody 
	@PostMapping("update-score") 
	public Score mentor_score(HttpServletRequest request,HttpSession session, @RequestBody Map<String, String> requestBody) { 
	    String loggedInUser = (String) session.getAttribute("userId");
	    String boardId = requestBody.get("boardId");
	    String scScore = requestBody.get("scScore");
	    String mentorId = requestBody.get("mentorId");
	
	    // Score 객체 생성 후 속성 설정
	    Score score = new Score();
	    score.setBoard_id(Long.parseLong(boardId)); // requestBody에서 받아온 boardId 설정
	    score.setSc_score(Integer.parseInt(scScore)); // requestBody에서 받아온 scScore 설정
	    score.setUser_id(loggedInUser);
	    score.setMentor_id(mentorId);
	    score.setUser_id(cc.getUserIdInSession(request));
	    
	    System.out.println("boardId->" + boardId); 
	    System.out.println("scScore->" + scScore); 
	    System.out.println("mentorId->" + mentorId);
	    System.out.println("userId->" + loggedInUser);
	    System.out.println("score->" + score);
	    
	    // 데이터베이스에서 해당 평가가 이미 존재하는지 확인
	    int dataResult = MK_Service_Interface.dataResult(score);
	    
	    // 존재하지 않으면 새로운 평가 저장
	    if (dataResult == 0) {
	        MK_Service_Interface.saveMentorScore(score);
	        System.out.println("새로운 평가 저장 완료"); 
	    } 
	    // 이미 존재하면 평가 업데이트
	    else {
	        MK_Service_Interface.updateMentorScore(score);
	        System.out.println("평가 업데이트 완료"); 
	    }
	    
	    return score; 
	}

	 //코멘트 수정 
	  @ResponseBody 
	  @PostMapping("update-comment") 
	  public Score Mentor_Comment(HttpSession session, @RequestBody Map<String,String> requestBody,HttpServletRequest request) { 
		  System.out.println("updateComment Start.....");
		    String boardId = requestBody.get("boardId");
		    String updatedComment = requestBody.get("updatedComment");
		    String mentorId = requestBody.get("mentorId");
		    System.out.println("mentorId"+mentorId+"scCmt"+updatedComment );
		    // Score 객체 생성 후 속성 설정
		    Score scoreComment = new Score();
		    scoreComment.setBoard_id(Long.parseLong(boardId)); // requestBody에서 받아온 boardId 설정
		    scoreComment.setSc_cmt(updatedComment); // requestBody에서 받아온 scScore 설정
		    scoreComment.setMentor_id(mentorId);
		    scoreComment.setUser_id(cc.getUserIdInSession(request));
		    System.out.println("scoreComment->"+scoreComment);
	  //테이터베이스에서 해당 코멘트가 이미 존재하는지 확인 
	  int CommentdataResult = MK_Service_Interface.CommentdataResult(scoreComment);
	  
	  //존재하지 않으면 새로운 평가 저장 
	  if(CommentdataResult ==0) {
		  MK_Service_Interface.insertMentorComment(scoreComment);
		  System.out.println("새로운 평가 저장 완료");
	  } // 이미 코멘트가 존재하면 코멘트 업네이트
	  	MK_Service_Interface.saveMentorComment(scoreComment);
	  	System.out.println("코멘트 업데이트 완료 ");
	  	return scoreComment;
	  }
	  
	  
		/*
		 * // 코멘트 삽입
		 * 
		 * @ResponseBody
		 * 
		 * @PostMapping("insert-comment") public Score Mentor_insertComment(HttpSession
		 * session, @RequestBody Score score) { String loggedInUser = (String)
		 * session.getAttribute("userId"); Score scoreComment = new Score();
		 * scoreComment.setBoard_id(score.getBoard_id());
		 * scoreComment.setSc_cmt(score.getSc_cmt());
		 * scoreComment.setUser_id(loggedInUser);
		 * System.out.println("scoreComment->"+scoreComment);
		 * MK_Service_Interface.insertMentorComment(scoreComment);
		 * 
		 * System.out.println("매퍼완료"); return scoreComment;
		 */

	  
	  
	  
		@GetMapping("ex")
		public String MyProject1(HttpSession session, Model model ,HttpServletRequest request,
				@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
				@RequestParam(value = "pageSize", defaultValue = "12") int pageSize) {
			System.out.println("MKController start MyProject");

			// 세션에서 로그인된 사용자 아이디를 가져옴
			String loggedInUser = (String) session.getAttribute("userId");

			if (loggedInUser != null) {
				// Board_tbl -> board_id, user_id, board_title,board_edate,boare_wdate
				// ,board_type,pr_price 조회
				List<ProjectResultDTO> myProjectBoard = MK_Service_Interface.myProjectBoard(loggedInUser);

				model.addAttribute("boards", myProjectBoard);
				System.out.println("MKController MyProjectBoard list.size()" + myProjectBoard.size());
				System.out.println(myProjectBoard);
				return "MK_views/ex";

			} else {
				// 현재 페이지의 URL을 세션에 저장
				String requestURL = request.getRequestURL().toString();
				session.setAttribute("redirectUrl", requestURL);

				// 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
				return "/YS_views/login";
			}
		}
}

