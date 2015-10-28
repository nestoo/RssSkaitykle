package com.ernkebe.appconstants;

import java.text.SimpleDateFormat;

public class RssConstants {
	public static final String SITE_TEXT_FILE_NAME		= "sites.txt";
	
	public static final SimpleDateFormat DATE_FORMAT 	= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

	public static final String NON_WORDS_SYMBOLS 		=  "[^a-zA-ZĄČĘĖĮŠŲŪŽąčęėįšųūž\\s]";

	public static final int MAX_WORD_END_LENGHT 		= 4;

	public static final int KMeansCentroids 			= 60;
	
	public static final int DAYS_NEWS_OLD				= 7;
	
	
	
	public static final String ARTICLES_COUNT_BY_NEWS_QUERY = "select "+
		"sites.id as id, "+
		"sites.name as text, "+
		"count(article.article_id) as frequency "+
		"from sites, article "+
		"where sites.id = article.site_id "+
		" and article.words_count > 300 "+
		"group by sites.id";
	
	
	public static final String TOP_KEYS_QUERY ="select "+
		"news_words.id as id, news_words.word as text, count(article_news_words.id) as  frequency "+
		"from news_words  "+
		"inner join article_news_words on article_news_words.word_id = news_words.id "+
		"group by news_words.id "+
		"order by frequency DESC "+
		"limit 200";
	
	public static final String TOP_STEM_KEYS_QUERY ="select "+
		"news_words_stem.id as id, news_words_stem.word as text, count(article_news_words_stem.id) as  frequency "+
		"from news_words_stem   "+
		"inner join article_news_words_stem on article_news_words_stem.word_id = news_words_stem.id "+
		"group by news_words_stem.id "+
		"order by frequency DESC  "+
		"limit 200";

	
	public static final String NEWS_FREQUENCY_BY_DATE ="select "+
		"1 as id, "+
		"to_char(article.pub_date, 'yyyy-mm-dd') as text, "+
		"count(*)as frequency "+
		"from article "+
		" where article.words_count > 300 "+
		"group by 2 "+
		"ORDER BY 2 DESC";

	// -------------------------- grouping ------------------------------------------------------------------
	
	public static final String CUSTOM_ARTICLES_STEM_FOR_TOLLS_QUERY = "select "+
		"article.article_id, article.title, article.link, article.description,  "+
		"article.pub_date, article.update_date, article.site_id,  "+
		"string_agg(news_words_stem.word, ' ') as full_text,   "+
		"article.words_count,  "+
		"count(article_news_words_stem.id) as  frequency,  "+
		"sites.name as site_name  "+
		"from article    "+
		"inner join sites on sites.id = article.site_id "+ 
		"inner join  article_news_words_stem on article.article_id = article_news_words_stem.article_id "+  
		"inner join news_words_stem on article_news_words_stem.word_id = news_words_stem.id  "+
		"and article.words_count > 300  "+
		"group by article.article_id, sites.name";

	public static final String CUSTOM_ARTICLES_ORIGINAL_FOR_TOLLS_QUERY = "select "+
		"article.article_id, article.title, article.link, article.description,   "+
		"article.pub_date, article.update_date, article.site_id,   "+
		"article.full_text as full_text,   "+
		"article.words_count,  "+
		"count(article.article_id) as  frequency,  "+
		"sites.name as site_name  "+
		"from article    "+
		"inner join sites on sites.id = article.site_id  "+
		"and article.words_count > 300 "+
		"group by article.article_id,sites.name";

	public static final String CUSTOM_ARTICLES_KEY_WORDS_FOR_TOLLS_QUERY = "select "+
		"article.article_id, article.title, article.link, article.description,  "+
		"article.pub_date, article.update_date, article.site_id,  "+
		"string_agg(news_words.word, ' ') as full_text,   "+
		"article.words_count,  "+
		"count(article_news_words.id) as  frequency,  "+
		"sites.name as site_name  "+
		"from article    "+
		"inner join sites on sites.id = article.site_id  "+
		"inner join  article_news_words on article.article_id = article_news_words.article_id   "+
		"inner join news_words on article_news_words.word_id = news_words.id  "+
		"and article.words_count > 300  "+
		"group by article.article_id, sites.name";
	
	
	public static final String CUSTOM_ARTICLES_ORIGINAL_BE_DAZNIAUSIU_FOR_TOLLS_QUERY = "select  "+
		"article.article_id, article.title, article.link, article.description,    "+
		"article.pub_date, article.update_date, article.site_id,    "+
		"articles_uncommon_words.uncommon_simple_words as full_text,    "+
		"article.words_count,   "+
		"count(article.article_id) as  frequency,   "+
		"sites.name as site_name   "+
		"from article, sites, articles_uncommon_words    "+ 
		"where sites.id = article.site_id   "+
		"and article.article_id = articles_uncommon_words.article_id  "+
		"and article.words_count > 300   "+
		"group by article.article_id, site_name,articles_uncommon_words.uncommon_simple_words";
	
	public static final String CUSTOM_ARTICLES_ORIGINAL_BE_DAZNIAUSIU_STEM_FOR_TOLLS_QUERY = "select "+
		"article.article_id, article.title, article.link, article.description,   "+
		"article.pub_date, article.update_date, article.site_id,  "+ 
		"articles_uncommon_words_stem.uncommon_stem_words as full_text,   "+
		"article.words_count,  "+
		"count(article.article_id) as  frequency,  "+
		"sites.name as site_name  "+
		"from article, sites, articles_uncommon_words_stem   "+ 
		"where sites.id = article.site_id  "+
		"and article.article_id = articles_uncommon_words_stem.article_id "+
		"and article.words_count > 300  "+
		"group by article.article_id, site_name,articles_uncommon_words_stem.uncommon_stem_words";

	
	//---------------------------end grouping --------------------------------------------------------------
	
	// ---------------------------key words -------------------------------------------------------------
	
	public static final String ARCTICLES_KEY_WORDS_QUERY = "select article.article_id as id, "+
		"string_agg(news_words.word, ',') as text, "+
		"count(news_words.word) as frequency "+
		"from article, news_words, article_news_words "+
		"where article.article_id = article_news_words.article_id "+
		"and article_news_words.word_id = news_words.id "+
		"and article_news_words.frequency > 1 "+
		"and article.words_count > 300 "+
		"group by article.article_id "+ 
		"order by frequency desc";
	
			
	public static final String ARCTICLES_KEY_WORDS_STEM_QUERY = "select article.article_id as id,  "+
		"string_agg(news_words_stem.word, ',') as text,  "+
		"count(news_words_stem.word) as frequency  "+
		"from article, news_words_stem, article_news_words_stem  "+
		"where article.article_id = article_news_words_stem.article_id  "+
		"and article_news_words_stem.word_id = news_words_stem.id  "+
		"and article_news_words_stem.frequency > 1  "+
		"and article.words_count > 300  "+
		"group by article.article_id "+ 
		"order by frequency desc";

	
	//----------------------------end keywords ----------------------------------------------------------
	
	public static final String QUERY_GET_CATEGORIES = "SELECT pc.id AS cat_id, pc.category AS name, COUNT(*) AS count "+
			"FROM article, article_categories as ac, provided_categories AS pc "+
			"where  pc.id = ac.category_id"+
			" and article.article_id = ac.article_id "+
			" and article.words_count > 300  "+
			"GROUP   BY pc.id "+
			"order by count desc";


}
