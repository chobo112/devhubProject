package com.oracle.team2.service.TE_Service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@Data 1개만 적어줘도 됨
public class TE_Paging {
	private int currentPage = 1; private int rowPage = 10;
	private int pageBlock = 10;
	private int start; private int end;
	private int startPage; private int endPage;
	private int total; private int totalPage;
	
	//				23					null(2)
	public TE_Paging(int currentPage1,int total) {
		this.total = total;
		this.currentPage = currentPage1; //2
		
		
		//		1				10
		start = (currentPage-1) * rowPage+1; //시작시1	11
		end = start + rowPage -1;//시작시 10			20
		
		//				23			10
		totalPage = (int)Math.ceil((double)total / rowPage);//시작시 3
		
		startPage = currentPage - (currentPage -1)%pageBlock; //시작시 1
		endPage = startPage + pageBlock - 1; //10
		
		//	10			14
		if(endPage > totalPage) {
			endPage = totalPage;
		}
	}
	
}
