package org.knoesis.models;

import java.sql.Timestamp;
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
	private String tweetID;
	private Timestamp publishedDate;
	private String author;
	private double latitude;
	private double longitude;
	
	//twitter_id, tweet, published_date, twitter_author, latitude, longitude
	
	public AnnotatedTweet( String tweetText )
	{
		this.tweetText = tweetText;
		entities = new ArrayList<DBpediaSpotlightEntity>();
	}
	
	public AnnotatedTweet( String tweetText, String tweetId, Timestamp publishedDate, String author, double latitude, double longitude )
	{
		this.tweetText = tweetText;
		entities = new ArrayList<DBpediaSpotlightEntity>();
		this.tweetID = tweetId;
		this.publishedDate = publishedDate;
		this.author = author;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public String getTweetID() 
	{
		return tweetID;
	}

	public void setTweetID(String tweetID) 
	{
		this.tweetID = tweetID;
	}

	public Timestamp getPublishedDate() 
	{
		return publishedDate;
	}

	public void setPublishedDate(Timestamp publishedDate) 
	{
		this.publishedDate = publishedDate;
	}

	public String getAuthor() 
	{
		return author;
	}

	public void setAuthor(String author) 
	{
		this.author = author;
	}

	public double getLatitude() 
	{
		return latitude;
	}

	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}

	public double getLongitude() 
	{
		return longitude;
	}

	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}
	
	
	
}
