package com.ernkebe.pages;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;

import com.ernkebe.agregator.utils.TextUtils;
import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.DbUtils;
import com.ernkebe.entities.grouping.CustomInfo;
import com.ernkebe.reader.AgregatorParameters;
import com.ernkebe.reader.Reader;

@ManagedBean
@SessionScoped
public class MainPage extends Page{

	private DashboardModel model;
	private CartesianChartModel  newsmodel;
	private CartesianChartModel  newsByDatemodel;
	
	private String topKey;
	private String  topStemKey;
	
	public MainPage()
	{
		if(Reader.isStarted() == false)
		{
			Reader.startSession();
		}
		initializeModel();
		initializeValues();
		
		initializeNewsDiagram();
		initializeNewsByDateDiagram();
		
	}
	
	@Override
	public void updateOnMenuChange()
	{
		System.out.println("MainPage.updateOnMenuChange()");
		initializeValues();
		
		initializeNewsDiagram();
		initializeNewsByDateDiagram();
	}
	
	private void initializeNewsByDateDiagram() {
		List<CustomInfo> newsList = DbUtils.getCustomInfoList(RssConstants.NEWS_FREQUENCY_BY_DATE);
		newsByDatemodel = new CartesianChartModel();
		 BarChartSeries item = new BarChartSeries();
		item.setLabel("Šaltinis");
		for (CustomInfo customInfo : newsList) {
			 item.setLabel(customInfo.getText());
			 item.set(customInfo.getText(), customInfo.getFrequency());
		}
		newsByDatemodel.addSeries(item);
	}

	private void initializeNewsDiagram() {
		List<CustomInfo> newsList = DbUtils.getCustomInfoList(RssConstants.ARTICLES_COUNT_BY_NEWS_QUERY);
		newsmodel = new CartesianChartModel();
		 BarChartSeries item = new BarChartSeries();
		 item.setLabel("Šaltinis");
		for (CustomInfo customInfo : newsList) {
			 item.setLabel(customInfo.getText());
			 item.set(customInfo.getText(), customInfo.getFrequency());
		}
		newsmodel.addSeries(item);
	}

	private void initializeValues() {
		List<CustomInfo> topKeyList = DbUtils.getCustomInfoList(RssConstants.TOP_KEYS_QUERY);
		topKey = TextUtils.getListOfObjectsAsString(topKeyList);
		
		List<CustomInfo> topStemKeyList = DbUtils.getCustomInfoList(RssConstants.TOP_STEM_KEYS_QUERY);
		topStemKey = TextUtils.getListOfObjectsAsString(topStemKeyList);
		
	}

	private void initializeModel() {
		if(model == null || model.getColumnCount() == 0)
		{
			model = new DefaultDashboardModel();
			DashboardColumn column1 = new DefaultDashboardColumn();
			DashboardColumn column2 = new DefaultDashboardColumn();
			DashboardColumn column3 = new DefaultDashboardColumn();
			DashboardColumn column4 = new DefaultDashboardColumn();
			column1.addWidget("keywords");
			column2.addWidget("keystem");
			column3.addWidget("news");
			column4.addWidget("newsforDay");
			
			
			model.addColumn(column1);
			model.addColumn(column2);
			model.addColumn(column3);
			model.addColumn(column4);
		}
	}

	private int activeIndex= 0;

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public DashboardModel getModel() {
		return model;
	}

	public void setModel(DashboardModel model) {
		this.model = model;
	}

	public String getTopKey() {
		return topKey;
	}

	public void setTopKey(String topKey) {
		this.topKey = topKey;
	}

	public String getTopStemKey() {
		return topStemKey;
	}

	public void setTopStemKey(String topStemKey) {
		this.topStemKey = topStemKey;
	}

	public CartesianChartModel getNewsmodel() {
		return newsmodel;
	}

	public void setNewsmodel(CartesianChartModel newsmodel) {
		this.newsmodel = newsmodel;
	}

	public CartesianChartModel getNewsByDatemodel() {
		return newsByDatemodel;
	}

	public void setNewsByDatemodel(CartesianChartModel newsByDatemodel) {
		this.newsByDatemodel = newsByDatemodel;
	}
	
	
}
