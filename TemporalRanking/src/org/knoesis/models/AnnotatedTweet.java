package org.knoesis.models;

import java.util.ArrayList;
import java.util.List;


/**
 * This class holds entities mentioned in a tweet.
 * 
 * @author sanjaya@knoesis.org
 *
 */
public class AnnotatedTweet
{

	private String tweetText;
	private List<DBpediaSpotlightEntity> entities;
	private int eventId;
	
	public AnnotatedTweet( String tweetText )
	{
		this.tweetText = tweetText;
		entities = new ArrayList<DBpediaSpotlightEntity>();
	}
	
	public AnnotatedTweet()
	{
		
	}
	
	public String getTweetText()
	{
		return tweetText;
	}
	public void setTweetText( String tweetText )
	{
		this.tweetText = tweetText;
	}
	public List<DBpediaSpotlightEntity> getEntities()
	{
		return entities;
	}
	public void setEntities( List<DBpediaSpotlightEntity> entities )
	{
		this.entities = entities;
	}
	
	public void addEntity( DBpediaSpotlightEntity entity )
	{
		this.entities.add( entity );
	}

	public int getEventId()
	{
		return eventId;
	}

	public void setEventId( int eventId )
	{
		this.eventId = eventId;
	}
	
}
