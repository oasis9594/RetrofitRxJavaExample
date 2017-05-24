package com.example.dell.retrofitrx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.retrofitrx.models.Worldpopulation;
import com.example.dell.retrofitrx.models.RootObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopulationAdapter extends RecyclerView.Adapter<PopulationAdapter.ViewHolder> {

    List<Worldpopulation> worldpopulations;
    Context mContext;
    public PopulationAdapter(Activity activity, List<Worldpopulation> obj) {
        Log.d(PopulationActivity.TAG, "PopulationAdapter");
        this.worldpopulations =obj;
        this.mContext=activity;
    }
    public void setData(RootObject rootObject)
    {
        worldpopulations = rootObject.getWorldpopulation();
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MyClickHandler mlistener;
        ImageView image;
        public ViewHolder(View itemView, MyClickHandler listener) {
            super(itemView);
            mlistener=listener;
            image=(ImageView)itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mlistener.showBigImage(v, getAdapterPosition());
        }

        public interface MyClickHandler {
            void showBigImage(View v, int pos);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(PopulationActivity.TAG, "onCreateViewHolder1");
        View customView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row, parent, false);
        ViewHolder vh= new ViewHolder(customView, new ViewHolder.MyClickHandler() {

            @Override
            public void showBigImage(View v, int pos) {
                mContext.startActivity(new Intent(mContext, PopulationFlag.class).
                        putExtra(mContext.getResources().getString(R.string.image_key), worldpopulations.get(pos).getFlag()));
            }
        });
        Log.v(PopulationActivity.TAG, "onCreateViewHolder2");
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v(PopulationActivity.TAG, "onBindViewHolder1");
        Worldpopulation worldpopulation = worldpopulations.get(position);
        Picasso.with(holder.image.getContext()).load(worldpopulation.getFlag()).error(R.drawable.noposter).into(holder.image);
        Log.v(PopulationActivity.TAG, "onBindViewHolder2");
    }

    @Override
    public int getItemCount() {
        return worldpopulations.size();
    }

}
