package ru.rpuxa.messenger

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rpuxa.messenger.model.server.Server

class FriendUnitTest {

    val server = Server.create("http://localhost")

    @Test
    fun test() {
        runBlocking {
            // rpuxa  JkOovaVAajMFInHKCSrMBfeUylsNpAQQ

            // test   YJwkkkPHBOBYDAMLOMXSOMkAFBdEmSbu

            val actions = server.getActions("JkOovaVAajMFInHKCSrMBfeUylsNpAQQ", 5)
            assertEquals(0, actions.error)
            println(actions)
        }
    }
}