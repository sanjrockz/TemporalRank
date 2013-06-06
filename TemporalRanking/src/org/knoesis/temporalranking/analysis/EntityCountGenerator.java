package org.knoesis.temporalranking.analysis;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.knoesis.data.TopKEntity;
import org.knoesis.data.TrendingEntity;

public class EntityCountGenerator
{

	private HashMap<String, Integer> entityCount = null;
	private List<TopKEntity> topKEntityCounts;

	public void initializeEntityList()
	{
		entityCount = new HashMap<String, Integer>();
		BufferedReader reader = null;
		String line = null;
		String dataFields[] = null;
		try
		{
			reader = new BufferedReader( new FileReader( "data/sandy/wikipedia_entity_map.txt" ) );

			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split( "\t" );
				entityCount.put( dataFields[1], 0 );
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
				reader.close();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	public void resetEntityCounts()
	{
		for ( Map.Entry<String, Integer> entry : entityCount.entrySet() )
		{
			String key = entry.getKey();
			entityCount.put( key, 0 );
		}
	}

	public void generateAndSaveTopKEntityCounts()
	{
		String path = "data/sandy/nytimes/entities/";

		File folder = new File( path );
		File[] listOfDirectories = folder.listFiles();
		File[] listOfFiles = null;

		BufferedReader reader = null;
		String line = null;
		String dataFields[] = null;
		int currentCount = 0;
		HashSet<String> tweetIds = new HashSet<String>();

		HashSet<String> uniqueEntitiesWithTrend = new HashSet<String>();
		
		for ( int i = 0; i < listOfDirectories.length; i++ )
		{
			if ( listOfDirectories[i].isDirectory() )
			{
				initializeEntityList();
				tweetIds = new HashSet<String>();
				listOfFiles = listOfDirectories[i].listFiles();
				for ( int j = 0; j < listOfFiles.length; j++ )
				{
					if ( !( listOfFiles[j].getName().equalsIgnoreCase( "top-k-ranks" ) ) )
					{
						try
						{
							reader = new BufferedReader( new FileReader( listOfFiles[j] ) );
							while ( ( line = reader.readLine() ) != null )
							{
								dataFields = line.split( "\t" );
								if( dataFields.length > 1 )
								{
									currentCount = entityCount.get( dataFields[1] );
									entityCount.put( dataFields[1], ++currentCount );
								}
								tweetIds.add( dataFields[0] );
							}
						}
						catch ( Exception e )
						{
							e.printStackTrace();
						}
						finally
						{

						}
					}
				}

				topKEntityCounts = new ArrayList<TopKEntity>();
				for ( Map.Entry<String, Integer> entry : entityCount.entrySet() )
				{
					double devider = ( double ) tweetIds.size();
					if( devider == 0.0 )
					{
						devider = 1.0;
					}
					topKEntityCounts.add( new TopKEntity( entry.getKey(), entry.getValue(), ( ( double ) entry.getValue() / devider ), 0.0 ) );
				}

				Collections.sort( topKEntityCounts );
				StringBuffer buffer = new StringBuffer();
				for ( TopKEntity topKEntity : topKEntityCounts )
				{
					buffer.append( topKEntity.getUrl() + "\t" + topKEntity.getAppearanceCount() + "\t" + topKEntity.getEntityToTotalTweetsRatio() + "\r\n" );
					if( topKEntity.getAppearanceCount() > 0 )
					{
						uniqueEntitiesWithTrend.add( URLEncoder.encode( topKEntity.getUrl() ) );
					}
				}

				
				
				try
				{
					File file = new File( listOfDirectories[i].getAbsolutePath() + "/top-k-ranks" );
					if ( !file.exists() )
					{
						file.mkdir();
					}
					file = new File( file.getAbsolutePath() + "/topktweets_sorted.txt" );
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

		
		try
		{
			File file = new File( "data/sandy/nytimes/urls_with_trends.txt" );
			FileWriter fw = new FileWriter( file.getAbsoluteFile() );
			BufferedWriter bw = new BufferedWriter( fw );
			StringBuffer buffer = new StringBuffer();
			for( String url : uniqueEntitiesWithTrend )
			{
				buffer.append( url + "\r\n" );
			}
			
			bw.write( buffer.toString() );
			bw.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		
	}

	public void calculatePercentageIncreaseOfEntityAppearance()
	{

		String path = "data/sandy/nytimes/entities/";

		File folder = new File( path );
		File[] listOfDirectories = folder.listFiles();

		java.util.Arrays.sort(listOfDirectories, new Comparator<File>()
		{
			 public int compare(File f1, File f2)
			    {
			        return String.valueOf(f1.getName()).compareTo(f2.getName());
			    } 
	    });
		
		File[] listOfFiles = null;
		File[] topKRanksFile = null;

		BufferedReader reader = null;
		String line = null;
		String dataFields[] = null;
		int currentCount = 0;
		HashMap<String, Double> trendingURLs = new HashMap<String, Double>();
		List<TrendingEntity> trendingEntityList = new ArrayList<TrendingEntity>();

		HashMap<String, TopKEntity> topKListPrevious = new HashMap<String, TopKEntity>();
		HashMap<String, TopKEntity> topKListCurrent = new HashMap<String, TopKEntity>();

		for ( int i = 0; i < listOfDirectories.length; i++ )
		{
			if ( listOfDirectories[i].isDirectory() )
			{
				initializeEntityList();

				listOfFiles = listOfDirectories[i].listFiles();
				for ( int j = 0; j < listOfFiles.length; j++ )
				{
					if ( ( listOfFiles[j].getName().equalsIgnoreCase( "top-k-ranks" ) ) )
					{
						try
						{
							trendingURLs = new HashMap<String, Double>();
							trendingEntityList = new ArrayList<TrendingEntity>();
							topKRanksFile = listOfFiles[j].listFiles();
							for ( int k = 0; k < topKRanksFile.length; k++ )
							{
								if ( !topKRanksFile[k].getName().equalsIgnoreCase( "trends_sorted.txt" ) )
								{
									reader = new BufferedReader( new FileReader( topKRanksFile[k] ) );
									while ( ( line = reader.readLine() ) != null )
									{
										dataFields = line.split( "\t" );
										if ( Integer.parseInt( dataFields[1] ) > 0 )
										{
											if( dataFields.length > 2 )
											{
												topKListCurrent.put( dataFields[0], new TopKEntity( dataFields[0], Integer.parseInt( dataFields[1] ), Double.parseDouble( dataFields[2] ), 0.0 ) );
											}
										}
									}

									if ( j == 0 ) // This is the first file, so nothing to compare here...
									{
										for ( Map.Entry<String, TopKEntity> entry : topKListCurrent.entrySet() )
										{
											if ( entry.getValue().getAppearanceCount() > 0 )
											{
												TopKEntity currentEntity = entry.getValue();
												trendingURLs.put( currentEntity.getUrl(), currentEntity.getEntityToTotalTweetsRatio() );
												trendingEntityList.add( new TrendingEntity( currentEntity.getUrl(), currentEntity.getEntityToTotalTweetsRatio() ) );
												currentEntity.setTrend( currentEntity.getEntityToTotalTweetsRatio() );
												topKListCurrent.put( entry.getKey(), currentEntity );
											}
										}
									}
									else
									{
										for ( Map.Entry<String, TopKEntity> entry : topKListCurrent.entrySet() )
										{
											if ( entry.getValue().getAppearanceCount() > 0 )
											{
												TopKEntity previousEntity = topKListPrevious.get( entry.getKey() );
												TopKEntity currentEntity = entry.getValue();
												if ( previousEntity == null ) // we have a new trending entity, add it
												{
													trendingURLs.put( currentEntity.getUrl(), currentEntity.getEntityToTotalTweetsRatio() );
													trendingEntityList.add( new TrendingEntity( currentEntity.getUrl(), currentEntity.getEntityToTotalTweetsRatio() ) );
													currentEntity.setTrend( currentEntity.getEntityToTotalTweetsRatio() );
													topKListCurrent.put( entry.getKey(), currentEntity );
												}
												else
												{
													/*if( previousEntity.getTrend() < 0 )
													{
														
													}
													else
													{
														
													}*/
													trendingURLs.put( currentEntity.getUrl(), ( entry.getValue().getEntityToTotalTweetsRatio() - previousEntity.getEntityToTotalTweetsRatio() ) );
													trendingEntityList.add( new TrendingEntity( entry.getValue().getUrl(), ( entry.getValue().getEntityToTotalTweetsRatio() - previousEntity.getEntityToTotalTweetsRatio() ) ) );
													currentEntity.setTrend(  ( entry.getValue().getEntityToTotalTweetsRatio() - previousEntity.getEntityToTotalTweetsRatio() ) );
													topKListCurrent.put( entry.getKey(), currentEntity );
												}
											}
										}
										
										/*for ( Map.Entry<String, TopKEntity> entry : topKListPrevious.entrySet() )
										{
											if ( entry.getValue().getAppearanceCount() > 0 )
											{
												Double trend = trendingURLs.get( entry.getKey() );
												if ( trend == null ) // If this is not in the currrentTrends but was in the previous trends, the trend is decreasing.
												{
													trendingURLs.put( entry.getValue().getUrl(), entry.getValue().getEntityToTotalTweetsRatio() );
													trendingEntityList.add( new TrendingEntity( entry.getValue().getUrl(), ( 0 - entry.getValue().getEntityToTotalTweetsRatio() ) ) );
												}
											}
										}*/
									}
									topKListPrevious = new HashMap<String, TopKEntity>();
									topKListPrevious.putAll( topKListCurrent );
									topKListCurrent = new HashMap<String, TopKEntity>();
								}
							}

							Collections.sort( trendingEntityList );
							StringBuffer buffer = new StringBuffer();
							for ( TrendingEntity trendingEntity : trendingEntityList )
							{
								buffer.append( trendingEntity.getUrl() + "\t" + trendingEntity.getTrend() + "\r\n" );
							}

							try
							{
								File file = new File( listOfDirectories[i].getAbsolutePath() + "/top-k-ranks" );
								if ( !file.exists() )
								{
									file.mkdir();
								}
								file = new File( file.getAbsolutePath() + "/trends_sorted.txt" );
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
						catch ( Exception e )
						{
							e.printStackTrace();
						}
						finally
						{

						}
					}
				}

			}
		}

	}

	public static void main( String args[] )
	{
		EntityCountGenerator ecg = new EntityCountGenerator();
//		ecg.generateAndSaveTopKEntityCounts();
		ecg.calculatePercentageIncreaseOfEntityAppearance();
	}

}
