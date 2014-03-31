package com.example.geomobile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity implements LocationListener{
	
	private static final String MAP_URL = "file:///android_asset/map.html";
	LocationManager mLocationManager = null;
	Location mLastLocation = null;
	WebView mWebView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.loadUrl(MAP_URL);

		mWebView.addJavascriptInterface(new JavaScriptInterface(), "android");

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		mLocationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class JavaScriptInterface {
		
		@JavascriptInterface
        public double getLatitude() {
			if (mLastLocation == null) {
				return Double.parseDouble("23.937591");
			}
			
			double latitude = mLastLocation.getLatitude();
			Log.e("GeoMobile", "Latitude:"+latitude);
            return Double.parseDouble(Double.toString(latitude));
        }

		@JavascriptInterface
        public double getLongitude() {
			if (mLastLocation == null) {
				return Double.parseDouble("120.700807");
			}
			
			double longtude = mLastLocation.getLongitude();
			Log.e("GeoMobile", "Latitude:"+longtude);
            return Double.parseDouble(Double.toString(longtude));
        }

    }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		mLastLocation = arg0;
		if (mLastLocation != null) {
			Log.e("GeoMobile", "Latitude:"+mLastLocation.getLatitude()+", Latitude:"+ mLastLocation.getLongitude() );
			mWebView.loadUrl(MAP_URL);
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
