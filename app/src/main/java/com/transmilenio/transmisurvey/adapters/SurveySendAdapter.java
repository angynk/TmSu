package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;

import java.util.ArrayList;

import io.realm.RealmResults;


public class SurveySendAdapter  extends BaseAdapter {

    private Context context;
    private RealmResults<EncuestaTM> lista;
    private ArrayList<EncuestaTM> selectedItems;
    private int layout;

    public SurveySendAdapter() {
    }

    public SurveySendAdapter(Context context, RealmResults<EncuestaTM> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
        this.selectedItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public EncuestaTM getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SurveySendAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new SurveySendAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.textView_send_tituloEncuesta);
            vh.fecha = (TextView) convertView.findViewById(R.id.textView_send_fechaEncuesta);
            vh.selection= (CheckBox) convertView.findViewById(R.id.checkBox_send_encuesta);
            convertView.setTag(vh);
        }else{
            vh = (SurveySendAdapter.ViewHolder) convertView.getTag();
        }

        EncuestaTM encuesta = lista.get(position);
        vh.titulo.setText(encuesta.getNombre_encuesta());
        vh.fecha.setText(encuesta.getIdentificador());
        vh.selection.setTag( position);
        vh.selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)  vh.selection.getTag();
                EncuestaTM cuadro = lista.get(pos);
                if(selectedItems.contains(cuadro)){
                    selectedItems.remove(cuadro);
                }else{
                    selectedItems.add(cuadro);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder{
        TextView titulo,fecha;
        CheckBox selection;
    }

    public ArrayList<EncuestaTM> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<EncuestaTM> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
