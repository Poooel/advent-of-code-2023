package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day02CubeConundrum : Executable {
    override fun executePartOne(input: String): Any {
        val redCubes = 12
        val greenCubes = 13
        val blueCubes = 14

        return input.lines().sumOf { line ->
            val removedGame = line.replace(Regex("(Game )\\d{1,2}(: )"), "")
            val sets = removedGame.split(";")

            val gameIdRegex = Regex("(Game )\\d{1,2}")
            val gameId = gameIdRegex.find(line)!!.value.split(" ").last().toInt()

            sets.forEach { set ->
                val reveals = set.split(",")
                reveals.forEach { reveal ->
                    val (number, color) = reveal.trim().split(" ")
                    when (color) {
                        "red" -> if (number.toInt() > redCubes) return@sumOf 0
                        "green" -> if (number.toInt() > greenCubes) return@sumOf 0
                        "blue" -> if (number.toInt() > blueCubes) return@sumOf 0
                    }
                }
            }

            gameId
        }
    }

    override fun executePartTwo(input: String): Any {
        return input.lines().sumOf { line ->
            val removedGame = line.replace(Regex("(Game )\\d{1,2}(: )"), "")
            val sets = removedGame.split(";")

            var maxReds = 0
            var maxGreens = 0
            var maxBlues = 0

            sets.forEach { set ->
                val reveals = set.split(",")
                reveals.forEach { reveal ->
                    val (number, color) = reveal.trim().split(" ")
                    when (color) {
                        "red" -> if (number.toInt() > maxReds) maxReds = number.toInt()
                        "green" -> if (number.toInt() > maxGreens) maxGreens = number.toInt()
                        "blue" -> if (number.toInt() > maxBlues) maxBlues = number.toInt()
                    }
                }
            }

            maxReds * maxGreens * maxBlues
        }
    }
}
