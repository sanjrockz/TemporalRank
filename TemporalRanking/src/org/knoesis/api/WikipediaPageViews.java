package org.knoesis.api;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.knoesis.utils.*;
import org.knoesis.models.*;
import org.knoesis.utils.Commons;

public class WikipediaPageViews {
	
	/*
	 * phrase: Name of the wikipedia page Eg: "President Barack Obama" 
	 * returns a string of the form President%20Barack%20Obama
	 */
	private String formatPhrase(String phrase) {
		int count = 0;
		StringTokenizer tokenizePhrase = new StringTokenizer(phrase," ");
		List<String> tokens = new ArrayList<String>();
		String response = new String();
		while(tokenizePhrase.hasMoreTokens()) {
			String part = tokenizePhrase.nextToken();
			tokens.add(part);			
		}		
		for(String token : tokens) {
			response = count != 0 ? (response + "%20" + token) : token;
			count++;
		}
		return response;
	}
	
	/*Formatting the date to return 2 digit month*/
/*	private String formatDate(Calendar date, String format) {
		SimpleDateFormat form = new SimpleDateFormat(format);
		String dt = form.format(date.getTime());
		return dt;	
	}*/
	
	/*
	 * Entity: name of the Wikipedia Page whose page-views are to be counted.
	 * date: Date in the form of yyyymm
	 */
	private JSONObject getWebPage(String Entity, String date) {
		JSONObject responseObject = new JSONObject();

		try {
			URL url = new URL(WikipediaConstants.PageView_URL + date + "/" + Entity);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = reader.readLine();
			responseObject = new JSONObject(response);			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return responseObject;
	}	
	
	private int parseJSONObject(JSONObject response, String property, String key) {
		int value = 0;
		try {
			  JSONObject propertyObj = response.getJSONObject(property);
			  value = Integer.parseInt(propertyObj.getString(key));			 
		} catch(Exception e) {
			value = -1;
			//e.printStackTrace();
		}
		return value;
	}
	
	public PageStatistics getPageStats(String entity, Calendar date) {			
				
		int pageViews, prevViews = 0;
		double percentageDiff = 0.0;
		JSONObject response = getWebPage(formatPhrase(entity),Commons.formatDate(date, WikipediaConstants.YYYYMM));
	
		//PageViews on a given day
		pageViews = parseJSONObject(response, WikipediaConstants.JSON_Property, Commons.formatDate(date, WikipediaConstants.YYYYMMDD));
		
		//PageViews on the previous day
		Calendar previousDate = (Calendar)date.clone();
		previousDate.add(Calendar.DAY_OF_MONTH,-1);
		prevViews = parseJSONObject(response, WikipediaConstants.JSON_Property, Commons.formatDate(previousDate, WikipediaConstants.YYYYMMDD));
		
		if(prevViews > pageViews) {
		//Calculate Percentage Decrease
			percentageDiff = (((double)prevViews - (double)pageViews)/(double)prevViews) * 100;
			percentageDiff = (-1) * percentageDiff;
		}
		else {
		//Calculate Percentage Increase
			percentageDiff = (((double)pageViews - (double)prevViews)/(double)pageViews) * 100;
		}
		
		
		if(percentageDiff == Double.POSITIVE_INFINITY || percentageDiff == Double.NEGATIVE_INFINITY || Double.isNaN(percentageDiff)) 
			percentageDiff = 0;
		
		//System.out.println(entity + "\t" + pageViews + "\t" + Commons.formatDouble(percentageDiff,WikipediaConstants.DECIMAL_FORMAT) + "\t" + Commons.formatDate(previousDate, WikipediaConstants.YYYYMMDD));
		PageStatistics pgStats = new PageStatistics(pageViews,Double.parseDouble(Commons.formatDouble(percentageDiff,WikipediaConstants.DECIMAL_FORMAT)));		
		return pgStats; 
		
	}
		
	/*public static void main(String[] args){
		
		WikipediaPageViews wp = new WikipediaPageViews();
		Calendar date = Calendar.getInstance();
		date.set(2013, 01, 15);
		PageStatistics pgStats = wp.getPageStats(wp, "President Barack Obama", date);
		System.out.println("Final results: " + pgStats.getPageViewCount() + "\t\t" + pgStats.getPercentageIncrease());
		
	}*/
	
}
