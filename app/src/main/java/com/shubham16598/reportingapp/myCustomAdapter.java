package com.shubham16598.reportingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shubham16598 on 2/12/17.
 */

public class myCustomAdapter extends RecyclerView.Adapter<myCustomAdapter.MyViewHolder> {
    private ArrayList<info> data;
    private Context context;
    public LayoutInflater inflater;

    public myCustomAdapter(ArrayList<info> data, Context context, LayoutInflater inflater) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.reportgrid, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final info getdata = data.get(position);
        holder.t1.setText(getdata.getProblem());
        holder.t2.setText(getdata.getDate());
        holder.t3.setText(getdata.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t1;
        TextView t2;
        TextView t3;
        public MyViewHolder(View itemView) {
            super(itemView);
            t1 = (TextView)itemView.findViewById(R.id.problem);
            t1 = (TextView)itemView.findViewById(R.id.date);
            t1 = (TextView)itemView.findViewById(R.id.description);


        }
    }
}
