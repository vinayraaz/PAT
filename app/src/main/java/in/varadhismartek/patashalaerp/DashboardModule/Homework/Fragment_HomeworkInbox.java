package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.DashboardModule.NotificationModule.NotificationModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_HomeworkInbox extends Fragment implements View.OnClickListener {


    String strDiv = "", str_class = "ukg", str_toDate = "", endYear = "", currentDate = "";

    HomeworkAdapter homeworkAdapter;
    APIService mApiService;
    LinearLayout linearLayoutDate;
    Calendar calendar;
    Date date1;

    private int year, month, date, rowNumber = 1;
    CustomSpinnerAdapter customSpinnerAdapter;
    TextView tvFromDate;
    ImageView iv_backBtn;
    Spinner spDivision, spnClass, spnSection, spnSubject, spnTeacher;
    RecyclerView recycler_view;
    FloatingActionButton fab;

    ArrayList<HomeworkModel> homeworkModelsInbox;
    ArrayList<HomeworkModel> homeworkModelsInboxDate;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSubject;
    String homeworkId, homeworkTitle, description, homeClass, section, subject, startDate, endDate;

    private ArrayList<NotificationModel> classSectionList;
    ArrayList<String> classList = new ArrayList<>();
    ArrayList<String> divisionList = new ArrayList<>();
    ArrayList<String> employeeNameList = new ArrayList<>();
    public Fragment_HomeworkInbox() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__homework_inbox, container, false);


        initViews(view);
        initListeners();
        getSchoolHomework();
        getDivisionApi();
        getTeacherList();
        //   getAllClasses();

        return view;
    }

    private void getTeacherList() {
        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                      //  employeeList.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("EMPLKEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("EMPKEYDATA", keyData.toString());

                               /* sex = keyData.getString("sex");
                                employee_status = keyData.getString("employee_status");
                                wing_name = keyData.getString("wing_name");
                                employee_photo = keyData.getString("employee_photo");
                                role = keyData.getString("role");

                                empUUId = keyData.getString("employee_uuid");
                                empFname = keyData.getString("first_name");
                                empLname = keyData.getString("last_name");
                                empPhoneNo = keyData.getLong("phone_number") + "";
                                empAdhaarNo = keyData.getString("adhaar_card_no");
                                empDept = keyData.getString("department_name");
                                Approved */
                                String employee_status = keyData.getString("employee_status");

                               String empFname = keyData.getString("first_name");
                                String empLname = keyData.getString("last_name");
                              //  Log.d("empPhoneNo", empPhoneNo);
                                String empName = empFname + " " + empLname;
                                if (employee_status.equalsIgnoreCase("Approved")) {
                                    employeeNameList.add(empName);
                                }

                              /*  employeeList.add(new EmpLeaveModel(empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept
                                        , employee_status, wing_name, employee_photo, role, sex));*/
                            }

                           // setRecylerView();
                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),employeeNameList);
                            spnTeacher.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException je) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void getSchoolHomework() {

        mApiService.getHomeWorkBySchool(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        Log.i("homeinboxSChool**", "" + response.body() + "*****" + response.code());

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = object.getJSONArray("datadict");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectData = jsonArray.getJSONObject(i);


                                homeworkId = objectData.getString("homework_uuid");
                                homeworkTitle = objectData.getString("homework_title");
                                description = objectData.getString("description");
                                homeClass = objectData.getString("class");
                                section = objectData.getString("section");
                                subject = objectData.getString("subject");
                                startDate = objectData.getString("start_date");
                                endDate = objectData.getString("end_date");

                                homeworkModelsInbox.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                        homeClass, section, subject, startDate, endDate));


                            }


                            setRecyclerView();
                        }

                    } catch (JSONException je) {

                    }

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


    private void initListeners() {
        fab.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        linearLayoutDate.setOnClickListener(this);
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        spDivision = view.findViewById(R.id.sp_division);
        spnClass = view.findViewById(R.id.sp_class);
        spnSection = view.findViewById(R.id.sp_section);
        spnSubject = view.findViewById(R.id.sp_subject);
        spnTeacher = view.findViewById(R.id.sp_teacher);
        linearLayoutDate = view.findViewById(R.id.ll_date);
        tvFromDate = view.findViewById(R.id.tv_fromDate);
        recycler_view = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        classSectionList = new ArrayList<>();
        homeworkModelsInbox = new ArrayList<>();
        homeworkModelsInboxDate = new ArrayList<>();
        spnSection.setVisibility(View.GONE);
        spnTeacher.setVisibility(View.GONE);
        spnSubject.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime());

        tvFromDate.setText(currentDate);
    }


    private void getDivisionApi() {


        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "-Division-");

                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");
                                    String school_id = object1.getString("school_id");
                                    String added_by_id = object1.getString("added_by_id");

                                    listDivision.add(division);
                                }


                            }
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spDivision.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strDiv = parent.getSelectedItem().toString();
                System.out.println("strDiv**" + strDiv);
                //  setSpinnerForClass();

                spnSection.setVisibility(View.GONE);
                spnTeacher.setVisibility(View.GONE);
                spnSubject.setVisibility(View.GONE);

                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                try {
                    array.put(strDiv);
                    jsonObject.put("divisions", array);
                    Constant.DIVISION_NAME = strDiv;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getClassSectionList(jsonObject);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getClassSectionList(JSONObject jsonObject) {

        listClass.clear();
        listClass.add(0, "-Class-");
        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                if (response.isSuccessful()) {
                    Log.i("CLASS_SECTIONDDD", "" + response.body() + "***" + response.code());
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                //modelArrayList.clear();
                                // customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();

                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");

                                    listSection = new ArrayList<>();
                                    String Section = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Section = jsonArray.getString(i);
                                        listSection.add(Section);
                                    }

                                    classSectionList.add(new NotificationModel(className, listSection));

                                    listClass.add(className);
                                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                                    spnClass.setAdapter(customSpinnerAdapter);

                                }
                            }


                        }


                        //  setRecyclerView();

                    } catch (JSONException je) {

                    }

                } else {

                    listClass.clear();
                    listClass.add("-Class-");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                    spnClass.setAdapter(customSpinnerAdapter);
                    customSpinnerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_class = parent.getItemAtPosition(position).toString();
                if (str_class.equalsIgnoreCase("-Class-")) {
                    //Toast.makeText(getActivity(), "Please select all fields", Toast.LENGTH_SHORT).show();

                } else {

                    ArrayList<String> listSection = new ArrayList<>();
                    listSection.clear();
                    listSection.add("-Section-");
                    spnSection.setVisibility(View.VISIBLE);
                    spnTeacher.setVisibility(View.VISIBLE);
                    spnSubject.setVisibility(View.VISIBLE);

                    str_class = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < classSectionList.size(); i++) {
                        if (str_class.contains(classSectionList.get(i).getClassName())) {
                            for (int j = 0; j < classSectionList.get(i).getSectionName().size(); j++) {
                                String strScetion = classSectionList.get(i).getSectionName().get(j);
                                System.out.println("strScetion***" + strScetion);

                                listSection.add(strScetion);

                            }
                        }
                    }

                    CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(getActivity(), listSection);
                    spnSection.setAdapter(customSpinnerAdapter1);

                    homeworkModelsInbox.clear();
                    homeworkModelsInboxDate.clear();

                    getSubject(strDiv, str_class);

                    getHomeWorkList(str_class);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getSubject(String strDiv, String str_class) {
        mApiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                    if (response.isSuccessful()) {
                        try {
                            listSubject = new ArrayList<>();
                            listSubject.clear();
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String status = (String) object.get("status");
                            Log.i("SubjectList***D", "" + response.code() + "**" + status);
                            if (status.equalsIgnoreCase("Success")) {

                                JSONObject jsonObject1 = object.getJSONObject("Section");

                                Iterator<String> keys = jsonObject1.keys();

                                while (keys.hasNext()) {
                                    String sectionkey = keys.next();
                                    JSONObject jsonSection = jsonObject1.getJSONObject(sectionkey);


                                    Iterator<String> keys2 = jsonSection.keys();

                                    while (keys2.hasNext()) {
                                        String subjectkey = keys2.next();
                                        JSONObject jsonSubject = jsonSection.getJSONObject(subjectkey);

                                        String strType = jsonSubject.getString("subject_type");
                                        String strCode = jsonSubject.getString("subject_code");
                                        boolean strStatus = jsonSubject.getBoolean("status");

                                        listSubject.add(subjectkey);


                                    }
                                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                    spnSubject.setAdapter(customSpinnerAdapter);
                                    // setRecyclerView();
                                }


                            } else {
                                listSubject.clear();
                                listSubject.add(0, "-Subject-");
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                spnSubject.setAdapter(customSpinnerAdapter);
                            }

                        } catch (JSONException je) {

                        }
                    } else {

                        Log.i("SubjectList***F", "" + response.code());
                        listSubject.clear();
                        listSubject.add(0, "-Subject-");
                        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                        spnSubject.setAdapter(customSpinnerAdapter);
                    }

                }



            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void getHomeWorkList(String str_class) {
        homeworkModelsInbox.clear();

        mApiService.getHomeWorkByClass(Constant.SCHOOL_ID, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("homeinbox**", "" + response.body() + "*****" + response.code());
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectData = jsonArray.getJSONObject(i);


                                homeworkId = objectData.getString("homework_id");
                                homeworkTitle = objectData.getString("homework_title");
                                description = objectData.getString("description");
                                homeClass = objectData.getString("class");
                                section = objectData.getString("section");
                                subject = objectData.getString("subject");
                                startDate = objectData.getString("start_date");
                                endDate = objectData.getString("end_date");

                                homeworkModelsInbox.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                        homeClass, section, subject, startDate, endDate));


                            }


                            setRecyclerView();
                        }

                    } catch (JSONException je) {

                    }

                } else {
                    Log.i("homeinbox**A", "" + response.body() + "*****" + response.code());
                    homeworkModelsInboxDate.clear();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }


   /* private void getAllClasses() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Class_api_0", String.valueOf(divisionList.size()));

                progressDialog.dismiss();

                // if (divisionList.size() > 0) {

                Log.d("Class_api_1", String.valueOf(divisionList.size()));

                JSONObject object = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                jsonArray.put("All");


                try {
                    object.put("divisions", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Class_api_2", object.toString());

                if (classList.size() > 0)
                    classList.clear();
                classList.add("-Class-");
                mApiService.getClassSections(Constant.SCHOOL_ID, object).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        if (response.isSuccessful()) {
                            try {

                                JSONObject obj = null;

                                obj = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("myDivisionKey_data2", String.valueOf(response.code()));

                                //JSONObject obj = new JSONObject(new Gson().toJson(response.body()));

                                String status = obj.getString("status");
                                Log.d("myDivisionKey_data2", obj.toString());


                                if (status.equalsIgnoreCase("Success")) {

                                    JSONObject jsonData = obj.getJSONObject("data");
                                    Log.d("myDivisionKey_data1", jsonData.toString());


                                    Iterator<String> key = jsonData.keys();

                                    while (key.hasNext()) {

                                        String myDivisionKey = key.next();
                                        Log.d("myDivisionKey", myDivisionKey);

                                        JSONObject keyData = jsonData.getJSONObject(myDivisionKey);
                                        Log.d("myDivisionKey_data", keyData.toString());

                                        Iterator<String> classKey = keyData.keys();

                                        while (classKey.hasNext()) {

                                            JSONObject classData = keyData.getJSONObject(classKey.next());
                                            Log.d("classData", classData.toString());

                                            String class_name = classData.getString("class_name");
                                            Log.d("className", class_name);
                                            classList.add(class_name);


                                            JSONArray jsonArray1 = classData.getJSONArray("sections");
                                            Log.d("classData_array", jsonArray1.toString());

                                            ArrayList<String> sections = new ArrayList<>();

                                            for (int i = 0; i < jsonArray1.length(); i++) {

                                                sections.add(jsonArray1.getString(i));
                                                Log.d("classData_array " + i, jsonArray1.getString(i));

                                            }

                                            classSectionList.add(new NotificationModel(class_name, sections));
                                        }
                                    }

                                }

                                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), classList);
                                spnClass.setAdapter(customSpinnerAdapter);

                                spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        ArrayList<String> listSection = new ArrayList<>();
                                        listSection.clear();
                                        listSection.add("-Select-");
                                        str_class = parent.getItemAtPosition(position).toString();
                                        for (int i = 0; i < classSectionList.size(); i++) {
                                            if (str_class.contains(classSectionList.get(i).getClassName())) {
                                                for (int j = 0; j < classSectionList.get(i).getSectionName().size(); j++) {
                                                    String strScetion = classSectionList.get(i).getSectionName().get(j);
                                                    System.out.println("strScetion***" + strScetion);

                                                    listSection.add(strScetion);

                                                }
                                            }
                                        }

                                        CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(getActivity(), listSection);
                                        spnSection.setAdapter(customSpinnerAdapter1);


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("create_API", message);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });

            }

            // }
        }, 2000);


    }
*/

    private void setRecyclerView() {

        homeworkAdapter = new HomeworkAdapter(homeworkModelsInbox, getActivity(),
                tvFromDate.getText().toString(), Constant.RV_HOMEWORK_INBOX_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(homeworkAdapter);
        homeworkAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {

        DashBoardMenuActivity homeWorkActivity = (DashBoardMenuActivity) getActivity();

        switch (view.getId()) {

            case R.id.ll_date:
                openDateDialog();
                break;
            case R.id.fab:

                homeWorkActivity.loadFragment(Constant.HOMEWORK_CREATE_FRAGMENT, null);
                break;

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }
    }

    private void openDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());


                endYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                tvFromDate.setText(str_toDate);
                getHomeWorkListNew(str_class);


            }
        }, year, month, date);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();

    }

    private void getHomeWorkListNew(String str_class) {

        homeworkModelsInbox.clear();
        homeworkModelsInboxDate.clear();

        mApiService.getHomeWorkByClass(Constant.SCHOOL_ID, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("homeinbox**", "" + response.body() + "*****" + response.code());
                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectData = jsonArray.getJSONObject(i);


                                homeworkId = objectData.getString("homework_id");
                                homeworkTitle = objectData.getString("homework_title");
                                description = objectData.getString("description");
                                homeClass = objectData.getString("class");
                                section = objectData.getString("section");
                                subject = objectData.getString("subject");
                                startDate = objectData.getString("start_date");
                                endDate = objectData.getString("end_date");
                                if (startDate.equals(tvFromDate.getText().toString())) {

                                    homeworkModelsInboxDate.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                            homeClass, section, subject, startDate, endDate));
                                }


                            }

                            homeworkAdapter = new HomeworkAdapter(homeworkModelsInboxDate, getActivity(),
                                    tvFromDate.getText().toString(), Constant.RV_HOMEWORK_INBOX_ROW);
                            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recycler_view.setAdapter(homeworkAdapter);
                            homeworkAdapter.notifyDataSetChanged();

                            //  setRecyclerView();
                        }

                    } catch (JSONException je) {

                    }

                } else {
                    Log.i("homeinbox**B", "" + response.body() + "*****" + response.code());
                    homeworkModelsInboxDate.clear();
                    homeworkAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


}
