package com.mimotech.testgmapapi;

import java.util.ArrayList;

public class Nearby {
	//Bangkok as default
	public String title ;
	public String lat ;
	public String lng ;
	public static ArrayList<Camera> camList;
	public Nearby(String title,String lat,String lng){
		this.title = title;
		this.lat = lat;
		this.lng = lng;
	}
	
	public Nearby clone(){
		return new Nearby(this.title,this.lat,this.lng);
	}
	
	public String howFar(){
		double howFar = (int) (Info.getInstance().distance(Double.parseDouble(lat),
				Double.parseDouble(lng), Info.lat, Info.lng, "K") * 100) / 100.0;
		
		return howFar+" km";
		
	}

	
}
