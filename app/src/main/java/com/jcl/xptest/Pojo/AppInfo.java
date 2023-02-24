package com.jcl.xptest.Pojo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class AppInfo {

    private String app_name;
    private String app_pgName;
    private Bitmap app_icon;
    private String app_class;
    private String app_modeName;
    private String app_return;
    private String hook_app_canshu;
    private String hook_app_BeiZhu;

    public AppInfo(String app_pgName, String app_name, Bitmap app_icon, String app_class, String app_modeName, String app_return, String hook_app_canshu,String hook_app_BeiZhu) {
        this.app_name = app_name;
        this.app_pgName = app_pgName;
        this.app_icon = app_icon;
        this.app_class = app_class;
        this.app_modeName = app_modeName;
        this.app_return = app_return;
        this.hook_app_canshu = hook_app_canshu;
        this.hook_app_BeiZhu = hook_app_BeiZhu;
    }

    public String getHook_app_BeiZhu(){
        return hook_app_BeiZhu;
    }
    public void setHook_app_BeiZhu(String app_beiZhu){
       this.hook_app_BeiZhu = app_beiZhu;
    }
    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_pgName() {
        return app_pgName;
    }

    public void setApp_pgName(String app_pgName) {
        this.app_pgName = app_pgName;
    }

    public Bitmap getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(Bitmap app_icon) {
        this.app_icon = app_icon;
    }

    public String getApp_class() {
        return app_class;
    }

    public void setApp_class(String app_class) {
        this.app_class = app_class;
    }

    public String getApp_modeName() {
        return app_modeName;
    }

    public void setApp_modeName(String app_modeName) {
        this.app_modeName = app_modeName;
    }

    public String getApp_return() {
        return app_return;
    }

    public void setApp_return(String app_return) {
        this.app_return = app_return;
    }

    public String getHook_app_canshu() {
        return hook_app_canshu;
    }

    public void setHook_app_canshu(String hook_app_canshu) {
        this.hook_app_canshu = hook_app_canshu;
    }
}
