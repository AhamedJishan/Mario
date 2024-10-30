package Observers.Events;

public class Event
{
    public EventType type;

    public Event(EventType eventType)
    {
        this.type = eventType;
    }

    public Event()
    {
        this.type = EventType.UserEvent;
    }
}
