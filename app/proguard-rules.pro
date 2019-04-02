# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\deeru\Desktop\Ian\Android Stuff\adt-bundle-windows-x86_64-20130514\adt-bundle-windows-x86_64-20130514\sdk/tools/proguard/proguard-android.txt
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
#   public *;0
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify

-verbose
-optimizations !code/simplification/arithmetic,!field
-ignorewarnings
-keep class cn.pedant.** { *; }
-dontwarn org.apache.lang.**
-dontwarn org.apache.lang.**
-dontwarn com.samsung.**
-dontwarn org.bouncycastle.**
-dontwarn org.slf4j.**
-dontwarn com.special.**
-dontwarn com.squareup.**
-dontwarn com.ogaclejapa.**
-dontwarn com.google.android.**
-dontwarn org.xml.**
-dontwarn javax.xml.**
-dontwarn org.apache.directory.**
-dontwarn com.github.afollestad
