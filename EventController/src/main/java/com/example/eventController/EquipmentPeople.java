package com.example.eventController;

public class EquipmentPeople {
    Equipment equipment;
    int count;

    public EquipmentPeople(Equipment equipment, int count) {
        this.equipment = equipment;
        this.count = count;
    }

    @Override
    public String toString() {
        DatabaseAdapter.getDBConnection();
        Event res = DatabaseAdapter.getEvent(equipment.id_event);
        if(null!=res)
            return res.title+": "+ equipment.title+" ("+count+")";
        else return equipment.title+" ("+count+")";
    }
}
