package org.knoesis.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WikiRevision {
	private Long id;
	private Integer size; 
	private Date date;


	/**
	 * Added by Sanjaya to hold parent revision id and time stamp which is used to calculate 
	 * and keep track of the changes in two revisions.
	 */
	private Long parentRevisionId;
	
	public static List<WikiRevision> serializeJsonObject(JSONObject object){
		System.out.println(object);
		List<WikiRevision> revisions = new ArrayList<WikiRevision>();
		try {
			JSONObject temp = object.getJSONObject("query");
			object = temp.getJSONObject("pages");
			//temp = object.getJSONObject("33455163");
			//temp = object.getJSONObject("33223959");
			temp = object.getJSONObject("20102947");
			
			object = temp;
			//System.out.println(object);
			//System.out.println(temp);
			System.out.println("WikiRevision: " + object);
			System.out.println("WikiRevision: " + temp);
			WikiRevision prevRevision = new WikiRevision();
			JSONArray revisionJsonArray = object.getJSONArray("revisions");
			for(int i=0; i<revisionJsonArray.length(); i++){
				WikiRevision revision = new WikiRevision();
				JSONObject revisionJson = (JSONObject) revisionJsonArray.get(i);
				revision.setId(revisionJson.getLong("revid"));
				String dateString = revisionJson.getString("timestamp");
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				revision.setDate((Date)formatter.parse(dateString));
				revision.setSize(revisionJson.getInt("size"));
				if(i>0){
					prevRevision.setSize(prevRevision.getSize() - revision.getSize());
					revisions.add(prevRevision);
				}
				prevRevision = revision;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return revisions;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
	public Long getParentRevisionId() {
		return parentRevisionId;
	}

	public void setParentRevisionId(Long parentRevisionId) {
		this.parentRevisionId = parentRevisionId;
	}

	@Override
	public String toString() {
		return DateFormatUtils.format(date, "yyyy-MM-dd").toString()+"\t"+this.getSize();
	}


}