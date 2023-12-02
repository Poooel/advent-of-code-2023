package com.github.advent.of.code

import fuel.Fuel
import fuel.method
import kotlinx.coroutines.runBlocking
import java.io.File

object InputFetcher {
    private val cookies = File("cookies").readText()

    fun fetch(day: Int): String {
        return runBlocking {
            Fuel.method(
                url = "https://adventofcode.com/2023/day/$day/input",
                method = "GET",
                headers = mapOf("Cookie" to "session=$cookies"),
            ).body.trimEnd()
        }
    }
}
