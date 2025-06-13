package com.company.project_mobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.project_mobile.classes.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registrar can create a schedule for each class using this Activity.
 * Compatible with the provided activity_create_schedule.xml.
 */
public class BuildClassScheduleActivity extends AppCompatActivity {
    private Spinner spClass, spDay, spSubject, spTeacher;
    private TimePicker timePickerStart;
    private Button btnAddPeriod, btnSaveSchedule;
    private LinearLayout layoutPeriodsList;

    // Store periods before saving
    private final List<Period> periodList = new ArrayList<>();
    private final List<Button> ButtonList = new ArrayList<>();
    // Caching loaded data for Spinners
    private List<String> classList = new ArrayList<>();
    private List<Item> subjectList = new ArrayList<>();
    private List<Item> teacherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_build_class_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spClass = findViewById(R.id.spClass);
        spDay = findViewById(R.id.spDay);
        spSubject = findViewById(R.id.spSubject);
        spTeacher = findViewById(R.id.spTeacher);
        timePickerStart = findViewById(R.id.timepicker_start_time);
        btnAddPeriod = findViewById(R.id.btnaddperiod);
        btnSaveSchedule = findViewById(R.id.btn_save_schedule);
        layoutPeriodsList = findViewById(R.id.layout_periods_list);


        timePickerStart.setHour(8);
        timePickerStart.setMinute(0);

        timePickerStart.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            if (hourOfDay < 8 || hourOfDay > 13) {
                 timePickerStart.setHour(8);
                timePickerStart.setMinute(0);
                Toast.makeText(this, "Please select a valid time (between 8 AM and 5 PM).", Toast.LENGTH_SHORT).show();
            } else if (minute != 0) {
                timePickerStart.setMinute(0);
                Toast.makeText(this, "Minutes must be 00", Toast.LENGTH_SHORT).show();
            }
        });


        loadSpinnerData();

        btnAddPeriod.setOnClickListener(v -> addPeriod());

        btnSaveSchedule.setOnClickListener(v -> saveScheduleToServer());
    }

    private void loadSpinnerData() {
        loadSpinner("http://10.0.2.2:3000/class.php", spClass, classList);
        uploudcladd("http://10.0.2.2:3000/getsupject.php", spSubject, subjectList);
        uploudcladd("http://10.0.2.2:3000/getteacher.php", spTeacher, teacherList);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_dropdown_item);
        spDay.setAdapter(dayAdapter);
    }


    private void loadSpinner(String url, Spinner spinner, List<String> storageList) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        storageList.clear();

                        if (response.length() == 0) {
                            Toast.makeText(BuildClassScheduleActivity.this, "No classes found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String className = obj.getString("class_name"); // الحصول على اسم الصف
                                storageList.add(className);
                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BuildClassScheduleActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, storageList);
                        spinner.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuildClassScheduleActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        Volley.newRequestQueue(BuildClassScheduleActivity.this).add(request);
    }


    private void uploudcladd(String url,Spinner spinner, List<Item> storageList)
    {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        subjectList = new ArrayList<>();

                        storageList.clear();
                        if (response.length() == 0) {
                            Toast.makeText(BuildClassScheduleActivity.this, "No classes found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String Name = obj.getString("name");
                                int id =Integer.parseInt(obj.getString("id"));

                                Item n=new Item(id, Name);
                                storageList.add(n);


                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }
                        ArrayAdapter<Item> adapter = new ArrayAdapter<>(BuildClassScheduleActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, storageList);

                         spinner.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuildClassScheduleActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });
        Volley.newRequestQueue(BuildClassScheduleActivity.this).add(request);

    }


    private void addPeriod() {
        String className = spClass.getSelectedItem() != null ? spClass.getSelectedItem().toString() : "";
        String day = spDay.getSelectedItem() != null ? spDay.getSelectedItem().toString() : "";
        Item selectedSubject = (Item) spSubject.getSelectedItem();
        Item selectedTeacher = (Item) spTeacher.getSelectedItem();
        int hour = timePickerStart.getHour();
        int minute = timePickerStart.getMinute();
        String startTime = String.format("%02d:%02d:00", hour, minute);

        if (className.isEmpty() || day.isEmpty() || selectedSubject.getName().isEmpty() || selectedTeacher.getName().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        for (int i=0;i<periodList.size();i++)
        {
            if(day.equals(periodList.get(i).day)&&startTime.equals(periodList.get(i).startTime)&&className.equals(periodList.get(i).className))
            {

                periodList.get(i).teacher=selectedTeacher.getName();
                periodList.get(i).subject=selectedSubject.getName();

                ButtonList.get(i).setText(day + ": " + selectedSubject.getName() + " (" + startTime + "), " + selectedTeacher.getName());

                return;
            }

        }

        periodList.add(new Period(className, day,selectedSubject.getId()+"" , selectedTeacher.getId()+"", startTime));

        Button periodView = new Button(this);
        periodView.setText(day + ": " + selectedSubject.getName() + " (" + startTime + "), " + selectedTeacher.getName());
        periodView.setAllCaps(false);
        periodView.setEnabled(false);

        ButtonList.add(periodView);
        layoutPeriodsList.addView(periodView);


        Toast.makeText(this, "Period added", Toast.LENGTH_SHORT).show();
    }

    private void saveScheduleToServer() {
        if (periodList.isEmpty()) {
            Toast.makeText(this, "No periods to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Period p : periodList) {
            String url = "http://10.0.2.2:3000/classschedule.php";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {},
                    error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("class_name", p.className);
                    params.put("day", p.day);
                    params.put("subject_id", p.subject);
                    params.put("teacher_id", p.teacher);
                    params.put("start_time", p.startTime);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }

        Toast.makeText(this, "Schedule saved for class!", Toast.LENGTH_LONG).show();
        periodList.clear();
        layoutPeriodsList.removeAllViews();
    }

    private static class Period {
        String className, day, subject, teacher, startTime;
        Period(String className, String day, String subject, String teacher, String startTime) {
            this.className = className;
            this.day = day;
            this.subject = subject;
            this.teacher = teacher;
            this.startTime = startTime;
        }
    }


}