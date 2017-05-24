package com.example.dell.retrofitrx;

import com.example.dell.retrofitrx.models.RootObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by dell on 5/23/2017.
 */

public interface PopulationAPI {
    String SERVICE_ENDPOINT = "http://www.androidbegin.com/tutorial/jsonparsetutorial.txt";
    @GET("tutorial/jsonparsetutorial.txt")
    Observable<RootObject> getWorldPopulation();
}
