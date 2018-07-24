package com.example.gagan.bloodbank;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPrefrence {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    UserSharedPrefrence(Context context)
    {
        sharedPreferences=context.getSharedPreferences("mydata",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public Boolean getFirst_time_opening() {
        return sharedPreferences.getBoolean("first_time",true);
    }

    public void setFirst_time_opening(Boolean first_time_opening) {
        editor.putBoolean("first_time",first_time_opening);
        editor.commit();
    }
}
