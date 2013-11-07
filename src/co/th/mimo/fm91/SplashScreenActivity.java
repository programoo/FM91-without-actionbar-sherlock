package co.th.mimo.fm91;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class SplashScreenActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Thread(new Runnable() {
	        public void run() {
	            try {
	                Thread.sleep(3000);
	            } catch (InterruptedException e) { }

	            Intent intent = new Intent(SplashScreenActivity.this, FM91MainActivity.class);
	            startActivity(intent);
	            finish();
	        }
	    }).start();
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
	
}
