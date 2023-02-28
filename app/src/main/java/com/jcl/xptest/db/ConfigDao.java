package com.jcl.xptest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.jcl.xptest.pojo.AppInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ConfigDao {
    private SQLiteOpenHelper myDbHelper;

    private Context context;
    public ConfigDao(Context context){
        myDbHelper = ConfigureDbHelper.getInstance(context);
        System.out.println("myDbHelper:"+myDbHelper);
        this.context = context;
    }

    //添加配置
    public int add(AppInfo appInfo){
        Bitmap hook_app_AppImage = appInfo.getApp_icon();
        String hook_app_class = appInfo.getApp_class();
        String hook_app_AppName = appInfo.getApp_name();
        String hook_app_pgName = appInfo.getApp_pgName();
        String hook_app_modelName = appInfo.getApp_modeName();
        String hook_app_return = appInfo.getApp_return();
        String hook_app_BZname = appInfo.getHook_app_BeiZhu();
        String hook_app_canshu = appInfo.getHook_app_canshu();


        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if(db.isOpen()){
            Cursor c = db.query("HookAppInfo", new String[]{"hook_app_AppName"}, "hook_app_class=? and hook_app_modelName=? and hook_app_canshu=?",new String[]{hook_app_class,hook_app_modelName,hook_app_canshu}, null, null, null);
            if (c.getCount()==0) {
                c.close();
                ContentValues values = new ContentValues();
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                hook_app_AppImage.compress(Bitmap.CompressFormat.PNG, 100, os);

                values.put("hook_app_AppName", hook_app_AppName);
                values.put("hook_app_AppImage", os.toByteArray());
                values.put("hook_app_pgName", hook_app_pgName);
                values.put("hook_app_class", hook_app_class);
                values.put("hook_app_modelName", hook_app_modelName);
                values.put("hook_app_canshu", hook_app_canshu);
                values.put("hook_app_return", hook_app_return);
                values.put("hook_app_BZname", hook_app_BZname);
                db.insert("HookAppInfo", "_id", values);
                db.close();
                return 0;
            }else {
                c.close();
                Toast.makeText(context, "已存在该方法的hook", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }
        return 1;
    }

    //更新Hook配置
    public void update(int id,AppInfo appInfo){
        Bitmap hook_app_AppImage = appInfo.getApp_icon();
        String hook_app_class = appInfo.getApp_class();
        String hook_app_AppName = appInfo.getApp_name();
        String hook_app_pgName = appInfo.getApp_pgName();
        String hook_app_modelName = appInfo.getApp_modeName();
        String hook_app_return = appInfo.getApp_return();
        String hook_app_BZname = appInfo.getHook_app_BeiZhu();
        String hook_app_canshu = appInfo.getHook_app_canshu();


        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            hook_app_AppImage.compress(Bitmap.CompressFormat.PNG, 100, os);
            values.put("hook_app_AppName", hook_app_AppName);
            values.put("hook_app_AppImage", os.toByteArray());

            values.put("hook_app_pgName", hook_app_pgName);
            values.put("hook_app_class", hook_app_class);
            values.put("hook_app_modelName", hook_app_modelName);
            values.put("hook_app_canshu", hook_app_canshu);
            values.put("hook_app_return", hook_app_return);
            values.put("hook_app_BZname", hook_app_BZname);
            db.update("HookAppInfo", values, " _id = ? ", new String[]{String.valueOf(id)});
            db.close();
        }

    }

    //得到所有Hook配置
    public ArrayList<AppInfo> findAll(){
        ArrayList<AppInfo> AppHookInfo = new ArrayList<>();

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor c = db.query("HookAppInfo", new String[]{"hook_app_AppName","hook_app_AppImage","hook_app_pgName","hook_app_class","hook_app_modelName","hook_app_canshu","hook_app_return","hook_app_BZname","_id"}, null, null, null, null, null,null);
            while(c.moveToNext()){
                String hook_app_AppName = c.getString(0);
                byte[]  in = c.getBlob(1);
                Bitmap hook_app_AppImage = BitmapFactory.decodeByteArray(in, 0, in.length);
                String hook_app_pgName = c.getString(2);
                String hook_app_class = c.getString(3);
                String hook_app_modelName = c.getString(4);
                String hook_app_canshu = c.getString(5);
                String hook_app_return = c.getString(6);
                String hook_app_BZname = c.getString(7);
                int ID = c.getInt(8);
                AppHookInfo.add(new AppInfo(hook_app_pgName,hook_app_AppName,hook_app_AppImage,hook_app_class,hook_app_modelName,hook_app_return,hook_app_canshu,hook_app_BZname,ID));
            }
            c.close();
            db.close();
        }
        return AppHookInfo;
    }

    public ArrayList<AppInfo> findByPgName(String pgName){
        ArrayList<AppInfo> AppHookInfo = new ArrayList<AppInfo>();
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor c = db.query("HookAppInfo", new String[]{"hook_app_AppName","hook_app_AppImage","hook_app_pgName","hook_app_class","hook_app_modelName","hook_app_canshu","hook_app_return","hook_app_BZname","_id"}, "hook_app_pgName=?",new String[]{pgName}, null, null, null);
            while(c.moveToNext()){
                String hook_app_AppName = c.getString(0);
                byte[]  in = c.getBlob(1);
                Bitmap hook_app_AppImage = BitmapFactory.decodeByteArray(in, 0, in.length);
                String hook_app_pgName = c.getString(2);
                String hook_app_class = c.getString(3);
                String hook_app_modelName = c.getString(4);
                String hook_app_canshu = c.getString(5);
                String hook_app_return = c.getString(6);
                String hook_app_BZname = c.getString(7);
                int ID = c.getInt(8);
                AppHookInfo.add(new AppInfo(hook_app_pgName,hook_app_AppName,hook_app_AppImage,hook_app_class,hook_app_modelName,hook_app_return,hook_app_canshu,hook_app_BZname,ID));
            }
            c.close();
            db.close();
        }
        return AppHookInfo;
    }
    public ArrayList<AppInfo> findAppName(Boolean distinct){
        ArrayList<AppInfo> AppHookInfo = new ArrayList<AppInfo>();
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor c = db.query(distinct,"HookAppInfo", new String[]{"hook_app_AppName","hook_app_AppImage","hook_app_pgName"}, null, null, null, null, null,null);
            while(c.moveToNext()){
                String hook_app_AppName = c.getString(0);
                byte[]  in = c.getBlob(1);
                Bitmap hook_app_AppImage = BitmapFactory.decodeByteArray(in, 0, in.length);
                String hook_app_pgName = c.getString(2);
                AppHookInfo.add(new AppInfo(hook_app_pgName,hook_app_AppName,hook_app_AppImage,null,null,null,null,null));
            }
            c.close();
            db.close();
        }
        return AppHookInfo;
    }

    public void deleteHookById(int id){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        int result = db.delete("HookAppInfo", "_id=?", new String[]{String.valueOf(id)});
    }

}
