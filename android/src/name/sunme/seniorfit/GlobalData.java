package name.sunme.seniorfit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import name.sunme.maindrawbar.R;
import name.sunme.maindrawbar.R.drawable;
import name.sunme.workingout.FitDetail_MainMenuTitleActivity;
import name.sunme.workingout.OtherProgramItem;

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
			
			
			FitApiDataClass rest = new FitApiDataClass();
			rest._subMenuTitle = "�޽�";
			FitApiDataClass running = new FitApiDataClass();
			running._subMenuTitle = "����";
			
			
			DBAdapter dbAdapter = new DBAdapter(context);
			opis = new OtherProgramItem[9];

			//a
			FitApiDataClass[] fads_a = dbAdapter.get_fitapidata_fromMainMenuId("a");
			FitApiDataClass[] fads_b = dbAdapter.get_fitapidata_fromMainMenuId("b");
			FitApiDataClass[] fads_c = dbAdapter.get_fitapidata_fromMainMenuId("c");
			FitApiDataClass[] fads_d = dbAdapter.get_fitapidata_fromMainMenuId("d");
			FitApiDataClass[] fads_e = dbAdapter.get_fitapidata_fromMainMenuId("e");
			FitApiDataClass[] fads_f = dbAdapter.get_fitapidata_fromMainMenuId("f");
			FitApiDataClass[] fads1d = concatFits(fads_b, fads_c, fads_d, fads_e);
			Log.d(TAG,"test2");
			
			opis[0] = new OtherProgramItem("�Ϸ� �", "2�ð�", "��2ȸ����", fads1d,
					R.drawable.otherprogram_bg_1d, false);
			int dayOfYear = (Calendar.getInstance()).get(Calendar.DAY_OF_YEAR);
			FitApiDataClass[] fadsw5 = null;
			Log.d(TAG, ""+dayOfYear%7);
			switch (dayOfYear%7) {
            case 0:
                try { fadsw5 = concatFit(fads_a[0],fads_c[0],fads_b[0],fads_e[4],fads_d[0],fads_e[5]); } catch (Exception e) {}
                break;
            case 1:
                try { fadsw5 = concatFit(fads_a[0],fads_c[1],fads_c[2],fads_d[1],fads_d[2],fads_d[4], running); } catch (Exception e) {}
                break;
            case 2:
                try { fadsw5 = concatFit(fads_a[0],fads_c[0],fads_b[3],fads_b[4],fads_e[3],fads_e[4]); } catch (Exception e) {}
                break;
            case 3:
                try { fadsw5 = concatFit(fads_a[0],fads_c[3],fads_b[4],fads_d[4],fads_d[0],running); } catch (Exception e) {}
                break; 
            case 4:
                try { fadsw5 = concatFit(fads_a[0],fads_c[0],fads_b[0],fads_e[3],fads_d[0],fads_e[4]); } catch (Exception e) {}
                break;
            case 5:
                try { fadsw5 = concatFit(rest); } catch (Exception e) {}
                break;
            case 6:
                fadsw5 = concatFit(running);
                break;
			default:
				fadsw5 = concatFit(rest);
				break;
			}
			Log.d(TAG,"test344");
			opis[1] = new OtherProgramItem("��5ȸ�", "1�ð���", "��5ȸ", fadsw5,
					R.drawable.otherprogram_bg_w5, false);
			
			FitApiDataClass[] fadsw3 = null;
			switch (dayOfYear%14) {
            case 0:
                try { fadsw3 = concatFit(fads_a[0],fads_b[1],fads_e[1],fads_e[4]); } catch (Exception e) {}
                break;
            case 2:
                try { fadsw3 = concatFit(fads_a[0],fads_c[1],fads_c[2],fads_d[1],fads_d[2],fads_e[3],fads_e[5]); } catch (Exception e) {}
                break;
            case 4:
                try { fadsw3 = concatFit(fads_a[0],fads_b[0],fads_d[0],fads_e[3]); } catch (Exception e) {}
                break;
            case 7:
                try { fadsw3 = concatFit(fads_a[0],fads_c[0],fads_b[3],fads_e[1],fads_e[4]); } catch (Exception e) {}
                break;
            case 9://a_1 c_4 c_5 d_4 d_5 e_1 e_3 e_5
                try { fadsw3 = concatFit(fads_a[0],fads_c[3],fads_c[4],fads_d[3],fads_d[4],fads_e[0],fads_e[2],fads_e[4]); } catch (Exception e) {}
                break;
            case 11:
                try { fadsw3 = concatFit(fads_a[0],fads_b[0],fads_d[0],fads_e[3],running); } catch (Exception e) {}
                break;
			default:
				fadsw3 = concatFit(rest);//concatFit(rest);
				break;
			}
			opis[2] = new OtherProgramItem("��3ȸ�", "1�ð���", "��3ȸ", fadsw3,
					R.drawable.otherprogram_bg_w3, false);
			
			Log.d(TAG,"test3");
			opis[3] = new OtherProgramItem("�غ�", "15��", "�� 1-2ȸ", fads_a,
					R.drawable.otherprogram_bg_a, false);
			//b
			
			opis[4] = new OtherProgramItem("�ٷ°�ȭ", "25��", "�� 2-3ȸ", fads_b,
					R.drawable.otherprogram_bg_b, false);
			//c
			
			opis[5] = new OtherProgramItem("���� ������ ��ȭ", "45��", "�� 2-3ȸ", fads_c,
					R.drawable.otherprogram_bg_c, false);
			//d
			
			opis[6] = new OtherProgramItem("��������ȭ", "15��", "�� 2-3ȸ", fads_d,
					R.drawable.otherprogram_bg_d, false);
			//e
			
			opis[7] = new OtherProgramItem("���༺/ü�°�ȭ", "25��", "�� 2-3ȸ", fads_e,
					R.drawable.otherprogram_bg_e, false);
			//f
			
			opis[8] = new OtherProgramItem("�����", "15��", "� ��", fads_f,
					R.drawable.otherprogram_bg_f, false);
			
			//1d - b,c,d,e
			 
			Log.d(TAG,"test1");
			
			
			
			
			String myProgram = dbAdapter.get_setting("myProgram");
			for (int i = 0; i < opis.length; i++) {
				Log.d(TAG, "title : "+opis[i].title);
				if (opis[i].title.equals(myProgram)) {
					opis[i].isTodays = true;
				}
			}Log.d(TAG,"test1111");
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
	
	public static JSONArray otherprogram_programjson = null;
	
	public static HashMap<String, Integer[]> otherprogram_programlist = new HashMap<String, Integer[]>(){{
	}};
	
	
	public static FitApiDataClass[] concatFits(FitApiDataClass[]... params) {
		int t_len = 0;
		for (FitApiDataClass[] fitds : params) {
			t_len += fitds.length;
		}
		FitApiDataClass[] result = new FitApiDataClass[t_len];
		int j=0;
		for (FitApiDataClass[] fitds : params) {
			for (int i = 0; i < fitds.length; i++, j++) {
				result[j]=fitds[i];
			}
		}
		return result;
	}
	public static FitApiDataClass[] concatFit(FitApiDataClass... params) {
		Log.d(TAG,"paramlen : " +params.length);
		FitApiDataClass[] result = new FitApiDataClass[params.length];
		Log.d(TAG,"paramlen : " +params.length);
		for(int i=0; i<params.length; i++) {
			Log.d(TAG,"paramlen : " +params.length+", i : "+i);
			result[i] = params[i];
		}
		return result;
	}
}
