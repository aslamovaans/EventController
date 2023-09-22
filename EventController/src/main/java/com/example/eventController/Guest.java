package com.example.eventController;


import java.util.ArrayList;
import java.util.List;

public class Guest{
    public int id_event;
    int id;
    String description;
    int count_need;
    int count_exist;
    List<People> peoples = new ArrayList<>();

    public Guest(int id, String description, int count_need, int count_exist) {
        this.id = id;
        this.description = description;
        this.count_need = count_need;
        this.count_exist = count_exist;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }


    @Override
    public String toString() {
        DatabaseAdapter.getDBConnection();
        Event res = DatabaseAdapter.getEvent(id_event);
        if(null!=res)
            return res.title+": "+ description+" ("+(count_need-count_exist)+")";
        else return description+" ("+(count_need-count_exist)+")";
    }
}
