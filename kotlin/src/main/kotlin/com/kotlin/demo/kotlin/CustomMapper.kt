package com.kotlin.demo.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class CustomMapper {
}

data class Entity(
    val id: Int,
    val name: String,
    val age: Int
)

data class DataClass(
    val id: Int,
    val name: String,
    val age: Int
)

fun <T : Any, R : Any> map(entity: T, targetClass: KClass<R>): R {
    val constructor = targetClass.primaryConstructor
        ?: throw IllegalArgumentException("Target class must have a primary constructor")

    val paramMap = mutableMapOf<String, Any?>()
    for (prop in entity::class.memberProperties) {
        @Suppress("UNCHECKED_CAST")
        val value = (prop as KProperty1<T, *>).get(entity)
        paramMap[prop.name] = value
    }

    return constructor.callBy(constructor.parameters.associateWith { paramMap[it.name] })
}

fun main() {
    val entity = Entity(id = 1, name = "John John", age = 30)
    val dataClass = map(entity, DataClass::class)

    println(dataClass)
}
