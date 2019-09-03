package com.kevinlinxc.guitarautoscroll;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinlinxc.guitarautoscroll.CustomClasses.ASApplication;
import com.kevinlinxc.guitarautoscroll.CustomClasses.Tab;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tab> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    ImageAdapter(Context context, List<Tab> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_image_tab, parent, false);
        TabViewHolder viewHolder = new TabViewHolder(view);
        return viewHolder;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TabViewHolder tvh = (TabViewHolder) holder;
        String title = mData.get(position).getTitle();
        Date date = mData.get(position).getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        String formattedDate=sdf.format(date);
        String path=mData.get(position).getPath();
        Bitmap preview = BitmapFactory.decodeFile(path);
        tvh.titleView.setText(title);
        tvh.dateView.setText(formattedDate);
        tvh.mImageView.setImageBitmap(preview);
        tvh.mImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTitle(position,tvh.getContext());
                notifyDataSetChanged();
            }

            });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class TabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        TextView dateView;
        ImageView mImageView;
        ImageView mImageButton;

        TabViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.imageTitle);
            dateView = itemView.findViewById(R.id.dateView);
            mImageView = itemView.findViewById(R.id.previewImage);
            mImageButton = itemView.findViewById(R.id.editButton);
            itemView.setOnClickListener(this);
        }

        public Context getContext() {return itemView.getContext();}

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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void editTitle(final int position, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter A New Title");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();
                List<Tab> mTabs = ASApplication.getmTabs();
                mTabs.get(position).setTitle(inputText);
                notifyItemChanged(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}