package com.jcl.xptest.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.xptest.db.ConfigDao;
import com.jcl.xptest.pojo.AppInfo;
import com.jcl.xptest.R;

import java.util.List;

public class HookAppAdapter extends RecyclerView.Adapter<HookAppAdapter.ViewHolder>  {
    private Context context;
    private List<AppInfo> appInfo;

    private TextView hook_app_pgName;
    private EditText hook_app_class;
    private EditText hook_app_modelName;
    private EditText hook_app_canshu;
    private EditText hook_app_return;
    private EditText hook_app_BZname;
    private Button update_hook_app;

    private AlertDialog dialog;
    public HookAppAdapter(Context context, List<AppInfo> appInfo) {
        this.context = context;
        this.appInfo = appInfo;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView app_icon;
        private TextView app_name;
        public ViewHolder(View itemView) {
            super(itemView);
            app_icon = itemView.findViewById(R.id.app_icon);
            app_name = itemView.findViewById(R.id.app_name);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hook_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppInfo info = appInfo.get(position);
        String app_pgName = "";
        Bitmap app_icon = info.getApp_icon();
        if(info.getApp_class()!=null){
            holder.app_icon.setImageBitmap(app_icon);
            app_pgName = info.getApp_pgName();
            System.out.println("id:"+info.getId());
            System.out.println(info.getHook_app_BeiZhu());
            if (info.getHook_app_BeiZhu()!=null){
                holder.app_name.setText(info.getHook_app_BeiZhu());
            }else {
                holder.app_name.setText(info.getApp_class());
            }

        }else {
            String app_name = info.getApp_name();
            app_pgName = info.getApp_pgName();
            holder.app_icon.setImageBitmap(app_icon);
            holder.app_name.setText(app_name);
        }
        String finalApp_pgName = app_pgName;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (info.getApp_class()!=null){
                    System.out.println("第二步");
                    showConfigDialog(info);
                }else {
                    appInfo = new ConfigDao(context).findByPgName(finalApp_pgName);
                    System.out.println("第一步");
                    notifyDataSetChanged();
                }
            }
        });
        if (info.getApp_class()!=null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public boolean onLongClick(View v) {

                    showDelete(v, info);


                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return appInfo.size();
    }

    private void showConfigDialog(AppInfo info) {
        AlertDialog.Builder builder = new
                AlertDialog.Builder(context);
        dialog = builder.create();
        View dialogView = null;
        dialogView = View.inflate(context,
                R.layout.hook_dialog, null);
        dialog.setView(dialogView);
        dialog.show();
        hook_app_pgName = dialogView.findViewById(R.id.hook_app_pgName);
        hook_app_class = dialogView.findViewById(R.id.hook_app_class);
        hook_app_modelName = dialogView.findViewById(R.id.hook_app_modelName);
        hook_app_canshu = dialogView.findViewById(R.id.hook_app_canshu);
        hook_app_return = dialogView.findViewById(R.id.hook_app_return);
        hook_app_BZname = dialogView.findViewById(R.id.hook_app_BZname);
        update_hook_app = dialogView.findViewById(R.id.update_hook_app);

        String app_pgName = info.getApp_pgName();
        String app_class = info.getApp_class();
        String app_return = info.getApp_return();
        String hook_app_canshu1 ="";
        if (info.getHook_app_canshu()!=null){
         hook_app_canshu1 = info.getHook_app_canshu();
        }
        String app_modeName = info.getApp_modeName();
        String hook_app_beiZhu = info.getHook_app_BeiZhu();
        String app_name = info.getApp_name();
        Bitmap app_icon = info.getApp_icon();
        int id = info.getId();

        hook_app_pgName.setText(app_pgName);
        hook_app_class.setText(app_class);
        hook_app_modelName.setText(app_modeName);
        hook_app_canshu.setText(hook_app_canshu1);
        hook_app_return.setText(app_return);
        hook_app_BZname.setText(hook_app_beiZhu);

        update_hook_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = hook_app_pgName.getText().toString();
                String s2 = hook_app_class.getText().toString();
                String s3 = hook_app_modelName.getText().toString();
                String s4 = hook_app_canshu.getText().toString();
                String s5 = hook_app_return.getText().toString();
                String s6 = hook_app_BZname.getText().toString();
                if (s4.equals("")){
                    s4 = null;
                }

                AppInfo appInfo1 = new AppInfo(s1, app_name, app_icon, s2, s3, s5, s4, s6,id);

                new ConfigDao(context).update(id,appInfo1);

                Toast.makeText(context, "配置已更新", Toast.LENGTH_SHORT).show();

                appInfo = new ConfigDao(context).findByPgName(s1);

                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }

    private void showDelete(View v, AppInfo info) {
        //定义PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(context, v);
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.delete, popupMenu.getMenu());
        //设置PopupMenu的点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println(info.getId());
                new ConfigDao(context).deleteHookById(info.getId());
                Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();

                appInfo = new ConfigDao(context).findByPgName(info.getApp_pgName());
                notifyDataSetChanged();
                return true;
            }
        });
        //显示菜单
        popupMenu.show();
    }

}
