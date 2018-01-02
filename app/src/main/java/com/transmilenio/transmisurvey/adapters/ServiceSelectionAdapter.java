package com.transmilenio.transmisurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;



public class ServiceSelectionAdapter  extends BaseAdapter {

    private Context context;
    private List<String> lista;
    private ArrayList<String> selectedItems;
    private int layout;

    private Realm realm;

    public ServiceSelectionAdapter() {
    }

    public ServiceSelectionAdapter(Context context, List<String> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
        this.selectedItems = new ArrayList<>();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ServiceSelectionAdapter.ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ServiceSelectionAdapter.ViewHolder();
            vh.titulo = (TextView) convertView.findViewById(R.id.servicioNombre);
            vh.selection= (CheckBox) convertView.findViewById(R.id.checkBox_select_servicio);
            convertView.setTag(vh);
        }else{
            vh = (ServiceSelectionAdapter.ViewHolder) convertView.getTag();
        }

        vh.titulo.setText(lista.get(position));
        vh.selection.setTag( position);
        vh.selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)  vh.selection.getTag();
                String cuadro = lista.get(pos);
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
        TextView titulo;
        CheckBox selection;
    }

    public ArrayList<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<String> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
