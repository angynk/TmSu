package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupaBus;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;

import java.util.List;

/**
 * Created by nataly on 11/01/2018.
 */

public class RegistroFoBusAdapter extends BaseAdapter {


    private Context context;
    private List<RegistroFrecOcupaBus> lista;
    private int layout;

    public RegistroFoBusAdapter(Context context, List<RegistroFrecOcupaBus> lista, int layout) {
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
        RegistroFoBusAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new RegistroFoBusAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_tituloRegistro);
            convertView.setTag(vh);
        }else{
            vh = (RegistroFoBusAdapter.ViewHolder) convertView.getTag();
        }

        RegistroFrecOcupaBus registro = lista.get(position);
        vh.titulo.setText(registro.getServicio());

        return convertView;
    }

    public class ViewHolder{
        TextView titulo;
    }


}
