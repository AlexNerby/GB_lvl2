package com.example.appweathergb.observers;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    //TODO: реализовать
    private List<MyDialogFragment> observers;

    public void Publisher() {
        observers = new ArrayList<>();
    }

    public void subscribe(MyDialogFragment observer) {
        observers.add(observer);
    }

    public void unsubscribe(MyDialogFragment observer) {
        observers.remove(observer);
    }

    public void sendMsg(MyDialogFragment observer, String msg) {
        observer.updateText(msg);
    }
}
