package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Animation extends AppCompatActivity {

    TextView welcom;
    ImageView image;
    RequestQueue queue;
    int id;
    private android.view.animation.Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.animation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        welcom = findViewById(R.id.welcom);
        image=findViewById(R.id.image);

        top = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        bottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        welcom.setAnimation(bottom);
        image.setAnimation(top);
        queue = Volley.newRequestQueue(this);


    }







    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Animation.this, Main_Dashboard.class);
                intent.putExtra("ID_user",id);
                startActivity(intent);
                finish();
            }
        }, 4000);
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId =Integer.parseInt(sharedPreferences.getString("user_id", "default_value"));

        String url = "http://10.0.2.2:3000/index1.php?id="+userId;
        Log.d("TAG", url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                         for (int i = 0; i < response.length(); i++) {
                            try {



                                JSONObject obj = response.getJSONObject(0);

                                if (obj.has("name")) {
                                    String name = obj.getString("name");
                                    welcom.setText("Welcome " + name);
                                } else {
                                    Log.d("Error", "Full name not found");
                                }



                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Animation.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        queue.add(request);
    }



}
