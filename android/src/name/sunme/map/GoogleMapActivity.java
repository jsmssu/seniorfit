package name.sunme.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.maindrawbar.R;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends FragmentActivity implements
		LocationListener {
String TAG = "GoogleMapActivity";
	GoogleMap googleMap;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map);

		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		intent = getIntent();
		
		
		
		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}
			locationManager.requestLocationUpdates(provider, 20000, 0, (android.location.LocationListener) this);
			googleMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
				
				@Override
				public void onMapLoaded() {
					googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					
					String result = intent.getStringExtra("json");
					try {
						JSONObject jo = new JSONObject(result);
						JSONObject GeoInfoWalkwayWGS =jo.getJSONObject("GeoInfoWalkwayWGS");
						int list_total_count = GeoInfoWalkwayWGS.getInt("list_total_count");
						if(list_total_count>0) {
							JSONArray rows = GeoInfoWalkwayWGS.getJSONArray("row");
							for(int i=0; i<rows.length(); i++) {
								JSONObject row = rows.getJSONObject(i);
								double LNG = row.getDouble("LNG");
								double LAT = row.getDouble("LAT");
								Log.d(TAG,"LNG : "+LNG+", LAT : "+LAT);
								googleMap.addMarker(new MarkerOptions()
						        //.title("Hello world")
								.position(new LatLng(LAT, LNG)));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}); 
			
		}	

	}
	
	@Override
	public void onLocationChanged(Location location) {

		//TextView tvLocation = (TextView) findViewById(R.id.tv_location);

		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		//googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

		// Setting latitude and longitude in the TextView tv_location
		//tvLocation.setText("Latitude:" + latitude + ", Longitude:" + longitude);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return false;
	}
}