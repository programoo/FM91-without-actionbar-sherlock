package com.mimotech.testgmapapi;


public class Contact {
	public String phoneNum;
	public String name;
	public String imgUrl;
	public Contact(String phoneNUm, String name,String imgUrl ){
		this.phoneNum = phoneNUm;
		this.name = name;
		this.imgUrl = imgUrl;
	}

	public String toString(){
		return this.phoneNum+","+this.name+","+this.imgUrl;
	}
}
