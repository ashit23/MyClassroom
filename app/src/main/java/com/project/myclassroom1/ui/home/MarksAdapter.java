package com.project.myclassroom1.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.myclassroom1.FullScreen;
import com.project.myclassroom1.R;

import java.util.ArrayList;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MyViewHolder>{


    Context context;
    ArrayList<Details> myDetails;

    public MarksAdapter(Context context, ArrayList<Details> myDetails) {
        this.context = context;
        this.myDetails = myDetails;
    }

    @NonNull
    @Override
    public MarksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.marks_layout,parent,false);
        return new MarksAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksAdapter.MyViewHolder holder, int position) {
        Details details=  myDetails.get(position);
        holder.rollNo.setText(details.getRoll());
        holder.marks.setText(details.getMarks());
    }

    @Override
    public int getItemCount() {
        return myDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rollNo,marks;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            marks=itemView.findViewById(R.id.marks);
            rollNo = itemView.findViewById(R.id.rollNo);
        }
    }
}
