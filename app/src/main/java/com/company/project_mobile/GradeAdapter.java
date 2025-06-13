package com.company.project_mobile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.project_mobile.classes.grade;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {
    List<grade> grades;

    public GradeAdapter(List<grade> grades) {
        this.grades = grades;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        grade grade = grades.get(position);
        holder.title.setText(grade.getTitle());
        holder.mark.setText(String.valueOf(grade.getMark()));

    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView title, mark;

        public GradeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.grade_title);
            mark = itemView.findViewById(R.id.grade_mark);
        }
    }
}
