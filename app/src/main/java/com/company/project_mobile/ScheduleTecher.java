package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.company.project_mobile.classes.lecutre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleTecher extends AppCompatActivity {
    RequestQueue queue;

    ArrayList<lecutre> lecutres=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_techer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
        String url = "http://10.0.2.2:3000/lectureTecher.php?id="+userId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);



                                String name= obj.getString("subject_name");

                                String day=obj.getString("class_day");
                                int from =Integer.parseInt(obj.getString("class_start_time").split(":")[0])-7;
                                String classname=obj.getString("class_name");
                                int colome=0;
                                if (day.equalsIgnoreCase("sunday")) {
                                    colome = 1;
                                } else if (day.equalsIgnoreCase("monday")) {
                                    colome = 2;
                                } else if (day.equalsIgnoreCase("tuesday")) {
                                    colome = 3;
                                } else if (day.equalsIgnoreCase("wednesday")) {
                                    colome = 4;
                                } else if (day.equalsIgnoreCase("thursday")) {
                                    colome = 5;
                                }
                                lecutres.add(new lecutre(from,colome,name+"("+classname+")"));


                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }
                        for (int i=0;i<lecutres.size();i++)
                        {


                            String lovation = "r" + lecutres.get(i).getRow() + "c" + lecutres.get(i).getColume();

                            Log.d("TAG", lovation);
                            int resID = getResources().getIdentifier(lovation, "id", getPackageName());
                            TextView text = findViewById(resID);
                            text.setText(lecutres.get(i).getName());

                            Log.d("TAG", "ttttttttttttttttttttttttttttttttttttttttt: ");


                        }



                    }




                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduleTecher.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });


        Button back=findViewById(R.id.back);


        back.setOnClickListener(e->
        {
            Intent intent = new Intent(ScheduleTecher.this, Teacher_Main_Dashboard.class);
            startActivity(intent);
        });

        queue.add(request);
    }



}