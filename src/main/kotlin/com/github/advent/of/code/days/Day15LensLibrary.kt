package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day15LensLibrary : Executable {
    override fun executePartOne(input: String): Any {
        val steps = parseSteps(input)
        return steps.sumOf { step -> hash(step.label) }
    }

    override fun executePartTwo(input: String): Any {
        val steps = parseSteps(input)
        val line = processHASHMAP(steps)
        return computeFocusingPower(line)
    }

    private fun hash(string: String): Int {
        var currentValue = 0

        for (char in string) {
            currentValue += char.code
            currentValue *= 17
            currentValue %= 256
        }

        return currentValue
    }

    private fun parseSteps(input: String): List<Step> {
        return input.trim().split(",").map { step ->
            val match = Regex("\\w+").find(step)!!
            val label = match.value
            val operationBlock = step.substring(match.range.last + 1 until step.length)
            val operation = operationBlock.first()

            if (operation == '=') {
                val focalLength = operationBlock.last().digitToInt()
                Step(label, operation, focalLength)
            } else {
                Step(label, operation, null)
            }
        }
    }

    private fun processHASHMAP(steps: List<Step>): List<Box> {
        val line = List(256) { Box(mutableListOf()) }

        steps.forEach { step ->
            val numberOfTheBox = hash(step.label)
            val box = line[numberOfTheBox]

            if (step.operation == '-') {
                box.lenses.removeIf { lens -> lens.label == step.label }
            } else {
                val lens = Lens(step.label, step.focalLength!!)

                if (box.lenses.any { boxLens -> boxLens.label == lens.label }) {
                    box.lenses.replaceAll { boxLens -> if (boxLens.label == lens.label) lens else boxLens }
                } else {
                    box.lenses.add(lens)
                }
            }
        }

        return line
    }

    private fun computeFocusingPower(line: List<Box>): Int {
        var focusingPower = 0

        line.forEachIndexed { boxNumber, box ->
            box.lenses.forEachIndexed { slotNumber, lens ->
                focusingPower += (1 + boxNumber) * (1 + slotNumber) * lens.focalLength
            }
        }

        return focusingPower
    }

    data class Step(val label: String, val operation: Char, val focalLength: Int?)

    data class Lens(val label: String, val focalLength: Int)

    data class Box(val lenses: MutableList<Lens>)
}
