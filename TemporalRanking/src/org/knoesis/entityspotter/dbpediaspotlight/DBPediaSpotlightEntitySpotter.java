package org.knoesis.entityspotter.dbpediaspotlight;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
		spotter.spotEntities( "President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance.", "ID_1" );
	
	}
}
