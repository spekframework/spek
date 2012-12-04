package spek

public trait EventAggregator {
    fun subscribe(eventName: String, obj: EventHandler)
    fun unSubscribe(eventName: String, obj: EventHandler)
    fun publish(data: Any)
}
