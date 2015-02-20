package name.sunme.firstexecution;

import java.util.ArrayList;
import java.util.List;

import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.id;
import name.sunme.maindrawbar.R.layout;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.NewPermissionsRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookLoginActivity extends Activity {
	String TAG = "FacebookLoginActivity";
	LoginButton authButton;
	Button facebook_next;
	TextView lblEmail;
	private static final List<String> PERMISSIONS = new ArrayList<String>() {
		{

			add("publish_actions");
			add("user_friends");
			add("public_profile");
			add("email");
			add("basic_info");
		}
	};
	private static final int PICK_FRIENDS_ACTIVITY = 1;
	private Button pickFriendsButton;
	private TextView resultsTextView;
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;
	private Location pickPlaceForLocationWhenSessionOpened = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);

		/*Button t = (Button) findViewById(R.id.login_writebutton);
		t.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				publishStory();
			}
		});*/

		//authButton = (LoginButton) findViewById(R.id.authButton);
		lblEmail = (TextView) findViewById(R.id.lblEmail); 
		facebook_next = (Button)findViewById(R.id.facebook_next);

		
		facebook_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
				startActivity(intent);
				FacebookLoginActivity.this.fileList();
			}
		});
		
		
		
		lifecycleHelper = new UiLifecycleHelper(this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChanged(session, state, exception);
					}
				});
		lifecycleHelper.onCreate(savedInstanceState);
		//ensureOpenSession();
	}

	private boolean ensureOpenSession() {
		Log.d(TAG, "ensureOpenSession");
		if (Session.getActiveSession() == null
				|| !Session.getActiveSession().isOpened()) {
			Session.openActiveSession(this, true, new Session.StatusCallback() {
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					onSessionStateChanged(session, state, exception);
				}
			});
			return false;
		}
		return true;
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();

		// Update the display every time we are started (this will be
		// "no place selected" on first
		// run, or possibly details of a place if the activity is being
		// re-created).
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult lifecycleHelper " + lifecycleHelper
				+ "intent : " + data);

		super.onActivityResult(requestCode, resultCode, data);
		lifecycleHelper.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		lifecycleHelper.onDestroy();
	}

	private void onError(Exception exception) {
		Log.d(TAG, "onError");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Error").setMessage(exception.getMessage())
				.setPositiveButton("OK", null);
		builder.show();
	}

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		Log.d(TAG, "onSessionStateChanged state : " + state);
		if (pickPlaceForLocationWhenSessionOpened != null && state.isOpened()) {
			Location location = pickPlaceForLocationWhenSessionOpened;
			pickPlaceForLocationWhenSessionOpened = null;
		}
	}

	private void publishStory() {
		Log.d(TAG, "publishStory");
		Session session = Session.getActiveSession();
		Log.d(TAG, "session : " + session);
		if (session != null) {
			boolean pendingPublishReauthorization = false;
			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			postParams.putString("name", "TEST1");
			postParams.putString("caption", "");
			postParams.putString("description", "TEST2");
			postParams.putString("link", "");
			postParams.putString("picture", "");

			Log.d(TAG, "postParams : " + postParams);

			 
			new Request(session, "me/feed", postParams, HttpMethod.POST,
					new Request.Callback() {
						public void onCompleted(Response response) {
							/* handle the result */

							if (response.getError() == null) {
								Toast.makeText(FacebookLoginActivity.this,
										"Posted succeffully.",
										Toast.LENGTH_SHORT).show();

							}

							else {
								Log.d(TAG, response.toString());
								Toast.makeText(FacebookLoginActivity.this,
										"oops..please try again",
										Toast.LENGTH_SHORT).show();
							}

						}
					}).executeAsync();
		}
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
}
