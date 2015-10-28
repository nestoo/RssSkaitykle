package com.ernkebe.reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.InputSource;

import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.agregator.utils.TextUtils;
import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.Sites;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.entities.initial.ArticleCategories;
import com.ernkebe.entities.initial.ProvidedCategories;
import com.google.common.base.Joiner;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

public class XmlReader {

	public static void read() {
		List<Sites> sites = DbUtils.getSites();
		for (Sites siteItem : sites) {
			System.out.println(siteItem.getXmlUrl());
			readXml(siteItem.getXmlUrl(), siteItem);
		}
	}

	private static void readXml(String url, Sites site) {
		try {
			InputStream is = new URL(url).openConnection().getInputStream();
			InputSource source = new InputSource(is);

			SyndFeed feeds = new SyndFeedInput().build(source);

			for (Iterator<?> entryIter = feeds.getEntries().iterator(); entryIter.hasNext();) 
			{
				Article entryFeed = reedEntryFeed(entryIter, site);

				if (entryFeed != null) {
					saveFeedInformation(entryFeed);
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		}
	}

	private static void saveFeedInformation(Article entryFeed) {
		int feedId = DbUtils.saveArticle(entryFeed);
		ParametersCounter.addFeedsInserted();

		int categoryId = 0;

		for (String catItem : entryFeed.getCatList()) {
			categoryId = getCategoryId(catItem);

			if (feedId != 0 && categoryId != 0) {
				DbUtils.saveFeedCategory(feedId, categoryId);
			}
		}

	}

	private static Article reedEntryFeed(Iterator<?> entryIter, Sites site) {
		String title = "";
		String description = "";
		String link = "";
		Date pubDate = null;
		Date updateDate = null;
		List<String> categories = new ArrayList<String>();
		List<String> fullText = new ArrayList<String>();

		SyndEntry syndEntry = (SyndEntry) entryIter.next();

		title = syndEntry.getTitle();
		if (syndEntry.getPublishedDate() != null) {
			pubDate = new Date(syndEntry.getPublishedDate().getTime());
		}
		if(pubDate == null || AgregatorUtils.checkDateValid(pubDate))
		{
			if (DbUtils.isUniqueArticleName(title)) {
				link = syndEntry.getLink();
				if(syndEntry.getDescription() != null)
				{
					description = syndEntry.getDescription().getValue();
				}
	
				if (syndEntry.getPublishedDate() != null) {
					pubDate = new Date(syndEntry.getPublishedDate().getTime());
				}
				if (syndEntry.getUpdatedDate() != null) {
					updateDate = new Date(syndEntry.getUpdatedDate().getTime());
				}
	
				if (syndEntry.getContents() != null) {
					for (Iterator<?> it = syndEntry.getContents().iterator(); it
							.hasNext();) {
						SyndContent synd = (SyndContent) it.next();
						if (synd != null) {
							if (!synd.getValue().equals(description)) {
								fullText.add(synd.getValue());
							}
						}
					}
				}
	
				if (syndEntry.getCategories() != null) {
					for (Iterator<?> it = syndEntry.getCategories().iterator(); it
							.hasNext();) {
						SyndCategory syndCategory = (SyndCategory) it.next();
						if (syndCategory != null) {
							categories.add(syndCategory.getName());
						}
					}
				}
	
				if (site.getName().contains("-") && categories.size() == 0) {
					String feedCategory = site.getName().substring(
							site.getName().indexOf("-") + 2,
							site.getName().length());
					categories.add(feedCategory);
				}
	
				if (fullText == null || fullText.size() == 0) {						// for representation use only despription, for grouping use full texts
					fullText.add(TextUtils.formatFullText(HTMLReader.getFullText(link, site)));
				}
				else
				{
					fullText.add(TextUtils.formatFullText(description));
				}
				
				String fullTextString = TextUtils.getListAsString(fullText);
				if(fullTextString.length() < description.length() || fullTextString.isEmpty() || fullTextString == "")					// test if fullText is realy a full text
				{
					String text= TextUtils.formatFullText(description);
					if(text.length() > title.length())
					{
						fullTextString = TextUtils.formatFullText(description);
					}
					else
					{
						fullTextString = title;
					}
				}
	
				if (title != null)// feed title is required
				{
					Article article = new Article(
							title,
							link,
							description,
							pubDate != null ? new java.sql.Date(pubDate.getTime()) : new java.util.Date(), 
							updateDate != null ? new java.sql.Date(updateDate.getTime()) : new java.util.Date(),
							site.getId(),
							fullTextString,
							fullTextString.split(" ").length);
					article.setCatList(categories);
					return article;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		 else {
				return null;
			}
	}

	/*
	 * Finds unique category in db, if not creates new and return it's id
	 */
	private static int getCategoryId(String categoryName) {
		ProvidedCategories category = DbUtils.getCategoryByName(categoryName);
		if (category.getId() != null) {
			return category.getId();
		} else {
			ParametersCounter.addCategoriesInserted();
			return DbUtils.saveCategory(categoryName);
		}
	}

	
}
