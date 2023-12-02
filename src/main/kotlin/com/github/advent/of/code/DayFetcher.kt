package com.github.advent.of.code

import org.reflections.Reflections

object DayFetcher {
    fun fetch(day: Int): Executable {
        val reflections = Reflections("com.github.advent.of.code.days")
        val allClasses = reflections.getSubTypesOf(Executable::class.java)

        val dayClass =
            allClasses.first { clazz ->
                clazz.name.contains("$day".padStart(2, '0'))
            }

        val dayConstructor = dayClass.getConstructor()

        return dayConstructor.newInstance()
    }
}
