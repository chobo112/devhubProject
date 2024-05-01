package com.oracle.team2.controller;



import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

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
import com.oracle.team2.service.SK_Service.Paging;
import com.oracle.team2.service.SK_Service.ReplyPaging;
import com.oracle.team2.service.SK_Service.SK_Service_Interface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*RestController로 오면 ResponseBody로 들어옴 RestController -> 
 * jsonConvert 가 작동해서 문자열만

 * controller -> htmlConvert가 작동해서 html코드 읽어올 수가 있음
 * 
 * 
즉, RestController = @controller + @ResponseBody를 합친것 : 사용목적2가지
Ajax를 쉽게(Responsebody어노테이션 안달고 사용할수 있는정도의 차이 밖에 없음

  */


//@RestController
@Controller
@RequiredArgsConstructor
public class SK_Controller {
   
   private final SK_Service_Interface sk_Service_Interface;
   
   private final CommonController cc;

   
   //헤더에서 게시판 유형 선택시 폼 이동 + 페이징처리 불러오기
   @RequestMapping(value ="/1board")
   public String SK_Board(Model model, HttpServletRequest request,
                     SK_Board_Card sk_Board_Card) {
	  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@"+sk_Board_Card);
      sk_Board_Card.setUser_id(cc.getUserIdInSession(request));
      handleCardPageing(model, sk_Board_Card);
      List<Skill> skillList = sk_Service_Interface.getAllSkills();
      List<Region> regions = sk_Service_Interface.getAllRegions();
      model.addAttribute("skillList", skillList);
      model.addAttribute("regionList", regions);
      
      return "/SK_views/1board";
   }
   
   //1board.html에서 -> ajax로 게시판 유형별 보기(전체보기가 기본폼) + 페이징처리불러오기 + 필터링 
   //필터링 걸 애들 web --> server로 불러옴
   //@PostMapping("/cardAllAjax")하면 value를 안 써도 됨
   @RequestMapping(value = "cardAllAjax", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String SK_Board_Ajax(Model model, HttpServletRequest request,
                        @RequestBody SK_Board_Card sk_Board_Card) {
      sk_Board_Card.setUser_id(cc.getUserIdInSession(request));
       System.out.println("SK_Controller SK_Board_Ajax+filter start sk_Board_Card --> " + sk_Board_Card);
      handleCardPageing(model, sk_Board_Card);
      
      //필터링 해보자
      List<SK_Board_Card> filterList = sk_Service_Interface.doFilter(sk_Board_Card);
       System.out.println("filterlist-->"+filterList);
       
       //밑에는 필요 없는 것 같은데???
       model.addAttribute("filterAll", filterList);
       
       
      
       return "/SK_views/1board::#content";
   }
   
   
   //카드 페이징 및 타입별로 불러 올 수 있는 공통된 코드(헤드와 ajax게시판 전체보기 하는 법 동일
   public void handleCardPageing(Model model, SK_Board_Card sk_Board_Card) {
       System.out.println("Controller에서 SK_Board 헤드용 start sk_Board_Card======>"+sk_Board_Card);

       int totalAllCard = sk_Service_Interface.totalAllCard(sk_Board_Card);
       System.out.println("Controller에서 handleCardPageing Start totalAllCard--------->" + totalAllCard);
       System.out.println("Controller에서 handleCardPageing 1 sk_Board_Card.getCurrentPage()->" + sk_Board_Card.getCurrentPage());

       if (sk_Board_Card.getCurrentPage() < 1) {
           sk_Board_Card.setCurrentPage(1);
       } 
       
       Paging page = new Paging(totalAllCard, sk_Board_Card.getCurrentPage());
       //System.out.println("Paging : " + page);

       sk_Board_Card.setStart(page.getStart());
       sk_Board_Card.setEnd(page.getEnd());

       System.out.println("controller에서 sk_Board_Card 헤드부분+페이징 : " + sk_Board_Card);
       //카드 전체를 불러와서 담고 있는 리스트
       List<SK_Board_Card> skBoardCardList = sk_Service_Interface.headCard(sk_Board_Card);
//       System.out.println("SK_Controller handleCardPageing start skBoardCardList  " + skBoardCardList.get(0).getSkill_images());
       
       
       
       //스킬 이미지를 잘라서 넣어주려고 이렇게 함
      List<String> skillImageUrlList = new ArrayList<>();
       for (SK_Board_Card skCard : skBoardCardList) { // 일단 한번 타입을 맞춘 그릇에다가 리스트를 각각 꺼내와서 담은뒤에
       
           //model에 skills이미지를 넘겨줄 객체

          String skillImageUrls = skCard.getSkill_images(); //위에서 담은 그릇인 skillListImg를 꺼내서 접시위(SkillImages)에 올려주고
              //System.out.println("여기에는 이미지들이 1개씩 분리되서 담겨있으려니????????????????????????????"+skillImageUrls);
           
           if (skillImageUrls != null && !skillImageUrls.isEmpty()) {//접시위에 아무것도 없지 않은 경우에만
               String[] skillImageUrlArray = skillImageUrls.split(",");   //, ~~ ,~~ , 이런식으로 담긴 이미지를을 , 로 잘라서 배열로 다시 넣어주고
               //System.out.println("여기에다가는 이미지배열에다가 넣었을 애들인데 여기서는 ,가 잘린애들이 담겨있을거임"+skillImageUrlArray);
                  
               skillImageUrlList = Arrays.asList(skillImageUrlArray);
               //System.out.println("skillImageUrlList-->"+skillImageUrlList);
               //System.out.println("skillImageUrlList.size(): "+ skillImageUrlList.size());
               
               skCard.setSkillImgUrlList(skillImageUrlList);
               
           }
       }
       
       //리스트로 나온 이미지들을 => 조회용에 넣고 그걸 모델에 넣어서 
       model.addAttribute("skill_img",skillImageUrlList);
        System.out.println("skillImageUrlList@@@@"+skillImageUrlList);
       
       
        model.addAttribute("sk_board", skBoardCardList);//list로 넘긴다음에 html에서 each로 꺼냄
       model.addAttribute("page", page);
       //html에서 get파라미터로 받아온 board_type
       model.addAttribute("boardType", sk_Board_Card.getBoard_type());
   }
   
   //게시판 화면에서 -> 북마크 아이콘을 누르면 북마크가 되게 하도록..
   @ResponseBody
   @RequestMapping(value="/bookmarkUpdate", method = RequestMethod.POST)
   public void bmkClick(@RequestParam("board_id") String board_id, HttpServletRequest request) {
      System.out.println("컨트롤러에서 bmkclick메서드가 실행됨");
      System.out.println("SK_Controller bmkClick start board_id  " + board_id);

       //User user = null;
       BookMark bookMark = new BookMark();
       System.out.println("컨트롤러에서 bmkClick 요청이 뭘로 들어왔냐 -> "+request);
       bookMark.setUser_id(cc.getUserIdInSession(request));
       bookMark.setBoard_id(Long.valueOf(board_id));
       System.out.println("controller에서 bmkClick ->" + bookMark);
       sk_Service_Interface.bmkUpdate(bookMark);
       
   }
   
   //게시판 -> 글등록 -> 그룹당 스킬이미지 불러오기
   @RequestMapping(value = "/boardWirte")
   public String boardRegsiter(SK_board_write board_write
                        ,Model model
                        ,HttpServletRequest request) {
      //System.out.println("SK_Controller boardRegsiter Start... ");
      
      if(!cc.loginCheck(request, "boardWirte")) {
            // 로그인 안 되어있으면 로그인 페이지로
            return "forward:loginView";
       }
      board_write.setUser_id(cc.getUserIdInSession(request));
      
      
      List<Skill> skillList = sk_Service_Interface.getSkillList();
      //System.out.println("SK_Controller boardRegsiter skillList --> " + skillList);
      
      
      model.addAttribute("techStackList", skillList);
      
      
      return "/SK_views/board_write";
   }
   
   //게시판 -> 글등록 -> 그룹당 스킬이미지 불러오기 
   @ResponseBody
   @PostMapping("/getSkillImgAjax")
   public List<Skill> getSkillImgAjax(@RequestParam("selectedSkillNameArray") String[] selectedSkillNameArray, Model model) {
      //System.out.println("SK_Controller getSkillImgAjax Start... ");
      //System.out.println("SK_Controller getSkillImgAjax selectedSkillNameArray --> " + selectedSkillNameArray);
      
      List<Skill> skillList = sk_Service_Interface.getSkillList();
      //System.out.println("SK_Controller boardRegsiter skillList --> " + skillList);
      
      
      List<Skill> resultSkillList = new ArrayList<>();
      List<String> selectedSkillNameList = Arrays.asList(selectedSkillNameArray);
      
      for (String skillName : selectedSkillNameList) {
            Skill foundSkill = skillList.stream()
                    .filter(skill -> skill.getSkill_name().equals(skillName))
                    .findFirst()
                    .orElse(null);

            if (foundSkill != null) {
                resultSkillList.add(foundSkill);
            }
        }
      
      //System.out.println("SK_Controller getSkillImgAjax resultSkillList --> " + resultSkillList);
      
      
      return resultSkillList;
   }
   //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   //게시판 -> 글등록 -> 등록하기 (insert)  //user가 필요함  온라인일 경우에는 NULL 처리 해보자
   @RequestMapping(value ="/submit_board",  method = RequestMethod.POST) 
   public String board_insert(@ModelAttribute SK_board_write boardWrite, HttpServletRequest request, User user) {
      boardWrite.setUser_id(cc.getUserIdInSession(request));
      //System.out.println("SK_Controller board_insert Start... ");
      System.out.println("SK_Controller board_insert board_write --> " + boardWrite);
      for (String[] arr : boardWrite.getSkillImgArray()) {
          System.out.println("여기는 그룹당 스킬Id들 맨 마지막이 멘토스킬 -->"+Arrays.toString(arr));
      }
      
            
      SK_board_write skBoard= sk_Service_Interface.insertBoard(boardWrite);
      
      //글작성 되면 글이 작성된 폼으로 가져야 함@@@@@@@@@@@@@@@@@
       return "redirect:/cardDetailForm?board_id="+boardWrite.getBoard_id(); 
   }

   //게시판 -> 카드클릭 -> 게시판 상세보기 폼 이동
   @RequestMapping(value ="/cardDetailForm")
   public String boarddetail(@RequestParam("board_id") String board_id, Model model) {
      System.out.println("컨트롤러에서 boarddetail 실행");
      System.out.println("클릭한 카드형 게시판의 board_id->"+board_id);
      
      // 1. 게시판 정보 조회 + 조회수(Dao에서분리) 1씩 증가
       SK_Board_Card sk_board_detail = sk_Service_Interface.getBoardById(board_id);
       //System.out.println("컨트롤러에서 모델에 넘기기전인 sk_board_detail ->"+sk_board_detail);
       model.addAttribute("board_detail", sk_board_detail); // 조회한 게시판 정보를 모델에 추가
       System.out.println("Controller에서 게시판글보기부분 boarddetail ===============>"+sk_board_detail);
      
       // 2. 게시판에 속한 그룹 정보 조회
      List<Group> groups = sk_Service_Interface.getGroupsByBoardId(board_id);
      //System.out.println("controller에서 boarddetail메서드 groups--------->"+groups); 
      model.addAttribute("groups", groups); // 조회한 그룹 정보를 모델에 추가
      
      //뷰에서 비밀번호를 보여질떄만 뿌려주자
      //3. 수락된 인원 셀렉트해와서(List) 뷰로 넘겨줘야함 --> 뷰에서 session.userId가 리스트에 있으면 비밀번호 보이게
      //apply_tbl에서 ap_accept = 1인 애들을 찾아줘야함
      //board_id로 group_tbl을 가서 group_id를 가져온다음에 user_id까지 3가지로 찾아와야함
      List<Apply> acceptApplier = sk_Service_Interface.getAceeptedUsers(board_id);
      System.out.println("controller 카드비밀번호를 숨기기 윈함 acceptApplier  = = >"+acceptApplier);
      model.addAttribute("AcceptedApply", acceptApplier);
      
      
      /**************************************************************************/

      // 게시판에 속한 댓글 갯수 조회
      Comment comment = new Comment();
      comment.setBoard_id(Long.valueOf(board_id));
      // paging 작업
      int totCmtCnt = sk_Service_Interface.getTotalCmtCount(comment);
      //System.out.println("SK_Controller boarddetail totCmtCnt : " + totCmtCnt);
      
                                    // 총 댓글의 갯수        현재페이지
      ReplyPaging replyPaging = new ReplyPaging(totCmtCnt, comment.getCurrentPage());
      comment.setStart(replyPaging.getStart()); // 페이지의 시작
      comment.setEnd(replyPaging.getEnd()); // 페이지의 끝
      
      // 쿼리에서 페이지된 작업을 리스트에 넣어줌
      List<Comment> commentList = sk_Service_Interface.getCommentList(comment);
      //System.out.println("SK_Controller boarddetail commentList start" + commentList);
      model.addAttribute("replyPaging", replyPaging);
      model.addAttribute("commentList", commentList);
      model.addAttribute("commentCount", totCmtCnt);
      
      
      
      return "/SK_views/card_detail";// 게시판 상세 정보를 보여줄 뷰로 이동
   }
   
   //cardDetailForm 게시판 -> 카드클릭 -> 상세페이지에서 --> 신청버튼(insert) 취소버튼(delete) --> applytable
   //user가 필요함
   @RequestMapping(value="/apply")
   public String applyORcancle(
                        @RequestParam("board_id") String board_id, 
                        @RequestParam("group_id") String group_id, 
                        Model model,
                        HttpServletRequest request
                        ) {
      System.out.println("controller로 board_id잘 받오왔니@@@@@@@@@@@@@@@@@@@@@@@@"+board_id);
      System.out.println("controller로 group_id잘 받오왔니@@@@@@@@@@@@@@@@@@@@@@@@"+group_id);
      
      
      if(!cc.loginCheck(request, "cardDetailForm?board_id="+board_id)) {
            // 로그인 안 되어있으면 로그인 페이지로
            return "forward:loginView";
        }
      //String user_id = request.getSession().getAttribute("userId").toString();
      //System.out.println("Controller에서 apply해보자 !! user_id ==> @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
      //		+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+user_id);
      
      //이렇게 되는 경우에는 버튼 신청과 취소가 1개만 읽히는 문제 가있는것같은데??
      Apply apply = new Apply();
      apply.setBoard_id(Long.valueOf(board_id));
      apply.setGroup_id(Long.valueOf(group_id));
      apply.setUser_id((cc.getUserIdInSession(request)));
      
      System.out.println("apply에 board_id는 잘 담겨져 있냐 -->"+apply.getBoard_id());
      System.out.println("apply에 Group_id는 잘 담겨져 있냐 -->"+apply.getGroup_id());
      System.out.println("apply에 User_id는 잘 담겨져 있냐 -->"+apply.getUser_id());
      
      sk_Service_Interface.apply(apply);//user_id를 제거
          
       // 처리 후 리턴
       return "forward:cardDetailForm";
   }
   //용상형님 댓글부분
   

   //카드게시판 댓글부분 view ajax 로 받아옴
   @RequestMapping("/cardDetailForm_reply_view")
   public String reply_view(HttpServletRequest request, @RequestBody Comment comment, Model model,
         HttpSession session) {
      if (!cc.loginCheck(request, "cardDetailForm")) {
         // 로그인 안 되어있으면 로그인 페이지로
         return "forward:loginView";
      }
      comment.setUser_id(cc.getUserIdInSession(request));
      //System.out.println("SK_Controller reply_view start" + comment);
      // 댓글 인서트
      int replyInsert = sk_Service_Interface.commentSave(comment);
      // 댓글입력하면 list 갱신 됨 (조회 select 재사용)
      // paging 작업
      int totCmtCnt = sk_Service_Interface.getTotalCmtCount(comment);
      //System.out.println("SK_Controller loadComments totCmtCnt : " + totCmtCnt);

      ReplyPaging replyPaging = new ReplyPaging(totCmtCnt, comment.getCurrentPage());
      comment.setStart(replyPaging.getStart());
      comment.setEnd(replyPaging.getEnd());

      List<Comment> commentList = sk_Service_Interface.getCommentList(comment);
      //System.out.println("SK_Controller loadComments commentList start" + commentList);
      model.addAttribute("replyPaging", replyPaging);
      model.addAttribute("commentList", commentList);
      return "/SK_views/card_detail::#replaceWithCommentList";
   }

   // ajax 댓글 삭제
   @RequestMapping("/replydelete")
   public String replydelete(HttpServletRequest request, @RequestBody Comment comment, Model model) {
      if (!cc.loginCheck(request, "cardDetailForm")) {
         // 로그인 안 되어있으면 로그인 페이지로
         return "forward:loginView";
      }
      comment.setUser_id(cc.getUserIdInSession(request));
      System.out.println("댓글 삭제 파라미터 확인용: "+comment);
      sk_Service_Interface.replydelete(comment);

      // paging 작업
      int totCmtCnt = sk_Service_Interface.getTotalCmtCount(comment);
      //System.out.println("SK_Controller loadComments totCmtCnt : " + totCmtCnt);

      ReplyPaging replyPaging = new ReplyPaging(totCmtCnt, comment.getCurrentPage());
      comment.setStart(replyPaging.getStart());
      comment.setEnd(replyPaging.getEnd());

      List<Comment> commentList = sk_Service_Interface.getCommentList(comment);
      //System.out.println("SK_Controller loadComments commentList start" + commentList);
      model.addAttribute("replyPaging", replyPaging);
      model.addAttribute("commentList", commentList);

      return "/SK_views/card_detail::#replaceWithCommentList";
   }
   
   
   @RequestMapping("/loadComments")
   public String loadComments(Comment comment, Model model) {
      //System.out.println("SK_Controller loadComments start");
      // paging 작업
      int totCmtCnt = sk_Service_Interface.getTotalCmtCount(comment);
      //System.out.println("SK_Controller loadComments totCmtCnt : " + totCmtCnt);

      ReplyPaging replyPaging = new ReplyPaging(totCmtCnt, comment.getCurrentPage());
      comment.setStart(replyPaging.getStart());
      comment.setEnd(replyPaging.getEnd());

      List<Comment> commentList = sk_Service_Interface.getCommentList(comment);
      //System.out.println("SK_Controller loadComments commentList start" + commentList);
      model.addAttribute("replyPaging", replyPaging);
      model.addAttribute("commentList", commentList);
      model.addAttribute("commentCount", totCmtCnt);
      return "/SK_views/card_detail::#replaceWithCommentList";
   }
   
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@여기까지가 댓글 용상이형님


}//class