package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day01Trebuchet : Executable {
    override fun executePartOne(input: String): Any {
        return input.lines().sumOf { line ->
            val trimmedLine = line.replace(Regex("\\D"), "")
            val number = trimmedLine.first().toString() + trimmedLine.last().toString()
            number.toInt()
        }
    }

    override fun executePartTwo(input: String): Any {
        val digits =
            mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3,
                "four" to 4,
                "five" to 5,
                "six" to 6,
                "seven" to 7,
                "eight" to 8,
                "nine" to 9,
            )

        return input.lines().sumOf { line ->
            val digitsFound = mutableListOf<Int>()
            var offset = 0

            for (i in line.indices) {
                for (j in offset..<line.length) {
                    val substring = line.substring(offset..j)

                    for (digit in digits) {
                        if (substring == digit.key) {
                            digitsFound.add(digit.value)
                            offset = j
                            break
                        } else if (substring.length == 1 && substring[0].isDigit()) {
                            digitsFound.add(substring[0].digitToInt())
                            offset = j
                            break
                        }
                    }
                }
                offset++
            }

            (digitsFound.first().toString() + digitsFound.last().toString()).toInt()
        }
    }
}
