package org.knoesis.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.knoesis.models.NYTimesArticle;
import org.knoesis.utils.WikipediaConstants;

public class NYTimesArticleDataManager
{

	private Connection connection = null;

	public void insert( String dbConnectionUrl, NYTimesArticle nytimesArticle )
	{
		PreparedStatement ps = null;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );

			String insertStatement = "INSERT INTO " + WikipediaConstants.NYTIMES_ARTICLE_TABLE_NAME + " (article_id, event_id, web_url, snippet, lead_paragraph, abstract, print_page, headline, keywords, datetime_created, section) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			int count = 0;
			ps = connection.prepareStatement( insertStatement, Statement.RETURN_GENERATED_KEYS );
			ps.setString( ++count, nytimesArticle.getArticleId() );
			ps.setInt( ++count, WikipediaConstants.EVENT_ID );
			ps.setString( ++count, nytimesArticle.getWebUrl() );
			ps.setString( ++count, nytimesArticle.getSnippet() );
			ps.setString( ++count, nytimesArticle.getLeadParagpraph() );
			ps.setString( ++count, nytimesArticle.getAbstractParagraph() );
			ps.setString( ++count, nytimesArticle.getPrintPage() );
			ps.setString( ++count, nytimesArticle.getHeadline() );
			ps.setString( ++count, nytimesArticle.getKeywords() );
			ps.setTimestamp( ++count, nytimesArticle.getCreatedTimestamp() );
			ps.setString( ++count, nytimesArticle.getSection() );
			ps.executeUpdate();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( ps != null )
				{
					ps.close();
				}
				if ( connection != null )
				{
					connection.close();
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	public NYTimesArticle load( String dbConnectionUrl, long rowId )
	{
		PreparedStatement ps = null;
		ResultSet resultsSet = null;
		NYTimesArticle nytimesArticle = null;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );

			String selectStatement = "SELECT * FROM " + WikipediaConstants.NYTIMES_ARTICLE_TABLE_NAME + " WHERE " + " id = " + rowId;
			ps = connection.prepareStatement( selectStatement );
			resultsSet = ps.executeQuery();

			while ( resultsSet.next() )
			{
				int count = 0;
				nytimesArticle = new NYTimesArticle();
				nytimesArticle.setArticleId( resultsSet.getString( ++count ) );
				nytimesArticle.setEventId( resultsSet.getInt( ++count ) );
				nytimesArticle.setWebUrl( resultsSet.getString( ++count ) );
				nytimesArticle.setSnippet( resultsSet.getString( ++count ) );
				nytimesArticle.setLeadParagpraph( resultsSet.getString( ++count ) );
				nytimesArticle.setAbstractParagraph( resultsSet.getString( ++count ) );
				nytimesArticle.setPrintPage( resultsSet.getString( ++count ) );
				nytimesArticle.setHeadline( resultsSet.getString( ++count ) );
				nytimesArticle.setKeywords( resultsSet.getString( ++count ) );
				nytimesArticle.setCreatedTimestamp( resultsSet.getTimestamp( ++count ) );
				nytimesArticle.setSection( resultsSet.getString( ++count ) );

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
				if ( resultsSet != null )
				{
					resultsSet.close();
				}
				if ( ps != null )
				{
					ps.close();
				}
				if ( connection != null )
				{
					connection.close();
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
		return nytimesArticle;
	}

}
