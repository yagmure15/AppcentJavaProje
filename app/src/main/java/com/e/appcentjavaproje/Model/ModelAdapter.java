package com.e.appcentjavaproje.Model;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.appcentjavaproje.R;


import java.util.ArrayList;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.TimelineViewHolder> {
    ArrayList<ModelItem> timelineModelList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onIsChecked (int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        mListener= listener;
    }



    public ModelAdapter(ArrayList<ModelItem> timelineModelList) {
        this.timelineModelList = timelineModelList;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline,parent,false);


        return  new TimelineViewHolder(view, mListener);


    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {

        ModelItem model = timelineModelList.get(position);
        holder.hedefTV.setText(model.getHedefadi());
        holder.oncelikTV.setText(model.getOncelikDurumu());
        holder.sontarihTV.setText(model.getBitisTarihi());
        int seciliMi = model.getYapildiMi();
        if (seciliMi ==0 ){
            holder.boxYapildiMi.setChecked(false);
            holder.hedefTV.setPaintFlags( holder.hedefTV.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }else
        {

            holder.hedefTV.setPaintFlags(holder.hedefTV.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            holder.boxYapildiMi.setChecked(true);

        }




    }


    @Override
    public int getItemCount() {
        return timelineModelList.size();
    }


    public static class TimelineViewHolder extends RecyclerView.ViewHolder {

        TextView hedefTV, oncelikTV, sontarihTV;
        CheckBox boxYapildiMi;

        ImageView mDeleteImageView;

        public TimelineViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            hedefTV = itemView.findViewById(R.id.hedef_adi);
            sontarihTV = itemView.findViewById(R.id.sontarih);
            oncelikTV = itemView.findViewById(R.id.oncelik);
            boxYapildiMi = itemView.findViewById(R.id.gorevBittiMi);
            mDeleteImageView = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


            boxYapildiMi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {

                            listener.onIsChecked(position);
                        }
                    }


                }
            });






            mDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {

                            listener.onDeleteClick(position);
                        }
                    }

                }
            });





        }
    }







}