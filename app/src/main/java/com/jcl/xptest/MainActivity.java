package com.jcl.xptest;


import static com.jcl.xptest.utli.AppInfoUtli.getAppIcon;
import static com.jcl.xptest.utli.AppInfoUtli.getAppName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.xptest.adapter.HookAppAdapter;
import com.jcl.xptest.db.ConfigDao;
import com.jcl.xptest.pojo.AppInfo;
import com.jcl.xptest.utli.MyUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView hookAppList;
    private HookAppAdapter hookAppAdapter;
    private EditText hook_app_pgName;
    private EditText hook_app_class;
    private EditText hook_app_modelName;
    private EditText hook_app_canshu;
    private EditText hook_app_return;
    private EditText hook_app_BZname;
    private TextView isUsed;
    private Button add_hook_app;
    private ImageView back;

    private ArrayList<AppInfo> redConfig = new ArrayList<>();

    private AppInfo appInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        init();
        initDate();
        System.out.println(Integer.parseInt("2064c", 16));

        if (redConfig.size()>0){
            start(false,redConfig);
        }

    }

    public void initDate(){
       MyUtil myUtil = new MyUtil(MainActivity.this);
    }
    public void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 111);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                // 用户已经授权悬浮窗权限，执行相应的操作
            } else {
                // 用户未授权悬浮窗权限，执行相应的操作
            }
        }
    }

    public void start(Boolean b,List<AppInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        hookAppAdapter = new HookAppAdapter(this,list);
        if(b){
            hookAppList.scrollToPosition(hookAppAdapter.getItemCount()-1);
        }
        hookAppList.setLayoutManager(manager);
        hookAppList.setAdapter(hookAppAdapter);
    }

    public void init(){


        hookAppList = findViewById(R.id.HookAppList);
        hook_app_pgName = findViewById(R.id.hook_app_pgName);
        hook_app_canshu = findViewById(R.id.hook_app_canshu);
        hook_app_class = findViewById(R.id.hook_app_class);
        hook_app_modelName = findViewById(R.id.hook_app_modelName);
        hook_app_return = findViewById(R.id.hook_app_return);
        hook_app_BZname = findViewById(R.id.hook_app_BZname);
        add_hook_app = findViewById(R.id.add_hook_app);
        isUsed = findViewById(R.id.isUsed);
        back = findViewById(R.id.back);

        back.setOnClickListener(this);
        add_hook_app.setOnClickListener(this);

        redConfig = getAppInfoArrayList(true);

        isUsed.setText(isHook()?"已激活":"未激活");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_hook_app:

                addHookApp();
                break;
        }
    }

    public void addHookApp(){
        if (hook_app_pgName!=null && !hook_app_pgName.getText().toString().equals("") ){
            if (hook_app_class!=null && !hook_app_class.getText().toString().equals("") ){
                if (hook_app_modelName!=null && !hook_app_modelName.getText().toString().equals("") ){
                    if (hook_app_return!=null&& !hook_app_return.getText().toString().equals("") ){
                        String app_pgName = hook_app_pgName.getText().toString();
                        String app_class = hook_app_class.getText().toString();
                        String app_modeName = hook_app_modelName.getText().toString();
                        String app_return = hook_app_return.getText().toString();
                        Drawable drawable = getAppIcon(this, app_pgName);
                        Bitmap appIcon = null;
                        if (drawable!=null) {
                             appIcon = drawableToBitmap(drawable);
                        }else {
                            Toast.makeText(this, "目标应用不存在！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String appName = getAppName(this,app_pgName);
                        System.out.println("目标应用名："+appName);
                        System.out.println(hook_app_canshu.getText().toString());
                        if (!hook_app_canshu.getText().toString().equals("")){
                            String app_CanShu = hook_app_canshu.getText().toString();
                            if (!hook_app_BZname.getText().toString().equals("")){
                                String app_BZname = hook_app_BZname.getText().toString();
                                System.out.println("添加的备注："+app_BZname);
                                appInfo = new AppInfo(app_pgName, appName, appIcon, app_class, app_modeName, app_return, app_CanShu, app_BZname);
                            }else {
                                appInfo = new AppInfo(app_pgName, appName, appIcon, app_class, app_modeName, app_return, app_CanShu, null);
                            }
                        }else {
                            if (!hook_app_BZname.getText().toString().equals("")){
                                String app_BZname = hook_app_BZname.getText().toString();
                                appInfo = new AppInfo(app_pgName,appName,appIcon,app_class,app_modeName,app_return,null,app_BZname);
                            }else {
                                appInfo = (new AppInfo(app_pgName, appName, appIcon, app_class, app_modeName, app_return, null, null));
                            }
                        }
                        int add = new ConfigDao(this).add(appInfo);
                        if (add == 0) {
                            Toast.makeText(this, "已保存自定义Hook", Toast.LENGTH_SHORT).show();
                        }
                        redConfig = getAppInfoArrayList(true);
                        start(false,redConfig);

                    }else {
                        Toast.makeText(this, "返回值不能为空", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "方法名不能为空", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "类名不能为空", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "包名不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<AppInfo> getAppInfoArrayList(Boolean distinct){

        return new ConfigDao(this).findAppName(distinct);
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if ((drawable.getIntrinsicWidth() <= 0) || (drawable.getIntrinsicHeight() <= 0)) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
        else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    public boolean isHook(){
        return false;
    }
    public Context getMyContext(){
        Context context = this;
        return context;
    }

}