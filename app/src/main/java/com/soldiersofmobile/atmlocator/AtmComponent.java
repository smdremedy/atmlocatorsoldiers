package com.soldiersofmobile.atmlocator;

import com.soldiersofmobile.atmlocator.activities.AddAtmActivity;
import com.soldiersofmobile.atmlocator.activities.MapsActivity;
import com.soldiersofmobile.atmlocator.db.DbHelper;

import dagger.Component;

@Component(modules = AtmModule.class)
public interface AtmComponent {

    void inject(MapsActivity mapsActivity);

    void inject(AddAtmActivity addAtmActivity);

    DbHelper getDbHelper();
}
