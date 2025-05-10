package main

import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

fun main() {
    print("".replace("^/+".toRegex(), "").split("/"))
}