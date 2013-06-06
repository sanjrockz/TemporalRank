package org.knoesis.tempopralranking.evaluation.googletrends;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * This class contains methods to generate HTTP requests to Google Trends Web site and get the results it return.
 * 
 * @author sanjaya@knoesis.org
 */
public class GoogleTrendRanksExtractor
{
	private HashMap<String, HashMap<String, Integer>> googleTrendDataForEntity;

	public GoogleTrendRanksExtractor()
	{
		this.googleTrendDataForEntity = new HashMap<String, HashMap<String, Integer>>();
	}

	// url - http://www.google.com/trends/explore#q=Hurricane%20Sandy&date=10%2F2012%203m&cmpt=q&export=2

	public void readGoogleTrendURL( String encodedEntityName, String startDate )
	{
		InputStream inputReader = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		try
		{
			HttpClient client = new HttpClient();
			client.getParams().setParameter( "Email", "temporalranking" );
			client.getParams().setParameter( "Passwd", "knoesis123" );
			client.getParams().setParameter( "PersistentCookie", "yes" );
		
			client.getParams().setParameter( "Referrer", "https://www.google.com/accounts/ServiceLoginBoxAuth" );
			client.getParams().setParameter( "Content-type", "application/x-www-form-urlencoded" );
			client.getParams().setParameter( "User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21" );
			client.getParams().setParameter( "Accept", "text/plain" );

			GetMethod method = new GetMethod( "http://www.google.com/trends/fetchComponent?q=Hurricane%20Sandy&date=10%2F2012%203m&cid=TIMESERIES_GRAPH_0&export=3" );

			System.out.println( client.executeMethod( method ) );

			//if ( method.getStatusCode() == HttpStatus.SC_OK )
			{
				inputReader = method.getResponseBodyAsStream();
				String line = null;
				
				try
				{
					br = new BufferedReader( new InputStreamReader( inputReader ) );
					while ( ( line = br.readLine() ) != null )
					{
						//if ( line.startsWith( "google.visualization.Query.setResponse" ) )
						{
							sb.append( line + "\r\n" );
						}
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}
				finally
				{
					if ( br != null )
					{
						try
						{
							br.close();
						}
						catch ( Exception e )
						{
							e.printStackTrace();
						}
					}
				}
				
				System.out.println( sb.toString() );
				
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
	}

	public HashMap<String, HashMap<String, Integer>> getGoogleTrendDataForEntity()
	{
		return googleTrendDataForEntity;
	}

	public void setGoogleTrendDataForEntity( HashMap<String, HashMap<String, Integer>> googleTrendDataForEntity )
	{
		this.googleTrendDataForEntity = googleTrendDataForEntity;
	}

	public static void main( String args[] )
	{
		GoogleTrendRanksExtractor trendExtractor = new GoogleTrendRanksExtractor();
		trendExtractor.readGoogleTrendURL( "Hurrican Sandy", "10%2F2012%203m" );
	}

}
