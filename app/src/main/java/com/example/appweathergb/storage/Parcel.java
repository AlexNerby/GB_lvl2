package com.example.appweathergb.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parcel implements Serializable {

    //TODO: найти применение для практики

    private List<String> List;

    public List<String> getList() {
        return List;
    }

    public void setList(List<String> List) {
        this.List = List;
    }
}
