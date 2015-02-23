package name.sunme.seniorfit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import net.htmlparser.jericho.Source;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class UrlOpenerProgress {
	String TAG = "UrlOpener";
	private String url;
	private Source source;
	private Context context;
	private ProgressDialog progressDialog;
	private Handler mainhandler; 
	
	public UrlOpenerProgress(Context pcontext, Handler phandler_main, String url) {
		this.context = pcontext;
		this.mainhandler = phandler_main; 
		this.url = url;
	}
	
	public void open()
	{
		try
		{
			process();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void process() throws IOException
	{
		final Handler mHandler = new Handler();

		new Thread()
		{

			@Override
			public void run()
			{
				URL nURL; 
				try
				{
					nURL = new URL(url);
					
					
					mHandler.post(new Runnable(){
						@Override
						public void run()
						{ 
							progressDialog = ProgressDialog.show(context, "", "Data loding...");
						}
					});
					 
					
					//InputStream html = nURL.openStream();//instead
					
					URLConnection c = nURL.openConnection();
				    c.setConnectTimeout(5000);
				    c.setReadTimeout(5000); 
				    BufferedInputStream bufferdata = new BufferedInputStream(c.getInputStream());
					source = new Source(new InputStreamReader(bufferdata, "utf-8"));
					
					
					
					if (mainhandler != null) {
						Bundle bundle = new Bundle();
						Message msg =  new Message();
						bundle.putString("result", source.toString());
						msg.setData(bundle);
						msg.what = 1;
						mainhandler.sendMessage(msg);
					}
					
					mHandler.post(new Runnable() {
						public void run() {
							progressDialog.cancel();
							Toast.makeText (context, "API 데이터를 받았습니다.", 3).show();
						}
					});
										
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (Exception e) {
					mHandler.post(new Runnable() {
						public void run() {
							progressDialog.cancel(); 
						}
					}); 
					if (mainhandler != null) {
						mainhandler.sendEmptyMessage(-1);
					}
					e.printStackTrace();
				} finally {
					
				}
			}
		}.start();
	}

}
