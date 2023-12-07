package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day07CamelCards : Executable {
    override fun executePartOne(input: String): Any {
        val handAndBids = parseHandAndBids(input)
        val sortedHands = handAndBids.sortedWith(handComparator)
        return computeWinnings(sortedHands)
    }

    override fun executePartTwo(input: String): Any {
        jokers = true
        val handAndBids = parseHandAndBids(input)
        val sortedHands = handAndBids.sortedWith(handComparator)
        return computeWinnings(sortedHands)
    }

    private fun parseHandAndBids(input: String): List<HandAndBid> {
        return input.lines().map { rawHandAndBid ->
            val (hand, rawBid) = rawHandAndBid.split(" ")
            HandAndBid(hand, rawBid.toInt())
        }
    }

    private val handComparator =
        Comparator<HandAndBid> { hand1, hand2 ->
            val hand1 = hand1.hand
            val hand2 = hand2.hand

            if (getType(hand1) == getType(hand2)) {
                if (firstHandIsStrongest(hand1, hand2)) {
                    1
                } else {
                    -1
                }
            } else {
                if (getType(hand1) < getType(hand2)) {
                    1
                } else {
                    -1
                }
            }
        }

    private fun getType(hand: String): HandType {
        val cardCount = countCards(hand, jokers)

        if (cardCount.size == 1) { // Five of a kind
            return HandType.FiveOfAKind
        } else if (cardCount.size == 2) { // Four of a kind & full house
            if (cardCount.minOf { it.value } == 1) { // Four of a kind
                return HandType.FourOfAKind
            } else { // Full house
                return HandType.FullHouse
            }
        } else if (cardCount.size == 3) { // Three of a kind & two pair
            if (cardCount.maxOf { it.value } == 3) { // Three of a kind
                return HandType.ThreeOfAKind
            } else { // two pair
                return HandType.TwoPair
            }
        } else if (cardCount.size == 4) { // One pair
            return HandType.OnePair
        } else { // High card
            return HandType.HighCard
        }
    }

    private fun firstHandIsStrongest(
        hand1: String,
        hand2: String,
    ): Boolean {
        val cardStrength = if (jokers) cardStrengthWithJokers else cardStrength

        for (i in hand1.indices) {
            if (cardStrength.indexOf(hand1[i]) < cardStrength.indexOf(hand2[i])) {
                // println("[$hand1] is stronger than [$hand2] - (${hand1[i]}>${hand2[i]})")
                return true
            } else if (cardStrength.indexOf(hand1[i]) > cardStrength.indexOf(hand2[i])) {
                // println("[$hand2] is stronger than [$hand1] - (${hand2[i]}>${hand1[i]})")
                return false
            }
        }
        throw IllegalArgumentException("This shouldn't be reached - hand1: {$hand1} - hand2: {$hand2}")
    }

    private fun computeWinnings(hands: List<HandAndBid>): Int {
        var winnings = 0

        for (i in hands.indices) {
            winnings += hands[i].bid * (i + 1)
        }

        return winnings
    }

    private fun countCards(
        hand: String,
        jokers: Boolean,
    ): Map<Char, Int> {
        val cardCount = hand.groupingBy { it }.eachCount()

        if (jokers) {
            if (cardCount['J'] != null) {
                val mutableCardCount = cardCount.toMutableMap()
                val joker = mutableCardCount.remove('J')!!
                if (mutableCardCount.isEmpty()) return cardCount
                val mostCards = mutableCardCount.maxBy { it.value }.key
                mutableCardCount[mostCards] = mutableCardCount[mostCards]!! + joker
                println("Original cards: [$cardCount] - New cards: [$mutableCardCount]")
                return mutableCardCount
            } else {
                return cardCount
            }
        } else {
            return cardCount
        }
    }

    data class HandAndBid(val hand: String, val bid: Int)

    private val cardStrength = "AKQJT98765432"
    private val cardStrengthWithJokers = "AKQT98765432J"
    private var jokers = false

    enum class HandType {
        FiveOfAKind,
        FourOfAKind,
        FullHouse,
        ThreeOfAKind,
        TwoPair,
        OnePair,
        HighCard,
    }
}
