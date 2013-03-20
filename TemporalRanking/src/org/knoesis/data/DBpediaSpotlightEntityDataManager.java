package org.knoesis.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.knoesis.models.DBpediaSpotlightEntity;
import org.knoesis.utils.WikipediaConstants;


/**
 * This class is a Database Handler for DBpediaSpotlighEntity object saving and retrieval.
 * 
 * @author sanjaya@knoesis.org
 *
 */
public class DBpediaSpotlightEntityDataManager
{
	private Connection connection = null;

	/**
	 * This method inserts DBpediaSpotlightEntity object into the database.
	 * 
	 * @param dbConnectionUrl
	 * @param entity
	 * @param key
	 */
	public void insert( String dbConnectionUrl, DBpediaSpotlightEntity entity, int key )
	{
		PreparedStatement ps = null;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );

			String insertStatement = "INSERT INTO " + WikipediaConstants.DBPEDIA_SPOTLIGHT_ENTITY_TABLE_NAME + " (url, support, types, entity_name, offset, similarity_score, percentage_of_second_rank, parent_id, event_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			int count = 0;
			ps = connection.prepareStatement( insertStatement );
			ps.setString( ++count, entity.getUrl() );
			ps.setDouble( ++count, entity.getSupport() );
			ps.setString( ++count, entity.getTypes() );
			ps.setString( ++count, entity.getEntityName() );
			ps.setInt( ++count, entity.getOffset() );
			ps.setDouble( ++count, entity.getSimilarityScore() );
			ps.setDouble( ++count, entity.getPercentageOfSecondRank() );
			ps.setString( ++count, entity.getParentId() );
			ps.setInt( ++count, WikipediaConstants.EVENT_ID );
			ps.execute();

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

	
	/**
	 * This method retrieves a DBpediaSpotlightEntity from database.
	 * 
	 * @param dbConnectionUrl
	 * @param rowId
	 * @return
	 */
	public DBpediaSpotlightEntity load( String dbConnectionUrl, long rowId )
	{
		PreparedStatement ps = null;
		ResultSet resultsSet = null;
		PreparedStatement ps2 = null;
		ResultSet resultsSet2 = null;
		DBpediaSpotlightEntity spotlightEntity = null;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );
			
			String selectStatement = "SELECT * FROM " + WikipediaConstants.DBPEDIA_SPOTLIGHT_ENTITY_TABLE_NAME + " WHERE " + " id = " + rowId;
			ps = connection.prepareStatement( selectStatement );
			resultsSet = ps.executeQuery();

			while ( resultsSet.next() )
			{
				int count = 0;
				spotlightEntity = new DBpediaSpotlightEntity();
				spotlightEntity.setUrl( resultsSet.getString( ++count ) );
				spotlightEntity.setSupport( resultsSet.getDouble( ++count ) );
				spotlightEntity.setTypes( resultsSet.getString( ++count ) );
				spotlightEntity.setEntityName( resultsSet.getString( ++count ) );
				spotlightEntity.setOffset( resultsSet.getInt( ++count ) );
				spotlightEntity.setSimilarityScore( resultsSet.getDouble( ++count ) );
				spotlightEntity.setPercentageOfSecondRank( resultsSet.getDouble( ++count ) );
				spotlightEntity.setParentId( resultsSet.getString( ++count ) );
				spotlightEntity.setEventId( resultsSet.getInt( ++count  ) );
			}
			
			String selectAnootatedTweetKey = "SELECT * FROM " + WikipediaConstants.ANNOTATED_TWEET_TABLE_NAME + " WHERE " + " id = " + spotlightEntity.getParentId();
			ps2 = connection.prepareStatement( selectAnootatedTweetKey );
			resultsSet2 = ps2.executeQuery();
			
			while( resultsSet2.next() )
			{
				spotlightEntity.setTweetText( resultsSet2.getString( 1 ) );
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
				if ( resultsSet2 != null )
				{
					resultsSet2.close();
				}
				if ( ps2 != null )
				{
					ps2.close();
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
		return spotlightEntity;
	}

	
	
	/**
	 * This method retrieves a list of DBpediaSpotlightEntity objects from database.
	 * 
	 * @param dbConnectionUrl
	 * @param key
	 * @return
	 */
	public List<DBpediaSpotlightEntity> loadAllEntities( String dbConnectionUrl, long key )
	{
		PreparedStatement ps = null;
		ResultSet resultsSet = null;
		PreparedStatement ps2 = null;
		ResultSet resultsSet2 = null;
		List<DBpediaSpotlightEntity> spotlightEntityList = new ArrayList<DBpediaSpotlightEntity>();
		DBpediaSpotlightEntity spotlightEntity = null; 
		String tweetText = null;
		try
		{
			Class.forName( WikipediaConstants.DRIVER );
			connection = DriverManager.getConnection( dbConnectionUrl );

			String selectAnootatedTweetKey = "SELECT * FROM " + WikipediaConstants.ANNOTATED_TWEET_TABLE_NAME + " WHERE " + " id = " + key;
			ps = connection.prepareStatement( selectAnootatedTweetKey );
			resultsSet = ps.executeQuery();
			
			while( resultsSet.next() )
			{
				tweetText = resultsSet.getString( 1 );
			}
			
			String selectStatement = "SELECT * FROM " + WikipediaConstants.DBPEDIA_SPOTLIGHT_ENTITY_TABLE_NAME + " WHERE " + " id = " + key;
			ps2 = connection.prepareStatement( selectStatement );
			resultsSet2 = ps2.executeQuery();
			
			while ( resultsSet2.next() )
			{
				int count = 0;
				spotlightEntity = new DBpediaSpotlightEntity();
				spotlightEntity.setUrl( resultsSet2.getString( ++count ) );
				spotlightEntity.setSupport( resultsSet2.getDouble( ++count ) );
				spotlightEntity.setTypes( resultsSet2.getString( ++count ) );
				spotlightEntity.setEntityName( resultsSet2.getString( ++count ) );
				spotlightEntity.setOffset( resultsSet2.getInt( ++count ) );
				spotlightEntity.setSimilarityScore( resultsSet2.getDouble( ++count ) );
				spotlightEntity.setPercentageOfSecondRank( resultsSet2.getDouble( ++count ) );
				spotlightEntity.setParentId( resultsSet2.getString( ++count ) );
				spotlightEntity.setEventId( resultsSet2.getInt( ++count  ) );
				spotlightEntity.setTweetText( tweetText );
				spotlightEntityList.add( spotlightEntity );
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
				if ( resultsSet2 != null )
				{
					resultsSet2.close();
				}
				if ( ps2 != null )
				{
					ps2.close();
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
		return spotlightEntityList;
	}
		
}
