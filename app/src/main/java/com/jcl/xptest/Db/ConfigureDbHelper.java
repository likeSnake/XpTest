package com.jcl.xptest.Db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ConfigureDbHelper extends SQLiteOpenHelper {

    private static SQLiteOpenHelper sql;
    private final static String name = "HookConfig.db";

    public static SQLiteOpenHelper getInstance(Context context){
        if(sql == null){
            sql = new ConfigureDbHelper(context, name, null, 1);
        }
        return sql;
    }
    public ConfigureDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ConfigureDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public ConfigureDbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table HookAppInfo(_id integer primary key autoincrement,hook_app_AppName text,hook_app_AppImage BLOB,hook_app_pgName text,hook_app_class text,hook_app_modelName text,hook_app_canshu text,hook_app_return text,hook_app_BZname text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
