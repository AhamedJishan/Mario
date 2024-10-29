package Observers;

import Engine.GameObject;
import Observers.Events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem
{
    private static List<Observer> observers = new ArrayList<>();

    public static void AddObserver(Observer observer)
    {
        observers.add(observer);
    }

    public static void Notify(GameObject gameObject, Event event)
    {
        for (Observer observer: observers)
        {
            observer.OnNotify(gameObject, event);
        }
    }
}
