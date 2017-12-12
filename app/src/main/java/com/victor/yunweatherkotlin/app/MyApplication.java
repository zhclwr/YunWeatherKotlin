package com.victor.yunweatherkotlin.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import com.victor.yunweatherkotlin.dao.DaoMaster;
import com.victor.yunweatherkotlin.dao.DaoSession;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

/**
 * Created by Victor on 2017/12/12.
 */

public class MyApplication extends Application {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupDb();
        LitePal.initialize(this);
    }

    public static MyApplication getInstances(){
        return instance;
    }

    private void setupDb() {
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
