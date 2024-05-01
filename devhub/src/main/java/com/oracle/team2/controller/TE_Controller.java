package com.oracle.team2.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.team2.model.Skill_Gbl_lang;
import com.oracle.team2.model.Skill_Kr_skill;
import com.oracle.team2.service.TE_Service.TE_Paging;
import com.oracle.team2.service.TE_Service.TE_Service_Interface;

import lombok.RequiredArgsConstructor;

@Controller
//@RestController
@RequiredArgsConstructor
public class TE_Controller {
	private final TE_Service_Interface tes;

	@RequestMapping("/techStackRanking")
	public String krTechStackRanking(Model model) {
		System.out.println("TE_Controller krTechStackRanking Start... ");
		
		int techStackType=0;
		
		List<Skill_Kr_skill> krTechStackTop3 = tes.getKrTechStackRankingList(3, techStackType,"");
		System.out.println("TE_Controller krTechStackRanking krTechStackTop3 --> " + krTechStackTop3);
		
		List<Skill_Gbl_lang> gblLangTop3 = tes.getGblLangRankingList(3);
		System.out.println("TE_Controller krTechStackRanking gblLangTop3 --> " + gblLangTop3);
		
		Skill_Kr_skill krTechStackTotalListCnt = tes.getKrTechStackTotalList(techStackType);
		
		
		

		model.addAttribute("krTechStackTop3", krTechStackTop3);
		model.addAttribute("gblLangTop3", gblLangTop3);
		model.addAttribute("greatTotal", krTechStackTotalListCnt.getSum_total_recruit_cnt());
		
		return "TE_views/dataRanking";
	}
	

	
	@RequestMapping("/getKrTechStackRankingList")
	public String getKrTechStackList(Model model, 
									@RequestParam("krPage")int krListCurrentPage,
									@RequestParam("techStackType")int techStackType,
									@RequestParam("keyword")String keyword
									) {
		System.out.println("TE_Controller getKrTechStackRankingList Start... ");
		System.out.println("TE_Controller getKrTechStackList keyword --> " + keyword);
		
		Skill_Kr_skill krTechStackTotalListCnt = tes.getKrTechStackTotalList(techStackType);
		System.out.println("TE_Controller getKrTechStackList krTechStackTotalListCnt.getRow_count() --> " + krTechStackTotalListCnt.getRow_count());
		
		TE_Paging paging= new TE_Paging(krListCurrentPage, krTechStackTotalListCnt.getRow_count());
		System.out.println("TE_Controller getKrTechStackList paging.getEnd() --> " + paging.getEnd());
		
		List<Skill_Kr_skill> krTechStackList = tes.getKrTechStackRankingList(paging.getEnd(), techStackType, "");
		System.out.println("TE_Controller getKrTechStackList krTechStackList --> " + krTechStackList);
		
		
		

		model.addAttribute("krTechStackList", krTechStackList);
		model.addAttribute("greatTotal", krTechStackTotalListCnt.getSum_total_recruit_cnt());

		return "TE_views/dataRanking::#krTechStackRankingListTbody";
	}

	
	// 차트에 값 전달해주는 컨트롤러
	@ResponseBody
	@RequestMapping("/getAjaxKrTechStackList")
	public List<Skill_Kr_skill> getAjaxKrTechStackList(Model model,
										@RequestParam("krPage")int krListCurrentPage,
										@RequestParam("techStackType")int techStackType) {
		System.out.println("TE_Controller getAjaxKrTechStackList Start... ");
		
		Skill_Kr_skill krTechStackTotalListCnt = tes.getKrTechStackTotalList(techStackType);
		System.out.println("TE_Controller getAjaxKrTechStackList krTechStackTotalListCnt --> " + krTechStackTotalListCnt);
		
		TE_Paging paging= new TE_Paging(krListCurrentPage, krTechStackTotalListCnt.getRow_count());

		List<Skill_Kr_skill> krTechStackList = tes.getKrTechStackRankingList(paging.getEnd(), techStackType,"");
		System.out.println("TE_Controller getAjaxKrTechStackList krTechStackList --> " + krTechStackList);
		
		return krTechStackList;
	}
	
	
	
	
	@RequestMapping("/getGblLangRankingList")
	public String getGblLangRankingList(Model model, @RequestParam("gblPage") int gblListCurrentPage) {
		System.out.println("TE_Controller getGblLangRankingList Start... ");
		System.out.println("TE_Controller getGblLangRankingList currentPage --> " + gblListCurrentPage);
		
		List<Skill_Gbl_lang> gblLangTotalList = tes.getGblLangTotalList();
		TE_Paging paging= new TE_Paging(gblListCurrentPage, gblLangTotalList.size());
		System.out.println("TE_Controller getGblLangRankingList  gblLangTotalList.size() --> " +  gblLangTotalList.size());
		System.out.println("TE_Controller getGblLangRankingList paging.getEnd() --> " + paging.getEnd());
		
		
		List<Skill_Gbl_lang> gblLangList = tes.getGblLangRankingList(paging.getEnd());
		
		
		model.addAttribute("gblLangList", gblLangList);
		model.addAttribute("gblPage", gblListCurrentPage);
		

		return "TE_views/dataRanking::#gblLangListTbody";
	}
	
	
	// 차트에 값 전달해주는 컨트롤러
	@ResponseBody
	@RequestMapping("/getAjaxGblLangRankingList")
	public List<Skill_Gbl_lang> getAjaxGblLangRankingList(Model model,
										@RequestParam("gblPage")int gblListCurrentPage) {
		System.out.println("TE_Controller getAjaxGblLangRankingList Start... ");
	
		List<Skill_Gbl_lang> gblLangTotalList = tes.getGblLangTotalList();
		System.out.println("TE_Controller getAjaxGblLangRankingList gblLangTotalList --> " + gblLangTotalList);
		
		TE_Paging paging= new TE_Paging(gblListCurrentPage, gblLangTotalList.size());

		List<Skill_Gbl_lang> axjaxGblLangList = tes.getGblLangRankingList(paging.getEnd());
		System.out.println("TE_Controller getAjaxGblLangRankingList gblLangList --> " + axjaxGblLangList);
		
		
		return axjaxGblLangList;
	}
	
	
	


	
	
	
}//