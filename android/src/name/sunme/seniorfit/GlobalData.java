package name.sunme.seniorfit;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import name.sunme.functionactivity.OtherProgramItem;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;

public class GlobalData {
	static String TAG = "GlobalData";
	static HashMap<String, Integer> backgroundMap = new HashMap<String, Integer>(){{
		put("a_1",R.drawable.bg_a_1);
		put("b_1",R.drawable.bg_b_1);
		put("b_2",R.drawable.bg_b_2);
		put("b_3",R.drawable.bg_b_3);
		put("b_4",R.drawable.bg_b_4);
		put("b_5",R.drawable.bg_b_5);
		put("c_1",R.drawable.bg_c_1);
		put("c_2",R.drawable.bg_c_2);
		put("c_3",R.drawable.bg_c_3);
		put("c_4",R.drawable.bg_c_4);
		put("c_5",R.drawable.bg_c_5);
		put("d_1",R.drawable.bg_d_1);
		put("d_2",R.drawable.bg_d_2);
		put("d_3",R.drawable.bg_d_3);
		put("d_4",R.drawable.bg_d_4);
		put("d_5",R.drawable.bg_d_5);
		put("e_1",R.drawable.bg_e_1);
		put("e_2",R.drawable.bg_e_2);
		put("e_3",R.drawable.bg_e_3);
		put("f_1",R.drawable.bg_f_1);
	}};
	public static int getBackground(String subMenuId) {
		if (!backgroundMap.containsKey(subMenuId)) return R.drawable.bg_a_1;
		return backgroundMap.get(subMenuId);
	}
	static OtherProgramItem[] opis = null;
	public static OtherProgramItem[] getOtherProgramItem(Context context) {
		if (opis == null) {
			DBAdapter dbAdapter = new DBAdapter(context);
			opis = new OtherProgramItem[7];

			FitApiDataClass[] fads1 = dbAdapter
					.get_fitapidata_fromMainMenuId("DayWorkingout");
			opis[0] = new OtherProgramItem("하루 운동", "2시간", "주 1-2회", fads1,
					R.drawable.otherprogram_bg1, false);

			FitApiDataClass[] fads2 = dbAdapter.get_fitapidata_fromMainMenuId("a");
			opis[1] = new OtherProgramItem("준비운동", "15분", "주 1-2회", fads2,
					R.drawable.otherprogram_bg2, false);

			FitApiDataClass[] fads3 = dbAdapter.get_fitapidata_fromMainMenuId("b");
			opis[2] = new OtherProgramItem("근력강화", "25분", "주 2-3회", fads3,
					R.drawable.otherprogram_bg3, false);

			FitApiDataClass[] fads4 = dbAdapter.get_fitapidata_fromMainMenuId("c");
			opis[3] = new OtherProgramItem("심폐 지구력 강화", "45분", "주 2-3회", fads4,
					R.drawable.otherprogram_bg4, false);

			FitApiDataClass[] fads5 = dbAdapter.get_fitapidata_fromMainMenuId("d");
			opis[4] = new OtherProgramItem("유연성강화", "15분", "주 2-3회", fads5,
					R.drawable.otherprogram_bg5, false);

			FitApiDataClass[] fads6 = dbAdapter.get_fitapidata_fromMainMenuId("e");
			opis[5] = new OtherProgramItem("평행성/체력강화", "25분", "주 2-3회", fads6,
					R.drawable.otherprogram_bg6, false);

			FitApiDataClass[] fads7 = dbAdapter.get_fitapidata_fromMainMenuId("f");
			opis[6] = new OtherProgramItem("정리운동", "15분", "운동 후", fads7,
					R.drawable.otherprogram_bg7, false);
			
			String myProgram = dbAdapter.get_setting("myProgram");
			for (int i = 0; i < opis.length; i++) {
				Log.d(TAG, "title : "+opis[i].title);
				if (opis[i].title.equals(myProgram)) {
					opis[i].isTodays = true;
				}
			}
		} else {
			for (int i = 0; i < opis.length; i++) {
				Log.d(TAG, "else title : "+opis[i].title);
			}
		}
		return opis;
	} 
	public static Integer[] getThumbnail(String subMenuId) {
		try {
			return thumbnailMap.get(subMenuId);
		} catch (Exception e) {
			return new Integer[]{R.drawable.temp_thumbnail};
		}
	}
	
	public static HashMap<String, Integer[]> thumbnailMap = new HashMap<String, Integer[]>(){{
		put("a_1",new Integer[]{
				R.drawable.thumbnail_a_1_1,
				R.drawable.thumbnail_a_1_2,
				R.drawable.thumbnail_a_1_3,
				R.drawable.thumbnail_a_1_4,
				R.drawable.thumbnail_a_1_5,
				R.drawable.thumbnail_a_1_6,
				R.drawable.thumbnail_a_1_7,
				R.drawable.thumbnail_a_1_8});
		put("b_1",new Integer[]{
				R.drawable.thumbnail_b_1_1,
				R.drawable.thumbnail_b_1_2,
				R.drawable.thumbnail_b_1_3,
				R.drawable.thumbnail_b_1_4});
		put("b_2",new Integer[]{R.drawable.thumbnail_b_2});
		put("b_3",new Integer[]{R.drawable.thumbnail_b_3});
		put("b_4",new Integer[]{R.drawable.thumbnail_b_4});
		put("b_5",new Integer[]{R.drawable.thumbnail_b_5});
		put("c_1",new Integer[]{R.drawable.thumbnail_c_1});
		put("c_2",new Integer[]{R.drawable.thumbnail_c_2});
		put("c_3",new Integer[]{R.drawable.thumbnail_c_3});
		put("c_4",new Integer[]{R.drawable.thumbnail_c_4});
		put("c_5",new Integer[]{R.drawable.thumbnail_c_5});
		put("d_1",new Integer[]{
				R.drawable.thumbnail_d_1_1,
				R.drawable.thumbnail_d_1_2,
				R.drawable.thumbnail_d_1_3});
		put("d_2",new Integer[]{R.drawable.thumbnail_d_2});
		put("d_3",new Integer[]{R.drawable.thumbnail_d_3});
		put("d_4",new Integer[]{R.drawable.thumbnail_d_4});
		put("d_5",new Integer[]{R.drawable.thumbnail_d_5});
		put("e_1",new Integer[]{R.drawable.thumbnail_e_1});
		put("e_2",new Integer[]{R.drawable.thumbnail_e_2});
		put("e_3",new Integer[]{R.drawable.thumbnail_e_3});
		put("f_1",new Integer[]{R.drawable.thumbnail_d_5});
	}};
}
