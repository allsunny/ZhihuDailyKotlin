# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/frank/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Retain generic type information for use by reflection by converters and adapters.
# copyright zhonghanwen
#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
# 指定代码的压缩级别
-optimizationpasses 5

-ignorewarnings # 抑制警告
-verbose    # 混淆时是否记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射
-dontpreverify # 不预校验
-dontoptimize #不优化输入的类文件
-dontshrink #该选项 表示 不启用压缩  混淆时是否做预校验（可去掉加快混淆速度）

-dontusemixedcaseclassnames # 混淆时不会产生形形色色的类名  是否使用大小写混合
-dontskipnonpubliclibraryclasses #不跳过(混淆) jars中的 非public classes   默认选项

-keepattributes Exceptions # 解决AGPBI警告
-keepattributes Exceptions,InnerClasses,...
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable #运行抛出异常时保留代码行号
#过滤泛型
-keepattributes Signature
#过滤注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }


#继承自activity,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.view.View
-keep class android.support.** {*;}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# 所有View的子类及其子类的get、set方法都不进行混淆
-keep public class * extends android.view.View {  #保持自定义控件指定规则的方法不被混淆
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持指定规则的方法不被混淆（Android layout 布局文件中为控件配置的onClick方法不能混淆）
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(*Event);
}

 #保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* { *; }
# 不混淆R类里及其所有内部static类中的所有static变量字段，$是用来分割内嵌类与其母体的标志
-keep public class **.R$*{
   public static final int *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

#过滤js
-keepattributes *JavascriptInterface*
#保护WebView对HTML页面的API不被混淆
-keep class **.Webview2JsInterface { *; }
-keepclassmembers class * extends android.webkit.WebViewClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
     public void *(android.webkit.WebView,java.lang.String);
}
#对WebView的简单说明下：经过实战检验,做腾讯QQ登录，如果引用他们提供的jar，若不加防止WebChromeClient混淆的代码，oauth认证无法回调，
#反编译基代码后可看到他们有用到WebChromeClient，加入此代码即可。

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);  #保持自定义控件类不被混淆，指定格式的构造方法不去混淆
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keep public class * {
#    public protected *;
#}

#需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keep public class * implements java.io.Serializable {
        public *;
}

#不混淆Serializable接口的子类中指定的某些成员变量和方法
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep class org.** {*;}
-keep class com.android.**{*;}
#-assumenosideeffects class_specification

#v7 不混淆
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-dontwarn android.support.v7.**

# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# support-v7-appcompat
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# support-v7-cardview.pro
# http://stackoverflow.com/questions/29679177/cardview-shadow-not-appearing-in-lollipop-after-obfuscate-with-proguard/29698051
-keep class android.support.v7.widget.RoundRectDrawable { *; }

-dontwarn android.net.http.**
-keep class org.apache.http.** { *;}
#-------------------------------------------基本不用动区域--------------------------------------------


# >>>>>>>>>>>>>>>>>>>>>>>>  Glide3.7  Start >>>>>>>>>>>>>>>>>>>>>>>>
# Glide specific rules #
# https://github.com/bumptech/glide
-dontwarn  com.bumptech.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
# >>>>>>>>>>>>>>>>>>>>>>>>  Glide3.7  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  sharesdk  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-keep class m.framework.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**
-dontwarn **.R$*
# >>>>>>>>>>>>>>>>>>>>>>>>  sharesdk  End >>>>>>>>>>>>>>>>>>>>>>>>



# >>>>>>>>>>>>>>>>>>>>>>>>  Umeng  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# >>>>>>>>>>>>>>>>>>>>>>>>  Umeng  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  BaseRecyclerViewAdapterHelper  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
# >>>>>>>>>>>>>>>>>>>>>>>>  BaseRecyclerViewAdapterHelper  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  Android-AdvancedWebView  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class * extends android.webkit.WebChromeClient { *; }
-dontwarn im.delight.android.webview.**
# >>>>>>>>>>>>>>>>>>>>>>>>  Android-AdvancedWebView  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  Butterknife  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class butterknife.Butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
# >>>>>>>>>>>>>>>>>>>>>>>>  Butterknife  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  Retrofit2  Start >>>>>>>>>>>>>>>>>>>>>>>>
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# >>>>>>>>>>>>>>>>>>>>>>>>  Retrofit2  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  okhttp3  Start >>>>>>>>>>>>>>>>>>>>>>>>
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
## okhttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.{*;}
# >>>>>>>>>>>>>>>>>>>>>>>>  okhttp3  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  RxJava  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# >>>>>>>>>>>>>>>>>>>>>>>>  RxJava  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  Gson  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
# >>>>>>>>>>>>>>>>>>>>>>>>  Gson  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  EventBus  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
# >>>>>>>>>>>>>>>>>>>>>>>>  EventBus  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  LeakCanary  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
# >>>>>>>>>>>>>>>>>>>>>>>>  LeakCanary  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  阿里云推送  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**
# >>>>>>>>>>>>>>>>>>>>>>>>  阿里云推送  End >>>>>>>>>>>>>>>>>>>>>>>>

-keep class com.orhanobut.** {*;}
-keep class com.meituan.android.walle.** {*;}
-keep class com.afollestad.material-dialogs.** {*;}
-keep class com.timehop.stickyheadersrecyclerview.**{*;}
-keep class com.youth.banner.**{*;}
-keep class com.airsaid.library.**{*;}
-keep class com.androidkun.**{*;}
-keep class com.leo.gesturelibray.**{*;}
-keep class com.github.SuperKotlin.**{*;}
-keep class com.vdurmont.**{*;}
-keep class de.hdodenhof.**{*;}
-keep class com.gongwen.**{*;}
-keep class com.gcssloop.widget.**{*;}
#-keep class ch.ielse:switchbutton.**{*;}
# SmartRefreshHeader 无需混淆过滤
-dontwarn com.squareup.picasso.**

#所有实体类不能混淆
-keep class com.allsunny.zhihudailykotlin.bean.** {*;}