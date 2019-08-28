//package com.example.guitarautoscroll;
//
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import static android.content.ContentValues.TAG;
//
//public class imageAdapter extends RecyclerView.Adapter<imageAdapter.MyViewHolder> {
//    private List<TabModel> tabs;
//    private static imageRecyclerClickListener itemClickListener;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        // each data item is just a string in this case
//        TextView textView;
//
//        MyViewHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            itemClickListener.imageRecyclerViewListClicked(view,
//                getLayoutPosition());
//            Log.d(TAG,"Position clicked: "+getLayoutPosition());
//        }
//    }
//
//    // Public interfaces
//    public interface imageRecyclerClickListener {
//        public void imageRecyclerViewListClicked(View v, int position);
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public imageAdapter(List<TabModel> mTabs) {
//        tabs=mTabs;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public imageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
//                                                        int viewType) {
//        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//            .inflate(R.layout.my_text_view, parent, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        holder.textView.setText(tabs[position]);
//
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return tabs.size();
//    }
//}