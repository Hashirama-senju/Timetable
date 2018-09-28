package com.example.msa.timetable.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.msa.timetable.Model.Period;
import com.example.msa.timetable.R;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InputActivity extends AppCompatActivity {
    private String dayofweek;
    private String typeofperiod;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int currentHour;
    int currentMinute;
    String amPm;
    private TextView starttimetext;
    private TextView endtimetext;
    private TextView periodname;
    private TextView teachername;
    private TextView placetext;
    private DatabaseReference mpostDatabase;
    private FirebaseDatabase mdatabase;
    private LinearLayout theorylayout;
    private ScrollView practicallayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.createtoolbar);
        mytoolbar.setTitle("Add Period");
        setSupportActionBar(mytoolbar);
        ActionBar ab = getSupportActionBar();

        //Initialise the Database
        mpostDatabase = FirebaseDatabase.getInstance().getReference().child("Timetable");
        mpostDatabase.keepSynced(true);

        //Getting the reference
        starttimetext = (TextView) findViewById(R.id.starttimetext);
        endtimetext = (TextView) findViewById(R.id.endtimestart);
        periodname = (TextView) findViewById(R.id.periodname);
        teachername = (TextView) findViewById(R.id.teachertextinput);
        placetext = (TextView) findViewById(R.id.placetextinput);
        theorylayout = (LinearLayout) findViewById(R.id.theoryperiod);
        practicallayout = (ScrollView) findViewById(R.id.practicalperiod);
        practicallayout.setVisibility(View.GONE);

        //onClickListener
        starttimetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settime(starttimetext);
            }
        });
        endtimetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settime(endtimetext);
            }
        });

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //Day Horizontal Selector
        HorizontalPicker daypicker = (HorizontalPicker) findViewById(R.id.daypicker);
        List<HorizontalPicker.PickerItem> daytextItems = new ArrayList<>();
        final String weekday[] = getResources().getStringArray(R.array.week_day);
        for (int i = 0; i <= 5; i++) {
            daytextItems.add(new HorizontalPicker.TextItem(weekday[i]));
        }
        daypicker.setItems(daytextItems); //3 here signifies the default selected item. Use : daypicker.setItems(daytextItems) if none of the items are selected by default.

        //Horizontal Selector Listner
        HorizontalPicker.OnSelectionChangeListener dayhlistener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker horizontalPicker, int i) {
                HorizontalPicker.PickerItem selected = horizontalPicker.getSelectedItem();
                switch (selected.getText().toString()) {
                    case "Mon":
                        dayofweek = "Monday";
                        break;
                    case "Tue":
                        dayofweek = "Tuesday";
                        break;
                    case "Wed":
                        dayofweek = "Wednesday";
                        break;
                    case "Thu":
                        dayofweek = "Thursday";
                        break;
                    case "Fri":
                        dayofweek = "Friday";
                        break;
                    case "Sat":
                        dayofweek = "Saturday";
                        break;
                }
            }
        };
        daypicker.setChangeListener(dayhlistener);

        //Period Type Horizontal Picker
        final HorizontalPicker periodtype = (HorizontalPicker) findViewById(R.id.typepicker);
        final List<HorizontalPicker.PickerItem> typetextItems = new ArrayList<>();
        final String ptype[] = getResources().getStringArray(R.array.period_type);
        for (int i = 0; i <= 1; i++) {
            typetextItems.add(new HorizontalPicker.TextItem(ptype[i]));
        }
        periodtype.setItems(typetextItems, 0); //3 here signifies the default selected item. Use : daypicker.setItems(daytextItems) if none of the items are selected by default.

        periodtype.setChangeListener(new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker horizontalPicker, int i) {
                HorizontalPicker.PickerItem selected = horizontalPicker.getSelectedItem();
                switch (selected.getText().toString()) {
                    case "Theory":
                        typeofperiod = "Theory";
                        theorylayout.setVisibility(View.VISIBLE);
                        practicallayout.setVisibility(View.GONE);

                        break;
                    case "Practical":
                        typeofperiod = "Practical";
                        theorylayout.setVisibility(View.GONE);
                        practicallayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


    }

    private void settime(final TextView starttimetext) {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(InputActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                    if (hourOfDay != 12) {
                        hourOfDay = hourOfDay - 12;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
        MenuItem additem = menu.findItem(R.id.action_create);
        if (ChoiceActivity.studentlogin) {
            additem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                //Store the Value in the Database
                startposting();
                startActivity(new Intent(InputActivity.this, DashboardActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void startposting() {
        String pnamevalue = periodname.getText().toString().trim();
        String starttimevalue = starttimetext.getText().toString().trim();
        ;
        String endtimevalue = endtimetext.getText().toString().trim();
        String tnamevalue = teachername.getText().toString().trim();
        String placevalue = placetext.getText().toString().trim();

        DatabaseReference newPost = mpostDatabase.child(dayofweek);
        String key = newPost.push().getKey();
        Period period = new Period(pnamevalue, starttimevalue, endtimevalue, tnamevalue, placevalue, typeofperiod, key);

        newPost.child(key).setValue(period).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(InputActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }


}



