package org.knoesis.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DBpediaURLGenerator
{
	public static void main( String args[] )
	{
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( WikipediaConstants.ALL_WIKIPEDIA_ENTITIES_FOR_SANDY_FILE ) );
			String line = null;
			int count = 1;
			String dataFields[] = null;
			String beforeDecode = "http://dbpedia.org/page/";
			StringBuffer buffer = new StringBuffer();
			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split( "\t" );
				beforeDecode = "http://dbpedia.org/page/";
				if ( dataFields.length < 6 )
				{
					beforeDecode = beforeDecode + dataFields[1].replaceAll( " ", "_" );
					try
					{
						URL url = new URL( beforeDecode );
						URLConnection connection = url.openConnection();
						BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
						String inputLine;
						while ( ( inputLine = in.readLine() ) != null )
						{
						}
						in.close();
						buffer.append( line + "\t" + beforeDecode + "\r\n" );
					}
					catch ( Exception e )
					{
						e.printStackTrace();
						buffer.append( line + "\t" + "::NULL::" + beforeDecode + "\r\n" );
					}
				}
				else
				{
					buffer.append( line + "\r\n" );
				}
			}
			File file = new File( "/home/sanjrockz/Desktop/wiki_entities_new.txt" );

			FileWriter fw = new FileWriter( file.getAbsoluteFile() );
			BufferedWriter bw = new BufferedWriter( fw );
			bw.write( buffer.toString() );
			bw.close();

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

}
