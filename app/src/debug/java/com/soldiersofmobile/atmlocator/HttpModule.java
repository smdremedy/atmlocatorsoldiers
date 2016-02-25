package com.soldiersofmobile.atmlocator;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit.Ok3Client;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit.client.Client;

@Module
public class HttpModule {


    @Provides
    public Client provideClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        return new Ok3Client(okHttpClient);
    }

}
