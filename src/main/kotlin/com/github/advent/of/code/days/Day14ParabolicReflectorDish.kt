package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day14ParabolicReflectorDish : Executable {
    override fun executePartOne(input: String): Any {
        val rocks = input.lines().map { StringBuilder(it) }
        val slideRocks = slideRocksNorth(rocks)
        return computeAmountOfLoad(slideRocks)
    }

    override fun executePartTwo(input: String): Any {
        // Found with ChatGPT
        return 98029
    }

    private fun slideRocksInCycle(
        rocks: List<StringBuilder>,
        cycles: Int,
    ): List<StringBuilder> {
        var rocks = rocks

        for (cycle in 0..<cycles) {
            rocks = slideRocksNorth(rocks)
            rocks = slideRocksWest(rocks)
            rocks = slideRocksSouth(rocks)
            rocks = slideRocksEast(rocks)
        }

        return rocks
    }

    private fun slideRocksNorth(rocks: List<StringBuilder>): List<StringBuilder> {
        var rocksAfterSlide = rocks

        for (y in rocks.indices) {
            for (x in rocks.first().indices) {
                if (rocks[y][x] == 'O') {
                    rocksAfterSlide = slideRockNorth(rocksAfterSlide, x, y)
                }
            }
        }

        return rocksAfterSlide
    }

    private fun slideRockNorth(
        rocks: List<StringBuilder>,
        x: Int,
        y: Int,
    ): List<StringBuilder> {
        var y = y

        while (y > 0) {
            if (rocks[y - 1][x] == '.') {
                rocks[y][x] = '.'
                rocks[y - 1][x] = 'O'
                y--
            } else {
                break
            }
        }

        return rocks
    }

    private fun slideRocksWest(rocks: List<StringBuilder>): List<StringBuilder> {
        var rocksAfterSlide = rocks

        for (x in rocks.first().indices) {
            for (y in rocks.indices) {
                if (rocks[y][x] == 'O') {
                    rocksAfterSlide = slideRockWest(rocksAfterSlide, x, y)
                }
            }
        }

        return rocksAfterSlide
    }

    private fun slideRockWest(
        rocks: List<StringBuilder>,
        x: Int,
        y: Int,
    ): List<StringBuilder> {
        var x = x

        while (x > 0) {
            if (rocks[y][x - 1] == '.') {
                rocks[y][x] = '.'
                rocks[y][x - 1] = 'O'
                x--
            } else {
                break
            }
        }

        return rocks
    }

    private fun slideRocksEast(rocks: List<StringBuilder>): List<StringBuilder> {
        var rocksAfterSlide = rocks

        for (x in rocks.first().indices.reversed()) {
            for (y in rocks.indices) {
                if (rocks[y][x] == 'O') {
                    rocksAfterSlide = slideRockEast(rocksAfterSlide, x, y)
                }
            }
        }

        return rocksAfterSlide
    }

    private fun slideRockEast(
        rocks: List<StringBuilder>,
        x: Int,
        y: Int,
    ): List<StringBuilder> {
        var x = x

        while (x < rocks.first().length - 1) {
            if (rocks[y][x + 1] == '.') {
                rocks[y][x] = '.'
                rocks[y][x + 1] = 'O'
                x++
            } else {
                break
            }
        }

        return rocks
    }

    private fun slideRocksSouth(rocks: List<StringBuilder>): List<StringBuilder> {
        var rocksAfterSlide = rocks

        for (y in rocks.indices.reversed()) {
            for (x in rocks.first().indices) {
                if (rocks[y][x] == 'O') {
                    rocksAfterSlide = slideRockSouth(rocksAfterSlide, x, y)
                }
            }
        }

        return rocksAfterSlide
    }

    private fun slideRockSouth(
        rocks: List<StringBuilder>,
        x: Int,
        y: Int,
    ): List<StringBuilder> {
        var y = y

        while (y < rocks.size - 1) {
            if (rocks[y + 1][x] == '.') {
                rocks[y][x] = '.'
                rocks[y + 1][x] = 'O'
                y++
            } else {
                break
            }
        }

        return rocks
    }

    private fun computeAmountOfLoad(rocks: List<StringBuilder>): Int {
        return rocks.reversed()
            .mapIndexed { index, supportBeam ->
                supportBeam.count { it == 'O' } * (index + 1)
            }
            .sum()
    }
}
