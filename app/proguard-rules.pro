# General
-keepattributes Exceptions, Signature, LineNumberTable, *Annotation*
-keep class moises.com.appcoer.model.** { *; }
-keep class moises.com.appcoer.api.** { *; }
-keep class moises.com.appcoer.global.** { *; }
-keepclassmembers class moises.com.appcoer.model.** { *; }

# ButterKnife
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# TestFairy
-dontwarn com.testfairy.**
-keep class com.testfairy.** { *; }

# Google GMS
-dontwarn com.google.android.gms.internal.**
-keep class com.google.android.gms.internal.** { *; }

# Google annotations
-dontwarn com.google.errorprone.annotations.*

# okhttp3
-dontwarn org.xmlpull.v1.**
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

# Glide
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# ShapeImageView
-dontwarn com.github.siyamed.shapeimageview.**
-keep class org.kxml2..io.** { *; }

# Java
-keep public class * extends java.lang.annotation.** { *; }