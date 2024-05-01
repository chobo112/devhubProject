package com.oracle.team2.service.SK_Service;

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


public interface SK_Service_Interface {


	List<SK_Board_Card> find_AllCard(SK_Board_Card sk_Board_Card);

	List<SK_Board_Card> headCard(SK_Board_Card sk_Board_Card);

	int totalAllCard(SK_Board_Card sk_Board_Card);

	SK_board_write insertBoard(SK_board_write board_registerList);

	List<Skill> getSkillList();

	SK_Board_Card getBoardById(String board_id);

	List<Group> getGroupsByBoardId(String board_id);

	void apply(Apply apply);

	void bmkUpdate(BookMark bookMark);

	// =============================================================
	// 댓글 insert
	int commentSave(Comment comment);
	// 댓글 삭제
	void replydelete(Comment comment);
	// 댓글의 총 갯수 
	int getTotalCmtCount(Comment comment);
	
	// =============================================================

	// 페이징 리스트
	List<Comment> getCommentList(Comment comment);

	List<Skill> getAllSkills();

	List<Region> getAllRegions();

	List<SK_Board_Card> doFilter(SK_Board_Card sk_Board_Card);

	List<Apply> getAceeptedUsers(String board_id);


	



	

}
