package com.soldiersofmobile.atmlocator;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit.Ok3Client;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

@Module(includes = HttpModule.class)
public class AtmModule {

    private Context context;

    public AtmModule(Context context) {

        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Named("url")
    @Provides
    public String provideUrl() {
        return "http://api.tumblr.com";
    }

    @Named("date")
    @Provides
    public String provideDate() {
        return "yyyy-MM-dd";
    }

    @Provides
    public RestAdapter provideRestAdapter(Client client, @Named("url")String url, @Named("date") String dateFormat) {
        RestAdapter.Builder builder = new RestAdapter.Builder();

        builder.setEndpoint(url);
        GsonBuilder gson = new GsonBuilder().setDateFormat(dateFormat);

        builder.setConverter(new GsonConverter(gson.create()));


        builder.setClient(client);

        return builder.build();
    }

    @Provides
    public TumblrApi provideTumblrApi(RestAdapter restAdapter) {

        return restAdapter.create(TumblrApi.class);
    }


}
