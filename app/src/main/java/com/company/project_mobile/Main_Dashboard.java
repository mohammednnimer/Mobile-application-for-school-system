package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Main_Dashboard extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        LinearLayout btnMarks = findViewById(R.id.btnMarks);
        LinearLayout btnAssignments = findViewById(R.id.btnAssignments);
        LinearLayout btnSubjects = findViewById(R.id.btnSubjects);
        LinearLayout btnSchedule = findViewById(R.id.btnSchedule);

        ImageView logout = findViewById(R.id.imageView3);
        ImageView user = findViewById(R.id.imageView4);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("your_shared_pref_name", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                 Intent intent = new Intent(Main_Dashboard.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // يمسح كل الأنشطة السابقة
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton flo=findViewById(R.id.floatingActionButton2);

        flo.setOnClickListener(e->
        {
            Intent intent = new Intent(Main_Dashboard.this, MapsActivity.class);
            startActivity(intent);

        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Main_Dashboard.this, useracoount.class); // Activity2 هو مثال على النشاط الجديد
                startActivity(intent);
            }
        });

        btnMarks.setOnClickListener(v -> {
            Intent intent = new Intent(Main_Dashboard.this, Marks.class);
            startActivity(intent);
        });

        btnAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(Main_Dashboard.this, Assignments.class);
            startActivity(intent);
        });

        btnSubjects.setOnClickListener(v -> {
            Intent intent = new Intent(Main_Dashboard.this, Subject.class);
            startActivity(intent);
        });

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(Main_Dashboard.this, Schedule.class);
            startActivity(intent);
        });
    }
}
