package com.transmilenio.transmisurvey.http;

import com.transmilenio.transmisurvey.models.Resultado;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nataly on 16/10/2017.
 */

public interface SurveyService {

    @POST("test/new/")
    Call<Resultado> sendSurvey(@Body String res);
}
