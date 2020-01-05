package in.varadhismartek.patashalaerp.StudentModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.R;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentViewHolder> {
    ArrayList<StudentModel> allstudentList ;
    Context context;
    String rvType;
    public StudentRecyclerAdapter(ArrayList<StudentModel> studentModels, Context context, String rvType) {
        this.allstudentList =studentModels;
        this.context = context;
        this.rvType =rvType;


    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (rvType){
            case Constant.RV_STUDENT_ALL_LIST:
                return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_row,parent,false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {
        switch (rvType){
            case Constant.RV_STUDENT_ALL_LIST:
                holder.tvStudentName.setText("Name: "+allstudentList.get(position).getStrFirstName() +" "+allstudentList.get(position).getStrLastName());
                holder.tvClass.setText("Class : "+allstudentList.get(position).getStrClass());
                holder.tvSection.setText("Section : "+allstudentList.get(position).getStrSection());
                holder.tvDOB.setText("DOB : "+allstudentList.get(position).getStrDOB());
                holder.tvCertificate.setText("Certificate No : "+allstudentList.get(position).getStrCertificateNo());
                String image_Value = allstudentList.get(position).getStrPhoto();
                if (!allstudentList.get(position).getStrPhoto().isEmpty() || !allstudentList.get(position).getStrPhoto().equals("")){
                    Picasso.with(context)
                            .load(Constant.IMAGE_LINK+image_Value)
                            .error(R.drawable.user_icon)
                            .into(holder.studentImage);
                }
                else {
                    holder.studentImage.setBackgroundResource(R.drawable.user_icon);
                }
                holder.linearLayoutStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StudentModuleActivity studentModuleActivity = (StudentModuleActivity)context;

                        Bundle bundle = new Bundle();
                        bundle.putString("FName", allstudentList.get(position).getStrFirstName());
                        bundle.putString("LName", allstudentList.get(position).getStrLastName());
                        bundle.putString("DOB", allstudentList.get(position).getStrDOB());
                        bundle.putString("CertificateNo", allstudentList.get(position).getStrCertificateNo());
                        bundle.putString("EEUID", allstudentList.get(position).getStrStudentID());

                        studentModuleActivity.loadFragment(Constant.STUDENT_DETAILS, bundle);


                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (rvType){
            case Constant.RV_STUDENT_ALL_LIST:
               return allstudentList.size();
        }
        return 0;
    }
}
