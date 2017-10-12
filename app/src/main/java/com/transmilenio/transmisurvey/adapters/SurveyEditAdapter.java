package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.Cuadro;
import com.transmilenio.transmisurvey.models.Encuesta;

import java.util.List;

import io.realm.RealmResults;


public class SurveyEditAdapter extends BaseAdapter {

    private Context context;
    private RealmResults<Cuadro> lista;
    private int layout;

    public SurveyEditAdapter() {
    }

    public SurveyEditAdapter(Context context, RealmResults<Cuadro> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Cuadro getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SurveyEditAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_edit_tituloEncuesta);
            vh.fecha = (TextView) convertView.findViewById(R.id.textView_edit_fechaEncuesta);
            convertView.setTag(vh);
        }else{
            vh = (SurveyEditAdapter.ViewHolder) convertView.getTag();
        }

        Cuadro encuesta = lista.get(position);
        vh.titulo.setText(encuesta.getNombreEncuesta());
        vh.fecha.setText(encuesta.getFecha());

        return convertView;
    }

    public class ViewHolder{
        TextView titulo,fecha;
    }
}
