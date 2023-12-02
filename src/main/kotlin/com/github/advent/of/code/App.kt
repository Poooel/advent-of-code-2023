package com.github.advent.of.code

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.check
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.mordant.animation.textAnimation
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.runInterruptible

class App : CliktCommand() {
    private val day by argument(
        help = "The number of the day you wish to execute",
    ).int().check("Value must be between 1 & 25") { it in 1..25 }
    private val part by argument(help = "The part of the day you wish to execute").int().check("Value must be 1 or 2") { it in 1..2 }

    private val errorStyle = (TextStyles.bold + TextColors.brightRed)
    private val promptStyle = (TextStyles.bold + TextStyles.underline + TextColors.brightBlue)
    private val resultStyle = (TextStyles.bold + TextColors.brightMagenta)

    private val terminal = Terminal()

    override fun run() {
        val input =
            try {
                InputFetcher.fetch(day)
            } catch (error: Error) {
                terminal.println(errorStyle("Unable to fetch input for day #$day."))
                return
            }

        val dayToExecute =
            try {
                DayFetcher.fetch(day)
            } catch (error: NoSuchElementException) {
                terminal.println(errorStyle("Unable to find day #$day."))
                return
            }

        val result = dayToExecute.execute(part, input)

        terminal.println("${promptStyle("The answer for day #$day part #$part is:")} ${resultStyle("$result")}")
    }
}

fun main(args: Array<String>) = App().main(args)
