package com.tutorconnect.app;

import android.app.Application;

import com.tutorconnect.app.model.Parent;
import com.tutorconnect.app.model.Student;
import com.tutorconnect.app.model.Tutor;

public class TutorApplication extends Application {

    public Tutor tutor;
    public Student student;
    public Parent parent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
