# Remove source file name from stack traces
-renamesourcefileattribute SourceFile
# Preserve the line number information
-keepattributes SourceFile,LineNumberTable

-keep class com.cmtelematics.** { *; }
-keep class com.amazonaws.** { *; }
