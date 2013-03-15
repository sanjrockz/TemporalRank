package org.knoesis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

import org.knoesis.models.*;
import org.knoesis.utils.*;

public class DataManager {
		
		private Connection connection = null;
		
		public DataManager(String dbConnection) {
			try {
				Class.forName(WikipediaConstants.DRIVER);
				connection = DriverManager.getConnection(dbConnection);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private boolean returnMessage(int[] returnCode) {
			for(int i : returnCode) {
				if(i == Statement.EXECUTE_FAILED) 
					return false;				
			}
			return true;
		}
		
		public List<WikiObject> displayWikiLinks(String tableName, int event_id) {
			System.out.println("Ordering links by their page views.");
			List<WikiObject> results = new ArrayList<WikiObject>();
			String sql = "SELECT link_id, revision_id, link_added, page_views, percentage_inc_pgviews, timestamp FROM " + tableName + " WHERE event_id = " + event_id
						+ " order by timestamp, percentage_inc_pgviews desc";
			
			Statement stmt = null;
			try {
				stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					Timestamp timestamp = rs.getTimestamp("timestamp");
					String link = rs.getString("link_added").trim();
					int pgViews = rs.getInt("page_views");
					double percentageInc = rs.getDouble("percentage_inc_pgviews");
					long revision_id = rs.getLong("revision_id");
					WikiObject obj = new WikiObject(timestamp, link, pgViews, percentageInc, event_id,revision_id);
					results.add(obj);
				}
			
				return results;
			}catch(Exception e) {
				e.printStackTrace();
				
			}finally {
				try {
					stmt.close();
				} catch(Exception e) {
					e.printStackTrace();					
				}
			}
			return null;		
			
		}
		
		public Event getEvent(int eventId, String tableName) {
		
			Event event = null;
			
			String sql = "SELECT event_name, start_date, end_date FROM " + tableName + " WHERE event_id = " + eventId;
			Statement stmt = null;
			try {
				stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				if(rs.next()) {
					String eventName = rs.getString(1);
					Timestamp createdDate = rs.getTimestamp(2);
					Timestamp endDate = rs.getTimestamp(3);	
					event = new Event(eventId,eventName,createdDate,endDate);
				}
				
			} catch(Exception e) {
				e.printStackTrace();				
			} finally {
				try {
					stmt.close();					
				} catch(Exception e) {
					e.printStackTrace();					
				}
			} 
			return event;
			
		}
		
		public void insertWikiLinks(String tableName,int event_id, Map<Integer, WikiObject> link_info,boolean batchInsert) {
			System.out.println("Inserting wikilinks" + tableName);
			String sql = "INSERT INTO " + tableName + " (event_id,revision_id,link_added,page_views,percentage_inc_pgviews,timestamp) VALUES (?,?,?,?,?,?)";
			PreparedStatement ps = null;
			int count = 0;			
			try {
					connection.setAutoCommit(false);
					ps = connection.prepareStatement(sql);
					System.out.println("Wikiobject size: " + link_info.size());
					for(WikiObject w : link_info.values()) {
						ps.setInt(1,event_id);
						ps.setLong(2, w.getRevisionId());
						ps.setString(3, w.getLink());
						ps.setInt(4, w.getPageViews());
						ps.setDouble(5, w.getPercentageIncrease());
						ps.setTimestamp(6, w.getDate());	
						ps.addBatch();	
						if(batchInsert && (++count % WikipediaConstants.BATCH_COUNT == 0)) {												
							if(!returnMessage(ps.executeBatch())) {
								System.err.println("Error inserting: " + w.getLink() + "\t for " + w.getDate());
							}
							System.out.println("Inserted " + count + "links to the database!");
						}
						if(!batchInsert) {
							ps.execute();
						}
					}								
				
			} catch(Exception e) {				
				e.printStackTrace();
			}
			finally {
				try {
					ps.close();
					connection.commit();
					connection.setAutoCommit(true);
				}
				catch(Exception e) {
					e.printStackTrace();					
				}
				
			}
		}
} 