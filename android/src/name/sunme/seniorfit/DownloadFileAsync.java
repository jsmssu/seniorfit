package name.sunme.seniorfit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class DownloadFileAsync extends AsyncTask<String, String, String> {
	String TAG = "DownloadFileAsync";
	private ProgressDialog mprogressdialog;
	private Context mContext;
	private String path_myapp;
	private Handler downloadendhandler = null;
	//  
	public DownloadFileAsync(Context context, Handler handler) {
		this.downloadendhandler = handler;
		mContext = context; 
		Log.d(TAG, "make dir : " + mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
		makeDirectory(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
		path_myapp = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "";
	}
	public DownloadFileAsync(Context context) { 
		mContext = context; 
		Log.d(TAG, "make dir : " + mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
		makeDirectory(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
		path_myapp = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "";
	}
	@Override
	protected void onPreExecute() {
		mprogressdialog = new ProgressDialog(mContext);
		mprogressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mprogressdialog.setMessage("Start");
		mprogressdialog.show();

		super.onPreExecute();
	}
	
	
	@Override
	protected String doInBackground(String... params) { 
		for (String url_str : params) {	 
			Log.d(TAG, "URL : " + url_str);
			String filename = url_str.substring(url_str.lastIndexOf('/')+1);
			Log.d(TAG, "FILE NAME : " + filename);
			if (url_str == null) {
				Log.d(TAG, "url is null, "+url_str);
				continue;
			}
			try {
				Thread.sleep(100);
				
				File newfile = new File(path_myapp+"/"+filename);
				if(newfile==null||newfile.exists()) {
					Log.d(TAG, filename + " is exists");
					mprogressdialog.setMax(0);
					publishProgress("progress", "1", filename + " is exists");
					continue;
				}
				
				
				URL turl = new URL(url_str);
				
				URLConnection turlconnection = turl.openConnection();
				turlconnection.connect(); 
				int length_file = turlconnection.getContentLength(); //get file lenght
				mprogressdialog.setMax(length_file);//set max number from file size
				Log.d(TAG, "file Lenght : " + length_file);

				InputStream input = new BufferedInputStream(turl.openStream());
				
				//OutputStream output = new FileOutputStream(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+filename);
				
				
				OutputStream output = new FileOutputStream(newfile);
				//OutputStream output = new FileOutputStream(path_myapp+"/"+filename);
				
				Log.d(TAG, "OUTPUT NAME : " + path_myapp+"/"+filename);
				byte data[] = new byte[1024];
				
				long total = 0;
				int count = 0;
				while ((count = input.read(data)) != -1) {
					Log.d(TAG, total+", / "+length_file);
					total += count; 
					publishProgress("progress", total+"", "download "+filename);
					output.write(data, 0, count);
				}
				Log.d(TAG, path_myapp+"");
				output.flush();
				Log.d(TAG, "OUTPUT flush");
				output.close();
				Log.d(TAG, "OUTPUT close");
				input.close(); 
				Log.d(TAG, "INPUT close");
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		} 
		if (downloadendhandler != null) {
			downloadendhandler.sendEmptyMessage(0);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		if (progress[0].equals("progress")) {
			mprogressdialog.setProgress(Integer.parseInt(progress[1]));
			mprogressdialog.setMessage(progress[2]);
		} /*else if (progress[0].equals("max")) {
			mprogressdialog.setMax(Integer.parseInt(progress[1]));
		}*/
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(String unused) {
		mprogressdialog.dismiss();
	}
	
	private File makeDirectory(File dir) {
        //File dir = new File(dir_path);
        if (!dir.exists()) {
            dir.mkdirs();
            Log.i( TAG , "!dir.exists" );
        } else {
            Log.i( TAG , "dir.exists" );
        } 
        return dir;
    }
}
