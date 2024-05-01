package com.oracle.team2.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SK_Board_Card { //board와 user를 사용하고 싶다면??
	private Long board_id;		// PK			// 게시글번호
	
	private String user_id;		// FK (user_tbl PK)		// 작성자
	
	private String board_title;	// 제목
	
	private String pr_title;	// 프로젝트명
	
	private Date board_wdate;	// 작성일		// sysdate
	
	private Date board_edate;	// 마감일
	
	private String board_content;	// 내용
	
	private int board_view;			// 조회수
	private String local_code;		// FK (region_tbl PK)		// 행정동코드
	
	private String kakaolink;		// 카톡링크
	private String kakaopw;			// 카톡암호
	
	private String board_type;		// 게시판 유형		// 1=멘토주도, 2=멘토모집, 3=팀원모집
	
	private String board_status;	// 게시판 상태		// 0=종료, 1=모집중
	
	private Date pr_sdate;			// 프로젝트 시작일
	
	private Date pr_edate;		// 프로젝트 종료일
	
	private String pr_type;			// 진행방식		// 0=온라인, 1=오프라인, 2=온/오프라인 통합
	private int pr_price;			// 금액
	private int tot_round;			// 총회차
	private int cur_round;			// 진행된 회차
	private String del_status;		// 삭제구분		// 0=정상, 1=삭제, 2=게시 중지
	
	private String mentor_id;	//멘토아이디 -> 게시글작성할때 보드타입이 1(멘토주도)인경우 mentor_id에도 user_id가 추가 
	private int min_people;		//최소인원 그룹인원들의 합 -> 최소인원을 넘어야지 플젝 진행가능
	
	//user테이블 닉네임
	
	private String user_name;	//닉네임 no-null
	private int user_score;	//평점 		not-null
	
	
	private int d_day; // D-day계산용
	private String bookmarkYN;
	
	private String avg_score; // 평균 평점
	private String skills; // 게시글에서 모집하려는 기술 목록
	private String skill_images; // 모집하려는 기술 이미지 목록
	
	
	
	// page 정보
	private String pageNum;
	private int start; private int end;
	private int currentPage;
	
	//필터용
	private String cardSort;
	
	//private List<String> cardSort; 이렇게 했으면 listAGg를 안했어도 된다..
	
	private List<Group> groups;
	
	//조회용
	private List<String> skillImgUrlList;
	private String skillImgUrlArray;
	private List<Skill> skillList;
	private String selectedRegion;
	private String selectedOnOff;
	
	private String local;		// 시군구 not-null

}
