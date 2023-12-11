package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import com.google.common.collect.Sets
import kotlin.math.abs

class Day11CosmicExpansion : Executable {
    override fun executePartOne(input: String): Any {
        val expansionFactor = 2
        val universe = input.lines()
        val universeExpansion = findWhereUniverseExpands(universe)
        val galaxies = findAllGalaxies(universe)
        val galaxyPairs = findAllPairs(galaxies)
        val distances = computeManhattanDistanceAccountingForUniverseExpansion(galaxyPairs, expansionFactor, universeExpansion)
        return distances.sum()
    }

    override fun executePartTwo(input: String): Any {
        val expansionFactor = 1_000_000
        val universe = input.lines()
        val universeExpansion = findWhereUniverseExpands(universe)
        val galaxies = findAllGalaxies(universe)
        val galaxyPairs = findAllPairs(galaxies)
        val distances = computeManhattanDistanceAccountingForUniverseExpansion(galaxyPairs, expansionFactor, universeExpansion)
        return distances.sum()
    }

    private fun findWhereUniverseExpands(universe: List<String>): Pair<List<Int>, List<Int>> {
        val yExpand = mutableListOf<Int>()

        for (i in universe.indices) {
            if (containsNoGalaxies(universe[i])) {
                yExpand += i
            }
        }

        val xExpand = mutableListOf<Int>()

        for (i in universe.first().indices) {
            if (universe.indices.all { universe[it][i] == '.' }) {
                xExpand += i
            }
        }

        return xExpand to yExpand
    }

    private fun computeManhattanDistanceAccountingForUniverseExpansion(
        galaxyPairs: List<Pair<Galaxy, Galaxy>>,
        expansionFactor: Int,
        expansion: Pair<List<Int>, List<Int>>,
    ): List<Long> {
        return galaxyPairs.map { galaxyPair ->
            val (galaxyA, galaxyB) = galaxyPair
            val (expansionX, expansionY) = expansion
            val (galaxyAX, galaxyBX) = adjustGalaxyPointToExpansion(galaxyA.x, galaxyB.x, expansionX, expansionFactor)
            val (galaxyAY, galaxyBY) = adjustGalaxyPointToExpansion(galaxyA.y, galaxyB.y, expansionY, expansionFactor)

            manhattanDistance(Galaxy(galaxyAX, galaxyAY), Galaxy(galaxyBX, galaxyBY))
        }
    }

    private fun adjustGalaxyPointToExpansion(
        galaxyPoint1: Long,
        galaxyPoint2: Long,
        expansion: List<Int>,
        expansionFactor: Int,
    ): Pair<Long, Long> {
        var galaxyA = galaxyPoint1
        var galaxyB = galaxyPoint2

        if (galaxyA < galaxyB) {
            val galaxyRange = galaxyA..galaxyB
            if (expansion.any { it in galaxyRange }) {
                val expansions = expansion.count { it in galaxyRange }
                galaxyB += expansions * (expansionFactor - 1)
            }
        } else if (galaxyA > galaxyB) {
            val galaxyRange = galaxyB..galaxyA
            if (expansion.any { it in galaxyRange }) {
                val expansions = expansion.count { it in galaxyRange }
                galaxyA += expansions * (expansionFactor - 1)
            }
        }

        return Pair(galaxyA, galaxyB)
    }

    private fun containsNoGalaxies(line: String): Boolean {
        return line.all { it == '.' }
    }

    private fun findAllGalaxies(universe: List<String>): List<Galaxy> {
        val galaxies = mutableListOf<Galaxy>()

        for (i in universe.indices) {
            for (j in universe[i].indices) {
                if (universe[i][j] == '#') {
                    galaxies.add(Galaxy(j.toLong(), i.toLong()))
                }
            }
        }

        return galaxies
    }

    private fun findAllPairs(galaxies: List<Galaxy>): List<Pair<Galaxy, Galaxy>> {
        val allPairs = Sets.combinations(galaxies.toSet(), 2)
        return allPairs.map { pair ->
            val (a, b) = pair.toList()
            Pair(a, b)
        }
    }

    private fun manhattanDistance(
        a: Galaxy,
        b: Galaxy,
    ): Long {
        return abs(a.x - b.x) + abs(a.y - b.y)
    }

    data class Galaxy(val x: Long, val y: Long)
}
