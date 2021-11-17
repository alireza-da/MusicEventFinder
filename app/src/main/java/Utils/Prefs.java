package Utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity){
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public void setChartType(String chartType){
        sharedPreferences.edit().putString("chart_type", chartType).apply();
    }

    public String getChartType(){
        return sharedPreferences.getString("chart_type", "Artist");
    }
}
