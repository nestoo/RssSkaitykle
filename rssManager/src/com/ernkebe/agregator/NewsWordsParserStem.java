package com.ernkebe.agregator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;

import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.ArticlesUncommonWordsStem;
import com.ernkebe.entities.newsWords.ArticleNewsWordsStem;
import com.ernkebe.entities.newsWords.NewsWordsStem;
/*
 * Naudojamos db lentos: article_news_words_stem, news_words_stem
 * Visi zodziai (isskyrus dazniausius) isrenkami po viena 
 * ir unikalus sudedami i news_words_stem. 
 * Kiekvienai naujienai skaiciuojami zodziu daznumai ir issaugomi article_news_words_stem
 * 
 * Naudojama paieskai pagal raktinius zodzius. Naujienu susiejimui pagal naudojamus zodzius
 * 
 * 
 */
public class NewsWordsParserStem {

	private static HashMap<String, NewsWordsStem> wordsInDB;
	
	public static void startAnalize(Integer articleId) {
		wordsInDB = new HashMap<String, NewsWordsStem>();
		computeAllWordsInDb();
		List<ArticlesUncommonWordsStem> articleUncommonWordsList = SqlUtils.findByFieldExpresion(ArticlesUncommonWordsStem.class, Restrictions.ge(ArticlesUncommonWordsStem.FIELD_ARTICLE_ID, articleId));
		for (ArticlesUncommonWordsStem article : articleUncommonWordsList) 
		{
			parseAndSaveArticlesWords(article);
		}
		wordsInDB.clear();
	}
	
	private static void computeAllWordsInDb() {
		List<NewsWordsStem> allNewsWords = SqlUtils.findAll(NewsWordsStem.class);
		for (NewsWordsStem newsWords : allNewsWords) {
			wordsInDB.put(newsWords.getWord(), newsWords);
		}
		
	}

	/**
	 * Perziuri naujienos teksta be dazniausiu zodziu
	 * ir iskiria atskirai po zodi ir issaugo su daznumu
	 * @param article konkreti naujiena
	 */
	private static void parseAndSaveArticlesWords(ArticlesUncommonWordsStem article) {
		Map<String, Integer> articleWords = new HashMap<String, Integer>();
		String [] words = article.getUncommonStemWords().split(" ");
		for (String word: words) {
			if(articleWords.containsKey(word))
			{
				int wordCount = (int) articleWords.get(word);
				articleWords.put(word, wordCount + 1);
			}
			else
			{
				articleWords.put(word, 1);
			}
		}
		saveArticleWords(article.getArticleId(), articleWords);
	}


	private static void saveArticleWords(Integer articleId, Map<String, Integer> articleWords) {
		for (String wordKey : articleWords.keySet()) 
		{
			if(articleWords.get(wordKey) > 1)
			{
				Integer wordKeyId = wordKeyAlreadySaved(wordKey);
			    if(wordKeyId != null)
			    {
			    	ArticleNewsWordsStem articleWord = new ArticleNewsWordsStem(articleId, wordKeyId, articleWords.get(wordKey) );
			    	SqlUtils.saveNewEntity(articleWord);
			    }
			}
		}
	}


	/**
	 * Metode, jei visame wordsInDB yra jau zodis tai grazina jo id 
	 * jei nera - issaugo ir ideda i wordsInDB ir grazina id
	 * @param wordKey
	 * @return
	 */
	private static Integer wordKeyAlreadySaved(String wordKey) {
		if(wordsInDB.containsKey(wordKey))
		{
			return wordsInDB.get(wordKey).getId();
		}
		else
		{
			NewsWordsStem newWord = new NewsWordsStem(wordKey);
			newWord = (NewsWordsStem) SqlUtils.saveNewEntityAndRefresh(newWord);
			wordsInDB.put(wordKey, newWord);
			return newWord.getId();
		}
	}

}
