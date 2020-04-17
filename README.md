# PixivLib
A library for pixiv, uses kotlin-coroutines

## Core
Core code for any target or any instance, does not contains any engine implementation.

### Dependencies
    org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3
    com.alibaba:fastjson:1.2.62
    org.jsoup:jsoup:1.12.1

## JVM
A engine implementation for JVM platform, uses native URLConnectin.

### Dependencies
    org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3

## Ktor
A engine implementation for JVM platform (Expect to be multiplatform), depends on Jetbrains Ktor library.

### Dependencies
    io.ktor:ktor-client-core-jvm:1.3.0
    io.ktor:ktor-client-apache:1.3.0

# Usage
1. Import core library and its dependencies.
2. Import any engine library and its dependencies.
3. Use Pixiv() function to create a instance depends on the specified engine.

Example:
```Kotlin
val pixiv = Pixiv<JVM>()
pixiv.getRank()
```
There is a full example at another repository, named PixivUtil.
