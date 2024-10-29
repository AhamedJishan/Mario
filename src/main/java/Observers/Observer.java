package Observers;

import Engine.GameObject;
import Observers.Events.Event;

public interface Observer
{
    void OnNotify(GameObject gameObject, Event event);
}
