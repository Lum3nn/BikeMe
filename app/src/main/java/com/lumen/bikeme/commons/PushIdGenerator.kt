package com.lumen.bikeme.commons

import java.util.*


object PushIdGenerator {
    private const val PUSH_CHARS =
        "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"
    private const val MIN_PUSH_CHAR = '-'
    private const val MAX_PUSH_CHAR = 'z'
    private const val MAX_KEY_LEN = 786
    private val randGen = Random()
    private var lastPushTime = 0L
    private val lastRandChars = IntArray(12)

    const val MAX_KEY_NAME = "[MAX_KEY]"
    const val MIN_KEY_NAME = "[MIN_NAME]"

    @Synchronized
    fun generatePushChildName(currentTime: Long): String {
        var now = currentTime
        val duplicateTime = now == lastPushTime
        lastPushTime = now
        val timeStampChars = CharArray(8)
        val result = StringBuilder(20)
        for (i in 7 downTo 0) {
            timeStampChars[i] = PUSH_CHARS[(now % 64).toInt()]
            now /= 64
        }

        result.append(timeStampChars)
        if (!duplicateTime) {
            for (i in 0..11) {
                lastRandChars[i] = randGen.nextInt(64)
            }
        } else {
            incrementArray()
        }
        for (i in 0..11) {
            result.append(PUSH_CHARS[lastRandChars[i]])
        }

        return result.toString()
    }

    // `key` is assumed to be non-empty.
    fun predecessor(key: String): String {
        val num: Int? = tryParseInt(key)
        if (num != null) {
            return if (num == Int.MIN_VALUE) {
                MIN_KEY_NAME
            } else (num - 1).toString()
        }
        val next = StringBuilder(key)
        if (next[next.length - 1] == MIN_PUSH_CHAR) {
            return if (next.length == 1) {
                Int.MAX_VALUE.toString()
            } else next.substring(0, next.length - 1)
            // If the last character is the smallest possible character, then the next
            // smallest string is the prefix of `key` without it.
        }
        // Replace the last character with it's immediate predecessor, and fill the
        // suffix of the key with MAX_PUSH_CHAR. This is the lexicographically largest
        // possible key smaller than `key`.
        next.setCharAt(
            next.length - 1,
            PUSH_CHARS[PUSH_CHARS.indexOf(next[next.length - 1]) - 1]
        )
        return next.append(
            String(CharArray(MAX_KEY_LEN - next.length)).replace("\u0000", "" + MAX_PUSH_CHAR)
        )
            .toString()
    }

    fun successor(key: String): String {
        val num: Int? = tryParseInt(key)
        if (num != null) {
            return if (num == Int.MAX_VALUE) {
                // See https://firebase.google.com/docs/database/web/lists-of-data#data-order
                MIN_PUSH_CHAR.toString()
            } else (num + 1).toString()
        }
        val next = StringBuilder(key)
        if (next.length < MAX_KEY_LEN) {
            // If this key doesn't have all possible character slots filled,
            // the lexicographical successor is the same string with the smallest
            // possible character appended to the end.
            next.append(MIN_PUSH_CHAR)
            return next.toString()
        }
        var i = next.length - 1
        while (i >= 0 && next[i] == MAX_PUSH_CHAR) {
            i--
        }

        // `successor` was called on the lexicographically largest possible key, so return the
        // maxName, which sorts larger than all keys.
        if (i == -1) {
            return MAX_KEY_NAME
        }

        // `i` now points to the last character in `key` that is < MAX_PUSH_CHAR,
        // where all characters in `key.substring(i + 1, key.length)` are MAX_PUSH_CHAR.
        // The lexicographical successor is attained by increment this character, and
        // returning the prefix of `key` up to and including it.
        val source = next[i]
        val sourcePlusOne = PUSH_CHARS[PUSH_CHARS.indexOf(source) + 1]
        next.replace(i, i + 1, sourcePlusOne.toString())
        return next.substring(0, i + 1)
    }

    private fun incrementArray() {
        for (i in 11 downTo 0) {
            if (lastRandChars[i] != 63) {
                lastRandChars[i] = lastRandChars[i] + 1
                return
            }
            lastRandChars[i] = 0
        }
    }

    // NOTE: We could use Ints.tryParse from guava, but I don't feel like pulling in guava (~2mb) for
    // that small purpose.
    private fun tryParseInt(num: String): Int? {
        if (num.length > 11 || num.isEmpty()) {
            return null
        }
        var i = 0
        var negative = false
        if (num[0] == '-') {
            if (num.length == 1) {
                return null
            }
            negative = true
            i = 1
        }
        // long to prevent overflow
        var number: Long = 0
        while (i < num.length) {
            val c = num[i]
            if (c < '0' || c > '9') {
                return null
            }
            number = number * 10 + (c - '0')
            i++
        }
        return if (negative) {
            if (-number < Int.MIN_VALUE) {
                null
            } else {
                (-number).toInt()
            }
        } else {
            if (number > Int.MAX_VALUE) {
                null
            } else number.toInt()
        }
    }
}
