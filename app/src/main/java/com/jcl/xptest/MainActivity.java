package com.jcl.xptest;

import static com.jcl.xptest.Utli.AppInfoUtli.getAppIcon;
import static com.jcl.xptest.Utli.AppInfoUtli.getAppName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jcl.xptest.Adapter.HookAppAdapter;
import com.jcl.xptest.Db.ConfigDao;
import com.jcl.xptest.Db.ConfigureDbHelper;
import com.jcl.xptest.Pojo.AppInfo;
import com.jcl.xptest.Utli.AppInfoUtli;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Button add_hook_app;

    private ArrayList<AppInfo> redConfig = new ArrayList<>();

    private ArrayList<AppInfo> appInfoArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        System.out.println(getAppName(this, "com.junge.algorithmAide"));

        if (redConfig.size()>0){
            start(false,redConfig);

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

        add_hook_app.setOnClickListener(this);

        redConfig = getAppInfoArrayList(true);
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
                        Bitmap appIcon = drawableToBitmap(drawable);

                        String appName = getAppName(this,app_pgName);
                        System.out.println("目标应用名："+appName);
                        if (!hook_app_canshu.getText().toString().equals("")){
                            if (!hook_app_BZname.getText().toString().equals("")){
                                String app_BZname = hook_app_BZname.getText().toString();
                                appInfoArrayList.add(new AppInfo(app_pgName,appName,appIcon,app_class,app_modeName,app_return,null,app_BZname));
                            }else {
                                appInfoArrayList.add(new AppInfo(app_pgName, appName, appIcon, app_class, app_modeName, app_return, null, null));
                            }
                        }else {
                            if (!hook_app_BZname.getText().toString().equals("")){
                                String app_BZname = hook_app_BZname.getText().toString();
                                appInfoArrayList.add(new AppInfo(app_pgName,appName,appIcon,app_class,app_modeName,app_return,null,app_BZname));
                            }else {
                                String app_canshu = hook_app_canshu.getText().toString();
                                appInfoArrayList.add(new AppInfo(app_pgName, appName, appIcon, app_class, app_modeName, app_return, app_canshu, null));
                            }
                        }
                        new ConfigDao(this).add(appInfoArrayList.get(0));
                        Toast.makeText(this, "已保存自定义Hook", Toast.LENGTH_SHORT).show();

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

}