package com.github.advent.of.code

interface Executable {
    fun execute(
        part: Int,
        input: String,
    ): Any {
        return if (part == 1) {
            executePartOne(input)
        } else {
            executePartTwo(input)
        }
    }

    fun executePartOne(input: String): Any

    fun executePartTwo(input: String): Any
}
