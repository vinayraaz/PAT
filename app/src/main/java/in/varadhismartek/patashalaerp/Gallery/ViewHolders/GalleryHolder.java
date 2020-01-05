package in.varadhismartek.patashalaerp.Gallery.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class GalleryHolder extends RecyclerView.ViewHolder {

    public TextView tv_date;
    public RecyclerView recycler_view;

    public ImageView iv_attach, ivImages, iv_cancel;
    public TextView tv_gallery_title;

    public ImageView iv_event_image;
    public TextView tv_count, tv_event_title;

    public GalleryHolder(@NonNull View itemView) {
        super(itemView);

        tv_date = itemView.findViewById(R.id.tv_date);
        recycler_view = itemView.findViewById(R.id.recycler_view);
        iv_attach = itemView.findViewById(R.id.iv_attach);
        tv_gallery_title = itemView.findViewById(R.id.tv_gallery_title);
        ivImages = itemView.findViewById(R.id.ivImages);
        iv_cancel = itemView.findViewById(R.id.iv_cancel);


        ivImages = itemView.findViewById(R.id.ivImages);


        iv_event_image = itemView.findViewById(R.id.iv_event_image);
        tv_count = itemView.findViewById(R.id.tv_count);
        tv_event_title = itemView.findViewById(R.id.tv_event_title);




    }
}
