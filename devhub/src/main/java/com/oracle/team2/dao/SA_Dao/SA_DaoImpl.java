package com.oracle.team2.dao.SA_Dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.BookMark;
import com.oracle.team2.model.Group;
import com.oracle.team2.model.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SA_DaoImpl implements SA_Dao_Interface {
	
	private final SqlSession session;
	private final PlatformTransactionManager tm;

	// 보드 정보 조회
	@Override
	public Board findBoard(Board board) {

		Board findBoard = session.selectOne("saFindBoard", board);
		
		return findBoard;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 페이징
	@Override
	public int totalBoard(Board board) {

		int result = 0;
		
		try {
			
			result = session.selectOne("saTotalBoard", board);
			System.out.println("dao 페이징 결과: " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	
		return result;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 리스트
	@Override
	public List<Board> saMyBoard(Board board) {

		System.out.println("SA_DaoImpl saMyBoard Start!");
		
		List<Board> listMyBoard = null;
		
		try {
			System.out.println("조회전 board : "+board);
			listMyBoard = session.selectList("SA_myBoard", board);
			System.out.println("listMyBoard.size(): " + listMyBoard);
			
			if (listMyBoard.size() != 0) {
				
				// Board 안의 Skill 세팅
				for(Board board1 : listMyBoard) {
					
					board1.setSkills(session.selectList("saBoardSkills", board1.getBoard_id()));
					System.out.println("보드별 스킬 3개: " + board1.getSkills());
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return listMyBoard;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 리스트
	@Override
	public List<Group> saGetApplyDetails(String board_id){
		
		System.out.println("SA_DaoImpl saGetApplyDetails Start!");
		
		System.out.println("클릭한 보드아이디: " + board_id);
		List<Group> groupList = null;
		
		try {
			
			// ApplyDetail에 보드아이디 넣어서 그룹 정보 조회
			groupList = session.selectList("saGroupList", board_id);
			System.out.println("saGroupList: "+ groupList);

			if(groupList.size()!=0) {
				
				// 그룹리스트 안의 유저리스트 세팅
				for(Group group : groupList) {
					
					group.setUsers(session.selectList("saApplyUsers", group));
					System.out.println("유저리스트 사이즈: "+group.getUsers().size());
					System.out.println("saApplyUsers: "+ group.getUsers()); 
					
					// 유저 타입의 리스트가 ad.getUsers()
					if(group.getUsers().size()!=0) {
						
						for(User user : group.getUsers()) {
							
							System.out.println("user에 담긴 유저아이디: "+user.getUser_id());
							
							user.setSkills(session.selectList("saUserSkills", user));
							System.out.println("스킬리스트 사이즈: "+user.getSkills().size());
							System.out.println("saUserSkills: "+ user.getSkills());
							
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return groupList;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 정보 불러오기
	@Override
	public Apply saFindApply(Apply apply) {
		
		Apply findApply = session.selectOne("saFindApply", apply);
		
		return findApply;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 리스트 > 수락
	@Override
	public int saAcceptUser(Apply apply) {
		
		int result = 0;
		System.out.println("SA_DaoImpl saAcceptUser Start!");
		System.out.println("dao 파라미터 apply: " + apply);
		
		TransactionStatus tStatus = tm.getTransaction(new TransactionDefinition() {
		});
		
		try {
			
			session.update("saAcceptUser", apply);
			session.insert("saAcceptUserAlarm",apply);
			tm.commit(tStatus);
			System.out.println("Update Commit!!!");
			
			// 프로시저 호출 성공 결과값을 1로 고정
			result = 1;
			
			System.out.println("수락 dao int result: " + result);
			
		} catch (Exception e) {
			
			tm.rollback(tStatus);
			System.out.println("Update Rollbackkkkkkkkkkkkk");
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return result;
	}
	
	// 지원자 수락 후 변경된 group_tbl 정보 불러오기
	@Override
	public Group saFindGroup(Apply apply) {

		System.out.println("SA_DaoImpl saFindGroup Start!");
		
		Group group = null;
		
		System.out.println("SA_DaoImpl saFindGroup apply->"+apply);
		group = session.selectOne("saFindGroup", apply);
		System.out.println("dao 결과 group: " + group);
		
		return group;
	}
	
	// 메인 > 드랍다운 > 내가 쓴 글 관리 > 지원자 현황 리스트 > 거절
	@Override
	public int saRejectUser(Apply apply) {

		int result = 0;
		System.out.println("SA_DaoImpl rejectApplier Start!");
		System.out.println("dao 파라미터 apply: " + apply);
		
		TransactionStatus tStatus = tm.getTransaction(new TransactionDefinition() {
		});
		
		try {
			
			result = session.update("saRejectUser", apply);
			session.insert("saRejectUserAlarm",apply);
			System.out.println("dao int result: " + result);
			
			tm.commit(tStatus);
			System.out.println("Update Commit!!!");
			
		} catch (Exception e) {
			
			tm.rollback(tStatus);
			System.out.println("Update Rollbackkkkkkkkkkkkk");
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return result;
	}

	// 북마크 페이징
	@Override
	public int totalBmk(BookMark bookMark) {
		
		int result = 0;
		
		try {
			
			result = session.selectOne("saTotalBookmark", bookMark);
			System.out.println("dao 북마크 페이징 결과: " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return result;
	}
	
	// 메인 > 드랍다운 > 북마크 리스트
	@Override
	public List<Board> saBookmarkList(BookMark bookMark) {

		System.out.println("SA_DaoImpl saBookmarkList Start!");
		
		List<Board> listBookmark = null;
		
		try {
			
			listBookmark = session.selectList("SA_bookmark", bookMark);
			System.out.println("listBookmark.size()->" + listBookmark.size());
			System.out.println("listBookmark: " + listBookmark);
			
			if (listBookmark.size() != 0) {
				
				// Board 안의 Skill 세팅
				for(Board board : listBookmark) {
					
					board.setSkills(session.selectList("saBoardSkills", board.getBoard_id()));
					System.out.println("보드별 스킬 3개: " + board.getSkills());
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return listBookmark;
	}

	// 북마크 취소
	@Override
	public int saBookmarkCancle(BookMark bookMark) {

		System.out.println("SA_DaoImpl saBookmarkCancle Start!");
		
		int result = 0;
		
		TransactionStatus tStatus = tm.getTransaction(new TransactionDefinition() {
		});
		
		try {
			
			result = session.delete("saBookmarkCancle", bookMark);
			System.out.println("int result: " + result);
			
			tm.commit(tStatus);
			System.out.println("Delete Commit!!!");
			
		} catch (Exception e) {
			
			tm.rollback(tStatus);
			System.out.println("Delete Rollback!");
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return result;
	}

}
