name := "Events Manager"

version := "1.0.0"

scalaVersion := "2.12.1"

//enablePlugins(JniNative)

sourceDirectory in nativeCompile := sourceDirectory.value / "main" / "native"

target in javah := sourceDirectory.value / "main" / "native"
