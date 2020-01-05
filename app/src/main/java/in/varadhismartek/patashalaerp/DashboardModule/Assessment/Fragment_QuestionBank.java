package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.Gallery.GalleryAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_QuestionBank extends Fragment implements View.OnClickListener {

    String strDiv = "", str_class = "ukg", strSection = "", str_subject = "", str_toDate = "", endYear = "", currentDate = "";
    private ArrayList<Uri> arrayList = new ArrayList<>();
    APIService mApiService, apiService;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSectionNew;
    ArrayList<String> listSubject;
    ArrayList<ClassSectionModel> modelArrayList;
    CustomSpinnerAdapter customSpinnerAdapter;
    TextView tvFromDate;
    ImageView iv_backBtn;
    Spinner spDivision, spnClass, spn_section, spn_Subject;
    RecyclerView recycler_view;
    Button btnSave;
    EditText edQuestionTitle, edQuestionDesc;
    ImageView ivAttachmentImage;
    int PICK_IMAGE_MULTIPLE = 1;

    String imageEncoded;
    List<String> imagesEncodedList = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<Uri> imageList= new ArrayList<>();
    GridView gvGallery;


    List<File> fileList = new ArrayList<>();
    public Fragment_QuestionBank() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__question_bank, container, false);


        initViews(view);
        //   initListeners();
        getDivisionApi();


        return view;
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        apiService = ApiUtilsPatashala.getService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        spDivision = view.findViewById(R.id.spn_division);
        spnClass = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_Subject = view.findViewById(R.id.spn_Subject);
        btnSave = view.findViewById(R.id.btn_submit);

        edQuestionTitle = view.findViewById(R.id.ed_question_title);
        edQuestionDesc = view.findViewById(R.id.ed_description);
        ivAttachmentImage = view.findViewById(R.id.imageView_attach);

        //  linearLayoutDate = view.findViewById(R.id.ll_date);
        tvFromDate = view.findViewById(R.id.tv_fromDate);
        recycler_view = view.findViewById(R.id.recycler_view);

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();

        modelArrayList = new ArrayList<>();
        listClass.add("Class");

        btnSave.setOnClickListener(this);
        ivAttachmentImage.setOnClickListener(this);
        // listSubject.add("Subject");
        // listSubject.clear();


    }


    private void getDivisionApi() {


        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "Division");
                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");

                                    listDivision.add(division);
                                }

                                Log.i("Division Object***", "" + listDivision + "***" + statusDivision);
                            }

                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spDivision.setAdapter(customSpinnerAdapter);

                            /*JSONArray array = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int i = 0; i < listDivision.size(); i++) {


                                try {
                                    array.put(listDivision.get(i));
                                    jsonObject.put("divisions", array);
                                    Constant.DIVISION_NAME = strDiv;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                               // getClassSectionList(jsonObject);
                            }
                            getClassSectionList(jsonObject);
                            System.out.println("jsonObject***"+jsonObject);*/

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

                //  setSpinnerForClass();

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
        listClass.add(0, "Class");
//        customSpinnerAdapter.notifyDataSetChanged();
        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.body());

                if (response.isSuccessful()) {
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

                                    listClass.add(className);

                                    modelArrayList.add(new ClassSectionModel(className, listSection));
                                    Gson gson = new Gson();

                                }
                            }


                        }

                    } catch (JSONException je) {

                    }

                } else {
                    if (String.valueOf(response.code()).equals("400")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else {
                        listClass.clear();
                        listClass.add("Class");
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spnClass.setAdapter(customSpinnerAdapter);


        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class = spnClass.getSelectedItem().toString();
                listSectionNew = new ArrayList<>();
                listSectionNew.clear();
                listSectionNew.add("Select Section");

                boolean b = true;

                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        listSectionNew.clear();

                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                            listSectionNew.add(modelArrayList.get(j).getListSection().get(k));

                        }
                        getSubject(strDiv, str_class);
                    }
                }


                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSectionNew);
                spn_section.setAdapter(customSpinnerAdapter);

                getSubject(strDiv, str_class);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strSection = spn_section.getSelectedItem().toString();
                getSubject(strDiv, str_class);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void getSubject(final String strDiv, final String str_class) {

        mApiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                listSubject = new ArrayList<>();

                if (response.isSuccessful()) {
                    try {

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

                                    Log.i("SubjectList***E", "" + response.code() + "**" + status + "***" + subjectkey);
                                    listSubject.add(subjectkey);


                                }
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                spn_Subject.setAdapter(customSpinnerAdapter);
                                // setRecyclerView();
                            }
                        } else {
                            listSubject.clear();
                            listSubject.add(0, "Subject");
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                            spn_Subject.setAdapter(customSpinnerAdapter);
                        }

                    } catch (JSONException je) {

                    }
                } else {

                    Log.i("SubjectList***F", "" + response.code());
                    listSubject.clear();
                    listSubject.add(0, "Subject");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                    spn_Subject.setAdapter(customSpinnerAdapter);
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SubjectList***B", "" + t.getMessage());

            }
        });
        spn_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_subject = spn_Subject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_submit:

                if (strDiv.equalsIgnoreCase("Division")) {
                    Toast.makeText(getActivity(), "Division ", Toast.LENGTH_SHORT).show();
                } else if (str_class.equalsIgnoreCase("Division")) {
                    Toast.makeText(getActivity(), "Class ", Toast.LENGTH_SHORT).show();
                } else {

                    uploadQuestionBank();

                    Toast.makeText(getActivity(), "Select Submit ", Toast.LENGTH_SHORT).show();


                }
                break;

            case R.id.imageView_attach:

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE_MULTIPLE);


                break;
        }


    }

    private void uploadQuestionBank() {



/*school_id:8f82a5f5-3441-49eb-b67a-c113668f9140
class:3
sections:A
subject:English
question_bank_title:Python question123
description:question bank
added_employeeid:396f016c-d2a1-4386-9c2b-c9c68cb643a0
question_bank_attachement:['question_bank_attachement1','question_bank_attachement2']*/



        String title = edQuestionTitle.getText().toString();
        String desc =  edQuestionDesc.getText().toString();


        RequestBody school_id = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
        RequestBody classes = RequestBody.create(MediaType.parse("text/plain"), str_class);
        RequestBody subject = RequestBody.create(MediaType.parse("text/plain"), strSection);
        RequestBody section = RequestBody.create(MediaType.parse("text/plain"), str_subject);
        RequestBody added_employeeid = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
        RequestBody ques_title = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody ques_desc = RequestBody.create(MediaType.parse("text/plain"), desc);


        System.out.println("school_id111***"+Constant.SCHOOL_ID+"**"+classes+"**"+str_class
                +"***"+strSection+"***"+str_subject+"****"+title+"**"+desc+"**"+Constant.EMPLOYEE_BY_ID);


        List<MultipartBody.Part> partList = new ArrayList<>();

        Log.d("file size", String.valueOf(fileList.size()));

        for (int i = 0; i < fileList.size(); i++) {

            Log.d("file_name", fileList.get(i).getName());
            Toast.makeText(getActivity(), fileList.get(i).getName(), Toast.LENGTH_SHORT).show();

            RequestBody image = RequestBody.create(MediaType.parse("image"), fileList.get(i));
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("question_bank_attachement",
                    fileList.get(i).getName(), image);

            partList.add(fileUpload);
        }

        Log.d("PartList", String.valueOf(partList.size()));
      System.out.println("school_id***"+school_id+"**"+classes+"***"+section+"***"+added_employeeid+"****"+ques_title+"**"+ques_desc+"***"+partList.toString());


        apiService.addQuestionBank(school_id,classes,section,subject,added_employeeid,ques_title,ques_desc,partList)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("QuestionBank_res",""+response.body()+"***"+response.code());
                if (response.isSuccessful()){

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK) {

            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();

                for (int i = 0; i < mClipData.getItemCount(); i++) {

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                        String path = saveImage(bitmap);

                        Log.d("File_PAth1", path);

                        File file = new File(path);
                        fileList.add(file);

                        Log.d("File_PAth2", String.valueOf(fileList.size())+"***"+fileList.get(0));


                        imageList.add(uri);
                       // adapter.notifyDataSetChanged();
                        Log.d("File_PAth1_uri", ""+uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                GalleryAdapter questionbankAdapter = new GalleryAdapter(fileList,getActivity(),Constant.RV_QUESTION_BANK);
                recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recycler_view.setAdapter(questionbankAdapter);


            }else {

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),mImageUri);
                        String path = saveImage(bitmap);

                        Log.d("File_PAth3", path);


                        File file = new File(path);
                        fileList.add(file);

                        Log.d("File_PAth4", String.valueOf(fileList.size()));


                        imageList.add(mImageUri);
                     //   adapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        }

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}







