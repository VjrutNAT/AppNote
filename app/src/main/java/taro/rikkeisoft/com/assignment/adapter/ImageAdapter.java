package taro.rikkeisoft.com.assignment.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.renderscript.RenderScript;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.model.Note;

/**
 * Created by VjrutNAT on 10/27/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<String> mImages;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(ArrayList<String> mImages, Context mContext) {
        this.mImages = mImages;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_image, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Glide.with(mContext).load(mImages.get(position)).placeholder(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(holder.ivImage);
        holder.id = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        private int id;
        private ImageView ivImage;
        private ImageView btDelete;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.imv_image_item);
            btDelete = itemView.findViewById(R.id.bt_delete_image_item);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog(id);
                }
            });
        }
    }

    private void confirmDialog(final int imagePos){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.warning));
        builder.setMessage(mContext.getString(R.string.delete_photo_question));
        builder.setPositiveButton(mContext.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mImages.remove(imagePos);
                notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void refreshList(ArrayList<String> listImage){
        mImages.clear();
        mImages.addAll(listImage);
        notifyDataSetChanged();
    }
}
