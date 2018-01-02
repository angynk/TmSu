package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.RegistroAdPunto;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;

import java.util.List;



public class RegistroAdPAdapter extends BaseAdapter {


    private Context context;
    private List<RegistroAdPunto> lista;
    private int layout;

    public RegistroAdPAdapter(Context context, List<RegistroAdPunto> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegistroAdPAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new RegistroAdPAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_tituloRegistro);
            convertView.setTag(vh);
        }else{
            vh = (RegistroAdPAdapter.ViewHolder) convertView.getTag();
        }

        RegistroAdPunto registro = lista.get(position);
        vh.titulo.setText(registro.getServicio()+"-"+registro.getNumBus());

        return convertView;
    }

    public class ViewHolder{
        TextView titulo;
    }


}
