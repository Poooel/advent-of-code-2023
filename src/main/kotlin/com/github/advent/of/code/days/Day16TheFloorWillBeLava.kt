package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day16TheFloorWillBeLava : Executable {
    override fun executePartOne(input: String): Any {
        val contraption = input.lines()
        val startingBeam = Beam(Direction.EAST, -1, 0)
        val beamPath = simulateLight(contraption, startingBeam)
        return countEnergizedTiles(beamPath)
    }

    override fun executePartTwo(input: String): Any {
        val contraption = input.lines()

        val allStartingWestPosition = contraption.indices.map { Beam(Direction.EAST, -1, it) }
        val allStartingEastPosition = contraption.indices.map { Beam(Direction.WEST, contraption.first().length, it) }
        val allStartingNorthPosition = contraption.first().indices.map { Beam(Direction.SOUTH, it, -1) }
        val allStartingSouthPosition = contraption.first().indices.map { Beam(Direction.NORTH, it, contraption.size) }

        return (
            allStartingNorthPosition +
                allStartingWestPosition +
                allStartingSouthPosition +
                allStartingEastPosition
        ).maxOf { startingBeam ->
            val beamPath = simulateLight(contraption, startingBeam)
            countEnergizedTiles(beamPath)
        }
    }

    private fun simulateLight(
        contraption: List<String>,
        startingBeam: Beam,
    ): Set<Beam> {
        val visitedCells = mutableSetOf<Beam>()
        val beams = mutableListOf(startingBeam)

        while (beams.isNotEmpty()) {
            val currentBeam = beams.removeFirst()
            val movedBeam = currentBeam.move()
            if (movedBeam.y in contraption.indices && movedBeam.x in contraption.first().indices) {
                if (!visitedCells.add(movedBeam)) {
                    continue
                }

                when (val encounter = contraption[movedBeam.y][movedBeam.x]) {
                    '.' -> {
                        beams.add(movedBeam)
                    }
                    '-' -> {
                        if (movedBeam.direction == Direction.EAST || movedBeam.direction == Direction.WEST) {
                            beams.add(movedBeam)
                        } else {
                            beams.add(Beam(Direction.EAST, movedBeam.x, movedBeam.y))
                            beams.add(Beam(Direction.WEST, movedBeam.x, movedBeam.y))
                        }
                    }
                    '|' -> {
                        if (movedBeam.direction == Direction.NORTH || movedBeam.direction == Direction.SOUTH) {
                            beams.add(movedBeam)
                        } else {
                            beams.add(Beam(Direction.NORTH, movedBeam.x, movedBeam.y))
                            beams.add(Beam(Direction.SOUTH, movedBeam.x, movedBeam.y))
                        }
                    }
                    '/', '\\' -> {
                        beams.add(Beam(movedBeam.direction.rotate(encounter), movedBeam.x, movedBeam.y))
                    }
                }
            }
        }

        return visitedCells
    }

    private fun countEnergizedTiles(beamPath: Set<Beam>): Int {
        return beamPath.groupingBy { beam -> beam.x to beam.y }.eachCount().size
    }

    enum class Direction(val x: Int, val y: Int) {
        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0),
        ;

        fun rotate(mirrorType: Char): Direction {
            return if (mirrorType == '/') {
                when (this) {
                    NORTH -> EAST
                    EAST -> NORTH
                    SOUTH -> WEST
                    WEST -> SOUTH
                }
            } else {
                when (this) {
                    NORTH -> WEST
                    EAST -> SOUTH
                    SOUTH -> EAST
                    WEST -> NORTH
                }
            }
        }
    }

    data class Beam(val direction: Direction, val x: Int, val y: Int) {
        fun move(): Beam {
            return Beam(direction, x + direction.x, y + direction.y)
        }

        fun move(direction: Direction): Beam {
            return Beam(direction, x + direction.x, y + direction.y)
        }
    }
}
