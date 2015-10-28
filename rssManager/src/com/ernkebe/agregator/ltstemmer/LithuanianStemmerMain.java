package com.ernkebe.agregator.ltstemmer;

import com.ernkebe.agregator.ltstemmer.snowball.ext.englishStemmer;
import com.ernkebe.agregator.ltstemmer.snowball.ext.lithuanianStemmer;

public class LithuanianStemmerMain {
	
	
	public static void main(String args[]){
	//create a new instance of PorterStemmer
		englishStemmer stemmer1 = new englishStemmer();
		stemmer1.setCurrent("happiness");
		if(stemmer1.stem())
		{
		        System.out.println(stemmer1.getCurrent());
		}
		
		lithuanianStemmer stemmer = new lithuanianStemmer();
		stemmer.setCurrent("galvoja");
		//stemmer.setCurrent("rytojus");
		if(stemmer.stem())
		{
		        System.out.println(stemmer.getCurrent());
		}
	}
}
