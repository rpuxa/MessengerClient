package ru.rpuxa.messenger

import org.junit.Test
import kotlin.math.abs


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val toLoad = hashSetOf(1, 2, 3, 4, 8, 9 ,10, 11)
        val sorted = toLoad.sorted()
        var first = sorted.first()
        var last = sorted.first()
        var bestLength = -1
        var best: Pair<Int, Int>? = null
        sorted.forEach {
            if (abs(last - it) > 1) {
                first = it
            }
            last = it

            val length = it - first
            if (length > bestLength) {
                bestLength = length
                best = first to it
            }
        }

        println(best)
    }
}
