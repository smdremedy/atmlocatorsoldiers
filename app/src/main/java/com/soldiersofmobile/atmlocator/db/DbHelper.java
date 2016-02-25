package com.soldiersofmobile.atmlocator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by madejs on 25.02.16.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

    public static final String ATMS_DB = "atms.db";
    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, ATMS_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Bank.class);
            TableUtils.createTable(connectionSource, Atm.class);

            Dao<Bank, Integer> bankDao = getDao(Bank.class);

            bankDao.create(new Bank("PKO BP", "500 600 700"));
            bankDao.create(new Bank("PKO SA", "500 600 701"));
            bankDao.create(new Bank("Milenium", "500 600 702"));
            bankDao.create(new Bank("ING Bank", "500 600 703"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
