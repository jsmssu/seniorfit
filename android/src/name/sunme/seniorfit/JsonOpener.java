package name.sunme.seniorfit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.htmlparser.jericho.Source;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class JsonOpener {
	String TAG = "JsonOpener";
	private String url;
	private Source source;
	private Context context;
	private ProgressDialog progressDialog;
	private Handler mainhandler;

	public JsonOpener(Context pcontext, Handler mainhandler, String url) {
		this.context = pcontext;
		this.mainhandler = mainhandler;
		this.url = url;
	}

	public void open() {
		try {
			process();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void process() throws IOException {

		new Thread() {

			@Override
			public void run() {
				URL nURL;
				try {
					nURL = new URL(url);

					// InputStream html = nURL.openStream();//instead

					URLConnection c = nURL.openConnection();
					c.setConnectTimeout(5000);
					c.setReadTimeout(5000);
					BufferedInputStream bufferdata = new BufferedInputStream(
							c.getInputStream());
					source = new Source(new InputStreamReader(bufferdata,
							"utf-8"));

					if (mainhandler != null) {
						Bundle bundle = new Bundle();
						Message msg = new Message();
						bundle.putString("result", source.toString());
						msg.setData(bundle);
						msg.what = 1;
						mainhandler.sendMessage(msg);
					}
				} catch (Exception e) {
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