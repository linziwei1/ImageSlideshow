# ImageSlideshow
一个兼容安卓4.4.4的图片轮播程序

# 项目结构
```
ImageSlideshow/
├── .github/workflows/build.yml    # GitHub Actions 自动打包脚本
├── app/
│   ├── build.gradle               # App 级 Gradle 配置 (minSdk 19 = Android 4.4)
│   └── src/main/
│       ├── AndroidManifest.xml    # 包含开机自启动权限和接收器
│       ├── java/com/slideshow/app/
│       │   ├── MainActivity.java  # 全屏图片轮播主界面
│       │   └── BootReceiver.java  # 开机自启动广播接收器
│       └── res/
│           ├── anim/              # 淡入淡出动画
│           ├── layout/            # 布局文件
│           ├── mipmap-*/          # 应用图标占位文件
│           ├── raw/               # 轮播图片占位文件
│           │   ├── slide_1.jpg    ← 替换为你的图片
│           │   ├── slide_2.jpg
│           │   ├── slide_3.jpg
│           │   ├── slide_4.jpg
│           │   └── slide_5.jpg
│           └── values/            # 字符串和样式
├── build.gradle                   # 根级 Gradle 配置
├── settings.gradle
└── gradlew.bat                    # Windows Gradle 脚本
```

# 功能特性
- 兼容 Android 4.4.4 (API 19)
- 开机自启动 - 通过 BootReceiver 监听开机广播
- 全屏显示 - 隐藏状态栏和导航栏
- 竖屏锁定 - 1080×1920 分辨率优化
- 淡入淡出动画 - 500ms 切换动画
- 3秒轮播间隔 - 可在 MainActivity.java:31 修改

# 需要替换的占位文件
|文件	|用途	|建议尺寸
|res/raw/slide_*.jpg	|轮播图片	|1080×1920 px
|res/mipmap-mdpi/ic_launcher*.png	|应用图标	|48×48 px
|res/mipmap-hdpi/ic_launcher*.png	|应用图标	|72×72 px
|res/mipmap-xhdpi/ic_launcher*.png	|应用图标	|96×96 px
|res/mipmap-xxhdpi/ic_launcher*.png	|应用图标	|144×144 px
|res/mipmap-xxxhdpi/ic_launcher*.png	|应用图标	|192×192 px

# GitHub Actions 使用方法
1. 将项目推送到 GitHub 仓库
2. 推送后会自动触发构建，或手动在 Actions 页面点击 "Run workflow"
3. 构建完成后在 Artifacts 中下载 app-debug.apk