package org.knoesis.temporalranking.newsapis.newyorktimes;

/**
 * This class contains constants used (such as API keys) to connect to third party New APIs.
 * 
 * @author sanjaya@knoesis.org
 *
 */
public class NewsSearchAPIConstants
{
	
	/**
	 *  NYTimes Related Constants...
	 */
	public static final String NYTIMES_ARTICLE_SEARCH_API_KEY = "77d701c6d35677f25434482a6fb985c7:17:67696300";
	public static final String NYTIMES_MOST_POPULAR_API_KEY = "f1cf2e998a27c697ef33c1f4a78d0a1f:14:67696300";
	public static final String NYTIMES_SEMANTIC_API_KEY = "95f83fa309a10f58499ba8fbc50846a2:8:67696300";
	public static final String NYTIMES_COMMUNITY_API_KEY = "7750984f6dc7a4bda758d9d576d36a57:19:67696300";

	public static final String NYTIMES_QUERY_GET_ALL_SANDY_ARTICLE_ABSTRACTS = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=hurricane+sandy";
	public static final String NYTIMES_DBCONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/Timeline?user=root&password=admin&useUnicode=true&characterEncoding=UTF-8";
}
