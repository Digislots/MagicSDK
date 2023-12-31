# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
# Keep public classes and methods
#-keep public class com.magicbid.app.MagicBidSdk {
#    public *;
#}
#
## Keep all classes in a package
#
## Keep specific method
#-keepclassmembers class com.magicbid.app.App {
#     public *;
#}
#-keepclassmembers class com.magicbid.app.ApiUtilities
##-keepclassmembers class com.magicbid.app.App {
##    void myMethod();
##}
# Keep the entry point to your library (change 'com.example.yourlibrary.YourLibrary' to your actual entry point)
-keep public class com.magicbid.app.App {
    public *;
}

# Add any other specific rules as needed
