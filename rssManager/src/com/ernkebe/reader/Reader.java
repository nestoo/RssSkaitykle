package com.ernkebe.reader;


import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.ernkebe.agregator.Agregator;
import com.ernkebe.agregator.WordsAnalizer;
import com.ernkebe.agregator.groupingtolls.CarrotGrouping;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.AnalizedDataClassifier;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.entities.views.VwCommonWordsEndsSimpe;
import com.ernkebe.entities.views.VwCommonWordsSimpe;

public class Reader implements Runnable{

	private static SessionFactory factory;
	private static boolean started;
	private static AnalizedDataClassifier classifier;
	
	
	
	
	public static void startReader()
	{
		
		classifier = SqlUtils.findAll(AnalizedDataClassifier.class).get(0);
		//SitesUtils.updateSitesList();
		//NewsRemover.removeNews();
		
		//XmlReader.read();																		// nuskaitomos naujos naujienos
		
	//	Agregator.startCommonWordsAnalize(classifier.getFreqAnalizeArticleId() + 1);			//analizuoja zodziu daznumus ir galuniu daznumus
		
	//	WordsAnalizer.startAnalize(classifier.getFreqAnalizeArticleId() + 1);
		
	//	updateAnalizedArticleNumber(classifier);
	}
	
	public static void startSession() {
		if(started == false)
		{
			try {
				factory = new Configuration().configure().buildSessionFactory();
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object." + ex);
				throw new ExceptionInInitializerError(ex);
			}
			//new Thread(new Reader()).start();
			started = true;
		}
	}
	
	public static SessionFactory getFactory() {
		return factory;
	}
	public static void setFactory(SessionFactory factory) {
		Reader.factory = factory;
	}

	@Override
	public void run() {
		while(true)
		{
			ParametersCounter.setFeedsInserted(0);
			ParametersCounter.setCategoriesInserted(0);
			long startTime = System.currentTimeMillis();
			startReader();
			long endTime   = System.currentTimeMillis();
			ParametersCounter.getExecutionTime(startTime, endTime);
			System.out.println(ParametersCounter.printParameter());
	         try {
	        	 Thread.sleep(1000 * 60 * 60 * 3);
	         } catch (Exception e) {
	              System.out.println(e);
	           }
	      }
		
	}

	public static boolean isStarted() {
		return started;
	}

	public static void setStarted(boolean started) {
		Reader.started = started;
	}
	
	private static void updateAnalizedArticleNumber(AnalizedDataClassifier classifier) {
		List<Article> articles = SqlUtils.findAll(Article.class);
		Integer max = 0;
		for (Article article : articles) {
			if(max < article.getArticleId())
			{
				max = article.getArticleId();
			}
		}
		classifier.setFreqAnalizeArticleId(max);
		classifier.setDataAnalized(new Date());
		SqlUtils.updateEntity(classifier);
		
	}

}
