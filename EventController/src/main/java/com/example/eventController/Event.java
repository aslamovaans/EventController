package com.example.eventController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Event {
    LocalDateTime dateTime;
    String place;
    String title;
    int id;
    List<People> guests = new ArrayList<>();
    List<People> equipment = new ArrayList<>();
    public Event(RequestBuilder requestBuilder)
    {
        this.title=requestBuilder.title;
        this.dateTime=requestBuilder.dateTime;
        this.place=requestBuilder.place;
        this.id = requestBuilder.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String toString()
    {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))+": "+title+" ("+place+")";
    }

    public static class RequestBuilder{
        LocalDateTime dateTime;
        String place;
        String title;
        int id;
        List<Guest> guests = new ArrayList<>();
        List<Equipment> equipment = new ArrayList<>();

        public RequestBuilder(LocalDateTime dateTime, String title,String place)
        {
         this.dateTime = dateTime;
         this.title = title;
         this.place = place;
        }
        public RequestBuilder setID(int id)
        {
            this.id = id;
            return this;
        }
        public RequestBuilder setGuest(List<Guest> guests)
        {
            this.guests = guests;
            return this;
        }
        public RequestBuilder setEquipment(List<Equipment> equipment)
        {
            this.equipment = equipment;
            return this;
        }
        public Event build()
        {
            return new Event(this);
        }

    }
}
