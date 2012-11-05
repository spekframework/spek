package spek

import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

object Lock {
    val lock = ReentrantLock()

    fun lock() {
        lock.lock()
    }

    fun unlock() {
        lock.unlock()
    }
}

public class SimpleEventAggregator: EventAggregator {


    val subscribers: HashMap<String, ArrayList<EventHandler>> = hashMap<String, ArrayList<EventHandler>>()

    override fun subscribe(eventName: String, obj: EventHandler) {

        Lock.lock()
        try {
            if (subscribers[eventName] == null) {
                subscribers[eventName] = ArrayList<EventHandler>()
            }
            subscribers[eventName]?.add(obj)
        } finally {
            Lock.unlock()
        }
    }

    override fun unSubscribe(eventName: String, obj: EventHandler) {
        Lock.lock()
        try {
            subscribers[eventName]?.remove(obj)
        } finally {
            Lock.unlock()
        }
    }

    override fun publish(data: Any) {

        Lock.lock()
        try {
            var eventSubscribers = subscribers[data.javaClass.getSimpleName()]
            if (eventSubscribers != null) {
                for (obj in eventSubscribers?.iterator()) {
                    obj.handle(data)
                }
            }
        } finally {
            Lock.unlock()
        }
    }

}