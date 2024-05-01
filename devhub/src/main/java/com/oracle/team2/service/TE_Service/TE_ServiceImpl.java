package com.oracle.team2.service.TE_Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.team2.dao.TE_Dao.TE_Dao_Interface;
import com.oracle.team2.model.Skill_Gbl_lang;
import com.oracle.team2.model.Skill_Kr_skill;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TE_ServiceImpl implements TE_Service_Interface {

	private final TE_Dao_Interface ted;

	@Override
	public List<Skill_Kr_skill> getKrTechStackRankingList(int end, int techStackType, String keyword) {
		System.out.println("TE_ServiceImpl getKrTechStackRankingList Start... type->  " + techStackType);
		String techStackTypeStr = techStackType + "";
		List<Skill_Kr_skill> stackList = ted.krTechStackRanking(end, techStackTypeStr, keyword);
		List<Skill_Kr_skill> prevList = ted.getPrevTechStackTotalList(techStackType);
		
		updateSkillList(stackList, prevList, techStackType);
		System.out.println("TE_ServiceImpl getKrTechStackRankingList stackList --> " + stackList);

		return stackList;
	}

	@Override
	public Skill_Kr_skill getKrTechStackTotalList(int techStackType) {
		System.out.println("TE_ServiceImpl getKrTechStackTotalList Start...type->  " + techStackType);

		Skill_Kr_skill sks = ted.getKrTechStackTotalList(techStackType);
		
		return sks;
	}

	///////////////////////////
	private List<Skill_Kr_skill> updateSkillList(List<Skill_Kr_skill> stackList, List<Skill_Kr_skill> prevList, int techStackType) {
		int gTotalRecruitCnt = ted.getKrTechStackTotalList(techStackType).getSum_total_recruit_cnt();
		
		int gTotalPrevRecruitCnt = prevList.stream()
				.mapToInt(sks -> sks.getTotal_recruit_cnt())
		        .sum();
		
		System.out.println("TE_ServiceImpl updateSkillList gTotalRecruitCnt --> " + gTotalRecruitCnt);
		System.out.println("TE_ServiceImpl updateSkillList gTotalPrevRecruitCnt --> " + gTotalPrevRecruitCnt);
		
		stackList.forEach(sks -> {
		    double stackListRate = (double) sks.getTotal_recruit_cnt() / gTotalRecruitCnt;
		    sks.setRate(stackListRate);
		});
		
		prevList.forEach(prevSks -> {
		    double prevListRate = (double) prevSks.getTotal_recruit_cnt() / gTotalPrevRecruitCnt;
		    prevSks.setRate(prevListRate);
		});
		
		
		stackList.forEach(sks -> {
	        prevList.stream()
	                .filter(prevSks -> prevSks.getSkill_id().equals(sks.getSkill_id()))
	                .findFirst()
	                .ifPresent(prevSks -> {
	                	String skillName= sks.getSkill_name();
	                	skillName = skillName.contains("#") ? skillName.replace("#", "sharp") : skillName;
	                    String alternativeImagePath = "/img/Skillstackimage/TechStackImageAll/" + skillName + ".png";
	                    sks.setAlternativeImagePath(alternativeImagePath);
	                    sks.setPrev_total_recruit_cnt(prevSks.getTotal_recruit_cnt());
	                    sks.setPrevRowKrRanking(prevSks.getRowKrRanking());
	                    sks.setPrevRate(prevSks.getRate());
	                });
	    });
		return stackList;
	}

	
	@Override
	public List<Skill_Gbl_lang> getGblLangRankingList(int end) {
		System.out.println("TE_ServiceImpl getGblLangRankingList Start... ");

		List<Skill_Gbl_lang> gblLangList = ted.getGblLangRankingList(end);

		return gblLangList;
	}

	@Override
	public List<Skill_Gbl_lang> getGblLangTotalList() {
		System.out.println("TE_ServiceImpl getGblLangTotalList Start... ");

		List<Skill_Gbl_lang> getGblLangTotalList = ted.getGblLangTotalList();
		return getGblLangTotalList;
	}

	@Override
	public void setTiobeData(List<String[]> top20TableDataList) {
		System.out.println("TE_ServiceImpl setTiobeData Start... ");
		System.out.println("TE_ServiceImpl setTiobeData top20TableDataList.size() --> " + top20TableDataList.size());

		List<Skill_Kr_skill> getKrTechStackRankingList = ted.krTechStackRanking(200, "0", "");
		List<Skill_Gbl_lang> sendSkillGblLangList = new ArrayList<>();

		top20TableDataList.sort((rowDataArray1, rowDataArray2) -> {
			return rowDataArray1[2].compareTo(rowDataArray2[2]);
		});
		for (String[] rowDataArray : top20TableDataList) {
			Skill_Gbl_lang skillGblLang = new Skill_Gbl_lang();
			skillGblLang.setLast_rank(Integer.parseInt(rowDataArray[1]));

			skillGblLang.setRate(rowDataArray[3]);

			skillGblLang.setChange(rowDataArray[4]);

			skillGblLang.setSkill_img(rowDataArray[5]);

			Date today = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(today);
			skillGblLang.setGbl_date(dateStr);

			for (Skill_Kr_skill sks : getKrTechStackRankingList) {
				if (sks.getSkill_name().equals(rowDataArray[2])) {

					skillGblLang.setSkill_id(sks.getSkill_id());
					break; // 일치하는 객체를 찾았으므로 루프 종료
				} else {
					skillGblLang.setSkill_name(rowDataArray[2]);
					skillGblLang.setSkill_type("1");
				}
			}

			System.out.println("찾은 skill_id: " + rowDataArray[2] + "  " + skillGblLang.getSkill_id() + "  "
					+ skillGblLang.getSkill_name());

			sendSkillGblLangList.add(skillGblLang);
		} // for
		System.out.println("TE_ServiceImpl setTiobeData sendSkillGblLangList --> " + sendSkillGblLangList);

		ted.setTiobeData(sendSkillGblLangList);
	}

	@Override
	public void setJobkoreaData(List<Skill_Kr_skill> jobkoreaDataList) {
		System.out.println("TE_ServiceImpl setJobkoreaData Start... ");

		for (Skill_Kr_skill sks : jobkoreaDataList) {
			System.out.println("TE_ServiceImpl jobkoreaDataList --> " + sks.getSkill_id() + " " + sks.getIsmajor() + " "
					+ sks.getKr_ref() + " " + sks.getRecruit_cnt() + " " + sks.getMajor_recruit_cnt());
		}

		ted.setJobkoreaData(jobkoreaDataList);
	}//

	@Override
	public void setSaramin(List<Skill_Kr_skill> saraminDataList) {
		System.out.println("TE_ServiceImpl setSaramin Start... ");

		for (Skill_Kr_skill sks : saraminDataList) {
			System.out.println("TE_ServiceImpl saraminDataList --> " + sks.getSkill_id() + " " + sks.getIsmajor() + " "
					+ sks.getKr_ref() + " " + sks.getRecruit_cnt() + " " + sks.getMajor_recruit_cnt());
		}

		ted.setSaraminData(saraminDataList);

	}

}
