package com.example.eventController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Peoples {
    ObservableList<People> people = FXCollections.observableArrayList();
    public Peoples()
    {
        DatabaseAdapter.getDBConnection();
    }
    public List<People> getPeople() {
        return people;
    }
    public void addPeople(People people)
    {
        this.people.add(DatabaseAdapter.getUser(DatabaseAdapter.addUser(people)));
    }
    public void removeRequest(People people)
    {
        this.people.remove(people);
    }
    public People getPeople(int id)
    {
        return people.get(id);
    }
}
