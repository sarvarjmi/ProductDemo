# Optimization flags for maximum reduction
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-repackageclasses ''
-overloadaggressively

# Retrofit 3 & OkHttp
-keepattributes Signature, InnerClasses, AnnotationDefault
-keep class retrofit2.** { *; }
-keep @retrofit2.http.* interface * { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# Moshi - Generated Adapters & Models
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keep class * { @com.squareup.moshi.Json *; }
-keep class * { @com.squareup.moshi.JsonQualifier *; }
# Keep generated adapters
-keep class *JsonAdapter { *; }
-keep class com.productdemo.data.remote.dto.** { *; }

# Room - Entities & DAOs
-keepclassmembers class com.productdemo.data.local.entity.** { *; }
-keepclassmembers interface com.productdemo.data.local.dao.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.**

# Hilt - Internal components
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-keep class * extends androidx.lifecycle.ViewModel
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keep class * extends dagger.hilt.internal.ComponentEntryPoint

# Kotlin Serialization (for Navigation 3 Keys)
-keepattributes *Annotation*, EnclosingMethod, InnerClasses
-keep class kotlinx.serialization.json.** { *; }
-keep class com.productdemo.ui.navigation.Destination** { *; }
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# General Obfuscation
-keepattributes SourceFile,LineNumberTable
-keep class com.productdemo.MainActivity { *; }
-keep class com.productdemo.ProductApplication { *; }
