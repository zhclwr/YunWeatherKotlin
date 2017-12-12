package com.victor.yunweatherkotlin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.victor.yunweatherkotlin.bean.City;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CITY".
*/
public class CityDao extends AbstractDao<City, Void> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity City.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CityCode = new Property(0, String.class, "cityCode", false, "CITY_CODE");
        public final static Property County = new Property(1, String.class, "county", false, "COUNTY");
        public final static Property City = new Property(2, String.class, "city", false, "CITY");
        public final static Property Province = new Property(3, String.class, "province", false, "PROVINCE");
    }


    public CityDao(DaoConfig config) {
        super(config);
    }
    
    public CityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CITY\" (" + //
                "\"CITY_CODE\" TEXT," + // 0: cityCode
                "\"COUNTY\" TEXT," + // 1: county
                "\"CITY\" TEXT," + // 2: city
                "\"PROVINCE\" TEXT);"); // 3: province
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, City entity) {
        stmt.clearBindings();
 
        String cityCode = entity.getCityCode();
        if (cityCode != null) {
            stmt.bindString(1, cityCode);
        }
 
        String county = entity.getCounty();
        if (county != null) {
            stmt.bindString(2, county);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(3, city);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(4, province);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, City entity) {
        stmt.clearBindings();
 
        String cityCode = entity.getCityCode();
        if (cityCode != null) {
            stmt.bindString(1, cityCode);
        }
 
        String county = entity.getCounty();
        if (county != null) {
            stmt.bindString(2, county);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(3, city);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(4, province);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public City readEntity(Cursor cursor, int offset) {
        City entity = new City( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // cityCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // county
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // city
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // province
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, City entity, int offset) {
        entity.setCityCode(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCounty(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCity(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProvince(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(City entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(City entity) {
        return null;
    }

    @Override
    public boolean hasKey(City entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
