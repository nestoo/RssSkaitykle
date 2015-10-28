package com.ernkebe.agregator.utils;

import java.util.List;




import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Jsoup;

import com.ernkebe.appconstants.RssConstants;
import com.google.common.base.Joiner;

public class TextUtils {

	
	public static String html2text(String html) {
	    return Jsoup.parse(html).text();
	}
	
	
	/**
	 * @param text
	 * @return grazina teksta kuriame nera nezodiniu simboliu ir visos raides mazosios
	 */
	public static String formatWord(String text) {
		String t = text.replaceAll(RssConstants.NON_WORDS_SYMBOLS,"").toLowerCase();
		t= t.replaceAll("\\s+", " ");
		String rezults = "";
		for(String word: t.split(" "))
		{
			if(word.length() > 2)
			{
				rezults = rezults + word + " ";
			}
		}
		return rezults;
	}


	public static String formatFullText(String fullText) {
		String rezults = null ;
		rezults = fullText.toString().replaceAll("\\<.*?>","");
		return formatWord(rezults);
	}
	
	public static String getListAsString(List<String> items) 
	{
		Joiner joiner = Joiner.on("");
		return joiner.join(items);
	}
	
	public static <T> String getListOfObjectsAsString(List<T> items) 
	{
		String rezults = "" ;
		for (Object object : items) {
			rezults = rezults + object.toString() + System.getProperty("line.separator");
		}
		return rezults;
	}
	
	public static <T> String getListOfObjectsAsStringOneLine(List<T> items) 
	{
		String rezults = "" ;
		for (Object object : items) {
			rezults = rezults + object.toString() + " ";
		}
		return rezults;
	}
	
}
