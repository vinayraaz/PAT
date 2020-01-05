package in.varadhismartek.patashalaerp.Gallery;


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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class GalleryGridFragment extends Fragment {

    ImageView iv_backBtn;
    RecyclerView recycler_view;
    TextView tv_date, tv_event_title;

    public GalleryGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_grid, container, false);

        initViews(view);
        getBundles();

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void initViews(View view) {

        recycler_view = view.findViewById(R.id.recycler_view);
        tv_date = view.findViewById(R.id.tv_date);
        tv_event_title = view.findViewById(R.id.tv_event_title);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
    }

    private void getBundles() {

        Bundle bundle = getArguments();
        assert bundle != null;
        //noinspection unchecked
        ArrayList<String> list = (ArrayList<String>) bundle.getSerializable("list");
        String date =  bundle.getString("date");
        String title =  bundle.getString("title");

        String[] str = date.split("-");

        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.YEAR, year);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        String strMonth = simpleDateFormat.format(calendar.getTime());

        Log.d("lkejhlkehfnl", strMonth);

        tv_date.setText(strMonth+" "+day+", "+year);
        tv_event_title.setText(title);

        setRecyclerView(list);

    }

    private void setRecyclerView(ArrayList<String> list) {

        GalleryAdapter adapter = new GalleryAdapter(getActivity(), list, Constant.RV_GALLERY_IMAGES);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
