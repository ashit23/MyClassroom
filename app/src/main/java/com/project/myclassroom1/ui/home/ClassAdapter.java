package com.project.myclassroom1.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.myclassroom1.ClassDetail;
import com.project.myclassroom1.JoinClass;
import com.project.myclassroom1.R;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder>{

    Context context;
    ArrayList<Classes> myClasses;

    public ClassAdapter(Context context, ArrayList<Classes> myClasses) {
        this.context = context;
        this.myClasses = myClasses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.class_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Classes classes= (Classes) myClasses.get(position);
        holder.className.setText(classes.getClasName());
        holder.teacherName.setText(classes.getTeacherName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClassDetail.class);
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

        TextView className,teacherName;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout=itemView.findViewById(R.id.relativeLayout);
            className=itemView.findViewById(R.id.className);
            teacherName=itemView.findViewById(R.id.teacherName);
        }
    }

}
