package com.soldiersofmobile.atmlocator.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "atm", daoClass = AtmDao.class)
public class Atm {

    class Columns {

        public static final String ADDRESS = "address";
        public static final String LATITUDE = "latitude";
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = Columns.ADDRESS)
    private String address;
    @DatabaseField(canBeNull = false, columnName = Columns.LATITUDE)
    private double latitude;
    @DatabaseField(canBeNull = false)
    private double longitude;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Bank bank;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "Atm{" +
                "address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", bank=" + bank +
                '}';
    }
}
