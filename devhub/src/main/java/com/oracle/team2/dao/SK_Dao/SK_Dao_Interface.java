package com.oracle.team2.dao.SK_Dao;

import java.util.List;

import com.oracle.team2.model.Apply;
import com.oracle.team2.model.BookMark;
import com.oracle.team2.model.Comment;
import com.oracle.team2.model.Group;
import com.oracle.team2.model.Grp_skill;
import com.oracle.team2.model.Region;
import com.oracle.team2.model.SK_Board_Card;
import com.oracle.team2.model.SK_board_write;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;


public interface SK_Dao_Interface {

	List<SK_Board_Card> find_AllCard(SK_Board_Card sk_Board_Card);

	List<SK_Board_Card> headCardType(SK_Board_Card sk_Board_Card);

	int totalAllCard(SK_Board_Card sk_Board_Card);

	SK_board_write insertBoard(SK_board_write board_write);

	List<Skill> getSkillList();

	SK_Board_Card getDetailBoard(String board_id);

	List<Group> getBoardDetailGroup(String board_id);

	void applyinsert(Apply apply);


	void updateBookmark(BookMark bookMark);
	
	
	//======================================================
	
	// 댓글 저장
	int commentSave(Comment comment);
	// 댓글의 총 개수
	int getTotalCmtCount(Comment comment);
	// 페이징 해서 리스트 받을꺼
	List<Comment> getcommentListCnt(Comment comment);
	// 댓글 삭제
	int replydelete(Comment comment);

	List<Skill> getAllSkills();

	List<Region> getAllRegions();

	List<SK_Board_Card> doFilter(SK_Board_Card sk_Board_Card);

	List<Apply> getAcceptedApplies(String board_id);

	
	// ===========================================================




}
