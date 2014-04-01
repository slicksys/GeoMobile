package com.example.geomobile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.geomobile.GeoLocation.LocationResult;

public class MainActivity extends Activity{
	
	private static final String MAP_URL = "file:///android_asset/showmap.html";
	private double nowLat, nowLng;
	WebView mWebView = null;
	GeoLocation geoLocation = new GeoLocation();

	LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {
			if (location != null) {
				nowLat = location.getLatitude();
				nowLng = location.getLongitude();
				mWebView.loadUrl("javascript:moveTo(" + nowLat + "," + nowLng
						+ ")");
				mWebView.setVisibility(View.VISIBLE);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		geoLocation.getLocation(this, locationResult);
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.loadUrl(MAP_URL);

		mWebView.addJavascriptInterface(new FromJavaScript(this), "Android");

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Button btnUpdate = (Button)findViewById(R.id.btn_update);
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				geoLocation.getLocation(MainActivity.this, locationResult);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class FromJavaScript {
		private Context c;

		FromJavaScript(Context c) {
			this.c = c;
		}

		@JavascriptInterface
		public double getLatLng(String data) {
			//Message msg = new Message();
			//Bundle db = new Bundle();
			//db.putString("data", data);
			//msg.setData(db);
			Toast.makeText(c, data, Toast.LENGTH_LONG).show();
			return 0;
		}
	}
	
}
