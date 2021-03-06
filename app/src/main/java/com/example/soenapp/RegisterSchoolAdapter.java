package com.example.soenapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RegisterSchoolAdapter extends RecyclerView.Adapter<RegisterSchoolAdapter.MyViewHolder> {
    private SchoolData mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, address;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.school_name);
            address = v.findViewById(R.id.school_address);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RegisterSchoolAdapter(SchoolData myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RegisterSchoolAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_list_object, parent,false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.results[position].SCHUL_NM);
        holder.address.setText(mDataset.results[position].SCHUL_RDNMA);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.results.length;
    }
}