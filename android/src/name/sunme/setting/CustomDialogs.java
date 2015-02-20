package name.sunme.setting;

import name.sunme.maindrawbar.R;
import name.sunme.seniorfit.DBAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CustomDialogs {

	public static void change_age(Context context, final TextView textview) {//this
		final DBAdapter dbAdapter = new DBAdapter(context);
		
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		final View layout = inflater.inflate(
				R.layout.custom_numberpicker_dialog,
				(ViewGroup) ((Activity) context).findViewById(R.id.layout_root_numberpicker));
		final NumberPicker dialogNumberPicker = (NumberPicker) layout
				.findViewById(R.id.customNumberPicker);

		dialogNumberPicker.setMinValue(50);
		dialogNumberPicker.setMaxValue(150);

		final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
				context);
		InputDialogbuilder
				.setTitle("나이 입력")
				.setView(layout)
				.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker
												.getValue());
								dbAdapter.put_setting("age", myInputText);
								if (textview!=null)textview.setText(myInputText);
							}
						}).setNegativeButton("취소", null).create().show();
	}
	public static void change_weight(Context context, final TextView textview, final Handler mHandler) {//this
		final DBAdapter dbAdapter = new DBAdapter(context);
		
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		final View layout = inflater.inflate(
				R.layout.custom_numberpicker_dialog,
				(ViewGroup) ((Activity) context).findViewById(R.id.layout_root_numberpicker));
		final NumberPicker dialogNumberPicker = (NumberPicker) layout
				.findViewById(R.id.customNumberPicker);

		dialogNumberPicker.setMinValue(30);
		dialogNumberPicker.setMaxValue(150);

		final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
				context);
		InputDialogbuilder
				.setTitle("무게 입력")
				.setView(layout)
				.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker
												.getValue());
								dbAdapter.put_setting("weight", myInputText);
								if (textview!=null)textview.setText(myInputText);
								if (mHandler!=null) mHandler.sendEmptyMessage(0);
							}
						}).setNegativeButton("취소", null).create().show();
	}
	
	public static void change_height(Context context, final TextView textview, final Handler mHandler) {//this
		final DBAdapter dbAdapter = new DBAdapter(context);
		
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		final View layout = inflater.inflate(
				R.layout.custom_numberpicker_dialog,
				(ViewGroup) ((Activity) context).findViewById(R.id.layout_root_numberpicker));
		final NumberPicker dialogNumberPicker = (NumberPicker) layout
				.findViewById(R.id.customNumberPicker);

		dialogNumberPicker.setMinValue(100);
		dialogNumberPicker.setMaxValue(200);

		final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
				context);
		InputDialogbuilder
				.setTitle("키 입력")
				.setView(layout)
				.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = Integer
										.toString(dialogNumberPicker
												.getValue());
								dbAdapter.put_setting("height", myInputText);
								if (textview!=null)textview.setText(myInputText);
								if (mHandler!=null) mHandler.sendEmptyMessage(0);
							}
						}).setNegativeButton("취소", null).create().show();
	}
	
	public static void change_name(Context context, final TextView textview) {//this
		final DBAdapter dbAdapter = new DBAdapter(context);
		
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(
				R.layout.custom_textedit_dialog,
				(ViewGroup) ((Activity) context).findViewById(R.id.layout_root_textedit));
		final EditText dialogEdit = (EditText) layout
				.findViewById(R.id.customEditText);
		final AlertDialog.Builder InputDialogbuilder = new AlertDialog.Builder(
				context);
		InputDialogbuilder
				.setTitle("이름 입력")
				.setView(layout)
				.setPositiveButton("입력",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String myInputText = dialogEdit.getText()
										.toString();
								dbAdapter.put_setting("name", myInputText);
								textview.setText(myInputText);
							}
						}).setNegativeButton("취소", null).create().show();
	}
}
