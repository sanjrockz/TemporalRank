package org.knoesis.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.DBpediaSpotlightEntity;
import org.knoesis.utils.WikipediaConstants;

/**
 * This class is a Database Handler for AnnotatedTweet object saving and retrieval.
 * 
 * @author sanjaya@knoesis.org
 *
 */
public class AnnotatedTweetDataManager
{

	private Connection connection = null;
	
	/**
	 * This method inserts AnnotatedTweet object into the database. The integer value returned is used to identify 
	 * DBpediaSpotlight Entities attached to this AnnotataedTweet object.
	 * 
	 * @param dbConnectionUrl
	 * @param tweet
	 * @return
	 */
	public int insert( String dbConnectionUrl, AnnotatedTweet tweet )
	{
		PreparedStatement ps = null;
		int key = -1;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );

			String insertStatement = "INSERT INTO " + WikipediaConstants.ANNOTATED_TWEET_TABLE_NAME + " (tweet_text, event_id) VALUES (?, ?)";
			int count = 0;
			ps = connection.prepareStatement( insertStatement, Statement.RETURN_GENERATED_KEYS );
			ps.setString( ++count, tweet.getTweetText() );
			ps.setInt( ++count, WikipediaConstants.EVENT_ID );
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();    
			keys.next();  
			key = keys.getInt(1);
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
		return key;
	}

	
	/**
	 * This method retrieves a AnnotatedTweet from database.
	 * 
	 * @param dbConnectionUrl
	 * @param rowId
	 * @return
	 */
	public AnnotatedTweet load( String dbConnectionUrl, long rowId )
	{
		PreparedStatement ps = null;
		ResultSet resultsSet = null;
		AnnotatedTweet tweet = null;
		DBpediaSpotlightEntityDataManager spotlightEntityDataManager = new DBpediaSpotlightEntityDataManager();
		List<DBpediaSpotlightEntity> spotlightEntity = null;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );

			String selectStatement = "SELECT * FROM " + WikipediaConstants.ANNOTATED_TWEET_TABLE_NAME + " WHERE " + " id = " + rowId;
			ps = connection.prepareStatement( selectStatement );
			resultsSet = ps.executeQuery();

			while ( resultsSet.next() )
			{
				int count = 0;
				tweet = new AnnotatedTweet();
				tweet.setTweetText( resultsSet.getString( ++count ) );
				tweet.setEventId( WikipediaConstants.EVENT_ID );
				spotlightEntity = spotlightEntityDataManager.loadAllEntities( dbConnectionUrl, rowId );
				tweet.setEntities( spotlightEntity );
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
		return tweet;
	}

	
	
}
