package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_EmployeeList extends Fragment {
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<EmpLeaveModel> employeeList, newEmployeeList;
    Spinner spn_employee;
    APIService mApiService;
    RecyclerView recycler_view;
    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept;
    String fragmentType = "", employee_status, wing_name, employee_photo, role, sex;
    TextView tvTitle;
    ArrayList<String> arrayListDept = new ArrayList<>();
    ArrayList<String> arrayListRoles = new ArrayList<>();
    ArrayList<Data> list = new ArrayList<>();
    Spinner sp_dept, sp_roles;
    JSONObject wingsJsonObject = new JSONObject();
    String deptValue, strRoles;

    public Fragment_EmployeeList() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);

        initViews(view);

        getDepartment();
        getRoles();

        getAllEmployeeAPI();
        return view;
    }


    //private void fileter(String deptValue, String strRoles) {
    private void fileter(String strRoles) {
        if (employeeList.size() > 0) {
            newEmployeeList.clear();
            for (int i = 0; i < employeeList.size(); i++) {
                if (strRoles.contains(employeeList.get(i).getEmpDept()) || (strRoles.contains(employeeList.get(i).getRole()))) {
                    String empName = employeeList.get(i).getEmpFname();
                    Log.i("empName**", empName);

                    newEmployeeList.add(new EmpLeaveModel(
                            employeeList.get(i).getEmpFname(),
                            employeeList.get(i).getEmpLname(),
                            employeeList.get(i).getEmpUUId(),
                            employeeList.get(i).getEmpPhoneNo(),
                            employeeList.get(i).getEmpAdhaarNo(),
                            employeeList.get(i).getEmpDept(),
                            employeeList.get(i).getEmployee_status(),
                            employeeList.get(i).getWing_name(),
                            employeeList.get(i).getEmployee_photo(),
                            employeeList.get(i).getRole(),
                            employeeList.get(i).getSex()

                    ));
                }
            }
            setRecylerViewFilter(newEmployeeList);

        }

    }

    private void setRecylerViewFilter(ArrayList<EmpLeaveModel> newEmployeeList) {

        EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), this.newEmployeeList, Constant.RV_EMPLOYEE_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getDepartment() {
        try {
            JSONArray wingsArray = new JSONArray();
            Constant.DEPART_NAME = "departments";
            wingsArray.put("All");
            wingsJsonObject.put("wings", wingsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        arrayListDept = new ArrayList<>();
        arrayListDept.clear();
        arrayListDept.add(0, "Department");
        mApiService.gettingDept(Constant.SCHOOL_ID, wingsJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {

                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(Constant.DEPART_NAME);

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String wing_name = jsonObjectValue.getString("name");
                                boolean dept_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (dept_status) {
                                    arrayListDept.add(wing_name);
                                }

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));

                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("Role ERROR", "" + t.toString());

            }
        });


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListDept);
        sp_dept.setAdapter(customSpinnerAdapter);
        sp_dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONArray deptArray = new JSONArray();
                JSONObject deptJsonObject = new JSONObject();

                getAllEmployeeAPI();

                deptValue = parent.getSelectedItem().toString();

                if (deptValue.equalsIgnoreCase("Department")) {
                    sp_roles.setVisibility(View.INVISIBLE);

                } else {

                    Constant.deptName = deptValue;
                    sp_roles.setVisibility(View.VISIBLE);
                    try {
                        Constant.ROLES_NAME = deptValue;
                        deptArray.put(Constant.deptName);
                        deptJsonObject.put("departments", deptArray);
                        fileter(deptValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void getRoles() {

        JSONObject wingsJsonObject = new JSONObject();
        JSONObject deptJsonObject = new JSONObject();
        JSONArray deptArray = new JSONArray();
        try {
            deptArray.put("All");
            wingsJsonObject.put("wings", deptArray);
            deptJsonObject.put("departments", deptArray);
        } catch (JSONException j) {

        }

        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        arrayListRoles.clear();
                        arrayListRoles.add("-Roles-");
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("roles");


                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String rolesName = jsonObjectValue.getString("role");
                                boolean roles_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (roles_status) {
                                    arrayListRoles.add(rolesName);
                                }
                            }
                            Log.i("makerDATA", "" + arrayListRoles);
                        }

                        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListRoles);
                        sp_roles.setAdapter(customSpinnerAdapter);
                        sp_roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                strRoles = sp_roles.getItemAtPosition(position).toString();
                                System.out.println("strROLS**" + strRoles);

                                //fileter(deptValue, strRoles);
                                fileter(strRoles);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {

                    }
                    if (String.valueOf(response.code()).equals("404")) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();

        recycler_view = view.findViewById(R.id.recyclerview);
        sp_dept = view.findViewById(R.id.sp_dept);
        sp_roles = view.findViewById(R.id.sp_roles);
        tvTitle = view.findViewById(R.id.tvTitle);

        tvTitle.setText("Employee List ");

        employeeList = new ArrayList<>();
        newEmployeeList = new ArrayList<>();
        sp_roles.setVisibility(View.GONE);
        // getAllEmployeeAPI();

    }

    private void getAllEmployeeAPI() {
        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        employeeList.clear();
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


                                if (keyData.isNull("first_name")) {
                                    empFname = "";
                                } else {
                                    empFname = keyData.getString("first_name");
                                }


                                if (keyData.isNull("last_name")) {
                                    empLname = "";
                                } else {
                                    empLname = keyData.getString("last_name");
                                }

                                if (keyData.isNull("employee_uuid")) {
                                    empUUId = "";
                                } else {
                                    empUUId = keyData.getString("employee_uuid");
                                }


                                if (keyData.isNull("phone_number")) {
                                    empPhoneNo = "";
                                } else {
                                    empPhoneNo = keyData.getLong("phone_number")+"";
                                }

                                if (keyData.isNull("adhaar_card_no")) {
                                    empAdhaarNo = "";
                                } else {
                                    empAdhaarNo = keyData.getString("adhaar_card_no");
                                }
                                if (keyData.isNull("department_name")) {
                                    empDept = "";
                                } else {
                                    empDept = keyData.getString("department_name");
                                }
                                if (keyData.isNull("employee_status")) {
                                    employee_status = "";
                                } else {
                                    employee_status = keyData.getString("employee_status");
                                }
                                if (keyData.isNull("wing_name")) {
                                    wing_name = "";
                                } else {
                                    wing_name = keyData.getString("wing_name");
                                }


                                if (keyData.isNull("employee_photo")) {
                                    employee_photo = "";
                                } else {
                                    employee_photo = keyData.getString("employee_photo");
                                }


                                if (keyData.isNull("role")) {
                                    role = "";
                                } else {
                                    role = keyData.getString("role");
                                }

                                if (keyData.isNull("sex")) {
                                    sex = "";
                                } else {
                                    sex = keyData.getString("sex");
                                }



                                String empName = empFname + " " + empLname;
                                employeeNameList.add(empName);
                               // if (employee_status.equalsIgnoreCase("Approved")) {
                                    employeeList.add(new EmpLeaveModel(empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept
                                            , employee_status, wing_name, employee_photo, role, sex));

                              //  }

                                System.out.println("data1****" + employeeNameList.size());


                            }
                            setRecylerView(employeeList);

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

    private void setRecylerView(ArrayList<EmpLeaveModel> employeeList) {
        Gson g = new Gson();

        System.out.println("data****" + g.toJson(this.employeeList));

        EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), this.employeeList, Constant.RV_EMPLOYEE_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
