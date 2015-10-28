package com.ernkebe.reader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.DbUtils;
import com.ernkebe.entities.Sites;

public class SitesUtils {

	public static void updateSitesList()
	{
		List<Sites> sites = readSitesTextFile();
		DbUtils.updateSites(sites);
	}
	
	private static List<Sites> readSitesTextFile()
	{
		List<Sites> rezSites = new ArrayList<Sites>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(RssConstants.SITE_TEXT_FILE_NAME);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			  
			while ((strLine = br.readLine()) != null)   
			{
				String name = "";
				String xmlUrl = "";
				String mobileUrl = "";
				String logoImage = "";
				
				String[] siteInfo = strLine.split(";"); 
				name = siteInfo[0];
				xmlUrl = siteInfo[1].replaceAll("\\s+","");
				if(siteInfo[2].replaceAll("\\s+","").length() != 0)
				{
					mobileUrl = siteInfo[2].replaceAll("\\s+","");
				}
				logoImage = siteInfo[3].replaceAll("\\s+","");
				
				rezSites.add(new Sites(name, xmlUrl, mobileUrl, logoImage, ""));
			}
			  
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
	   return rezSites;
	}
}
