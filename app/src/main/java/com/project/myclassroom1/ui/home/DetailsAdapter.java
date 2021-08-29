package com.project.myclassroom1.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.myclassroom1.R;
import com.project.myclassroom1.Submit;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder>{

    Context context;
    ArrayList<Details> myDetails;

    public DetailsAdapter(Context context, ArrayList<Details> myDetails) {
        this.context = context;
        this.myDetails = myDetails;
    }

    @NonNull
    @Override
    public DetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.MyViewHolder holder, int position) {
        Details details=  myDetails.get(position);
        holder.type.setText(details.getType());
        holder.date.setText(details.getDate());
        holder.link.setText(details.getLink());
        holder.teacherName.setText(details.getTeacherName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.type.getText().equals("Class Link")){
                    Intent intent = new Intent(context, Submit.class);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return myDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

    TextView link,type,date,teacherName;
    RelativeLayout layout;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        layout=itemView.findViewById(R.id.itemRelative);
        link=itemView.findViewById(R.id.link);
        teacherName=itemView.findViewById(R.id.teacherName);
        type=itemView.findViewById(R.id.type);
        date=itemView.findViewById(R.id.date);
    }
}
}
