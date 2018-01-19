
package com.transmilenio.transmisurvey.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.transmilenio.transmisurvey.R;
import com.transmilenio.transmisurvey.adapters.SurveySendAdapter;
import com.transmilenio.transmisurvey.fragments.AlertObservacion;
import com.transmilenio.transmisurvey.http.API;
import com.transmilenio.transmisurvey.http.SurveyService;
import com.transmilenio.transmisurvey.models.db.AdPuntoEncuesta;
import com.transmilenio.transmisurvey.models.db.ConteoDesEncuesta;
import com.transmilenio.transmisurvey.models.db.Cuadro;
import com.transmilenio.transmisurvey.models.db.FOcupacionBusBase;
import com.transmilenio.transmisurvey.models.db.FOcupacionEncuesta;
import com.transmilenio.transmisurvey.models.db.OrigenDestinoBase;
import com.transmilenio.transmisurvey.models.db.RegistroAdPunto;
import com.transmilenio.transmisurvey.models.db.RegistroConteo;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupaBus;
import com.transmilenio.transmisurvey.models.db.RegistroFrecOcupacion;
import com.transmilenio.transmisurvey.models.db.RegistroOD;
import com.transmilenio.transmisurvey.models.db.Resultado;
import com.transmilenio.transmisurvey.models.db.TransbordoOD;
import com.transmilenio.transmisurvey.models.json.AD_Abordo;
import com.transmilenio.transmisurvey.models.json.AD_Fijo;
import com.transmilenio.transmisurvey.models.json.CO_Despacho;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestaJSON;
import com.transmilenio.transmisurvey.models.json.EncuestaTM;
import com.transmilenio.transmisurvey.models.json.EncuestasTerminadas;
import com.transmilenio.transmisurvey.models.json.FR_Bus;
import com.transmilenio.transmisurvey.models.json.FR_Ocupacion;
import com.transmilenio.transmisurvey.models.json.ODEncuesta;
import com.transmilenio.transmisurvey.models.json.ODRegistro;
import com.transmilenio.transmisurvey.models.json.ODTransbordo;
import com.transmilenio.transmisurvey.models.json.RegADAbordo;
import com.transmilenio.transmisurvey.models.json.RegADFijo;
import com.transmilenio.transmisurvey.models.json.RegCoDespachos;
import com.transmilenio.transmisurvey.models.json.RegFROcupacion;
import com.transmilenio.transmisurvey.models.json.RegFROcupacionBus;
import com.transmilenio.transmisurvey.models.json.RegistroEncuesta;
import com.transmilenio.transmisurvey.models.json.TipoEncuesta;
import com.transmilenio.transmisurvey.models.util.ExtrasID;
import com.transmilenio.transmisurvey.models.util.Mensajes;

import java.util.ArrayList;;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaSurveyEnvioActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<EncuestaTM>> {

    private ListView listView;
    private Button buttonEnviar;
    private SurveySendAdapter surveyAdapter;
    private RealmResults<EncuestaTM> encuestas;
    private SharedPreferences prefs;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_survey_envio);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        encuestas = realm.where(EncuestaTM.class).findAll();
        encuestas.addChangeListener(this);
        bindUI();
    }

    private void bindUI() {

        listView = (ListView) findViewById(R.id.listView_send_surveys);
        buttonEnviar = (Button) findViewById(R.id.button_enviar_encuestas);
        surveyAdapter = new SurveySendAdapter(this,encuestas,R.layout.list_view_send_survey);
        listView.setAdapter(surveyAdapter);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(surveyAdapter.getSelectedItems().size()>0){
                    enviarDatosEncuesta();
                }else{
                    Toast.makeText(ListaSurveyEnvioActivity.this,Mensajes.MSG_NO_HAY_ENCUESTAS,Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void enviarDatosEncuesta() {
        ArrayList<EncuestaTM> selectedItems = surveyAdapter.getSelectedItems();
        progressDoalog = new ProgressDialog(ListaSurveyEnvioActivity.this);
        progressDoalog.setMessage(Mensajes.MSG_ENVIANDO);
        progressDoalog.setTitle(Mensajes.MSG_ENCUESTA);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();

        EncuestasTerminadas encuestas = new EncuestasTerminadas();
        ArrayList<EncuestaJSON> valores = new ArrayList<>();
        for(EncuestaTM encuestaTM:selectedItems) {
            valores.add(convertToJSON(encuestaTM));
        }
        encuestas.setEncuestas(valores);
        enviarEncuesta(encuestas);

    }

    private EncuestaJSON convertToJSON(EncuestaTM encuestaTM) {
        EncuestaJSON encuestaJSON = new EncuestaJSON();
        encuestaJSON.setTipo(encuestaTM.getTipo());
        encuestaJSON.setNombre_encuesta(encuestaTM.getNombre_encuesta());
        encuestaJSON.setAforador(encuestaTM.getAforador());
        encuestaJSON.setId_realm(encuestaTM.getId());
        encuestaJSON.setFecha_encuesta(encuestaTM.getFecha_encuesta());
        encuestaJSON.setDia_semana(encuestaTM.getDia_semana());
        if(encuestaTM.getTipo() == TipoEncuesta.ENC_AD_ABORDO){
            generarEncuestaAscDescAbordo(encuestaTM, encuestaJSON);
        }else if(encuestaTM.getTipo() == TipoEncuesta.ENC_FR_OCUPACION){
            generarFrecOcupacion(encuestaTM,encuestaJSON);
        }else if(encuestaTM.getTipo() == TipoEncuesta.ENC_AD_PUNTO){
            generarEncuestaADPunto(encuestaTM,encuestaJSON);
        }else if (encuestaTM.getTipo() == TipoEncuesta.ENC_CONT_DESPACHOS){
            generarEncuestaConteoDespachos(encuestaTM,encuestaJSON);
        }else if(encuestaTM.getTipo() == TipoEncuesta.ENC_ORI_DEST){
            generarEncuestaOrigenDestino(encuestaTM,encuestaJSON);
        }else if (encuestaTM.getTipo() == TipoEncuesta.ENC_FR_BUS){
            generarEncuestaFrecuenciaOcupacionBus(encuestaTM,encuestaJSON);
        }

        return encuestaJSON;
    }

    private void generarEncuestaFrecuenciaOcupacionBus(EncuestaTM encuestaTM, EncuestaJSON encuestaJSON) {
        FOcupacionBusBase focupacionBase = encuestaTM.getFo_bus();
        FR_Bus fr_bus = new FR_Bus();
        fr_bus.setEstacion(focupacionBase.getEstacion());
        fr_bus.setSentido(focupacionBase.getSentido());
        RealmList<RegistroFrecOcupaBus> registroBD = focupacionBase.getRegistros();
        List<RegFROcupacionBus> registros = new ArrayList<>();
        for(RegistroFrecOcupaBus reg:registroBD){
            RegFROcupacionBus registro = new RegFROcupacionBus();
            registro.setNum_bus(reg.getMovBus());
            registro.setHora_paso(reg.getHoraPaso());
            registro.setCodigo(reg.getServicio());
            registros.add(registro);
        }

        fr_bus.setRegistros(registros);
        encuestaJSON.setFr_bus(fr_bus);
    }

    private void generarEncuestaOrigenDestino(EncuestaTM encuestaTM, EncuestaJSON encuestaJSON) {
        OrigenDestinoBase origenDestinoBase = encuestaTM.getOd_destino();
        ODEncuesta odEncuesta = new ODEncuesta();
        odEncuesta.setEstacion(origenDestinoBase.getEstacion());
        odEncuesta.setTipo(origenDestinoBase.getTipo());

        RealmList<RegistroOD> registroBD = origenDestinoBase.getRegistros();
        List<ODRegistro> registros = new ArrayList<>();
        for(RegistroOD reg:registroBD){
            ODRegistro registro = new ODRegistro();
            registro.setEstacion_origen(reg.getEstacionOrigen());
            registro.setEstacion_destino(reg.getEstacionDestino());
            registro.setServicio_origen(reg.getServicioOrigen());
            registro.setHora_encuesta(reg.getHora());
            registro.setCant_viaje(reg.getNumVeces());
            registro.setModo_llegada(reg.getModoLlegada());
            registro.setComentario(reg.getComentario());
            RealmList<TransbordoOD> transbordoODs = reg.getTransbordos();
            List<ODTransbordo> transbordos = new ArrayList<>();
            for(TransbordoOD trans:transbordoODs){
                ODTransbordo odTransbordo = new ODTransbordo();
                odTransbordo.setServicio(trans.getServicio());
                odTransbordo.setEstacion(trans.getEstacion());
                transbordos.add(odTransbordo);
            }
            registro.setTransbordos(transbordos);
            registros.add(registro);
        }

        odEncuesta.setRegistros(registros);
        encuestaJSON.setOd_encuesta(odEncuesta);
    }

    private void generarEncuestaConteoDespachos(EncuestaTM encuestaTM, EncuestaJSON encuestaJSON) {
        ConteoDesEncuesta conteoDesEncuesta = encuestaTM.getCo_despachos();
        CO_Despacho co_despacho = new CO_Despacho();
        co_despacho.setEstacion(conteoDesEncuesta.getEstacion());

        RealmList<RegistroConteo> registroBD = conteoDesEncuesta.getRegistros();
        List<RegCoDespachos> registros = new ArrayList<>();
        for(RegistroConteo reg:registroBD){
            RegCoDespachos registro = new RegCoDespachos();
            registro.setNum_bus(reg.getNumBus());
            registro.setHora_despacho(reg.getHoradespacho());
            registro.setServicio(reg.getServicio());
            registros.add(registro);
        }

        co_despacho.setRegistros(registros);
        encuestaJSON.setCo_despacho(co_despacho);
    }

    private void generarEncuestaADPunto(EncuestaTM encuestaTM, EncuestaJSON encuestaJSON) {
        AdPuntoEncuesta adPuntoEncuesta = encuestaTM.getAd_punto();
        AD_Fijo ad_fijo = new AD_Fijo();
        ad_fijo.setEstacion(adPuntoEncuesta.getEstacion());
        ad_fijo.setDia_semana(adPuntoEncuesta.getDiaSemana());

        RealmList<RegistroAdPunto> registrosBD = adPuntoEncuesta.getRegistros();
        List<RegADFijo> registros = new ArrayList<>();
        for(RegistroAdPunto reg: registrosBD){
            RegADFijo regADFijo = new RegADFijo();
            regADFijo.setHora_llegada(reg.getHoraLlegada());
            regADFijo.setHora_salida(reg.getHoraSalida());
            regADFijo.setNum_bus(reg.getNumBus());
            regADFijo.setPas_bajan(reg.getPasBajan());
            regADFijo.setPas_suben(reg.getPasSuben());
            regADFijo.setPas_quedan(reg.getPasQuedan());
            regADFijo.setObservacion(reg.getObservacion());
            regADFijo.setServicio(reg.getServicio());
            registros.add(regADFijo);
        }

        ad_fijo.setRegistros(registros);
        encuestaJSON.setAd_fijo(ad_fijo);
    }

    private void generarEncuestaAscDescAbordo(EncuestaTM encuestaTM, EncuestaJSON encuestaJSON) {
        CuadroEncuesta cuadro = encuestaTM.getAd_abordo();
        AD_Abordo ad_abordo = new AD_Abordo();
        ad_abordo.setDia_semana(cuadro.getDia_semana());
        ad_abordo.setNum_bus(cuadro.getNum_bus());
        ad_abordo.setNum_puerta(cuadro.getNum_puerta());
        ad_abordo.setRecorrido(cuadro.getRecorrido());
        ad_abordo.setServicio(cuadro.getServicio());

        RealmList<RegistroEncuesta> cuadroRegistros = cuadro.getRegistros();
        List<RegADAbordo> registros = new ArrayList<>();
        for(RegistroEncuesta reg:cuadroRegistros){
            RegADAbordo regADAbordo = new RegADAbordo();
            regADAbordo.setBajan(reg.getBajan());
            regADAbordo.setQuedan(reg.getQuedan());
            regADAbordo.setSuben(reg.getSuban());
            regADAbordo.setHora_llegada(reg.getHora_llegada());
            regADAbordo.setHora_salida(reg.getHora_salida());
            regADAbordo.setObservacion(reg.getObservacion());
            regADAbordo.setEstacion(reg.getEstacion());
            registros.add(regADAbordo);
        }
        ad_abordo.setRegistros(registros);
        encuestaJSON.setAd_abordo(ad_abordo);
    }

    private void generarFrecOcupacion(EncuestaTM encuestaTM, EncuestaJSON encuestaJSON) {
        FOcupacionEncuesta fr_ocupacion = encuestaTM.getFr_ocupacion();
        FR_Ocupacion fr_ocu = new FR_Ocupacion();
        fr_ocu.setSentido(fr_ocupacion.getSentido());
        fr_ocu.setEstacion(fr_ocupacion.getEstacion());

        RealmList<RegistroFrecOcupacion> registrosBD = fr_ocupacion.getRegistros();
        List<RegFROcupacion> registros = new ArrayList<>();
        for(RegistroFrecOcupacion reg: registrosBD){
            RegFROcupacion frOcupacion = new RegFROcupacion();
            frOcupacion.setHora_paso(reg.getHora_paso());
            frOcupacion.setOcupacion(reg.getOcupacion());
            frOcupacion.setCodigo(reg.getServicio());
            registros.add(frOcupacion);
        }

        fr_ocu.setRegistros(registros);
        encuestaJSON.setFr_ocupacion(fr_ocu);


    }

    ProgressDialog progressDoalog;

    private void enviarEncuesta(final EncuestasTerminadas encuestas) {
        SurveyService surveyService = API.getApi().create(SurveyService.class);
        Call<List<Resultado>> call = surveyService.sendSurvey(encuestas);
               call.enqueue(new Callback<List<Resultado>>() {
            @Override
            public void onResponse(Call<List<Resultado>> call, Response<List<Resultado>> response) {
                List<Resultado>  resulta = response.body();
                progressDoalog.dismiss();
                showAlertDialog(Mensajes.MSG_ENCUESTAS_ENVIADAS,resulta);
            }



                   @Override
            public void onFailure(Call<List<Resultado>> call, Throwable t) {
                progressDoalog.dismiss();
                showAlertDialog(Mensajes.MSG_ENCUESTAS_NO_ENVIADAS, new ArrayList<Resultado>());
            }
        });
    }



    FragmentManager fm = getSupportFragmentManager();

    private void showAlertDialog(String mensaje, final List<Resultado> resultado){

                 AlertObservacion dFragment = newInstance(resultado,mensaje);
                    dFragment.show(fm, Mensajes.MSG_SALIR_ENVIO);
    }



    public static AlertObservacion newInstance(final List<Resultado> resultado,String mensaje) {
        AlertObservacion f = new AlertObservacion();
        ArrayList<Integer> lista = new ArrayList<>();
        for(Resultado res : resultado){
            lista.add(res.getId());
        }

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putIntegerArrayList("lista", lista);
        args.putString(ExtrasID.EXTRA_MENSAJE,mensaje);
        f.setArguments(args);

        return f;
    }



    @Override
    public void onChange(RealmResults<EncuestaTM> cuadros) {
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
