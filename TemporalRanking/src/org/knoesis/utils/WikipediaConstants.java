package org.knoesis.utils;

public class WikipediaConstants {
	public static final String API_URL = "http://en.wikipedia.org/w/api.php";
	public static final String PageView_URL = "http://stats.grok.se/json/en/";
	
	public static final String WIKIPEDIA_EVENT_PAGE_NAME = "United_States_presidential_election,_2012";
	
	
	/**
	 * Database related constants.
	 */
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String TIMELINE_CONNECTION = "jdbc:mysql://127.0.0.1:3306/Timeline?user=root&password=admin";
	public static final String WIKI_TABLE = "tl_wikipedia";
	public static final String EVENT_TABLE = "tl_event";
	public static final String EVENT_STARTDATE = "start_date";
	public static final String EVENT_ENDDATE = "end_date";
	public static final String ANNOTATED_TWEET_TABLE_NAME = "tl_annotated_tweet";
	public static final String DBPEDIA_SPOTLIGHT_ENTITY_TABLE_NAME = "tl_spotlight_entity";
	
	
	public static final int BATCH_COUNT = 100;
	
	/**
	 *  ASSUMPTION - WE GET THIS AS A COMMAND LINE ARGUMENT AND STORE IT HERE.
	 */
	public static final int EVENT_ID = 2;
	
	public static final String JSON_Property = "daily_views";
	public static final String YYYYMM = "YYYYMM";	
	public static final String YYYYMMDD ="yyyy-MM-dd"; 
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DECIMAL_FORMAT = "#.##";
	
		
	/**
	 * Used for Stopword Removal
	 */
	public static String[] SPLIT_CHARACTERS = { ",", ".", "(", ")", "{", "}", "[", "]", "?", "/", "\\", "!", "\"", ";", "-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "'", "+", "=", "%", "@", "#", "$", "^", "&", "*", ":", "<", ">", "|", "_", "`", "~" };
	public static String STOP_WORD_FILE_READ_LOCATION = "data/stopwordslist.txt";

	
	/**
	 * DBpedia Annotation Service Related Constants
	 */
	public static String SPOTLIGHT_API_URL = "http://spotlight.dbpedia.org/rest/annotate";
	public static final double CONFIDENCE = 0.2;
	public static final int SUPPORT = 20;
	
}