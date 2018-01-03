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



public class ServiceSelectionAdapter  extends BaseAdapter {

    private Context context;
    private List<String> lista;
    private ArrayList<String> selectedItems;
    private int layout;
    ArrayList<Boolean> positionArray;

    private Realm realm;

    public ServiceSelectionAdapter() {
    }

    public ServiceSelectionAdapter(Context context, List<String> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
        this.selectedItems = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        positionArray = new ArrayList<Boolean>(lista.size());
        for(int i =0;i<lista.size();i++){
            positionArray.add(false);
        }
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
        vh.selection.setOnCheckedChangeListener(null);
        vh.selection.setFocusable(false);
        vh.selection.setChecked(positionArray.get(position));
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
        vh.selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked ){
                    System.out.println(position+"--- :)");
                    positionArray.set(position, true);

                }else{
                    positionArray.set(position, false);
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
