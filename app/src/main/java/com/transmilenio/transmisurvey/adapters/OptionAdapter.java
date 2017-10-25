package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Encuesta;
import com.transmilenio.transmisurvey.models.db.Opcion;

import java.util.List;

/**
 * Created by nataly on 24/10/2017.
 */

public class OptionAdapter extends BaseAdapter{

    private Context context;
    private List<Opcion> opciones;
    private int layout;

    public OptionAdapter() {
    }

    public OptionAdapter(Context context, List<Opcion> opciones, int layout) {
        this.context = context;
        this.opciones = opciones;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return opciones.size();
    }

    @Override
    public Object getItem(int position) {
        return opciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final OptionAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new OptionAdapter.ViewHolder();
            vh.opcion = (TextView) convertView.findViewById(R.id.op_name_textView);
            vh.icon = (ImageView) convertView.findViewById(R.id.op_icon_imageView);
            convertView.setTag(vh);
        }else{
            vh = (OptionAdapter.ViewHolder) convertView.getTag();
        }

        Opcion opcion = opciones.get(position);
        vh.opcion.setText(opcion.getName());
        Glide.with(context).load(opcion.getThumbnail())
                .placeholder(opcion.getThumbnail())
                .into(vh.icon);
        return convertView;
    }

    public class ViewHolder{
        TextView opcion;
        ImageView icon;
    }
}
