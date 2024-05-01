package com.oracle.team2.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.BookMark;
import com.oracle.team2.model.Group;
import com.oracle.team2.model.User;
import com.oracle.team2.service.SA_Service.SA_Paging;
import com.oracle.team2.service.SA_Service.SA_Service_Interface;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

//@RestController
@Controller
@RequiredArgsConstructor
public class SA_Controller {
	
	private final SA_Service_Interface saService;
	
	// 로그인한 유저아이디를 session에서 불러오기 위해
	private final CommonController cc;
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 (전체)
	@RequestMapping(value = "/viewAppliers")
	public String viewAppliers(Model model, HttpServletRequest request) {
		
		if(!cc.loginCheck(request, "viewAppliers")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		
		System.out.println("SA_Controller viewAppliers Start!");
		
		// 로그인한 유저아이디
		String userId = cc.getUserIdInSession(request);
		System.out.println("로그인 한 유저아이디: " + userId);
		
////////////////////////////////////		
		
		// 페이징
		
		// 보드 객체 세팅
		Board board = new Board();
		board.setUser_id(userId);
		
		int totalBoard = saService.totalBoard(board);
		System.out.println("totalBoard 개수: " + totalBoard);
		
		if (board.getCurrentPage() < 1) board.setCurrentPage(1);
		System.out.println("보드 currentPage 확인: " + board.getCurrentPage());
		
		SA_Paging page = new SA_Paging(totalBoard, board.getCurrentPage());
		System.out.println("보드 page: " + page);
		
		board.setStart(page.getStart());
		board.setEnd(page.getEnd());
		
		System.out.println("보드 정보: " + board);
		
		
////////////////////////////////////////////		
		
		List<Board> listMyBoard = saService.saMyBoard(board);
		
		model.addAttribute("listMyBoard", listMyBoard);
		model.addAttribute("page", page);
		model.addAttribute("saBoardStatus", board.getBoard_status());
		
		return "/SA_views/viewAppliers";
		
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 ajax
	@RequestMapping(value = "/allPage")
	public String allPage(Board board, Model model, HttpServletRequest request) {
			
		System.out.println("SA_Controller allPage Start!");
		System.out.println("board 파라미터"+ board);
		// 로그인한 유저아이디
		String userId = cc.getUserIdInSession(request);
			
		// 보드 객체 세팅
		board.setUser_id(cc.getUserIdInSession(request));
		
		// 페이징
		int totalBoard = saService.totalBoard(board);
		if (board.getCurrentPage() < 1) board.setCurrentPage(1);
		System.out.println("현재 보드 페이지: " + board.getCurrentPage());
		SA_Paging page = new SA_Paging(totalBoard, board.getCurrentPage());
		board.setStart(page.getStart());
		board.setEnd(page.getEnd());
			
		// 리스트 새로 불러오기
		List<Board> listMyBoard = saService.saMyBoard(board);
			
		model.addAttribute("listMyBoard", listMyBoard);
		model.addAttribute("page", page);
		model.addAttribute("saBoardStatus", board.getBoard_status());
			
		return "/SA_views/viewAppliers::#profile";
		
	}

	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황
	@RequestMapping(value = "/applyDetail")
	public String applyDetail(@RequestParam("board_id") String board_id, Board board, Model model, HttpServletRequest request) {
		
		if(!cc.loginCheck(request, "applyDetail")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		
		System.out.println("SA_Controller applyDetail Start!");
		System.out.println("클릭한 보드 아이디: " + board_id);
		
		// 파라미터 보드아이디로 보드정보 얻기
		board.setBoard_id(Long.parseLong(board_id));
		Board findBoard = saService.findBoard(board);
		
		model.addAttribute("boardType", findBoard.getBoard_type());
		
		List<Group> listApplyDetailInfo = saService.saGetApplyDetails(board_id);
		model.addAttribute("listApplyDetailInfo", listApplyDetailInfo);
		
		return "/SA_views/applyDetail";
		
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 >  수락 버튼 ajax
	@ResponseBody
	@RequestMapping(value = "/accept")
	public int acceptApplier(@RequestBody Apply apply, Model model) {
		
		int result = 0;
		System.out.println("SA_Controller acceptApplier Start!");
		System.out.println("수락 파라미터 apply: " + apply);
		
		// 일단 파라미터 board_id, group_id, user_id로 해당 유저 정보 가져옴
		Apply findApply = saService.saFindApply(apply);
		System.out.println("서비스에 넘겨줄 apply: " + findApply);
		
		// 수락 비즈니스 로직
		// 프로시저 호출할거다
		saService.saAcceptUser(findApply);
	
		// 수락 후 변경된 group_tbl 정보 가져오기
		Group group = saService.saFindGroup(apply);
		
		// 변경된 accept_num
		result = group.getAccept_num();
		
		return result;
		
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 >  거절 버튼 ajax
	@ResponseBody
	@RequestMapping(value = "/reject")
	public int rejectApplier(@RequestBody Apply apply) {
		
		int result = 0;
		
		System.out.println("SA_Controller rejectApplier Start!");
		System.out.println("거절 파라미터 apply: " + apply);
		
		// 일단 파라미터 board_id, group_id, user_id로 해당 유저 정보 가져옴
		Apply findApply = saService.saFindApply(apply);
		System.out.println("서비스에 넘겨줄 apply: " + findApply);
		
		// 거절 비즈니스 로직
		result = saService.saRejectUser(findApply);
		System.out.println("거절 결과 int result: " + result);

		return result;
		
	}
	
	// 메인 > 드랍다운 > 북마크
	@RequestMapping(value = "/bookmark")
	public String bookmark(User user, Model model, HttpServletRequest request) {
		
		if(!cc.loginCheck(request, "bookmark")) {
			// 로그인 안 되어있으면 로그인 페이지로
			return "forward:loginView";
		}
		
		System.out.println("SA_Controller bookmark Start!");
		
		// 로그인한 유저아이디
		String userId = cc.getUserIdInSession(request);
		System.out.println("로그인 한 유저아이디: " + userId);
								
		// 유저 객체 세팅
		user.setUser_id(cc.getUserIdInSession(request));
		
		
//		페이징
		BookMark bookMark = new BookMark();
		bookMark.setUser_id(userId);
		
		int totalBmk = saService.totalBmk(bookMark);
		System.out.println("totalBoard 개수: " + totalBmk);
		System.out.println("세팅 전 currentPage: " + bookMark.getCurrentPage());
		
		if (bookMark.getCurrentPage() < 1) bookMark.setCurrentPage(1);
		System.out.println("bookMark currentPage 확인: " + bookMark.getCurrentPage());
		
		SA_Paging page = new SA_Paging(totalBmk, bookMark.getCurrentPage());
		System.out.println("page: " + page);
		
		bookMark.setStart(page.getStart());
		bookMark.setEnd(page.getEnd());
		System.out.println("stat pg: " + page.getStart());
		System.out.println("end pg: " + page.getEnd());
		
		System.out.println("북마크정보: " + bookMark);
		
////////////////		
						
		// Board, User 조인해서 북마크 카드에 뿌릴거 가져와보기
		List<Board> listBookmark = saService.saBookmarkList(bookMark);
						
		// 카드에 전달할 값
		model.addAttribute("listBookmark", listBookmark);
		model.addAttribute("page", page);
	
		return "/SA_views/bookmark";
		
	}
	
	// 북마크 페이지 ajax
	@RequestMapping(value = "bmkPaging")
	public String bmkPaging(BookMark bookMark, User user, Model model, HttpServletRequest request) {
		
		System.out.println("SA_Controller bmkPaging Start!");
		
		// 로그인한 유저아이디
		String userId = cc.getUserIdInSession(request);
								
		// 유저 객체 세팅
		user.setUser_id(cc.getUserIdInSession(request));
		
//		페이징
		bookMark.setUser_id(userId);
		
		int totalBmk = saService.totalBmk(bookMark);
		System.out.println("totalBoard 개수: " + totalBmk);
		
		if (bookMark.getCurrentPage() < 1) bookMark.setCurrentPage(1);
		System.out.println("bookMark currentPage 확인: " + bookMark.getCurrentPage());
		
		SA_Paging page = new SA_Paging(totalBmk, bookMark.getCurrentPage());
		System.out.println("page: " + page);
		
		bookMark.setStart(page.getStart());
		bookMark.setEnd(page.getEnd());
		
		System.out.println("stat pg: " + page.getStart());
		System.out.println("end pg: " + page.getEnd());
		
		System.out.println("북마크정보: " + bookMark);
		
////////////////		
						
		// Board, User 조인해서 북마크 카드에 뿌릴거 가져와보기
		List<Board> listBookmark = saService.saBookmarkList(bookMark);
						
		// 카드에 전달할 값
		model.addAttribute("listBookmark", listBookmark);
		model.addAttribute("page", page);
		
		return "/SA_views/bookmark::#content";
	}
	
	// 메인 > 드랍다운 > 북마크 > 북마크 취소 버튼 ajax
	@RequestMapping(value = "/bookmarkCancle")
	public String bookmarkCancle(@RequestParam("board_id") String board_id,
			User user, BookMark bookMark, Model model, HttpServletRequest request) {
		
		System.out.println("SA_Controller bookmark Start!");
		
		// 로그인한 유저아이디
		String userId = cc.getUserIdInSession(request);
		System.out.println("로그인 한 유저아이디: " + userId);
		
		System.out.println("파라미터 보드아이디: " + bookMark.getBoard_id());
						
		// 유저 객체 세팅
		user.setUser_id(cc.getUserIdInSession(request));
		
		// 북마크 객체 세팅
		bookMark.setUser_id(userId);
		bookMark.setBoard_id(Long.parseLong(board_id));
		
		// 버튼 DML
		int result = saService.saBookmarkCancle(bookMark);
		
		System.out.println("컨트롤러 결과: " + result);
		
//		 리스트 불러오기 전에 페이징을 다시? //////////////////////////
		int totalBmk = saService.totalBmk(bookMark);
		System.out.println("totalBoard 개수: " + totalBmk);
		
		if (bookMark.getCurrentPage() < 1) bookMark.setCurrentPage(1);
		System.out.println("bookMark currentPage 확인: " + bookMark.getCurrentPage());
		
		SA_Paging page = new SA_Paging(totalBmk, bookMark.getCurrentPage());
		System.out.println("page: " + page);
		
		bookMark.setStart(page.getStart());
		bookMark.setEnd(page.getEnd());
		
		model.addAttribute("page", page);
		////////////////////////////
		
		// DML 실행 후의 북마크 리스트 다시 불러오기
		List<Board> listBookmark = saService.saBookmarkList(bookMark);
								
		model.addAttribute("listBookmark", listBookmark);
		
		// ajax로 껍데기 갈아끼우기
		return "/SA_views/bookmark::#content";
	}
	
}
