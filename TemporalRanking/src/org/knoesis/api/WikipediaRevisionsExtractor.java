package org.knoesis.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.knoesis.models.WikiRevision;
import org.knoesis.utils.WikipediaConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class contains methods to query Wikipedia revisions and extract text segments added on a given date.
 * 
 * @author sanjaya@knoesis.org
 */
public class WikipediaRevisionsExtractor
{

	private static HashMap<String, HashMap<Integer, WikiRevision>> wikiRevisionsMap;
	private static String getRevisionIDsQuery = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&format=json&rvprop=timestamp|ids&rvlimit=500&titles=";
	private static String getRevisionTextQuery = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&format=json&rvprop=content&rvlimit=1&rvstartid=539326896&rvdiffto=prev&titles=";
	private static URL wikipediaApiConnector;
	private static String jsonText;
	private String contentWithEntityMentions = "";

	public void initialize()
	{
		wikiRevisionsMap = new HashMap<String, HashMap<Integer, WikiRevision>>();
	}

	/**
	 * This method takes a Wikipedia Event Page Name and Populate the first 500 revisions.
	 * 
	 * @param eventPageName
	 */
	public void populateRevisionsMap( String eventPageName )
	{
		initialize();
		getRevisionIDsQuery = getRevisionIDsQuery + eventPageName;
		System.out.println(getRevisionIDsQuery);
		JSONObject responseObject = new JSONObject();
		try
		{
			wikipediaApiConnector = new URL( getRevisionIDsQuery );
			HttpURLConnection urlConnection = ( HttpURLConnection ) wikipediaApiConnector.openConnection();
			urlConnection.connect();
			BufferedReader reader = new BufferedReader( new InputStreamReader( urlConnection.getInputStream() ) );
			String line = null;
			JSONArray resultsArray = null;
			String dateString = null;
			JSONObject jsonObject = null;
			WikiRevision wikiRevision = null;
			HashMap<Integer, WikiRevision> wikiRevisionsList = null;
			String dateKey = null;
			while ( ( line = reader.readLine() ) != null )
			{
				responseObject = new JSONObject( line );
				// TODO How to find the pageid of the page?
				resultsArray = responseObject.getJSONObject( "query" ).getJSONObject( "pages" ).getJSONObject( "20102947" ).getJSONArray( "revisions" );
				if ( resultsArray != null )
				{
					for ( int i = 0; i < resultsArray.length(); i++ )
					{
						jsonObject = ( JSONObject ) resultsArray.get( i );
						wikiRevision = new WikiRevision();
						wikiRevision.setId( jsonObject.getLong( "revid" ) );
						wikiRevision.setParentRevisionId( jsonObject.getLong( "parentid" ) );
						dateString = jsonObject.getString( "timestamp" );
						DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
						wikiRevision.setDate( ( Date ) formatter.parse( dateString ) );
						dateKey = wikiRevision.getDate().getYear() + "-" + wikiRevision.getDate().getMonth() + "-" + wikiRevision.getDate().getDate();
						wikiRevisionsList = wikiRevisionsMap.get( dateKey );
						if ( wikiRevisionsList == null )
						{
							wikiRevisionsList = new HashMap<Integer, WikiRevision>();
						}
						wikiRevisionsList.put( Integer.valueOf( wikiRevisionsList.size() ), wikiRevision );
						wikiRevisionsMap.put( dateKey, wikiRevisionsList );
					}

				}
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method takes a Wikipedia Event Page name as an argument, retrieves its revision information and returns it.
	 * 
	 * @param eventPageName
	 * @return
	 */
	public String getRevisionText( String eventPageName )
	{
		initialize();
		getRevisionTextQuery = getRevisionTextQuery + eventPageName;
		JSONObject responseObject = new JSONObject();
		String returnString = "";
		try
		{
			wikipediaApiConnector = new URL( getRevisionTextQuery );
			HttpURLConnection urlConnection = ( HttpURLConnection ) wikipediaApiConnector.openConnection();
			urlConnection.connect();
			BufferedReader reader = new BufferedReader( new InputStreamReader( urlConnection.getInputStream() ) );
			String line = null;
			JSONArray resultsArray = null;
			JSONObject jsonObject = null;
			while ( ( line = reader.readLine() ) != null )
			{
				responseObject = new JSONObject( line );
				System.out.println(responseObject);
				resultsArray = responseObject.getJSONObject( "query" ).getJSONObject( "pages" ).getJSONObject( "20102947" ).getJSONArray( "revisions" );
				if ( resultsArray != null )
				{
					for ( int i = 0; i < resultsArray.length(); i++ )
					{
						jsonObject = ( JSONObject ) resultsArray.get( i );
						returnString = returnString + jsonObject.getJSONObject( "diff" ).getString( "*" );
					}
				}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		jsonText = returnString;
		return returnString;
	}

	/**
	 * This method takes Wikipedia revision text and an Entity Name we are interested in searching in the revision text as inputs and returns all text
	 * segments in the revision text which contains the entity name.
	 * 
	 * @param revisionText
	 * @param entityName
	 * @return
	 */
	public String getRevisionContentWithEntityMentions( String revisionText, String entityName )
	{
		parseRevisionText( revisionText, entityName );
		return contentWithEntityMentions;
	}

	/**
	 * This method takes Wikipedia revision text and an Entity Name we are interested in searching in the revision text as inputs and stores text
	 * fractions in the revision text which contains entity name.
	 * 
	 * @param revisionText
	 * @param entityName
	 */
	public void parseRevisionText( String revisionText, String entityName )
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating( false );
			dbf.setNamespaceAware( true );
			dbf.setIgnoringComments( false );
			dbf.setIgnoringElementContentWhitespace( false );
			dbf.setExpandEntityReferences( false );
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document htmlDomTree = db.parse( new InputSource( new StringReader( "<html>" + revisionText + "</html>" ) ) );
			listNodes( htmlDomTree.getDocumentElement(), entityName );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method takes an XML document built based on the revision text and searches for the given entity name in the text elements of the XML
	 * document.
	 * 
	 * @param node
	 * @param entityName
	 */
	public void listNodes( Node node, String entityName )
	{
		NodeList list = node.getChildNodes();
		Node currentNode = null;
		Node attributeNode = null;
		Node parentOfTextNode = null;
		NamedNodeMap attributesOfCurrentNode = null;
		if ( list.getLength() > 0 )
		{
			for ( int i = 0; i < list.getLength(); i++ )
			{
				currentNode = list.item( i );
				attributesOfCurrentNode = currentNode.getAttributes();
				if ( attributesOfCurrentNode != null )
				{
					for ( int j = 0; j < attributesOfCurrentNode.getLength(); j++ )
					{
						attributeNode = attributesOfCurrentNode.item( j );
						if ( attributeNode != null && attributeNode.getNodeValue().equalsIgnoreCase( "diff-addedline" ) )
						{
							parentOfTextNode = ( ( Attr ) attributeNode ).getOwnerElement();
							if ( parentOfTextNode.getTextContent() != null && parentOfTextNode.getTextContent().contains( entityName ) )
							{
								contentWithEntityMentions = contentWithEntityMentions + "\r\n" + parentOfTextNode.getTextContent();
							}
						}
					}
					attributesOfCurrentNode = null;
				}
				listNodes( currentNode, entityName );
			}
		}
	}

	public static HashMap<String, HashMap<Integer, WikiRevision>> getWikiRevisionsMap()
	{
		return wikiRevisionsMap;
	}

	public static void setWikiRevisionsMap( HashMap<String, HashMap<Integer, WikiRevision>> wikiRevisionsMap )
	{
		WikipediaRevisionsExtractor.wikiRevisionsMap = wikiRevisionsMap;
	}

	public static String getGetRevisionIDsQuery()
	{
		return getRevisionIDsQuery;
	}

	public static void setGetRevisionIDsQuery( String getRevisionIDsQuery )
	{
		WikipediaRevisionsExtractor.getRevisionIDsQuery = getRevisionIDsQuery;
	}

	public static String getGetRevisionTextQuery()
	{
		return getRevisionTextQuery;
	}

	public static void setGetRevisionTextQuery( String getRevisionTextQuery )
	{
		WikipediaRevisionsExtractor.getRevisionTextQuery = getRevisionTextQuery;
	}

	public static URL getWikipediaApiConnector()
	{
		return wikipediaApiConnector;
	}

	public static void setWikipediaApiConnector( URL wikipediaApiConnector )
	{
		WikipediaRevisionsExtractor.wikipediaApiConnector = wikipediaApiConnector;
	}

	public static String getJsonText()
	{
		return jsonText;
	}

	public static void setJsonText( String jsonText )
	{
		WikipediaRevisionsExtractor.jsonText = jsonText;
	}

	public String getContentWithEntityMentions()
	{
		return contentWithEntityMentions;
	}

	public void setContentWithEntityMentions( String contentWithEntityMentions )
	{
		this.contentWithEntityMentions = contentWithEntityMentions;
	}

	public static void main( String args[] )
	{
		WikipediaRevisionsExtractor wikiRevisionExtractor = new WikipediaRevisionsExtractor();
		 wikiRevisionExtractor.populateRevisionsMap( WikipediaConstants.WIKIPEDIA_EVENT_PAGE_NAME );
//		 wikiRevisionExtractor.parseRevisionText( wikiRevisionExtractor.getRevisionText( WikipediaConstants.WIKIPEDIA_EVENT_PAGE_NAME ),
		// "Barack_Obama" );
		 System.out.println( WikipediaRevisionsExtractor.jsonText );
//		System.out.println( wikiRevisionExtractor.getRevisionContentWithEntityMentions( wikiRevisionExtractor.getRevisionText( WikipediaConstants.WIKIPEDIA_EVENT_PAGE_NAME ), "United States" ) );
		// System.out.println( WikipediaRevisionsExtractor.jsonText );
	}

}
