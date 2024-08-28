package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.tutor.ViewPagerAdapter;

public class TutorViewStudents extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        setTitle("Tutor - Students");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(this);

        viewPagerAdapter.addFragment(new MarkAttendanceFragment(), "Mark Attendance");
        viewPagerAdapter.addFragment(new ViewAttendanceFragment(), "View Attendance");

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(viewPagerAdapter.getPageTitle(position))).attach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.add_new_student) {
            onClickAddNewStudent();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onClickAddNewStudent() {
        Intent intent = new Intent(TutorViewStudents.this, AddStudent.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tutor_dashboard_menu, menu);
        return true;
    }
}
