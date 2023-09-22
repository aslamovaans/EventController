package com.example.eventController;

public class GuestPeople {
    Guest guest;

    public GuestPeople(Guest guest) {
        this.guest = guest;
    }

    @Override
    public String toString() {
        DatabaseAdapter.getDBConnection();
        Event res = DatabaseAdapter.getEvent(guest.id_event);
        if(null!=res)
            return res.title+": "+ guest.description;
        else return guest.description;
    }
}
