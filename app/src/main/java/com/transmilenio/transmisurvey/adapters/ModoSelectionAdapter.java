package com.transmilenio.transmisurvey.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ModoSelectionAdapter extends BaseAdapter {

    private Context context;
    private List<String> lista;
    private int layout;

    private Realm realm;


    public ModoSelectionAdapter() {
    }

    public ModoSelectionAdapter(Context context, List<String> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public String getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ModoSelectionAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ModoSelectionAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_tituloEncuesta);
            convertView.setTag(vh);
        }else{
            vh = (ModoSelectionAdapter.ViewHolder) convertView.getTag();
        }

        vh.titulo.setText(lista.get(position));

        return convertView;
    }

    public class ViewHolder{
        TextView titulo;
    }

}
