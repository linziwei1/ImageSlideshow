package com.slideshow.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 全屏图片轮播Activity
 * 兼容Android 4.4.4 (API 19)
 * 竖屏显示，分辨率1080*1920
 */
public class MainActivity extends Activity {

    private ImageView imageView;
    private Handler handler;
    private Runnable slideRunnable;

    // 图片资源列表
    private List<Integer> imageResources;
    private int currentIndex = 0;

    // 轮播间隔时间（毫秒）
    private static final long SLIDE_INTERVAL = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏
        setupFullscreen();

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        handler = new Handler();

        // 加载图片资源
        loadImageResources();

        // 显示第一张图片
        if (!imageResources.isEmpty()) {
            imageView.setImageResource(imageResources.get(0));
        }

        // 开始轮播
        startSlideshow();
    }

    /**
     * 设置全屏显示
     */
    private void setupFullscreen() {
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 全屏标志
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // 隐藏状态栏和导航栏 (API 19+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    /**
     * 从raw文件夹加载所有图片资源
     * 图片命名规则：slide_1, slide_2, slide_3, ...
     */
    private void loadImageResources() {
        imageResources = new ArrayList<>();

        // 通过反射获取raw文件夹中所有slide_开头的资源
        Field[] fields = R.raw.class.getFields();
        List<String> slideNames = new ArrayList<>();

        for (Field field : fields) {
            String name = field.getName();
            if (name.startsWith("slide_")) {
                slideNames.add(name);
            }
        }

        // 按名称排序
        java.util.Collections.sort(slideNames);

        // 获取资源ID
        for (String name : slideNames) {
            int resId = getResources().getIdentifier(name, "raw", getPackageName());
            if (resId != 0) {
                imageResources.add(resId);
            }
        }

        // 如果没有找到slide_开头的资源，则加载所有raw资源
        if (imageResources.isEmpty()) {
            for (Field field : fields) {
                try {
                    int resId = field.getInt(null);
                    imageResources.add(resId);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开始轮播
     */
    private void startSlideshow() {
        if (imageResources.isEmpty()) {
            return;
        }

        slideRunnable = new Runnable() {
            @Override
            public void run() {
                showNextImage();
                handler.postDelayed(this, SLIDE_INTERVAL);
            }
        };

        handler.postDelayed(slideRunnable, SLIDE_INTERVAL);
    }

    /**
     * 显示下一张图片，带淡入淡出动画
     */
    private void showNextImage() {
        if (imageResources.isEmpty()) {
            return;
        }

        // 淡出动画
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // 切换到下一张图片
                currentIndex = (currentIndex + 1) % imageResources.size();

                // 从raw资源加载图片
                try {
                    java.io.InputStream is = getResources().openRawResource(imageResources.get(currentIndex));
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 淡入动画
                Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                imageView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        imageView.startAnimation(fadeOut);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullscreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && slideRunnable != null) {
            handler.removeCallbacks(slideRunnable);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setupFullscreen();
        }
    }
}
