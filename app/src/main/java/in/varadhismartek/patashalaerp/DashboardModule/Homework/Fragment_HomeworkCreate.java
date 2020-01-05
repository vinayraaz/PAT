package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.SectionSubjectModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import static android.app.Activity.RESULT_OK;

public class Fragment_HomeworkCreate extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    RecyclerView rv_book, rv_URL, rv_attachment;
    TextView tv_book_minus, tv_book_count, tv_book_plus;
    TextView tv_url_minus, tv_url_count, tv_url_plus;
    Spinner spn_division, spn_class, spn_section, spn_Subject;
    EditText et_work_title, et_description;
    TextView tv_fromDate, tv_toDate;
    Button btn_submit;
    LinearLayout ll_book_row, ll_url_row;

    int bookCount = 1, urlCount = 1;
    String currentDate = "", fromDate = "", toDate = "";
    int date, month, year;
    String str_class = "", strSection = "", str_subject = "";

    ArrayList<String> listBook;
    ArrayList<String> listUrl;
    ArrayList<ClassSectionModel> modelArrayList;
    ArrayList<EditText> etListBookName, etListPageNo,etListBookAuthor, etListURL;
    ArrayList<ImageView> ivBookImageList = new ArrayList<>();

    APIService mApiService;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSectionNew;
    ArrayList<String> listSubject;
  //  ArrayList<String> imageList;
    ArrayList<String> referenceAttachImage;
    ArrayList<SectionSubjectModel> sectionSubjectModels;
    CustomSpinnerAdapter customSpinnerAdapter;
    private String strDiv = "";
    ImageView bookImage;
    private static int FROM_GALLERY = 1;
    String picturePath;
    Intent in_gallery1;
    ImageView ivAttachImage;
    String ImageClick ;
    File bookFile,referenceAttachFile;
    JSONObject obj= null,obj2= null,obj3= null;

    public Fragment_HomeworkCreate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework_create, container, false);

        initViews(view);
        initListeners();

        getDivisionApi();

       /* setSpinnerForClass();
        setSpinnerForSubject();
        getCurrentDate();*/

        //setRecyclerViewBook();
        //setRecyclerViewURL();

        View viewBook = inflater.inflate(R.layout.layout_book_row, null);
        ll_book_row.addView(viewBook);

        View viewURL = inflater.inflate(R.layout.layout_url_row, null);
        ll_url_row.addView(viewURL);

        EditText etBookName = viewBook.findViewById(R.id.et_book_name);
        EditText etPageNo = viewBook.findViewById(R.id.et_page_no);
        EditText etBookAuthor = viewBook.findViewById(R.id.ed_book_author);
        EditText etUrl = viewURL.findViewById(R.id.et_url);
        bookImage = viewBook.findViewById(R.id.bookattached);

        etListBookName.add(etBookName);
        etListPageNo.add(etPageNo);
        etListBookAuthor.add(etBookAuthor);
      //  ivBookImageList.add(bookImage);

        etListURL.add(etUrl);

        bookImage.setOnClickListener(this);

        return view;
    }

    private void getCurrentDate() {

        Calendar cal = Calendar.getInstance();

        year = cal.get(java.util.Calendar.YEAR);
        month = cal.get(java.util.Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        currentDate = dateFormat.format(cal.getTime());

        tv_fromDate.setText(currentDate);
        tv_toDate.setText(currentDate);

        Log.d("CURRENTDATE", currentDate);

        fromDate = currentDate;
        toDate = currentDate;

    }
/*
    private void setSpinnerForSubject() {

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Select Class");
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), arrayList, "Blue");
        spn_class.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0)
                    str_class = "";
                else
                    str_class = spn_class.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerForClass() {

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Select Subject");
        arrayList.add("English");
        arrayList.add("Hindi");
        arrayList.add("Math");
        arrayList.add("Science");

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), arrayList, "Blue");
        spn_Subject.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0)
                    str_subject = "";
                else
                    str_subject = spn_Subject.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }*/

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        tv_book_minus.setOnClickListener(this);
        tv_book_plus.setOnClickListener(this);
        tv_url_minus.setOnClickListener(this);
        tv_url_plus.setOnClickListener(this);
        tv_fromDate.setOnClickListener(this);
        tv_toDate.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        ivAttachImage.setOnClickListener(this);
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();

        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        rv_book = view.findViewById(R.id.rv_book);
        rv_URL = view.findViewById(R.id.rv_URL);
        rv_attachment = view.findViewById(R.id.rv_attachment);

        tv_book_minus = view.findViewById(R.id.tv_book_minus);
        tv_book_count = view.findViewById(R.id.tv_book_count);
        tv_book_plus = view.findViewById(R.id.tv_book_plus);

        tv_url_minus = view.findViewById(R.id.tv_url_minus);
        tv_url_count = view.findViewById(R.id.tv_url_count);
        tv_url_plus = view.findViewById(R.id.tv_url_plus);

        spn_division = view.findViewById(R.id.spn_division);
        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_Subject = view.findViewById(R.id.spn_Subject);

        et_work_title = view.findViewById(R.id.et_work_title);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        tv_toDate = view.findViewById(R.id.tv_toDate);
        et_description = view.findViewById(R.id.et_description);

        btn_submit = view.findViewById(R.id.btn_submit);

        ll_book_row = view.findViewById(R.id.ll_book_row);
        ll_url_row = view.findViewById(R.id.ll_url_row);

        ivAttachImage = view.findViewById(R.id.iv_attachmentImage);

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        listSubject = new ArrayList<>();


        sectionSubjectModels = new ArrayList<>();


        listBook = new ArrayList<>();
        listUrl = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        etListBookName = new ArrayList<>();
        etListPageNo = new ArrayList<>();
        etListBookAuthor = new ArrayList<>();
       // imageList = new ArrayList<>();
        referenceAttachImage = new ArrayList<>();


        etListURL = new ArrayList<>();

        listBook.add("1");
        listUrl.add("1");
        tv_book_count.setText(String.valueOf(bookCount));

    }


    private void getDivisionApi() {


        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "Select Division");
                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            Log.i("Dvision**!@**", "" + response.body());
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
                            spn_division.setAdapter(customSpinnerAdapter);

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

        spn_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        listClass.add(0, "Select Class");
        customSpinnerAdapter.notifyDataSetChanged();
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
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spn_class.setAdapter(customSpinnerAdapter);


        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class = spn_class.getSelectedItem().toString();
                listSectionNew = new ArrayList<>();
                boolean b = true;

                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        listSectionNew.clear();

                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                            listSectionNew.add(modelArrayList.get(j).getListSection().get(k));

                        }
                        // getSubject(strDiv, str_class);
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
                Log.i("SubjectList***", "" + response.body());

                if (response.isSuccessful()) {
                    try {
                        listSubject.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
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
                                    listSubject.add(subjectkey);


                                }
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                spn_Subject.setAdapter(customSpinnerAdapter);
                                // setRecyclerView();
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
    public void onClick(View view) {

        assert getActivity() != null;

        switch (view.getId()) {

            /*case R.id.bookattached:
                ImageClick ="99";
                in_gallery1 = new Intent(Intent.ACTION_PICK);
                in_gallery1.setType("image/*");
                startActivityForResult(in_gallery1, FROM_GALLERY);

                break;*/

            case R.id.iv_attachmentImage:
                 ImageClick ="66";
                 in_gallery1 = new Intent(Intent.ACTION_PICK);
                in_gallery1.setType("image/*");
                startActivityForResult(in_gallery1, FROM_GALLERY);

                break;
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.tv_book_minus:

                if (bookCount > 1) {
                    bookCount = bookCount - 1;
                    listBook.remove(bookCount - 1);
                    tv_book_count.setText(String.valueOf(bookCount));
                    View viewBook = getLayoutInflater().inflate(R.layout.layout_book_row, null);
                    ll_book_row.removeViewAt(bookCount - 1);
                    etListBookName.remove(bookCount - 1);
                    etListPageNo.remove(bookCount - 1);
                    etListBookAuthor.remove(bookCount - 1);
                  //  imageList.remove(bookCount - 1);

                }

                break;

            case R.id.tv_book_plus:

                if (bookCount < 5) {
                    bookCount = bookCount + 1;
                    listBook.add("bookCount-1");
                    tv_book_count.setText(String.valueOf(bookCount));
                    View viewBook = getLayoutInflater().inflate(R.layout.layout_book_row, null);
                    ll_book_row.addView(viewBook);

                    EditText etBookName = viewBook.findViewById(R.id.et_book_name);
                    EditText etPageNo = viewBook.findViewById(R.id.et_page_no);
                    EditText etBookAuthor = viewBook.findViewById(R.id.ed_book_author);
                     bookImage = viewBook.findViewById(R.id.bookattached);

                    etListBookName.add(etBookName);
                    etListPageNo.add(etPageNo);
                    etListBookAuthor.add(etBookAuthor);
                  //  ivBookImageList.add(bookImage);

                   /* bookImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // ImageClick ="99";
                             in_gallery1 = new Intent(Intent.ACTION_PICK);
                            in_gallery1.setType("image/*");
                            startActivityForResult(in_gallery1, FROM_GALLERY);
                        }
                    });*/

                }

                break;

            case R.id.tv_url_minus:

                if (urlCount > 1) {
                    urlCount = urlCount - 1;
                    listUrl.remove(urlCount - 1);
                    tv_url_count.setText(String.valueOf(urlCount));
                    View viewUrl = getLayoutInflater().inflate(R.layout.layout_url_row, null);
                    ll_url_row.removeViewAt(urlCount - 1);
                    etListURL.remove(urlCount - 1);
                }

                break;

            case R.id.tv_url_plus:

                if (urlCount < 5) {
                    urlCount = urlCount + 1;
                    listUrl.add("urlCount-1");
                    tv_url_count.setText(String.valueOf(urlCount));
                    View viewURL = getLayoutInflater().inflate(R.layout.layout_url_row, null);
                    ll_url_row.addView(viewURL);
                    EditText etUrl = viewURL.findViewById(R.id.et_url);
                    etListURL.add(etUrl);
                }

                break;

            case R.id.tv_fromDate:

                DatePickerDialog fromDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar fromCal = Calendar.getInstance();
                        fromCal.set(Calendar.YEAR, i);
                        fromCal.set(Calendar.MONTH, i1);
                        fromCal.set(Calendar.DAY_OF_MONTH, i2);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        fromDate = dateFormat.format(fromCal.getTime());
                        tv_fromDate.setText(fromDate);

                    }
                }, year, month, date);

                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();

                break;

            case R.id.tv_toDate:

                DatePickerDialog toDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar toCal = Calendar.getInstance();
                        toCal.set(Calendar.YEAR, i);
                        toCal.set(Calendar.MONTH, i1);
                        toCal.set(Calendar.DAY_OF_MONTH, i2);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        toDate = dateFormat.format(toCal.getTime());
                        tv_toDate.setText(toDate);

                    }
                }, year, month, date);

                toDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                toDateDialog.show();

                break;

            case R.id.btn_submit:

                String work_title = et_work_title.getText().toString();
                String description = et_description.getText().toString();

              /*  if (str_class.equals(""))
                    Toast.makeText(getActivity(), "Class Is Required", Toast.LENGTH_SHORT).show();

                else if (str_subject.equals(""))
                    Toast.makeText(getActivity(), "Subject Is Required", Toast.LENGTH_SHORT).show();
*/
              //  else
                    if (work_title.equals(""))
                    Toast.makeText(getActivity(), "HomeWork Title Is Required", Toast.LENGTH_SHORT).show();

                else if (fromDate.equals(""))
                    Toast.makeText(getActivity(), "Start Date Is Required", Toast.LENGTH_SHORT).show();

                else if (toDate.equals(""))
                    Toast.makeText(getActivity(), "End Date Is Required", Toast.LENGTH_SHORT).show();

                else if (description.equals(""))
                    Toast.makeText(getActivity(), "Description Is Required", Toast.LENGTH_SHORT).show();

                else {

                    JSONObject bookJson = new JSONObject();

                    for (int i = 0; i < etListBookName.size(); i++) {

                        String bookName = etListBookName.get(i).getText().toString();
                        String pageNo = etListPageNo.get(i).getText().toString();
                        String bookAuthor = etListBookAuthor.get(i).getText().toString();
                      //  String bookImagestr = imageList.get(i).toString();

                        Log.d("MY_DATA_IS",  " " + pageNo + " " + etListBookName.size());


                        JSONObject jsonObject = new JSONObject();

                        try {


                          //  jsonObject.put("book_image", bookBodyPart);
                            jsonObject.put("book_name", bookName);
                            jsonObject.put("page_no", pageNo);
                           // jsonObject.put("author_name", bookAuthor);

                            bookJson.put(String.valueOf(i + 1), jsonObject);


                        } catch (JSONException je) {

                        }

                          obj = new JSONObject();
                        try {
                            obj.put("Book", bookJson);
                        } catch (JSONException je) {

                        }

                        System.out.println("Book***" + obj);
                    }


                    JSONObject urlJson = new JSONObject();
                    for (int i = 0; i < etListURL.size(); i++) {

                        String url = etListURL.get(i).getText().toString();
                        Log.d("MY_DATA_IS", url);

                        JSONObject jsonObject2 = new JSONObject();

                        try {

                            jsonObject2.put("ref_url", url);
                            jsonObject2.put("url_message", "url_message ok");

                            urlJson.put(String.valueOf(i + 1), jsonObject2);


                        } catch (JSONException je) {

                        }

                         obj2 = new JSONObject();
                        try {
                            obj2.put("URL", urlJson);
                        } catch (JSONException je) {

                        }

                        System.out.println("URL***" + obj2);

                    }



                    //{"Attachment" : {"1":{"attachment_image":"refrence_image"}}}
                    System.out.println("referenceAttachImage**"+referenceAttachImage.size());
                    JSONObject attachJson = new JSONObject();

                    for (int k=0;k<referenceAttachImage.size();k++){

                        JSONObject jsonObject3= new JSONObject();

                        try{
                           // jsonObject3.put("attachment_image",referenceAttachImage.get(k).toString());
                            jsonObject3.put("attachment_image","refrence_image");
                            attachJson.put(String.valueOf(k+1),jsonObject3);

                        }catch (JSONException je ){

                        }

                    }
                    obj3= new JSONObject();
                    try {
                        obj3.put("Attachment", attachJson);
                    } catch (JSONException je) {

                    }


                    //api call
                    String homeTitle,homeStartDate,homeEndDate,homeDesc;



                    /*Constant.SCHOOL_ID = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID));
                    str_class =String.valueOf(RequestBody.create(MediaType.parse("text/plain"), str_class));
                    str_subject =String.valueOf(RequestBody.create(MediaType.parse("text/plain"), str_subject));
                    homeTitle=String.valueOf(RequestBody.create(MediaType.parse("text/plain"),et_work_title.getText().toString()));
                    homeStartDate=String.valueOf(RequestBody.create(MediaType.parse("text/plain"),tv_fromDate.getText().toString()));
                    homeEndDate=String.valueOf(RequestBody.create(MediaType.parse("text/plain"),tv_toDate.getText().toString()));
                    homeDesc=String.valueOf(RequestBody.create(MediaType.parse("text/plain"),et_description.getText().toString()));
                    strSection =String.valueOf(RequestBody.create(MediaType.parse("text/plain"), strSection));
                    Constant.EMPLOYEE_BY_ID =String.valueOf(RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID));

                    RequestBody referenceAttach = RequestBody.create(MediaType.parse("image/*"), referenceAttachFile);
                    MultipartBody.Part refereneceBodyPart = MultipartBody.Part.createFormData("refrence_image",
                            referenceAttachFile.getName(), referenceAttach);

                    MultipartBody.Part bookJsonPart = MultipartBody.Part.createFormData("book_name", String.valueOf(obj));
                    MultipartBody.Part urlJsonPart = MultipartBody.Part.createFormData("reference_url", String.valueOf(obj));

                    Log.i("Value***",""+Constant.SCHOOL_ID);
                    Log.i("Value***",""+str_class);
                    Log.i("Value***",""+str_subject);
                    Log.i("Value***",""+homeTitle);
                    Log.i("Value***",""+homeStartDate);
                    Log.i("Value***",""+homeEndDate);
                    Log.i("Value***",""+homeDesc);
                    Log.i("Value***",""+strSection);
                    Log.i("Value***",""+ Constant.EMPLOYEE_BY_ID);
                    Log.i("Value***",""+ bookJsonPart);
                    Log.i("Value***",""+ urlJsonPart);
                    Log.i("Value***",""+ refereneceBodyPart);

               */



                   mApiService.createHomework(Constant.SCHOOL_ID,
                           str_class,str_subject,et_work_title.getText().toString(),
                           tv_fromDate.getText().toString(),tv_toDate.getText().toString(),
                           et_description.getText().toString(), strSection,
                           Constant.EMPLOYEE_BY_ID,obj,obj2)
                            .enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Log.i("CREATE_RES**",""+response.body());
                            Log.i("Value***",""+ response.raw().request().url());
                            Log.i("CREATE_RES**",""+response.code());


                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });



                }

                break;


        }
    }

    private void crateHomeworkAPI() {



      /*


                   mApiService.createHomework(Constant.SCHOOL_ID,str_class,str_subject,et_work_title.getText().toString(),
                           tv_fromDate.getText().toString(),tv_toDate.getText().toString(),
                           et_description.getText().toString(), strSection,Constant.EMPLOYEE_BY_ID,obj,obj2,obj3)
       */
       //  RequestBody bookRequestBody = RequestBody.create(MediaType.parse("image/*"), bookFile);

                         //   MultipartBody.Part bookBodyPart = MultipartBody.Part.createFormData("book_image ", bookFile.getName(), bookRequestBody);


  /*    RequestBody bookRequestBody = RequestBody.create(MediaType.parse("image/*"), bookFile);

        MultipartBody.Part bookBodyPart = MultipartBody.Part.createFormData("book_image ", bookFile.getName(), bookRequestBody);
*/





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             picturePath = cursor.getString(columnIndex);
            cursor.close();
            ivAttachImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            referenceAttachImage.add(picturePath);

            referenceAttachFile = new File(picturePath);
/*

            if (ImageClick.equals("99")) {
                System.out.println(picturePath);
                bookImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                imageList.add(picturePath);
                bookFile = new File(picturePath);
            }else if (ImageClick.equals("66")){
                ivAttachImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                referenceAttachImage.add(picturePath);
                referenceAttachFile = new File(picturePath);
            }
*/


        }

    }


}

