# PixivLib
A library for pixiv, uses kotlin-coroutines

## Core
Core code for any target or any instance, does not contains any engine implementation.

## Ktor
A engine implementation for JVM platform, uses Jetbrains Ktor library to do network requesting.

# Usage
1. Import core library.
2. Import any engine library.
3. Use Pixiv() function to create a instance of the specified engine.

Example:
```Kotlin
val pixiv = Pixiv<EngineName>()
pixiv.getRank()
```
There is a full example in ktor/test using Ktor engine.
