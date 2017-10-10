package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import com.bumptech.glide.Glide;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.Opcion;


public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.MyViewHolder> {

    private Context mContext;
    private List<Opcion> albumList;
    private static MyClickListener myClickListener;


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public ImageView thumbnail;
        public RelativeLayout layout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            layout = (RelativeLayout) view.findViewById(R.id.option_card);
            thumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public OpcionAdapter(Context mContext, List<Opcion> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.options_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Opcion album = albumList.get(position);
        holder.title.setText(album.getName());
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}