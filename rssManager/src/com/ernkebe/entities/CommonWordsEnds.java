package com.ernkebe.entities;
// Generated Oct 4, 2014 9:44:00 PM by Hibernate Tools 3.4.0.CR1

/**
 * CommonWords generated by hbm2java
 */
public class CommonWordsEnds implements java.io.Serializable {

	public static final String FIELD_FREQUENCY = "frequency";
	private Integer id;
	private String wordEnd;
	private int frequency;

	public CommonWordsEnds() {
	}

	public CommonWordsEnds(String wordEnd, int frequency) {
		this.wordEnd = wordEnd;
		this.frequency = frequency;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getWordEnd() {
		return wordEnd;
	}

	public void setWordEnd(String wordEnd) {
		this.wordEnd = wordEnd;
	}
	
	

}
