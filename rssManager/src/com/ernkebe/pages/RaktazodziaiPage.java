package com.ernkebe.pages;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;









import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

import com.ernkebe.agregator.grouping.GroupingKeywords;
import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.Sites;
import com.ernkebe.entities.UserData;
import com.ernkebe.entities.grouping.CustomArticle;
import com.ernkebe.entities.grouping.CustomGroupInfo;
import com.ernkebe.entities.grouping.CustomGroupKeywordsInfo;
import com.ernkebe.entities.grouping.CustomInfo;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.export.ExcelExportUtils;
import com.ernkebe.reader.CategoriesCloud;
import com.ernkebe.reader.Reader;

@ManagedBean
@SessionScoped
public class RaktazodziaiPage extends Page {

	
	private int activeIndex= 3;
	private List<CustomArticle> articles;
	private boolean stemKeywords;
	private DefaultStreamedContent articlesFile;
	
	private List<CustomInfo> keywordsSets;
	
	private List<CustomGroupKeywordsInfo> groupArticles; //atvaizduoja pulsapyje
	
	
	
	public RaktazodziaiPage() {
		if(Reader.isStarted() == false)
		{
			Reader.startSession();
		}
		activeIndex = 3;
	}
	
	
	public void constructKeywordsDistinctSets(ActionEvent event) {
		keywordsSets= GroupingKeywords.generateKeyWordsSets(stemKeywords, null);
		generateInfoForPage();
		RequestContext.getCurrentInstance().update("raktazodziai_panel");
		
	}
	
	/**
	 * kiekvienai raktazodziu eibei generuojama info apie turimas naujienas, top raktazodzius
	 */
	private void generateInfoForPage() {
		groupArticles = new ArrayList<CustomGroupKeywordsInfo>();
		for (CustomInfo keySet : keywordsSets) {
			CustomGroupKeywordsInfo item = DbUtils.getCustomGroupKeywordsInfo(keySet.getText(),stemKeywords);
			item.setKeys(keySet.getText());
			BigInteger count = BigInteger.valueOf(keySet.getText().split(" ").length);
			item.setKeycount(count);
			groupArticles.add(item);
		}
	}


	public void openKeysGroup(ActionEvent event) {
		 CustomGroupKeywordsInfo row = (CustomGroupKeywordsInfo) event.getComponent().getAttributes().get("currentRow");
		 clusterKeysSubGroup(row);
		 RequestContext.getCurrentInstance().update("raktazodziai_panel");
	}	
	
	private void clusterKeysSubGroup(CustomGroupKeywordsInfo row) {
		keywordsSets= GroupingKeywords.generateKeyWordsSets(stemKeywords, row.getKeys());
		generateInfoForPage();
		RequestContext.getCurrentInstance().update("raktazodziai_panel");
	}


	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<CustomArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<CustomArticle> articles) {
		this.articles = articles;
	}

	public boolean isStemKeywords() {
		return stemKeywords;
	}

	public void setStemKeywords(boolean stemKeywords) {
		this.stemKeywords = stemKeywords;
	}

	public DefaultStreamedContent getArticlesFile() {
		return articlesFile;
	}

	public void setArticlesFile(DefaultStreamedContent articlesFile) {
		this.articlesFile = articlesFile;
	}

	public List<CustomInfo> getKeywordsSets() {
		return keywordsSets;
	}

	public void setKeywordsSets(List<CustomInfo> keywordsSets) {
		this.keywordsSets = keywordsSets;
	}


	public List<CustomGroupKeywordsInfo> getGroupArticles() {
		return groupArticles;
	}


	public void setGroupArticles(List<CustomGroupKeywordsInfo> groupArticles) {
		this.groupArticles = groupArticles;
	}
	
	
}
