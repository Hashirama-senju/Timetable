package com.example.msa.timetable.Data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.msa.timetable.Model.Period;
import com.example.msa.timetable.R;

import java.util.List;

public class PeriodAdaptor extends ArrayAdapter<Period> {
    private Activity context;
    private List<Period> periodlist;

    public PeriodAdaptor(Activity context,List<Period> periodlist) {
        super(context,R.layout.period_row,periodlist);
        this.context = context;
        this.periodlist = periodlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View itemviewlist = inflater.inflate(R.layout.period_row,null,true);
        TextView starttime = (TextView) itemviewlist.findViewById(R.id.starttime);
        TextView endtime = (TextView) itemviewlist.findViewById(R.id.endtime);
        TextView periodname = (TextView) itemviewlist.findViewById(R.id.periodname);
        TextView teachername = (TextView) itemviewlist.findViewById(R.id.teachertext);
        TextView place = (TextView) itemviewlist.findViewById(R.id.placetext);

        Period period = periodlist.get(position);

        starttime.setText(period.getSt());
        endtime.setText(period.getEt());
        periodname.setText(period.getPn());
        teachername.setText(period.getTn());
        place.setText(period.getPl());

        return itemviewlist;
    }
}
