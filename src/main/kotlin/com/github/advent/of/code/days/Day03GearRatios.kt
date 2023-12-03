package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day03GearRatios : Executable {
    override fun executePartOne(input: String): Any {
        val engineSchematic = input.lines()
        val numbers = findAllNumbers(engineSchematic)
        val symbols = findAllSymbols(engineSchematic)
        val partNumbers = findAllPartNumbers(symbols, numbers)
        return partNumbers.sumOf { partNumber -> partNumber.value }
    }

    override fun executePartTwo(input: String): Any {
        val engineSchematic = input.lines()
        val numbers = findAllNumbers(engineSchematic)
        val symbols = findAllSymbols(engineSchematic)
        val partNumbers = findAllPartNumbers(symbols, numbers)
        val gears = findAllGears(symbols, partNumbers)
        val gearRatios = findAllGearRatios(gears, partNumbers)
        return gearRatios.sum()
    }

    private fun findAllNumbers(engineSchematic: List<String>): List<Number> {
        return engineSchematic.mapIndexed { y, schematicLine ->
            val matches = Regex("\\d+").findAll(schematicLine)
            matches.map { match ->
                Number(match.value.toInt(), match.range, y)
            }.toList()
        }.flatten()
    }

    private fun findAllSymbols(engineSchematic: List<String>): List<Symbol> {
        return engineSchematic.mapIndexed { y, schematicLine ->
            schematicLine.mapIndexedNotNull { x, potentialSymbol ->
                if (potentialSymbol.isDigit() || potentialSymbol == '.') {
                    null
                } else {
                    Symbol(potentialSymbol, x, y)
                }
            }
        }.flatten()
    }

    private fun findAllPartNumbers(
        symbols: List<Symbol>,
        numbers: List<Number>,
    ): List<Number> {
        return numbers.filter { number ->
            symbols.any { symbol ->
                isNumberAdjacentToSymbol(symbol, number)
            }
        }
    }

    private fun isNumberAdjacentToSymbol(
        symbol: Symbol,
        number: Number,
    ): Boolean {
        if (symbol.x - 1 in number.x && symbol.y == number.y) return true // left
        if (symbol.x + 1 in number.x && symbol.y == number.y) return true // right
        if (symbol.x in number.x && symbol.y - 1 == number.y) return true // up
        if (symbol.x in number.x && symbol.y + 1 == number.y) return true // down
        if (symbol.x - 1 in number.x && symbol.y - 1 == number.y) return true // left up
        if (symbol.x - 1 in number.x && symbol.y + 1 == number.y) return true // left down
        if (symbol.x + 1 in number.x && symbol.y - 1 == number.y) return true // right up
        if (symbol.x + 1 in number.x && symbol.y + 1 == number.y) return true // right down

        return false
    }

    private fun findAllGears(
        symbols: List<Symbol>,
        partNumbers: List<Number>,
    ): List<Symbol> {
        val potentialGears = symbols.filter { symbol -> symbol.value == '*' }

        return potentialGears.filter { potentialGear ->
            val adjacentPartNumbers =
                partNumbers.filter { partNumber ->
                    isNumberAdjacentToSymbol(potentialGear, partNumber)
                }

            adjacentPartNumbers.size == 2
        }
    }

    private fun findAllGearRatios(
        gears: List<Symbol>,
        partNumbers: List<Number>,
    ): List<Int> {
        return gears.map { gear ->
            val adjacentPartNumbers =
                partNumbers.filter { partNumber ->
                    isNumberAdjacentToSymbol(gear, partNumber)
                }

            adjacentPartNumbers.first().value * adjacentPartNumbers.last().value
        }
    }

    data class Number(val value: Int, val x: IntRange, val y: Int)

    data class Symbol(val value: Char, val x: Int, val y: Int)
}
