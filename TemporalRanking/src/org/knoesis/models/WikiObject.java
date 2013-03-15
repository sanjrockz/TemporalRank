package org.knoesis.models;

import java.sql.Timestamp;

public class WikiObject {
		private Timestamp editedDate;
		private String link;
		private int pageViews;
		private double percentageIncrease;
		private int eventId;
		private long revision_id;
		
		public WikiObject(Timestamp eDate, String lk) {
			editedDate = eDate;
			link = lk;			
		}
		
		public WikiObject(Timestamp eDate, String lk, int pgViews, double percentageInc, int eId, long rev_id) {
			editedDate = eDate;
			link = lk;
			pageViews = pgViews;
			percentageIncrease = percentageInc;
			eventId = eId;
			revision_id = rev_id;
		}
		
		public int getPageViews() {
			return pageViews;
		}
		
		public double getPercentageIncrease() {
			return percentageIncrease;
		}
		
		public int getEventId() {
			return eventId;
		}
		
		public Timestamp getDate() {
			return editedDate;
		}
		
		public String getLink() {
			return link;
		}
		
		public long getRevisionId() {
			return revision_id;
		}
}