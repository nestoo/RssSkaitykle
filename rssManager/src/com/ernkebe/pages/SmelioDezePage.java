package com.ernkebe.pages;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

import com.ernkebe.agregator.groupingtolls.CarrotGrouping;
import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.Sites;
import com.ernkebe.entities.grouping.CustomArticle;
import com.ernkebe.entities.grouping.CustomGroupInfo;
import com.ernkebe.entities.grouping.CustomInfo;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.export.ExcelExportUtils;
import com.ernkebe.reader.Reader;

@ManagedBean
@SessionScoped
public class SmelioDezePage {

	
	private int activeIndex= 4;
	
	private boolean ltGroup;
	private boolean lingo;
	private boolean kmeans;
	private boolean stcc;
	
	private boolean originalus;
	private boolean prasminiai;
	private boolean kamienizuoti;
	private boolean originalusBeDazniausiu;
	private boolean originalusStemBeDazniausiu;
	
	private DefaultStreamedContent articlesFile;
	
	private List<CustomArticle> customArticle;
	private List<CustomGroupInfo> groupArticles;
	private List<Cluster> clusters;
	private String bendraInfo;
	private List<String> kategorijos;
	
	public SmelioDezePage() {
		if(Reader.isStarted() == false)
		{
			Reader.startSession();
		}
		activeIndex = 4;
		ltGroup = true;
		prasminiai = true;
		lingo= true;
		search(null);
		
	}

	public void search(ActionEvent event) {
		clasterCarrot();
		RequestContext.getCurrentInstance().update("smelio_deze_panel");
	}
	
	public void openNewsGroup(ActionEvent event) {
		 CustomGroupInfo row = (CustomGroupInfo) event.getComponent().getAttributes().get("currentRow");
		 clusterSubGroup(row);
		 RequestContext.getCurrentInstance().update("smelio_deze_panel");
	}
	
	private void clusterSubGroup(CustomGroupInfo row) {
		kategorijos = new ArrayList<String>();
		
		
		Cluster rowCluster = clusters.get(row.getId());
		clusters = CarrotGrouping.groupClusterDocuments(rowCluster, lingo, kmeans, stcc);
		groupArticles = new ArrayList<CustomGroupInfo>();
		bendraInfo = "naujienu sk. " +  rowCluster.getDocuments().size() +" kategoriju sk. " +clusters.size() + " " + System.getProperty("line.separator");
		for ( Cluster cluster : clusters) 
		{
			bendraInfo = bendraInfo +  cluster.getLabel() +  ", " + cluster.getDocuments().size() + System.getProperty("line.separator");
			kategorijos.add(cluster.getLabel());
			
			
			String news = "";
			List<String> newsids = new ArrayList<String>();
			for (Document documents : cluster.getAllDocuments() ) {
				
				news =news + documents.getContentUrl() + " <br></br> ";
				newsids.add(documents.getStringId());
			}
			groupArticles.add(new CustomGroupInfo(cluster.getId(), cluster.getLabel(), cluster.getDocuments().size(),news,newsids));
			
		}
		if(kamienizuoti == true || originalusStemBeDazniausiu == true){
			GroupMeasurment.measureKategories(kategorijos,clusters, true);
		}
		else
		{
			GroupMeasurment.measureKategories(kategorijos,clusters, false);
		}
	}

	private void clasterCarrot() {
		kategorijos = new ArrayList<String>();
		
		generateCustomArticlesForGrouping();
		clusters = CarrotGrouping.groupArticles(customArticle, ltGroup, lingo,kmeans,stcc);
		groupArticles = new ArrayList<CustomGroupInfo>();
		bendraInfo = "naujienu sk. " +  customArticle.size() +" kategoriju sk. " + clusters.size() + " " + System.getProperty("line.separator");
		for ( Cluster cluster : clusters) 
		{
			bendraInfo = bendraInfo +  cluster.getLabel() +  ", " + cluster.getDocuments().size()  + System.getProperty("line.separator");
			kategorijos.add(cluster.getLabel());
			
			String news = "";
			List<String> newsids = new ArrayList<String>();
			for (Document documents : cluster.getAllDocuments() ) {
				
				news =news + documents.getContentUrl() + " <br></br> ";
				newsids.add(documents.getStringId());
			}
			groupArticles.add(new CustomGroupInfo(cluster.getId(), cluster.getLabel(), cluster.getDocuments().size(),news,newsids));
			
		}
		if(kamienizuoti == true || originalusStemBeDazniausiu == true){
			GroupMeasurment.measureKategories(kategorijos,clusters, true);
		}
		else
		{
			GroupMeasurment.measureKategories(kategorijos,clusters, false);
		}
	}

	private void generateCustomArticlesForGrouping() {
		if(originalus)
		{
			customArticle = DbUtils.getCustomArticles(RssConstants.CUSTOM_ARTICLES_ORIGINAL_FOR_TOLLS_QUERY);
		}
		else if(prasminiai)
		{
			customArticle = DbUtils.getCustomArticles(RssConstants.CUSTOM_ARTICLES_KEY_WORDS_FOR_TOLLS_QUERY);
		}
		else if(kamienizuoti)
		{
			customArticle = DbUtils.getCustomArticles(RssConstants.CUSTOM_ARTICLES_STEM_FOR_TOLLS_QUERY);
		}
		else if(originalusBeDazniausiu)
		{
			customArticle = DbUtils.getCustomArticles(RssConstants.CUSTOM_ARTICLES_ORIGINAL_BE_DAZNIAUSIU_FOR_TOLLS_QUERY);
		}
		else if (originalusStemBeDazniausiu)
		{
			customArticle = DbUtils.getCustomArticles(RssConstants.CUSTOM_ARTICLES_ORIGINAL_BE_DAZNIAUSIU_STEM_FOR_TOLLS_QUERY);
		}
		else
		{
			customArticle = DbUtils.getCustomArticles(RssConstants.CUSTOM_ARTICLES_KEY_WORDS_FOR_TOLLS_QUERY);
		}
	}

	public void exportArticles(ActionEvent event) {
		InputStream stream =	ExcelExportUtils.createExcelFileCustomArticles(bendraInfo, groupArticles);
		articlesFile = new DefaultStreamedContent(stream, "xlsx", "naujienos.xlsx");
	}
	
	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public DefaultStreamedContent getArticlesFile() {
		return articlesFile;
	}

	public void setArticlesFile(DefaultStreamedContent articlesFile) {
		this.articlesFile = articlesFile;
	}

	public List<CustomArticle> getCustomArticle() {
		return customArticle;
	}

	public void setCustomArticle(List<CustomArticle> customArticle) {
		this.customArticle = customArticle;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public String getBendraInfo() {
		return bendraInfo;
	}

	public void setBendraInfo(String bendraInfo) {
		this.bendraInfo = bendraInfo;
	}

	public List<CustomGroupInfo> getGroupArticles() {
		return groupArticles;
	}

	public void setGroupArticles(List<CustomGroupInfo> groupArticles) {
		this.groupArticles = groupArticles;
	}

	public boolean isLtGroup() {
		return ltGroup;
	}

	public void setLtGroup(boolean ltGroup) {
		this.ltGroup = ltGroup;
	}

	public boolean isLingo() {
		return lingo;
	}

	public void setLingo(boolean lingo) {
		this.lingo = lingo;
	}

	public boolean isKmeans() {
		return kmeans;
	}

	public void setKmeans(boolean kmeans) {
		this.kmeans = kmeans;
	}

	public boolean isStcc() {
		return stcc;
	}

	public void setStcc(boolean stcc) {
		this.stcc = stcc;
	}

	public boolean isOriginalus() {
		return originalus;
	}

	public void setOriginalus(boolean originalus) {
		this.originalus = originalus;
	}

	public boolean isPrasminiai() {
		return prasminiai;
	}

	public void setPrasminiai(boolean prasminiai) {
		this.prasminiai = prasminiai;
	}

	public boolean isKamienizuoti() {
		return kamienizuoti;
	}

	public void setKamienizuoti(boolean kamienizuoti) {
		this.kamienizuoti = kamienizuoti;
	}

	public boolean isOriginalusBeDazniausiu() {
		return originalusBeDazniausiu;
	}

	public void setOriginalusBeDazniausiu(boolean originalusBeDazniausiu) {
		this.originalusBeDazniausiu = originalusBeDazniausiu;
	}

	public boolean isOriginalusStemBeDazniausiu() {
		return originalusStemBeDazniausiu;
	}

	public void setOriginalusStemBeDazniausiu(boolean originalusStemBeDazniausiu) {
		this.originalusStemBeDazniausiu = originalusStemBeDazniausiu;
	}
	
}
