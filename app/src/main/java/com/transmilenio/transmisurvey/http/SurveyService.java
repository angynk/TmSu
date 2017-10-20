package com.transmilenio.transmisurvey.http;

import com.transmilenio.transmisurvey.models.db.Resultado;
import com.transmilenio.transmisurvey.models.json.CuadroEncuesta;
import com.transmilenio.transmisurvey.models.json.EncuestasTerminadas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nataly on 16/10/2017.
 */

public interface SurveyService {

    @POST("survey/new/")
    Call<List<Resultado>> sendSurvey(@Body EncuestasTerminadas res);
}
