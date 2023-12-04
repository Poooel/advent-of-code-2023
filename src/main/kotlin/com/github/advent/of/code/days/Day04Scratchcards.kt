package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import kotlin.math.pow

class Day04Scratchcards : Executable {
    override fun executePartOne(input: String): Any {
        return input.lines().sumOf { scratchCard ->
            val removedGame = scratchCard.replace(Regex("(Card)( )+\\d{1,3}(: )"), "")
            val (rawWinningNumbers, rawNumbers) = removedGame.split("|")
            val winningNumbers = rawWinningNumbers.split(" ").filter { it.trim().isNotEmpty() }.map { it.trim().toInt() }
            val numbers = rawNumbers.split(" ").filter { it.trim().isNotEmpty() }.map { it.trim().toInt() }
            val goodNumbers = winningNumbers.intersect(numbers)
            val points = if (goodNumbers.isNotEmpty()) 2.toDouble().pow(goodNumbers.size - 1).toInt() else 0
            points
        }
    }

    override fun executePartTwo(input: String): Any {
        val matchingNumbers =
            input.lines().map { scratchCard ->
                val removedGame = scratchCard.replace(Regex("(Card)( )+\\d{1,3}(: )"), "")
                val (rawWinningNumbers, rawNumbers) = removedGame.split("|")
                val winningNumbers = rawWinningNumbers.split(" ").filter { it.trim().isNotEmpty() }.map { it.trim().toInt() }
                val numbers = rawNumbers.split(" ").filter { it.trim().isNotEmpty() }.map { it.trim().toInt() }
                val goodNumbers = winningNumbers.intersect(numbers)
                goodNumbers.size
            }

        val counts = matchingNumbers.map { it to 1 }.toMutableList()

        for (i in 0..<counts.size) {
            for (j in 1..counts[i].first) {
                counts[j + i] = counts[j + i].first to counts[j + i].second + counts[i].second
            }
        }

        return counts.sumOf { it.second }
    }
}
