package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day06WaitForIt : Executable {
    override fun executePartOne(input: String): Any {
        val races = parseRaces(input)

        return races.map { race -> winRace(race) }
            .map { holdTimes -> holdTimes.size }
            .reduce { accumulator, i -> accumulator * i }
    }

    override fun executePartTwo(input: String): Any {
        val race = parseRace(input)
        val holdTimes = winRace(race)
        return holdTimes.size
    }

    private fun parseRaces(input: String): List<Race> {
        val (rawTimes, rawDistances) = input.lines()
        val times =
            rawTimes.removePrefix("Time:")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toLong() }
        val distances =
            rawDistances.removePrefix("Distance:")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toLong() }

        return times.mapIndexed { i, time ->
            Race(time, distances[i])
        }
    }

    private fun parseRace(input: String): Race {
        val (rawTime, rawDistance) = input.lines()
        val time =
            rawTime.removePrefix("Time:")
                .replace(" ", "")
                .toLong()
        val distance =
            rawDistance.removePrefix("Distance:")
                .replace(" ", "")
                .toLong()
        return Race(time, distance)
    }

    private fun winRace(race: Race): List<Long> {
        val holdTimes = mutableListOf<Long>()

        for (i in 1 until race.time) {
            val distance = i * (race.time - i)
            if (distance > race.distance) {
                holdTimes.add(i)
            }
        }

        return holdTimes
    }

    data class Race(val time: Long, val distance: Long)
}
