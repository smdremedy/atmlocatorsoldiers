package com.soldiersofmobile.atmlocator;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by madejs on 25.02.16.
 */
public class App extends Application {

    private static AtmComponent atmComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        AtmModule atmModule = new AtmModule(getApplicationContext());
        atmComponent = DaggerAtmComponent.builder()
                .atmModule(atmModule)
                .build();
    }

    public static AtmComponent getAtmComponent() {
        return atmComponent;
    }
}
