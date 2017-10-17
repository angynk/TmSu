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
import com.transmilenio.transmisurvey.models.Cuadro;

import java.util.ArrayList;

import io.realm.RealmResults;


public class SurveySendAdapter  extends BaseAdapter {

    private Context context;
    private RealmResults<Cuadro> lista;
    private ArrayList<Cuadro> selectedItems;
    private int layout;

    public SurveySendAdapter() {
    }

    public SurveySendAdapter(Context context, RealmResults<Cuadro> lista, int layout) {
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
    public Cuadro getItem(int position) {
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

        Cuadro encuesta = lista.get(position);
        vh.titulo.setText(encuesta.getNombreEncuesta()+" - "+encuesta.getServicio());
        vh.fecha.setText("No."+encuesta.getRecorrido()+" - "+encuesta.getFecha());
       // vh.selection.setChecked(encuesta.getSeleccionado());

//        vh.selection.setTag(R.integer.btnplusview, convertView);
        vh.selection.setTag( position);
        vh.selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                View tempview = (View) vh.selection.getTag(R.integer.btnplusview);
//                TextView tv = (TextView) tempview.findViewById(R.id.animal);
                Integer pos = (Integer)  vh.selection.getTag();
                Toast.makeText(context, "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();
                Cuadro cuadro = lista.get(pos);
                if(selectedItems.contains(cuadro)){
                    selectedItems.remove(cuadro);
                }else{
                    selectedItems.add(cuadro);
                }
//                if(lista.get(pos).isSelected()){
//                    lista.get(pos).setSelected(false);
//                }else {
//                    lista.get(pos).setSelected(true);
//                }

            }
        });

        return convertView;
    }

    public class ViewHolder{
        TextView titulo,fecha;
        CheckBox selection;
    }

    public ArrayList<Cuadro> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<Cuadro> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
