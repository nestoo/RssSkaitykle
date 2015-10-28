package com.ernkebe.agregator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;

import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.ArticlesUncommonWords;
import com.ernkebe.entities.newsWords.ArticleNewsWords;
import com.ernkebe.entities.newsWords.NewsWords;
/*
 * Naudojamos db lentos: article_news_words, news_words
 * Visi zodziai (isskyrus dazniausius) isrenkami po viena 
 * ir unikalus sudedami i news_words. 
 * Kiekvienai naujienai skaiciuojami zodziu daznumai ir issaugomi article_news_words
 * 
 * Naudojama paieskai pagal raktinius zodzius. Naujienu susiejimui pagal naudojamus zodzius
 * 
 * 
 */
public class NewsWordsParser {

	private static HashMap<String, NewsWords> wordsInDB;
	
	public static void startAnalize(Integer articleId) {
		wordsInDB = new HashMap<String, NewsWords>();
		computeAllWordsInDb();
		List<ArticlesUncommonWords> articleUncommonWordsList = SqlUtils.findByFieldExpresion(ArticlesUncommonWords.class, Restrictions.ge(ArticlesUncommonWords.FIELD_ARTICLE_ID, articleId));
		for (ArticlesUncommonWords article : articleUncommonWordsList) 
		{
			parseAndSaveArticlesWords(article);
		}
		wordsInDB.clear();
	}
	
	private static void computeAllWordsInDb() {
		List<NewsWords> allNewsWords = SqlUtils.findAll(NewsWords.class);
		for (NewsWords newsWords : allNewsWords) {
			wordsInDB.put(newsWords.getWord(), newsWords);
		}
		
	}

	/**
	 * Perziuri naujienos teksta be dazniausiu zodziu
	 * ir iskiria atskirai po zodi ir issaugo su daznumu
	 * @param article konkreti naujiena
	 */
	private static void parseAndSaveArticlesWords(ArticlesUncommonWords article) {
		Map<String, Integer> articleWords = new HashMap<String, Integer>();
		String [] words = article.getUncommonSimpleWords().split(" ");
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


	/**
	 * @param articleId
	 * @param articleWords
	 * issaugo tik prasminius zodzius - kurie tekste pasikartojo daugiau nei viena karta
	 */
	private static void saveArticleWords(Integer articleId, Map<String, Integer> articleWords) {
		for (String wordKey : articleWords.keySet()) 
		{
			if(articleWords.get(wordKey) > 1)
			{
				Integer wordKeyId = wordKeyAlreadySaved(wordKey);
			    if(wordKeyId != null)
			    {
			    	ArticleNewsWords articleWord = new ArticleNewsWords(articleId, wordKeyId, articleWords.get(wordKey) );
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
			NewsWords newWord = new NewsWords(wordKey);
			newWord = (NewsWords) SqlUtils.saveNewEntityAndRefresh(newWord);
			wordsInDB.put(wordKey, newWord);
			return newWord.getId();
		}
	}

}
