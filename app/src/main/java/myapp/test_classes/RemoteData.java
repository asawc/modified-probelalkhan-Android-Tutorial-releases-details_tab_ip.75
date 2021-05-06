package myapp.test_classes;//package myapp.test_classes;
//package zoftino.com.uicontrols;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import net.simplifiedcoding.simplifiedcoding.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RemoteData {
    private Context context;

    public static final String BASE_URL = "http://www.zoftino.com/api/";
    private static Retrofit retrofit = null;

    public RemoteData(Context contextIn){
        context = contextIn;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public interface StoreDataService {
        @GET("coupons/")
        Call<StoreDataWrapper> getStoreData();
    }

    public void getStoreData(){

        retrofit.create(StoreDataService.class).getStoreData()
                .enqueue(new Callback<StoreDataWrapper>() {

                    @Override
                    public void onResponse(Call<StoreDataWrapper> call,
                                           Response<StoreDataWrapper> response) {

                        Log.d("Async Data RemoteData",
                                "Got REMOTE DATA "+response.body().getCoupons().size());

                        List<String> str = new ArrayList<String>();
                        for(StoreData s : response.body().getCoupons()){
                            str.add(s.getStore());
                        }

                        AutoCompleteTextView storeTV =
                                (AutoCompleteTextView)((Activity)context).findViewById(R.id.autoComplete);

                        ArrayAdapter<String> adapteo = new ArrayAdapter<String>(context,
                                android.R.layout.simple_dropdown_item_1line, str.toArray(new String[0]));
                        storeTV.setAdapter(adapteo);
                    }
                    @Override
                    public void onFailure(Call<StoreDataWrapper> call, Throwable t) {
                        Log.e("Async Data RemoteData",
                                "error in getting remote data");
                    }
                });
    }
}