package ru.rpuxa.messenger

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.rpuxa.messenger.model.server.Server

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        runBlocking {
          val answer =  Server.create("http:// 176.57.217.44").login("12388", "221238")
            println(answer.error)
        }
    }
}
