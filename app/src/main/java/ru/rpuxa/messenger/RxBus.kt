package ru.rpuxa.messenger

import rx.Observable
import rx.subjects.PublishSubject

class RxBus {
    private val _bus = PublishSubject.create<Any>()
    val bus: Observable<Any> get() = _bus

    fun send(o: Any) {
        _bus.onNext(o)
    }

    object NewMessage
}