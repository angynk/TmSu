package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.RegistroTiempoRecorrido;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;

import java.util.List;

/**
 * Created by nataly on 9/04/2018.
 */

public class RegistroTRecorridoAdapter extends BaseAdapter {

    private Context context;
    private List<RegistroTiempoRecorrido> lista;
    private int layout;
    public RegistroTRecorridoAdapter(Context context, List<RegistroTiempoRecorrido> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public RegistroTiempoRecorrido getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RegistroTRecorridoAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new RegistroTRecorridoAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_tituloRegistro);
            convertView.setTag(vh);
        }else{
            vh = (RegistroTRecorridoAdapter.ViewHolder) convertView.getTag();
        }

        RegistroTiempoRecorrido registro = lista.get(position);
        vh.titulo.setText(registro.getEstacion());

        return convertView;
    }

    public class ViewHolder{
        TextView titulo;
    }
}
