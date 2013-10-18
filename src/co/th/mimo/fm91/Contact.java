package co.th.mimo.fm91;

import android.graphics.Bitmap;


public class Contact {
	public String phoneNum;
	public String name;
	public Bitmap imgUrl;
	public Contact(String phoneNUm, String name,Bitmap imgUrl ){
		this.phoneNum = phoneNUm;
		this.name = name;
		this.imgUrl = imgUrl;
	}

	public String toString(){
		return this.phoneNum+","+this.name;
	}
}
