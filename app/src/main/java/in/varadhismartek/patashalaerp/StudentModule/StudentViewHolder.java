package in.varadhismartek.patashalaerp.StudentModule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;

public class StudentViewHolder extends RecyclerView.ViewHolder {
    public TextView tvStudentName,tvClass,tvSection,tvDOB,tvCertificate;
    public CircleImageView studentImage;
    public LinearLayout linearLayoutStudent;
    public StudentViewHolder(View itemView) {
        super(itemView);
        tvStudentName = itemView.findViewById(R.id.tv_name);
        tvClass = itemView.findViewById(R.id.tv_class);
        tvSection = itemView.findViewById(R.id.tv_section);
        tvDOB = itemView.findViewById(R.id.tv_dob);
        tvCertificate = itemView.findViewById(R.id.tv_certificateno);
        studentImage = itemView.findViewById(R.id.civEmpImage);
        linearLayoutStudent = itemView.findViewById(R.id.ll_student);
    }
}
