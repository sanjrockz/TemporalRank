package org.knoesis.models;

public class PageStatistics {
	private int pageViews;
	private double percentageIncrease;
	
	public PageStatistics(int pgViews, double percentageInc) {
		pageViews = pgViews;
		percentageIncrease = percentageInc;
	}
	
	public int getPageViewCount() {
		return pageViews;
	}
	
	public double getPercentageIncrease() {
		return percentageIncrease;
	}
}