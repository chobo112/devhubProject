package com.oracle.team2.service.SK_Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.team2.dao.SK_Dao.SK_Dao_Interface;
import com.oracle.team2.model.Apply;
import com.oracle.team2.model.Board;
import com.oracle.team2.model.BookMark;
import com.oracle.team2.model.Comment;
import com.oracle.team2.model.Group;
import com.oracle.team2.model.Grp_skill;
import com.oracle.team2.model.Region;
import com.oracle.team2.model.SK_Board_Card;
import com.oracle.team2.model.SK_board_write;
import com.oracle.team2.model.Skill;
import com.oracle.team2.model.User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SK_ServiceImpl implements SK_Service_Interface {

	private final SK_Dao_Interface sk_Dao_Interface;

		//head에서 게시판전체보기~팀원 클릭시에 원하는 화면 나오게 하는법
		@Override
		public List<SK_Board_Card> headCard(SK_Board_Card sk_Board_Card) {
			List<SK_Board_Card> find_SK_headtype_Card = null;
			
			System.out.println("Sk_service에서 헤드용 find_SK_headtype_Card시작됨");
			
			if("0".equals(sk_Board_Card.getBoard_type())) { // 전체 조회
				//find_SK_headtype_Card = SK_Dao_Interface.find_AllCard(sk_Board_Card).size();
				find_SK_headtype_Card = sk_Dao_Interface.find_AllCard(sk_Board_Card);
				System.out.println("service head용 findheadtypecard좀 되자고--->"+find_SK_headtype_Card);
				
				//int size = find_SK_headtype_Card.size();
			} else { // 나머지는 타입별로 가져오는 것
				System.out.println("board타입이 전체보기가 아닌경우에는 else문에서 시작됨");
				find_SK_headtype_Card = sk_Dao_Interface.headCardType(sk_Board_Card);
			}
			
			
			return find_SK_headtype_Card;
		}
	
	
	
	// ajax 전체보기, 팀원모집, 멘토모집, 멘토주도는 sql에서 불러오는 쿼리문이 다름.
	@Override
	//전체보기 카드 불러오기
	public List<SK_Board_Card> find_AllCard(SK_Board_Card sk_Board_Card) {
		List<SK_Board_Card> sk_Board_CardList = null;
		System.out.println("SK_ServiceImpl의 find_AllCard 메서드 시작 "); //여기서 가는ㄴ중 이제 안오는중
		
		sk_Board_CardList = sk_Dao_Interface.find_AllCard(sk_Board_Card);
		System.out.println("SK_serviceImpl의 find_AllCard에서 find_AllCard.size()->"+sk_Board_CardList.size());
		System.out.println(sk_Board_CardList);
		
		return sk_Board_CardList;
	}
	

	//전체카드 가져오기 -> 코드재활용
	@Override
	public int totalAllCard(SK_Board_Card sk_Board_Card) {
		System.out.println("서비스에서 totalAllCard start+sk_Board_Card(파라미터 확인) -> "+sk_Board_Card);
		int totalAllcardCnt =  sk_Dao_Interface.totalAllCard(sk_Board_Card);

		return totalAllcardCnt;
	}



	//게시글 등록하기 -> insert
	@Override
	public SK_board_write insertBoard(SK_board_write boardWrite) {
		System.out.println("SK_ServiceImpl InsertBoard boardWrite --> " + boardWrite);
		
		List <Group> groupList = new ArrayList<>();
		//가져온 그룹만큰 쪼개기
		int startIndex = 1;
		if(boardWrite.getBoard_type().equals("2"))
			startIndex = 0;
		for (int i = startIndex; i < boardWrite.getGroupIndexArray().length; i++) {
			Group group = new Group();
			Long longGroupId=Long.valueOf(boardWrite.getGroupIndexArray()[i]+1);
			group.setGroup_id(longGroupId); //valueOf를 써야함
			System.out.println("groupid로 사용할 indexArray =>"+longGroupId);
			
			group.setGroup_num(boardWrite.getGroupNumArray()[i]); //그룹인원수 넣어주기
			
			System.out.println("grouplist를 group으로 넣어주기 --> insert를 위한 그룹들 분리 -->"+group.getGroup_num());
			
			//스킬담자
			List <Grp_skill> grpSkillList = new ArrayList<>();
			String[] skillIdArray= boardWrite.getSkillImgArray().get(i); //
			for(String id : skillIdArray) {
				Grp_skill gskill = new Grp_skill();
				gskill.setGroup_id(longGroupId);
				gskill.setSkill_id(id);
				grpSkillList.add(gskill);
				
				System.out.println("그룹이미지배열리스트 -> 스킬배열로넣고 -> 향상된포문으로 id하나씩 가져오고"
						+ "			가져온 id를 1개 1개 skillList에다가 넣어주자 => 가져온 스킬들의 id들 ->>"+grpSkillList);

			}

			//그럼 위에 리스트 스킬리스트에 각각의 스킬들이 담겨져 있음. group안에 List<skill>을 처리하자
			group.setGrpSkills(grpSkillList);
			groupList.add(group);
			System.out.println("groupList에 하나하나 담긴 그룹들 ->"+groupList);
		}
		System.out.println("SK_ServiceImpl InsertBoard groupList --> " + groupList.size());
		//System.out.println("SK_ServiceImpl InsertBoard groupList1 --> " + groupList.get(0).getGrpSkills().size());
		//System.out.println("SK_ServiceImpl InsertBoard groupList2 --> " + groupList.get(1).getGrpSkills().size());
		//System.out.println("SK_ServiceImpl InsertBoard groupList3 --> " + groupList.get(2).getSkills().size());
		
		//위에서 그룹들을 나눈것들을 다시 boardWrite에 넣어줘야됨
		boardWrite.setGroups(groupList);
		
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		SK_board_write skBoard = sk_Dao_Interface.insertBoard(boardWrite);
		System.out.println("SK_ServiceImpl InsertBoard skBoard --> " + skBoard);

		return skBoard;
		//return null;
	}
	
	
	  //글등록폼에서 스킬목록 전체 가져오기 -> datalist 스킬목록가져오기
	   @Override
	   public List<Skill> getSkillList() {
	      System.out.println("SK_ServiceImpl getSkillList Start... ");
	      
	      List<Skill> skillList =sk_Dao_Interface.getSkillList();
	      
	      return skillList;
	   }


	//게시판 등록글에서 보드가져오기
	@Override
	public SK_Board_Card getBoardById(String board_id) {
		System.out.println("service에서 getBoardById start");
		System.out.println("service에서 board_id"+board_id);
		
		SK_Board_Card board_detail = sk_Dao_Interface.getDetailBoard(board_id);
		System.out.println("service에서 board_detail_list" + board_detail);
		
		return board_detail;
	}


	//게시판 등록글에 그룹가져오기
	@Override
	public List<Group> getGroupsByBoardId(String board_id) {
		System.out.println("service에서 group애들얻어오기");
		List<Group> board_grouplist = sk_Dao_Interface.getBoardDetailGroup(board_id);
		
		System.out.println("service에서 board_grouplist ->" + board_grouplist);
		return board_grouplist;
	}


	
	//카드상세페이지 -> 신청
	@Override
	public void apply(Apply apply) {
		System.out.println("service에서 apply start");
		sk_Dao_Interface.applyinsert(apply);
		//System.out.println("service에서 Applyinsert를 실행한뒤에 apply(등록하기 제대로 된거면 1을 반환) -->"+apply);
	}





	
	//게시판 화면 -> 북마크 클릭 -> 북마크 업데이트
	@Override
	public void bmkUpdate(BookMark bookMark) {
		System.out.println("서비스에서 bmkUpdate start");
		sk_Dao_Interface.updateBookmark(bookMark);
	}


	// ===============================================================================
	
	// 댓글 insert 
	@Override
	public int commentSave(Comment comment) {
		int commentSave = sk_Dao_Interface.commentSave(comment);
		
		return commentSave;
	} 

	// 댓글 삭제
	@Override
	public void replydelete(Comment comment) {
		int replydelete = sk_Dao_Interface.replydelete(comment);
		
	}

	// 댓글의 총 갯수
	@Override
	public int getTotalCmtCount(Comment comment) {
		System.out.println("SK_ServiceImpl getTotalCmtCount comment start");
		return sk_Dao_Interface.getTotalCmtCount(comment);
	}

	// 페이징 작업 리스트
	@Override
	public List<Comment> getCommentList(Comment comment) {
		System.out.println("SK_ServiceImpl getCommentList comment start"+comment);
		List<Comment> commentList = sk_Dao_Interface.getcommentListCnt(comment);
		System.out.println("SK_ServiceImpl getCommentList commentList size:" + commentList.size());
		return commentList;
	}



//==========================================================================================
	
	//게시판 => 기술필터 기술목록 조회해오기
	@Override
	public List<Skill> getAllSkills() {
		System.out.println("SK_ServiceImpl getAllSkills start");
		
		List<Skill> skillList = sk_Dao_Interface.getAllSkills(); 
		System.out.println("SK_ServiceImpl getAllSkills start skillList.size()  " + skillList.size());
		
		return skillList;
	}
	
	
	//게시판 => 지역필터 지역들 조회해오기
	@Override
	public List<Region> getAllRegions() {
		System.out.println("SK_ServiceImpl getAllRegions start");
		List<Region> regions = sk_Dao_Interface.getAllRegions();
		System.out.println("SK_ServiceImpl getAllRegions start regions.size(  " + regions.size());
		
		return regions;
	}
	
	
	//게시판 -> 필터링 작업 수행하
	@Override
	public List<SK_Board_Card> doFilter(SK_Board_Card sk_Board_Card) {
		System.out.println("SK_ServiceImpl getFilter start");
		//Paging page = new Paging();
		List<SK_Board_Card> filterList = sk_Dao_Interface.doFilter(sk_Board_Card);
		System.out.println("SK_ServiceImpl DoFilter start filterList  " + filterList);
		
		return filterList;
	}



	@Override
	public List<Apply> getAceeptedUsers(String board_id) {
		System.out.println("Dao에서 getAceeptedUsers start가 되었습니다!");
		
		List<Apply> AcceptedApplies = sk_Dao_Interface.getAcceptedApplies(board_id);
		
		System.out.println("service에서 Dao의 결과를 받아온 AcceptedApplies=====>"+AcceptedApplies);
		
		return AcceptedApplies;
	}






	
	
}
