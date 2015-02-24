package name.sunme.setting;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.android.gms.internal.bm;

import name.sunme.firstexecution.Setup1Activity;
import name.sunme.firstexecution.Setup3Activity;
import name.sunme.seniorfit.DBAdapter; 
import name.sunme.seniorfit.Utils;
import name.sunme.maindrawbar.R; 
import android.app.Activity; 
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log; 
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.ImageView;
import android.widget.LinearLayout; 
import android.widget.TextView;

public class SettingProfileActivity extends Activity {
	String TAG = "SettingProfileActivity";
	final int REQUEST_CODE_IMAGE = 1;
	
	
	ImageView settingprofile_photo;

	TextView settingprofile_name;
	LinearLayout settingprofile_bmicolor;
	LinearLayout settingprofile_box_weight;
	LinearLayout settingprofile_box_age;
	LinearLayout settingprofile_box_sex;
	LinearLayout settingprofile_box_height;

	TextView settingprofile_weight;
	TextView settingprofile_age;
	TextView settingprofile_sex;
	TextView settingprofile_height;
	
	
	TextView settingprofile_bminumber;
	TextView settingprofile_bmitext;
	private DBAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_profile);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		adapter = new DBAdapter(getApplicationContext());

		settingprofile_photo = (ImageView) findViewById(R.id.settingprofile_photo);
		settingprofile_name = (TextView) findViewById(R.id.settingprofile_name);

		settingprofile_bmicolor = (LinearLayout) findViewById(R.id.settingprofile_bmicolor);
		settingprofile_box_weight = (LinearLayout) findViewById(R.id.settingprofile_box_weight);
		settingprofile_box_age = (LinearLayout) findViewById(R.id.settingprofile_box_age);
		settingprofile_box_sex = (LinearLayout) findViewById(R.id.settingprofile_box_sex);
		settingprofile_box_height = (LinearLayout) findViewById(R.id.settingprofile_box_height);

		settingprofile_weight = (TextView) findViewById(R.id.settingprofile_weight);
		settingprofile_age = (TextView) findViewById(R.id.settingprofile_age);
		settingprofile_sex = (TextView) findViewById(R.id.settingprofile_sex);
		settingprofile_height = (TextView) findViewById(R.id.settingprofile_height);

		settingprofile_bminumber = (TextView) findViewById(R.id.settingprofile_bminumber);
		settingprofile_bmitext = (TextView) findViewById(R.id.settingprofile_bmitext);
		
		
		settingprofile_box_weight.setOnClickListener(listener_weightclick);
		settingprofile_box_age.setOnClickListener(listener_ageclick);
		//settingprofile_box_sex.setOnClickListener(null);
		settingprofile_box_height.setOnClickListener(listener_heightclick);

		settingprofile_photo.setOnClickListener(listener_photoclick);
		settingprofile_name.setOnClickListener(listener_nameclick);

		
		
		Log.d(TAG, "set elements");
		

		loadValues();
	}

	OnClickListener listener_weightclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomDialogs.change_weight(SettingProfileActivity.this, settingprofile_weight, mHandler);
		}
	};
	
	OnClickListener listener_ageclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomDialogs.change_age(SettingProfileActivity.this, settingprofile_age);
		}
	};
	
	
	OnClickListener listener_heightclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomDialogs.change_height(SettingProfileActivity.this, settingprofile_height, mHandler);
		}
	};

	OnClickListener listener_nameclick = new OnClickListener() {
		public void onClick(View v) {
			CustomDialogs.change_name(SettingProfileActivity.this, settingprofile_name);
		}
	};

	OnClickListener listener_photoclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showActivityToSelectPhoto();
		}
	};

	private void showActivityToSelectPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_CODE_IMAGE);
	}

	

	private void applyBMI() {
		DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
		int weight = 0;
		int height = 0;
		String weight_s = dbAdapter.get_setting("weight");
		String height_s = dbAdapter.get_setting("height");
		if (weight_s!=null)weight = Integer.parseInt(weight_s);
		if (height_s!=null)height = Integer.parseInt(height_s);
		
		try { 
			
			float t_bmi = Utils.getBMI_number(weight, height);
			String t_bmi_string = Utils.getBMI_string(t_bmi);
			
			if (t_bmi<=18.5) { 
				settingprofile_bmicolor.setBackgroundColor(Color.rgb(0xD4, 0xD4, 0xD4));
			} else if (t_bmi<23) { 
				settingprofile_bmicolor.setBackgroundColor(Color.rgb(0x90, 0xE3, 0xCB));
			} else if (t_bmi<25) { 
				settingprofile_bmicolor.setBackgroundColor(Color.rgb(0x37, 0xB7, 0xBB));
			} else if (t_bmi<30) { 
				settingprofile_bmicolor.setBackgroundColor(Color.rgb(0x4C, 0x3D, 0x8F)); 
			} else { 
				settingprofile_bmicolor.setBackgroundColor(Color.rgb(0xC8, 0x30, 0x44)); 
			}			
			
			settingprofile_bminumber.setText(String.format("%.2f", t_bmi));
			settingprofile_bmitext.setText("("+t_bmi_string+")");
		} catch (Exception e) {}
	}
	private void loadValues() {
		loadAge();
		loadHeight();
		loadProfilePicture();
		loadName();
		loadWeight();
		applyBMI();
	}
	private void loadAge() {
		String t_age = adapter.get_setting("age");
		if (t_age!=null) {
			settingprofile_age.setText(t_age);
		}
	}
	private void loadWeight() {
		String t_weight = adapter.get_setting("weight");
		if (t_weight!=null) {
			settingprofile_weight.setText(t_weight);
		}
	}
	private void loadHeight() {
		String t_height = adapter.get_setting("height");
		if (t_height!=null) {
			settingprofile_height.setText(t_height);
		}
	}

	private void loadProfilePicture() {
		File newfile = new File(getApplicationContext().getExternalFilesDir(
				Environment.DIRECTORY_DOWNLOADS), "profilepicture.png");
		if (newfile.isFile()) {
			settingprofile_photo.setImageBitmap(BitmapFactory
					.decodeFile(newfile + ""));
		}
	}

	private void loadName() {
		String name = adapter.get_setting("name");
		if (name != null) {
			settingprofile_name.setText(name);
		}
	}


	
	
	
	
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what) {
			case 0:
				applyBMI();
				break;
			default:
				break;
			}
		}
	};
	
	
	
	
	
	
	

	/*
	 * 갤러리에서 사진을 불러온후 처리하여 디비에 저장.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectImageUri = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor imageCursor = this.getContentResolver().query(
					selectImageUri, filePathColumn, null, null, null);
			imageCursor.moveToFirst();
			int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
			String imagePath = imageCursor.getString(columnIndex);

			imageCursor.close();

			Bitmap galleryImg = BitmapFactory.decodeFile(imagePath);
			Bitmap squareImg = Utils.getSquareBitmap(galleryImg);
			Bitmap scaledImg = Bitmap.createScaledBitmap(squareImg, 300, 300,
					true);
			Bitmap roundedImg = Utils.getRoundedBitmap(scaledImg);
			File newfile = new File(getApplicationContext()
					.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
					"profilepicture.png");
			if (newfile.isFile()) {
				newfile.delete();
			}
			try {
				OutputStream output = new FileOutputStream(newfile);

				roundedImg.compress(Bitmap.CompressFormat.PNG, 100, output);

				// output.write(Utils.bitmapToByteArray(roundedImg));
				settingprofile_photo.setImageBitmap(roundedImg);
				output.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		setResult(RESULT_OK);
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		setResult(RESULT_OK);
		finish();
		return false;
	}
	@Override
	protected void onResume() {
		this.overridePendingTransition(0,0); 
		super.onResume();
	}
}
