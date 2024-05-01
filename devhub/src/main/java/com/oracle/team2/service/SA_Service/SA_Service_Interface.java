package com.oracle.team2.service.SA_Service;

import java.util.List;

import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.BookMark;
import com.oracle.team2.model.Group;

public interface SA_Service_Interface {
	
	Board findBoard(Board board);

	int totalBoard(Board board);
	
	List<Board> saMyBoard(Board board);
	
	List<Group> saGetApplyDetails(String board_id);

	List<Board> saBookmarkList(BookMark bookMark);

	Apply saFindApply(Apply apply);

	int saAcceptUser(Apply apply);
	
	Group saFindGroup(Apply apply);

	int saRejectUser(Apply apply);

	int totalBmk(BookMark bookMark);

	int saBookmarkCancle(BookMark bookMark);

}
