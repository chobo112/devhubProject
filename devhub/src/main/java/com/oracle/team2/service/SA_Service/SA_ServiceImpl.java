package com.oracle.team2.service.SA_Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oracle.team2.dao.SA_Dao.SA_Dao_Interface;
import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.BookMark;
import com.oracle.team2.model.Group;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SA_ServiceImpl implements SA_Service_Interface{
	
	private final SA_Dao_Interface saDao;
	
	// 보드 정보 조회
	@Override
	public Board findBoard(Board board) {
		
		Board findBoard = saDao.findBoard(board);
		return findBoard;
	}

	// 메인 > 드랍다운 > 내가 쓴 글 페이징
	@Override
	public int totalBoard(Board board) {

		int result = saDao.totalBoard(board);
		System.out.println("SA_ServiceImpl 페이징 결과: " + result);
		return result;
	
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 리스트
	@Override
	public List<Board> saMyBoard(Board board) {

		System.out.println("SA_ServiceImpl saMyBoard Start!");
		
		List<Board> listMyBoard = saDao.saMyBoard(board);
		System.out.println("listMyBoard.size(): " + listMyBoard.size());
		
		return listMyBoard;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 리스트
	@Override
	public List<Group> saGetApplyDetails(String board_id) {
		
		System.out.println("SA_ServiceImpl getApplyDetails Start!");
		
		List<Group> groupList = saDao.saGetApplyDetails(board_id);
		System.out.println("groupList.size(): " + groupList.size());
		
		return groupList;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 정보 불러오기
	@Override
	public Apply saFindApply(Apply apply) {

		Apply findApply = saDao.saFindApply(apply);
		return findApply;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 리스트 > 수락
	@Override
	public int saAcceptUser(Apply apply) {

		System.out.println("SA_ServiceImpl saAcceptUser Start!");

		int result = saDao.saAcceptUser(apply);

		return result;
	}
	
	// 지원자 수락 후 변경된 group_tbl 정보 불러오기
	@Override
	public Group saFindGroup(Apply apply) {

		System.out.println("SA_ServiceImpl saFindGroup Start!");
		
		Group group = saDao.saFindGroup(apply);
		
		return group;
	
	}

	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 리스트 > 거절
	@Override
	public int saRejectUser(Apply apply) {

		System.out.println("SA_ServiceImpl saRejectUser Start!");

		int result = saDao.saRejectUser(apply);

		return result;
	}
	
	// 북마크 페이징
	@Override
	public int totalBmk(BookMark bookMark) {

		int result = saDao.totalBmk(bookMark);
		return result;
	}
	
	// 메인 > 드랍다운 > 북마크 리스트
	@Override
	public List<Board> saBookmarkList(BookMark bookMark) {

		System.out.println("SA_ServiceImpl saBookmarkList Start!");
		
		List<Board> listBookmark = saDao.saBookmarkList(bookMark);
		System.out.println("listBookmark.size(): " + listBookmark.size());
		
		return listBookmark;
	}

	// 북마크 취소
	@Override
	public int saBookmarkCancle(BookMark bookMark) {

		System.out.println("SA_ServiceImpl saBookmarkCancle Start!");
		
		int result = saDao.saBookmarkCancle(bookMark);
		
		return result;
	}

}
