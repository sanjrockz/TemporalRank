package org.knoesis.temporalranking.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.knoesis.data.DBpediaEntity;
import org.knoesis.utils.WikipediaConstants;

public class DBPediaEntitySorter
{

	private List<DBpediaEntity> dbpediaEntities;

	public DBPediaEntitySorter()
	{
		this.dbpediaEntities = new ArrayList<DBpediaEntity>();
	}

	public void generateSortedEntityMapFiles()
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( WikipediaConstants.ALL_WIKIPEDIA_ENTITIES_FOR_SANDY_FILE ) );
			String line = null;
			String dataFields[] = null;
			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split( "\t" );
				this.dbpediaEntities.add( new DBpediaEntity( dataFields[1], dataFields[5] ) );
			}
			Collections.sort( this.dbpediaEntities );
			StringBuffer entityNameToUrlBuffer = new StringBuffer();
			StringBuffer sortedEntityNameBuffer = new StringBuffer();
			for ( DBpediaEntity topKEntity : this.dbpediaEntities )
			{
				sortedEntityNameBuffer.append( topKEntity.getEntityName() + "\r\n" );
				entityNameToUrlBuffer.append( topKEntity.getEntityName() + "\t" + topKEntity.getUrl() + "\r\n" );
			}

			File file = new File( "data/sandy/entity_names_to_url_map.txt" );
			FileWriter fw = new FileWriter( file.getAbsoluteFile() );
			BufferedWriter bw = new BufferedWriter( fw );
			bw.write( entityNameToUrlBuffer.toString() );
			bw.close();

			file = new File( "data/sandy/sorted_entity_names.txt" );
			fw = new FileWriter( file.getAbsoluteFile() );
			bw = new BufferedWriter( fw );
			bw.write( sortedEntityNameBuffer.toString() );
			bw.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	public static void main( String args[] )
	{
		DBPediaEntitySorter entitySorter = new DBPediaEntitySorter();
		entitySorter.generateSortedEntityMapFiles();
	}

}
