package com.ernkebe.entities.grouping;

import java.math.BigInteger;

public class CustomInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9182357552892800529L;
	private Integer id;
	private String text;
	private BigInteger frequency;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public BigInteger getFrequency() {
		return frequency;
	}
	public void setFrequency(BigInteger frequency) {
		this.frequency = frequency;
	}
	@Override
	public String toString() {
		return text + " (" + frequency + ")";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
