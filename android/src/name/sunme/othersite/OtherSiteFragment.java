package name.sunme.othersite;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import name.sunme.firstexecution.Setup4Activity;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.id;
import name.sunme.maindrawbar.R.layout;
import name.sunme.seniorfit.UrlOpenerBasic;
import name.sunme.seniorfit.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OtherSiteFragment extends Fragment {
	String TAG = "OtherSiteFragment";
	public OtherSiteFragment() {
	}
	ArrayList<HashMap<String, String>> menu_arraylist;
	SimpleAdapter menu_list_adapter;
	JSONArray menu_jsonarray;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// do modify
		View rootView = inflater.inflate(R.layout.fragment_other_site,
				container, false);
		(new UrlOpenerBasic(menuHandler,
				"http://sunme.name/api/get_category_index/?parent=2&&include=title")).open();

		ListView menu_list = (ListView)rootView.findViewById(R.id.othersite_listview);
		
		
		menu_arraylist = new ArrayList<HashMap<String, String>>();
		menu_list_adapter = new SimpleAdapter(getActivity(), menu_arraylist, 
				R.layout.fragment_other_site_row, 
				new String[]{"title"}, 
				new int[]{R.id.othersite_row_title});
		
		menu_list.setAdapter(menu_list_adapter);
		menu_list.setOnItemClickListener(menuclick_listener);
		return rootView;
	}

	OnItemClickListener menuclick_listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {			
			try {
				Intent intent = new Intent(getActivity(), ContentActivity.class);
				intent.putExtra("menuInfo", menu_jsonarray.getJSONObject(position).toString());
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	};
	
	private final Handler menuHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
 
			if (msg.what == -1) {
				return;
			}
			else if (msg.what == 1) {
				Bundle bundle = msg.getData();
				String result = bundle.getString("result");
				JSONObject jo;
				try {
					jo = new JSONObject(result);
					menu_jsonarray = jo.getJSONArray("categories");
					Log.d(TAG,"1: " + menu_jsonarray.toString());
					menu_arraylist.clear();
					for(int i=0; i<menu_jsonarray.length(); i++) {
						JSONObject cat = menu_jsonarray.getJSONObject(i);
						final String title = cat.getString("title");
						Log.d(TAG,"2: " +  title);
						menu_arraylist.add(new HashMap<String, String>(){{put("title", title);}});
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
		menu_list_adapter.notifyDataSetChanged();
	}
}
