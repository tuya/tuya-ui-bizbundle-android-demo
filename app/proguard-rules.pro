## thingsmart sdk
-keep class com.thingclips.sdk.**{*;}
-keep class com.thingclips.smart.home.**{*;}
-keep class com.thingclips.smart.mqtt.**{*;}
-keepnames interface com.thingclips.smart.home.**{*;}
-keep class com.thingclips.smart.sdk.**{*;}
-keepnames interface com.thingclips.smart.sdk.**{*;}
-keep class com.thingclips.smart.common.**{*;}
-keep class com.thingclips.smart.android.**{*;}
-keepnames interface com.thingclips.smart.android.**{*;}
-keep class com.thingclips.smart.mmkv.**{*;}

## bean
-keep class com.thingclips.**.bean.**{*;}

#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.thingclips.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.thingclips.smart.mqttclient.mqttv3.**

#cronet for quic
-keep class com.thingclips.smart.quiclib.** { *; }
-dontwarn androidx.annotation.**
-dontwarn javax.annotation.**

-keep class com.thingclips.smart.mediaplayer.** {*;}

# Matter SDK
-keep class chip.** { *; }
-dontwarn chip.**
-keep class javax.jmdns.**{*;}
-dontwarn javax.jmdns.**

-keep class com.huawei.** { *; }
-dontwarn com.huawei.**

# qq 音乐
-dontwarn com.tencent.qqmusic.**

## 微信
-keep class com.tencent.mm.opensdk.** { *; }
-dontwarn com.tencent.mm.opensdk.**

# Keep RMI related classes
-dontwarn java.rmi.**
-keep class java.rmi.** { *; }

# Keep JavaScript engine related classes
-dontwarn javax.script.**
-keep class javax.script.** { *; }

-dontwarn kotlinx.parcelize.Parcelize

-dontwarn com.thingclips.android.universal.base.anno.MethodInfo

# Fresco
-keep class com.facebook.drawee.backends.pipeline.Fresco

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# RxJava2 / Reactive Streams
-dontwarn java.util.concurrent.Flow**
-dontwarn org.reactivestreams.**

# Google Play Services / GMS
# SDK 自带 consumer rules 已处理大多数反射入口和必要的 dontwarn
-dontwarn com.google.android.gms.**
# 项目自己的 GMS 回调监听器可被 R8 识别；如构建器较旧，可加防御性规则
-keepnames class * implements com.google.android.gms.tasks.OnSuccessListener
-keepnames class * implements com.google.android.gms.tasks.OnFailureListener
-keepnames class * implements com.google.android.gms.tasks.OnCompleteListener

# Lynx xelement: overlay/refresh 元素实现模块未随 gzl-lynx-shell 引入，
# 基础包 lynx-xelement 仅含 AutoRegistry 注册胶水类，引用悬空导致 R8 Missing class，忽略即可。
-dontwarn com.lynx.xelement.overlay.**
-dontwarn com.lynx.xelement.refresh.**


-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder