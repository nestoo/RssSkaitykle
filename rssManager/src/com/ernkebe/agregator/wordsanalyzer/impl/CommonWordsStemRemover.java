package com.ernkebe.agregator.wordsanalyzer.impl;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.criterion.Restrictions;

import com.ernkebe.agregator.Agregator;
import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.agregator.wordsanalyzer.interfaces.ICommonWordsAnalyzer;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.ArticlesUncommonWords;
import com.ernkebe.entities.ArticlesUncommonWordsStem;
import com.ernkebe.entities.CommonWordsEnds;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.entities.views.VwCommonWordsEndsSimpe;
import com.google.common.collect.Ordering;

public class CommonWordsStemRemover implements ICommonWordsAnalyzer{

	HashMap<String, Integer> commonWordsEndsMapForRemoval = new HashMap<String, Integer>();
	Integer articleId;
	
	public CommonWordsStemRemover(Integer articleId) {
		this.articleId = articleId;
	}

	@Override
	public void startAnalize() {
		
		generateCommonWordsEnds();
		List<ArticlesUncommonWords> articleUncommonWordsList = SqlUtils.findByFieldExpresion(ArticlesUncommonWords.class, Restrictions.ge(ArticlesUncommonWords.FIELD_ARTICLE_ID, articleId));
		for (ArticlesUncommonWords article : articleUncommonWordsList) 
		{
			if(article.getUncommonSimpleWords() != null)
			{
				String text = analizeAndRemoveCommonWordsEnds(article.getUncommonSimpleWords());
				SqlUtils.saveOrUpdateEntity(new ArticlesUncommonWordsStem(article.getArticleId(),text));   
			}
		}
	}

	private String analizeAndRemoveCommonWordsEnds(String words) {
		String uncommonWordsEnds = "";
		if(words != null && !words.isEmpty())
		{
			TreeMap<String, Integer> commonWordsStemArticleMap = new TreeMap<String, Integer>(Ordering.natural());
			String [] allWords = words.split(" ");
			for (String word : allWords) 
			{
				String stem = generateStemWord(word);
				uncommonWordsEnds = uncommonWordsEnds + stem + " ";
				if(!commonWordsStemArticleMap.containsKey(stem))
				{
					if(commonWordsStemArticleMap.containsKey(stem))
					{
						commonWordsStemArticleMap.put(stem, commonWordsStemArticleMap.get(stem) + 1);
					}
					else
					{
						commonWordsStemArticleMap.put(stem, 1);
					}
					
				}
				
				
			}
			return uncommonWordsEnds;
		}
		else
		{
			return null;
		}
	}

	private String generateStemWord(String word) {
		Integer[] maxFrequency  = new Integer[word.length()/ 2];
		int lenght = 0;
		Integer maxLenght = 0;
		Integer frequency = 0;
		for(int i = word.length()-1; i > word.length()/ 2; i -- )
		{
			maxFrequency[lenght] = commonWordsEndsMapForRemoval.get(word.substring(i, word.length()));
			lenght++;
			
		}
		// pasiimama ilgiausia galune rasta
		for (int i = maxFrequency.length-1; i >=0; i--) {
			if(maxFrequency[i] != null)
			{
				maxLenght = i;
				break ;
			}
		}
		return word.substring(0, word.length() - maxLenght);
	}

	private void generateCommonWordsEnds() {
		List<VwCommonWordsEndsSimpe>  rezultsList = SqlUtils.findAll(VwCommonWordsEndsSimpe.class);
		for (VwCommonWordsEndsSimpe item : rezultsList) {
			commonWordsEndsMapForRemoval.put(item.getWordEnd(), item.getFrequency());
		}                                                      
	}

	

}
