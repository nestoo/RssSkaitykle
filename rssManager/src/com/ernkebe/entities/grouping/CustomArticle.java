package com.ernkebe.entities.grouping;

import java.math.BigInteger;
import java.util.Date;

public class CustomArticle {
	
	private Integer article_id;
	private String title;
	private String link;
	private String description;
	private Date pub_date;
	private Date update_date;
	private int site_id;
	private String full_text;
	private Integer words_count;
	private BigInteger frequency;
	private String site_name;
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getPub_date() {
		return pub_date;
	}
	public void setPub_date(Date pub_date) {
		this.pub_date = pub_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public String getFull_text() {
		return full_text;
	}
	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}
	public Integer getWords_count() {
		return words_count;
	}
	public void setWords_count(Integer words_count) {
		this.words_count = words_count;
	}
	public BigInteger getFrequency() {
		return frequency;
	}
	public void setFrequency(BigInteger frequency) {
		this.frequency = frequency;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public Integer getArticle_id() {
		return article_id;
	}
	public void setArticle_id(Integer article_id) {
		this.article_id = article_id;
	}
	@Override
	public String toString() {
		return article_id.toString();
	}
	
}
