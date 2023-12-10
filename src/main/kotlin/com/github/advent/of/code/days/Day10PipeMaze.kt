package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.util.PriorityQueue
import kotlin.math.abs

class Day10PipeMaze : Executable {
    override fun executePartOne(input: String): Any {
        val graph = input.lines()
        val start = findStart(graph)
        val distances = dijkstra(start, graph)
        return distances.maxOf { it.value }
    }

    override fun executePartTwo(input: String): Any {
        val graph = input.lines()
        val start = findStart(graph)
        val distances = dijkstra(start, graph)
        val loop = getLoop(distances.keys.toList(), graph)
        val area = computeAreaOfPolygonUsingShoelaceFormula(loop)
        return picksTheorem(loop.size, area)
    }

    private fun findStart(graph: List<String>): Node {
        val startRow = graph.indexOfFirst { it.contains('S') }
        val startCol = graph[startRow].indexOf('S')
        val startNode = Node(graph[startRow][startCol], startCol, startRow)
        return startNode
    }

    private fun dijkstra(
        source: Node,
        graph: List<String>,
    ): Map<Node, Int> {
        val visited = mutableSetOf<Node>()
        val distances = mutableMapOf<Node, Int>()
        val previous = mutableMapOf<Node, Node>()
        val queue = PriorityQueue<Node>(compareBy { distances[it] })

        queue.add(source)
        distances[source] = 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            visited.add(current)

            val neighbors = getNeighbors(current, graph)
            for (neighbor in neighbors) {
                if (neighbor !in visited) {
                    val newDistance = distances[current]!! + 1
                    if (newDistance < distances.getOrDefault(neighbor, Int.MAX_VALUE)) {
                        distances[neighbor] = newDistance
                        previous[neighbor] = current
                        queue.add(neighbor)
                    }
                }
            }
        }

        return distances.toMap()
    }

    private fun getNeighbors(
        current: Node,
        graph: List<String>,
    ): List<Node> {
        val neighbors = mutableListOf<Node>()

        when (current.pipe) {
            '|' -> {
                val (x1, y1) = current.x to current.y - 1 // up
                val (x2, y2) = current.x to current.y + 1 // down
                neighbors.add(Node(graph[y1][x1], x1, y1))
                neighbors.add(Node(graph[y2][x2], x2, y2))
            }
            '-' -> {
                val (x1, y1) = current.x - 1 to current.y // left
                val (x2, y2) = current.x + 1 to current.y // right
                neighbors.add(Node(graph[y1][x1], x1, y1))
                neighbors.add(Node(graph[y2][x2], x2, y2))
            }
            'L' -> {
                val (x1, y1) = current.x to current.y - 1 // up
                val (x2, y2) = current.x + 1 to current.y // right
                neighbors.add(Node(graph[y1][x1], x1, y1))
                neighbors.add(Node(graph[y2][x2], x2, y2))
            }
            'J' -> {
                val (x1, y1) = current.x to current.y - 1 // up
                val (x2, y2) = current.x - 1 to current.y // left
                neighbors.add(Node(graph[y1][x1], x1, y1))
                neighbors.add(Node(graph[y2][x2], x2, y2))
            }
            '7' -> {
                val (x1, y1) = current.x - 1 to current.y // left
                val (x2, y2) = current.x to current.y + 1 // down
                neighbors.add(Node(graph[y1][x1], x1, y1))
                neighbors.add(Node(graph[y2][x2], x2, y2))
            }
            'F' -> {
                val (x1, y1) = current.x + 1 to current.y // right
                val (x2, y2) = current.x to current.y + 1 // down
                neighbors.add(Node(graph[y1][x1], x1, y1))
                neighbors.add(Node(graph[y2][x2], x2, y2))
            }
            'S' -> {
                val (x1, y1) = current.x to current.y - 1 // up
                val (x2, y2) = current.x to current.y + 1 // down
                val (x3, y3) = current.x - 1 to current.y // left
                val (x4, y4) = current.x + 1 to current.y // right

                try {
                    if (graph[y1][x1] in "F7|") { // up
                        neighbors.add(Node(graph[y1][x1], x1, y1))
                    }
                } catch (_: Exception) {
                }

                try {
                    if (graph[y2][x2] in "JL|") { // down
                        neighbors.add(Node(graph[y2][x2], x2, y2))
                    }
                } catch (_: Exception) {
                }

                try {
                    if (graph[y3][x3] in "FL-") { // left
                        neighbors.add(Node(graph[y3][x3], x3, y3))
                    }
                } catch (_: Exception) {
                }

                try {
                    if (graph[y4][x4] in "7J-") { // right
                        neighbors.add(Node(graph[y4][x4], x4, y4))
                    }
                } catch (_: Exception) {
                }
            }
        }

        return neighbors
    }

    private fun getLoop(
        nodes: List<Node>,
        graph: List<String>,
    ): List<Node> {
        val startingNode = nodes.find { it.pipe == 'S' }!!
        val loop = mutableListOf(startingNode)
        var current = getNeighbors(startingNode, graph).first()

        while (current != startingNode) {
            loop.add(current)
            val currentNeighbors = getNeighbors(current, graph)
            val filteredNeighbors = currentNeighbors.filter { it !in loop }

            current =
                if (filteredNeighbors.isEmpty()) {
                    startingNode
                } else {
                    filteredNeighbors.first()
                }
        }

        return loop
    }

    private fun computeAreaOfPolygonUsingShoelaceFormula(nodes: List<Node>): Int {
        var area = 0

        (nodes + nodes.first()).windowed(2).forEach { window ->
            val (node1, node2) = window
            area += (node1.y + node2.y) * (node1.x - node2.x)
        }

        return abs(area)
    }

    private fun picksTheorem(
        loopSize: Int,
        area: Int,
    ): Int {
        return (area / 2) - (loopSize / 2 - 1)
    }

    data class Node(val pipe: Char, val x: Int, val y: Int)
}
