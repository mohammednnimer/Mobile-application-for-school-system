package com.company.project_mobile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.company.project_mobile.classes.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarkAdpter extends RecyclerView.Adapter<MarkAdpter.ViewHolder> {

    private Context context;
    private List<Student> students;
    private List<String> marksList = new ArrayList<>();  // لتخزين العلامات المدخلة
    private HashMap<Integer, String> marksMap = new HashMap<>();  // لتخزين العلامات بواسطة الفهرس

    public MarkAdpter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_mark_adpter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = students.get(position);

        holder.tvStudentName.setText(student.getName());
        holder.etMark.setText("");  // Reset the EditText

        // إعداد الـ TextWatcher لكي نحفظ العلامة المدخلة بناءً على الفهرس
        holder.etMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // لا حاجة لفعل شيء هنا
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String enteredMark = charSequence.toString();

                marksMap.put(position, enteredMark);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // يمكن إضافة منطق إضافي هنا بعد تغيير النص
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        EditText etMark;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            etMark = itemView.findViewById(R.id.etMark);
        }
    }

    // للوصول إلى جميع العلامات المدخلة من خلال الفهرس
    public HashMap<Integer, String> getMarksMap() {
        return marksMap;
    }
}
