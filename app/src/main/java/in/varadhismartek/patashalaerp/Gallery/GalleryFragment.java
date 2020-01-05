package in.varadhismartek.patashalaerp.Gallery;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private ImageView iv_backBtn;
    private RecyclerView recycler_view;
    private GalleryAdapter adapter;
    private ArrayList<GalleryModel> galleryModelArrayList;
    private APIService mApiServicePatashala;
    private DateConvertor convertor;
    private ProgressDialog progressDialog;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        initView(view);
        initListeners();
        setRecyclerView();
        getSchoolGalleryAPI();


        return view;
    }

    private void setRecyclerView() {

        adapter = new GalleryAdapter(galleryModelArrayList, getActivity(), Constant.RV_GALLERY_NEW_LAYOUT,refreshGallery);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
    }

    private void initView(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();
        progressDialog = new ProgressDialog(getActivity());

        recycler_view = view.findViewById(R.id.recycler_view);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        galleryModelArrayList = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {

        assert getActivity() != null;

       switch (view.getId()){

           case R.id.iv_backBtn:
               getActivity().onBackPressed();
       }
    }

    private void getSchoolGalleryAPI() {

        if (galleryModelArrayList.size()>0)
            galleryModelArrayList.clear();

        progressDialog.show();

        mApiServicePatashala.getGallery(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");
                            JSONObject galleryDetails = jsonData.getJSONObject("gallery_details");
                            Iterator<String> key = galleryDetails.keys();

                            while (key.hasNext()){

                                String myKey = key.next();
                                JSONObject keyData = galleryDetails.getJSONObject(myKey);

                                //String description = keyData.getString("description");
                                String gallery_id = keyData.getString("gallery_id");
                                //String added_by = keyData.getString("added_by");
                                String added_datetime = keyData.getString("added_datetime");
                                String title = keyData.getString("title");

                                String addedDate = convertor.getDateByTZ_SSS(added_datetime);

                                ArrayList<String> list = new ArrayList<>();

                                if (keyData.getJSONObject("gallery_image").toString().equalsIgnoreCase("{}")){

                                    Log.d("GALLERY_API_image", "IF kahfdkgfkgasfkgk");

                                }else {

                                    JSONObject galleryObj = keyData.getJSONObject("gallery_image");
                                    Iterator<String> galleryKey = galleryObj.keys();

                                    while (galleryKey.hasNext()){

                                        JSONObject finalObj = galleryObj.getJSONObject(galleryKey.next());
                                        String gallery_image = finalObj.getString("gallery_image");
                                        list.add(gallery_image);
                                    }
                                    galleryModelArrayList.add(new GalleryModel(addedDate,title,gallery_id, list));
                                }
                            }
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();

                        }else {
                            Log.d("GALLERY_API_ELSE", message+" "+response.code());
                            progressDialog.dismiss();

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }



    RefreshGallery refreshGallery = new RefreshGallery() {
        @Override
        public void refresh() {
            getSchoolGalleryAPI();
        }
    };

    public interface RefreshGallery{

        void refresh();
    }
}
