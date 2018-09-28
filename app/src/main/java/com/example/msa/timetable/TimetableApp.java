package com.example.msa.timetable;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TimetableApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
