package com.ernkebe.reader;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DbTimestampType;

import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.initial.Article;

/**
 * @author Nesta
 * Salina naujienas is visu lentu, pagal naujienos data
 *
 */
public class NewsRemover {

	public static void removeNews() 
	{
		List<Article> articles = SqlUtils.findAll(Article.class);
		for (Article article : articles) 
		{
			if(!AgregatorUtils.checkDateValid(article.getPubDate()))
			{
				DbUtils.deleteArticleInfo(article.getArticleId());
			}
			
		}
		
		
	}

}
