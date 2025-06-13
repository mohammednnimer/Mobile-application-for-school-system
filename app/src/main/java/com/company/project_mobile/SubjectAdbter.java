package com.company.project_mobile;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.project_mobile.classes.Subject;

import java.util.List;

public class SubjectAdbter extends RecyclerView.Adapter<SubjectAdbter.ViewHolder> {


    private static String name="Name";


    private static String img="Img";

    private static String disc="disc";


    private List<Subject> productList;
    private Context context;

    public SubjectAdbter(List<Subject> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject product = productList.get(position);
        holder.nameTextView.setText(product.getName());

        CardView cardView = holder.card;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.image);
        Glide.with(context).load(product.getUrl()).into(imageView);

//        String imageName = product.getUrl().replace(".png", "");
//        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
//        holder.imageView.setImageResource(imageResId);


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Subject_detail.class);
                intent.putExtra("ID",product.getId());
                intent.putExtra(name,product.getName());
                intent.putExtra("Img",product.getUrl());
                intent.putExtra(disc,product.getdisc());
                intent.putExtra("Teacher",product.getTeacher());
                context.startActivity(intent);




            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            nameTextView = itemView.findViewById(R.id.txtName);
            card=itemView.findViewById(R.id.card_view);

        }
    }
}