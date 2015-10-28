package com.ernkebe.agregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.primefaces.expression.impl.ThisExpressionResolver;

import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.agregator.wordsanalyzer.impl.CommonWordsRemover;
import com.ernkebe.agregator.wordsanalyzer.impl.CommonWordsSimpleAnalyzer;
import com.ernkebe.agregator.wordsanalyzer.impl.CommonWordsStemAnalyzer;
import com.ernkebe.agregator.wordsanalyzer.impl.CommonWordsStemRemover;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.initial.Article;

public class Agregator {

	
	public static HashMap<String, Integer> commonWordsMap = new HashMap<String, Integer>();
	public static HashMap<String, Integer> commonWordsEndsMap = new HashMap<String, Integer>();
	
	public static List<Article> articles = new ArrayList<Article>();
	
	public static void startCommonWordsAnalize(Integer articleId)
	{
		articles = SqlUtils.findByFieldExpresion(Article.class, Restrictions.ge(Article.FIELD_ARTICLE_ID, articleId));
		
		// nebenaudojams commonWordsMap = AgregatorUtils.generateCommonWordsMap();
		//new CommonWordsSimpleAnalyzer().startAnalize();					//perskaiciuoja zodziu daznumus
		
		new CommonWordsRemover().startRemoveCommonWords();				//analizuoja tekstus pasalinant nepakeistus dazniausius zodzius
		
		// nebenaudojamas commonWordsEndsMap = AgregatorUtils.generateCommonWordsMapEnds();
		new CommonWordsStemAnalyzer(articleId).startAnalize();			// analizuoja dazniausias zodziu galunes. Is jau apdorotu zodziu
		
		new CommonWordsStemRemover(articleId).startAnalize();			//analizuoja tekstus pasalinant dazniausias galunes
		
	}
}
