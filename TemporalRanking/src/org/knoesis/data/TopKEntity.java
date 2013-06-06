package org.knoesis.data;

public class TopKEntity implements Comparable<TopKEntity>
{

	private String url;
	private int appearanceCount;
	private double entityToTotalTweetsRatio;
	private double trend;

	public TopKEntity( String url, int appearanceCount, double entityToTotalTweetsRatio, double trend )
	{
		this.url = url;
		this.appearanceCount = appearanceCount;
		this.entityToTotalTweetsRatio = entityToTotalTweetsRatio;
		this.trend = trend;
	}

	public double getTrend()
	{
		return trend;
	}

	public void setTrend( double trend )
	{
		this.trend = trend;
	}

	public double getEntityToTotalTweetsRatio()
	{
		return entityToTotalTweetsRatio;
	}

	public void setEntityToTotalTweetsRatio( double entityToTotalTweetsRatio )
	{
		this.entityToTotalTweetsRatio = entityToTotalTweetsRatio;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public int getAppearanceCount()
	{
		return appearanceCount;
	}

	public void setAppearanceCount( int appearanceCount )
	{
		this.appearanceCount = appearanceCount;
	}

	@Override
	public int compareTo( TopKEntity o )
	{
		return o.getAppearanceCount() - this.appearanceCount;
	}

	public int compareTrends( TopKEntity objct1, TopKEntity object2 )
	{
		//if()
		return 0;
	}
	
}
