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
import com.project.myclassroom1.Submit;

import java.util.ArrayList;

public class SubmittedAdapter extends RecyclerView.Adapter<SubmittedAdapter.MyViewHolder>{


    Context context;
    ArrayList<Details> myDetails;

    public SubmittedAdapter(Context context, ArrayList<Details> myDetails) {
        this.context = context;
        this.myDetails = myDetails;
    }

    @NonNull
    @Override
    public SubmittedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.submitted_layout,parent,false);
        return new SubmittedAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmittedAdapter.MyViewHolder holder, int position) {
        Details details=  myDetails.get(position);
        holder.rollNo.setText(details.getRoll());
        String s = details.getRoll();
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullScreen.class);
                intent.putExtra("uid",s);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rollNo;
        ImageView image;
        RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.submittedRelative);
           image = itemView.findViewById(R.id.testPhoto);
            rollNo = itemView.findViewById(R.id.rollNo);
        }
    }
}
