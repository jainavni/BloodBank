package com.example.gagan.bloodbank;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int totaltab;
    Context context;
    public ViewPagerAdapter(FragmentManager fm,int totaltab,Context context) {
        super(fm);
        this.totaltab=totaltab;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                DonerFragment donerFragment=new DonerFragment();
                return donerFragment;
            case 1:
                ReciverFragment reciverFragment=new ReciverFragment();
                return reciverFragment;
            case 2:
                BloodBankFragment bloodBankFragment=new BloodBankFragment();
                return bloodBankFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totaltab;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Donar";
            case 1:
                return "Reciver";
            case 3:
                return "Blood Bank";
            default:
                return null;
        }

    }
}

//ab mujhe ye bata ki ye kaha se call karana hai matlab ye view pager kon se activitt ke baad lana chata hai
