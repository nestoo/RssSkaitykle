package com.ernkebe.pages;

import java.net.InetAddress;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.mcavallo.opencloud.Tag;

import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.UserData;
import com.ernkebe.entities.initial.Article;
import com.ernkebe.reader.CategoriesCloud;
import com.ernkebe.reader.Reader;

@ManagedBean
@SessionScoped
public class OriginaliosNaujienosPage extends Page{

	
	private int activeIndex= 1;
	private List<Tag> tags;
	private List<Article> articles;
	private CategoriesCloud cat;
	
	public OriginaliosNaujienosPage() {
		if(Reader.isStarted() == false)
		{
			Reader.startSession();
		}
		activeIndex = 1;
		startRead();
	}
	
	public void startRead() {
		cat = new CategoriesCloud();
		CategoriesCloud.constructCategoriesCloud();
		tags = cat.getCloud().tags();
		articles = DbUtils.getTodayArticles();
		
	}
	
	public void updateArticlesList(ActionEvent event) {
		String catId = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("catId");
		articles = DbUtils.getArticlesByCategory(Integer.parseInt(catId));
	}
	
	public void openLink(ActionEvent event) {
		String link = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("link");
		saveUserData(link);
		try {
			//Desktop.getDesktop().browse(new URL(link).toURI());
		} catch (Exception e) {
		}
	}
	
	private void saveUserData(String link) {
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			System.out.println("IP:" + thisIp.getHostAddress());
			UserData data = new UserData();
			data.setUserIp(thisIp.getHostAddress());
			data.setLink(link);
			SqlUtils.saveNewEntity(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
}
