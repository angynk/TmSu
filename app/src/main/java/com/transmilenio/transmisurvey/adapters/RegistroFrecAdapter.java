package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Registro;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;

import java.util.List;

/**
 * Created by nataly on 06/11/2017.
 */

public class RegistroFrecAdapter extends BaseAdapter {


    private Context context;
    private List<RegistroFrecOcupacion> lista;
    private int layout;

    public RegistroFrecAdapter(Context context, List<RegistroFrecOcupacion> lista, int layout) {
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
        RegistroFrecAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new RegistroFrecAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_tituloRegistro);
            convertView.setTag(vh);
        }else{
            vh = (RegistroFrecAdapter.ViewHolder) convertView.getTag();
        }

        RegistroFrecOcupacion registro = lista.get(position);
        vh.titulo.setText(registro.getServicio());

        return convertView;
    }

    public class ViewHolder{
        TextView titulo;
    }


}
