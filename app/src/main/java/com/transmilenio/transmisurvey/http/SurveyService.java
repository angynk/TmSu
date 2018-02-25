package com.transmilenio.transmisurvey.http;

import com.transmilenio.transmisurvey.models.db.Aforador;
import com.transmilenio.transmisurvey.models.db.Resultado;
import com.transmilenio.transmisurvey.models.json.Config;
import com.transmilenio.transmisurvey.models.json.EncuestasTerminadas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;



public interface SurveyService {

    @POST("survey/new/")
    Call<List<Resultado>> sendSurvey(@Body EncuestasTerminadas res);

    @GET("config/servicioEstaciones/")
    Call<Config> getServicios(@Query("modo") String modo);

    @POST("user/login/")
    Call<Boolean> login(@Body Aforador aforador);
}
