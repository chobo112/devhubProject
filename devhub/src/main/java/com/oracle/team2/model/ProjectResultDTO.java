package com.oracle.team2.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProjectResultDTO {
	
		//board
	 	private Long board_id; 				// PK   // 게시글번호
	 	private String user_id; 			// FK (user_tbl PK)		// 작성자
	    private Date board_edate;			// 마감일
	    private String board_type;			// 게시판 유형		// 1=멘토주도, 2=멘토모집, 3=팀원모집
	    private String board_title;			// 제목
	    private Double pr_price;			// 금액
	    private String pr_title;			// 프로젝트명
	    private String mentor_id;			// *추가  해당 게시글의 멘토의 user_id 
	    private Date pr_sdate;
	    
	    //apply 
	    private String ap_accept;			//0대기, 1수락, 2거절
	    private String ap_result;			//0미진행, 1최종진행(팀원모집 : 최종진행, 멘토모집&멘토주도 : 결제까지 끝)
	    
	    //skill
	    private String skill_id;			//기술코드 pk
	    private String skill_name;			//기술명칭 not-null
	    private String skill_img;			// 이미지 not-null
	    private List<Skill> skills;
	    		
	    
	    //Score
	    private Double sc_score;			//평점 	*0~10점 null
	    private String sc_cmt;				//평점 코멘트 null
	    
	    
	    //pay
	    private String pay_status;			// 상태		// not-null	기본값-0=미결제, 1=결제완료, 2=결제오류, 3=환불대기, 4=환불완료
	    private String pay_id;
	    // page 정보
 		private String pageNum;
 		private int start; 
 		private int end;
 		private String currentPage;
 		private String ap_acceptPage;
 		

}
