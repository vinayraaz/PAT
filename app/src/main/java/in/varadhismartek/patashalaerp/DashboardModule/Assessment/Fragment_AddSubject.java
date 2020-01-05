package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_AddSubject extends Fragment implements View.OnClickListener {
    private Spinner spDivision, spClass, spSection, spSubject;
    TextView tvAdd;
    EditText edSubName, edSubCode;
    Spinner edSubType;
    APIService apiService;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> sectionClist;
    ArrayList<String> listSection;
    ArrayList<String> listSubject;
    ArrayList<ClassSectionModel> modelArrayList;
    ArrayList<SectionSubjectModel> sectionSubjectModels;
    ArrayList<SectionSubjectModel> sectionSubjectModelsNew;
    String strDiv = "", str_class = "", strSection = "";
    CustomSpinnerAdapter customSpinnerAdapter;
    RecyclerView recyclerView;
    String editvalue = "",strSubType;
    AssessmentAdapter adapter;
    LinearLayout ll_subject;
    TextView tvSection;
    ArrayList<String> subjectList= new ArrayList<>();
    public Fragment_AddSubject() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_subject, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initView(view);
        initListener();
        getDivision();




        return view;

    }


    private void initListener() {
        tvAdd.setOnClickListener(this);

    }

    private void initView(View view) {
        spDivision = view.findViewById(R.id.spDivision);
        spClass = view.findViewById(R.id.spClass);
        spSection = view.findViewById(R.id.spSection);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvSection = view.findViewById(R.id.section_tv);
        ll_subject = view.findViewById(R.id.ll_subject);


        tvAdd = view.findViewById(R.id.tvAdd);
        edSubName = view.findViewById(R.id.ed_sub_name);
        edSubCode = view.findViewById(R.id.ed_sub_code);
        edSubType = view.findViewById(R.id.ed_subject_type);
        apiService = ApiUtils.getAPIService();


        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        listSubject = new ArrayList<>();

        listSection = new ArrayList<>();
        sectionSubjectModels = new ArrayList<>();
        sectionSubjectModelsNew = new ArrayList<>();

        spSection.setVisibility(View.GONE);
        edSubName.setVisibility(View.GONE);
        edSubCode.setVisibility(View.GONE);
        edSubType.setVisibility(View.GONE);
        subjectList.add("Theory");
        subjectList.add("Practical");
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),subjectList);
        edSubType.setAdapter(customSpinnerAdapter);
        edSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 strSubType = edSubType.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    private void getDivision() {
        apiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
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

                listClass.clear();
                listSection.clear();
                listSubject.clear();
                strDiv = parent.getSelectedItem().toString();

                spSection.setVisibility(View.GONE);
                edSubName.setVisibility(View.GONE);
                edSubCode.setVisibility(View.GONE);
                edSubType.setVisibility(View.GONE);
                tvSection.setText("");
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
        sectionSubjectModels.clear();
        sectionSubjectModelsNew.clear();
        listSection.clear();
        listClass.clear();
        listClass.add(0, "-Class-");

        apiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        System.out.println("SectionList***object"+object);
                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                modelArrayList.clear();
                                customSpinnerAdapter.notifyDataSetChanged();
                               // setSpinnerForClass();

                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");

                                    sectionClist = new ArrayList<>();

                                    String Section = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Section = jsonArray.getString(i);
                                        sectionClist.add(Section);
                                    }
                                    System.out.println("SectionList***"+sectionClist);

                                    listClass.add(className);
                                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                                    spClass.setAdapter(customSpinnerAdapter);

                                    modelArrayList.add(new ClassSectionModel(className, sectionClist));
                                }
                            }


                        }


                        //   setRecyclerView();

                    } catch (JSONException je) {

                    }

                } else {
                    listClass.clear();
                  //  setSpinnerForClass();// no data in getsectionAOI
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spClass.setAdapter(customSpinnerAdapter);

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listSection.clear();
                listSection.add("-Section-");
                sectionSubjectModels.clear();
                adapter.notifyDataSetChanged();
                str_class = adapterView.getSelectedItem().toString();
                System.out.println("SectionList***2 "+sectionClist);

                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {
                            listSection.add(modelArrayList.get(j).getListSection().get(k));

                        }

                        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSection);
                        spSection.setAdapter(customSpinnerAdapter);


                        spSection.setVisibility(View.VISIBLE);
                        edSubName.setVisibility(View.VISIBLE);
                        edSubCode.setVisibility(View.VISIBLE);
                        edSubType.setVisibility(View.VISIBLE);

                        getSubject(strDiv, str_class);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strSection = spSection.getSelectedItem().toString();
                System.out.println("strSection****"+strSection);
                if (!strSection.equalsIgnoreCase("-Section-")){
                    tvSection.setText(strSection);
                }else {
                    tvSection.setText("");
                }
                if (sectionSubjectModels.size() > 0) {
                    sectionSubjectModelsNew.clear();
                    listSubject.clear();
                    //  spSubject.setVisibility(View.VISIBLE);
                    edSubName.setVisibility(View.VISIBLE);
                    for (int p = 0; p < sectionSubjectModels.size(); p++) {
                        if (sectionSubjectModels.get(p).getSectionName().contains(strSection)) {
                            listSubject.add(sectionSubjectModels.get(p).getSubjectName());


                            sectionSubjectModelsNew.add(new SectionSubjectModel(
                                    sectionSubjectModels.get(p).getSectionName(),
                                    sectionSubjectModels.get(p).getSubjectName(),
                                    sectionSubjectModels.get(p).getSubCode(),
                                    sectionSubjectModels.get(p).getSubType(),
                                    sectionSubjectModels.get(p).getDivisionName(),
                                    sectionSubjectModels.get(p).getClassName(),
                                    sectionSubjectModels.get(p).isIsselect()
                            ));
                        }
                    }
                    setRecyclerView();


                } else {

                    edSubName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setRecyclerView() {

        adapter = new AssessmentAdapter(sectionSubjectModelsNew, Constant.RV_SUBJECT_ROW, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getSubject(final String strDiv, final String str_class) {
        sectionSubjectModels.clear();
        sectionSubjectModelsNew.clear();
        apiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        ll_subject.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        System.out.println("subject object***"+object);
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
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

                                   // tvSection.setText(sectionkey);

                                    // sectionSubjectModels.add(new SectionSubjectModel(sectionkey, subjectkey));
                                    sectionSubjectModels.add(new SectionSubjectModel(sectionkey,subjectkey,strCode,strType,
                                            strDiv,str_class,strStatus));

                                }

                                if (sectionSubjectModelsNew.size()>0) {

                                    setRecyclerViewNew(sectionSubjectModels);
                                }
                            }
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

    private void setSpinnerForClass() {
        // listClass = new ArrayList<>();
        listClass.clear();
        listSubject.clear();
        sectionSubjectModels.clear();
        sectionSubjectModelsNew.clear();
        listClass.add(0, "-Class-");

        SharedPreferences preferences = getActivity().getSharedPreferences("DIV_CLASS", Context.MODE_PRIVATE);
        String strSecDivClass = preferences.getString("DIV_CLASS", "");
        try {
            JSONArray jsonArray = new JSONArray(strSecDivClass);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String DivName = jsonArray.getJSONObject(i).getString("divisionName");

                if (strDiv.isEmpty() || strDiv.equals("")) {
                } else if (strDiv.equals(DivName)) {
                    JSONArray jsonArray1 = jsonObject.getJSONArray("classList");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        String ClassName = jsonArray1.getJSONObject(j).getString("name");
                        listClass.add(ClassName);
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spClass.setAdapter(customSpinnerAdapter);

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listSection.clear();
                listSubject.clear();
                listSection.add("-Section-");
                str_class = adapterView.getSelectedItem().toString();
                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {
                            listSection.add(modelArrayList.get(j).getListSection().get(k));
                        }

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.tvAdd:
                editvalue = edSubName.getText().toString();
                if (edSubName.getText().toString().isEmpty() || edSubCode.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all field", Toast.LENGTH_SHORT).show();
                } else {
                    if (listSubject.size() > 0) {

                        boolean b = true;
                        for (int i = 0; i < listSubject.size(); i++) {
                            if ((listSubject.get(i)).contains(editvalue)) {
                                b = false;
                                Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (b) {

                            listSubject.add(editvalue);

                            sectionSubjectModels.add(new SectionSubjectModel(strSection,editvalue,edSubCode.getText().toString(),
                                    strSubType, strDiv,str_class,true));
                            Log.i("ADDSUB***B*", "" + Constant.SCHOOL_ID+"***"+Constant.EMPLOYEE_BY_ID+"**"+str_class+"**"+strDiv);


                            subjectSave();

                        }

                    } else {
                        sectionSubjectModels.add(new SectionSubjectModel(strSection,editvalue,edSubCode.getText().toString(),
                                strSubType, strDiv,str_class,true));
                        listSubject.add(editvalue);

                        subjectSave();
                    }

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    System.out.println("listSubject***" + listSubject);


                    setRecyclerView();

                }
                break;
        }
    }

    private void subjectSave() {
        Log.i("ADDSUB***C*", "" + Constant.SCHOOL_ID+"***"+Constant.EMPLOYEE_BY_ID+"**"+str_class+"**"+strDiv);

        JSONObject objectMain = new JSONObject();
        JSONObject objectSec = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();


        try {
            object.put("subject_name", edSubName.getText().toString());
            object.put("subject_code", edSubCode.getText().toString());
            object.put("subject_type", strSubType);
            jsonArray.put(object);

            objectSec.put(strSection, jsonArray);
            objectMain.put("sections", objectSec);

        } catch (JSONException je) {

        }
        addSubjectApi(objectMain);

    }

    private void addSubjectApi(JSONObject objectMain) {

        apiService.addSubject(Constant.SCHOOL_ID, strDiv, str_class, objectMain, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        Toast.makeText(getActivity(),"Subject added successfully",Toast.LENGTH_SHORT).show();
                        Log.i("ADDSUB***Save*", "" + response.body());
                        Log.i("ADDSUB***", "" + response.code());

                        spDivision.setSelection(0);
                        spClass.setSelection(0);
                        spSection.setSelection(0);
                        edSubName.setText("");
                        edSubCode.setText("");
                        edSubType.setSelection(0);


                        getSubject(strDiv,str_class);
                        adapter.notifyDataSetChanged();
                        //setRecyclerViewNew(sectionSubjectModels);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });



    }

    private void setRecyclerViewNew(ArrayList<SectionSubjectModel> sectionSubjectModels) {

        adapter = new AssessmentAdapter(sectionSubjectModels, Constant.RV_SUBJECT_ROW_CLASS, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}