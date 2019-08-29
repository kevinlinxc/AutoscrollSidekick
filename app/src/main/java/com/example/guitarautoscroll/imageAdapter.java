package com.example.guitarautoscroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;
public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ViewHolder> {

    private List<Tab> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    imageAdapter(Context context, List<Tab> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_image_tab, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        Date date = mData.get(position).getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        String formattedDate=sdf.format(date);
        String path=mData.get(position).getPath();
        Bitmap preview = BitmapFactory.decodeFile(path);
        holder.titleView.setText(title);
        holder.dateView.setText(formattedDate);
        holder.mImageView.setImageBitmap(preview);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        TextView dateView;
        ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.imageTitle);
            dateView = itemView.findViewById(R.id.dateView);
            mImageView = itemView.findViewById(R.id.previewImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Tab getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}