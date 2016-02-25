package com.soldiersofmobile.atmlocator.db;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by madejs on 25.02.16.
 */
public class AtmDao extends BaseDaoImpl<Atm, Integer> {

    public AtmDao(Class<Atm> dataClass) throws SQLException {
        super(dataClass);
    }

    public AtmDao(ConnectionSource connectionSource, Class<Atm> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public AtmDao(ConnectionSource connectionSource, DatabaseTableConfig<Atm> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }



    public List<Atm> getAtmsNearby(LatLng latLng) throws SQLException {


        QueryBuilder<Atm, Integer> atmIntegerQueryBuilder = queryBuilder();
        atmIntegerQueryBuilder.where().ge(Atm.Columns.LATITUDE, latLng.latitude - 1);

        return atmIntegerQueryBuilder.query();
    }
}
