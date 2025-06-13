package com.company.project_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Subject_detail extends AppCompatActivity {


    private TextView name;
    private  TextView Teachername;
    private  TextView desc ;
    private  ImageView imageView ;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.subject_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        name = findViewById(R.id.name);
        Teachername = findViewById(R.id.Tname);
        imageView = findViewById(R.id.image);
        desc= findViewById(R.id.desc);
        back=findViewById(R.id.back);



        Intent intent = getIntent();
        name.setText(intent.getStringExtra("Name"));
        Teachername.setText("Teacher : "+intent.getStringExtra("Teacher"));
      String img = intent.getStringExtra("Img");
      desc.setText(intent.getStringExtra("disc"));



//        String imageName = img.replace(".png", "");
//        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
//        imageView.setImageResource(imageResId);

        Glide.with(this).load(img).into(imageView);





        back.setOnClickListener(e->
        {

            Intent inten = new Intent(Subject_detail.this, Subject.class);
            startActivity(inten);
            finish();

        });





    }

}