package org.knoesis.temporalranking.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.knoesis.data.TopKEntity;

public class TopKEntityFinder
{

	private List<TopKEntity> topKEntityCounts;

	public TopKEntityFinder()
	{
		this.topKEntityCounts = new ArrayList<TopKEntity>();
	}

	public void findTopKEntities()
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( "data/sandy/topktweets.txt" ) );
			String line = null;
			String dataFields[] = null;
			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split( "\t" );
				topKEntityCounts.add( new TopKEntity( dataFields[0], Integer.parseInt( dataFields[1] ), 0.0, 0.0 ) );
			}
			Collections.sort( topKEntityCounts );
			StringBuffer buffer = new StringBuffer();
			for( TopKEntity topKEntity: topKEntityCounts )
			{
				buffer.append( topKEntity.getUrl() + "\t" + topKEntity.getAppearanceCount() + "\r\n" );
			}
			File file = new File("data/sandy/topktweets_sorted.txt");
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(buffer.toString());
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

	public static void main(String args[])
	{
		TopKEntityFinder entityFinder = new TopKEntityFinder();
		entityFinder.findTopKEntities();
	}
	
}
