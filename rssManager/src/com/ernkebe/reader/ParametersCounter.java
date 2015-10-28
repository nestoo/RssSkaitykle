package com.ernkebe.reader;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ParametersCounter {
	
	private static Integer executionTime = 0;
	private static Integer feedsInserted = 0;
	private static Integer categoriesInserted = 0;
	

	/*
	 * Count program execution time in seconds
	 */
	public static Integer getExecutionTime(long startTime, long endTime) {
		Integer time = 0;
		time = (int) (endTime - startTime);
		setExecutionTime(time);
		return time;
	}
	
	public static Double getExecutionTimeInSeconds(long startTime, long endTime) {
		Double time = new Double(0);
		time = (double) (endTime - startTime);
		return time;
	}

	public static String  printParameter()
	{
		String rez = "";
		NumberFormat formatter = new DecimalFormat("#0.00000");
		rez = rez+"Execution time is " + formatter.format((executionTime) / 1000d) + " seconds\n";
		rez = rez+"Feeds inserted "+feedsInserted+"\n";
		rez = rez+"Categories inserted "+ categoriesInserted+"\n";
		return rez;
	}
	
	public static void addFeedsInserted() {
		feedsInserted ++;
	}
	
	public static void addCategoriesInserted() {
		categoriesInserted ++;
	}

	public static Integer getExecutionTime() {
		return executionTime;
	}


	public static void setExecutionTime(Integer executionTime) {
		ParametersCounter.executionTime = executionTime;
	}


	public static Integer getFeedsInserted() {
		return feedsInserted;
	}


	public static void setFeedsInserted(Integer feedsInserted) {
		ParametersCounter.feedsInserted = feedsInserted;
	}


	public static Integer getCategoriesInserted() {
		return categoriesInserted;
	}


	public static void setCategoriesInserted(Integer categoriesInserted) {
		ParametersCounter.categoriesInserted = categoriesInserted;
	}
	
	
}
