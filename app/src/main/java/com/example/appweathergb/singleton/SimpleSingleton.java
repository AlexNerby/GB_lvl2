package com.example.appweathergb.singleton;

import java.util.ArrayList;
import java.util.List;

public class SimpleSingleton {
    //TODO переписать SimpleSingleton на MyApp

    private static final Object object = new Object();

    private static SimpleSingleton instance;

    private List<String> msg;

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = new ArrayList<>(msg);
    }

    private SimpleSingleton() {
    }

    public static SimpleSingleton getInstance() {
        synchronized (object) {
            if (instance == null) {
                instance = new SimpleSingleton();
            }
            return instance;
        }
    }
}
