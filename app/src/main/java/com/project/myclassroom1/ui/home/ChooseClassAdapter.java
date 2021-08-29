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

import com.project.myclassroom1.Assignment;
import com.project.myclassroom1.ClassDetail;
import com.project.myclassroom1.R;

import java.util.ArrayList;

public class ChooseClassAdapter extends RecyclerView.Adapter<ChooseClassAdapter.MyViewHolder>{

    Context context;
    ArrayList<Classes> myClasses;

    public ChooseClassAdapter(Context context, ArrayList<Classes> myClasses) {
        this.context = context;
        this.myClasses = myClasses;
    }

    @NonNull
    @Override
    public ChooseClassAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.choose_layout,parent,false);
        return new ChooseClassAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseClassAdapter.MyViewHolder holder, int position) {

        Classes classes= (Classes) myClasses.get(position);
        holder.className.setText(classes.getClasName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Assignment.class);
                intent.putExtra("classId", myClasses.get(position).getId());
                intent.putExtra("className",classes.getClasName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myClasses.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView className;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.relative);
            className=itemView.findViewById(R.id.name_of_class);
        }
    }

}
