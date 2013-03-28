package org.knoesis.entityspotter.dbpediaspotlight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.knoesis.data.AnnotatedTweetDataManager;
import org.knoesis.data.DBpediaSpotlightEntityDataManager;
import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.DBpediaSpotlightEntity;
import org.knoesis.utils.WikipediaConstants;

/**
 * This class implements a simple entity spotter using DBpediaSpotlight tool over a HTTP connection.
 * 
 * @author sanjaya@knoesis.org
 */
public class DBPediaSpotlightEntitySpotter
{

	public List<DBpediaSpotlightEntity> spotEntities( String text, String parentId )
	{
		HttpClient client = null;
		GetMethod getMethod = null;
		List<DBpediaSpotlightEntity> spotlightEntitiesList = new ArrayList<DBpediaSpotlightEntity>();
		try
		{
			client = new HttpClient();
			getMethod = new GetMethod( WikipediaConstants.SPOTLIGHT_API_URL + "?confidence=" + WikipediaConstants.CONFIDENCE + "&support=" + WikipediaConstants.SUPPORT + "&text=" + URLEncoder.encode( text, "utf-8" ) );
			getMethod.addRequestHeader( new Header( "Accept", "application/json" ) );
			client.executeMethod( getMethod );
			if ( 200 == getMethod.getStatusCode() )
			{
				InputStream stream = getMethod.getResponseBodyAsStream();
				InputStreamReader is = new InputStreamReader( stream );
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader( is );
				String read = null;

				while ( ( read = br.readLine() ) != null )
				{
					sb.append( read );
				}

				JSONObject resultJSON = null;
				JSONArray entities = null;

				try
				{
					//System.out.println(sb.toString());
					resultJSON = new JSONObject( sb.toString() );
					entities = resultJSON.getJSONArray( "Resources" );
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
				}

				for ( int i = 0; i < entities.length(); i++ )
				{
					try
					{
						JSONObject entity = entities.getJSONObject( i );
						DBpediaSpotlightEntity spotlightEntity = new DBpediaSpotlightEntity();
						spotlightEntity.setTweetText( text );
						spotlightEntity.setParentId( parentId );
						spotlightEntity.setUrl( entity.getString( "@URI" ) );
						spotlightEntity.setSupport(  entity.getInt( "@support" )  );
						spotlightEntity.setTypes(  entity.getString( "@types" )  );
						spotlightEntity.setEntityName(  entity.getString( "@surfaceForm" )  );
						spotlightEntity.setOffset( entity.getInt( "@offset" ) );
						spotlightEntity.setSimilarityScore( entity.getDouble( "@similarityScore" ) );
						spotlightEntity.setPercentageOfSecondRank( entity.getDouble( "@percentageOfSecondRank" ) );
						spotlightEntitiesList.add( spotlightEntity );
					}
					catch ( JSONException e )
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				System.err.println( "Request Failed" );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			if ( getMethod != null )
			{
				getMethod.releaseConnection();
			}
		}
		return spotlightEntitiesList;
	}

	public static void main( String args[] )
	{
		DBPediaSpotlightEntitySpotter spotter = new DBPediaSpotlightEntitySpotter();
		//spotter.spotEntities( "President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance.", "ID_1" );
	
		AnnotatedTweet annotatedTweet = null;
		AnnotatedTweetDataManager annotatedTweetDataManager = new AnnotatedTweetDataManager();
		DBpediaSpotlightEntityDataManager spotlightEntityDataManager = new DBpediaSpotlightEntityDataManager();
		List<DBpediaSpotlightEntity> spotlightEntityList = null;
		int tweetKey = -1;
		String dbConnectionUrl = "jdbc:mysql://127.0.0.1:3306/Timeline?user=root&password=admin&useUnicode=true&characterEncoding=UTF-8";
		try
		{
//			Class.forName( WikipediaConstants.DRIVER );
//			connection = DriverManager.getConnection( "jdbc:mysql://130.108.5.96:3306/usElection2012?user=root&password=pranyd09!" );
//
//			String selectStatement = "select * from twitterdata where eventID=\'hurricaneSandy2012\'";
//			ps = connection.prepareStatement( selectStatement );
//			resultsSet = ps.executeQuery();
			
			BufferedReader reader = new BufferedReader( new FileReader( "/tmp/sandy_all.txt" ) );
			String line = null;
			int count = 1;
			String dataFields[] = null;
			Date date = null;
			java.util.Date date2 = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			
			Timestamp dateTime = null;
			
			java.util.Date min = sdf.parse("2012-10-15 23:59:59");
			java.util.Date max = sdf.parse("2013-03-20 23:59:59");;
			
			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split("\t");
				for( int i = 0; i < dataFields.length; i++)
				{
					System.out.println(dataFields[i]);
				}
				if( count > 190 )
				{
					try
					{
						date2 = sdf.parse(dataFields[2]);
						dateTime = new Timestamp(date2.getTime());
						
						if( date2.after(min) && date2.before(max) )
						{
							annotatedTweet = new AnnotatedTweet( dataFields[1], dataFields[0], dateTime, dataFields[3], Double.parseDouble(dataFields[4]), Double.parseDouble(dataFields[5]) );
												
							tweetKey = annotatedTweetDataManager.insert( dbConnectionUrl, annotatedTweet );
							
							spotlightEntityList = spotter.spotEntities( annotatedTweet.getTweetText(), (tweetKey + "") );
							
							for( DBpediaSpotlightEntity spotlightEntity: spotlightEntityList )
							{
								spotlightEntityDataManager.insert(dbConnectionUrl, spotlightEntity, tweetKey );
							}
						}
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}
				}
				count++;
			}
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
}
