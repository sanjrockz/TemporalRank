package org.knoesis.models;

/**
 * This class holds data returned by DBpedia Spotlight Annotation Service.
 * 
 * @author sanjaya@knoesis.org
 */
public class DBpediaSpotlightEntity
{

	private String tweetText;
	private String url;
	private double support;
	private String types;
	private String entityName;
	private int offset;
	private double similarityScore;
	private double percentageOfSecondRank;
	private String parentId;
	private int eventId;
	
	public String getTweetText()
	{
		return tweetText;
	}

	public void setTweetText( String tweetText )
	{
		this.tweetText = tweetText;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public double getSupport()
	{
		return support;
	}

	public void setSupport( double support )
	{
		this.support = support;
	}

	public String getTypes()
	{
		return types;
	}

	public void setTypes( String types )
	{
		this.types = types;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName( String entityName )
	{
		this.entityName = entityName;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset( int offset )
	{
		this.offset = offset;
	}

	public double getSimilarityScore()
	{
		return similarityScore;
	}

	public void setSimilarityScore( double similarityScore )
	{
		this.similarityScore = similarityScore;
	}

	public double getPercentageOfSecondRank()
	{
		return percentageOfSecondRank;
	}

	public void setPercentageOfSecondRank( double percentageOfSecondRank )
	{
		this.percentageOfSecondRank = percentageOfSecondRank;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId( String parentId )
	{
		this.parentId = parentId;
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
