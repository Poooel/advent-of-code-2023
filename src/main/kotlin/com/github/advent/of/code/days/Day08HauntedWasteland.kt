package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day08HauntedWasteland : Executable {
    override fun executePartOne(input: String): Any {
        val instructions = parseInstructions(input)
        val map = parseMap(input)
        return followInstructions(instructions, map)
    }

    override fun executePartTwo(input: String): Any {
        val instructions = parseInstructions(input)
        val map = parseMap(input)
        return followInstructionsAsGhosts(instructions, map)
    }

    private fun parseInstructions(input: String): String {
        return input.lines().first()
    }

    private fun parseMap(input: String): Map<String, Node> {
        return input.lines()
            .drop(2)
            .associate { node ->
                val matches = Regex("\\w{3}").findAll(node)
                val (name, left, right) = matches.map { it.value }.toList()
                name to Node(name, left, right)
            }
    }

    private fun followInstructions(instructions: String, map: Map<String, Node>): Int {
        val endNode = map["ZZZ"]!!
        var currentNode = map["AAA"]!!
        var steps = 0
        var instructionsPointer = 0

        while (currentNode != endNode) {
            steps++

            if (instructionsPointer == instructions.length) {
                instructionsPointer = 0
            }

            if (instructions[instructionsPointer] == 'L') {
                currentNode = map[currentNode.left]!!
            } else if (instructions[instructionsPointer] == 'R') {
                currentNode = map[currentNode.right]!!
            }

            instructionsPointer++
        }

        return steps
    }

    private fun followInstructionsAsGhosts(instructions: String, map: Map<String, Node>): Long {
        val currentNodes = map.filter { node -> node.key.endsWith('A') }.values

        val steps = currentNodes.map { currentNodeStart ->
            var currentNode = currentNodeStart
            var steps = 0L
            var instructionsPointer = 0

            while (!currentNode.name.endsWith('Z')) {
                steps++

                if (instructionsPointer == instructions.length) {
                    instructionsPointer = 0
                }

                if (instructions[instructionsPointer] == 'L') {
                    currentNode = map[currentNode.left]!!
                } else if (instructions[instructionsPointer] == 'R') {
                    currentNode = map[currentNode.right]!!
                }

                instructionsPointer++
            }

            steps
        }

        return lcm(steps)
    }

    private fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    private fun lcm(a: Long, b: Long): Long {
        return a / gcd(a, b) * b
    }

    private fun lcm(numbers: List<Long>): Long {
        return numbers.reduce { acc, num -> lcm(acc, num) }
    }

    data class Node (val name: String, val left: String, val right: String)
}
