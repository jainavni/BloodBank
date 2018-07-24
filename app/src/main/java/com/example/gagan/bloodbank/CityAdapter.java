package com.example.gagan.bloodbank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CityAdapter extends ArrayAdapter<Cities> {
    private List<Cities> citylist;

    public CityAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull  List<Cities> citylist) {
        super(context, resource, textViewResourceId, citylist);
        this.citylist = citylist;
    }

    @Nullable
    @Override
    public Cities getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    private View initView(int position)
    {

        Cities cities=getItem(position);
        LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.state_list, null);
        TextView textView =  v.findViewById(R.id.spinnerText);
        textView.setText(cities.getState());
        return v;
    }
}
