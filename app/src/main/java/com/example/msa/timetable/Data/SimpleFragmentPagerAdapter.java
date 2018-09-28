package com.example.msa.timetable.Data;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.msa.timetable.Fragments.FridayFragment;
import com.example.msa.timetable.Fragments.MondayFragment;
import com.example.msa.timetable.Fragments.SaturdayFragment;
import com.example.msa.timetable.Fragments.ThursdayFragment;
import com.example.msa.timetable.Fragments.TuesdayFragment;
import com.example.msa.timetable.Fragments.WednesdayFragment;

import java.util.Calendar;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter{
    private String tabTitle[] = new String[]{"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_WEEK);

    public SimpleFragmentPagerAdapter(FragmentManager fragmentManager) {
     super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new MondayFragment();
        }
        else if(position == 1){
            return new TuesdayFragment();
        }
        else if(position == 2){
            return new WednesdayFragment();
        }
        else if(position == 3){
            return new ThursdayFragment();
        }
        else if(position == 4){
            return new FridayFragment();
        }
        else if (position==5){
            return new SaturdayFragment();
        }
        return new MondayFragment();

    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
