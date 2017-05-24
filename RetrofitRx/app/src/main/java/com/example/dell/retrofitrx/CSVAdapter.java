package com.example.dell.retrofitrx;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CSVAdapter extends  RecyclerView.Adapter<CSVAdapter.ViewHolder> {
    ArrayList<item> items;
    CSVAdapter(ArrayList<item> items)
    {
        this.items=items;
    }
    public void setItems(ArrayList<item> items)
    {
        this.items=items;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.contact_text);
        }
    }
    @Override
    public CSVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View customView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.csv_row, parent, false);
        return new ViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(CSVAdapter.ViewHolder holder, int position) {
        item person=items.get(position);
        holder.textView.setText(person.getName()+"----"+person.getNumber());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
