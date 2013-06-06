package org.knoesis.data;

public class DBpediaEntity implements Comparable<DBpediaEntity>
{

	private String url;
	private String entityName;

	public DBpediaEntity( String entityName, String url )
	{
		this.url = url;
		this.entityName = entityName;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName( String entityName )
	{
		this.entityName = entityName;
	}

	@Override
	public int compareTo( DBpediaEntity o )
	{
		return this.entityName.compareTo( o.getEntityName() );
	}

}
