package com.example.muhamed.kurani;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecycleViewSurahListingAdapter extends RecyclerView.Adapter<RecycleViewSurahListingAdapter.SurahViewHolder>{

    private ArrayList<SurasItem> mSurasItems;

    public RecycleViewSurahListingAdapter(ArrayList<SurasItem> surasItems)
    {
        mSurasItems = surasItems;
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_list_item,parent,false);
        SurahViewHolder svh = new SurahViewHolder(view,mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
           SurasItem currentItem = mSurasItems.get(position);
           holder.mNumberOfSurah.setText(Integer.toString(currentItem.getNumberOfSurah()));
           holder.mSurahName.setText(currentItem.getSurahName());
           holder.mNrOfAyasLabel.setText(currentItem.getNrOfAyasLabel());
           holder.mNrOfAyas.setText(Integer.toString(currentItem.getNrOfAyas()));
    }

    @Override
    public int getItemCount() {
        return mSurasItems.size();
    }

    public static class SurahViewHolder extends RecyclerView.ViewHolder{

        public TextView mNumberOfSurah;
        public TextView mSurahName;
        public TextView mNrOfAyasLabel;
        public TextView mNrOfAyas;

        public SurahViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNumberOfSurah = itemView.findViewById(R.id.tv_surah_number);
            mSurahName = itemView.findViewById(R.id.tv_surah_Name);
            mNrOfAyasLabel = itemView.findViewById(R.id.tv_nrOfAyasLabel);
            mNrOfAyas = itemView.findViewById(R.id.tv_nr_Of_Ayas);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if (listener != null)
                     {
                         int position = Integer.parseInt(mNumberOfSurah.getText().toString());
                         if (position != RecyclerView.NO_POSITION)
                         {
                             listener.onItemClick(position);
                         }
                     }
                }
            });
        }
    }

    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }
}
