# Keep specific classes or packages
-keep class com.example.MyClass { *; }

# Don't obfuscate classes in specific packages
-keep class com.example.utils.** { *; }

# Remove unused code
-dontwarn com.example.**

# Optimization settings
-optimizationpasses 5

# Enable or disable specific ProGuard features
-dontoptimize
