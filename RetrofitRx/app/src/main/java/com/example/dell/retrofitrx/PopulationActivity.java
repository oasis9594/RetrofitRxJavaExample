package com.example.dell.retrofitrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dell.retrofitrx.models.Worldpopulation;
import com.example.dell.retrofitrx.models.RootObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopulationActivity extends AppCompatActivity {

    RecyclerView populationList;
    public static final String TAG = "dell.nanodegree.TAG";
    List<Worldpopulation> worldpopulations = new ArrayList<Worldpopulation>();
    PopulationAdapter populationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_population_list);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("World Flags");
        populationList = (RecyclerView) findViewById(R.id.populationList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        populationList.setLayoutManager(layoutManager);

        populationAdapter = new PopulationAdapter(this, worldpopulations);
        populationList.setAdapter(populationAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.androidbegin.com/")
                .build();

        PopulationAPI populationAPI = retrofit.create(PopulationAPI.class);
        Observable<RootObject> obj = populationAPI.getWorldPopulation();

        Observer<RootObject> o = new Observer<RootObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe");
            }
            @Override
            public void onNext(@NonNull RootObject worldPopulation) {
                Log.d(TAG, "onNext");
                if(worldPopulation!=null)
                {
                    Log.e(TAG, "cool "+ worldpopulations.size());
                    worldpopulations =worldPopulation.getWorldpopulation();
                    if(worldpopulations !=null)
                    {
                        Log.e(TAG, "cool "+ worldpopulations.size());
                        populationAdapter.setData(worldPopulation);
                    }
                    else
                        Log.e(TAG, "un cool");
                }
                else
                    Log.e(TAG, "null data");
                //Log.d(TAG, ""+worldpopulations.size());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "error observer: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                //task complete
            }
        };
        obj.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o);
        Log.d(TAG, "setting adapter");
    }
}
