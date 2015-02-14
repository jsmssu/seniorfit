package name.sunme.seniorfit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ImageView;

import com.example.seniorfit.R;

public class TodaysWorkingoutList { 
	
	HashMap<Integer, ArrayList<String>> perOneWeek;
	ArrayList<String> perOneWeek_sun;
	ArrayList<String> perOneWeek_mon;
	ArrayList<String> perOneWeek_tue;
	ArrayList<String> perOneWeek_wed;
	ArrayList<String> perOneWeek_thu;
	ArrayList<String> perOneWeek_fri;
	ArrayList<String> perOneWeek_sat;
	
	
	public TodaysWorkingoutList() { 
		perOneWeek_sun = new ArrayList<String>(){{//1
            add("a_1");
        }};
        
        perOneWeek_mon = new ArrayList<String>(){{//2
            add("a_1");
            add("c_1");
            add("b_3");
            add("e_3");
            add("e_5");
        }};
        
        perOneWeek_tue = new ArrayList<String>(){{//3
            add("a_1");
            add("c_2");
            add("c_3");
            add("d_2");
            add("d_3");
            add("d_4");
            add("Á¶±ë");
        }};

        
        perOneWeek_wed = new ArrayList<String>(){{//4
            add("a_1");
            add("c_1");
            add("b_4");
            add("b_5");
            add("e_4");
            add("e_5"); 
        }};
        perOneWeek_thu = new ArrayList<String>(){{//5
            add("a_1");
            add("c_4");
            add("c_5");
            add("d_5");
            add("d_1");
            add("Á¶±ë"); 
        }};
        perOneWeek_fri = new ArrayList<String>(){{//6
            add("a_1");
            add("c_1");
            add("b_1");
            add("e_4");
            add("d_1");
            add("e_5±ë"); 
        }};
        perOneWeek_sat = new ArrayList<String>(){{//7
            add("ÈÞ½Ä"); 
        }};
        
        perOneWeek.put(1, perOneWeek_sun);
        perOneWeek.put(2, perOneWeek_mon);
        perOneWeek.put(3, perOneWeek_tue);
        perOneWeek.put(4, perOneWeek_wed);
        perOneWeek.put(5, perOneWeek_thu);
        perOneWeek.put(6, perOneWeek_fri);
        perOneWeek.put(7, perOneWeek_sat);
		
		
	}
	public ArrayList<String> getTodayWorkingout_perOneWeek() {
		Calendar cal= Calendar.getInstance ();  
		return perOneWeek.get(cal.get( Calendar.DAY_OF_WEEK ));
	}
}
