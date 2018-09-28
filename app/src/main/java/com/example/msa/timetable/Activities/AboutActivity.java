package com.example.msa.timetable.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.msa.timetable.R;

public class AboutActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        listView=(ListView)findViewById(R.id.listview);
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this, R.array.Member_info, android.R.layout.simple_list_item_1);
        listView.setAdapter(aa);
    }
}
