package com.jcl.xptest.utli;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingView extends View {
    private int mLastTouchX;
    private int mLastTouchY;
    private int mScreenWidth;
    private int mScreenHeight;

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取屏幕宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制自定义内容
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 处理触摸事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = (int) event.getRawX();
                mLastTouchY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - mLastTouchX;
                int dy = (int) event.getRawY() - mLastTouchY;
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
                layoutParams.x += dx;
                layoutParams.y += dy;
                // 防止悬浮窗超出屏幕范围
                if (layoutParams.x < 0) {
                    layoutParams.x = 0;
                } else if (layoutParams.x + getWidth() > mScreenWidth) {
                    layoutParams.x = mScreenWidth - getWidth();
                }
                if (layoutParams.y < 0) {
                    layoutParams.y = 0;
                } else if (layoutParams.y + getHeight() > mScreenHeight) {
                    layoutParams.y = mScreenHeight - getHeight();
                }
                ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, layoutParams);
                mLastTouchX = (int) event.getRawX();
                mLastTouchY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                // 处理点击事件
                break;
        }
        return true;
    }
}