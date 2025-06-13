package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Subject extends AppCompatActivity {
    RequestQueue queue;
    ArrayList<com.company.project_mobile.classes.Subject> newn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        FloatingActionButton back=findViewById(R.id.floatingActionButton);
        back.setOnClickListener(e->
        {

            Intent inten = new Intent(Subject.this, Main_Dashboard.class);
            startActivity(inten);
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (queue != null) {
            queue.cancelAll("TAG");
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        queue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId =Integer.parseInt(sharedPreferences.getString("user_id", "default_value"));
        String url = "http://10.0.2.2:3000/subject.php?id="+userId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {



                         newn=new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);



                                int id=Integer.parseInt(obj.getString("id"));
                                String name= obj.getString("subject_name");
                                String disc= obj.getString("description");
                                String image=obj.getString("image");
                                String Techername= obj.getString("teacher_name");


                                Log.d("TAG", image);
                                com.company.project_mobile.classes.Subject sub=new com.company.project_mobile.classes.Subject(id,name,image,disc,Techername);



                                newn.add(sub);
                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }

                        RecyclerView recyclerView = findViewById(R.id.product_recycler);
                        SubjectAdbter adapter = new SubjectAdbter(newn, Subject.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Subject.this));










                    }




                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Subject.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        queue.add(request);
    }


}