package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.RegistroOD;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;

import java.util.List;

/**
 * Created by nataly on 26/12/2017.
 */

public class RegistroOdAdapter extends BaseAdapter {



    private Context context;
    private List<RegistroOD> lista;
    private int layout;


    public RegistroOdAdapter(Context context, List<RegistroOD> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public RegistroOD getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RegistroOdAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new RegistroOdAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_tituloRegistro);
            convertView.setTag(vh);
        }else{
            vh = (RegistroOdAdapter.ViewHolder) convertView.getTag();
        }

        RegistroOD registro = lista.get(position);
        vh.titulo.setText("Encuesta No."+position);

        return convertView;
    }

    public class ViewHolder{
        TextView titulo;
    }
}
