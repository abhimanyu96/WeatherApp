package com.weekendr.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.weekendr.weatherapp.utils.AppConstants;

public class Main extends Activity 
{
	private Button selectCity;
	private Spinner citySelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		selectCity = (Button)findViewById(R.id.selectCity);
		citySelect = (Spinner)findViewById(R.id.citySelectSpinner);
		
		
		selectCity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				if(citySelect.getSelectedItem().toString().equals("Select City"))
				{
					Toast.makeText(Main.this,"Please select a city to continue..", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent(Main.this,ShowWeather.class);
					intent.putExtra(AppConstants.CITY_KEY,citySelect.getSelectedItem().toString());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId())
		{
		//case R.id.about:
			//Toast.makeText(this,"About clicked!",Toast.LENGTH_SHORT).show();
			//break;
		}
		return super.onOptionsItemSelected(item);
	}

}
