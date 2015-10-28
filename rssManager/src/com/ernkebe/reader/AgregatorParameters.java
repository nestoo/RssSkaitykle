package com.ernkebe.reader;

import java.util.List;

import com.ernkebe.database.DbUtils;
import com.ernkebe.entities.grouping.CustomInfo;

public class AgregatorParameters {

	public static void startCounting() {

		countVid();
		
		
		
	}

	private static void countVid() {
		String query = "select  article.article_id as id, articles_uncommon_words.uncommon_simple_words as text, "+
				"count(article.article_id) as frequency "+
				"from article, articles_uncommon_words  "+
				"where article.words_count > 300 "+
				"and articles_uncommon_words.article_id = article.article_id "+
				"group by article.article_id, articles_uncommon_words.uncommon_simple_words";
			List<CustomInfo> articlesCommon = DbUtils.getCustomInfoList(query);
			
			
			String querystem = "select  article.article_id as id, articles_uncommon_words_stem.uncommon_stem_words as text, "+
				"count(article.article_id) as frequency "+
				"from article, articles_uncommon_words_stem  "+
				"where article.words_count > 300 "+
				"and articles_uncommon_words_stem.article_id = article.article_id "+
				"group by article.article_id, articles_uncommon_words_stem.uncommon_stem_words";
			List<CustomInfo> articlesCommonstem = DbUtils.getCustomInfoList(querystem);
			
			Double vidcommon = 0.0;
			Double vidstem = 0.0;
			Integer comm = articlesCommon.size(); 
			Integer commstem = articlesCommonstem.size();
			Integer sum1 = 0;
			Integer sum2 = 0;
			for (CustomInfo customInfo : articlesCommon) {
				String[] words = customInfo.getText().split(" ");
				sum1 = sum1 + words.length;
				System.out.println(words.length);
			}
			vidcommon = (double) (sum1 / comm);
			System.out.println("common "+ vidcommon);
			
			for (CustomInfo customInfos : articlesCommonstem) {
				String[] words = customInfos.getText().split(" ");
				sum2 = sum2 + words.length;
				System.out.println(words.length);
			}
			vidstem = (double) (sum2 / commstem);
			System.out.println("common stem "+ vidstem);
		
	}

}
