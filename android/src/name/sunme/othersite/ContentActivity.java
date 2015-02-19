package name.sunme.othersite;

import java.util.ArrayList;
import java.util.HashMap;

import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.JsonOpener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ContentActivity extends Activity {
	private String TAG = "ContentActivity";
	private JSONObject menuInfo;
	private String menu_id;
	private String menu_title;

	private ArrayList<HashMap<String, String>> post_arraylist;
	private SimpleAdapter postlist_adapter;
	private JSONArray post_jsonarray;
	private ListView post_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		post_list = (ListView) this.findViewById(R.id.content_listview);

		Intent myIntent = getIntent();
		post_arraylist = new ArrayList<HashMap<String, String>>();

		try {
			menuInfo = new JSONObject(myIntent.getStringExtra("menuInfo"));
			menu_id = menuInfo.getString("id");
			menu_title = menuInfo.getString("title");
		} catch (Exception e) {
			finish();
		}

		postlist_adapter = new SimpleAdapter(getApplicationContext(),
				post_arraylist, R.layout.activity_content_row,
				new String[] { "title" }, new int[] { R.id.content_row_title });

		post_list.setAdapter(postlist_adapter);
		String url = "http://sunme.name/api/get_category_posts/?cat=" + menu_id
				+ "&&include=title";
		// "http://sunme.name/api/get_category_posts/?cat="+2+"&&include=title,custom_fields,content";
		(new JsonOpener(this, postHandler, url)).open();
		post_list.setOnItemClickListener(contentclick_listener);

	}

	OnItemClickListener contentclick_listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			try {
				Intent intent = new Intent(getApplicationContext(),
						ContentPageActivity.class);
				intent.putExtra("postInfo", post_jsonarray.get(position)
						.toString());
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	};

	private final Handler postHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Log.d(TAG, "msg : " + msg.what);
			if (msg.what == -1) {
				return;
			} else if (msg.what == 1) {
				Bundle bundle = msg.getData();
				String result = bundle.getString("result");
				JSONObject jo;
				try {
					jo = new JSONObject(result);
					post_jsonarray = jo.getJSONArray("posts");
					post_arraylist.clear();
					for (int i = 0; i < post_jsonarray.length(); i++) {
						JSONObject post = post_jsonarray.getJSONObject(i);
						final String title = post.getString("title");
						post_arraylist.add(new HashMap<String, String>() {
							{
								put("title", title);
							}
						});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listUpdate();
			}
		}
	};

	private void listUpdate() {
		// adapter.refresh_data();
		postlist_adapter.notifyDataSetChanged();
	}
}
