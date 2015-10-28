package com.ernkebe.agregator.utils;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.ernkebe.appconstants.RssConstants;
import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.CommonWords;
import com.ernkebe.entities.CommonWordsEnds;

public class AgregatorUtils {

	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
		Comparator<K> valueComparator =  new Comparator<K>() {
		    public int compare(K k1, K k2) {
		        int compare = map.get(k2).compareTo(map.get(k1));
		        if (compare == 0) return 1;
		        else return compare;
		    }
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
	
	public static HashMap<String, Integer> generateCommonWordsMap() {
		HashMap<String, Integer> commonWordsMap = new HashMap<String, Integer>();
		List<CommonWords> commonWordsList = SqlUtils.findAll(CommonWords.class);                
		for (CommonWords item : commonWordsList) {
			commonWordsMap.put(item.getWord(), item.getFrequency());
		}  
		return commonWordsMap;
	}
	
	public static HashMap<String,Double> convertToStringToHashMap(String text){
		HashMap<String,Double> data = new HashMap<String,Double>();
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(text);
		for ( int i=1; i+2 <= split.length; i+=2 ){
			data.put( split[i], new Double(split[i+1]) );
		}
		return data;
	}

	public static String generateMapKeysToString(TreeMap<String, Integer> commonWordsArticleMap) {
		String rez = "";
		for (String item : commonWordsArticleMap.keySet()) {
			rez = rez + item + " ";
		}
		return rez.trim();
	}

	public static HashMap<String, Integer> generateCommonWordsMapEnds() {
		HashMap<String, Integer> commonWordsMap = new HashMap<String, Integer>();
		List<CommonWordsEnds> commonWordsList = SqlUtils.findAll(CommonWordsEnds.class);                
		for (CommonWordsEnds item : commonWordsList) {
			commonWordsMap.put(item.getWordEnd(), item.getFrequency());
		}  
		return commonWordsMap;
	}

	public static boolean checkDateValid(Date pubDate) {
		Date today = new Date();
		long diff = today.getTime() - pubDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000); 
		if(diffDays >= RssConstants.DAYS_NEWS_OLD)
		{
			return false;
		}
		return true;
	}
	
	public static String parseKeywords2(String keywords) {
		// raktazodziu turi buti formatu (array['x%', 'x%'])
		String rez = "";
		rez = "(array[";
		String[] words = keywords.split(" ");
		for (int i =0; i < words.length; i++) {
			if(i +1 == words.length)
			{
				rez = rez + "'" + words[i].trim().toLowerCase()  + "%'" + "])";
			}
			else
			{
				rez = rez + "'" + words[i].trim().toLowerCase()  + "%'" + ",";
			}
		}
		return rez;
	}

	public static String parseKeywords(String keywords) {
		// raktazodziu turi buti formatu ('x', 'y')
		String rez = "";
		rez = "(";
		String[] words = keywords.split(" ");
		for (int i =0; i < words.length; i++) {
			if(i +1 == words.length)
			{
				rez = rez + "'" + words[i].trim().toLowerCase()  + "'" + ")";
			}
			else
			{
				rez = rez + "'" + words[i].trim().toLowerCase()  + "'" + ",";
			}
		}
		return rez;
	}
	
}
