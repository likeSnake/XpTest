package com.jcl.xptest.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.xptest.Db.ConfigDao;
import com.jcl.xptest.Db.ConfigureDbHelper;
import com.jcl.xptest.Pojo.AppInfo;
import com.jcl.xptest.R;

import java.util.ArrayList;
import java.util.List;

public class HookAppAdapter extends RecyclerView.Adapter<HookAppAdapter.ViewHolder>  {
    private Context context;
    private List<AppInfo> appInfo;
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
        Bitmap app_icon = info.getApp_icon();
        String app_name = info.getApp_name();
        String app_pgName = info.getApp_pgName();
        holder.app_icon.setImageBitmap(app_icon);
        holder.app_name.setText(app_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                if (info.getApp_class()!=null){
                    System.out.println("第二步");
                }else {
                    appInfo = new ConfigDao(context).findByPgName(app_pgName);
                    System.out.println("第一步");
                    notifyDataSetChanged();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return appInfo.size();
    }


}
