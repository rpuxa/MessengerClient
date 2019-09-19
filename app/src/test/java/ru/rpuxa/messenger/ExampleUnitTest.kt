package ru.rpuxa.messenger

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rpuxa.messenger.model.server.Server
import java.io.File
import okhttp3.RequestBody
import java.io.FileInputStream


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val image = File("image.png")
        assertEquals(image.exists(), true)
        runBlocking {

            val answer =
                Server.create("http://localhost").setIcon("BukidUVNtYPdiirUIydvFjOlPAWtrrjF", part)
            println(answer.url)
            assertEquals(answer.error, 0)
        }
    }
}
