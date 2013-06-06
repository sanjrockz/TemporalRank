package org.knoesis.data;

public class TrendingEntity implements Comparable<TrendingEntity>
{

	private String url;
	private double trend;

	public TrendingEntity( String url, double trend )
	{
		this.url = url;
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

	
	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	
	@Override
	public int compareTo( TrendingEntity o )
	{
		if ( o.getTrend() - this.trend > 0 )
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

	
}

