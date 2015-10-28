package com.ernkebe.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.Sites;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.reader.Reader;

@ManagedBean
@SessionScoped
public class TiesiogiaiPage {

	
	private int activeIndex= 0;
	private List<Sites> saltiniai;
	private String feedLink;
	private Sites selectedSaltinis;
	
	public TiesiogiaiPage() {
		if(Reader.isStarted() == false)
		{
			Reader.startSession();
		}
		activeIndex = 2;
		feedLink = null;
		generateSaltiniai();
	}

	private void generateSaltiniai() {
		saltiniai = new ArrayList<Sites>();
		saltiniai = SqlUtils.findAll(Sites.class);
		
	}
	
	public void changeSaltinis(){ 
	    if(selectedSaltinis != null && selectedSaltinis.getId() != null)
	    {
	    	feedLink = selectedSaltinis.getXmlUrl();
	    }
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<Sites> getSaltiniai() {
		return saltiniai;
	}

	public void setSaltiniai(List<Sites> saltiniai) {
		this.saltiniai = saltiniai;
	}

	public String getFeedLink() {
		return feedLink;
	}

	public void setFeedLink(String feedLink) {
		this.feedLink = feedLink;
	}

	public Sites getSelectedSaltinis() {
		return selectedSaltinis;
	}

	public void setSelectedSaltinis(Sites selectedSaltinis) {
		this.selectedSaltinis = selectedSaltinis;
	}
	
	
}
