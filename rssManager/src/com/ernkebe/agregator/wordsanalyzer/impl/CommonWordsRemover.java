package com.ernkebe.agregator.wordsanalyzer.impl;

import java.util.HashMap;
import java.util.List;

import com.ernkebe.agregator.Agregator;
import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.ArticlesUncommonWords;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.entities.views.VwCommonWordsSimpe;

import java.util.TreeMap;




import com.google.common.collect.Ordering;

public class CommonWordsRemover {                                                                             
                                                                                                              
	HashMap<String, Integer> commonWordsMapForRemoval = new HashMap<String, Integer>();
	
	public void startRemoveCommonWords() {                                              
		
		generateCommonWordsList();	
		                                                                                                      
		for (Article article : Agregator.articles) 
		{
			String uncommonWords = "";
			
			if(article.getFullText() != null && article.getFullText().length() > 0)
			{
				String[] words = article.getFullText().split(" ");
				TreeMap<String, Integer> commonWordsArticleMap = new TreeMap<String, Integer>(Ordering.natural());
				for (String word : words) 
				{
					if(!commonWordsMapForRemoval.containsKey(word))
					{
						if(commonWordsArticleMap.containsKey(word))
						{
							commonWordsArticleMap.put(word, commonWordsArticleMap.get(word) + 1);
						}
						else
						{
							commonWordsArticleMap.put(word, 1);
						}
						uncommonWords = uncommonWords + word + " ";
					}
					
				}
				
				SqlUtils.saveOrUpdateEntity(new ArticlesUncommonWords(article.getArticleId(),uncommonWords));   
			}
		}                                                                                                     
		                                                                                                      
	}                                                                                                         
                                                                                                              
	private void generateCommonWordsList() {                                                                
		List<VwCommonWordsSimpe>  rezultsList = SqlUtils.findAll(VwCommonWordsSimpe.class);                  
		for (VwCommonWordsSimpe item : rezultsList) {
			commonWordsMapForRemoval.put(item.getWord(), 1);
		}                                                      
	}                                                                                                         
	      
}                                                                                                             
                                                                                                              