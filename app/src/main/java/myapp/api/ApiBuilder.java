package myapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {

    private final String BASE_URL = URLs.BASE_URL;
    private Retrofit mRetrofit;
    private Gson mGson;

    public ApiBuilder() {
        mGson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy HH:mm:ss")
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
/*
        String json = "{'object':{'id':5,'employee':{'id':2,'symbol':'RXH0002','name':'John','surname':'Watson'},'status':1,'creationDate':'0000-00-00 00:00:00','realizationDate':null,'productsRelease':null},'error':true,'message':'Rel'}";
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json.trim()));
        reader.setLenient(true);
        ResponseContainer<Release> restaurantObject = gson.fromJson(json.trim(), ResponseContainer.class); */
    }

    public RetrofitApi getApiService() {
        return mRetrofit.create(RetrofitApi.class);
    }
}
