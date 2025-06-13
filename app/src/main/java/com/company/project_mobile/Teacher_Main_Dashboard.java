package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Teacher_Main_Dashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main_dashboard);

        LinearLayout btnAssignments = findViewById(R.id.btnAssignments);
        LinearLayout btnSubjects = findViewById(R.id.btnSubjects);
        LinearLayout btnSchedule = findViewById(R.id.btnSchedule);


        ImageView logout = findViewById(R.id.imageView14);
        ImageView user = findViewById(R.id.imageView13);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("your_shared_pref_name", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(Teacher_Main_Dashboard.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // يمسح كل الأنشطة السابقة
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton flo=findViewById(R.id.floatingActionButton3);

        flo.setOnClickListener(e->
        {
            Intent intent = new Intent(Teacher_Main_Dashboard.this, MapsActivity.class);
            startActivity(intent);

        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_Main_Dashboard.this, useracoount.class); // Activity2 هو مثال على النشاط الجديد
                startActivity(intent);
            }
        });
        btnAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(Teacher_Main_Dashboard.this, TeacherClassActivity.class);
            startActivity(intent);
        });

        btnSubjects.setOnClickListener(v -> {
            Intent intent = new Intent(Teacher_Main_Dashboard.this, Techer_subject_mark.class);
            startActivity(intent);
        });

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(Teacher_Main_Dashboard.this, ScheduleTecher.class);
            startActivity(intent);
        });
    }
}