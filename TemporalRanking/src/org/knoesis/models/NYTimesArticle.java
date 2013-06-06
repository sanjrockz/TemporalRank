package org.knoesis.models;

import java.sql.Timestamp;

/**
 * This class models the fields that are found in a NYTimes Article.
 * 
 * @author sanjaya@knoesis.org
 */
public class NYTimesArticle
{
	private String articleId;
	private int eventId;
	private String webUrl;
	private String snippet;
	private String leadParagpraph;
	private String abstractParagraph;
	private String printPage;
	private String headline;
	private String keywords;
	private Timestamp createdTimestamp;
	private String section;

	public String getArticleId()
	{
		return articleId;
	}

	public void setArticleId( String articleId )
	{
		this.articleId = articleId;
	}

	public int getEventId()
	{
		return eventId;
	}

	public void setEventId( int eventId )
	{
		this.eventId = eventId;
	}

	public String getWebUrl()
	{
		return webUrl;
	}

	public void setWebUrl( String webUrl )
	{
		this.webUrl = webUrl;
	}

	public String getSnippet()
	{
		return snippet;
	}

	public void setSnippet( String snippet )
	{
		this.snippet = snippet;
	}

	public String getLeadParagpraph()
	{
		return leadParagpraph;
	}

	public void setLeadParagpraph( String leadParagpraph )
	{
		this.leadParagpraph = leadParagpraph;
	}

	public String getAbstractParagraph()
	{
		return abstractParagraph;
	}

	public void setAbstractParagraph( String abstractParagraph )
	{
		this.abstractParagraph = abstractParagraph;
	}

	public String getPrintPage()
	{
		return printPage;
	}

	public void setPrintPage( String printPage )
	{
		this.printPage = printPage;
	}

	public String getHeadline()
	{
		return headline;
	}

	public void setHeadline( String headline )
	{
		this.headline = headline;
	}

	public String getKeywords()
	{
		return keywords;
	}

	public void setKeywords( String keywords )
	{
		this.keywords = keywords;
	}

	public Timestamp getCreatedTimestamp()
	{
		return createdTimestamp;
	}

	public void setCreatedTimestamp( Timestamp createdTimestamp )
	{
		this.createdTimestamp = createdTimestamp;
	}

	public String getSection()
	{
		return section;
	}

	public void setSection( String section )
	{
		this.section = section;
	}

	public String getTextToAnnotate()
	{
		int snippetlength = 0;
		int leadParagraphLength = 0;
		int abstractLength = 0;

		String textToAnnotate = "";

		int maxLength = 0;

		if ( this.snippet != null )
		{
			snippetlength = this.snippet.length();
			textToAnnotate = this.snippet;
		}
		if ( this.leadParagpraph != null )
		{
			leadParagraphLength = this.leadParagpraph.length();
		}
		if ( this.abstractParagraph != null )
		{
			abstractLength = this.abstractParagraph.length();
		}

		maxLength = snippetlength;

		if ( maxLength < leadParagraphLength )
		{
			maxLength = leadParagraphLength;
			textToAnnotate = this.leadParagpraph;
		}

		if ( maxLength < abstractLength )
		{
			textToAnnotate = this.abstractParagraph;
		}

		if ( this.headline != null )
		{
			textToAnnotate = textToAnnotate + " " + this.headline;
		}

		if ( this.keywords != null )
		{
			textToAnnotate = textToAnnotate + this.keywords;
		}

		return textToAnnotate;
	}

}
