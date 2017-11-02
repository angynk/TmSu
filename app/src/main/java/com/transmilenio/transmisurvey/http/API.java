package com.transmilenio.transmisurvey.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nataly on 19/10/2017.
 */

public class API {

//    public static final String BASE_URL = "http://10.0.2.2:8080/TmAPI/";
    public static final String BASE_URL = "http://35.165.230.191:8080/TmAPI/";
    private static Retrofit retrofit = null;

    public static Retrofit getApi(){
        if(retrofit == null ){
            retrofit =  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
