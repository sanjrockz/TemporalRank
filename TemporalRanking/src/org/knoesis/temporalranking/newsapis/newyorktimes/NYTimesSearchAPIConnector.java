package org.knoesis.temporalranking.newsapis.newyorktimes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.knoesis.data.NYTimesArticleDataManager;
import org.knoesis.models.NYTimesArticle;
import org.knoesis.utils.WikipediaConstants;

public class NYTimesSearchAPIConnector
{

	public JSONObject executeQuery( String queryURL, String apiKey, int resultOffset, String startDate )
	{
		URL nytimesURL = null;
		BufferedReader inputReader = null;
		JSONObject responseObject = null;
		try
		{
			nytimesURL = new URL( queryURL + "&begin_date=" + startDate + "&end_date=" + startDate + "&page=" + resultOffset + "&api-key=" + apiKey );
			inputReader = new BufferedReader( new InputStreamReader( nytimesURL.openStream() ) );
			// Thread.sleep( 1000 );
			String inputLine = null;
			while ( ( inputLine = inputReader.readLine() ) != null )
			{
				// System.out.println( inputLine );
				responseObject = new JSONObject( inputLine );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( inputReader != null )
				{
					inputReader.close();
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
		return responseObject;
	}

	public List<NYTimesArticle> decodeResults( JSONObject responseObject )
	{
		List<NYTimesArticle> newYorkTimesArticles = new ArrayList<NYTimesArticle>();
		try
		{
			JSONArray resultsArray = responseObject.getJSONObject( "response" ).getJSONArray( "docs" );
			if ( resultsArray != null )
			{
				SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				Date timeStamp = null;
				Timestamp dateTime = null;
				for ( int i = 0; i < resultsArray.length(); i++ )
				{
					JSONObject jsonObject = ( JSONObject ) resultsArray.get( i );

					NYTimesArticle newYorkTimesArticle = new NYTimesArticle();
					newYorkTimesArticle.setArticleId( jsonObject.getString( "_id" ) );
					newYorkTimesArticle.setEventId( WikipediaConstants.EVENT_ID );
					newYorkTimesArticle.setWebUrl( jsonObject.getString( "web_url" ) );
					newYorkTimesArticle.setSnippet( jsonObject.getString( "snippet" ) );
					newYorkTimesArticle.setLeadParagpraph( jsonObject.getString( "lead_paragraph" ) );
					newYorkTimesArticle.setAbstractParagraph( jsonObject.getString( "abstract" ) );
					newYorkTimesArticle.setPrintPage( jsonObject.getString( "print_page" ) );
					newYorkTimesArticle.setHeadline( jsonObject.getJSONObject( "headline" ).getString( "main" ) );
					JSONArray keywords = jsonObject.getJSONArray( "keywords" );
					String keywordsString = "";
					if ( keywords != null )
					{
						for ( int j = 0; j < keywords.length(); j++ )
						{
							keywordsString = keywordsString + ( ( JSONObject ) keywords.get( j ) ).getString( "value" );
						}
					}
					newYorkTimesArticle.setKeywords( keywordsString );
					String dateString = jsonObject.getString( "pub_date" ).split( "Z" )[0];
					String[] dateStrings = dateString.split( "T" );
					timeStamp = sdf.parse( dateStrings[0] + " " + dateStrings[1] );
					dateTime = new Timestamp( timeStamp.getTime() );
					newYorkTimesArticle.setCreatedTimestamp( dateTime );
					newYorkTimesArticle.setSection( jsonObject.getString( "section_name" ) );
					newYorkTimesArticles.add( newYorkTimesArticle );
				}

			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return newYorkTimesArticles;
	}

	public int getTotalHits( JSONObject responseObject )
	{
		try
		{
			JSONObject metaDataObject = responseObject.getJSONObject( "response" ).getJSONObject( "meta" );
			if ( metaDataObject != null )
			{
				return metaDataObject.getInt( "hits" );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return 0;
	}

	public List<NYTimesArticle> decodeAllResults( JSONObject responseObject, String startDate )
	{
		List<NYTimesArticle> allNewYorkTimesArticles = new ArrayList<NYTimesArticle>();
		try
		{
			int hits = ( getTotalHits( responseObject ) ) / 10;
			allNewYorkTimesArticles.addAll( decodeResults( responseObject ) );
			for ( int i = 1; i <= hits; i++ )
			{
				allNewYorkTimesArticles.addAll( decodeResults( executeQuery( NewsSearchAPIConstants.NYTIMES_QUERY_GET_ALL_SANDY_ARTICLE_ABSTRACTS, NewsSearchAPIConstants.NYTIMES_ARTICLE_SEARCH_API_KEY, i, startDate ) ) );
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return allNewYorkTimesArticles;
	}

	public HashMap<String, List<NYTimesArticle>> decodeAllResults( String fromDate, String toDate )
	{
		HashMap<String, List<NYTimesArticle>> allNewYorkTimesArticles = new HashMap<String, List<NYTimesArticle>>();
		;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
			Date startDate = sdf.parse( fromDate );
			Date endDate = sdf.parse( toDate );

			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime( startDate );

			while ( currentDate.getTime().before( endDate ) )
			{
				JSONObject responseObject = this.executeQuery( NewsSearchAPIConstants.NYTIMES_QUERY_GET_ALL_SANDY_ARTICLE_ABSTRACTS, NewsSearchAPIConstants.NYTIMES_ARTICLE_SEARCH_API_KEY, 0, sdf.format( currentDate.getTime() ) );
				List<NYTimesArticle> newYorkTimesArticles = this.decodeAllResults( responseObject, sdf.format( currentDate.getTime() ) );
				allNewYorkTimesArticles.put( sdf.format( currentDate.getTime() ), newYorkTimesArticles );
				System.out.println( "Processed " + newYorkTimesArticles.size() + " documents for " + sdf.format( currentDate.getTime() ) );
				currentDate.add( Calendar.DAY_OF_MONTH, 1 );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return allNewYorkTimesArticles;
	}

	public void annotateAndInsertRecords( HashMap<String, List<NYTimesArticle>> allNewYorkTimesArticles )
	{
		NYTimesArticleDataManager nytArticleDataManager = null;
		StringBuffer buffer = null;
		File file = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try
		{

			for ( Map.Entry<String, List<NYTimesArticle>> hashMapEntry : allNewYorkTimesArticles.entrySet() )
			{
				List<NYTimesArticle> articlesList = hashMapEntry.getValue();
				if ( articlesList != null )
				{
					file = new File( "data/sandy/nytimes/" + hashMapEntry.getKey() + ".txt" );
					fileWriter = new FileWriter( file.getAbsoluteFile() );
					bufferedWriter = new BufferedWriter( fileWriter );
					buffer = new StringBuffer();
					for ( NYTimesArticle currentArticle : articlesList )
					{
						//nytArticleDataManager = new NYTimesArticleDataManager();
						//nytArticleDataManager.insert( NewsSearchAPIConstants.NYTIMES_DBCONNECTION_URL, currentArticle );
						buffer.append( currentArticle.getArticleId() + "\t" + currentArticle.getTextToAnnotate() + "\r\n" );
					}
					bufferedWriter.write( buffer.toString() );
					bufferedWriter.close();
				}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static void main( String args[] )
	{
		NYTimesSearchAPIConnector nyTimesConnector = new NYTimesSearchAPIConnector();
		HashMap<String, List<NYTimesArticle>> allNewYorkTimesArticles = nyTimesConnector.decodeAllResults( "20121015", "20130331" );
		nyTimesConnector.annotateAndInsertRecords( allNewYorkTimesArticles );
	}

}
