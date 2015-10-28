package com.ernkebe.reader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.DbUtils;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.grouping.CarrotCategories;
import com.ernkebe.entities.grouping.CarrotCategories2;
import com.ernkebe.entities.grouping.CustomCategories;
import com.ernkebe.entities.grouping.KmeansUncommonWords;

public class CategoriesCloud {

	private static Cloud cloud = new Cloud();  // create cloud 
	private static List<CustomCategories> categories;
	
	public static void constructCategoriesCloud()
	{
		cloud = new Cloud(); 
		cloud.setMaxWeight(65.0);
		categories = DbUtils.getCategoriesList();
		for (CustomCategories element : categories) {
			Tag tag = new Tag(element.getName(), element.getCat_id().toString(), element.getCount().intValue());   // creates a tag
			cloud.addTag(tag);
		}
	}
	
	public Cloud getCloud() {
		return cloud;
	}
	
	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

	public static List<CustomCategories> getCategories() {
		return categories;
	}

	public static void setCategories(List<CustomCategories> categories) {
		CategoriesCloud.categories = categories;
	}
	
	

	
}
