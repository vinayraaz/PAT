package in.varadhismartek.patashalaerp.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.ExamActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.HouseModule;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by varadhi10 on 8/5/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    ArrayList<Data> dataArrayList;
    private ArrayList<MyModel> houseList;
    private ArrayList<HouseModule> newhouseList;
    ArrayList<Data> checkedList;
    Context context;
    Data data;
    String tag;
    APIService mApiService;
    RecyclerAdapter recyclerAdapter;
    ProgressDialog mProgressDialog;
    Dialog dialog;
    String strOldHouseName, strNewHouseName = "", strTeacherID, strCaptionID, strViceCaptionID, strOldHColor, strNewHColor = "", strStudentNo;

    public RecyclerAdapter() {
    }

    public RecyclerAdapter(Context context, ArrayList<Data> dataArrayList, String tag) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.tag = tag;
        mApiService = ApiUtils.getAPIService();

    }

/*    public RecyclerAdapter(ArrayList<MyModel> arrayList, Context context, String tag) {
        this.houseList = arrayList;
        this.context = context;
        this.tag = tag;
        mApiService = ApiUtils.getAPIService();
    }*/

    public RecyclerAdapter(ArrayList<HouseModule> newhouseList, Context context, String tag) {
        this.newhouseList = newhouseList;
        this.context = context;
        this.tag = tag;
        mApiService = ApiUtils.getAPIService();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (tag) {

            case Constant.BARRIERS_FRAG:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_card, parent, false));

            case Constant.DEPARTMENT_FRAG:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_card, parent, false));
            case Constant.RV_ADD_HOUSES:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.house_row, parent, false));


        }
        return null;
    }


    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {


        switch (tag) {
            case Constant.RV_ADD_HOUSES:

                holder.tvHouseName.setText(newhouseList.get(position).getHouse_name());


                String colorCode = "", strColorName = "";


                colorCode = newhouseList.get(position).getHouse_color();
                if (colorCode.equalsIgnoreCase("#1CBE22") ||
                        colorCode.equalsIgnoreCase("#023EF8") ||
                        colorCode.equalsIgnoreCase("#F81200") ||
                        colorCode.equalsIgnoreCase("#FF9800") ||
                        colorCode.equalsIgnoreCase("#FF5722")) {
                    holder.tvStudentCount.setTextColor(Color.parseColor(colorCode));
                    holder.llHouseAddress.setBackgroundColor(Color.parseColor(colorCode));
                    holder.tvStudent.setTextColor(Color.parseColor(colorCode));

                } else {
                    holder.tvStudentCount.setTextColor(Color.parseColor("#000000"));
                    holder.llHouseAddress.setBackgroundColor(Color.parseColor("#000000"));
                    holder.tvStudent.setTextColor(Color.parseColor("#000000"));
                }




              //  String Teacher = newhouseList.get(position).getmName();

              //  System.out.println("Teacher***" + Teacher);

              /*  if (Teacher.equals("null") || Teacher.trim().equals("") || Teacher.trim().equals(null) || Teacher.isEmpty()) {

                    holder.tvTeacherName.setVisibility(View.GONE);
                    System.out.println("Mentor Teacher***" + Teacher);
                } else {
                    holder.tvTeacherName.setVisibility(View.VISIBLE);
                    holder.tvTeacherName.setText("Mentor Teacher : " + Teacher);

                    System.out.println("captionNameelse***" + Teacher);

                }*/


             /*   String Caption = newhouseList.get(position).getCName();

                System.out.println("Teacher***" + Caption);

                if (Caption.equals("null") || Caption.trim().equals("") || Caption.trim().equals(null) || Caption.isEmpty()) {

                    holder.tvCaptionName.setVisibility(View.GONE);
                    System.out.println("Mentor Teacher***" + Caption);
                } else {
                    holder.tvCaptionName.setVisibility(View.VISIBLE);
                    holder.tvCaptionName.setText("Mentor Teacher : " + Caption);

                    System.out.println("captionNameelse***" + Caption);

                }
*/
                holder.llHouseAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final DashBoardMenuActivity examActivity = (DashBoardMenuActivity) context;
                        Bundle bundle= new Bundle();
                        bundle.putString("HouseID",newhouseList.get(position).getHouse_uuid());
                        bundle.putString("HouseName",newhouseList.get(position).getHouse_name());
                        bundle.putString("HouseColor",newhouseList.get(position).getHouse_color());
                        examActivity.loadFragment(Constant.ADD_HOUSES_LIST, bundle);
                    }
                });












               /* String captionName = newhouseList.get(position).getC_fname() + " "
                        + newhouseList.get(position).getC_lname();

                String vCaptionName = newhouseList.get(position).getViceC_fname() + " "
                        + newhouseList.get(position).getViceC_lname();

               // if (captionName.isEmpty() || captionName.equals("null")||captionName.equals("")) {
                if (holder.tvCaptionName.getText().toString().equals("null")) {
                    holder.tvCaptionName.setVisibility(View.GONE);

                } else {
                    holder.tvCaptionName.setVisibility(View.VISIBLE);
                    holder.tvCaptionName.setText("Captaian : "+vCaptionName);
                    //holder.tvViceCaptionName.setVisibilaty(View.GONE);
                }
*/

               /* holder.tvTeacherName.setText("Mentor Teacher : " + newhouseList.get(position).getMentor_fname() + " "
                        + newhouseList.get(position).getMentor_lname());
*/

               /* String captionName = newhouseList.get(position).getC_fname() + " "
                        + newhouseList.get(position).getC_lname();
*/



             /*   System.out.println("captionName2***" + newhouseList.get(position).getC_lname());
                System.out.println("captionName***" + newhouseList.get(position).getC_fname());
*/

//                holder.tvStudentCount.setTextColor(ContextCompat.getColor(context, Integer.parseInt(colorCode)));
                // holder.llHouseAddress.setBackgroundColor(Color.parseColor(colorCode));


//
//                holder.tvStudent.setTextColor(Color.parseColor(newhouseList.get(position).getHouse_color()));
                //              holder.llHouseAddress.setBackgroundColor(Color.parseColor(newhouseList.get(position).getHouse_color()));
                //            holder.tvTeacherName.setText(newhouseList.get(position).getMentor_fname()+" "
                //                  +newhouseList.get(position).getMentor_lname());
                //        holder.tvCaptionName.setText(newhouseList.get(position).getC_fname()+" "
                //              +newhouseList.get(position).getC_lname());
                //    holder.tvViceCaptionName.setText(newhouseList.get(position).getViceC_fname()+" "
                //          +newhouseList.get(position).getViceC_lname());

/*
 holder.tvStudentCount.setTextColor(Color.parseColor(houseList.get(position).getColorName()));
                holder.tvStudent.setTextColor(Color.parseColor(houseList.get(position).getColorName()));
                holder.llHouseAddress.setBackgroundColor(Color.parseColor(houseList.get(position).getColorName()));
                holder.tvTeacherName.setText(houseList.get(position).getTeacher());
                holder.tvCaptionName.setText(houseList.get(position).getCaption());
                holder.tvViceCaptionName.setText(houseList.get(position).getViceCaption());
                holder.tvHouseName.setText(houseList.get(position).getHouseName());

*/



               /* holder.ll_updatehouse .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        strOldHouseName = houseList.get(position).getHouseName();
                        strTeacherID = houseList.get(position).getTeacher();
                        strCaptionID = houseList.get(position).getCaption();
                        strViceCaptionID = houseList.get(position).getViceCaption();
                        strOldHColor = houseList.get(position).getColorName();
                        strStudentNo = houseList.get(position).getStudentNo();
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.update_houses_dialog);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();



                        updateHousePOP(strOldHouseName,strNewHouseName,strTeacherID,strCaptionID,
                                strViceCaptionID,strOldHColor,strNewHColor,strStudentNo);
                    }
                });
*/

                break;

            case Constant.BARRIERS_FRAG:

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataArrayList.get(position).setSelect(isChecked);
                        holder.check_box.setChecked(isChecked);
                        if (isChecked) {
                            holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {

                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }
                    }
                });

                //This is for Whatever click the values that are Selectable

                data = dataArrayList.get(position);
                holder.check_box.setText(data.getName());
                holder.check_box.setChecked(data.isSelect());
                if (data.isSelect()) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                    holder.check_box.setTextColor(Color.WHITE);
                    Log.d("Added", "onBindViewHolder: ");
                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }


                break;


            case Constant.DEPARTMENT_FRAG:

                Log.d("asdf", dataArrayList.size() + "");

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataArrayList.get(position).setSelect(isChecked);
                        holder.check_box.setChecked(isChecked);
                        if (isChecked) {

                            holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {

                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }


                    }
                });

                //This is for Whatever click the values that are Selectable

                data = dataArrayList.get(position);
                holder.check_box.setText(data.getName());
                holder.check_box.setChecked(data.isSelect());
                if (data.isSelect()) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                    holder.check_box.setTextColor(Color.WHITE);
                    Log.d("Added", "onBindViewHolder: ");
                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status;
                        if (holder.check_box.isChecked()) {
                            status = "true";
                        } else {
                            status = "false";
                        }
                        String deptName = holder.check_box.getText().toString();

                        JSONObject objectSize = new JSONObject();
                        JSONObject objectDept = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", deptName);
                            jsonObject.put("status", status);
                            objectSize.put("1", jsonObject);
                            objectDept.put("departments", objectSize);

                        } catch (JSONException je) {

                        }
                        updateDepartmentStatus(Constant.wingName, objectDept);

                    }
                });


                break;


        }


    }


    private void updateHousePOP(String strOldHouseName, String strNewHouseName, String strTeacherID, String strCaptionID,
                                String strViceCaptionID, String strOldHColor, String strNewHColor, String strStudentNo) {
        mApiService.updateHouse(Constant.SCHOOL_ID, strOldHouseName, strNewHouseName, strTeacherID, strCaptionID, strViceCaptionID
                , strOldHColor, strNewHColor, strStudentNo, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("Update_House", "" + response.raw().request().url() + "**" + response.body() + "**" + response.code());


                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void updateDepartmentStatus(String wingName, JSONObject objectDept) {
        mApiService.updateDeptStatus(Constant.SCHOOL_ID, wingName, objectDept, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("STATUS_UPDATE", "" + response.body());
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


    }


    private void OpenDialogUpdateAndDeleteRoles(final String role) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_delete_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText etOldRole = dialog.findViewById(R.id.etOldRole);
        final EditText newRole = dialog.findViewById(R.id.etNewRole);
        Button updateRole = dialog.findViewById(R.id.btnUpdateRole);
        final Button deleteRole = dialog.findViewById(R.id.btnDeleteRole);

        etOldRole.setText(role);
        etOldRole.setFocusable(false);
        etOldRole.setFocusableInTouchMode(false);


        updateRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateRole = newRole.getText().toString();
                if (!updateRole.equals("")) {
                    //updateRolesApi(role, updateRole);

                }

            }
        });


        deleteRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  deleteRoleApi(role);

            }
        });


        dialog.show();


    }


    @Override
    public int getItemCount() {
        switch (tag) {
            case Constant.BARRIERS_FRAG:
                return dataArrayList.size();

            case Constant.DEPARTMENT_FRAG:
                return dataArrayList.size();

            case Constant.RV_ADD_HOUSES:
                return newhouseList.size();
            //return houseList.size();

        }
        return 0;
    }


    public void newValues(String s) {
        dataArrayList.add(new Data(s, true));
        notifyDataSetChanged();
    }

}





