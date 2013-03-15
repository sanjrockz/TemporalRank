package org.knoesis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class reads the SMART's stop word list saved in a local directory and builds a stop words map.
 * 
 * @author sanjaya@knoesis.org
 */
public class StopWordsRemover
{

	private HashMap<String, List<String>> stopWordsMap;

	public StopWordsRemover()
	{
		this.stopWordsMap = new HashMap<String, List<String>>();
		initializeStopWordsMap();
	}

	/**
	 * This method initializes stop words map.
	 */
	private void initializeStopWordsMap()
	{
		if ( this.stopWordsMap == null )
		{
			this.stopWordsMap = new HashMap<String, List<String>>();
		}
		this.stopWordsMap.put( "a", new ArrayList<String>() );
		this.stopWordsMap.put( "b", new ArrayList<String>() );
		this.stopWordsMap.put( "c", new ArrayList<String>() );
		this.stopWordsMap.put( "d", new ArrayList<String>() );
		this.stopWordsMap.put( "e", new ArrayList<String>() );
		this.stopWordsMap.put( "f", new ArrayList<String>() );
		this.stopWordsMap.put( "g", new ArrayList<String>() );
		this.stopWordsMap.put( "h", new ArrayList<String>() );
		this.stopWordsMap.put( "i", new ArrayList<String>() );
		this.stopWordsMap.put( "j", new ArrayList<String>() );
		this.stopWordsMap.put( "k", new ArrayList<String>() );
		this.stopWordsMap.put( "l", new ArrayList<String>() );
		this.stopWordsMap.put( "m", new ArrayList<String>() );
		this.stopWordsMap.put( "n", new ArrayList<String>() );
		this.stopWordsMap.put( "o", new ArrayList<String>() );
		this.stopWordsMap.put( "p", new ArrayList<String>() );
		this.stopWordsMap.put( "q", new ArrayList<String>() );
		this.stopWordsMap.put( "r", new ArrayList<String>() );
		this.stopWordsMap.put( "s", new ArrayList<String>() );
		this.stopWordsMap.put( "t", new ArrayList<String>() );
		this.stopWordsMap.put( "u", new ArrayList<String>() );
		this.stopWordsMap.put( "v", new ArrayList<String>() );
		this.stopWordsMap.put( "w", new ArrayList<String>() );
		this.stopWordsMap.put( "x", new ArrayList<String>() );
		this.stopWordsMap.put( "y", new ArrayList<String>() );
		this.stopWordsMap.put( "z", new ArrayList<String>() );
	}

	/**
	 * This method populates the stop words map when the file path to SMART's stop word list is given.
	 * 
	 * @param stopWordsFilePath
	 */
	public void populateStopWordsMap( String stopWordsFilePath )
	{
		if ( this.stopWordsMap == null )
		{
			this.stopWordsMap = new HashMap<String, List<String>>();
			initializeStopWordsMap();
		}

		BufferedReader stopWordsFileReader = null;
		String line = null;
		try
		{
			stopWordsFileReader = new BufferedReader( new FileReader( new File( stopWordsFilePath ) ) );
			while ( ( line = stopWordsFileReader.readLine() ) != null )
			{
				if ( !line.startsWith( "#" ) ) // remove the comments
				{
					if ( line.trim().length() > 1 )
					{
						if ( this.stopWordsMap.get( Character.toString( line.trim().charAt( 0 ) ) ) != null )
						{
							if ( line.trim().length() == 3 && line.trim().endsWith( "'s" ) ) // remove "'s" that comes with certain stop words such as
																								// a,c and t.
							{
								this.stopWordsMap.get( Character.toString( line.trim().charAt( 0 ) ) ).add( line.trim().substring( 0, ( line.trim().length() - 2 ) ) );
								// System.out.println( "Added word: " + line + " as " + line.trim().substring( 0 , (line.trim().length() - 2) ) +
								// " to Map key " + line.trim().charAt( 0 ) );
							}
							else
							{
								this.stopWordsMap.get( Character.toString( line.trim().charAt( 0 ) ) ).add( line );
								// System.out.println( "Added word: " + line + " to Map key " + line.trim().charAt( 0 ) );
							}
						}
					}
				}
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
				if ( stopWordsFileReader != null )
				{
					stopWordsFileReader.close();
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}


	
	public void removeStopWords( String textToRemoveStopWordsFrom )
	{
		removeStopWordsInMemory( WikipediaConstants.STOP_WORD_FILE_READ_LOCATION, textToRemoveStopWordsFrom );
	}
	
	
	/**
	 * This method removes stop words from the given string.
	 * 
	 * @param pathToStopWprdsFile
	 * @param textToRemoveStopWordsFrom
	 * @return
	 */
	public String removeStopWordsInMemory( String pathToStopWprdsFile, String textToRemoveStopWordsFrom )
	{
		populateStopWordsMap( pathToStopWprdsFile );
		StringBuilder stringWithOutStopWords = new StringBuilder();

		String[] words = null;
		String currentWord = null;
		List<String> indexedStopWordsListForGivenCharacter = null;
		boolean isNotAStopWord = true;
		String line = null;

		line = textToRemoveStopWordsFrom;
		for ( int x = 0; x < WikipediaConstants.SPLIT_CHARACTERS.length; x++ )
		{
			if ( line.contains( WikipediaConstants.SPLIT_CHARACTERS[x] ) )
			{
				line = line.replace( WikipediaConstants.SPLIT_CHARACTERS[x], " " );
			}
		}

		words = line.split( " " );
		if ( words != null )
		{
			for ( int j = 0; j < words.length; j++ ) // iterate within each word of the line and check whether it's a stop word.
			{
				currentWord = words[j].trim();
				if ( currentWord != null && !( currentWord.isEmpty() ) && !( currentWord.trim().equalsIgnoreCase( " " ) ) )
				{
					if ( Character.isDigit( currentWord.charAt( 0 ) ) ) // omit numbers in our check.
					{
						stringWithOutStopWords.append( currentWord + " " );
					}
					else
					{
						indexedStopWordsListForGivenCharacter = this.stopWordsMap.get( Character.toString( currentWord.charAt( 0 ) ) );
						if ( indexedStopWordsListForGivenCharacter != null ) // If stops words are available, remove them, else append the word to the string.
						{
							for ( String stopWord : indexedStopWordsListForGivenCharacter ) // Iterate within the stop word list to check if the current word is a stop word.
							{
								if ( ( currentWord.trim().equalsIgnoreCase( stopWord ) ) )
								{
									isNotAStopWord = false;
									break;
								}
							}
							if ( isNotAStopWord )
							{
								stringWithOutStopWords.append( currentWord + " " );
							}
						}
						else
						{
							stringWithOutStopWords.append( currentWord + " " );
						}
					}
				}
				isNotAStopWord = true;
			}
		}

		return stringWithOutStopWords.toString();
	}

	public HashMap<String, List<String>> getStopWordsMap()
	{
		return stopWordsMap;
	}

	public void setStopWordsMap( HashMap<String, List<String>> stopWordsMap )
	{
		this.stopWordsMap = stopWordsMap;
	}

}
