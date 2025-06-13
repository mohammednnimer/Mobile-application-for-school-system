package com.company.project_mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SearchView;
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
import com.company.project_mobile.classes.Subject;
import com.company.project_mobile.classes.grade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Marks extends AppCompatActivity {
    ArrayList<Subject> newn;
    RequestQueue queue;
    SubjectAdapter adapter;
    RecyclerView subjectRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.marks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



         subjectRecycler = findViewById(R.id.subjects_recycler);
        subjectRecycler.setLayoutManager(new LinearLayoutManager(this));









        SearchView searchView = findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }





            @Override
            public boolean onQueryTextChange(String newText) {
                String se = searchView.getQuery().toString();
                if(se.isEmpty())
                {
                     adapter = new SubjectAdapter(newn);
                    subjectRecycler.setAdapter(adapter);


                 }
                ArrayList<Subject> searchitem=new ArrayList<>();
                for(int i=0;i<newn.size();i++)
                {

                    Log.d("TAG", newn.get(i).getName());

                    if(newn.get(i).getName().toLowerCase().contains(se.toLowerCase()))
                    {
                        searchitem.add(newn.get(i));
                    }




                }
                 adapter = new SubjectAdapter(searchitem);
                subjectRecycler.setAdapter(adapter);





                return false;
            }

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
        String url = "http://10.0.2.2:3000/marks.php?id="+userId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", "lllllllllllllllllllllllllllllllllll");


                        newn=new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                String name= obj.getString("subject_name");
                                String disc= obj.getString("mark_type");
                                String title=obj.getString("title");
                                Log.d("TAG", name);
                                double score = Double.parseDouble(obj.getString("score"));
                                boolean isexist=false;
                                for(int j=0;j<newn.size();j++)
                                {
                                    if(newn.get(j).getName().toLowerCase().equals(name.toLowerCase()))
                                    {

                                        newn.get(j).getGrades().add(new grade(title+"("+disc+")",score));

                                        isexist=true;
                                    }
                                }
                                if(!isexist)
                                {

                                    List<grade> newgrade=new ArrayList<>();
                                    newgrade.add(new grade(title+"("+disc+")",score));
                                    Subject sub=new Subject(name,newgrade);
                                    newn.add(sub);
                                }




                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }





                        adapter = new SubjectAdapter(newn);
                        subjectRecycler.setAdapter(adapter);




                    }




                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Marks.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        queue.add(request);
    }

}