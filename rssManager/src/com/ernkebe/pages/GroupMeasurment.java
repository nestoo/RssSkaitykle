package com.ernkebe.pages;

import java.util.List;

import org.carrot2.core.Cluster;

import com.ernkebe.agregator.utils.AgregatorUtils;
import com.ernkebe.database.DbUtils;
import com.ernkebe.entities.grouping.CustomInfo;

public class GroupMeasurment {

	public static void measureKategories(List<String> kategorijos, List<Cluster> clusters, boolean stem) {
		int katCount = 0;
		Double sum = 0.0;
		Double groupvid = 0.0;
		System.out.println("GroupMeasurment.measureKategories()");
		for (String string : kategorijos) {
			if(!string.contains("Other"))
			{
				katCount ++;
				String kat =string.replaceAll(",", " ").trim().toLowerCase();
				//String[] kat = string.replace(",", " ").trim().toLowerCase();
				String query = "";
				if(stem)
				{
					query = "select 1 as id, to_char(max(common_words.frequency),'FM999,999.00') as text, count(1) as frequency "+
						" from common_words  "+
						" where common_words.word  like any "+AgregatorUtils.parseKeywords2(kat);
				}
				else
				{
					query ="select 1 as id, to_char(avg(common_words.frequency),'FM999,999.00') as text, count(1) as frequency from common_words  where common_words.word in "+ AgregatorUtils.parseKeywords(kat);
				}
				
				List<CustomInfo> item = DbUtils.getCustomInfoList(query);
				sum = sum + new Double(item.get(0).getText().replaceAll(",", ""));
			}
		}
		System.out.println("vid = " + sum / katCount);
		Double sum1 = 0.0;
		for (Cluster cluster : clusters) {
			sum1 = sum1+ cluster.getDocuments().size();
		}
		System.out.println("GroupMeasurment.measureKategories()  cluster vid " + sum1 +" :  " + sum1 / clusters.size());
	}

}
