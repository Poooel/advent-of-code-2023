package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day09MirageMaintenance : Executable {
    override fun executePartOne(input: String): Any {
        return input.lines().sumOf { history ->
            val historyValues = toIntList(history)
            val differences = findDifferencesDownToZero(historyValues)
            val extrapolatedValues = extrapolateFromZeroDifferences(differences, false)
            extrapolatedValues.last()
        }
    }

    override fun executePartTwo(input: String): Any {
        return input.lines().sumOf { history ->
            val historyValues = toIntList(history)
            val differences = findDifferencesDownToZero(historyValues)
            val extrapolatedValues = extrapolateFromZeroDifferences(differences, true)
            extrapolatedValues.last()
        }
    }

    private fun findDifferencesDownToZero(sequence: List<Int>): List<List<Int>> {
        val sequences = mutableListOf(sequence)

        while (!allZeroes(sequences.last())) {
            val currentSequence = sequences.last()
            val derivedSequence = mutableListOf<Int>()

            for (i in 0..currentSequence.size - 2) {
                derivedSequence += currentSequence[i + 1] - currentSequence[i]
            }

            sequences += derivedSequence
        }

        return sequences
    }

    private fun extrapolateFromZeroDifferences(
        differences: List<List<Int>>,
        reversedHistory: Boolean,
    ): List<Int> {
        var reversedDifferences = differences.reversed().map { it.toMutableList() }

        if (reversedHistory) {
            reversedDifferences = reversedDifferences.map { it.reversed().toMutableList() }
        }

        reversedDifferences.first().add(0)

        for (i in 0..differences.size - 2) {
            val lastDifference = reversedDifferences[i].last()
            val lastValue = reversedDifferences[i + 1].last()

            if (reversedHistory) {
                reversedDifferences[i + 1].add(lastValue - lastDifference)
            } else {
                reversedDifferences[i + 1].add(lastValue + lastDifference)
            }
        }

        return reversedDifferences.last()
    }

    private fun allZeroes(input: List<Int>): Boolean {
        return input.all { it == 0 }
    }

    private fun toIntList(input: String): List<Int> {
        return input.split(" ").map { it.toInt() }
    }
}
