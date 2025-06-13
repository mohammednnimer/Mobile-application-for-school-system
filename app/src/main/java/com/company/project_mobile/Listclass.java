package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.company.project_mobile.classes.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listclass extends AppCompatActivity {

    ListView listView;
    RequestQueue queue;
    List<String> displayedList = new ArrayList<>();
    ArrayAdapter<String> listViewAdapter;
    List<String> subjectList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listclass);

        listView = findViewById(R.id.lvResults);
        queue = Volley.newRequestQueue(this);




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
                    updateListView(subjectList);


                }
                ArrayList<String> newn=new ArrayList<>();
                 for(int i=0;i<subjectList.size();i++)
                {


                    if(subjectList.get(i).toLowerCase().contains(se.toLowerCase()))
                    {
                        newn.add(subjectList.get(i));
                    }




                }
                updateListView(newn);





                return false;
            }

        });







        String url = "http://10.0.2.2:3000/class.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        subjectList = new ArrayList<>();

                        if (response.length() == 0) {
                            Toast.makeText(Listclass.this, "No classes found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("TAG", "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Log.d("TAG", "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

                                JSONObject obj = response.getJSONObject(i);
                                String className = obj.getString("class_name");
                                subjectList.add(className);

                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }

                        updateListView(subjectList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Listclass.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        queue.add(request);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedItem = displayedList.get(position);


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("class", clickedItem);
            editor.apply();
            Intent intent = new Intent(Listclass.this, Addstudent.class);
            startActivity(intent);
        });
    }

    private void updateListView(List<String> subjectList) {
        displayedList.clear();
        displayedList.addAll(subjectList);

        listViewAdapter = new ArrayAdapter<>(Listclass.this,
                R.layout.list_item_assignment,
                R.id.textItem,
                displayedList
        );
        listView.setAdapter(listViewAdapter);
    }
}
