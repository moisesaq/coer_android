
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