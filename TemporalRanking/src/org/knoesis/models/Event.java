package org.knoesis.models;

import java.sql.Timestamp;

public class Event {
	private int event_id;
	private String event_name;
	private Timestamp start_date;
	private Timestamp end_date;
	
	public Event(int eId, String eName, Timestamp sDate, Timestamp eDate) {
		event_id = eId;
		event_name = eName;
		start_date = sDate;
		end_date = eDate;
	}
	
	public int getEventId() {
		return event_id;
	}
	
	public String getEventName() {
		return event_name;
	}
	
	public Timestamp getStartDate() {
		return start_date;
	}
	
	public Timestamp getEndDate() {
		return end_date;
	}
}