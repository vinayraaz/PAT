package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.Adapters.RecyclerAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.StudentModule.StudentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_HouseBarrier extends Fragment implements View.OnClickListener {
    APIService mApiService;
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> captionNameList = new ArrayList<>();
    ArrayList<String> viceCaptionNameList = new ArrayList<>();

    ArrayList<EmpLeaveModel> employeeList, captionList ;

    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept, colorName;
    Spinner spTeacher, sp_Caption, sp_ViceCaption;
    CustomSpinnerAdapter customSpinnerAdapter;
    String empName = "";
    String strTeacherName, strTeacherUUID, strCaptionName, strCaprionId, strViceCaptionName, strViceCaprionId;
    LinearLayout linearLayoutAdd;
    EditText edStudentNo, edHouseName;
    Spinner tvColorCode;
    ArrayList<String> Colorcategories = new ArrayList<>();
    ArrayList<StudentModel> studentModels = new ArrayList<>();
    ArrayList<StudentModel> viceCaptionList = new ArrayList<>();

    String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo,
            strStatus, strdeleted, strPhoto;
    private ArrayList<HouseModule> houseModules = new ArrayList<>();

    String number_of_students = "", house_name = "", house_color = "", house_uuid = "",
            mentor_fname, mentor_lname = "", mentor_id = "", mentor_no = "", mentor_adharno = "",
            viceC_fname = "", viceC_lname = "", viceC_id = "", viceCDOB = "", viceCbirthID = "",
            C_fname = "", C_lname, C_Id = "", C_DOB = "", C_BID = "";
    private RecyclerView rvAddHouses;
    TextView tvAdd;
    public Fragment_HouseBarrier() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_houses_dialog, container, false);

        intiView(view);

        getHouseApi();

        return view;

    }

    private void getHouseApi() {
        mApiService.getHouseList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    Log.i("House**", "" + response.body());
                    Log.i("House**", "" + response.code());
                    try {
                        houseModules.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        System.out.println("MEssage**C*" + object);
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject objectData = jsonObject.getJSONObject(key);


                                number_of_students = objectData.getString("number_of_students");
                                house_name = objectData.getString("house_name");
                                house_color = objectData.getString("house_color");
                                house_uuid = objectData.getString("house_uuid");


                                String mName = "", CName = "";
                          /*      if (objectData.getJSONObject("house_mentor").toString().equalsIgnoreCase("{}")) {

                                    mentor_fname = "";
                                    mentor_lname = "";
                                    mentor_id = "";
                                    mentor_no = "";
                                    mentor_adharno = "";

                                } else {
                                    JSONObject mentorJSON = objectData.getJSONObject("house_mentor");

                                    mentor_fname = mentorJSON.getString("Mentor first name");
                                    mentor_lname = mentorJSON.getString("Mentor last name");
                                    mentor_id = mentorJSON.getString("Mentor_id");
                                    mentor_no = mentorJSON.getString("Mentor contact no");
                                    mentor_adharno = mentorJSON.getString("Mentor adhar card no");

                                    mName = mentor_fname + " " + mentor_lname;

                                    System.out.println("mName******" + mName);
                                }


                                if (objectData.getJSONObject("house_captain").toString().equalsIgnoreCase("{}")
                                        ||objectData.getJSONObject("house_captain").toString().isEmpty()) {
                                    System.out.println("CName1**" + CName);

                                    C_fname = "";
                                    C_lname = "";
                                    C_Id = "";
                                    C_DOB = "";
                                    C_BID = "";
                                    CName = C_fname+" "+C_lname;
                                } else {
                                    JSONObject captionJSON = objectData.getJSONObject("CName**");

                                    C_fname = captionJSON.getString("Captain first name");
                                    C_lname = captionJSON.getString("Captain last name");
                                    C_Id = captionJSON.getString("Captain id");
                                    C_DOB = captionJSON.getString("Captain dob");
                                    C_BID = captionJSON.getString("Captain birth certificate number");

                                    CName = C_fname+" "+C_lname;

                                    System.out.println("CName**" + CName);
                                }

                               if (objectData.getJSONObject("house_vice_captain").toString().equalsIgnoreCase("{}")){

                                    viceC_fname="";
                                    viceC_lname="";
                                    viceC_id="";
                                    viceCDOB="";
                                    viceCbirthID="";
                                }else {

                                    viceC_fname= objectData.getString("Vice captain first name");
                                    viceC_lname= objectData.getString("Vice captain last name");
                                    viceC_id= objectData.getString("Vice captain id");
                                    viceCDOB= objectData.getString("Vice captain dob");
                                    viceCbirthID= objectData.getString("Vice captain birth certificate number");
                                }

                               */

                                houseModules.add(new HouseModule(
                                        number_of_students,house_name,house_color,house_uuid,
                                        mentor_fname,mentor_lname,mentor_id,mentor_no,mentor_adharno));

                          /*      houseModules.add(new HouseModule(
                                        number_of_students, house_name, house_color, house_uuid,
                                        mentor_id, mentor_no, mentor_adharno,
                                        C_Id, C_DOB, C_BID, mName, CName));*/

                               /* houseModules.add(new HouseModule(
                                        number_of_students,house_name,house_color,house_uuid,
                                        mentor_fname,mentor_lname,mentor_id,mentor_no,mentor_adharno));*/
/*

                                houseModules.add(new HouseModule(
                                        number_of_students,house_name,house_color,house_uuid,

                                        mentor_fname,mentor_lname,mentor_id,mentor_no,mentor_adharno,
                                        viceC_fname,viceC_lname,viceC_id,viceCDOB,viceCbirthID,
                                        C_fname,C_lname,C_Id,C_DOB,C_BID));*/


                            }
                        }
                        System.out.println("houseModules*****" + houseModules.size() + "****" + houseModules.get(0).getCName());
                        setRecyclerView();
                    } catch (JSONException je) {

                    }


                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView() {


        RecyclerAdapter adapter = new RecyclerAdapter(houseModules, getActivity(), Constant.RV_ADD_HOUSES);
        rvAddHouses.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvAddHouses.setAdapter(adapter);


    }

    private void intiView(View view) {

        mApiService = ApiUtils.getAPIService();
        rvAddHouses = view.findViewById(R.id.rvAddHouses);
        spTeacher = view.findViewById(R.id.sp_teacher1);
        sp_Caption = view.findViewById(R.id.sp_Caption);
        sp_ViceCaption = view.findViewById(R.id.sp_ViceCaption);
       // linearLayoutAdd = view.findViewById(R.id.llAdd);
        edHouseName = view.findViewById(R.id.etHouseName);
        edStudentNo = view.findViewById(R.id.ed_studentNo);
        tvColorCode = view.findViewById(R.id.tvcolorcode);
        tvAdd = view.findViewById(R.id.tvAdd);
//        linearLayoutAdd.setOnClickListener(this);
        tvAdd.setOnClickListener(this);

        employeeList = new ArrayList<>();
        captionList = new ArrayList<>();
        viceCaptionList = new ArrayList<>();


        Colorcategories.clear();

        Colorcategories.add("Green");
        Colorcategories.add("Blue");
        Colorcategories.add("Red");
        Colorcategories.add("Yellow");
        Colorcategories.add("Orange");


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), Colorcategories);

        tvColorCode.setAdapter(customSpinnerAdapter);

        tvColorCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        colorName = "#1CBE22";
                        break;

                    case 1:
                        colorName = "#023EF8";
                        break;

                    case 2:
                        colorName = "#F81200";
                        break;

                    case 3:
                        colorName = "#FF9800";
                        break;

                    case 4:
                        colorName = "#FF5722";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    @Override
    public void onClick(View v) {


        if (edHouseName.getText().toString().isEmpty() ) {
            Toast.makeText(getActivity(), "Fill house name and no of student", Toast.LENGTH_SHORT).show();
        } else {

            mApiService.AddHouse(Constant.SCHOOL_ID, edHouseName.getText().toString(), colorName, Constant.EMPLOYEE_BY_ID)
                    .enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Log.i("ADD_HOUSE**2", "" + Constant.SCHOOL_ID+
                                    edHouseName.getText().toString()+colorName+
                                    Constant.EMPLOYEE_BY_ID);

                            Log.i("ADD_HOUSE**2", "" + response.body() + "**" + response.code());
                            if (response.isSuccessful()) {
                                try {
                                    Toast.makeText(getActivity(), "House Barriers have save successfully",
                                            Toast.LENGTH_SHORT).show();
                                    getHouseApi();
                                    edHouseName.setText("");
                                    edStudentNo.setText("");
                                    spTeacher.setSelection(0);
                                    sp_Caption.setSelection(0);
                                    sp_ViceCaption.setSelection(0);
                                } catch (Exception e) {

                                }
                            }else {
                                Toast.makeText(getActivity(), "Already Added to particular color of house ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });

        }


    }
}

