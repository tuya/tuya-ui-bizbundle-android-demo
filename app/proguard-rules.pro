# -----------------------------------反射与堆栈属性-------------------------------------
-keepattributes Exceptions,InnerClasses,Deprecated,Synthetic,EnclosingMethod
-keepattributes *Annotation*,Signature
-keepattributes SourceFile,LineNumberTable

# -----------------------------------默认保留区------------------------------------------

# 不混淆任何包含native方法的类的类名以及native方法名，但当成员没有被引用时会被移除
-keepclasseswithmembernames class * {
    native <methods>;
}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

# 不混淆任何一个View中的setXxx()和getXxx()方法，因为属性动画需要有相应的setter和getter的方法实现，混淆了就无法工作了。
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# 不混淆Activity中参数是View的方法，因为有这样一种用法，在XML中配置android:onClick=”buttonClick”属性，当用户点击该按钮时就会调用Activity中的buttonClick(View view)方法，如果这个方法被混淆的话就找不到了。
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep class javax.jmdns.**{*;}
-dontwarn androidx.annotation.**
-dontwarn javax.annotation.**

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class org.json.**{*;}

-keep,allowobfuscation @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class *
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

## thingsmart sdk
# SDK Cronet 桥接依赖：按字符串反射 installProvider/isInstalled，统一保留类与方法。
-keep class com.google.android.gms.net.** { *; }

# Matter/CHIP Java/native 绑定，统一保留 JNI 契约。
-keep class chip.** { *; }
-dontwarn chip.**
-keep class javax.jmdns.**{*;}
-dontwarn javax.jmdns.**
# Matter Kotlin TLV/JSON 支持，与 CHIP 同版本统一保留。
-keep class matter.** { *; }

# 重打包 Bouncy Castle，保留 Provider/SPI 按名称加载入口。
-keep class com.thingclips.bouncycastle.** { *; }

# SDK 包：保留类和成员，不再逐类枚举 JNI、反射入口或 JSON 模型。
# network 排除 UI 的 audit 子树；android.* 只匹配该层，不递归保留 UI 子包。
-keep class com.thingclips.ble.** { *; }
-keep class com.thingclips.crypto.** { *; }
-keep class com.thingclips.scene.** { *; }
-keep class com.thingclips.sdk.** { *; }
-keep class com.thingclips.smart.android.* { *; }
-keep class com.thingclips.smart.android.aistream.** { *; }
-keep class com.thingclips.smart.android.base.** { *; }
-keep class com.thingclips.smart.android.ble.** { *; }
-keep class com.thingclips.smart.android.blemesh.** { *; }
-keep class com.thingclips.smart.android.camera.** { *; }
-keep class com.thingclips.smart.android.common.** { *; }
-keep class com.thingclips.smart.android.config.** { *; }
-keep class com.thingclips.smart.android.device.** { *; }
-keep class com.thingclips.smart.android.hardware.** { *; }
-keep class com.thingclips.smart.android.mqtt.** { *; }
-keep class com.thingclips.smart.android.mvp.** { *; }
-keep class !com.thingclips.smart.android.network.audit.**,com.thingclips.smart.android.network.** { *; }
-keep class com.thingclips.smart.android.push.** { *; }
-keep class com.thingclips.smart.android.sweeper.** { *; }
-keep class com.thingclips.smart.android.user.** { *; }
-keep class com.thingclips.smart.asynclib.** { *; }
-keep class com.thingclips.smart.audioengine.** { *; }
-keep class com.thingclips.smart.avlogger.** { *; }
-keep class com.thingclips.smart.ble.** { *; }
-keep class com.thingclips.smart.bluet.** { *; }
-keep class com.thingclips.smart.cache.** { *; }
-keep class com.thingclips.smart.components.** { *; }
-keep class com.thingclips.smart.config.** { *; }
-keep class com.thingclips.smart.device.bean.** { *; }
-keep class com.thingclips.smart.dp.parser.** { *; }
-keep class com.thingclips.smart.home.sdk.** { *; }
-keep class com.thingclips.smart.interior.** { *; }
-keep class com.thingclips.smart.mmkv.** { *; }
-keep class com.thingclips.smart.mqtt.** { *; }
-keep class com.thingclips.smart.mqttclient.** { *; }
-keep class com.thingclips.smart.optimus.** { *; }
-keep class com.thingclips.smart.scene.** { *; }
-keep class com.thingclips.smart.sdk.** { *; }
-keep class com.thingclips.smart.security.** { *; }
-keep class com.thingclips.smart.thingapicheckannotation.** { *; }
-keep class com.thingclips.yulib.** { *; }

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}

#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.thingclips.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.thingclips.smart.mqttclient.mqttv3.**

#cronet for quic
-keep class com.thingclips.smart.quiclib.** { *; }

-keep class com.thingclips.smart.mediaplayer.** {*;}

#thingsmart-miniapp-security
-keep class com.thingclips.security.base.**{*;}

-keep class com.huawei.** { *; }
-dontwarn com.huawei.**

# qq 音乐
-dontwarn com.tencent.qqmusic.**

## 微信
-keep class com.tencent.mm.opensdk.** { *; }
-dontwarn com.tencent.mm.opensdk.**

# amazon avs
-keep class com.amazon.identity.**{*;}

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

# RN
-keep class com.facebook.react.ReactInstanceManager { *; }

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

-keepclassmembers class com.github.mikephil.charting.animation.ChartAnimator {
    public void setPhaseX(float);
    public void setPhaseY(float);
}
-keepclassmembers class com.github.mikephil.charting.jobs.AnimatedViewPortJob {
    public void setPhase(float);
}