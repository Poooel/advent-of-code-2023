package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day05IfYouGiveASeedAFertilizer : Executable {
    override fun executePartOne(input: String): Any {
        val almanach = parseAlmanach(input, false)
        val locations = computeLocationFromAlmanach(almanach)
        return locations.min()
    }

    override fun executePartTwo(input: String): Any {
        val almanach = parseAlmanach(input, true)
        val locations = computeLocationFromAlmanach(almanach)
        return locations.min()
    }

    private fun parseAlmanach(
        input: String,
        newSeeds: Boolean,
    ): Almanach {
        val splitInput = input.split("\n\n")

        val seeds = if (newSeeds) parseNewSeeds(splitInput[0]) else parseSeeds(splitInput[0])
        val seedToSoil = parseRanges(removeFirstLine(splitInput[1]))
        val soilToFertilizer = parseRanges(removeFirstLine(splitInput[2]))
        val fertilizerToWater = parseRanges(removeFirstLine(splitInput[3]))
        val waterToLight = parseRanges(removeFirstLine(splitInput[4]))
        val lightToTemperature = parseRanges(removeFirstLine(splitInput[5]))
        val temperatureToHumidity = parseRanges(removeFirstLine(splitInput[6]))
        val humidityToLocation = parseRanges(removeFirstLine(splitInput[7]))

        return Almanach(
            seeds,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation,
        )
    }

    private fun parseSeeds(input: String): List<LongRange> {
        return input.removePrefix("seeds: ")
            .split(" ")
            .map { it.toLong() }
            .map { it..it }
    }

    private fun parseNewSeeds(input: String): List<LongRange> {
        return input.removePrefix("seeds: ")
            .split(" ")
            .map { it.toLong() }
            .chunked(2)
            .map { chunk ->
                val (start, length) = chunk
                start..<start + length
            }
    }

    private fun removeFirstLine(input: String): String {
        return input.lines()
            .drop(1)
            .joinToString("\n")
    }

    private fun parseRanges(input: String): List<Range> {
        return input.lines().map { rangeString -> parseRange(rangeString) }
    }

    private fun parseRange(input: String): Range {
        val (destinationRangeStart, sourceRangeStart, rangeLength) = input.split(" ").map { it.toLong() }
        return Range(
            destinationRangeStart..<destinationRangeStart + rangeLength,
            sourceRangeStart..<sourceRangeStart + rangeLength,
            rangeLength,
        )
    }

    private fun computeLocationFromAlmanach(almanach: Almanach): List<Long> {
        return almanach.seeds.map { seedRange ->
            val locations = mutableListOf<Long>()

            for (seed in seedRange) {
                var temporarySeed = seed

                temporarySeed = convertFromSourceToDestination(almanach.seedToSoil, temporarySeed)
                temporarySeed = convertFromSourceToDestination(almanach.soilToFertilizer, temporarySeed)
                temporarySeed = convertFromSourceToDestination(almanach.fertilizerToWater, temporarySeed)
                temporarySeed = convertFromSourceToDestination(almanach.waterToLight, temporarySeed)
                temporarySeed = convertFromSourceToDestination(almanach.lightToTemperature, temporarySeed)
                temporarySeed = convertFromSourceToDestination(almanach.temperatureToHumidity, temporarySeed)
                temporarySeed = convertFromSourceToDestination(almanach.humidityToLocation, temporarySeed)

                locations.add(temporarySeed)
            }

            locations
        }.flatten()
    }

    private fun convertFromSourceToDestination(
        ranges: List<Range>,
        value: Long,
    ): Long {
        for (range in ranges) {
            if (value in range.source) {
                val offset = value - range.source.first
                return range.destination.first + offset
            }
        }

        return value
    }

    data class Almanach(
        val seeds: List<LongRange>,
        val seedToSoil: List<Range>,
        val soilToFertilizer: List<Range>,
        val fertilizerToWater: List<Range>,
        val waterToLight: List<Range>,
        val lightToTemperature: List<Range>,
        val temperatureToHumidity: List<Range>,
        val humidityToLocation: List<Range>,
    )

    data class Range(
        val destination: LongRange,
        val source: LongRange,
        val length: Long,
    )
}
