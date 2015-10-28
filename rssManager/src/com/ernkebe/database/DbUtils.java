package com.ernkebe.database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.agregator.utils.TextUtils;
import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.entities.Sites;
import com.ernkebe.entities.grouping.CustomArticle;
import com.ernkebe.entities.grouping.CustomCategories;
import com.ernkebe.entities.grouping.CustomGroupKeywordsInfo;
import com.ernkebe.entities.grouping.CustomInfo;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.entities.initial.ArticleCategories;
import com.ernkebe.entities.initial.ProvidedCategories;
import com.ernkebe.reader.Reader;

public class DbUtils {
	
	//TODO sudeti cascade
	public static int saveArticle(Article feed) {
		int articleId = 0;
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(feed);
			session.refresh(feed);
			articleId = feed.getArticleId();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return articleId;
	}
	
	public static boolean isUniqueArticleName(String title) {
		boolean isUnique = true;
		Integer count = 0;
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq(Article.FIELD_TITLE, title));
			//cr.add(Restrictions.eq(Article.FIELD_PUB_DATE, pubDate));
			count = cr.list().size();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (count.equals(0)) {
			isUnique = true;
		} else {
			isUnique = false;
		}
		return isUnique;
	}

	public static int saveCategory(String categoryName)
	{
		int id =0;
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProvidedCategories category = new ProvidedCategories(categoryName);
			session.save(category);
			session.refresh(category);
			id= category.getId();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}
	
	public static void saveFeedCategory(int feedId, int categoryId)
	{
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ArticleCategories category = new ArticleCategories(feedId, categoryId);
			session.save(category);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/*
	 * Adds sites informacion if database is emty
	 */
	public static void updateSites(List<Sites> sites) {
		Session session = Reader.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Sites> sitesWithName = SqlUtils.findAll(Sites.class);
			if(!(sites.size() == sitesWithName.size()))
			{
				for (Sites site : sites) 
				{
					boolean found = false;
					for (Sites sites2 : sitesWithName) 
					{
						if(site.getName().equalsIgnoreCase(sites2.getName()))
						{
							found = true;
							break;
						}
					}
					if(found)
					{
						site.setId(sitesWithName.get(0).getId());
						session.saveOrUpdate(site);
					}
					else
					{
						session.save(site);
					}
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/*
	 * Find category by name in db, if not returns null
	 */
	public static ProvidedCategories getCategoryByName(String name) {
		ProvidedCategories results = new ProvidedCategories();
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(ProvidedCategories.class);
			cr.add(Restrictions.eq(ProvidedCategories.FIELD_CATEGORY, name));
			if(cr.list().size() !=0 && cr.list() != null)
			{
				results = (ProvidedCategories) cr.list().get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	/*
	 * Return all sites from db
	 */
	public static List<Sites> getSites() {
		List<Sites> sites = new ArrayList<Sites>();
		Session session = Reader.getFactory().openSession();
		try {
			sites = session.createCriteria(Sites.class).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return sites;
	}
	
	public static void deleteArticle(Article article) {
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(ArticleCategories.class);
			cr.add(Restrictions.eq(ArticleCategories.FIELD_ARTICLE_ID,
					article.getArticleId()));
			List<ArticleCategories> results = cr.list();
			for (ArticleCategories element : results) {
				session.delete(element);
			}
			session.delete(article);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public static void deleteArticleInfo(Integer articleId) {
		Session session = Reader.getFactory().openSession();
		try {
			String sqlText = "DELETE FROM article WHERE article.article_id =aId ;DELETE FROM article_categories WHERE article_id =aId ;DELETE FROM article_news_words  WHERE article_id =aId ;DELETE FROM article_news_words_stem  WHERE article_id =aId ;DELETE FROM articles_uncommon_words WHERE article_id =aId ;DELETE FROM articles_uncommon_words_stem  WHERE article_id =aId ;DELETE FROM carrot_article  WHERE article_id =aId ;DELETE FROM carrot_article2  WHERE article_id =aId ;DELETE FROM kmeans_uncommon_stem_words  WHERE article_id =aId ;DELETE FROM kmeans_uncommon_words WHERE article_id =aId ;";
			sqlText = sqlText.replaceAll("aId", articleId.toString());
			Query query =session.createSQLQuery(sqlText);
			query.executeUpdate();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static List<CustomCategories> getCategoriesList() {
		List<CustomCategories> cat = new ArrayList<CustomCategories>();
		Session session = Reader.getFactory().openSession();
		try {
			Query query =session.createSQLQuery(RssConstants.QUERY_GET_CATEGORIES).setResultTransformer( Transformers.aliasToBean(CustomCategories.class)) ;
			cat = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return cat;
	}

	@SuppressWarnings("unchecked")
	public static List<Article> getArticlesByCategory(Integer catId) {
		List<Article> results = new ArrayList<Article>();
		Session session = Reader.getFactory().openSession();
		try {
			
			Query query =session.createSQLQuery("SELECT * FROM article LEFT OUTER JOIN article_categories  ON article.article_id = article_categories.article_id WHERE article.words_count > 300 and article_categories.category_id = :catId ORDER BY article.article_id DESC").
					addEntity(Article.class)
					.setParameter("catId", catId);
			results = query.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	public static List<Article> getTodayArticles() {
		List<Article> results = new ArrayList<Article>();
		Session session = Reader.getFactory().openSession();
		try {
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.ge(Article.FIELD_PUB_DATE, new java.util.Date()));
			cr.add(Restrictions.gt(Article.FIELD_WORDS_COUNT, 300));
			cr.addOrder(Order.desc(Article.FIELD_ARTICLE_ID));
			results = cr.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public static List<CustomArticle> getArticlesByKeywords(String keywordsString, List<CustomArticle> articles2) {
		List<CustomArticle> results = new ArrayList<CustomArticle>();
		Session session = Reader.getFactory().openSession();
		try {
			if(articles2 != null && articles2.size() > 0)
			{
				String articlesList = TextUtils.getListOfObjectsAsStringOneLine(articles2);
				Query query =session.createSQLQuery("select "+ 
				"article.article_id, article.title, article.link, article.description, "+
				"article.pub_date, article.update_date, article.site_id, article.full_text, article.words_count, "+
				"sum(article_news_words.frequency)as  frequency, sites.name  as site_name "+
				"from article "+ 
				"inner join sites on sites.id = article.site_id "+
				"inner join  article_news_words on article.article_id = article_news_words.article_id "+
				"inner join news_words on article_news_words.word_id = news_words.id "+
				"where news_words.word in "+ keywordsString + 
				" and article.article_id in "+ AgregatorUtils.parseKeywords(articlesList) +
				" group by article.article_id, sites.name order by frequency DESC")
				.setResultTransformer( Transformers.aliasToBean(CustomArticle.class)) ;
						
				results = query.list();
			}
			else
			{
				Query query =session.createSQLQuery("select "+ 
				"article.article_id, article.title, article.link, article.description, "+
				"article.pub_date, article.update_date, article.site_id, article.full_text, article.words_count, "+
				"sum(article_news_words.frequency)as  frequency, sites.name  as site_name "+
				"from article "+ 
				"inner join sites on sites.id = article.site_id "+
				"inner join  article_news_words on article.article_id = article_news_words.article_id "+
				"inner join news_words on article_news_words.word_id = news_words.id "+
				"where news_words.word in "+ keywordsString + 
				" group by article.article_id, sites.name order by frequency DESC")
				.setResultTransformer( Transformers.aliasToBean(CustomArticle.class)) ;
						
				results = query.list();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	public static List<CustomArticle> getArticlesByKeywordsStem(String keywordsString, List<CustomArticle> articles2) {
		List<CustomArticle> results = new ArrayList<CustomArticle>();
		Session session = Reader.getFactory().openSession();
		try {
			if(articles2 != null && articles2.size() > 0)
			{
				String articlesList = TextUtils.getListOfObjectsAsStringOneLine(articles2);
				Query query =session.createSQLQuery("select  "+
						"article.article_id, article.title, article.link, article.description, "+
						"article.pub_date, article.update_date, article.site_id, article.full_text, article.words_count, "+
						"sum(article_news_words_stem.frequency)as  frequency, sites.name as site_name "+
						"from article  "+
						"inner join sites on sites.id = article.site_id "+
						"inner join  article_news_words_stem on article.article_id = article_news_words_stem.article_id "+
						"inner join news_words_stem on article_news_words_stem.word_id = news_words_stem.id "+
						"where news_words_stem.word in "+ keywordsString + 
						" and article.article_id in "+ AgregatorUtils.parseKeywords(articlesList) +
						" group by article.article_id, sites.name order by frequency DESC")
				.setResultTransformer( Transformers.aliasToBean(CustomArticle.class)) ;
				results = query.list();
			}
			else
			{
				Query query =session.createSQLQuery("select  "+
						"article.article_id, article.title, article.link, article.description, "+
						"article.pub_date, article.update_date, article.site_id, article.full_text, article.words_count, "+
						"sum(article_news_words_stem.frequency)as  frequency, sites.name as site_name "+
						"from article  "+
						"inner join sites on sites.id = article.site_id "+
						"inner join  article_news_words_stem on article.article_id = article_news_words_stem.article_id "+
						"inner join news_words_stem on article_news_words_stem.word_id = news_words_stem.id "+
						"where news_words_stem.word in "+ keywordsString + 
						" group by article.article_id, sites.name order by frequency DESC")
				.setResultTransformer( Transformers.aliasToBean(CustomArticle.class)) ;
				results = query.list();
			}
			

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	public static List<CustomInfo> getCustomInfoList(String queryString) {
		List<CustomInfo> results = new ArrayList<CustomInfo>();
		Session session = Reader.getFactory().openSession();
		try {
			Query query = session.createSQLQuery(queryString).setResultTransformer( Transformers.aliasToBean(CustomInfo.class)) ;
			results = query.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	public static List<CustomArticle> getCustomArticles(String queryS) {
		List<CustomArticle> results = new ArrayList<CustomArticle>();
		Session session = Reader.getFactory().openSession();
		try {
				Query query =session.createSQLQuery(queryS)
				.setResultTransformer( Transformers.aliasToBean(CustomArticle.class)) ;
				results = query.list();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

	public static CustomGroupKeywordsInfo getCustomGroupKeywordsInfo(String keywords, boolean stem) {
		CustomGroupKeywordsInfo keywordsInfo = null;
		List<CustomInfo> results = new ArrayList<CustomInfo>();
		Session session = Reader.getFactory().openSession();
		try {
			if(stem)
			{
				Query query =session.createSQLQuery("select "+
					"1 as id, "+
					"string_agg(distinct article.article_id || ' '|| article.title || ' <a href=\"' ||article.link || '\"> Eiti </a> ' || '<br>','') as text, "+
					"count (distinct article.article_id) as frequency "+
					"from article "+
					"inner join  article_news_words_stem on article.article_id = article_news_words_stem.article_id   "+
					"inner join news_words_stem on article_news_words_stem.word_id = news_words_stem.id  "+
					"where  "+
					"news_words_stem.id = article_news_words_stem.word_id and  "+
					"news_words_stem.word in "+ AgregatorUtils.parseKeywords(keywords) +" and "+
					"article.words_count > 300 "+
					"union "+
					"select  "+
					"1 as id,  "+
					"array_to_string(array(select news_words_stem.word "+
					"from news_words_stem  "+
					"inner join article_news_words_stem on article_news_words_stem.word_id = news_words_stem.id   "+
					"inner join  article on article.article_id = article_news_words_stem.article_id    "+
					"where   "+ 
					"news_words_stem.word in "+ AgregatorUtils.parseKeywords(keywords) +" and "+ 
					"article.words_count > 300  "+
					"group by news_words_stem.word "+
					"order by sum(article_news_words_stem.frequency) desc "+
					"limit 10), ' ') as text, "+
					"count(1) as frequency")
				.setResultTransformer( Transformers.aliasToBean(CustomInfo.class)) ;
				results = query.list();
			}
			else
			{
				Query query =session.createSQLQuery("select "+
					"1 as id, "+
					"string_agg(distinct article.article_id || ' '|| article.title || ' <a href=\"' ||article.link || '\"> Eiti </a> ' || '<br>','') as text, "+
					"count (distinct article.article_id) as frequency "+
					"from article "+
					"inner join  article_news_words on article.article_id = article_news_words.article_id "+  
					"inner join news_words on article_news_words.word_id = news_words.id "+ 
					"where "+
					"news_words.id = article_news_words.word_id and "+
					"news_words.word in "+ AgregatorUtils.parseKeywords(keywords) +" and "+
					"article.words_count > 300 " +
					" union "+
					"select  "+
					"1 as id,  "+
					"array_to_string(array(select news_words.word "+
					"from news_words  "+
					"inner join article_news_words on article_news_words.word_id = news_words.id   "+
					"inner join  article on article.article_id = article_news_words.article_id    "+
					"where   "+ 
					"news_words.word in "+ AgregatorUtils.parseKeywords(keywords) +" and "+ 
					"article.words_count > 300  "+
					"group by news_words.word "+
					"order by sum(article_news_words.frequency) desc "+
					"limit 10), ' ') as text, "+
					"count(1) as frequency")
				.setResultTransformer( Transformers.aliasToBean(CustomInfo.class)) ;
				results = query.list();
			}
			

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		keywordsInfo = new CustomGroupKeywordsInfo(results.get(1).getText(), null, results.get(0).getFrequency(), results.get(0).getText(), null);
		
		return keywordsInfo;
	}

	public static List<CustomInfo> getCustomKeysSetByKeywordsList(boolean stem,String keywords) {
		List<CustomInfo> results = new ArrayList<CustomInfo>();
		Session session = Reader.getFactory().openSession();
		try {
			if(stem)
			{
				Query query =session.createSQLQuery("select article.article_id as id, "+
					"string_agg(news_words_stem.word, ',') as text, "+
					"count(news_words_stem.word) as frequency "+
					"from article, news_words_stem, article_news_words_stem "+
					"where article.article_id = article_news_words_stem.article_id "+
					"and article_news_words_stem.word_id = news_words_stem.id "+
					"and article_news_words_stem.frequency > 1 "+
					"and article.words_count > 300 "+
					"and news_words_stem.word in "+ AgregatorUtils.parseKeywords(keywords) +
					"group by article.article_id  "+
					"order by frequency desc")
				.setResultTransformer( Transformers.aliasToBean(CustomInfo.class)) ;
				results = query.list();
			}
			else
			{
				Query query =session.createSQLQuery("select article.article_id as id, "+
					"string_agg(news_words.word, ',') as text, "+
					"count(news_words.word) as frequency "+
					"from article, news_words, article_news_words "+
					"where article.article_id = article_news_words.article_id "+
					"and article_news_words.word_id = news_words.id "+
					"and article_news_words.frequency > 1 "+
					"and article.words_count > 300 "+
					"and news_words.word in "+ AgregatorUtils.parseKeywords(keywords) +
					"group by article.article_id  "+
					"order by frequency desc")
				.setResultTransformer( Transformers.aliasToBean(CustomInfo.class)) ;
				results = query.list();
			}
			

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results;
	}

}
