package com.transmilenio.transmisurvey.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.OpcionAdapter;
import com.transmilenio.transmisurvey.models.Opcion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OpcionAdapter adapter;
    private List<Opcion> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        albumList = new ArrayList<>();




        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new OpcionAdapter(this, albumList);
        recyclerView.setAdapter(adapter);

        prepareAlbums();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((OpcionAdapter) adapter).setOnItemClickListener(new OpcionAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(position==0){
                    Intent intent = new Intent(MainActivity.this,ListaSurveyActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.mipmap.ic_new,
                R.mipmap.ic_edit,
                R.mipmap.ic_send,
                R.mipmap.ic_settings};

        Opcion a = new Opcion("Nueva Encuesta", covers[0]);
        albumList.add(a);

        a = new Opcion("Editar Encuesta", covers[1]);
        albumList.add(a);

        a = new Opcion("Enviar Encuesta", covers[2]);
        albumList.add(a);

        a = new Opcion("Configuración", covers[3]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

}
