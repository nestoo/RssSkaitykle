package com.ernkebe.entities.grouping;

import java.math.BigInteger;
import java.util.List;


public class CustomGroupKeywordsInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private BigInteger keycount;
	private BigInteger newscount;
	private String news;
	private String keys;
	
	
	
	public CustomGroupKeywordsInfo(String title, BigInteger keycount,BigInteger newscount, String news, String keys) {
		super();
		this.title = title;
		this.keycount = keycount;
		this.newscount = newscount;
		this.news = news;
		this.keys = keys;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNews() {
		return news;
	}
	public void setNews(String news) {
		this.news = news;
	}
	public BigInteger getKeycount() {
		return keycount;
	}
	public void setKeycount(BigInteger keycount) {
		this.keycount = keycount;
	}
	public BigInteger getNewscount() {
		return newscount;
	}
	public void setNewscount(BigInteger newscount) {
		this.newscount = newscount;
	}
	public String getKeys() {
		return keys;
	}
	public void setKeys(String keys) {
		this.keys = keys;
	}
	
	
}
