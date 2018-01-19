package com.transmilenio.transmisurvey.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.models.db.RegistroOD;

import java.util.List;

import io.realm.Realm;

/**
 * Created by nataly on 26/12/2017.
 */

public class RegistroOdAdapter extends BaseAdapter {



    private Context context;
    private List<RegistroOD> lista;
    private int layout;
    Dialog dialog=null;
    private Realm realm;



    public RegistroOdAdapter(Context context, List<RegistroOD> lista, int layout) {
        this.context = context;
        this.lista = lista;
        this.layout = layout;
        realm = Realm.getDefaultInstance();
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
            vh.titulo = (TextView) convertView.findViewById(R.id.tituloEncuesta);
            vh.img = (ImageView) convertView.findViewById(R.id.edicionBoton);
            convertView.setTag(vh);
        }else{
            vh = (RegistroOdAdapter.ViewHolder) convertView.getTag();
        }

        final RegistroOD registro = lista.get(position);
        vh.titulo.setText("Encuesta No."+position);
        vh.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incluirComentarios(registro);
            }


        });

        return convertView;
    }

    private void incluirComentarios(final RegistroOD registro) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcoments);

        Button btnSubmit=(Button)dialog.findViewById(R.id.button_guardar);
        final EditText editText = (EditText) dialog.findViewById(R.id.editText_dialog);
        editText.setText(registro.getComentario());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                realm.beginTransaction();
                registro.setComentario(editText.getText().toString());
                realm.commitTransaction();
                dialog.dismiss();

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public class ViewHolder{
        TextView titulo;
        ImageView img;
    }
}
