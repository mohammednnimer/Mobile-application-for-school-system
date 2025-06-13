package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegistrarHomePageActivity extends AppCompatActivity {
    MaterialCardView CrdAddStudent, CrdAddSubject, CrdBuildSchedule, CrdAddUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView text=findViewById(R.id.forgotPassword);
        text.setOnClickListener(e->
        {
            Intent intent = new Intent(this, GetCode.class);
            startActivity(intent);

        });


        ImageView logout = findViewById(R.id.imageView);
        ImageView user = findViewById(R.id.imageView2);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("your_shared_pref_name", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(RegistrarHomePageActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // يمسح كل الأنشطة السابقة
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton flo=findViewById(R.id.floatingActionButton4);

        flo.setOnClickListener(e->
        {
            Intent intent = new Intent(RegistrarHomePageActivity.this, MapsActivity.class);
            startActivity(intent);

        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarHomePageActivity.this, useracoount.class); // Activity2 هو مثال على النشاط الجديد
                startActivity(intent);
            }
        });







        CrdAddStudent = findViewById(R.id.CrdAddStudent);
        CrdAddSubject = findViewById(R.id.CrdAddSubject);
        CrdBuildSchedule = findViewById(R.id.CrdBuildSchedule);
        CrdAddUser = findViewById(R.id.CrdAddUser);

                CrdAddUser.setOnClickListener(v -> {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("role", "Teacher");

                    editor.apply();
                    Intent intent = new Intent(this, AddTeacherActivity.class);

            startActivity(intent);
        });


        CrdAddStudent.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("role", "Student");
            editor.apply();
            Intent intent = new Intent(this, Listclass.class);
            startActivity(intent);
        });

        CrdAddSubject.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddSubjectActivity.class);
            startActivity(intent);
        });

        CrdBuildSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, BuildClassScheduleActivity.class);
            startActivity(intent);
        });


    }

}