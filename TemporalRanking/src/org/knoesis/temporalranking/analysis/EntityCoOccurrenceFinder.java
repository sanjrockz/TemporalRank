package org.knoesis.temporalranking.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.knoesis.utils.WikipediaConstants;


/**
 * This class takes entities seen in tweets for a given date and calculate rank for all entities present in wikipedia page for a given date.
 * 
 * @author sanjaya@knoesis.org
 *
 */
public class EntityCoOccurrenceFinder
{

	private HashMap<String, ArrayList<String>> wikipediaEntitiesAddedByDay;
	
	public EntityCoOccurrenceFinder()
	{
		this.wikipediaEntitiesAddedByDay = new HashMap<String, ArrayList<String>>();
	}
	
	public void generateEntityMapFile()
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( WikipediaConstants.ALL_WIKIPEDIA_ENTITIES_FOR_SANDY_FILE ) );
			String line = null;
			String dataFields[] = null;
			StringBuffer buffer = new StringBuffer();
			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split( "\t" );
				buffer.append( dataFields[1] + "\t" + dataFields[5] + "\r\n" );
			}
			File file = new File("data/sandy/wikipedia_entity_map.txt");
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(buffer.toString());
			bw.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main( String args[] ) throws Exception
	{
		//System.out.println( URLEncoder.encode( "Camag�ey", "UTF-8" ) );
		EntityCoOccurrenceFinder cf = new EntityCoOccurrenceFinder();
		cf.generateEntityMapFile();
	}
	
}
