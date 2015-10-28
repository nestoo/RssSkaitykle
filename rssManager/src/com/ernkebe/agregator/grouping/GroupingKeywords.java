package com.ernkebe.agregator.grouping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.DbUtils;
import com.ernkebe.entities.grouping.CustomArticle;
import com.ernkebe.entities.grouping.CustomInfo;
import com.google.common.collect.Sets;

public class GroupingKeywords {
	
	public static List<CustomArticle> generateQueryKeyWords(String keywords, boolean stem, List<CustomArticle> articles2)
	{
		
		String keywordsList = AgregatorUtils.parseKeywords(keywords);
		if(keywordsList != null && keywordsList.length() > 0)
		{
			List<CustomArticle> articles;
			if(stem)
			{
				 articles = DbUtils.getArticlesByKeywordsStem(keywordsList,articles2);
			}
			else
			{
				articles = DbUtils.getArticlesByKeywords(keywordsList,articles2);
			}
			return articles;
		}
		else
		{
			return new ArrayList<CustomArticle>();
		}
		
	}
	
	/**
	 * gaunamas naujienu id ir raktazodziu id aibes
	 * pagal kurias grupuojama i nesusikertancias aibes
	 * apribojimai raktazodziu daznis > 1 naujienos pradinis bag of words > 300
	 * 
	 */
	public static List<CustomInfo> generateKeyWordsSets(boolean stem, String keywords) {
		List<Set> rezultSets = new ArrayList<Set>();
		List<CustomInfo> articleWordsSetsStirngLIst = new ArrayList<CustomInfo>();
		if(keywords == null)
		{
			if(stem)
			{
				articleWordsSetsStirngLIst = DbUtils.getCustomInfoList(RssConstants.ARCTICLES_KEY_WORDS_STEM_QUERY);
			}
			else
			{
				articleWordsSetsStirngLIst = DbUtils.getCustomInfoList(RssConstants.ARCTICLES_KEY_WORDS_QUERY);
			}
		}
		else
		{
			articleWordsSetsStirngLIst = DbUtils.getCustomKeysSetByKeywordsList(stem, keywords);
		}
		
		
		for (CustomInfo article : articleWordsSetsStirngLIst) {
			Set tempSet = addAllKeyWordsInSet(article.getText());
			if(rezultSets.isEmpty())
			{
				rezultSets.add(tempSet);
			}
			else
			{
				boolean foundIntersection = false;
				for (Set itemSet : rezultSets) {
					if(!Sets.intersection(itemSet, tempSet).isEmpty()) //iesko ar yra susikertansciu aibiu
					{
						foundIntersection = true;
						itemSet.add(tempSet);
						break;
					}
				}
				if(foundIntersection == false)
				{
					rezultSets.add(tempSet);
				}
			}
		}
		return generateSetsAsStringList(rezultSets);
	}

	private static List<CustomInfo> generateSetsAsStringList(List<Set> rezultSets) {
		List<CustomInfo> rez = new ArrayList<CustomInfo>();
		for (Set set : rezultSets) {
			CustomInfo item = new CustomInfo();
			item.setText(getSetsData(set));
			rez.add(item);
		}
		return rez;
	}

	private static String getSetsData(Set set) {
		StringBuilder sb = new StringBuilder();
		for (Object object : set) {
			sb.append(object);
			sb.append(" ");
		}
		return sb.toString().trim().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "");
	}

	private static Set addAllKeyWordsInSet(String text) {
		Set tempSet = new HashSet<String>();
		String[] words = text.split(",");
		tempSet = Sets.newHashSet(words);
		return tempSet;
	}


	
}
