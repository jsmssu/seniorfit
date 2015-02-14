package name.sunme.setting;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import name.sunme.firstexecution.Setup1Activity;
import name.sunme.firstexecution.Setup3Activity;
import name.sunme.maindrawbar.ActivityStyle;
import name.sunme.seniorfit.DBAdapter;
import name.sunme.seniorfit.DBHelper;
import name.sunme.seniorfit.Utils;

import com.example.seniorfit.R;
import com.example.seniorfit.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingProfileActivity extends Activity {

	final int REQUEST_CODE_IMAGE = 1;
	ImageView settingprofile_photo;
	ImageView settingprofile_weight;
	TextView settingprofile_name;
	
	private DBHelper helper;
	private DBAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_profile);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
		helper = new DBHelper(getApplicationContext());
		adapter = new DBAdapter(getApplicationContext());
		
        
        settingprofile_photo = (ImageView)findViewById(R.id.settingprofile_photo);
        settingprofile_name = (TextView)findViewById(R.id.settingprofile_name);
        settingprofile_weight = (ImageView)findViewById(R.id.settingprofile_weight);
        settingprofile_weight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SettingBMIActivity.class);
				startActivity(intent);
			}
		});
        
        settingprofile_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
			}
		});
        
        settingprofile_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.custom_textedit_dialog,(ViewGroup) findViewById(R.id.layout_root));
                final EditText dialogEdit = (EditText)layout.findViewById(R.id.customEditText);
                final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(SettingProfileActivity.this);
                InputDialogbuilder.setTitle("이름 입력");
                InputDialogbuilder.setView(layout);
                InputDialogbuilder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String myInputText = dialogEdit.getText().toString();
                        adapter.put_setting("name", myInputText);
                        settingprofile_name.setText(myInputText);
                    }
                });
                InputDialogbuilder.setNegativeButton("취소", null);
                final AlertDialog InputDialog = InputDialogbuilder.create();
                InputDialog.show();
            }
        });
        
        loadValues();
	}
	private void loadValues() {
		loadProfilePicture();
		loadName();
	}
	private void loadProfilePicture() {
		File newfile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"profilepicture.png");
        if (newfile.isFile()) {
        	settingprofile_photo.setImageBitmap(BitmapFactory.decodeFile(newfile+""));
        }
	}
	private void loadName() {
		String name = adapter.get_setting("name");
		if (name != null) {settingprofile_name.setText(name);}
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }
	
	/*
     *갤러리에서 사진을 불러온후 처리하여 디비에 저장.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor imageCursor = this.getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
            imageCursor.moveToFirst();
            int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
            String imagePath = imageCursor.getString(columnIndex);

            imageCursor.close();

            Bitmap galleryImg = BitmapFactory.decodeFile(imagePath);
            Bitmap squareImg = Utils.getSquareBitmap(galleryImg);
            Bitmap scaledImg = Bitmap.createScaledBitmap(squareImg, 300, 300, true);
            Bitmap roundedImg = Utils.getRoundedBitmap(scaledImg);
            
            
            
            
            File newfile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"profilepicture.png");
            if (newfile.isFile()) {
            	newfile.delete();
            }
            try {
				OutputStream output = new FileOutputStream(newfile);
				
				roundedImg.compress(Bitmap.CompressFormat.PNG, 100, output);
				
				//output.write(Utils.bitmapToByteArray(roundedImg));
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
}
