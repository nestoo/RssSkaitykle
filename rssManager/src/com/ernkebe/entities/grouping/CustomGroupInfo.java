package com.ernkebe.entities.grouping;

import java.util.List;


public class CustomGroupInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private Integer count;
	private String news;
	private List<String> newdIdList;
	public CustomGroupInfo(Integer id,String title, int count, String news, List<String> newdIdList) {
		this.id = id;
		this.title = title;
		this.count = count;
		this.news = news;
		this.newdIdList = newdIdList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getNews() {
		return news;
	}
	public void setNews(String news) {
		this.news = news;
	}
	@Override
	public String toString() {
		return "article_id=" + id + " " + news;
	}
	
	
}
