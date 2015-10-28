package com.ernkebe.agregator;


public class WordsAnalizer {


	public static void startAnalize(int articleIdFrom) {
		
		
		NewsWordsParser.startAnalize(articleIdFrom);
		
		NewsWordsParserStem.startAnalize(articleIdFrom);
		
	}
	
	

}
