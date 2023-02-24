package com.jcl.xptest.Utli;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcl.xptest.Pojo.AppInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AppInfoUtli {
    public static Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppName(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadLabel(pm).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void save(ArrayList<AppInfo> arrayList,Context context){
//SharedPreferences存储list数据
        SharedPreferences.Editor editor = context.getSharedPreferences("key",MODE_PRIVATE).edit();
        Gson gson = new Gson();
//将list转成Json
        String jsonStr = gson.toJson(arrayList);
        editor.putString("HookAppList",jsonStr);
//提交
        editor.apply();
    }

    public static ArrayList<AppInfo> raed(Context context){
        //创建sp对象，取出关键字为key的sp
        ArrayList<AppInfo> appInfoList;
        SharedPreferences sp = context.getSharedPreferences("serialModel",Context.MODE_PRIVATE);
        String str = sp.getString("HookAppList","");
        if (str != ""){
            Gson gson = new Gson();
            //TypeToken<List<Integer>>()将json数据转成List集合
            appInfoList = gson.fromJson(str,new TypeToken<ArrayList<AppInfo>>(){}.getType());
            return appInfoList;
        }else {
            return null;
        }
    }
}
