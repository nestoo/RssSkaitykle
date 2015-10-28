package com.ernkebe.agregator.wordsanalyzer.impl;

import java.util.List;
import java.util.Map;





import com.ernkebe.agregator.Agregator;
import com.ernkebe.agregator.wordsanalyzer.interfaces.ICommonWordsAnalyzer;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.CommonWords;
import com.ernkebe.entities.initial.Article;

public class CommonWordsSimpleAnalyzer implements ICommonWordsAnalyzer{

	
	@Override
	public void startAnalize() {
		SqlUtils.removeDataFromTable("common_words");
		computeCommonWordsSimpleFrequency();
	}

	private void computeCommonWordsSimpleFrequency() {
		analizeArticlesTexts(Agregator.articles);
		
		saveCommonWords();
		
	}

	
	private void analizeArticlesTexts(List<Article> articles) {
		for (Article article : articles) {
			analyzeArticleText(article.getFullText());
		}
		
	}

	private void analyzeArticleText(String text) {
		String words[] = text.split(" ");
		for (String word : words) {
			saveWordValue(word);
		}
		
	}

	private void saveWordValue(String word) {
		if(Agregator.commonWordsMap.containsKey(word))
		{
			Agregator.commonWordsMap.put(word, Agregator.commonWordsMap.get(word) + 1);
		}
		else
		{
			Agregator.commonWordsMap.put(word, 1);
		}
		
	}
	
	private void saveCommonWords() {
		SqlUtils.saveAllCommonWords(Agregator.commonWordsMap.entrySet());
	}
	
}
