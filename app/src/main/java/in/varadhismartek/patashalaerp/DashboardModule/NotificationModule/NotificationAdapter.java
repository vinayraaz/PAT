package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.R;


class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    private String recyclerTag;
    private ArrayList<NotificationModel> notificationList;
    private ArrayList<EmpNotificationModel> empNotificationList;
    private ArrayList<String> listSelect;
    private ArrayList<String> checkedArrayList = new ArrayList<>();
    private Button bt_add_dialog;
    private Dialog dialog;
    private TextView tv_select;
    private EditText et_search;


    // this is for notification inbox main row
    public NotificationAdapter(ArrayList<EmpNotificationModel> empNotificationList, Context context, String recyclerTag) {
        this.context = context;
        this.empNotificationList = empNotificationList;
        this.recyclerTag = recyclerTag;

    }

    // this is for notification inbox Nested main row
    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationList, String recyclerTag) {

        this.context = context;
        this.notificationList = notificationList;
        this.recyclerTag = recyclerTag;

    }

    // this is for multi selection
    public NotificationAdapter(ArrayList<String> listSelect, Context context, Button bt_add_dialog, Dialog dialog,
                               TextView tv_select, String recyclerTag) {
        this.context = context;
        this.listSelect = listSelect;
        this.recyclerTag = recyclerTag;
        this.bt_add_dialog = bt_add_dialog;
        this.dialog = dialog;
        this.tv_select = tv_select;

    }

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationList,
                               Dialog dialog, EditText et_search, String recyclerTag) {

        this.context = context;
        this.notificationList = notificationList;
        this.recyclerTag = recyclerTag;
        this.dialog = dialog;
        this.et_search = et_search;

    }



    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag){

            case Constant.RV_NOTIFICATION_INBOX_ROW:
                return new NotificationViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.leave_admin_upper_list, viewGroup, false));

            case Constant.RV_NOTIFICATION_INBOX_NESTED_ROW:
                return new NotificationViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_notification_row, viewGroup, false));

            case Constant.RV_NOTIFICATION_CREATE_DIALOG:
                return new NotificationViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.text_row_check, viewGroup, false));

            case Constant.RV_NOTIFICATION_SEARCH_RESULT:
                return new NotificationViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_search_result, viewGroup, false));

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {

        switch (recyclerTag){

            case Constant.RV_NOTIFICATION_INBOX_ROW:


                holder.tv_applied_date.setText(empNotificationList.get(position).getApplyDate());
                NotificationAdapter adapter = new NotificationAdapter(context, empNotificationList.get(position).getNotificationList(),
                        Constant.RV_NOTIFICATION_INBOX_NESTED_ROW);
                holder.recycler_view.setLayoutManager(new LinearLayoutManager(context));
                holder.recycler_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;

            case Constant.RV_NOTIFICATION_INBOX_NESTED_ROW:

                String message = notificationList.get(position).getMessage();
                String title = notificationList.get(position).getTitle();

                //holder.tv_title.setText(notificationList.get(position).getTitle());

                if (title.length()>20)
                    holder.tv_title.setText(title.substring(0,19)+"...");
                else
                    holder.tv_title.setText(title);
                //-------------------------------------

                if (message.length()>50)
                    holder.tv_message.setText(message.substring(0,49)+"...");
                else
                    holder.tv_message.setText(message);

                if (title.length()>0)
                    holder.tv_shortName.setText(title.charAt(0)+"");

                holder.tv_status.setText(notificationList.get(position).getApproverStatus());

                if (notificationList.get(position).getApproverStatus().equalsIgnoreCase("Approved")){
                    holder.iv_status.setImageResource(R.drawable.circle_bg);
                }else if (notificationList.get(position).getApproverStatus().equalsIgnoreCase("Pending"))
                    holder.iv_status.setImageResource(R.drawable.circle_yellow);
                else if (notificationList.get(position).getApproverStatus().equalsIgnoreCase("Rejected"))
                    holder.iv_status.setImageResource(R.drawable.circle_red);

                if(position == 0){
                    holder.ll_row.setBackgroundResource(R.color.trans_green);
                }else if (position%2 != 0){
                    holder.ll_row.setBackgroundResource(R.color.white);
                }else
                    holder.ll_row.setBackgroundResource(R.color.trans_green);


                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DashBoardMenuActivity notificationActivity = (DashBoardMenuActivity) context;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", notificationList.get(position));
                        notificationActivity.loadFragment(Constant.NOTIFICATION_VIEW_FRAGMENT, bundle);
                    }
                });

                break;

            case Constant.RV_NOTIFICATION_CREATE_DIALOG:
                holder.check_box_sub.setText(listSelect.get(position));

                holder.check_box_sub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (compoundButton.isChecked()){
                            checkedArrayList.add(listSelect.get(position));
                        }else
                            checkedArrayList.remove(listSelect.get(position));

                        Log.d("checkedArrayList", String.valueOf(checkedArrayList.size()));
                    }
                });

                bt_add_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                         // for notification create

                       Log.d("SELECTED_LIST", String.valueOf(checkedArrayList.size()));

                        CreateNotificationFragment.SELECTED_LIST.clear();
                        CreateNotificationFragment.SELECTED_LIST.addAll(checkedArrayList);

                        StringBuffer sb = new StringBuffer();

                        for (int j = 0; j < checkedArrayList.size(); j++){
                            sb.append(checkedArrayList.get(j)+", ");
                        }

                        if (checkedArrayList.size()==0){
                            tv_select.setText("-- Select --");

                        }else
                            tv_select.setText(sb.toString());

                        dialog.dismiss();
                    }
                });


                break;

            case Constant.RV_NOTIFICATION_SEARCH_RESULT:

                holder.tv_name.setText(notificationList.get(position).getName());
                holder.tv_id.setText(notificationList.get(position).getId());
                holder.tv_mobile.setText(notificationList.get(position).getMobile());
                //holder.civ_image.setImageResource();
/*
                switch (CreateNotificationFragment.TAG_SEARCH){

                    case "Parent":
                        holder.ll_parent.setVisibility(View.VISIBLE);
                        holder.ll_staff.setVisibility(View.GONE);
                        break;

                    case "Staff":
                        holder.ll_parent.setVisibility(View.GONE);
                        holder.ll_staff.setVisibility(View.VISIBLE);
                        break;
                }*/

                holder.tv_class_name.setText(notificationList.get(position).getParentName());
                holder.tv_section_name.setText(notificationList.get(position).getChildName());
                holder.tv_dept_name.setText(notificationList.get(position).getParentName());
                holder.tv_role_name.setText(notificationList.get(position).getChildName());

                holder.ll_search_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Clicked "+position, Toast.LENGTH_SHORT).show();
                        et_search.setText(notificationList.get(position).getName());
                        dialog.dismiss();
                    }
                });

                if(position == 0){
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }else if (position%2 != 0){
                    holder.ll_search_row.setBackgroundResource(R.color.trans_green);

                }else {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }

                break;

        }
    }

    @Override
    public int getItemCount() {
        switch (recyclerTag){

            case Constant.RV_NOTIFICATION_INBOX_ROW:
                return empNotificationList.size();

            case Constant.RV_NOTIFICATION_INBOX_NESTED_ROW:
                return notificationList.size();

            case Constant.RV_NOTIFICATION_CREATE_DIALOG:
                return listSelect.size();

            case Constant.RV_NOTIFICATION_SEARCH_RESULT:
                return notificationList.size();

        }
        return 0;
    }





}
