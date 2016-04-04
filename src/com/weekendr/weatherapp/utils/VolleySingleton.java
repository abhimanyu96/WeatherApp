package com.weekendr.weatherapp.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
	private static VolleySingleton mInstance = null;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private VolleySingleton(final Context context){
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static VolleySingleton getInstance(Context context){
		if(mInstance == null){
			mInstance = new VolleySingleton(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue(){
		return this.mRequestQueue;
	}

	public ImageLoader getImageLoader(){
		return this.mImageLoader;
	}

}