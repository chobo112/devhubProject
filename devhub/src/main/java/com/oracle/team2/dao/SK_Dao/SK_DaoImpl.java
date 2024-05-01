package com.oracle.team2.dao.SK_Dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SK_DaoImpl implements SK_Dao_Interface {
   
   private final SqlSession session;
   
   //userId, boardType으로 파라미터로 받아서 페이지 넘겨주는 작업
   //boardType은 html로부터 0이면 전체게시판 ~~ 3이면 팀원모집게시판을 불러오게 됨
   //head에서 가져옴
   @Override
   public List<SK_Board_Card> headCardType(SK_Board_Card sk_Board_Card) {
      //System.out.println("SK_DaoImpl test start...");
      List<SK_Board_Card> cardList = null;
      System.out.println("Dao에서 headCardType=>"+sk_Board_Card);
      
      try {
         cardList = session.selectList("sk_head_typeList",sk_Board_Card);
         System.out.println("cardList"+cardList);

      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return cardList;
   }

   //게시판 카드전부 가져오기
   @Override
   public List<SK_Board_Card> find_AllCard(SK_Board_Card sk_Board_Card) {
      
      List<SK_Board_Card> SK_boardlist = null;
      //System.out.println("SK_DaoImpl find_AllCard start");
      System.out.println("SK_DaoImpl find_AllCard sk_Board_Card->"+sk_Board_Card);
      
      try {
         
         SK_boardlist = session.selectList("sk_head_typeList", sk_Board_Card);
         
         System.out.println("SK_DaoImpl find_AllCard SK_boardlist.size()---->"+SK_boardlist.size());
         //System.out.println(SK_boardlist);
         
      
      } catch (Exception e) {
         System.out.println("SK_DaoImpl find_SK_Board e.getMessage()->"+e.getMessage());
      }
      
      //System.out.println(SK_boardlist);
      return SK_boardlist;
   }
   

   @Override //페이징
   public int totalAllCard(SK_Board_Card sk_Board_Card) {
      int totAllCardCount = 0;
      //System.out.println("sk_DaoImpl Start totalAllCard..." );

      try {
         totAllCardCount = session.selectOne("totalAllCard",sk_Board_Card);
         System.out.println("handlerCardPaing부터 DAO까지 totalEmp totAllCardCount->" +totAllCardCount);
      } catch (Exception e) {
         //System.out.println("DAO에서 totalEmp Exception->"+e.getMessage());
      }
      return totAllCardCount;   
   }

   //게시판 글등록
   @Override
   @Transactional
   public SK_board_write insertBoard(SK_board_write board_write) {
      //System.out.println("DAO에서 insertBoard가 시작됨-->"+board_write);
       //System.out.println("Dao에서 group에 값이 들어가있는것들 ->"+board_write.getGroupIndexArray().toString());
        //System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD board_write.getGroupIndexArray()"+ board_write.getGroupIndexArray());
       System.out.println("@@@SK_DaoImpl insertBoard board_write->"+board_write);
	   
        try {
            // 1. 게시글 삽입
           
            int boardInsertCnt =  session.insert("boardInsert", board_write);
            //System.out.println("boardInsertCnt ==>"+boardInsertCnt);
        
            int groupInsertCnt = 0;
            //포문을 돌려서 그룹에 그룹들 넣어주고
            //System.out.println("2단계 getGroups ==>"+board_write.getGroups());
            for(Group group : board_write.getGroups()) {
                //System.out.println("Group에 담긴애들: " + group);
                group.setBoard_id(board_write.getBoard_id());
                // 그룹에 대한 작업을 세션으로 넘겨주고 insert
                groupInsertCnt = session.insert("groupInsert", group);
                //System.out.println("groupInsertCnt ==>"+groupInsertCnt);
                             
                // 각 그룹의 스킬에 대한 작업 수행
                List<Grp_skill> grpSkills = group.getGrpSkills(); // 이 부분도 그룹에 해당하는 스킬 리스트를 가져오는 로직을 구현해야 합니다.
                for(Grp_skill gskill : grpSkills) {
                    // 각 그룹 스킬에 대한 작업을 세션으로 넘겨주고 insert
                   //System.out.println("gskill-->"+gskill);
                    session.insert("grpSkillInsert", gskill);
                
                }
                
            }    
        }catch (Exception e) {
           e.printStackTrace();
        }
        
        return board_write;
   }
   
      @Override //등록폼에서 그룹당 skill이미지들도 나오도록 하자
      public List<Skill> getSkillList() {
         //System.out.println("SK_DaoImpl getSkillList Start... ");
         
         List<Skill> skillList =null;
         try {
            skillList= session.selectList("skSkillList");
            //System.out.println("SK_DaoImpl getSkillList skillList --> " + skillList);
            
         } catch (Exception e) {
            //System.out.println("getSkillList exception->" + e.getMessage());
         }
         return skillList;
      }


   //카드 눌러서 상세게시판 -> board애들 조회(그룹과, 스킬 제외한 모든애들 조회) +조회수 1증가
   @Override
   public SK_Board_Card getDetailBoard(String board_id) {
      //System.out.println("Dao에서 getDetailBoard start");
      
      SK_Board_Card detail_board = null;
      
      try {
         //조회수가 1증가됨
         session.update("updateView",board_id); //쿼리에서는 숫자로 써줘야함
         //그래서 xml에서는 TO_Number로 바꿔줘야함
         
         detail_board = session.selectOne("board_detail", board_id);
         System.out.println("조회로 불러온 detail_board"+detail_board);
         
         
      } catch (Exception e) {
         e.printStackTrace();
      }
      return detail_board;
      
      
   }

   
   
   
   
   
   //카드눌러서 상세게시판 -> 그룹과 스킬 관련된 부분 처리(그룹리스트 -> 스킬리스트 처리)
   @Override
   public List<Group> getBoardDetailGroup(String board_id) {
      //System.out.println("Dao에서 getBoardDetailGroup start");
      //System.out.println("Dao에서 getBoardDetailGroup board_id-->"+board_id);
      
      List<Group> grouplist = null;
      try {
         //board_id로 그룹을 구하자 -> group_num이 필요함.
         grouplist = session.selectList("getGrouplist", board_id);
         //System.out.println("getBoardDetailGroup에서 detail_board_grouplist"+grouplist);
      
         if(grouplist.size() != 0) {//그룹리스트에서 그룹을 뽑아냄
            
            //그룹당 스킬들 꺼내오기 => 스킬들 뽑아주기
            for(Group group : grouplist) {
               //그룹당 스킬리스트를 뽑아야함. 
               group.setSkills(session.selectList("getGroupSkills", group));
               //System.out.println("스킬리스트 사이즈-------->"+ group.getSkills());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         
         
      }
      return grouplist;
      
   }


   //카드상세 -> 신청 -> insert부분 Apply테이블
   @Override
   @Transactional
   public void applyinsert(Apply apply) {
      System.out.println("Dao에서 Applyinsert start & apply에는 값이 잘 들어있니??"+apply);
      System.out.println("Dao에서 applyinsert ===>"+apply);
      int result = 0;
      //insert는 조회하고 있는 경우에만 가능
      try {
    	  System.out.println("인서트로 조회해보자");
    	  Apply apdel = session.selectOne("applyCheck", apply);
    	  Board board = session.selectOne("boardByApply", apply);
    	 if (apdel == null) {
    		 if("1".equals(board.getBoard_type())) {
    			 // 멘토주도 게시판일 경우에 수락 및 알림
    			 apply.setAp_accept("1");
    			 board.setUser_id(apply.getUser_id());
    			 session.insert("acceptPayAlarm", board);
    			 System.out.println("SK_DaoImpl applyinsert apdel->"+ apply);
    			 session.update("increaseAcceptGroupNum", apply);
    		 } else {
    			 apply.setAp_accept("0");
    		 }
    		 result = session.insert("ApplyInsert", apply);
    		 System.out.println("@@@@@@insert@@@@@@@결과가 있으면??--->@@@@@@"+result);
    	 } else {
    	
    		 System.out.println("apply_table에 이미 존재하지만 값이 자꾸 들어갈고 하고 있습니다.");
    	 }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   





   //북마크 수정하기
   @Override
   @Transactional
   public void updateBookmark(BookMark bookMark) {
      System.out.println("DAO에서 updateBookmark start");
      System.out.println("updatBookmark에서 boomark객체 확인 => "+bookMark);
      System.out.println("Dao에서 --> xml로 업데이트 보내보자");
      
      
      try {
         BookMark bm = session.selectOne("bmkCheck", bookMark);
         
         if(  bm != null) {
        	 
         session.delete("bmkDelete", bookMark);
         
         } else {
        	 
         session.insert("bmkInsert", bookMark);
         
         }
         
      }catch (Exception e) {
         System.out.println("작업도중에 오류가 발생하였습니다"+e.getMessage());
      }
            
   }
   
   
   // --------------------------------------------------------------------------------------

   // 댓글 insert
   @Override
   public int commentSave(Comment comment) {
      System.out.println("SK_DaoImpl commentSave start");
      int commentSave = 0;
      try {
         //System.out.println("객체 넘어오는지 확인 : " + comment);
         commentSave = session.update("skCommentSaveInsert", comment);
      } catch (Exception e) {
         System.out.println("dao 오류 e.getMessage()->" + e.getMessage());
      }
      return commentSave;
   }// 댓글 인서트

   // 댓글 조회
   @Override
   public List<Comment> getcommentListCnt(Comment comment) {
      //System.out.println("SK_DaoImpl getcommentListCnt start");
      List<Comment> commentList = null;
      try {
         commentList = session.selectList("skGetCommentList", comment);
         //System.out.println("commentList.size() 사이즈는 ->" + commentList.size());
      } catch (Exception e) {
         //System.out.println("SK_DaoImpl getcommentList e.getMessage()->" + e.getMessage());
      }
      return commentList;
   }// 댓글 조회
   
   // 댓글 리스트 페이징 작업 
   @Override
   public int getTotalCmtCount(Comment comment) {
      //System.out.println("SK_DaoImpl getTotalCmtCount  start");
      int result = 0;
      try {
         result = session.selectOne("skTotalCmtCount", comment);
         //System.out.println("SK_DaoImpl getTotalCmtCount result start" + result);
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return result;
   }

   // 댓글삭제
   @Override
   public int replydelete(Comment comment) {
      int replydelete = 0;

      try {
         replydelete = session.delete("skreplydelete", comment);
      } catch (Exception e) {
         System.out.println("SK_DaoImpl replydelete e.getMessage()->" + e.getMessage());
      }
      return replydelete;
   }

// ==========================================================================================
   
   //게시판 -> 기술스택 필터링 -> 기술목록들 조회
   @Override
   public List<Skill> getAllSkills() {
      //System.out.println("SK_DaoImpl getAllSkills start");
      List<Skill> skillList = null;
      try {
         skillList = session.selectList("skAllSkills");
         System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
         		+ "SK_DaoImpl getAllSkills start skillList  " + skillList);
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return skillList;
   }
   
   //게시판 -> 지역 -> 지역목록들 조회
   @Override
   public List<Region> getAllRegions() {
      //System.out.println("SK_DaoImpl getAllRegions start");
      List<Region> regions = null;
      try {
         regions = session.selectList("skAllRegions");
         System.out.println("SK_DaoImpl getAllRegions start regions  " + regions);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return regions;
   }

   
   
   //게시판 => 필터링작업(기술스택, 지역, 온오프라인)
   @Override
   public List<SK_Board_Card> doFilter(SK_Board_Card sk_Board_Card) {
      //System.out.println("SK_DaoImpl DoFilter start");
      List<SK_Board_Card> filterlist = null;
      System.out.println("SK_DaoImpl doFilter start sk_Board_Card  " + sk_Board_Card);
      try {
         filterlist = session.selectList("sk_head_typeList", sk_Board_Card);
         //System.out.println("SK_DaoImpl DoFilter start filterlist  " + filterlist);
         
      } catch (Exception e) {
         System.out.println("SK_DaoImpl DoFilter get error...");
         e.printStackTrace();
      }
      
      return filterlist;
   }

@Override
public List<Apply> getAcceptedApplies(String board_id) {
	//지원자들을 전부 얻어와보자구
	System.out.println("Dao에서 getAcceptedApplies 시작됨");
	System.out.println("Dao에서  getAcceptedApplies  --> " + board_id);

	List<Apply> apply = null;
	
	//1.board_id로 group_id를 얻으러 가보자
	try {
		apply = session.selectList("applyUsers", board_id);

	} catch (Exception e) {
		System.out.println("오류가 발생하였습니다 받아들여진 지원자들 getAcceptedApplies=>"+e);
		
	}
	System.out.println("Dao에서 select로 불러들여온apply ==> "+apply);
	
	return apply;
	
}




   

   

}