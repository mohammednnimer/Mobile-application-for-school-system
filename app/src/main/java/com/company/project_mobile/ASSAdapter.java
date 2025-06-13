package com.company.project_mobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.company.project_mobile.classes.ASS;
import com.google.gson.Gson;

import java.util.List;

public class ASSAdapter extends RecyclerView.Adapter<ASSAdapter.ViewHolder> {

    private Context context;
    private List<ASS> assignments;


    public ASSAdapter(List<ASS> assignments, Context context) {
        this.assignments = assignments;
        this.context = context;
    }

    public void setFilteredList(List<ASS> filteredList) {
        this.assignments = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ASSAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asscard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ASSAdapter.ViewHolder holder, int position) {
        ASS ass = assignments.get(position);
        holder.subject.setText(ass.getSubjectName());
        holder.title.setText(ass.getTitle());
        holder.date.setText(ass.getDate());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Submet_Ass.class);

                Gson gson = new Gson();
                String json=gson.toJson(ass);
                intent.putExtra("this.Ass",json);
                context.startActivity(intent);



            }
        });



    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, title, date;

        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.tvSubject);
            title = itemView.findViewById(R.id.tvTitle);
            date = itemView.findViewById(R.id.tvDate);
            card=itemView.findViewById(R.id.card_view);
        }
    }
}
