package com.ernkebe.entities.grouping;

// Generated May 6, 2014 8:11:34 PM by Hibernate Tools 3.4.0.CR1

/**
 * ArticleCategories generated by hbm2java
 */
public class CarrotCategories implements java.io.Serializable {

	private Integer id;
	private String carrotCategory;
	
	public CarrotCategories(String carrotCategory) {
		this.carrotCategory = carrotCategory;
	}

	public CarrotCategories() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarrotCategory() {
		return carrotCategory;
	}

	public void setCarrotCategory(String carrotCategory) {
		this.carrotCategory = carrotCategory;
	}


}
