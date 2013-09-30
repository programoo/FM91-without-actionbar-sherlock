package com.mimotech.testgmapapi;


public class News {
	String id;
	String type;
	String primarySource;
	String secondarySource;
	String startTime;
	String endTime;
	String mediaType;
	String path;
	String title;
	String description;
	String locationType;
	String roadName;
	String startPoint;
	String startPointLat, startPointLong;
	String endPoint;
	String endPointLat, endPointLong;
	
	//assign later
	String alreadyPassTime="undefined";
	long unixTime=0;
	
	boolean isRead;
	public News(String id, String type, String primarySource,
			String secondarySource, String startTime, String endTime,
			String mediaType, String path, String title, String description,
			String locationType, String roadName, String startPoint,
			String startPointLat, String startPointLong, String endPoint,
			String endPointLat, String endPointLong,boolean isRead,String alreadyPassTime,long unixTime) {
		this.id = id;
		this.type = type;
		this.primarySource = primarySource;
		this.secondarySource = secondarySource;
		this.startTime = startTime;
		this.endTime = endTime;
		this.mediaType = mediaType;
		this.path = path;
		this.title = title;
		this.description = description;
		this.locationType = locationType;
		this.roadName = roadName;
		this.startPoint = startPoint;
		this.startPointLat = startPointLat;
		this.startPointLong = startPointLong;
		this.endPoint = endPoint;
		this.endPointLat = endPointLat;
		this.endPointLong = endPointLong;
		this.isRead = isRead;
		this.alreadyPassTime = alreadyPassTime;
		this.unixTime = unixTime;
		
	}

	@Override
	public String toString() {
		return id+","+type+","+primarySource+","+secondarySource+","+startTime+","+
				endTime+","+mediaType+","+path+","+title+","+description+","+locationType+","+
				roadName+","+startPoint+","+startPointLat+","+startPointLong+","+endPoint+","+endPointLat+","+endPointLong+","+isRead;
	}
}
