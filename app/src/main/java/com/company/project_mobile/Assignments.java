package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.company.project_mobile.classes.ASS;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Assignments extends AppCompatActivity {

    Spinner spinner;
    RecyclerView recyclerView;
    ASSAdapter assAdapter;
    List<ASS> allAssignments = new ArrayList<>();
    RequestQueue queue;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignments); // Make sure to use correct layout

        bindViews();

        queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = Integer.parseInt(sharedPreferences.getString("user_id", "default_value"));
        String url = "http://10.0.2.2:3000/ass.php?id=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> subjectList = new ArrayList<>();
                        subjectList.add("ALL SUBJECT");

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String subjectName = obj.getString("subject_name");
                                String assname = obj.getString("title");
                                String description = obj.getString("description");
                                String duedate = obj.getString("due_date");
                                int id = Integer.parseInt(obj.getString("assignment_id"));

                                ASS ass = new ASS(subjectName, assname, duedate, description, id);
                                allAssignments.add(ass);

                                if (!subjectList.contains(subjectName)) {
                                    subjectList.add(subjectName);
                                }
                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Assignments.this, android.R.layout.simple_spinner_item, subjectList);
                        spinner.setAdapter(spinnerAdapter);
                        updateRecyclerView(allAssignments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Assignments.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });
        queue.add(request);




        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedSubject = spinner.getSelectedItem().toString();
                if (selectedSubject.equals("ALL SUBJECT")) {
                    updateRecyclerView(allAssignments);
                } else {
                    List<ASS> filtered = new ArrayList<>();
                    for (ASS ass : allAssignments) {
                        if (ass.getSubjectName().equalsIgnoreCase(selectedSubject)) {
                            filtered.add(ass);
                        }
                    }
                    updateRecyclerView(filtered);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

    }



    private void bindViews() {
        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assAdapter = new ASSAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(assAdapter);
    }

    private void updateRecyclerView(List<ASS> assignments) {
        assAdapter.setFilteredList(assignments);
    }
}
