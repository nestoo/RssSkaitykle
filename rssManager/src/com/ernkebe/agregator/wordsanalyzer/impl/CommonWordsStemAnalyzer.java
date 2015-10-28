package com.ernkebe.agregator.wordsanalyzer.impl;

import java.util.List;
import java.util.Map;













import org.hibernate.criterion.Restrictions;

import com.ernkebe.agregator.Agregator;
import com.ernkebe.agregator.wordsanalyzer.interfaces.ICommonWordsAnalyzer;
import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.ArticlesUncommonWords;
import com.ernkebe.entities.ArticlesUncommonWordsStem;
import com.ernkebe.entities.CommonWordsEnds;
import com.ernkebe.entities.initial.Article;

public class CommonWordsStemAnalyzer implements ICommonWordsAnalyzer{

	Integer articleId;
	public CommonWordsStemAnalyzer(Integer articleId) {
		this.articleId = articleId;
	}

	@Override
	public void startAnalize() {
		
		SqlUtils.removeDataFromTable("common_words_end");
		// skaiciuoja daznius raidziu grupiu nuo zodzio antros puses
		computeCommonWordsStemFrequency();
	}

	private void computeCommonWordsStemFrequency() {
		analizeArticlesTexts();
		
		saveCommonEnds();
		
	}

	
	private void analizeArticlesTexts() {
		List<ArticlesUncommonWords> articles = SqlUtils.findByFieldExpresion(ArticlesUncommonWords.class, Restrictions.ge(ArticlesUncommonWords.FIELD_ARTICLE_ID, articleId));
		for (ArticlesUncommonWords article : articles ) 
		{
			String [] words = article.getUncommonSimpleWords().split(" ");
			if(words != null && words.length  > 0)
			{
				for (String word : words) {
					analizeWordsEnds(word);
				}
			}
		}
		
	}

	private void analizeWordsEnds(String word) {
		for(int i = word.length()-1; i >=getEnd(word); i -- )
		{
			if(Agregator.commonWordsEndsMap.containsKey(word.substring(i, word.length())))
			{
				Agregator.commonWordsEndsMap.put(word.substring(i, word.length()), Agregator.commonWordsEndsMap.get(word.substring(i, word.length())) + 1);
			}
			else
			{
				Agregator.commonWordsEndsMap.put(word.substring(i, word.length()), 1);
			}
		}
			
		
	}

	private int getEnd(String word) {
		if(word.length() >=RssConstants.MAX_WORD_END_LENGHT)
		{
			return word.length() - RssConstants.MAX_WORD_END_LENGHT;
		}
		else
		{
			return word.length() / 2;
		}
	}

	private void saveCommonEnds() {
		SqlUtils.saveAllCommonWordsEnd(Agregator.commonWordsEndsMap.entrySet());
		
	}

	
}
