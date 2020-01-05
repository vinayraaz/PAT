package in.varadhismartek.patashalaerp.StudentModule;

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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.TeacherModule.TeacherHomeworkAdapter;
import in.varadhismartek.patashalaerp.TeacherModule.TeacherHomeworkModle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeWork_Fragment extends Fragment {
    ArrayList<TeacherHomeworkModle> teacherHomeworkModles;
    RecyclerView recycler_view;

    APIService apiService;
    String completePercentage = "", note = "", reportDate = "", assignerID = "", assigerFName = "", assignDate = "",
            assignerLName = "", startDate = "", endDate = "",
            studentFName = "", studentLName = "", StudentId = "", homeTitle = "",
            homeDescription = "", homeClass = "", homeSubject = "", homeSection = "", homeworkID = "",DOB="",birthCertificate="";
    TeacherHomeworkAdapter teacherHomeworkAdapter;

    public StudentHomeWork_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_homework, container, false);

        initView(view);
        getHomework();
        return view;


    }

    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        recycler_view = view.findViewById(R.id.recycler_view);
        teacherHomeworkModles = new ArrayList<>();

    }


    private void getHomework() {

        String classes = "ukg", section = "A",
                homework_title = "Django",
                subject = "Chemistry_lab",
                first_name = "Manak",
                last_name = "Rishi",
                dob = "1992-06-08",
                birth_certificate_number = "1234",
                student_uuid = "52160d93-7c27-4d0a-9157-f2c5984e840d";




        apiService.getHomeworkStudent(Constant.SCHOOL_ID, classes, section
                , first_name, last_name, dob, birth_certificate_number, student_uuid)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            Log.i("Re****", "" + response.body());


                            try {
                                teacherHomeworkModles.clear();
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                System.out.println("MEssage**C*" + object.getString("message"));
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONObject jsonObject = object.getJSONObject("data");

                                    Iterator<String> keys = jsonObject.keys();


                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject objectData = jsonObject.getJSONObject(key);

                                        System.out.println("completePercentage***1 " + objectData);
                                        completePercentage = objectData.getString("completion_percent");

                                        note = objectData.getString("student_note");
                                        reportDate = objectData.getString("report_datetime");

                                        assignerID = objectData.getString("assigner_id");
                                        homeworkID = objectData.getString("homework_uuid");
                                        assigerFName = objectData.getString("assigner_first_name");
                                        assignerLName = objectData.getString("assigner_last_name");
                                         assignDate =objectData.getString("assigned_date");

                                        startDate = objectData.getString("start_date");
                                        endDate = objectData.getString("end_date");
                                        studentFName = objectData.getString("student_first_name");
                                        studentLName = objectData.getString("student_last_name");
                                        StudentId = objectData.getString("student_id");
                                        homeTitle = objectData.getString("homework_title");
                                        homeDescription = objectData.getString("description");
                                        homeClass = objectData.getString("class");
                                        homeSubject = objectData.getString("subject");
                                        homeSection = objectData.getString("section");

                                        DOB = objectData.getString("student_dob");
                                        birthCertificate = objectData.getString("student_birth_certificate_number");

                                        teacherHomeworkModles.add(new TeacherHomeworkModle(
                                                completePercentage, note, reportDate, assignerID,
                                                assigerFName, assignerLName, startDate, endDate,
                                                studentFName, studentLName, StudentId, homeTitle,
                                                homeDescription, homeClass, homeSubject, homeSection,
                                                homeworkID,DOB,birthCertificate,assignDate
                                        ));
                                    }

                                    setRecyclerView();
                                }
                            } catch (JSONException je) {

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("Res[ponse ERR**", "" + t.toString());
                    }
                });
    }

    private void setRecyclerView() {

        teacherHomeworkAdapter = new TeacherHomeworkAdapter(teacherHomeworkModles, getActivity(),
                Constant.RV_STUDENT_HOMEWORK_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(teacherHomeworkAdapter);
        teacherHomeworkAdapter.notifyDataSetChanged();

    }

}
