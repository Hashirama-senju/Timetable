package com.example.msa.timetable.Fragments;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.msa.timetable.Data.PeriodAdaptor;
import com.example.msa.timetable.Model.Period;
import com.example.msa.timetable.R;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WednesdayFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference weekdayReference;
    private ChildEventListener mChildEventListner;
    private PeriodAdaptor mPeriodAdaptor;
    List<Period> periods;
    private ListView listView;
    String typeofperiod;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int currentHour;
    int currentMinute;
    String amPm;


    public WednesdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.activity_list, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        weekdayReference = FirebaseDatabase.getInstance().getReference().child("Timetable").child("Wednesday");

        periods = new ArrayList<>();
        mPeriodAdaptor= new PeriodAdaptor(getActivity(),periods);
        listView.setAdapter(mPeriodAdaptor);


        //ON Long Press Show the Option
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Period period = periods.get(position);
                showDialog(period);
                return false;
            }
        });


//
        return view;
    }

    private void showDialog(final Period period) {
        //Alert Dialog Builder
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View dialogview = layoutInflater.inflate(R.layout.update_dialog,null);
        dialogbuilder.setView(dialogview);
        dialogbuilder.setCancelable(true);

        //Reference the Input
        final EditText periodnametext = (EditText) dialogview.findViewById(R.id.pnameEdittext);
        final EditText teachernametext = (EditText) dialogview.findViewById(R.id.teacherEdittext);
        final EditText placetext = (EditText) dialogview.findViewById(R.id.placeEditText);
        final TextView starttimetext = (TextView) dialogview.findViewById(R.id.starttimetext);
        final TextView endtimetext = (TextView) dialogview.findViewById(R.id.endtimestart);
        final Button updatebtn = (Button) dialogview.findViewById(R.id.updatebtn);
        final Button deletebtn  = (Button) dialogview.findViewById(R.id.deletebtn);


        //Horizontal Picker For TypeofPeriod
        final HorizontalPicker periodtype = (HorizontalPicker) dialogview.findViewById(R.id.typepicker);
        final List<HorizontalPicker.PickerItem> typetextItems = new ArrayList<>();
        final String ptype[] = getResources().getStringArray(R.array.period_type);
        for(int i=0;i<=1;i++){
            typetextItems.add(new HorizontalPicker.TextItem(ptype[i]));
        }
        periodtype.setItems(typetextItems,0); //3 here signifies the default selected item. Use : daypicker.setItems(daytextItems) if none of the items are selected by default.

        //Repopulate the Period Value
        periodnametext.setText(period.getPn());
        teachernametext.setText(period.getTn());
        placetext.setText(period.getPl());
        starttimetext.setText(period.getSt());
        endtimetext.setText(period.getEt());


        //Dialogue Builder
        dialogbuilder.setTitle("Edit Period");
        final AlertDialog alertDialog = dialogbuilder.create();
        alertDialog.show();


        //onClickListener
        //Starttime Dialog
        starttimetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settime(starttimetext);
            }
        });
        //End Time Dialog
        endtimetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settime(endtimetext);
            }
        });
        //Picker Dialog
        periodtype.setChangeListener(new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker horizontalPicker, int i) {
                HorizontalPicker.PickerItem selected = horizontalPicker.getSelectedItem();
                switch (selected.getText().toString()){
                    case "Theory":
                        typeofperiod = "Theory";

                        break;
                    case "Practical":
                        typeofperiod = "Practical";
                        break;
                }
            }
        });
        //Update the Entries
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Period Updated", Toast.LENGTH_SHORT).show();
                String periodname = periodnametext.getText().toString().trim();
                String teachername = teachernametext.getText().toString().trim();
                String place = placetext.getText().toString().trim();
                String startime = starttimetext.getText().toString().trim();
                String endtime = endtimetext.getText().toString().trim();
                String id = period.getId();

                Period newperiod = new Period(periodname,startime,endtime,teachername,place,typeofperiod,id);
                updatePeriod(newperiod);

                alertDialog.dismiss();
            }
        });
        //Delete the entry
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePeriod(period.getId());
                alertDialog.dismiss();
            }
        });


    }

    private void updatePeriod(Period period) {
        DatabaseReference updateReference = weekdayReference.child(period.getId());
        updateReference.setValue(period);
        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

    }

    private void deletePeriod(String id){
        DatabaseReference deleteRef = weekdayReference.child(id);
        deleteRef.removeValue();
        Toast.makeText(getActivity(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void settime(final TextView starttimetext) {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                    if(hourOfDay!=12){
                        hourOfDay=hourOfDay-12;
                    }

                } else {
                    amPm = "AM";
                }
                starttimetext.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm);
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        attachDatabaseReadListner();
    }

    private void attachDatabaseReadListner() {
        if (mChildEventListner == null){
            mChildEventListner = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Period period = dataSnapshot.getValue(Period.class);
                    mPeriodAdaptor.add(period);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Period period = dataSnapshot.getValue(Period.class);
                    int index=-1;
                    for (int i=0;i<mPeriodAdaptor.getCount();i++){
                        if (mPeriodAdaptor.getItem(i).getId()== period.getId()){
                            index=i;
                            break;
                        }
                    }
                    if (index !=-1){
                        Period item = mPeriodAdaptor.getItem(index);
                        item.setvalue(period);
                    }
                    mPeriodAdaptor.notifyDataSetChanged();
                }


                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Period period = dataSnapshot.getValue(Period.class);
                    int index=-1;
                    for (int i=0;i<mPeriodAdaptor.getCount();i++){
                        if (mPeriodAdaptor.getItem(i).getId()== period.getId()){
                            index=i;
                            break;
                        }
                    }
                    if (index !=-1){
                        mPeriodAdaptor.remove(mPeriodAdaptor.getItem(index));
                    }
                    mPeriodAdaptor.notifyDataSetChanged();

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            weekdayReference.addChildEventListener(mChildEventListner);
        }
    }
    private void detachedDatabaseReadListner() {
        if (mChildEventListner!=null){
            weekdayReference.removeEventListener(mChildEventListner);
            mChildEventListner=null;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        detachedDatabaseReadListner();
        mPeriodAdaptor.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListner();
    }
}
