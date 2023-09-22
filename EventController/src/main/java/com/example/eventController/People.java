package com.example.eventController;

import org.w3c.dom.events.Event;

import java.util.ArrayList;
import java.util.List;

public class People {
    String name;
    String password;
    String phone = "";
    int id;
    List<Event> events = new ArrayList<>();
    public People(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public People setID(int id)
    {
        this.id = id;
        return this;
    }
    public String toString()
    {
        return name;
    }

    public People setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPhone() {
        return phone;
    }
}
