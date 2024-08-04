package com.jcl.xptest.utli;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.jcl.xptest.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class MyUtil {

    public static Context context;

    public MyUtil(Context contexts) {
        context = contexts;
    }

    public static void MyLog(Object o){
        System.out.println("MyLog-ll: "+o);
    }

    public static void MyToast(Context context,String s){
        Toast toast = Toast.makeText(context, "" , Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }

    public static void startFloatingWindow() {
        if (context!=null){
            // 创建浮动窗口布局
            View floatView = LayoutInflater.from(context).inflate(R.layout.float_window, null);

            // 创建悬浮窗的布局参数
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.START;
            params.x = 0;
            params.y = 0;

            // 初始化 WindowManager
            WindowManager windowManager = ((Activity)context).getWindow().getWindowManager();


            // 添加悬浮窗到 WindowManager
            windowManager.addView(floatView, params);

            // 设置悬浮窗的触摸事件
            floatView.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 记录初始位置和触摸位置
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            // 更新悬浮窗的位置
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatView, params);
                            return true;
                    }
                    return false;
                }
            });

            Button okBtn = floatView.findViewById(R.id.start_button);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击确定按钮后的操作

                }
            });
            Button cancelBtn = floatView.findViewById(R.id.stop_button);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else {
            MyLog("MyContext为null");
        }


    }
    public static String addNewLine(String str) {
        StringBuilder sb = new StringBuilder(str);
        int i = 76;
        while (i < sb.length()) {
            sb.insert(i, "\\n");
            i += 78; // 因为我们插入了一个额外的字符，所以要跳过它
        }

        return sb.toString()+"\\n";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String myCompress(String str)  {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.close();

            //String s1 = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bos.toByteArray());
       //     Base64.encodeBase64URLSafeString(jsonBytes);

            //String s = Base64.getUrlEncoder().encodeToString(bos.toByteArray());

            return org.apache.commons.codec.binary.Base64.encodeBase64String(bos.toByteArray());
            //return s1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String compress(String input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            gzos.write(input.getBytes("UTF-8"));
            gzos.close();
            byte[] compressedBytes = baos.toByteArray();
            baos.close();
            return Base64.getEncoder().withoutPadding().encodeToString(compressedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeTextFileToInternalStorage(Context context, String fileName, String fileContent) {
        try {
            // 获取内部存储的根目录
            File root = context.getFilesDir();

            MyLog("路径: "+root);
            // 创建目标文件
            File file = new File(root, fileName);

            // 将字符串写入文件
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
