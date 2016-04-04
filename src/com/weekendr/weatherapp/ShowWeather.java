package com.weekendr.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weekendr.weatherapp.utils.AppConstants;
import com.weekendr.weatherapp.utils.VolleySingleton;

public class ShowWeather extends Activity {

	private TextView minTemp,maxTemp,weatherDescription,humidity;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_weather);

		minTemp = (TextView)findViewById(R.id.minTemp);
		maxTemp = (TextView)findViewById(R.id.maxTemp);
		weatherDescription = (TextView)findViewById(R.id.weatherDescription);
		humidity = (TextView)findViewById(R.id.humidity);

		//trigger asynctask with the given url
		//WeatherTask o = new WeatherTask();
		//o.execute(AppConstants.WEATHER_SERVER_URL + getIntent().getExtras().getString(AppConstants.CITY_KEY));
		initData(getIntent().getExtras().getString(AppConstants.CITY_KEY));
	}

	private void initData(String cityname)
	{
		Entry e = VolleySingleton.getInstance(this).getRequestQueue().getCache().get(AppConstants.WEATHER_SERVER_URL + cityname);
		if(e!=null)
		{
			if(e.data!=null && !e.data.toString().isEmpty())
			{
				String response = new String(e.data);
				try 
				{
					//json parsing below

					JSONObject json = new JSONObject(response);
					minTemp.setText(json.getJSONObject("main").getString("temp_min"));
					maxTemp.setText(json.getJSONObject("main").getString("temp_max"));
					weatherDescription.setText(json.getJSONArray("weather").getJSONObject(0).getString("description"));
					humidity.setText(json.getJSONObject("main").getString("humidity"));
				} 
				catch (JSONException e2) 
				{
					e2.printStackTrace();
				}
			}
			else
			{
				getWeatherDetails(cityname);
			}

		}
		else
		{
			getWeatherDetails(cityname);
		}
	}
	private void getWeatherDetails(String cityName) 
	{
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		final String url = AppConstants.WEATHER_SERVER_URL + cityName;
		StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) 
			{
				/*JSONObject responseObject;
				try
				{
					responseObject = new JSONObject(response);
					System.out.println(responseObject.toString());
					if(responseObject.has("errorCode")&&responseObject.getString("errorCode").equals("0"))
					{
						Utils.showToast(PostImagerySubmission.this,responseObject.getString("errorMessage"));
						Intent intent = new Intent(PostImagerySubmission.this,ImageryDetails.class);
						intent.putExtra(AppConstants.IMAGERY_ID, getIntent().getExtras().getString(AppConstants.IMAGERY_ID));
						intent.putExtra(AppConstants.THEME_NAME, getIntent().getExtras().getString(AppConstants.IMAGERY_NAME));
						startActivity(intent);
						finish();
					}
					else
					{
						Utils.showToast(PostImagerySubmission.this,responseObject.getString("errorMessage"));
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
				}*/
				toa();
				try 
				{
					//json parsing below
					Entry e = new Entry();
					e.data = response.getBytes();
					VolleySingleton.getInstance(ShowWeather.this).getRequestQueue().getCache().put(url, e);
					JSONObject json = new JSONObject(response);
					minTemp.setText(json.getJSONObject("main").getString("temp_min"));
					maxTemp.setText(json.getJSONObject("main").getString("temp_max"));
					weatherDescription.setText(json.getJSONArray("weather").getJSONObject(0).getString("description"));
					humidity.setText(json.getJSONObject("main").getString("humidity"));
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}
		},  new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				//	Utils.showToast(PostImagerySubmission.this, AppConstants.MESSAGES.ERROR_FETCHING_DATA_MESSAGE);

			}
		}) 
		{
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(PostImagerySubmission.this);
				Map<String,String> params = new HashMap<String,String>();
				params.put("authorId",Preferences.getInstance().submitterId);
				params.put("imageryId",imageryId);
				params.put("reply", Html.toHtml(description.getText()));
				return params;
			}*/};	
			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					8000, 
					3, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			queue.add(requestObject);
	}
	
	private void toa()
	{
		System.out.println("abc");
	}

	private class WeatherTask extends AsyncTask<String, Integer, String>
	{
		private String jsonResponse;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}


		@Override
		protected String doInBackground(String... params) 
		{
			try 
			{
				//make a URL connected with the given url that is passed
				//in execute method in on create
				URL url = new URL(params[0]);

				//make a HTTP GET request and open a URL connection
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accepts","application/json");

				//check for non 200 (error) response code
				if(conn.getResponseCode() != 200)
				{
					//populate -1 as an indication of error in response
					jsonResponse = "-1";
				}
				else
				{
					//read the response from stream and populate in a 
					//string as you do in case of File I/O
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String tempStore;
					jsonResponse = new String();
					while((tempStore = reader.readLine())!=null)
					{
						jsonResponse+=tempStore;
					}
				}
			} 
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}

			//return populated response so as it can be passed to onPostExecute
			//internally
			return jsonResponse;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			//result contains response populated in doinbackground
			//from server
			super.onPostExecute(result);
			if(result.equals("-1"))
			{
				Toast.makeText(ShowWeather.this, "Could not connect to server, please try after sometime!",Toast.LENGTH_SHORT).show();
			}
			else
			{
				try 
				{
					//json parsing below
					JSONObject json = new JSONObject(result);
					minTemp.setText(json.getJSONObject("main").getString("temp_min"));
					maxTemp.setText(json.getJSONObject("main").getString("temp_max"));
					weatherDescription.setText(json.getJSONArray("weather").getJSONObject(0).getString("description"));
					humidity.setText(json.getJSONObject("main").getString("humidity"));
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
