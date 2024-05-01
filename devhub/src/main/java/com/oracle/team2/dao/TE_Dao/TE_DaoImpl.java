package com.oracle.team2.dao.TE_Dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.team2.model.Gbl_lang;
import com.oracle.team2.model.Skill_Gbl_lang;
import com.oracle.team2.model.Skill_Kr_skill;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TE_DaoImpl implements TE_Dao_Interface {
	private final SqlSession session;

	@Override
	public List<Skill_Kr_skill> krTechStackRanking(@Param("end") int end,
													@Param("techStackType") String techStackType,
													@Param("keyword") String keyword
													) {
		System.out.println("TE_DaoImpl krTechStackRanking Start... ");
		List<Skill_Kr_skill> krSkillList = null;

		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("end", end);
			paramMap.put("techStackType", techStackType);
			paramMap.put("keyword", keyword);

			krSkillList = session.selectList("teKrSkillList", paramMap);
			System.out.println("TE_DaoImpl krTechStackRanking krSkillList --> " + krSkillList);

		} catch (Exception e) {
			System.out.println("krTechStackRanking exception->" + e.getMessage());
		}

		return krSkillList;
	}
	
	@Override
	public List<Skill_Kr_skill> getPrevTechStackTotalList(int techStackType){
		System.out.println("TE_DaoImpl getKrTechStackListPrevData Start... ");
		List<Skill_Kr_skill> getPrevTechStackTotalList = null;
		try {
			String techStackTypeStr = techStackType + "";
			getPrevTechStackTotalList= session.selectList("teKrTechStackListPrevData", techStackTypeStr);
			
		} catch (Exception e) {
			System.out.println("teKrTechStackListPrevData exception->" + e.getMessage());
		}
		return getPrevTechStackTotalList;
	}
	
	
	
	@Override
	public Skill_Kr_skill getKrTechStackTotalList(int techStackType) {
		System.out.println("TE_DaoImpl getKrTechStackTotalList Start techStackType--> " + techStackType);
		Skill_Kr_skill sks = new Skill_Kr_skill();
		String techStackTypeStr = techStackType + "";
		try {
			sks = session.selectOne("teKrSkillTypeTotalList", techStackTypeStr);
			System.out.println("TE_DaoImpl getKrTechStackTotalList sks --> "
					+ sks);

		} catch (Exception e) {
			System.out.println("getKrTechStackTotalList exception->" + e.getMessage());
		}
		return sks;
	}

	
	
	
	@Override
	public List<Skill_Gbl_lang> getGblLangRankingList(int end) {
		System.out.println("TE_DaoImpl getGblLangRankingList Start... ");
		List<Skill_Gbl_lang> gblLangList = null;

		try {
			gblLangList = session.selectList("teGblLangList", end);
			System.out.println("TE_DaoImpl getGblLangRankingList gblLangList --> " + gblLangList);
			System.out.println("TE_DaoImpl getGblLangRankingList gblLangList.size() --> " + gblLangList.size());

		} catch (Exception e) {
			System.out.println("getGblLangRankingList exception->" + e.getMessage());
		}
		return gblLangList;
	}

	@Override
	public List<Skill_Gbl_lang> getGblLangTotalList() {
		System.out.println("TE_DaoImpl getGblLangTotalList Start... ");
		List<Skill_Gbl_lang> getGblLangTotalList = null;

		try {
			getGblLangTotalList = session.selectList("teGblLangTotalList");
			System.out.println("TE_DaoImpl getGblLangTotalList teGblLangTotalList --> " + getGblLangTotalList);

		} catch (Exception e) {
			System.out.println("getGblLangRankingList exception->" + e.getMessage());
		}
		return getGblLangTotalList;
	}

	@Override
	public void setTiobeData(List<Skill_Gbl_lang> sendSkillGblLangList) {
		System.out.println("TE_DaoImpl setTiobeData Start... ");
		int newIdInsertResult = 0;
		int newDateInsertResult = 0;

		try {
			for (Skill_Gbl_lang skillGblLang : sendSkillGblLangList) {
				if (skillGblLang.getSkill_id() == null) {
					newIdInsertResult += session.insert("teSkillTiobeNewIdInsert", skillGblLang);
				}
				newDateInsertResult += session.insert("teGblLangTiobeNewDateInsert", skillGblLang);
			}
			System.out.println("TE_DaoImpl setTiobeData newIdInsertResult.--> " + newIdInsertResult);
			System.out.println("TE_DaoImpl setTiobeData newDateInsertResult. --> " + newDateInsertResult);
		} catch (Exception e) {
			System.out.println("setTiobeData exception->" + e.getMessage());
		}
	}

	@Override
	public void setJobkoreaData(List<Skill_Kr_skill> jobkoreaDataList) {
		System.out.println("TE_DaoImpl setJobkoreaData Start... ");
		int newDateInsertResult = 0;
		int majorNewDateInsertResult = 0;
		
		try {
			for (Skill_Kr_skill krSkill : jobkoreaDataList) {
				newDateInsertResult += session.insert("teJobkoreaNewDateInsert", krSkill);
				majorNewDateInsertResult += session.insert("teJobkoreaMajorNewDateInsert", krSkill);

			}

			System.out.println("TE_DaoImpl setJobkoreaData newDateInsertResult --> " + newDateInsertResult);
			System.out.println("TE_DaoImpl setJobkoreaData majorNewDateInsertResult --> " + majorNewDateInsertResult);

		} catch (Exception e) {
			System.out.println("setJobkoreaData exception->" + e.getMessage());
		}
	}

	@Override
	public void setSaraminData(List<Skill_Kr_skill> saraminDataList) {
		System.out.println("TE_DaoImpl setSaraminData Start... ");
		int newDateInsertResult = 0;
		int majorNewDateInsertResult = 0;
		try {
			for (Skill_Kr_skill krSkill : saraminDataList) {
				newDateInsertResult += session.insert("teSaraminNewDateInsert", krSkill);
				majorNewDateInsertResult += session.insert("teSaraminMajorNewDateInsert", krSkill);

			}

			System.out.println("TE_DaoImpl setSaraminData newDateInsertResult --> " + newDateInsertResult);
			System.out.println("TE_DaoImpl setSaraminData majorNewDateInsertResult --> " + majorNewDateInsertResult);

		} catch (Exception e) {
			System.out.println("setSaraminData exception->" + e.getMessage());
		}
	}

}//
