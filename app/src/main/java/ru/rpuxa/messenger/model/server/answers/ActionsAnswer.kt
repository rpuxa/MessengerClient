package ru.rpuxa.messenger.model.server.answers

data class ActionsAnswer(val actions: List<Action>) : Answer()

data class Action(val id: Int, val type: Int)

enum class ActionType(val id: Int) {
    FRIEND_REQUEST_RECEIVED(1),
    FRIEND_REQUEST_ACCEPTED(2),
    MESSAGE(3),
}
